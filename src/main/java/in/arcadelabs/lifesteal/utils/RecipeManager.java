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

import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class RecipeManager {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final ItemStack heart;
  private final FileConfiguration config = lifeSteal.getConfig();
  private final ShapedRecipe heartRecipe;

  public RecipeManager() {
    heart = new ItemStack(Material.valueOf(config.getString("HeartRecipe.Properties.ItemType")));
    ItemMeta heartMeta = heart.getItemMeta();
    heartMeta.setDisplayName(config.getString("HeartRecipe.Properties.Name"));
    heartMeta.setLore(config.getStringList("HeartRecipe.Properties.Lore"));
    if (config.getBoolean("HeartRecipe.Properties.CustomModel")) {
      heartMeta.setCustomModelData(config.getInt("HeartRecipe.Properties.ModelData"));
    }
    heartMeta.setUnbreakable(true);
    heartMeta.getPersistentDataContainer().set(lifeSteal.getNamespacedKey(),
            PersistentDataType.STRING, "You can't spoof hearts, bozo.");
    heart.setItemMeta(heartMeta);

    heartRecipe = new ShapedRecipe(lifeSteal.getNamespacedKey(), heart);
    heartRecipe.shape("ABC", "DEF", "GHI");
    char[] recipeIngredients = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
    for (char recipeIngredient : recipeIngredients) {
      heartRecipe.setIngredient(recipeIngredient,
              Material.valueOf((String) config.get("HeartRecipe." + recipeIngredient)));
    }
  }

  public ShapedRecipe getHeartRecipe() {
    return heartRecipe;
  }

  public ItemStack getHeartItem() {
    return this.heart;
  }
}
