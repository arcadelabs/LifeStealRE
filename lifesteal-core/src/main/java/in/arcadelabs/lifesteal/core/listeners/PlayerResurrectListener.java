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

package in.arcadelabs.lifesteal.core.listeners;

import in.arcadelabs.lifesteal.core.LifeStealPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

public class PlayerResurrectListener implements Listener {

  @EventHandler
  public void onPlayerResurrect(final EntityResurrectEvent event) {
    if (event.getEntity() instanceof final Player player) {
      if (event.isCancelled()) return;
      if (player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING ||
              player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
        if (LifeStealPlugin.getLifeSteal().getConfig().getBoolean("DisableTotem")) {
          event.setCancelled(LifeStealPlugin.getLifeSteal().getConfig().getBoolean("DisableTotem"));
          player.sendMessage(LifeStealPlugin.getLifeSteal().getUtils().formatString(
                  LifeStealPlugin.getLifeSteal().getKey("Messages.DisabledStuff.Totem")));
        }
      }
    }
  }
}
