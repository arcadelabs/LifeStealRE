package in.arcadelabs.lifesteal.utils.backend;

import in.arcadelabs.lifesteal.LifeStealManager;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.scheduler.BukkitTask;

@UtilityClass
public final class Executor {

  private final LifeStealPlugin plugin = LifeStealPlugin.getInstance();

    public BukkitTask run(Runnable runnable) {
        return plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    public BukkitTask runAsync(Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public BukkitTask runLater(Runnable runnable, long delay) {
        return plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public BukkitTask runLaterAsync(Runnable runnable, long delay) {
        return plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    public BukkitTask runTimer(Runnable runnable, long delay, long interval) {
        return plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, interval);
    }

    public BukkitTask runTimerAsync(Runnable runnable, long delay, long interval) {
        return plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, interval);
    }
}
