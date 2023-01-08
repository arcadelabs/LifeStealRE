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
import in.arcadelabs.lifesteal.api.enums.LifeState;
import in.arcadelabs.lifesteal.database.DatabaseManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class ProfileManager {

  @Getter(AccessLevel.NONE)
  private final LifeSteal lifeSteal;
  private final DatabaseManager databaseManager;
  private final Map<UUID, Profile> profileCache;

  public ProfileManager(LifeSteal lifeSteal) {
    this.lifeSteal = lifeSteal;
    this.databaseManager = lifeSteal.getDatabaseManager();
    this.profileCache = new ConcurrentHashMap<>();
    this.lifeSteal.getInstance().getServer().getScheduler().runTaskTimer(
        lifeSteal.getInstance(),
        new ProfileUpdateTask(this),
        0L, 20 * 60 * 5
    );
  }

  public boolean hasProfile(UUID uuid) throws SQLException {
    return this.databaseManager.getProfileDao().idExists(uuid);
  }

  public Profile getProfile(UUID uuid) throws SQLException {
    Profile profile = new Profile(uuid);

    if (this.databaseManager.getProfileDao().idExists(uuid)) {
      this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getMiniMessage().deserialize(
          "<gradient:#f58c67:#f10f5d>Loading " + Bukkit.getPlayer(uuid).getName()
              + "'s profile ...</gradient>"));
      return this.databaseManager.getProfileDao().queryForId(uuid);
    } else {
      this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getMiniMessage().deserialize(
          "<gradient:#f58c67:#f10f5d>Profile not found for " + uuid + " ! Creating...</gradient>"));
      this.saveProfile(profile);
    }
    return profile;
  }

  public void saveProfile(Profile profile) throws SQLException {
    if (this.hasProfile(profile.getUniqueID())) {
      this.databaseManager.getProfileDao().update(profile);
      this.databaseManager.getProfileDao().refresh(profile);
    } else {
      profile.setLifeState(LifeState.LIVING);
      if (Bukkit.getPlayer(profile.getUniqueID()).hasPlayedBefore()) {
        profile.setCurrentHearts((int) this.lifeSteal.getLifeStealAPI()
            .getPlayerHearts(Bukkit.getPlayer(profile.getUniqueID())));
      } else {
        profile.setCurrentHearts(this.lifeSteal.getConfig().getInt("DefaultHearts"));
      }
      profile.setLostHearts(0);
      profile.setNormalHearts(0);
      profile.setBlessedHearts(0);
      profile.setCursedHearts(0);
      if (Bukkit.getPlayer(profile.getUniqueID()).hasPlayedBefore()) {
        profile.setPeakHeartsReached((int) this.lifeSteal.getLifeStealAPI()
            .getPlayerHearts(Bukkit.getPlayer(profile.getUniqueID())));
      } else {
        profile.setPeakHeartsReached(this.lifeSteal.getConfig().getInt("DefaultHearts"));
      }
      this.databaseManager.getProfileDao().create(profile);
      this.databaseManager.getProfileDao().refresh(profile);
    }
  }

  public void operationC2D() {
    if (this.getProfileCache().isEmpty()) {
      return;
    }
    this.getDatabaseManager().getHikariExecutor()
        .execute(() -> this.getProfileCache().values().forEach(profile -> {
          try {
            this.saveProfile(profile);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }));
  }
}
