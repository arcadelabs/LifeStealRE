package in.arcadelabs.lifesteal.utils;

import in.arcadelabs.libs.boostedyaml.YamlDocument;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import static in.arcadelabs.lifesteal.LifeSteal.plugin;
import static in.arcadelabs.lifesteal.hooks.LifeStealHook.configUtils;

public class RecipeManager {

  private final YamlDocument config = configUtils.getConfig();
  private final ItemStack heart;

  public RecipeManager() {
    heart = new ItemStack(Material.valueOf(config.getString("HeartRecipe.Properties.ItemType")));
    ItemMeta heartMeta = heart.getItemMeta();
    heartMeta.setDisplayName(config.getString("HeartRecipe.Properties.Name"));
    heartMeta.setLore(config.getStringList("HeartRecipe.Properties.Lore"));
    if (config.getBoolean("HeartRecipe.Properties.CustomModel")) {
      heartMeta.setCustomModelData(config.getInt("HeartRecipe.Properties.ModelData"));
    }
    heart.setItemMeta(heartMeta);

    ShapedRecipe heartRecipe = new ShapedRecipe(new NamespacedKey(plugin, "lifesteal_heart"), heart);
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
