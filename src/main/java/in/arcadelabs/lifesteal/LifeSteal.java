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

import in.arcadelabs.labaide.LabAide;
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
import in.arcadelabs.lifesteal.hearts.HeartRecipeManager;
import in.arcadelabs.lifesteal.hearts.SkullMaker;
import in.arcadelabs.lifesteal.listeners.*;
import in.arcadelabs.lifesteal.utils.Interaction;
import in.arcadelabs.lifesteal.utils.SpiritFactory;
import in.arcadelabs.lifesteal.utils.Utils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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
import java.util.List;
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

    private void configInit() {
        try {
            language = YamlDocument.create(new File(instance.getDataFolder(), "language.yml"),
                    Objects.requireNonNull(instance.getResource("language.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
            logger = new Logger("❥",
                    miniMessage.deserialize(getKey("Prefix")),
                    getKey("ToAllPrefix"),
                    getKey("ToPlayerPrefix"));
        } catch (IOException e) {
            logger.logger(Logger.Level.ERROR, miniMessage.deserialize(e.getMessage()), e.fillInStackTrace());
        }
        try {
            config = YamlDocument.create(new File(instance.getDataFolder(), "config.yml"),
                    Objects.requireNonNull(instance.getResource("config.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
            utils = new Utils();
            logger.logger(Logger.Level.INFO, miniMessage.deserialize(getKey("Messages.ConfigLoad")));
        } catch (Exception e) {
            logger.logger(Logger.Level.ERROR, miniMessage.deserialize(getKey("Messages.ConfigLoadError")), e.fillInStackTrace());
        }

        try {
            heartConfig = YamlDocument.create(new File(instance.getDataFolder(), "hearts.yml"),
                    Objects.requireNonNull(instance.getResource("hearts.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
            logger.logger(Logger.Level.INFO, miniMessage.deserialize(getKey("Messages.HeartConfigLoad")));
        } catch (Exception e) {
            logger.logger(Logger.Level.ERROR, miniMessage.deserialize(getKey("Messages.HeartConfigLoadError")), e.fillInStackTrace());
        }
    }

    private void disableDatabaseLogger(boolean v) {
        List<String> loggerClassNames = Arrays.asList(
                "com.zaxxer.hikari.HikariDataSource",
                "com.zaxxer.hikari.pool.HikariPool"
        ); // TODO: Fucking make work.
        if (v) {
            com.j256.ormlite.logger.Logger.setGlobalLogLevel(com.j256.ormlite.logger.Level.OFF);
            loggerClassNames.forEach(string -> java.util.logging.Logger.getLogger(string).setLevel(Level.OFF));
        } else {
            com.j256.ormlite.logger.Logger.setGlobalLogLevel(com.j256.ormlite.logger.Level.ERROR);
            loggerClassNames.forEach(string -> java.util.logging.Logger.getLogger(string).setLevel(Level.ALL));
        }
    }

    private void databaseInit() {
        try {
            disableDatabaseLogger(true);
            databaseHandler = new DatabaseHandler(LifeStealPlugin.getInstance());
            logger.logger(Logger.Level.INFO, miniMessage.deserialize(getKey("Messages.DatabaseLoad")));
        } catch (Exception e) {
            logger.logger(Logger.Level.ERROR, miniMessage.deserialize(getKey("Messages.DatabaseLoadError")), e.fillInStackTrace());
            instance.getLogger().warning(e.toString());
        }
        disableDatabaseLogger(false);
    }

    private void profilesInit() {
        try {
            profileManager = new ProfileManager();
            logger.logger(Logger.Level.INFO, miniMessage.deserialize(getKey("Messages.ProfilesLoad")));
        } catch (Exception e) {
            logger.logger(Logger.Level.ERROR, miniMessage.deserialize(getKey("Messages.ProfilesLoadError")), e.fillInStackTrace());
            instance.getLogger().warning(e.toString());
        }
    }

    private void placeholderHeartInit() {
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

        logger.logger(Logger.Level.INFO, miniMessage.deserialize(getKey("Messages.CommandsAsyncLoad")));
    }

    private void registerListeners() {
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
        logger.logger(Logger.Level.INFO, miniMessage.deserialize(getKey("Messages.ListenersLoad")));
    }

    public String getKey(String path) {
        return language.getString(path);
    }

    /**
     * Initializes everything.
     */
    public void init() {

        LabAide.Logger().logger(Logger.Level.INFO, Component.text("  _     _   __       ___  _               _ ",
                TextColor.color(248, 153, 153)));
        LabAide.Logger().logger(Logger.Level.INFO, Component.text(" | |   (_) / _| ___ / __|| |_  ___  __ _ | |",
                TextColor.color(248, 153, 153)));
        LabAide.Logger().logger(Logger.Level.INFO, Component.text(" | |__ | ||  _|/ -_)\\__ \\|  _|/ -_)/ _` || |",
                TextColor.color(248, 160, 159)));
        LabAide.Logger().logger(Logger.Level.INFO, Component.text(" |____||_||_|  \\___||___/ \\__|\\___|\\__,_||_|",
                TextColor.color(247, 166, 164)));
        LabAide.Logger().logger(Logger.Level.INFO, Component.text("                                            ",
                TextColor.color(247, 173, 170)));

        instance = LifeStealPlugin.getInstance();

        miniMessage = MiniMessage.miniMessage();

        pluginManager = Bukkit.getPluginManager();

        configInit();

        databaseInit();

        profilesInit();

        interaction = new Interaction(config.getBoolean("Clean-Console"));

        skullMaker = new SkullMaker();

        spiritFactory = new SpiritFactory();

        placeholderHeartInit();

        heartRecipeManager = new HeartRecipeManager();

        LifeStealPlugin.getInstance().getServer().addRecipe(heartRecipeManager.getHeartRecipe());

        metrics = new BStats(LifeStealPlugin.getInstance(), 15272);

        registerCommands();

        registerListeners();
    }
}