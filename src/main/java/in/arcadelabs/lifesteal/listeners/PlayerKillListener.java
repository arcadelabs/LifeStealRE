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

import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.utils.HeartItemManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerKillListener implements Listener {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  HeartItemManager heartItemManager;
  ItemStack replacementHeart;

  @EventHandler
  public void onPlayerKilled(PlayerDeathEvent event) {

    Player victim = event.getEntity();
    int lostHearts = lifeSteal.getConfig().getInt("HeartsToLose", 2);
    if (lifeSteal.getUtils().getPlayerBaseHealth(victim) == 0) {
      victim.setGameMode(GameMode.SPECTATOR);
    } else {
      if (victim.getKiller() == null) {
        heartItemManager = new HeartItemManager(HeartItemManager.Mode.valueOf(lifeSteal.getHeartConfig().getString("Hearts.Mode.OnDeath")))
                .prepareIngedients()
                .cookHeart();
        replacementHeart = heartItemManager.getHeartItem();
        lifeSteal.getUtils().setPlayerBaseHealth(victim, lifeSteal.getUtils().getPlayerBaseHealth(victim) - lostHearts);
        victim.getWorld().dropItemNaturally(victim.getLocation(), replacementHeart);
      } else {
        lifeSteal.getUtils().transferHealth(victim, victim.getKiller());
      }
    }
  }
}