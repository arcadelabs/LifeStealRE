package in.arcadelabs.lifesteal.listeners;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {

    Player player = event.getPlayer();
    double defaultHealth = LifeStealPlugin.getInstance().getConfig().getDouble("DefaultHealth");

    if (!player.hasPlayedBefore()) {
      LifeStealPlugin.getLifeSteal().getUtils().setPlayerBaseHealth(player, defaultHealth);
    }
  }
}