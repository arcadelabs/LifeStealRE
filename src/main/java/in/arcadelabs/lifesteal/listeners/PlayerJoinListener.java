package in.arcadelabs.lifesteal.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static in.arcadelabs.lifesteal.LifeSteal.utils;
import static in.arcadelabs.lifesteal.hooks.LifeStealHook.configUtils;

public class PlayerJoinListener implements Listener {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {

    Player player = event.getPlayer();
    double defaultHealth = configUtils.getConfig().getDouble("DefaultHealth");

    if (!player.hasPlayedBefore()) {
      utils.setPlayerBaseHealth(player, defaultHealth);
    }
  }
}