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

import in.arcadelabs.enums.Mode;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.hearts.HeartItemManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class HeartCraftListener implements Listener {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private HeartItemManager heartItemManager;
  private ItemStack replacementHeart;
  private List<String> disabledWorlds;

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onCraftEvent(final CraftItemEvent event) {
    if (!(Objects.equals(event.getRecipe().getResult(), this.lifeSteal.getPlaceholderHeart()))) return;
    Player player = (Player) event.getWhoClicked();
    if (this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Craft").size() != 0) {
      this.disabledWorlds = this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Craft");
    }
    if (!this.disabledWorlds.contains(player.getWorld().getName())) {
      if (event.isShiftClick()) event.setCancelled(true);
      if (!this.lifeSteal.getCraftCooldown().isOnCooldown(player.getUniqueId())) {
        this.heartItemManager = new HeartItemManager()
                .setMode(Mode.valueOf(this.lifeSteal
                        .getHeartConfig().getString("Hearts.Mode.OnCraft")))
                .prepareIngedients()
                .cookHeart();
        this.replacementHeart = this.heartItemManager.getHeartItem();
        event.getInventory().setResult(this.replacementHeart);

        if (this.lifeSteal.getConfig().getInt("Cooldowns.Heart-Craft") >= 0)
          this.lifeSteal.getCraftCooldown().setCooldown(player.getUniqueId());

      } else {
        player.sendMessage(this.lifeSteal.getMiniMessage().deserialize(this.lifeSteal.getKey("Messages.CooldownMessage.Heart-Craft"),
                Placeholder.component("seconds", Component.text(this.lifeSteal.getCraftCooldown().getRemainingTime(player.getUniqueId())))));
        event.setCancelled(true);
      }
    } else {
      event.setCancelled(true);
      player.sendMessage(this.lifeSteal.getUtils().formatString(this.lifeSteal.getKey("Messages.DisabledStuff.Worlds.Heart-Craft")));
    }
  }
}