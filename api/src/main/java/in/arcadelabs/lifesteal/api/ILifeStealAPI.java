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

package in.arcadelabs.lifesteal.api;

import in.arcadelabs.lifesteal.api.enums.Mode;
import in.arcadelabs.lifesteal.api.interfaces.IStatisticsManager;
import in.arcadelabs.labaide.cooldown.CooldownManager;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public interface ILifeStealAPI {

  /**
   * Gets statistics manager.
   *
   * @return {@link IStatisticsManager} instance
   */
  @NonNull IStatisticsManager getStatisticsManager();

  /**
   * Gets heart recipe manager.
   *
   * @return {@link ShapedRecipe} instance of defualt heart recipe
   */
  @NonNull ShapedRecipe getHeartRecipe();

  /**
   * Gets placeholder heart.
   *
   * @return {@link ItemStack} instance of placeholder heart item
   */
  @NonNull ItemStack getPlaceholderHeart();

  /**
   * Gets craft cooldown.
   *
   * @return {@link CooldownManager} instance of heart craft cooldown
   */
  @NonNull CooldownManager getCraftCooldown();

  /**
   * Gets consume cooldown.
   *
   * @return {@link CooldownManager} instance of heart consume cooldown
   */
  @NonNull CooldownManager getConsumeCooldown();

  /**
   * Gets withdraw manager.
   *
   * @return {@link CooldownManager} instance of heart withdraw cooldown
   */
  @NonNull CooldownManager getWithdrawCooldown();

  /**
   * Gets player hearts.
   *
   * @param player the player
   * @return the player hearts
   */
  double getPlayerHearts(final Player player);

  /**
   * Sets player hearts.
   *
   * @param player the player
   * @param hearts the hearts
   */
  void setPlayerHearts(final Player player, final double hearts);

  /**
   * Transfer hearts.
   *
   * @param victim the victim
   * @param killer the killer
   */
  void transferHearts(final Player victim, final Player killer, double hearts);

  /**
   * Give hearts.
   *
   * @param target the target
   * @param mode   the mode
   * @param amount the amount
   */
  void giveHearts(Player target, Mode mode, int amount);
}
