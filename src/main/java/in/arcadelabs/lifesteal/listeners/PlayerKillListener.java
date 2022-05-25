package in.arcadelabs.lifesteal.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static in.arcadelabs.lifesteal.LifeSteal.utils;

public class PlayerKillListener implements Listener {

  @EventHandler
  public void onPlayerKilled(PlayerDeathEvent event) {

    Player victim = event.getEntity();

    if (utils.getPlayerBaseHealth(victim) == 0) {
      victim.setGameMode(GameMode.SPECTATOR);
    } else {
      if (victim.getKiller() == null) {
        utils.setPlayerBaseHealth(victim, utils.getPlayerBaseHealth(victim) - 2.0);
//      TODO - Drop heart at @killedPlayer 's position
      } else {
        utils.transferHealth(victim, victim.getKiller());
      }
    }
  }
}