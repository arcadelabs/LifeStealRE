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

import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.api.database.profile.IStatisticsManager;
import in.arcadelabs.lifesteal.api.hearts.Heart;
import in.arcadelabs.lifesteal.core.LifeSteal;
import in.arcadelabs.lifesteal.core.LifeStealPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class HeartConsumeListener implements Listener {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final LifeStealPlugin instance = LifeStealPlugin.getInstance();
  private final IStatisticsManager IStatisticsManager = this.lifeSteal.getIStatisticsManager();
  private List<String> disabledWorlds;

  @EventHandler
  public void onPlayerClick(final PlayerInteractEvent event) {
    if (!LifeStealPlugin.getLifeSteal().getSpiritFactory().getSpirits().contains(event.getPlayer())) {
      final Player player = event.getPlayer();

      if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
      if (!(player.getInventory().getItemInMainHand().hasItemMeta())) return;
      final ItemMeta heartMeta = player.getInventory().getItemInMainHand().getItemMeta();

      if (!(heartMeta != null && heartMeta.getPersistentDataContainer()
              .has(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_item"), PersistentDataType.STRING)))
        return;
      if (!this.lifeSteal.getConfig().getInt("Max-Hearts").equals(-1) &&
              this.lifeSteal.getConfig().getInt("Max-Hearts") <= this.lifeSteal.getUtils().getPlayerHearts(player)) {
        player.sendMessage(this.lifeSteal.getUtils().formatString(this.lifeSteal.getKey("Messages.MaxHeartsReached.Consume")));
        event.setCancelled(true);
      } else {
        if (player.getInventory().getItemInMainHand().getType().isEdible()) {
          if (player.getFoodLevel() == 20) {
            player.setFoodLevel(19);
            this.instance.getServer().getScheduler().scheduleSyncDelayedTask(this.instance, () -> player.setFoodLevel(20), 1L);
          }
        } else {
          if (this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Consume").size() != 0) {
            this.disabledWorlds = this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Consume");
          }
          if (!(this.disabledWorlds.contains(player.getWorld().getName()))) {

            if (!this.lifeSteal.getConsumeCooldown().isOnCooldown(player.getUniqueId())) {
              final Heart heart = new Heart(heartMeta);

              this.lifeSteal.getUtils().setPlayerHearts(player, this.lifeSteal.getUtils().getPlayerHearts(player)
                      + heart.getHealthPoints());
              player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
              this.lifeSteal.getUtils().spawnParticles(player, "heart");
              this.lifeSteal.getUtils().giveHeartEffects(player, heartMeta, this.instance);
              this.lifeSteal.getInteraction().verbose(Logger.Level.INFO, heart.getConsumeMessages(), player, heart.getConsumeSound());

              this.IStatisticsManager.setCurrentHearts(player, this.IStatisticsManager.getCurrentHearts(player) + (int) heart.getHealthPoints())
                      .setPeakReachedHearts(player, this.IStatisticsManager.getPeakReachedHearts(player) + (int) heart.getHealthPoints())
                      .update(player);

              switch (heart.getType() != null ? heart.getType() : "Normal") {
                case "Blessed" ->
                        this.IStatisticsManager.setBlessedHearts(player, this.IStatisticsManager.getBlessedHearts(player) + (int) heart.getHealthPoints())
                                .update(player);
                case "Normal" ->
                        this.IStatisticsManager.setNormalHearts(player, this.IStatisticsManager.getNormalHearts(player) + (int) heart.getHealthPoints())
                                .update(player);
                case "Cursed" ->
                        this.IStatisticsManager.setCursedHearts(player, this.IStatisticsManager.getCursedHearts(player) + (int) heart.getHealthPoints())
                                .update(player);
              }

              Bukkit.getScheduler().scheduleSyncDelayedTask(this.instance, () ->
                      player.setHealth(Math.min(player.getHealth() +
                              heart.getHealthPoints(), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())), 20L);

              if (this.lifeSteal.getConfig().getInt("Cooldowns.Heart-Consume") >= 0)
                this.lifeSteal.getConsumeCooldown().setCooldown(player.getUniqueId());

            } else {
              event.setCancelled(true);
              player.sendMessage(this.lifeSteal.getMiniMessage().deserialize(this.lifeSteal.getKey("Messages.CooldownMessage.Heart-Consume"),
                      Placeholder.component("seconds", Component.text(this.lifeSteal.getConsumeCooldown().getRemainingTime(player.getUniqueId())))));
            }
          } else {
            event.setCancelled(true);
            player.sendMessage(this.lifeSteal.getUtils().formatString(this.lifeSteal.getKey("Messages.DisabledStuff.Worlds.Heart-Consume")));
          }
        }
      }
    } else event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerEat(final PlayerItemConsumeEvent event) {
    final Player player = event.getPlayer();

    if (!(event.getItem().hasItemMeta())) return;
    final ItemMeta heartMeta = event.getItem().getItemMeta();

    if (!(heartMeta != null && heartMeta.getPersistentDataContainer()
            .has(this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_item"), PersistentDataType.STRING))) return;
    if (!this.lifeSteal.getConfig().getInt("Max-Hearts").equals(-1) &&
            this.lifeSteal.getConfig().getInt("Max-Hearts").equals((int) this.lifeSteal.getUtils().getPlayerHearts(player))) {
      player.sendMessage(this.lifeSteal.getUtils().formatString(this.lifeSteal.getKey("Messages.MaxHeartsReached.Consume")));
      event.setCancelled(true);
    } else {

      if (this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Consume").size() != 0) {
        this.disabledWorlds = this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Consume");
      }
      if (!(this.disabledWorlds.contains(player.getWorld().getName()))) {
        if (!this.lifeSteal.getConsumeCooldown().isOnCooldown(player.getUniqueId())) {
          final Heart heart = new Heart(heartMeta);
          this.lifeSteal.getUtils().setPlayerHearts(player, this.lifeSteal.getUtils().getPlayerHearts(player)
                  + heart.getHealthPoints());
          this.lifeSteal.getUtils().giveHeartEffects(player, heartMeta, this.instance);
          this.lifeSteal.getInteraction().verbose(Logger.Level.INFO, heart.getConsumeMessages(), player, heart.getConsumeSound());

          this.IStatisticsManager.setCurrentHearts(player, this.IStatisticsManager.getCurrentHearts(player) + (int) heart.getHealthPoints())
                  .setPeakReachedHearts(player, this.IStatisticsManager.getPeakReachedHearts(player) + (int) heart.getHealthPoints())
                  .update(player);

          switch (heart.getType() != null ? heart.getType() : "Normal") {
            case "Blessed" ->
                    this.IStatisticsManager.setBlessedHearts(player, this.IStatisticsManager.getBlessedHearts(player) + (int) heart.getHealthPoints())
                            .update(player);
            case "Normal" ->
                    this.IStatisticsManager.setNormalHearts(player, this.IStatisticsManager.getNormalHearts(player) + (int) heart.getHealthPoints())
                            .update(player);
            case "Cursed" ->
                    this.IStatisticsManager.setCursedHearts(player, this.IStatisticsManager.getCursedHearts(player) + (int) heart.getHealthPoints())
                            .update(player);
          }

          Bukkit.getScheduler().scheduleSyncDelayedTask(this.instance, () ->
                  player.setHealth(Math.min(player.getHealth() +
                          heart.getHealthPoints(), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())), 20L);

          if (this.lifeSteal.getConfig().getInt("Cooldowns.Heart-Consume") >= 0)
            this.lifeSteal.getConsumeCooldown().setCooldown(player.getUniqueId());

        } else {
          event.setCancelled(true);
          player.sendMessage(this.lifeSteal.getMiniMessage().deserialize(this.lifeSteal.getKey("Messages.CooldownMessage.Heart-Consume"),
                  Placeholder.component("seconds", Component.text(this.lifeSteal.getConsumeCooldown().getRemainingTime(player.getUniqueId())))));
        }
      } else {
        event.setCancelled(true);
        player.sendMessage(this.lifeSteal.getUtils().formatString(this.lifeSteal.getKey("Messages.DisabledStuff.Worlds.Heart-Consume")));
      }
    }
  }
}
