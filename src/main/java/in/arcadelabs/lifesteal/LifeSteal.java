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
import in.arcadelabs.lifesteal.database.profile.ProfileManager;
import in.arcadelabs.lifesteal.hearts.HeartItemCooker;
import in.arcadelabs.lifesteal.hearts.HeartItemManager;
import in.arcadelabs.lifesteal.hearts.HeartRecipeManager;
import in.arcadelabs.lifesteal.hearts.SkullMaker;
import in.arcadelabs.lifesteal.listeners.*;
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
  private Logger logger, noPrefixLogger;
  private HeartItemManager heartItemManager;

  private boolean langInit() {
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
      return true;

    } catch (IOException e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      return false;
    }
  }

  private boolean configInit() {
    try {
      config = YamlDocument.create(new File(instance.getDataFolder(), "config.yml"),
              Objects.requireNonNull(instance.getResource("config.yml")),
              GeneralSettings.DEFAULT,
              LoaderSettings.builder().setAutoUpdate(true).build(),
              DumperSettings.DEFAULT,
              UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
      utils = new Utils();
      return true;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      return false;
    }
  }

  private boolean heartsYMLInit() {
    try {
      heartConfig = YamlDocument.create(new File(instance.getDataFolder(), "hearts.yml"),
              Objects.requireNonNull(instance.getResource("hearts.yml")),
              GeneralSettings.DEFAULT,
              LoaderSettings.builder().setAutoUpdate(true).build(),
              DumperSettings.DEFAULT,
              UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
      return true;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      return false;
    }
  }

  private void disableDatabaseLogger(boolean v) {
    LogFilter.registerFilter();
    if (v) com.j256.ormlite.logger.Logger.setGlobalLogLevel(com.j256.ormlite.logger.Level.OFF);
    else com.j256.ormlite.logger.Logger.setGlobalLogLevel(com.j256.ormlite.logger.Level.ERROR);
  }

  private boolean databaseInit() {
    try {
      disableDatabaseLogger(true);
      databaseHandler = new DatabaseHandler(LifeStealPlugin.getInstance());
      return true;
    } catch (Exception e) {
      disableDatabaseLogger(false);
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      instance.getLogger().warning(e.toString());
      return false;
    }
  }

  private boolean profilesInit() {
    try {
      profileManager = new ProfileManager();
      return true;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      instance.getLogger().warning(e.toString());
      return false;
    }
  }

  private boolean placeholderHeartInit() {
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
      return true;
    } catch (IllegalArgumentException e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      instance.getLogger().warning(e.toString());
      return false;
    }
  }

  private boolean recipesInit() {
    try {
      heartRecipeManager = new HeartRecipeManager();
      Bukkit.removeRecipe(heartRecipeManager.getHeartRecipe().getKey());
      LifeStealPlugin.getInstance().getServer().addRecipe(heartRecipeManager.getHeartRecipe());
      return true;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      instance.getLogger().warning(e.toString());
      return false;
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

  private boolean registerCommands() {
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
      return true;
    } catch (SecurityException e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      instance.getLogger().warning(e.toString());
      return false;
    }
  }

  private boolean registerListeners() {
    try {
      final Listener[] listeners = {
              new PlayerPotionEffectListener(),
              new PlayerResurrectListener(),
              new PlayerDamageListener(),
              new HeartConsumeListener(),
              new PlayerDeathListener(),
              new PlayerClickListener(),
              new HeartPlaceListener(),
              new HeartCraftListener(),
              new PlayerJoinListener(),
              new ArrowPickupEvent(),
      };
      Arrays.stream(listeners).forEach(listener -> pluginManager.registerEvents(listener, instance));
      return true;
    } catch (Exception e) {
      logger.log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      instance.getLogger().warning(e.toString());
      return false;
    }
  }

  public String getKey(String path) {
    return language.getString(path);
  }

  public void fancyStuff() {

    final String configStatus = configInit() ? "<green>❥" : "<red>❥";
    final String langStatus = langInit() ? "<green>❥" : "<red>❥";
    final String heartsStatus = heartsYMLInit() ? "<green>❥" : "<red>❥";
    final String databaseMode = databaseHandler.isDbEnabled() ? "│   └── MySQL-Hikari  " : "│   └── database.db   ";
    final String databaseStatus = databaseInit() ? "<green>❥" : "<red>❥";
    final String profilesStatus = profilesInit() ? "<green>❥" : "<red>❥";
    final String commandsStatus = registerCommands() ? "<green>❥" : "<red>❥";
    final String listenersStatus = registerListeners() ? "<green>❥" : "<red>❥";
    final String recipeStatus = placeholderHeartInit() && recipesInit() ? "<green>❥" : "<red>❥";
    final String blessedHeartsCount = heartItemManager.getBlessedHearts().isEmpty() ?
            "<red>" + heartItemManager.getBlessedHearts().size() + "x" :
            "<green>" + heartItemManager.getBlessedHearts().size() + "x";
    final String normalHeartsCount = heartItemManager.getNormalHearts().isEmpty() ?
            "<red>" + heartItemManager.getNormalHearts().size() + "x" :
            "<green>" + heartItemManager.getNormalHearts().size() + "x";
    final String cursedHeartsCount = heartItemManager.getCursedHearts().isEmpty() ?
            "<red>" + heartItemManager.getCursedHearts().size() + "x" :
            "<green>" + heartItemManager.getCursedHearts().size() + "x";


    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B>  _     _   __       ___  _               _ "));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#EB554F> | |   (_) / _| ___ / __|| |_  ___  __ _ | |    "));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#E53A43> | |__ | ||  _|/ -_)\\__ \\|  _|/ -_)/ _` || |  " +
                    "<color:#F72585>LifeSteal Reimagined v" + instance.getDescription().getVersion()));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#E01E37> |____||_||_|  \\___||___/ \\__|\\___|\\__,_||_|  " +
                    "<color:#E01E37>Running on " + Bukkit.getVersion() + " " + Bukkit.getBukkitVersion()));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B> ."));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B> ├── I/O"));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B> │   ├── config.yml    " + configStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B> │   ├── language.yml  " + langStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B> │   ├── hearts.yml    " + heartsStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B> " + databaseMode + databaseStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B> └── Functions"));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B>     ├── Profiles      " + profilesStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B>     ├── Commands      " + commandsStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B>     ├── Listeners     " + listenersStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B>     ├── Recipes       " + recipeStatus));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B>     └── Hearts        "));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B>         ├── Blessed   " + blessedHeartsCount));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B>         ├── Normal    " + normalHeartsCount));
    noPrefixLogger.log(Logger.Level.INFO,
            miniMessage.deserialize("<b><color:#F0715B>         └── Cursed    " + cursedHeartsCount));
  }

  /**
   * Initializes everything.
   */
  public void init() {

    instance = LifeStealPlugin.getInstance();

    pluginManager = Bukkit.getPluginManager();

    miniMessage = MiniMessage.miniMessage();

    noPrefixLogger = new Logger("LifeSteal", Component.empty(), null, null);

    langInit();

    configInit();

    heartsYMLInit();

    databaseInit();

    profilesInit();

    interaction = new Interaction(config.getBoolean("Clean-Console"));

    skullMaker = new SkullMaker();

    spiritFactory = new SpiritFactory();

    metrics = new BStats(LifeStealPlugin.getInstance(), 15272);

    registerCommands();

    registerListeners();

    placeholderHeartInit();

    recipesInit();

    heartItemManager = new HeartItemManager(HeartItemManager.Mode.RANDOM_ALL);

    fancyStuff();
  }
}