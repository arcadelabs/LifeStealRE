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

import in.arcadelabs.labaide.cooldown.CooldownManager;
import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.labaide.namespacedkey.NamespacedKeyBuilder;
import in.arcadelabs.lifesteal.api.database.profile.IProfileManager;
import in.arcadelabs.lifesteal.api.database.profile.IStatisticsManager;
import in.arcadelabs.lifesteal.api.database.profile.ProfileManager;
import in.arcadelabs.lifesteal.api.hearts.HeartItemManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface LifeStealAPI {

  /**
   * Gets profile manager.
   *
   * @return {@link ProfileManager} instance
   */
  @NonNull IProfileManager getProfileManager();

  /**
   * Gets statistics manager.
   *
   * @return {@link IStatisticsManager} instance
   */
  @NonNull IStatisticsManager getStatisticsManager();

  /**
   * Gets heart item manager.
   *
   * @return {@link HeartItemManager} instance
   */
  @NonNull HeartItemManager getHeartItemManager();

  /**
   * Gets heart recipe manager.
   *
   * @return {@link ShapedRecipe} instance of defualt heart recipe
   */
  @NonNull ShapedRecipe getHeartRecipeManager();

  /**
   * Gets placeholder heart.
   *
   * @return {@link ItemStack} instance of placeholder heart item
   */
  @NonNull ItemStack getPlaceholderHeart();

  /**
   * Gets interaction.
   *
   * @return {@link Interaction} instance
   */
  @NonNull Interaction getInteraction();

  /**
   * Gets logger.
   *
   * @return {@link Logger} instance
   */
  @NonNull Logger getLogger();

  /**
   * Gets namespaced key builder.
   *
   * @return {@link NamespacedKeyBuilder} instance
   */
  @NonNull NamespacedKeyBuilder getNamespacedKeyBuilder();

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
}
