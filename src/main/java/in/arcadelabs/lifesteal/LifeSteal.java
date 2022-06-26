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
import in.arcadelabs.lifesteal.commands.Withdraw;
import in.arcadelabs.lifesteal.database.DatabaseHandler;
import in.arcadelabs.lifesteal.database.profile.ProfileManager;
import in.arcadelabs.lifesteal.listeners.HeartCraftListener;
import in.arcadelabs.lifesteal.listeners.PlayerClickListener;
import in.arcadelabs.lifesteal.listeners.PlayerJoinListener;
import in.arcadelabs.lifesteal.listeners.PlayerKillListener;
import in.arcadelabs.lifesteal.listeners.PlayerPotionEffectListener;
import in.arcadelabs.lifesteal.database.profile.ProfileListener;
import in.arcadelabs.lifesteal.listeners.PlayerResurrectListener;
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

import java.net.URL;
import java.util.Arrays;

@Getter
public class LifeSteal {

  private final LifeStealPlugin instance = LifeStealPlugin.getInstance();
  private final PluginManager PM = Bukkit.getPluginManager();

  private DatabaseHandler databaseHandler;
  private ProfileManager profileManager;

  private LSUtils utils;
  private HeartRecipeManager heartRecipeManager;
  private Placeholder papiHook;
  private SpigotMessenger messenger;
  private Config configYML;
  private Config heartYML;
  private FileConfiguration config;
  private FileConfiguration heartConfig;
  private BStats metrics;
  private HeartItemCooker heartItemCooker;
  private ItemStack placeholderHeart;

  /**
   * Check if server ruunning on Paper or it's forks.
   *
   * @return false if server running on Spigot or upstream.
   */
  private boolean isOnPaper() {
    try {
      Class.forName("com.destroystokyo.paper.ParticleBuilder");
      return true;
    } catch (ClassNotFoundException ignored) {
      return false;
    }
  }

  /**
   * Initializes everything.
   *
   * @throws Exception undefined exception
   */
  public void init() throws Exception {

//    Initialize SpigotMessenger with MiniMessage translator.
    messenger = SpigotMessenger
            .builder()
            .setPlugin(instance)
            .defaultToMiniMessageTranslator()
            .build();

//    Initialize, update and return config.
    try {
      configYML = new Config(instance, "Config.yml", false, true);
      configYML.updateConfig("3.0", "version");
      config = configYML.getConfig();
    } catch (Exception e) {
      e.printStackTrace();
    }

    databaseHandler = new DatabaseHandler(LifeStealPlugin.getInstance());
    profileManager = new ProfileManager();

//    Initialize, update and return Heart config.
    try {
      heartYML = new Config(instance, "Hearts.yml", false, true);
      heartYML.updateConfig("3.0", "version");
      heartConfig = heartYML.getConfig();
    } catch (Exception e) {
      e.printStackTrace();
    }

//    Initialize LifeSteal utils.
    utils = new LSUtils();

//    Cook a placeholder heart and assign it.
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

//    Initialize Heart recipe manager.
    heartRecipeManager = new HeartRecipeManager();

//    Initialize Player placeholder fetcher.
    papiHook = new Placeholder();

//    Register Heart recipe.
    LifeStealPlugin.getInstance().getServer().addRecipe(getHeartRecipeManager().getHeartRecipe());

//    Initialize update checker.
    new UpdateChecker(LifeStealPlugin.getInstance(), new URL("https://docs.taggernation.com/greetings-update.yml"), 6000)
            .setNotificationPermission("greetings.update")
            .enableOpNotification(true)
            .setup();

//    Initialize BStats metrics.
    metrics = new BStats(LifeStealPlugin.getInstance(), 15272);

//    Register commands.
    final BaseCommand[] commands = new BaseCommand[]{
            new Eliminate(),
            new Reload(),
            new Withdraw(),
    };
    if (isOnPaper()) {
      PaperCommandManager pcm = new PaperCommandManager(instance);
      Arrays.stream(commands).forEach(pcm::registerCommand);
    } else {
      BukkitCommandManager bcm = new BukkitCommandManager(instance);
      Arrays.stream(commands).forEach(bcm::registerCommand);
    }

//    Register listeners.
    final Listener[] listeners = new Listener[]{
            new PlayerPotionEffectListener(),
            new PlayerResurrectListener(),
            new PlayerClickListener(),
            new PlayerJoinListener(),
            new PlayerKillListener(),
            new HeartCraftListener(),
            new ProfileListener()
//            new HeartConsumeListener(),
    };
    Arrays.stream(listeners).forEach(listener -> PM.registerEvents(listener, instance));

  }
}
