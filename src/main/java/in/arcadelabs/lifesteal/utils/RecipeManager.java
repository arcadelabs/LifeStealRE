package in.arcadelabs.lifesteal.utils;

import in.arcadelabs.libs.boostedyaml.YamlDocument;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static in.arcadelabs.lifesteal.LifeSteal.getNamespacedKey;
import static in.arcadelabs.lifesteal.hooks.LifeStealHook.getConfigUtils;

public class RecipeManager {

  private final YamlDocument config = getConfigUtils().getConfig();
  private final ItemStack heart;

  public RecipeManager() {
    heart = new ItemStack(Material.valueOf(config.getString("HeartRecipe.Properties.ItemType")));
    ItemMeta heartMeta = heart.getItemMeta();
    heartMeta.setDisplayName(config.getString("HeartRecipe.Properties.Name"));
    heartMeta.setLore(config.getStringList("HeartRecipe.Properties.Lore"));
    if (config.getBoolean("HeartRecipe.Properties.CustomModel")) {
      heartMeta.setCustomModelData(config.getInt("HeartRecipe.Properties.ModelData"));
    }
    heartMeta.setUnbreakable(true);
    heartMeta.getPersistentDataContainer().set(getNamespacedKey(), PersistentDataType.STRING, "You can't spoof hearts, bozo.");
    heart.setItemMeta(heartMeta);

    ShapedRecipe heartRecipe = new ShapedRecipe(getNamespacedKey(), heart);
    heartRecipe.shape("ABC", "DEF", "GHI");
    char[] recipeIngredients = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
    for (int i = 0; i < recipeIngredients.length; i++) {
      heartRecipe.setIngredient(recipeIngredients[i],
              Material.valueOf((String) config.get("HeartRecipe." + i)));
    }
  }

  public ItemStack getHeartItem() {
    return this.heart;
  }
}
