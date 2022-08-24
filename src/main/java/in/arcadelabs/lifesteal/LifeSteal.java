/*
 *          LifeSteal - Yet another lifecore smp core.
 *                Copyright (C) 2022  Arcade Labs
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

import in.arcadelabs.labaide.cooldown.CooldownManager;
import in.arcadelabs.labaide.item.HeadBuilder;
import in.arcadelabs.labaide.item.ItemBuilder;
import in.arcadelabs.labaide.libs.aikar.acf.BaseCommand;
import in.arcadelabs.labaide.libs.aikar.acf.PaperCommandManager;
import in.arcadelabs.labaide.libs.boostedyaml.YamlDocument;
import in.arcadelabs.labaide.libs.boostedyaml.dvs.versioning.BasicVersioning;
import in.arcadelabs.labaide.libs.boostedyaml.settings.dumper.DumperSettings;
import in.arcadelabs.labaide.libs.boostedyaml.settings.general.GeneralSettings;
import in.arcadelabs.labaide.libs.boostedyaml.settings.loader.LoaderSettings;
import in.arcadelabs.labaide.libs.boostedyaml.settings.updater.UpdaterSettings;
import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.labaide.metrics.BStats;
import in.arcadelabs.labaide.updatechecker.UpdateChecker;
import in.arcadelabs.lifesteal.commands.AddHearts;
import in.arcadelabs.lifesteal.commands.Eliminate;
import in.arcadelabs.lifesteal.commands.GiveHearts;
import in.arcadelabs.lifesteal.commands.Reload;
import in.arcadelabs.lifesteal.commands.RemoveHearts;
import in.arcadelabs.lifesteal.commands.Revive;
import in.arcadelabs.lifesteal.commands.SetHearts;
import in.arcadelabs.lifesteal.commands.Withdraw;
import in.arcadelabs.lifesteal.database.DatabaseHandler;
import in.arcadelabs.lifesteal.database.profile.ProfileListener;
import in.arcadelabs.lifesteal.database.profile.ProfileManager;
import in.arcadelabs.lifesteal.hearts.HeartItemCooker;
import in.arcadelabs.lifesteal.hearts.HeartItemManager;
import in.arcadelabs.lifesteal.hearts.HeartRecipeManager;
import in.arcadelabs.lifesteal.hearts.SkullMaker;
import in.arcadelabs.lifesteal.listeners.*;
import in.arcadelabs.lifesteal.utils.FancyStuff;
import in.arcadelabs.lifesteal.utils.Interaction;
import in.arcadelabs.lifesteal.utils.LogFilter;
import in.arcadelabs.lifesteal.utils.SpiritFactory;
import in.arcadelabs.lifesteal.utils.Utils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

@Getter
public class LifeSteal {

  private LifeStealPlugin instance;
  private MiniMessage miniMessage;
  private PluginManager pluginManager;
  private DatabaseHandler databaseHandler;
  private ProfileManager profileManager;
  private Utils utils;
  private HeartRecipeManager heartRecipeManager;
  private YamlDocument config, heartConfig, language;
  private BStats metrics;
  private HeartItemCooker heartItemCooker;
  private ItemStack placeholderHeart;
  private Interaction interaction;
  private SkullMaker skullMaker;
  private SpiritFactory spiritFactory;
  private Logger logger;
  private HeartItemManager heartItemManager;
  private FancyStuff fancyStuff;
  private NamespacedKeyBuilder namespacedKeyBuilder;
  private CooldownManager craftCooldown, consumeCooldown, withdrawCooldown;
  private StatisticsManager statisticsManager;

  private void langInit() {
    try {
      language = YamlDocument.create(new File(instance.getDataFolder(), "language.yml"),
              Objects.requireNonNull(instance.getResource("language.yml")),
              GeneralSettings.DEFAULT,
              LoaderSettings.builder().setAutoUpdate(true).build(),
              DumperSettings.DEFAULT,
              UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
      logger = new Logger("LifeSteal",
              miniMessage.deserialize("<b><color:#e01e37>❥</color> </b>"),
              getKey("ToAllPrefix"),
              getKey("ToPlayerPrefix"));
      fancyStuff.setLangStatus(true);
    } catch (IOException e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      fancyStuff.setLangStatus(false);
    }
  }

  private void configInit() {
    try {
      config = YamlDocument.create(new File(instance.getDataFolder(), "config.yml"),
              Objects.requireNonNull(instance.getResource("config.yml")),
              GeneralSettings.DEFAULT,
              LoaderSettings.builder().setAutoUpdate(true).build(),
              DumperSettings.DEFAULT,
              UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
      utils = new Utils();
      fancyStuff.setConfigStatus(true);
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      fancyStuff.setConfigStatus(false);
    }
  }

  private void heartsYMLInit() {
    try {
      heartConfig = YamlDocument.create(new File(instance.getDataFolder(), "hearts.yml"),
              Objects.requireNonNull(instance.getResource("hearts.yml")),
              GeneralSettings.DEFAULT,
              LoaderSettings.builder().setAutoUpdate(true).build(),
              DumperSettings.DEFAULT,
              UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
      fancyStuff.setHeartsStatus(true);
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      fancyStuff.setHeartsStatus(false);
    }
  }

  private void disableDatabaseLogger(boolean v) {
    LogFilter.registerFilter();
    if (v) com.j256.ormlite.logger.Logger.setGlobalLogLevel(com.j256.ormlite.logger.Level.OFF);
    else com.j256.ormlite.logger.Logger.setGlobalLogLevel(com.j256.ormlite.logger.Level.ERROR);
  }

  private void databaseInit() {
    try {
      disableDatabaseLogger(true);
      databaseHandler = new DatabaseHandler(LifeStealPlugin.getInstance());
      fancyStuff.setDatabaseMode(databaseHandler.isDbEnabled());
      fancyStuff.setDatabaseStatus(true);
    } catch (Exception e) {
      disableDatabaseLogger(false);
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      fancyStuff.setDatabaseMode(databaseHandler.isDbEnabled());
      fancyStuff.setDatabaseStatus(false);
    }
  }

  private void profilesInit() {
    try {
      profileManager = new ProfileManager();
      fancyStuff.setProfilesStatus(true);
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      fancyStuff.setProfilesStatus(false);
    }
  }

  private void placeholderHeartInit() {
    try {
      final int amount = config.getInt("HeartsToGain", 2) / 2;
      heartItemCooker = new HeartItemCooker(Material.valueOf(config.getString("Heart.Properties.ItemType")))
              .setHeartName(utils.formatString(config.getString("Heart.Properties.Name")))
              .setHeartLore(utils.stringToComponentList(config.getStringList("Heart.Properties.Lore"),
                      "hp", amount))
              .setModelData(config.getInt("Heart.Properties.ModelData"))
              .setPDCString(new NamespacedKey(instance, "lifesteal_heart_item"), "No heart spoofing, dum dum.")
              .setPDCDouble(new NamespacedKey(instance, "lifesteal_heart_healthpoints"), amount)
              .cook();
      placeholderHeart = heartItemCooker.getCookedItem();
    } catch (IllegalArgumentException e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
    }
  }

  private void recipesInit() {
    try {
      heartRecipeManager = new HeartRecipeManager();
      Bukkit.removeRecipe(heartRecipeManager.getHeartRecipe().getKey());
      LifeStealPlugin.getInstance().getServer().addRecipe(heartRecipeManager.getHeartRecipe());
      fancyStuff.setRecipeStatus(true);
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      fancyStuff.setRecipeStatus(false);
    }
  }

  private void updateCheckerInit() {
    try {
      new UpdateChecker(LifeStealPlugin.getInstance(), new URL("https://docs.taggernation.com/greetings-update.yml"), 6000)
              .setNotificationPermission("greetings.update")
              .enableOpNotification(true)
              .setup();
    } catch (IOException e) {
      instance.getLogger().warning(e.toString());
    }
  }

  private void registerCommands() {
    try {
      final BaseCommand[] commands = {
              new RemoveHearts(),
              new GiveHearts(),
              new Eliminate(),
              new AddHearts(),
              new SetHearts(),
              new Withdraw(),
              new Reload(),
              new Revive(),
      };

      instance.getLogger().setLevel(Level.OFF);
      final PaperCommandManager pcm = new PaperCommandManager(instance);
      Arrays.stream(commands).forEach(pcm::registerCommand);
      instance.getLogger().setLevel(Level.ALL);
      fancyStuff.setCommandsStatus(true);
    } catch (SecurityException e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      fancyStuff.setCommandsStatus(false);
    }
  }

  private void registerListeners() {
    try {
      final Listener[] listeners = {
              new PlayerPotionEffectListener(),
              new PlayerResurrectListener(),
              new PlayerDamageListener(),
              new ServerReloadListener(),
              new HeartConsumeListener(),
              new PlayerDeathListener(),
              new HeartPlaceListener(),
              new HeartCraftListener(),
              new PlayerJoinListener(),
              new ArrowPickupEvent(),
              new ProfileListener(),
      };
      Arrays.stream(listeners).forEach(listener -> pluginManager.registerEvents(listener, instance));
      fancyStuff.setListenersStatus(true);
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      fancyStuff.setListenersStatus(false);
    }
  }

  public String getKey(String path) {
    return language.getString(path);
  }

  /**
   * Initializes everything.
   */
  public void init() {

    instance = LifeStealPlugin.getInstance();

    pluginManager = Bukkit.getPluginManager();

    miniMessage = MiniMessage.miniMessage();

    fancyStuff = new FancyStuff(instance, miniMessage);

    langInit();

    configInit();

    heartsYMLInit();

    databaseInit();

    profilesInit();

    interaction = new Interaction(config.getBoolean("Clean-Console"));

    this.craftCooldown = new CooldownManager(this.config.getInt("Cooldowns.Heart-Craft"));
    this.consumeCooldown = new CooldownManager(this.config.getInt("Cooldowns.Heart-Consume"));
    this.withdrawCooldown = new CooldownManager(this.config.getInt("Cooldowns.Heart-Withdraw"));

    spiritFactory = new SpiritFactory();

    metrics = new BStats(LifeStealPlugin.getInstance(), 15272);

    registerCommands();

    registerListeners();

    placeholderHeartInit();

    recipesInit();

    heartItemManager = new HeartItemManager(HeartItemManager.Mode.RANDOM_ALL);

    fancyStuff.setBlessedHeartsCount(heartItemManager.getBlessedHearts().size());
    fancyStuff.setNormalHeartsCount(heartItemManager.getNormalHearts().size());
    fancyStuff.setCursedHeartsCount(heartItemManager.getCursedHearts().size());
    fancyStuff.setBlessedHeartsStatus(heartItemManager.getBlessedHearts().isEmpty());
    fancyStuff.setNormalHeartsStatus(heartItemManager.getNormalHearts().isEmpty());
    fancyStuff.setCursedHeartsStatus(heartItemManager.getCursedHearts().isEmpty());

    fancyStuff.consolePrint();

    Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () ->
            this.getDatabaseHandler().getHikariExecutor()
                    .execute(() -> this.getProfileManager().getProfileCache().values().forEach(profile -> {
                      try {
                        if (!this.getProfileManager().getProfileCache().isEmpty())
                          this.getProfileManager().saveProfile(profile);
                      } catch (SQLException e) {
                        logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
                      }
                    })), 1L, 6000L);
  }
}