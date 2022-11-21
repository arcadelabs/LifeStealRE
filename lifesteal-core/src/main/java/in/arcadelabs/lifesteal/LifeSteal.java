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
import in.arcadelabs.labaide.namespacedkey.NamespacedKeyBuilder;
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
import in.arcadelabs.lifesteal.database.profile.StatisticsManager;
import in.arcadelabs.lifesteal.hearts.HeartItemManager;
import in.arcadelabs.lifesteal.hearts.HeartRecipeManager;
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
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
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
  private ItemBuilder itemBuilder;
  private ItemStack placeholderHeart;
  private Interaction interaction;
  private HeadBuilder headBuilder;
  private SpiritFactory spiritFactory;
  private Logger logger;
  private HeartItemManager heartItemManager;
  private FancyStuff fancyStuff;
  private NamespacedKeyBuilder namespacedKeyBuilder;
  private CooldownManager craftCooldown, consumeCooldown, withdrawCooldown;
  private StatisticsManager statisticsManager;

  private void langInit() {
    try {
      this.language = YamlDocument.create(new File(this.instance.getDataFolder(), "language.yml"),
              Objects.requireNonNull(this.instance.getResource("language.yml")),
              GeneralSettings.DEFAULT,
              LoaderSettings.builder().setAutoUpdate(true).build(),
              DumperSettings.DEFAULT,
              UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
      this.logger = new Logger("LifeSteal",
              this.miniMessage.deserialize("<b><color:#e01e37>❥</color> </b>"),
              getKey("ToAllPrefix"),
              getKey("ToPlayerPrefix"));
      this.fancyStuff.setLangStatus(true);
    } catch (IOException e) {
      this.logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      this.fancyStuff.setLangStatus(false);
    }
  }

  private void configInit() {
    try {
      this.config = YamlDocument.create(new File(this.instance.getDataFolder(), "config.yml"),
              Objects.requireNonNull(this.instance.getResource("config.yml")),
              GeneralSettings.DEFAULT,
              LoaderSettings.builder().setAutoUpdate(true).build(),
              DumperSettings.DEFAULT,
              UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
      this.utils = new Utils();
      this.fancyStuff.setConfigStatus(true);
    } catch (Exception e) {
      this.logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      this.fancyStuff.setConfigStatus(false);
    }
  }

  private void heartsYMLInit() {
    try {
      this.heartConfig = YamlDocument.create(new File(this.instance.getDataFolder(), "hearts.yml"),
              Objects.requireNonNull(this.instance.getResource("hearts.yml")),
              GeneralSettings.DEFAULT,
              LoaderSettings.builder().setAutoUpdate(true).build(),
              DumperSettings.DEFAULT,
              UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
      this.fancyStuff.setHeartsStatus(true);
    } catch (Exception e) {
      this.logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      this.fancyStuff.setHeartsStatus(false);
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
      this.databaseHandler = new DatabaseHandler(this.instance);
      this.fancyStuff.setDatabaseMode(this.databaseHandler.isDbEnabled());
      this.fancyStuff.setDatabaseStatus(true);
    } catch (Exception e) {
      disableDatabaseLogger(false);
      this.logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      this.fancyStuff.setDatabaseMode(this.databaseHandler.isDbEnabled());
      this.fancyStuff.setDatabaseStatus(false);
    }
  }

  private void profilesInit() {
    try {
      this.profileManager = new ProfileManager();
      this.fancyStuff.setProfilesStatus(true);
    } catch (Exception e) {
      this.logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      this.fancyStuff.setProfilesStatus(false);
    }
  }

  private void placeholderHeartInit() {
    try {
      final int amount = this.config.getInt("HeartsToTransfer", 1);
      this.itemBuilder = new ItemBuilder(Material.valueOf(this.config.getString("Heart.Properties.ItemType")))
              .setName(this.utils.formatString(this.config.getString("Heart.Properties.Name")))
              .setLore(this.utils.stringToComponentList(this.config.getStringList("Heart.Properties.Lore"),
                      "hp", amount))
              .setModelData(this.config.getInt("Heart.Properties.ModelData"))
              .setPDCObject(this.namespacedKeyBuilder.getNewKey("heart_item"), PersistentDataType.STRING, "No heart spoofing, dum dum.")
              .setPDCObject(this.namespacedKeyBuilder.getNewKey("heart_healthpoints"), PersistentDataType.DOUBLE, (double) amount)
              .build();
      this.placeholderHeart = this.itemBuilder.getBuiltItem();
    } catch (IllegalArgumentException e) {
      this.logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
    }
  }

  private void recipesInit() {
    try {
      this.heartRecipeManager = new HeartRecipeManager();
      Bukkit.removeRecipe(this.heartRecipeManager.getHeartRecipe().getKey());
      this.instance.getServer().addRecipe(this.heartRecipeManager.getHeartRecipe());
      this.fancyStuff.setRecipeStatus(true);
    } catch (Exception e) {
      this.logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      this.fancyStuff.setRecipeStatus(false);
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

      this.instance.getLogger().setLevel(Level.OFF);
      final PaperCommandManager pcm = new PaperCommandManager(this.instance);
      Arrays.stream(commands).forEach(pcm::unregisterCommand);
      Arrays.stream(commands).forEach(pcm::registerCommand);
      this.instance.getLogger().setLevel(Level.ALL);
      this.fancyStuff.setCommandsStatus(true);
    } catch (SecurityException e) {
      this.logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      this.fancyStuff.setCommandsStatus(false);
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

      if (!HandlerList.getHandlerLists().isEmpty()) HandlerList.unregisterAll(this.instance);
      Arrays.stream(listeners).forEach(listener -> this.pluginManager.registerEvents(listener, this.instance));
      this.fancyStuff.setListenersStatus(true);
    } catch (Exception e) {
      this.logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      this.fancyStuff.setListenersStatus(false);
    }
  }

  public String getKey(String path) {
    return this.language.getString(path);
  }

  /**
   * Initializes everything.
   */
  public void init() {

    this.instance = LifeStealPlugin.getInstance();

    this.pluginManager = Bukkit.getPluginManager();

    this.miniMessage = MiniMessage.miniMessage();

    this.fancyStuff = new FancyStuff(this.instance, this.miniMessage);

    this.namespacedKeyBuilder = new NamespacedKeyBuilder("lifesteal", this.instance);

    this.langInit();

    this.configInit();

    this.heartsYMLInit();

    this.databaseInit();

    this.profilesInit();

    this.statisticsManager = new StatisticsManager().isRealTime(this.config.getBoolean("DATABASE.REALTIME"));

    this.craftCooldown = new CooldownManager(this.config.getInt("Cooldowns.Heart-Craft"));
    this.consumeCooldown = new CooldownManager(this.config.getInt("Cooldowns.Heart-Consume"));
    this.withdrawCooldown = new CooldownManager(this.config.getInt("Cooldowns.Heart-Withdraw"));

    this.interaction = new Interaction(this.config.getBoolean("Clean-Console"));

    this.headBuilder = new HeadBuilder(this.logger, Logger.Level.ERROR);

    this.spiritFactory = new SpiritFactory();

    this.metrics = new BStats(this.instance, 15272);

    this.registerCommands();

    this.registerListeners();

    this.placeholderHeartInit();

    this.recipesInit();

    this.heartItemManager = new HeartItemManager();

    this.fancyStuff.setBlessedHeartsCount(this.heartItemManager.getBlessedHearts().size());
    this.fancyStuff.setNormalHeartsCount(this.heartItemManager.getNormalHearts().size());
    this.fancyStuff.setCursedHeartsCount(this.heartItemManager.getCursedHearts().size());
    this.fancyStuff.setBlessedHeartsStatus(this.heartItemManager.getBlessedHearts().isEmpty());
    this.fancyStuff.setNormalHeartsStatus(this.heartItemManager.getNormalHearts().isEmpty());
    this.fancyStuff.setCursedHeartsStatus(this.heartItemManager.getCursedHearts().isEmpty());

    this.fancyStuff.consolePrint();

    new BukkitRunnable() {
      @Override
      public void run() {
        getDatabaseHandler().getHikariExecutor()
                .execute(() -> getProfileManager().getProfileCache().values().forEach(profile -> {
                  try {
                    if (!getProfileManager().getProfileCache().isEmpty())
                      getProfileManager().saveProfile(profile);
                  } catch (SQLException e) {
                    logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
                  }
                }));
      }
    }.runTaskTimer(this.instance, 1L, 6000L);

    // init api ;)
    new LifeStealAPIMP(this);

  }

  public void reInit() {
    this.craftCooldown = new CooldownManager(this.config.getInt("Cooldowns.Heart-Craft"));
    this.consumeCooldown = new CooldownManager(this.config.getInt("Cooldowns.Heart-Consume"));
    this.withdrawCooldown = new CooldownManager(this.config.getInt("Cooldowns.Heart-Withdraw"));
    this.interaction = new Interaction(this.config.getBoolean("Clean-Console"));
    this.statisticsManager = new StatisticsManager().isRealTime(this.config.getBoolean("DATABASE.REALTIME"));
  }
}