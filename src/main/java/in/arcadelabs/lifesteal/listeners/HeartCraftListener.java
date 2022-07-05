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

import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.hearts.HeartItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class HeartCraftListener implements Listener {
  private HeartItemManager heartItemManager;
  private ItemStack replacementHeart;

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onCraftEvent(final CraftItemEvent event) {
    if (!(Objects.equals(event.getRecipe().getResult(), LifeStealPlugin.getLifeSteal().getPlaceholderHeart()))) return;
    if (event.isShiftClick()) event.setCancelled(true);
    heartItemManager = new HeartItemManager(HeartItemManager.Mode.valueOf(LifeStealPlugin.getLifeSteal()
            .getHeartConfig().getString("Hearts.Mode.OnCraft")))
            .prepareIngedients()
            .cookHeart();
    replacementHeart = heartItemManager.getHeartItem();
    event.getInventory().setResult(replacementHeart);
  }
}