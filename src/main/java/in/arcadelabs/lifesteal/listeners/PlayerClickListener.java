package in.arcadelabs.lifesteal.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import static in.arcadelabs.lifesteal.LifeSteal.*;

public class PlayerClickListener implements Listener {

  public void onPlayerClick(PlayerInteractEvent event) {

    Player player = event.getPlayer();
    if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has
            (getNamespacedKey(), PersistentDataType.STRING)) {
      getUtils().setPlayerBaseHealth(player, getUtils().getPlayerBaseHealth(player) + 2);
      player.getInventory().getItemInMainHand().setAmount
              (player.getInventory().getItemInMainHand().getAmount() - 1);
    }
  }
}
