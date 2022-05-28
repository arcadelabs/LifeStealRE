package in.arcadelabs.lifesteal;

import com.google.gson.Gson;
import in.arcadelabs.lifesteal.handler.MongoHandler;
import in.arcadelabs.lifesteal.hooks.LifeStealHook;
import in.arcadelabs.lifesteal.profile.ProfileStorage;
import in.arcadelabs.lifesteal.profile.impl.JsonProfileHandler;
import in.arcadelabs.lifesteal.profile.impl.MongoProfileHandler;
import in.arcadelabs.lifesteal.utils.LSUtils;
import in.arcadelabs.lifesteal.utils.RecipeManager;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@Getter
public final class LifeStealPlugin extends JavaPlugin {

  @Getter
  private static LifeStealPlugin instance;
  private LSUtils utils;
  private LifeStealHook lifeStealHook;
  private RecipeManager recipeManager;
  private NamespacedKey namespacedKey;

  private ProfileStorage profileStorage;
  private Gson GSON = new Gson();

  private boolean useMongo;

  @Override
  public void onEnable() {
    instance = this;
    utils = new LSUtils();
    lifeStealHook = new LifeStealHook();
    try {
      lifeStealHook.init();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    if (this.getConfig().getBoolean(""))

    namespacedKey = new NamespacedKey(this, "lifesteal_heart");
    recipeManager = new RecipeManager();

    useMongo = this.getConfig().getBoolean("MongoDB.ENABLED");
    if (useMongo) {
        profileStorage = new MongoProfileHandler(this);
    } else {
      try {
          profileStorage = new JsonProfileHandler(this);
      } catch (IOException e) {
          e.printStackTrace();
          this.getLogger().warning("CANNOT LOAD JSON");
          this.setEnabled(false);
      }
    }

  }

  @Override
  public void onDisable() {
    lifeStealHook.getMessenger().closeMessenger();
    profileStorage.disconnect();
    getLogger().info(ChatColor.of("#f72585") + "  ___  _  _   __   ");
    getLogger().info(ChatColor.of("#b5179e") + " / __)( \\/ ) /__\\  ");
    getLogger().info(ChatColor.of("#7209b7") + "( (__  \\  / /(__)\\ ");
    getLogger().info(ChatColor.of("#560bad") + " \\___) (__)(__)(__)... on the other side");
    getLogger().info(ChatColor.of("#560bad") + " ");
  }
}
