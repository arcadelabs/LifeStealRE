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
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class HeartConsumeListener implements Listener {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final LifeStealPlugin instance = LifeStealPlugin.getInstance();
  private List<String> disabledWorlds;

  @EventHandler
  public void onPlayerEat(final PlayerItemConsumeEvent event) {
    final Player player = event.getPlayer();

    if (!(event.getItem().hasItemMeta())) return;
    final ItemMeta heartMeta = event.getItem().getItemMeta();

    if (!(heartMeta != null && heartMeta.getPersistentDataContainer()
            .has(new NamespacedKey(instance, "lifesteal_heart_item"), PersistentDataType.STRING))) return;

    if (lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Consume").size() != 0) {
      disabledWorlds = lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Consume");
    }
    if (!(disabledWorlds.contains(player.getWorld().toString().toLowerCase()))) {
      final double healthPoints = Objects.requireNonNull(heartMeta.getPersistentDataContainer().get
              (new NamespacedKey(instance, "lifesteal_heart_healthpoints"), PersistentDataType.DOUBLE));
      final String type = heartMeta.getPersistentDataContainer().get
              (new NamespacedKey(instance, "lifesteal_heart_itemtype"), PersistentDataType.STRING);
      final String index = heartMeta.getPersistentDataContainer().get
              (new NamespacedKey(instance, "lifesteal_heart_itemindex"), PersistentDataType.STRING);
      final String consumeSound = Objects.requireNonNull(heartMeta.getPersistentDataContainer().get
              (new NamespacedKey(instance, "lifesteal_heart_consumesound"), PersistentDataType.STRING));
      final List<String> consumeMessages = lifeSteal.getHeartConfig().getStringList
              ("Hearts.Types." + type + "." + index + ".Properties.ConsumeMessage");

    lifeSteal.getUtils().setPlayerBaseHealth(player, lifeSteal.getUtils().getPlayerBaseHealth(player)
            + healthPoints);
    player.setHealth(player.getHealth() + healthPoints);
    lifeSteal.getUtils().giveHeartEffects(player, heartMeta, instance);
    lifeSteal.getInteraction().retuurn(Level.INFO, consumeMessages, player, consumeSound);
  }
}