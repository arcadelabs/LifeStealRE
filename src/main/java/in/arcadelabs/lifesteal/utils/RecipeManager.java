package in.arcadelabs.lifesteal.utils;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class RecipeManager {

  private final ItemStack heart;
  private final FileConfiguration config = LifeStealPlugin.getInstance().getConfig();
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
    heartMeta.getPersistentDataContainer().set(LifeStealPlugin.getLifeSteal().getNamespacedKey(), PersistentDataType.STRING, "You can't spoof hearts, bozo.");
    heart.setItemMeta(heartMeta);

    heartRecipe = new ShapedRecipe(LifeStealPlugin.getLifeSteal().getNamespacedKey(), heart);
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
