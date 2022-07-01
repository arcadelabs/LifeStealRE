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

import java.util.Objects;

public class PlayerKillListener implements Listener {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private HeartItemManager heartItemManager;
  private ItemStack replacementHeart;

  @EventHandler
  public void onPlayerKilled(final PlayerDeathEvent event) {

    final Player victim = event.getEntity();
    final int lostHearts = lifeSteal.getConfig().getInt("HeartsToLose", 2);
    if (lifeSteal.getUtils().getPlayerBaseHealth(victim) == 0) {
      if (victim.getKiller() == null) {
        switch (Objects.requireNonNull(victim.getLastDamageCause()).getCause()) {
          case CONTACT:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByDamagingBlocks", victim);
          case ENTITY_ATTACK:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByMob", victim);
          case ENTITY_SWEEP_ATTACK:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByMob", victim);
          case PROJECTILE:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByProjectile", victim);
          case SUFFOCATION:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.BySuffocation", victim);
          case FALL:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByFallDamage", victim);
          case FIRE:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByBurn", victim);
          case FIRE_TICK:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByBurn", victim);
          case LAVA:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByLava", victim);
          case DROWNING:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByDrowning", victim);
          case BLOCK_EXPLOSION:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByExplosion", victim);
          case ENTITY_EXPLOSION:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByCreeper", victim);
          case SUICIDE:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.BySuicide", victim);
          case VOID:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByVoid", victim);
          case LIGHTNING:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByLightning", victim);
          case STARVATION:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByStarvation", victim);
          case POISON:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByPoison", victim);
          case MAGIC:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByMagic", victim);
          case WITHER:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByWither", victim);
          case FALLING_BLOCK:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByFallingBlock", victim);
          case THORNS:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByThorns", victim);
          case DRAGON_BREATH:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByDragonBreath", victim);
          case FLY_INTO_WALL:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByKineticEnergy", victim);
          case HOT_FLOOR:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByMagmaBlock", victim);
          case CRAMMING:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByCramming", victim);
          case FREEZE:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByFreeze", victim);
          default:
            lifeSteal.getInteraction().broadcast("Messages.Elimination.ByCommand", victim);
        }
      } else {
        lifeSteal.getInteraction().broadcast("Messages.Elimination.ByPlayer", victim);}
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