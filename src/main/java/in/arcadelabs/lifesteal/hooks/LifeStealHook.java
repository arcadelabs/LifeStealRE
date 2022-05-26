package in.arcadelabs.lifesteal.hooks;

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
import in.arcadelabs.lifesteal.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.net.URL;
import java.util.Arrays;

import static in.arcadelabs.lifesteal.LifeSteal.plugin;

public class LifeStealHook {

  public static Placeholder papiHook;
  public static ConfigUtils configUtils;
  public static SpigotMessenger messenger;
  public static PluginManager PM = Bukkit.getPluginManager();

  private boolean isOnPaper() {
    try {
      Class.forName("com.destroystokyo.paper.ParticleBuilder");
      return true;
    } catch (ClassNotFoundException ignored) {
      return false;
    }
  }

  private void registerCommands() {
    final BaseCommand[] commands = new BaseCommand[]{
            new Eliminate(),
            new Reload(),
            new Stats(),
            new Withdraw()
    };
    if (isOnPaper()) {
      PaperCommandManager pcm = new PaperCommandManager(plugin);
      Arrays.stream(commands).forEach(pcm::registerCommand);
    } else {
      BukkitCommandManager bcm = new BukkitCommandManager(plugin);
      Arrays.stream(commands).forEach(bcm::registerCommand);
    }
  }

  private void registerListener() {
    final Listener[] listeners = new Listener[]{
            new PlayerJoinListener(),
            new PlayerKillListener()
    };
    Arrays.stream(listeners).forEach(listener -> PM.registerEvents(listener, plugin));
  }

  public void init() throws Exception {

    messenger = SpigotMessenger
            .builder()
            .setPlugin(plugin)
            .defaultToMiniMessageTranslator()
            .build();

    papiHook = new Placeholder();
    configUtils = new ConfigUtils();

    configUtils.loadFiles();

    registerCommands();
    registerListener();

    new UpdateChecker(plugin, new URL("https://docs.taggernation.com/greetings-update.yml"), 6000)
            .setNotificationPermission("greetings.update")
            .enableOpNotification(true)
            .setup();

    BStats metrics = new BStats(plugin, 15272);
  }

}
