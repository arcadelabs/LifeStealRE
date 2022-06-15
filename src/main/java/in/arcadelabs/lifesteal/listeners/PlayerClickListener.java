/*
 * LifeSteal - Yet another lifecore smp core.
 * Copyright (C) 2022  Arcade Labs
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

import in.arcadelabs.lifesteal.LifeStealManager;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.utils.event.Events;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;
import org.bukkit.persistence.PersistentDataType;

public class PlayerClickListener {

  private final LifeStealManager lifeSteal = LifeStealPlugin.getLifeSteal();

  public PlayerClickListener() {

    Events.subscribe(PlayerInteractEvent.class, event -> {
      Player player = event.getPlayer();
      if (!(event.getAction() == Action.RIGHT_CLICK_AIR)) return;
      if (Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).getPersistentDataContainer().has
          (new NamespacedKey(LifeStealPlugin.getInstance(), "lifesteal_heart_item"), PersistentDataType.STRING)) {
        lifeSteal.getUtils().setPlayerBaseHealth(player,
            lifeSteal.getUtils().getPlayerBaseHealth(player) + 2);
        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
      }
    }, EventPriority.HIGH);
  }
}
