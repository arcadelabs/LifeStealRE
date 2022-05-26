package in.arcadelabs.lifesteal;

import in.arcadelabs.lifesteal.hooks.LifeStealHook;
import in.arcadelabs.lifesteal.utils.LSUtils;
import in.arcadelabs.lifesteal.utils.RecipeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import static in.arcadelabs.lifesteal.hooks.LifeStealHook.messenger;

public final class LifeSteal extends JavaPlugin {

  public static final Logger LOGGER = Bukkit.getLogger();
  public static LifeSteal plugin;
  public static LSUtils utils;

  @Override
  public void onEnable() {
    plugin = this;
    utils = new LSUtils();
    LifeStealHook hook = new LifeStealHook();
    try { hook.init(); } catch (Exception e) { throw new RuntimeException(e); }

    RecipeManager recipeManager = new RecipeManager();
  }

  @Override
  public void onDisable() {
    messenger.closeMessenger();
    LOGGER.info(ChatColor.of("#f72585") + "  ___  _  _   __   ");
    LOGGER.info(ChatColor.of("#b5179e") + " / __)( \\/ ) /__\\  ");
    LOGGER.info(ChatColor.of("#7209b7") + "( (__  \\  / /(__)\\ ");
    LOGGER.info(ChatColor.of("#560bad") + " \\___) (__)(__)(__)... on the other side");
    LOGGER.info(ChatColor.of("#560bad") + " ");
  }
}
