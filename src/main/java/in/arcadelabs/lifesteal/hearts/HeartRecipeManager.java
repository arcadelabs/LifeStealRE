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

package in.arcadelabs.lifesteal.hearts;

import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;

public class HeartRecipeManager {

  private final LifeSteal lifeSteal;
  private final ShapedRecipe heartRecipe;

  public HeartRecipeManager() {
    this.lifeSteal = LifeStealPlugin.getLifeSteal();
    this.heartRecipe = new ShapedRecipe(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_recipe"),
            this.lifeSteal.getPlaceholderHeart());
    this.heartRecipe.shape("ABC", "DEF", "GHI");
    final char[] recipeIngredients = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
    for (final char recipeIngredient : recipeIngredients) {
      this.heartRecipe.setIngredient(recipeIngredient,
              Material.valueOf((String) lifeSteal.getConfig().get("Heart.Recipe." + recipeIngredient)));
    }
  }

  /**
   * Heart recipe getter.
   *
   * @return the heart recipe
   */
  public ShapedRecipe getHeartRecipe() {
    return this.heartRecipe;
  }
}