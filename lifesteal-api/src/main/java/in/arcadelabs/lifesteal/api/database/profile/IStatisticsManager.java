/*
 *          LifeSteal - Yet another lifecore smp core.
 *                Copyright (C) 2022  Arcade Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General License for more details.
 *
 * You should have received a copy of the GNU General License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package in.arcadelabs.lifesteal.api.database.profile;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface IStatisticsManager {

  /**
   * Is real time statistics manager.
   *
   * @param realtimeUpdate the realtime update
   * @return the statistics manager
   */
  IStatisticsManager isRealTime(boolean realtimeUpdate);

  /**
   * Sets lifestate.
   *
   * @param uuid      the uuid
   * @param lifeState the life state
   * @return the life state
   */
  IStatisticsManager setLifeState(final UUID uuid, final LifeState lifeState);

  /**
   * Sets lifestate.
   *
   * @param player    the player
   * @param lifeState the life state
   * @return the life state
   */
  IStatisticsManager setLifeState(final Player player, final LifeState lifeState);

  /**
   * Sets current hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the current hearts
   */
  IStatisticsManager setCurrentHearts(final UUID uuid, final int hearts);

  /**
   * Sets current hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the current hearts
   */
  IStatisticsManager setCurrentHearts(final Player player, final int hearts);

  /**
   * Sets lost hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the lost hearts
   */
  IStatisticsManager setLostHearts(final UUID uuid, final int hearts);

  /**
   * Sets lost hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the lost hearts
   */
  IStatisticsManager setLostHearts(final Player player, final int hearts);

  /**
   * Sets normal hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the normal hearts
   */
  IStatisticsManager setNormalHearts(final UUID uuid, final int hearts);

  /**
   * Sets normal hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the normal hearts
   */
  IStatisticsManager setNormalHearts(final Player player, final int hearts);

  /**
   * Sets blessed hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the blessed hearts
   */
  IStatisticsManager setBlessedHearts(final UUID uuid, final int hearts);

  /**
   * Sets blessed hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the blessed hearts
   */
  IStatisticsManager setBlessedHearts(final Player player, final int hearts);

  /**
   * Sets cursed hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the cursed hearts
   */
  IStatisticsManager setCursedHearts(final UUID uuid, final int hearts);

  /**
   * Sets cursed hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the cursed hearts
   */
  IStatisticsManager setCursedHearts(final Player player, final int hearts);

  /**
   * Sets peak reached hearts.
   *
   * @param uuid   the uuid
   * @param hearts the hearts
   * @return the peak reached hearts
   */
  IStatisticsManager setPeakReachedHearts(final UUID uuid, final int hearts);

  /**
   * Sets peak reached hearts.
   *
   * @param player the player
   * @param hearts the hearts
   * @return the peak reached hearts
   */
  IStatisticsManager setPeakReachedHearts(final Player player, final int hearts);

  /**
   * Update.
   *
   * @param uuid the uuid
   */
  void update(final UUID uuid);

  /**
   * Update.
   *
   * @param player the player
   */
  void update(final Player player);

  /**
   * Gets lifestate.
   *
   * @param uuid the uuid
   * @return the life state
   */
  LifeState getLifeState(final UUID uuid);

  /**
   * Gets lifestate.
   *
   * @param player the player
   * @return the life state
   */
  LifeState getLifeState(final Player player);

  /**
   * Gets current hearts.
   *
   * @param uuid the uuid
   * @return the current hearts
   */
  int getCurrentHearts(final UUID uuid);

  /**
   * Gets current hearts.
   *
   * @param player the player
   * @return the current hearts
   */
  int getCurrentHearts(final Player player);

  /**
   * Gets lost hearts.
   *
   * @param uuid the uuid
   * @return the lost hearts
   */
  int getLostHearts(final UUID uuid);

  /**
   * Gets lost hearts.
   *
   * @param player the player
   * @return the lost hearts
   */
  int getLostHearts(final Player player);

  /**
   * Gets normal hearts.
   *
   * @param uuid the uuid
   * @return the normal hearts
   */
  int getNormalHearts(final UUID uuid);

  /**
   * Gets normal hearts.
   *
   * @param player the player
   * @return the normal hearts
   */
  int getNormalHearts(final Player player);

  /**
   * Gets blessed hearts.
   *
   * @param uuid the uuid
   * @return the blessed hearts
   */
  int getBlessedHearts(final UUID uuid);

  /**
   * Gets blessed hearts.
   *
   * @param player the player
   * @return the blessed hearts
   */
  int getBlessedHearts(final Player player);

  /**
   * Gets cursed hearts.
   *
   * @param uuid the uuid
   * @return the cursed hearts
   */
  int getCursedHearts(final UUID uuid);

  /**
   * Gets cursed hearts.
   *
   * @param player the player
   * @return the cursed hearts
   */
  int getCursedHearts(final Player player);

  /**
   * Gets peak reached hearts.
   *
   * @param uuid the uuid
   * @return the peak reached hearts
   */
  int getPeakReachedHearts(final UUID uuid);

  /**
   * Gets peak reached hearts.
   *
   * @param player the player
   * @return the peak reached hearts
   */
  int getPeakReachedHearts(final Player player);
}
