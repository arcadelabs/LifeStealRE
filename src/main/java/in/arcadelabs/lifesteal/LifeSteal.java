package in.arcadelabs.lifesteal;

import in.arcadelabs.lifesteal.hooks.LifeStealHook;
import in.arcadelabs.lifesteal.utils.LSUtils;
import in.arcadelabs.lifesteal.utils.RecipeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import static in.arcadelabs.lifesteal.hooks.LifeStealHook.getMessenger;

public final class LifeSteal extends JavaPlugin {

  private static LifeSteal instance;
  private static LSUtils utils;
  private static RecipeManager recipeManager;
  private static NamespacedKey namespacedKey;

  public static LifeSteal getInstance() {
    return instance;
  }

  public static LSUtils getUtils() {
    return utils;
  }

  public static RecipeManager getRecipeManager() {
    return recipeManager;
  }

  public static NamespacedKey getNamespacedKey() { return  namespacedKey; }

  @Override
  public void onEnable() {
    instance = this;
    utils = new LSUtils();
    LifeStealHook hook = new LifeStealHook();
    try {
      hook.init();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    namespacedKey = new NamespacedKey(this, "lifesteal_heart");
    recipeManager = new RecipeManager();
  }

  @Override
  public void onDisable() {
    getMessenger().closeMessenger();
    getLogger().info(ChatColor.of("#f72585") + "  ___  _  _   __   ");
    getLogger().info(ChatColor.of("#b5179e") + " / __)( \\/ ) /__\\  ");
    getLogger().info(ChatColor.of("#7209b7") + "( (__  \\  / /(__)\\ ");
    getLogger().info(ChatColor.of("#560bad") + " \\___) (__)(__)(__)... on the other side");
    getLogger().info(ChatColor.of("#560bad") + " ");
  }
}
