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

import in.arcadelabs.labaide.libs.adventurelib.impl.SpigotMessenger;
import in.arcadelabs.labaide.libs.aikar.acf.BaseCommand;
import in.arcadelabs.labaide.libs.aikar.acf.BukkitCommandManager;
import in.arcadelabs.labaide.libs.aikar.acf.PaperCommandManager;
import in.arcadelabs.labaide.libs.boostedyaml.YamlDocument;
import in.arcadelabs.labaide.libs.boostedyaml.dvs.versioning.BasicVersioning;
import in.arcadelabs.labaide.libs.boostedyaml.settings.dumper.DumperSettings;
import in.arcadelabs.labaide.libs.boostedyaml.settings.general.GeneralSettings;
import in.arcadelabs.labaide.libs.boostedyaml.settings.loader.LoaderSettings;
import in.arcadelabs.labaide.libs.boostedyaml.settings.updater.UpdaterSettings;
import in.arcadelabs.labaide.metrics.BStats;
import in.arcadelabs.labaide.placeholder.Placeholder;
import in.arcadelabs.labaide.updatechecker.UpdateChecker;
import in.arcadelabs.lifesteal.commands.Eliminate;
import in.arcadelabs.lifesteal.commands.GiveHearts;
import in.arcadelabs.lifesteal.commands.Reload;
import in.arcadelabs.lifesteal.commands.SetHearts;
import in.arcadelabs.lifesteal.commands.Withdraw;
import in.arcadelabs.lifesteal.database.DatabaseHandler;
import in.arcadelabs.lifesteal.database.profile.ProfileListener;
import in.arcadelabs.lifesteal.database.profile.ProfileManager;
import in.arcadelabs.lifesteal.hearts.HeartItemCooker;
import in.arcadelabs.lifesteal.hearts.HeartRecipeManager;
import in.arcadelabs.lifesteal.hearts.SkullMaker;
import in.arcadelabs.lifesteal.listeners.HeartConsumeListener;
import in.arcadelabs.lifesteal.listeners.HeartCraftListener;
import in.arcadelabs.lifesteal.listeners.PlayerClickListener;
import in.arcadelabs.lifesteal.listeners.PlayerJoinListener;
import in.arcadelabs.lifesteal.listeners.PlayerKillListener;
import in.arcadelabs.lifesteal.listeners.PlayerPotionEffectListener;
import in.arcadelabs.lifesteal.listeners.PlayerResurrectListener;
import in.arcadelabs.lifesteal.utils.I18n;
import in.arcadelabs.lifesteal.utils.Interaction;
import in.arcadelabs.lifesteal.utils.LSUtils;
import lombok.Getter;
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

  private final LifeStealPlugin instance = LifeStealPlugin.getInstance();
  private final PluginManager pluginManager = Bukkit.getPluginManager();

  private I18n i18n;
  private DatabaseHandler databaseHandler;
  private ProfileManager profileManager;
  private LSUtils utils;
  private HeartRecipeManager heartRecipeManager;
  private Placeholder papiHook;
  private SpigotMessenger messenger;
  private YamlDocument config;
  private YamlDocument heartConfig;
  private BStats metrics;
  private HeartItemCooker heartItemCooker;
  private ItemStack placeholderHeart;
  private Interaction interaction;
  private SkullMaker skullMaker;

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

  private void messengerInit() {
    try {
      messenger = SpigotMessenger
              .builder()
              .setPlugin(instance)
              .defaultToMiniMessageTranslator()
              .build();
    } catch (Exception e) {
      instance.getLogger().warning(e.getLocalizedMessage());
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
      i18n.translate(Level.FINE, "ConfigLoad");
    } catch (Exception e) {
      i18n.translate(Level.WARNING, "ConfigLoadError");
      instance.getLogger().warning(e.toString());
    }

    try {
      heartConfig = YamlDocument.create(new File(instance.getDataFolder(), "hearts.yml"),
              Objects.requireNonNull(instance.getResource("hearts.yml")),
              GeneralSettings.DEFAULT,
              LoaderSettings.builder().setAutoUpdate(true).build(),
              DumperSettings.DEFAULT,
              UpdaterSettings.builder().setVersioning(new BasicVersioning("Version")).build());
      i18n.translate(Level.FINE, "HeartConfigLoad");
    } catch (Exception e) {
      i18n.translate(Level.WARNING, "HeartConfigLoadError");
      instance.getLogger().warning(e.toString());
    }
  }

  private void databaseInit() {
    try {
      databaseHandler = new DatabaseHandler(LifeStealPlugin.getInstance());
      i18n.translate(Level.FINE, "DatabaseLoad");
    } catch (Exception e) {
      i18n.translate(Level.WARNING, "DatabaseLoadError");
      instance.getLogger().warning(e.toString());
    }
  }

  private void profilesInit() {
    try {
      profileManager = new ProfileManager();
      i18n.translate(Level.FINE, "ProfilesLoad");
    } catch (Exception e) {
      i18n.translate(Level.WARNING, "ProfilesLoadError");
      instance.getLogger().warning(e.toString());
    }
  }

  private void placeholderHeartInit() {
    final int amount = config.getInt("HeartsToGain", 2) / 2;
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
            new Eliminate(),
            new GiveHearts(),
            new Reload(),
            new SetHearts(),
            new Withdraw(),
    };
    if (isOnPaper()) {
      final PaperCommandManager pcm = new PaperCommandManager(instance);
      Arrays.stream(commands).forEach(pcm::registerCommand);
      i18n.translate(Level.FINE, "CommandsLoad");
    } else {
      final BukkitCommandManager bcm = new BukkitCommandManager(instance);
      Arrays.stream(commands).forEach(bcm::registerCommand);
      i18n.translate(Level.FINEST, "CommandsAsyncLoad");
    }
  }

  private void registerListeners() {
    final Listener[] listeners = {
            new PlayerPotionEffectListener(),
            new PlayerResurrectListener(),
            new HeartConsumeListener(),
            new PlayerClickListener(),
            new PlayerJoinListener(),
            new PlayerKillListener(),
            new HeartCraftListener(),
            new ProfileListener(),
    };
    Arrays.stream(listeners).forEach(listener -> pluginManager.registerEvents(listener, instance));
    i18n.translate(Level.FINE, "ListenersLoad");
  }

  /**
   * Initializes everything.
   *
   * @throws IOException undefined exception
   */
  public void init() throws IOException {
    messengerInit();

    i18n = new I18n();

    utils = new LSUtils();

    configInit();

    databaseInit();

    profilesInit();

    interaction = new Interaction(instance.getLogger(), config.getBoolean("Clean-Console"));

    skullMaker = new SkullMaker();

    placeholderHeartInit();

    heartRecipeManager = new HeartRecipeManager();

    papiHook = new Placeholder();

    LifeStealPlugin.getInstance().getServer().addRecipe(heartRecipeManager.getHeartRecipe());

    updateCheckerInit();

    metrics = new BStats(LifeStealPlugin.getInstance(), 15272);

    registerCommands();

    registerListeners();
  }
}