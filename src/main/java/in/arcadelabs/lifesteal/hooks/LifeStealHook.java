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
import in.arcadelabs.lifesteal.listeners.PlayerJoinListener;
import in.arcadelabs.lifesteal.listeners.PlayerKillListener;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.net.URL;
import java.util.Arrays;

import static in.arcadelabs.lifesteal.LifeStealPlugin.getInstance;

@Getter
public class LifeStealHook {

  private PluginManager PM = Bukkit.getPluginManager();
  private Placeholder papiHook;
  private SpigotMessenger messenger;

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
      PaperCommandManager pcm = new PaperCommandManager(getInstance());
      Arrays.stream(commands).forEach(pcm::registerCommand);
    } else {
      BukkitCommandManager bcm = new BukkitCommandManager(getInstance());
      Arrays.stream(commands).forEach(bcm::registerCommand);
    }
  }

  private void registerListener() {
    final Listener[] listeners = new Listener[]{
            new PlayerJoinListener(),
            new PlayerKillListener()
    };
    Arrays.stream(listeners).forEach(listener -> PM.registerEvents(listener, getInstance()));
  }

  public void init() throws Exception {

    messenger = SpigotMessenger
            .builder()
            .setPlugin(getInstance())
            .defaultToMiniMessageTranslator()
            .build();

    papiHook = new Placeholder();

    registerCommands();
    registerListener();

    new UpdateChecker(getInstance(), new URL("https://docs.taggernation.com/greetings-update.yml"), 6000)
            .setNotificationPermission("greetings.update")
            .enableOpNotification(true)
            .setup();

    BStats metrics = new BStats(getInstance(), 15272);
  }
}
