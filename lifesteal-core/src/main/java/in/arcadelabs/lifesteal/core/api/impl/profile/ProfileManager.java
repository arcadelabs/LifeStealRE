/*
 *          LifeSteal - Yet another lifecore smp core.
 *                Copyright (C) 2022  Arcade Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package in.arcadelabs.lifesteal.core.api.impl.profile;

import in.arcadelabs.labaide.libs.boostedyaml.YamlDocument;
import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.api.database.profile.IProfileManager;
import in.arcadelabs.lifesteal.api.database.profile.LifeState;
import in.arcadelabs.lifesteal.api.database.profile.Profile;
import in.arcadelabs.lifesteal.core.api.impl.database.DatabaseHandler;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ProfileManager implements IProfileManager {

  private final DatabaseHandler databaseHandler;
  private final YamlDocument config;
  private final Logger logger;
  @Getter(AccessLevel.NONE)
  private final Map<UUID, Profile> profileCache = new HashMap<>();

  public ProfileManager(final DatabaseHandler databaseHandler, final YamlDocument config, final Logger logger) {
    this.databaseHandler = databaseHandler;
    this.config = config;
    this.logger = logger;
  }

  @Override
  public boolean hasProfile(UUID uuid) throws SQLException {
    return this.databaseHandler.getProfileDao().idExists(uuid);
  }

  @Override
  public Profile getProfile(UUID uuid) throws SQLException {
    Profile profile = new Profile(uuid);

    if (this.hasProfile(uuid)) {
      this.logger.log(Logger.Level.INFO, MiniMessage.miniMessage().deserialize(
              "<gradient:#f58c67:#f10f5d>Loading " + Bukkit.getPlayer(uuid).getName() + "'s profile ...</gradient>"));
      return this.databaseHandler.getProfileDao().queryForId(uuid);
    } else {
      this.logger.log(Logger.Level.INFO, MiniMessage.miniMessage().deserialize(
              "<gradient:#f58c67:#f10f5d>Profile not found for " + uuid + " ! Creating...</gradient>"));
      this.saveProfile(profile);
    }
    return profile;
  }

  @Override
  public void saveProfile(Profile profile) throws SQLException {
    if (this.hasProfile(profile.getUniqueID())) {
      this.databaseHandler.getProfileDao().update(profile);
      this.databaseHandler.getProfileDao().refresh(profile);
    } else {
      profile.setLifeState(LifeState.LIVING);
      if (Bukkit.getPlayer(profile.getUniqueID()).hasPlayedBefore())
        profile.setCurrentHearts((int) (Bukkit.getPlayer(profile.getUniqueID()).getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue() / 2);
      else profile.setCurrentHearts(this.config.getInt("DefaultHearts"));
      profile.setLostHearts(0);
      profile.setNormalHearts(0);
      profile.setBlessedHearts(0);
      profile.setCursedHearts(0);
      if (Bukkit.getPlayer(profile.getUniqueID()).hasPlayedBefore())
        profile.setPeakHeartsReached((int) (Bukkit.getPlayer(profile.getUniqueID()).getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue() / 2);
      else profile.setPeakHeartsReached(this.config.getInt("DefaultHearts"));
      this.databaseHandler.getProfileDao().create(profile);
      this.databaseHandler.getProfileDao().refresh(profile);
    }
  }
}
