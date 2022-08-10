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
import in.arcadelabs.lifesteal.hearts.HeartItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerDeathListener implements Listener {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private HeartItemManager heartItemManager;
  private ItemStack replacementHeart;
  private List<String> disabledWorlds, disabledWorldsNatural;

  @EventHandler
  public void onPlayerKilled(final PlayerDeathEvent event) {

    final Player victim = event.getEntity();
    final int lostHearts = lifeSteal.getConfig().getInt("HeartsToTransfer", 1);
    if (lifeSteal.getUtils().getPlayerHearts(victim) == 1) {
      if (victim.getKiller() == null) {
        lifeSteal.getInteraction().broadcast(
                lifeSteal.getUtils().getEliminationMessage(victim.getLastDamageCause().getCause()), victim);
      } else {
        lifeSteal.getInteraction().broadcast(lifeSteal.getKey("Messages.Elimination.ByPlayer"), victim);
      }
      lifeSteal.getProfileManager().getProfileCache().get
              (victim.getUniqueId()).setCurrentHearts(
                      lifeSteal.getProfileManager().getProfileCache().get(victim.getUniqueId()).getCurrentHearts() - 1);
      lifeSteal.getProfileManager().getProfileCache().get
              (victim.getUniqueId()).setLostHearts(
              lifeSteal.getProfileManager().getProfileCache().get(victim.getUniqueId()).getLostHearts() + 1);
      lifeSteal.getUtils().handleElimination(victim, event);
    } else {
      if (victim.getKiller() == null) {
        if (lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Drops.Other").size() != 0) {
          disabledWorlds = lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Drops.Other");
        }
        if (!(disabledWorlds.contains(victim.getWorld().getName()))) {
          heartItemManager = new HeartItemManager(HeartItemManager.Mode.valueOf(lifeSteal.getHeartConfig().getString("Hearts.Mode.OnDeath")))
                  .prepareIngedients()
                  .cookHeart();
          replacementHeart = heartItemManager.getHeartItem();
          lifeSteal.getUtils().setPlayerHearts(victim, lifeSteal.getUtils().getPlayerHearts(victim) - lostHearts);
          lifeSteal.getProfileManager().getProfileCache().get
                  (victim.getUniqueId()).setCurrentHearts(
                          lifeSteal.getProfileManager().getProfileCache().get(victim.getUniqueId()).getCurrentHearts() - lostHearts);
          lifeSteal.getProfileManager().getProfileCache().get
                  (victim.getUniqueId()).setLostHearts(
                  lifeSteal.getProfileManager().getProfileCache().get(victim.getUniqueId()).getLostHearts() + lostHearts);
          victim.getWorld().dropItemNaturally(victim.getLocation(), replacementHeart);
        } else {
          victim.sendMessage(lifeSteal.getUtils().formatString(lifeSteal.getKey("Messages.DisabledStuff.Worlds.Heart-Drops.Other")));
        }
      } else {
        if (lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Drops.Player-Kill").size() != 0) {
          disabledWorldsNatural = lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Drops.Player-Kill");
        }
        if (!(disabledWorldsNatural.contains(victim.getWorld().getName()))) {
          lifeSteal.getUtils().transferHearts(victim, victim.getKiller());
          lifeSteal.getProfileManager().getProfileCache().get
                  (victim.getUniqueId()).setCurrentHearts(
                          lifeSteal.getProfileManager().getProfileCache().get(victim.getUniqueId()).getCurrentHearts() - lostHearts);
          lifeSteal.getProfileManager().getProfileCache().get
                  (victim.getKiller().getUniqueId()).setCurrentHearts(
                          lifeSteal.getProfileManager().getProfileCache().get(victim.getKiller().getUniqueId()).getCurrentHearts() + lostHearts);
          lifeSteal.getProfileManager().getProfileCache().get
                  (victim.getUniqueId()).setLostHearts(
                  lifeSteal.getProfileManager().getProfileCache().get(victim.getUniqueId()).getLostHearts() + lostHearts);
          lifeSteal.getProfileManager().getProfileCache().get
                  (victim.getUniqueId()).setPeakHeartsReached(
                  lifeSteal.getProfileManager().getProfileCache().get(victim.getUniqueId()).getPeakHeartsReached() + lostHearts);
        } else {
          victim.getKiller().sendMessage(lifeSteal.getUtils().formatString(
                  lifeSteal.getKey("Messages.DisabledStuff.Worlds.Heart-Drops.Player-Kill.Killer")));
          victim.sendMessage(lifeSteal.getUtils().formatString(
                  lifeSteal.getKey("Messages.DisabledStuff.Worlds.Heart-Drops.Player-Kill.Victim")));
        }
      }
    }
  }
}