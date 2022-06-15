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
import in.arcadelabs.libs.aikar.acf.BukkitCommandManager;
import in.arcadelabs.libs.aikar.acf.PaperCommandManager;
import in.arcadelabs.lifesteal.utils.LSUtils;
import in.arcadelabs.lifesteal.utils.HeartRecipeManager;
import in.arcadelabs.lifesteal.utils.backend.ClassRegistration;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;

import java.net.URL;

@Getter
public class LifeStealManager {

  private final PluginManager PM = Bukkit.getPluginManager();
  private final Gson GSON = new Gson();
  private LSUtils utils;
  private HeartRecipeManager heartRecipeManager;
  private NamespacedKey namespacedKey;
  private Placeholder papiHook;
  private SpigotMessenger messenger;
  private Config configYML;
  private FileConfiguration config;
  private BStats metrics;

  private ClassRegistration classRegistration;

  private boolean isOnPaper() {
    try {
      Class.forName("com.destroystokyo.paper.ParticleBuilder");
      return true;
    } catch (ClassNotFoundException ignored) {
      return false;
    }
  }

  private void registerCommands() {
    if (isOnPaper()) {
      PaperCommandManager pcm = new PaperCommandManager(LifeStealPlugin.getInstance());
      pcm.registerCommand(new LifeStealCommands());
    } else {
      BukkitCommandManager bcm = new BukkitCommandManager(LifeStealPlugin.getInstance());
      bcm.registerCommand(new LifeStealCommands());
    }
  }

  public void init() throws Exception {

    classRegistration = new ClassRegistration();
    messenger = SpigotMessenger
            .builder()
            .setPlugin(LifeStealPlugin.getInstance())
            .defaultToMiniMessageTranslator()
            .build();

    try {
      configYML = new Config(LifeStealPlugin.getInstance(), "Config.yml", false, true);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      configYML.updateConfig("2.0", "version");
    } catch (Exception e) {
      e.printStackTrace();
    }

    config = configYML.getConfig();

    utils = new LSUtils();
    namespacedKey = new NamespacedKey(LifeStealPlugin.getInstance(), "lifesteal_heart");
    heartRecipeManager = new HeartRecipeManager();

    papiHook = new Placeholder();

    registerCommands();
    classRegistration.init("in.arcadelabs.lifesteal.listeners");

    LifeStealPlugin.getInstance().getServer().addRecipe(getHeartRecipeManager().getHeartRecipe());

    new UpdateChecker(LifeStealPlugin.getInstance(), new URL("https://docs.taggernation.com/greetings-update.yml"), 6000)
            .setNotificationPermission("greetings.update")
            .enableOpNotification(true)
            .setup();

    metrics = new BStats(LifeStealPlugin.getInstance(), 15272);
  }

  public FileConfiguration getConfiguration() {
    return this.config;
  }

  public Config getConfigYML() {
    return this.configYML;
  }
}
