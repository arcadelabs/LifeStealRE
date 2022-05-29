package in.arcadelabs.lifesteal.listeners;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class PlayerClickListener implements Listener {

  @EventHandler
  public void onPlayerClick(PlayerInteractEvent event) {

    Player player = event.getPlayer();
    if (Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).getPersistentDataContainer().has
            (LifeStealPlugin.getLifeSteal().getNamespacedKey(), PersistentDataType.STRING)) {
      LifeStealPlugin.getLifeSteal().getUtils().setPlayerBaseHealth(player,
              LifeStealPlugin.getLifeSteal().getUtils().getPlayerBaseHealth(player) + 2);
      player.getInventory().getItemInMainHand().setAmount
              (player.getInventory().getItemInMainHand().getAmount() - 1);
    }
  }
}
