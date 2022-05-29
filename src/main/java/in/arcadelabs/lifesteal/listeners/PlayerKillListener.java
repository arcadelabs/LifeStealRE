package in.arcadelabs.lifesteal.listeners;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKillListener implements Listener {

  @EventHandler
  public void onPlayerKilled(PlayerDeathEvent event) {

    Player victim = event.getEntity();

    if (LifeStealPlugin.getLifeSteal().getUtils().getPlayerBaseHealth(victim) == 0) {
      victim.setGameMode(GameMode.SPECTATOR);
    } else {
      if (victim.getKiller() == null) {
        LifeStealPlugin.getLifeSteal().getUtils().setPlayerBaseHealth(victim,
                LifeStealPlugin.getLifeSteal().getUtils().getPlayerBaseHealth(victim) - 2.0);
        victim.getWorld().dropItemNaturally(victim.getLocation(),
                LifeStealPlugin.getLifeSteal().getRecipeManager().getHeartItem());
      } else {
        LifeStealPlugin.getLifeSteal().getUtils().transferHealth(victim, victim.getKiller());
      }
    }
  }
}