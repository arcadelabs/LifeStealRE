/*
 *          LifeSteal - Yet another lifecore smp core.
 *                Copyright (C) 2022  Arcade Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package in.arcadelabs.lifesteal.listeners;

import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class PlayerClickListener implements Listener {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final LifeStealPlugin instance = LifeStealPlugin.getInstance();
  private List<String> disabledWorlds;

  @EventHandler
  public void onPlayerClick(final PlayerInteractEvent event) {
    final Player player = event.getPlayer();

    if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
    if (!(player.getInventory().getItemInMainHand().hasItemMeta())) return;
    final ItemMeta heartMeta = player.getInventory().getItemInMainHand().getItemMeta();

      if (!(heartMeta != null && heartMeta.getPersistentDataContainer()
              .has(new NamespacedKey(instance, "lifesteal_heart_item"), PersistentDataType.STRING))) return;
      if (player.getInventory().getItemInMainHand().getType().isEdible()) {
        if (player.getFoodLevel() == 20) player.setFoodLevel(19);
        instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, () -> player.setFoodLevel(20), 1L);
      } else {
        if (lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Consume").size() != 0) {
          disabledWorlds = lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Consume");
        }
        if (!(disabledWorlds.contains(player.getWorld().getName()))) {
          final double healthPoints = Objects.requireNonNull(heartMeta.getPersistentDataContainer().get
                  (new NamespacedKey(instance, "lifesteal_heart_healthpoints"), PersistentDataType.DOUBLE));
          final String type = heartMeta.getPersistentDataContainer().get
                  (new NamespacedKey(instance, "lifesteal_heart_itemtype"), PersistentDataType.STRING);
          final String index = heartMeta.getPersistentDataContainer().get
                  (new NamespacedKey(instance, "lifesteal_heart_itemindex"), PersistentDataType.STRING);
          final String consumeSound = Objects.requireNonNull(heartMeta.getPersistentDataContainer().get
                  (new NamespacedKey(instance, "lifesteal_heart_consumesound"), PersistentDataType.STRING));
          final List<Component> consumeMessages = lifeSteal.getUtils().stringToComponentList(lifeSteal.getHeartConfig().getStringList
                  ("Hearts.Types." + type + "." + index + ".Properties.ConsumeMessage"), false);

          lifeSteal.getUtils().setPlayerHearts(player, lifeSteal.getUtils().getPlayerHearts(player)
                  + healthPoints);
          player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
          lifeSteal.getUtils().spawnParticles(player, "heart");
          lifeSteal.getUtils().giveHeartEffects(player, heartMeta, instance);
          lifeSteal.getInteraction().retuurn(Logger.Level.INFO, consumeMessages, player, consumeSound);

          Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () ->
                  player.setHealth(Math.min(player.getHealth() +
                          healthPoints, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())), 20L);
        } else {
          event.setCancelled(true);
          player.sendMessage(lifeSteal.getUtils().formatString(lifeSteal.getKey("Messages.DisabledStuff.Worlds.Heart-Consume")));
        }
      }
    }
  }
}