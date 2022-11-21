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

import in.arcadelabs.lifesteal.api.database.profile.IStatisticsManager;
import in.arcadelabs.lifesteal.api.hearts.HeartItemManager;
import in.arcadelabs.lifesteal.core.LifeSteal;
import in.arcadelabs.lifesteal.core.LifeStealPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerDeathListener implements Listener {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final IStatisticsManager IStatisticsManager = this.lifeSteal.getIStatisticsManager();
  private HeartItemManager heartItemManager;
  private ItemStack replacementHeart;
  private List<String> disabledWorlds, disabledWorldsNatural;

  @EventHandler
  public void onPlayerKilled(final PlayerDeathEvent event) {

    final Player victim = event.getEntity();
    final int lostHearts = this.lifeSteal.getConfig().getInt("HeartsToTransfer", 1);
    if (this.lifeSteal.getUtils().getPlayerHearts(victim) == lostHearts) {
      if (victim.getKiller() == null) {
        this.lifeSteal.getInteraction().broadcast(
                this.lifeSteal.getUtils().getEliminationMessage(victim.getLastDamageCause().getCause()), victim);
      } else {
        this.lifeSteal.getInteraction().broadcast(this.lifeSteal.getKey("Messages.Elimination.ByPlayer"), victim);
      }
      this.IStatisticsManager.setCurrentHearts(victim, this.IStatisticsManager.getCurrentHearts(victim) - lostHearts)
              .setLostHearts(victim, this.IStatisticsManager.getLostHearts(victim) + lostHearts)
              .update(victim);
      this.lifeSteal.getUtils().handleElimination(victim, event);
    } else {
      if (victim.getKiller() == null) {
        if (this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Drops.Other").size() != 0) {
          this.disabledWorlds = this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Drops.Other");
        }
        if (!(this.disabledWorlds.contains(victim.getWorld().getName()))) {
          this.heartItemManager = new HeartItemManager()
                  .setMode(HeartItemManager.Mode.valueOf(this.lifeSteal.getHeartConfig().getString("Hearts.Mode.OnDeath")))
                  .prepareIngedients()
                  .cookHeart();
          this.replacementHeart = this.heartItemManager.getHeartItem();
          this.lifeSteal.getUtils().setPlayerHearts(victim, this.lifeSteal.getUtils().getPlayerHearts(victim) - lostHearts);

          this.IStatisticsManager.setCurrentHearts(victim, this.IStatisticsManager.getCurrentHearts(victim) - lostHearts)
                  .setLostHearts(victim, this.IStatisticsManager.getLostHearts(victim) + lostHearts)
                  .update(victim);

          victim.getWorld().dropItemNaturally(victim.getLocation(), this.replacementHeart);
        } else {
          victim.sendMessage(this.lifeSteal.getUtils().formatString(this.lifeSteal.getKey("Messages.DisabledStuff.Worlds.Heart-Drops.Other")));
        }
      } else {
        if (this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Drops.Player-Kill").size() != 0) {
          this.disabledWorldsNatural = this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Drops.Player-Kill");
        }
        if (!(this.disabledWorldsNatural.contains(victim.getWorld().getName()))) {
          if (!this.lifeSteal.getConfig().getInt("Max-Hearts").equals(-1) &&
                  this.lifeSteal.getConfig().getInt("Max-Hearts").equals((int) this.lifeSteal.getUtils().getPlayerHearts(victim.getKiller()))) {
            victim.getKiller().sendMessage(this.lifeSteal.getMiniMessage().deserialize(this.lifeSteal.getKey("Messages.MaxHeartsReached.OnDeath"),
                    Placeholder.component("location", Component.text(
                            "x:" + Math.round(event.getPlayer().getLocation().getX()) +
                                    ", y:" + Math.round(event.getPlayer().getLocation().getY()) +
                                    ", z:" + Math.round(event.getPlayer().getLocation().getZ())))));
            this.heartItemManager = new HeartItemManager()
                    .setMode(HeartItemManager.Mode.valueOf(this.lifeSteal.getHeartConfig().getString("Hearts.Mode.OnDeath")))
                    .prepareIngedients()
                    .cookHeart();
            this.replacementHeart = this.heartItemManager.getHeartItem();
            this.lifeSteal.getUtils().setPlayerHearts(victim, this.lifeSteal.getUtils().getPlayerHearts(victim) - lostHearts);

            this.IStatisticsManager.setCurrentHearts(victim, this.IStatisticsManager.getCurrentHearts(victim) - lostHearts)
                    .setLostHearts(victim, this.IStatisticsManager.getLostHearts(victim) + lostHearts)
                    .update(victim);
            victim.getWorld().dropItemNaturally(victim.getLocation(), this.replacementHeart);
          } else {
            this.lifeSteal.getUtils().transferHearts(victim, victim.getKiller());
            this.IStatisticsManager.setCurrentHearts(victim, this.IStatisticsManager.getCurrentHearts(victim) - lostHearts)
                    .setCurrentHearts(victim.getKiller(), this.IStatisticsManager.getCurrentHearts(victim.getKiller()) + lostHearts)
                    .setLostHearts(victim, this.IStatisticsManager.getLostHearts(victim) + lostHearts)
                    .setPeakReachedHearts(victim.getKiller(), this.IStatisticsManager.getPeakReachedHearts(victim.getKiller()) + lostHearts)
                    .update(victim);
          }
        } else {
          victim.getKiller().sendMessage(this.lifeSteal.getUtils().formatString(
                  this.lifeSteal.getKey("Messages.DisabledStuff.Worlds.Heart-Drops.Player-Kill.Killer")));
          victim.sendMessage(this.lifeSteal.getUtils().formatString(
                  this.lifeSteal.getKey("Messages.DisabledStuff.Worlds.Heart-Drops.Player-Kill.Victim")));
        }
      }
    }
  }
}
