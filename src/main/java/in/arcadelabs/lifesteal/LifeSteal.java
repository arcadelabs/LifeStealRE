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

package in.arcadelabs.lifesteal;

import com.google.gson.Gson;
import in.arcadelabs.arcadelibs.config.Config;
import in.arcadelabs.arcadelibs.metrics.BStats;
import in.arcadelabs.arcadelibs.placeholder.Placeholder;
import in.arcadelabs.arcadelibs.updatechecker.UpdateChecker;
import in.arcadelabs.libs.adventurelib.impl.SpigotMessenger;
import in.arcadelabs.libs.aikar.acf.BaseCommand;
import in.arcadelabs.libs.aikar.acf.BukkitCommandManager;
import in.arcadelabs.libs.aikar.acf.PaperCommandManager;
import in.arcadelabs.lifesteal.commands.Eliminate;
import in.arcadelabs.lifesteal.commands.Reload;
import in.arcadelabs.lifesteal.commands.Stats;
import in.arcadelabs.lifesteal.commands.Withdraw;
import in.arcadelabs.lifesteal.listeners.HeartCraftListener;
import in.arcadelabs.lifesteal.listeners.PlayerClickListener;
import in.arcadelabs.lifesteal.listeners.PlayerJoinListener;
import in.arcadelabs.lifesteal.listeners.PlayerKillListener;
import in.arcadelabs.lifesteal.listeners.PlayerResurrectListener;
import in.arcadelabs.lifesteal.profile.ProfileStorage;
import in.arcadelabs.lifesteal.profile.impl.JsonProfileHandler;
import in.arcadelabs.lifesteal.profile.impl.MongoProfileHandler;
import in.arcadelabs.lifesteal.utils.HeartItemCooker;
import in.arcadelabs.lifesteal.utils.HeartRecipeManager;
import in.arcadelabs.lifesteal.utils.LSUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;


@Getter
public class LifeSteal {

  private final LifeStealPlugin instance = LifeStealPlugin.getInstance();
  private final PluginManager PM = Bukkit.getPluginManager();
  private final Gson GSON = new Gson();
  private LSUtils utils;
  private HeartRecipeManager heartRecipeManager;
  private ProfileStorage profileStorage;
  private Placeholder papiHook;
  private SpigotMessenger messenger;
  private Config configYML;
  private Config heartYML;
  private FileConfiguration config;
  private FileConfiguration heartConfig;
  private boolean useMongo;
  private BStats metrics;
  private HeartItemCooker heartItemCooker;
  private ItemStack placeholderHeart;


  //<editor-fold desc="Paper server check.">
  private boolean isOnPaper() {
    try {
      Class.forName("com.destroystokyo.paper.ParticleBuilder");
      return true;
    } catch (ClassNotFoundException ignored) {
      return false;
    }
  }
  //</editor-fold>

  //<editor-fold desc="Register commands.">
  private void registerCommands() {
    final BaseCommand[] commands = new BaseCommand[]{
            new Eliminate(),
            new Reload(),
            new Stats(),
            new Withdraw(),
    };
    if (isOnPaper()) {
      PaperCommandManager pcm = new PaperCommandManager(instance);
      Arrays.stream(commands).forEach(pcm::registerCommand);
    } else {
      BukkitCommandManager bcm = new BukkitCommandManager(instance);
      Arrays.stream(commands).forEach(bcm::registerCommand);
    }
  }
  //</editor-fold>

  //<editor-fold desc="Register event listeners.">
  private void registerListener() {
    final Listener[] listeners = new Listener[]{
            new PlayerResurrectListener(),
            new PlayerClickListener(),
            new PlayerJoinListener(),
            new PlayerKillListener(),
            new HeartCraftListener(),
//            new HeartConsumeListener(),
//            new ProfileListener()
    };
    Arrays.stream(listeners).forEach(listener -> PM.registerEvents(listener, instance));
  }
  //</editor-fold>

  //  Initialize everything.
  public void init() throws Exception {


    //<editor-fold desc="Adventure lib.">
    messenger = SpigotMessenger
            .builder()
            .setPlugin(instance)
            .defaultToMiniMessageTranslator()
            .build();
    //</editor-fold>

    //<editor-fold desc="Config init.">
    try {
      configYML = new Config(instance, "Config.yml", false, true);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      configYML.updateConfig("3.0", "version");
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      heartYML = new Config(instance, "Hearts.yml", false, true);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      heartYML.updateConfig("3.0", "version");
    } catch (Exception e) {
      e.printStackTrace();
    }

    config = configYML.getConfig();
    heartConfig = heartYML.getConfig();
    //</editor-fold>

    //<editor-fold desc="Other stuff.">
    utils = new LSUtils();

    int amount = config.getInt("HeartsToGain", 2) / 2;
    heartItemCooker = new HeartItemCooker(Material.valueOf(config.getString("Heart.Properties.ItemType")))
            .setHeartName(utils.formatString(config.getString("Heart.Properties.Name"), "hp",
                    amount))
            .setHeartLore(utils.formatStringList(config.getStringList("Heart.Properties.Lore"),
                    "hp", amount))
            .setModelData(config.getInt("Heart.Properties.ModelData"))
            .setPDCString(new NamespacedKey(instance, "lifesteal_heart_item"), "No heart spoofing, dum dum.")
            .setPDCDouble(new NamespacedKey(instance, "lifesteal_heart_healthpoints"), amount)
            .cook();
    placeholderHeart = heartItemCooker.getCookedItem();
    heartRecipeManager = new HeartRecipeManager();


    //</editor-fold>

    //<editor-fold desc="Profile storage handler.">
    useMongo = this.getConfiguration().getBoolean("MongoDB.ENABLED");
    if (useMongo) {
      profileStorage = new MongoProfileHandler(instance);
    } else {
      try {
        profileStorage = new JsonProfileHandler(instance);
      } catch (IOException e) {
        e.printStackTrace();
        instance.getLogger().warning("CANNOT LOAD JSON");
      }
    }
    //</editor-fold>

    //<editor-fold desc="PlaceholderAPI hook.">
    papiHook = new Placeholder();
    //</editor-fold>

    //<editor-fold desc="Registering stuff.">
    registerCommands();
    registerListener();
    //</editor-fold>

    //<editor-fold desc="Registering recipe.">
//    Bukkit.addRecipe(getRecipeManager().getHeartRecipe());
    instance.getServer().addRecipe(getHeartRecipeManager().getHeartRecipe());
    //</editor-fold>

    //<editor-fold desc="Plugin update checker.">
    new UpdateChecker(instance, new URL("https://docs.taggernation.com/greetings-update.yml"), 600000)
            .setNotificationPermission("greetings.update")
            .enableOpNotification(true)
            .setup();
    //</editor-fold>

    //<editor-fold desc="BStats metrics hook.">
    metrics = new BStats(instance, 15272);
    //</editor-fold>
  }

  public FileConfiguration getConfiguration() {
    return this.config;
  }

  public Config getConfigYML() {
    return this.configYML;
  }
}
