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

package in.arcadelabs.lifesteal.database.profile;

import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.database.DatabaseHandler;
import in.arcadelabs.lifesteal.utils.LifeState;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ProfileManager {

  @Getter(AccessLevel.NONE)
  private final DatabaseHandler databaseHandler = LifeStealPlugin.getLifeSteal().getDatabaseHandler();

  private final Map<UUID, Profile> profileCache = new HashMap<>();
  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();

  public boolean hasProfile(UUID uuid) throws SQLException {
    return databaseHandler.getProfileDao().idExists(uuid);
  }

  public Profile getProfile(UUID uuid) throws SQLException {
    Profile profile = new Profile(uuid);

    if (this.hasProfile(uuid)) {
      lifeSteal.getLogger().log(Logger.Level.INFO, lifeSteal.getMiniMessage().deserialize(
              "<gradient:#f58c67:#f10f5d>Loading " + Bukkit.getPlayer(uuid).getName() + "'s profile ...</gradient>"));
      return databaseHandler.getProfileDao().queryForId(uuid);
    } else {
      lifeSteal.getLogger().log(Logger.Level.INFO, lifeSteal.getMiniMessage().deserialize(
              "<gradient:#f58c67:#f10f5d>Profile not found for " + uuid + " ! Creating...</gradient>"));
      this.saveProfile(profile);
    }
    return profile;
  }

  public void saveProfile(Profile profile) throws SQLException {
    if (this.hasProfile(profile.getUniqueID())) {
      databaseHandler.getProfileDao().update(profile);
      databaseHandler.getProfileDao().refresh(profile);
    } else {
      profile.setLifeState(LifeState.LIVING);
      if (Bukkit.getPlayer(profile.getUniqueID()).hasPlayedBefore())
        profile.setCurrentHearts((int) lifeSteal.getUtils().getPlayerHearts(Bukkit.getPlayer(profile.getUniqueID())));
      else profile.setCurrentHearts(lifeSteal.getConfig().getInt("DefaultHearts"));
      profile.setLostHearts(0);
      profile.setNormalHearts(0);
      profile.setBlessedHearts(0);
      profile.setCursedHearts(0);
      if (Bukkit.getPlayer(profile.getUniqueID()).hasPlayedBefore())
        profile.setPeakHeartsReached((int) lifeSteal.getUtils().getPlayerHearts(Bukkit.getPlayer(profile.getUniqueID())));
      else  profile.setPeakHeartsReached(lifeSteal.getConfig().getInt("DefaultHearts"));
      databaseHandler.getProfileDao().create(profile);
      databaseHandler.getProfileDao().refresh(profile);
    }
  }
}