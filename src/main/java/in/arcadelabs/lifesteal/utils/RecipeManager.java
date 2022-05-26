package in.arcadelabs.lifesteal.utils;

import in.arcadelabs.libs.boostedyaml.YamlDocument;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static in.arcadelabs.lifesteal.hooks.LifeStealHook.configUtils;

public class RecipeManager {

  final YamlDocument config = configUtils.getConfig();

  {
    ItemStack heart = new ItemStack(Material.valueOf(config.getString("HeartRecipe.Properties.ItemType")));
    ItemMeta heartMeta = heart.getItemMeta();
    assert heartMeta != null;
    heartMeta.setDisplayName(config.getString("HeartRecipe.Properties.Name"));
    heartMeta.setLore(config.getStringList("HeartRecipe.Properties.Lore"));
    if (config.getBoolean("HeartRecipe.Properties.CustomModel")) {
      heartMeta.setCustomModelData(config.getInt("HeartRecipe.Properties.ModelData"));
    }
    heart.setItemMeta(heartMeta);
  }
}
