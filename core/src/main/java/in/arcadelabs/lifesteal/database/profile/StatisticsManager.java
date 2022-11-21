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

import in.arcadelabs.lifesteal.api.enums.LifeState;
import in.arcadelabs.lifesteal.api.interfaces.IStatisticsManager;
import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

@SuppressWarnings("unused")
public class StatisticsManager implements IStatisticsManager {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private boolean realtimeUpdate;

  /**
   * Instantiates a new Statistics manager.
   */
  public StatisticsManager() {
  }

  /**
   * Is real time statistics manager.
   *
   * @param realtimeUpdate the realtime update
   * @return the statistics manager
   */
  @Override
  public StatisticsManager isRealTime(boolean realtimeUpdate) {
    this.realtimeUpdate = realtimeUpdate;
    return this;
  }

  /**
   * Sets lifestate.
   *
   * @param uuid      the uuid
   * @param lifeState the life state
   * @return the life state
   */
  @Override
  public StatisticsManager setLifeState(final UUID uuid, final LifeState lifeState) {
    this.lifeSteal.getProfileManager().getProfileCache().get(uuid).setLifeState(lifeState);
    return this;
  }

  /**
   * Sets lifestate.
   *
   * @param player    the player
   * @param lifeState the life state
   * @return the life state
   */
  @Override
  public StatisticsManager setLifeState(final Player player, final LifeState lifeState) {
    this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLifeState(lifeState);
    return this;
  }

  /**
   * Sets current hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the current hearts
   */
  @Override
  public StatisticsManager setCurrentHearts(final UUID uuid, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(uuid).setCurrentHearts(hearts);
    return this;
  }

  /**
   * Sets current hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the current hearts
   */
  @Override
  public StatisticsManager setCurrentHearts(final Player player, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setCurrentHearts(hearts);
    return this;
  }

  /**
   * Sets lost hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the lost hearts
   */
  @Override
  public StatisticsManager setLostHearts(final UUID uuid, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(uuid).setLostHearts(hearts);
    return this;
  }

  /**
   * Sets lost hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the lost hearts
   */
  @Override
  public StatisticsManager setLostHearts(final Player player, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLostHearts(hearts);
    return this;
  }

  /**
   * Sets normal hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the normal hearts
   */
  @Override
  public StatisticsManager setNormalHearts(final UUID uuid, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(uuid).setNormalHearts(hearts);
    return this;
  }

  /**
   * Sets normal hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the normal hearts
   */
  @Override
  public StatisticsManager setNormalHearts(final Player player, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setNormalHearts(hearts);
    return this;
  }

  /**
   * Sets blessed hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the blessed hearts
   */
  @Override
  public StatisticsManager setBlessedHearts(final UUID uuid, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(uuid).setBlessedHearts(hearts);
    return this;
  }

  /**
   * Sets blessed hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the blessed hearts
   */
  @Override
  public StatisticsManager setBlessedHearts(final Player player, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setBlessedHearts(hearts);
    return this;
  }

  /**
   * Sets cursed hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the cursed hearts
   */
  @Override
  public StatisticsManager setCursedHearts(final UUID uuid, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(uuid).setCursedHearts(hearts);
    return this;
  }

  /**
   * Sets cursed hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the cursed hearts
   */
  @Override
  public StatisticsManager setCursedHearts(final Player player, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setCursedHearts(hearts);
    return this;
  }

  /**
   * Sets peak reached hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the peak reached hearts
   */
  @Override
  public StatisticsManager setPeakReachedHearts(final UUID uuid, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(uuid).setPeakHeartsReached(hearts);
    return this;
  }

  /**
   * Sets peak reached hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the peak reached hearts
   */
  @Override
  public StatisticsManager setPeakReachedHearts(final Player player, final int hearts) {
    this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setPeakHeartsReached(hearts);
    return this;
  }

  /**
   * Update.
   *
   * @param uuid the uuid
   */
  @Override
  public void update(final UUID uuid) {
    if (this.realtimeUpdate) {
      this.lifeSteal.getDatabaseHandler().getHikariExecutor().execute(() -> {
        try {
          this.lifeSteal.getProfileManager().saveProfile(
                  this.lifeSteal.getProfileManager().getProfileCache().get(uuid));
        } catch (SQLException e) {
          this.lifeSteal.getLogger().log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
        }
      });
    }
  }

  /**
   * Update.
   *
   * @param player the player
   */
  @Override
  public void update(final Player player) {
    if (this.realtimeUpdate) {
      this.lifeSteal.getDatabaseHandler().getHikariExecutor().execute(() -> {
        try {
          this.lifeSteal.getProfileManager().saveProfile(
                  this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()));
        } catch (SQLException e) {
          this.lifeSteal.getLogger().log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
        }
      });
    }
  }

  /**
   * Gets lifestate.
   *
   * @param uuid the uuid
   * @return the life state
   */
  @Override
  public LifeState getLifeState(final UUID uuid) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(uuid).getLifeState();
  }

  /**
   * Gets lifestate.
   *
   * @param player the player
   * @return the life state
   */
  @Override
  public LifeState getLifeState(final Player player) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).getLifeState();
  }

  /**
   * Gets current hearts.
   *
   * @param uuid the uuid
   * @return the current hearts
   */
  @Override
  public int getCurrentHearts(final UUID uuid) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(uuid).getCurrentHearts();
  }

  /**
   * Gets current hearts.
   *
   * @param player the player
   * @return the current hearts
   */
  @Override
  public int getCurrentHearts(final Player player) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).getCurrentHearts();
  }

  /**
   * Gets lost hearts.
   *
   * @param uuid the uuid
   * @return the lost hearts
   */
  @Override
  public int getLostHearts(final UUID uuid) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(uuid).getLostHearts();
  }

  /**
   * Gets lost hearts.
   *
   * @param player the player
   * @return the lost hearts
   */
  @Override
  public int getLostHearts(final Player player) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).getLostHearts();
  }

  /**
   * Gets normal hearts.
   *
   * @param uuid the uuid
   * @return the normal hearts
   */
  @Override
  public int getNormalHearts(final UUID uuid) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(uuid).getNormalHearts();
  }

  /**
   * Gets normal hearts.
   *
   * @param player the player
   * @return the normal hearts
   */
  @Override
  public int getNormalHearts(final Player player) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).getNormalHearts();
  }

  /**
   * Gets blessed hearts.
   *
   * @param uuid the uuid
   * @return the blessed hearts
   */
  @Override
  public int getBlessedHearts(final UUID uuid) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(uuid).getBlessedHearts();
  }

  /**
   * Gets blessed hearts.
   *
   * @param player the player
   * @return the blessed hearts
   */
  @Override
  public int getBlessedHearts(final Player player) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).getBlessedHearts();
  }

  /**
   * Gets cursed hearts.
   *
   * @param uuid the uuid
   * @return the cursed hearts
   */
  @Override
  public int getCursedHearts(final UUID uuid) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(uuid).getCursedHearts();
  }

  /**
   * Gets cursed hearts.
   *
   * @param player the player
   * @return the cursed hearts
   */
  @Override
  public int getCursedHearts(final Player player) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).getCursedHearts();
  }

  /**
   * Gets peak reached hearts.
   *
   * @param uuid the uuid
   * @return the peak reached hearts
   */
  @Override
  public int getPeakReachedHearts(final UUID uuid) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(uuid).getPeakHeartsReached();
  }

  /**
   * Gets peak reached hearts.
   *
   * @param player the player
   * @return the peak reached hearts
   */
  @Override
  public int getPeakReachedHearts(final Player player) {
    return this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).getPeakHeartsReached();
  }
}
