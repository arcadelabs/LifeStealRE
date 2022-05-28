package in.arcadelabs.lifesteal.listeners;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerClickListener implements Listener {

  @EventHandler
  public void onPlayerClick(PlayerInteractEvent event) {

    Player player = event.getPlayer();
    if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has
            (LifeStealPlugin.getInstance().getNamespacedKey(), PersistentDataType.STRING)) {
      LifeStealPlugin.getInstance().getUtils().setPlayerBaseHealth(player, LifeStealPlugin.getInstance().getUtils().getPlayerBaseHealth(player) + 2);
      player.getInventory().getItemInMainHand().setAmount
              (player.getInventory().getItemInMainHand().getAmount() - 1);
    }
  }
}
