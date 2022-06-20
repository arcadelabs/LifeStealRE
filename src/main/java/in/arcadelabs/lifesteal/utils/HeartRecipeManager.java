/*
 * LifeSteal - Yet another lifecore smp core.
 * Copyright (C) 2022  Arcade Labs
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

package in.arcadelabs.lifesteal.utils;

import in.arcadelabs.lifesteal.LifeStealManager;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class HeartRecipeManager {

  private final LifeStealManager lifeSteal = LifeStealPlugin.getLifeSteal();
  private final ItemStack heart;
  private final FileConfiguration config = lifeSteal.getConfig();
  private final ShapedRecipe heartRecipe;

  public HeartRecipeManager() {
    LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
    heartRecipe = new ShapedRecipe(new NamespacedKey(LifeStealPlugin.getInstance(), "lifesteal_heart_recipe"), lifeSteal.getPlaceholderHeart());
    heartRecipe.shape("ABC", "DEF", "GHI");
    char[] recipeIngredients = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
    for (char recipeIngredient : recipeIngredients) {
      heartRecipe.setIngredient(recipeIngredient,
              Material.valueOf((String) lifeSteal.getConfig().get("Heart.Recipe." + recipeIngredient)));
    }
  }

  public ShapedRecipe getHeartRecipe() {
    return this.heartRecipe;
  }
}