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

package in.arcadelabs.lifesteal.utils;

import in.arcadelabs.labaide.experience.ExperienceManager;
import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.api.enums.LifeState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Utils {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final MiniMessage miniMessage = MiniMessage.miniMessage();
  private final LegacyComponentSerializer legecySerializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();

  /**
   * Command dispatcher.
   *
   * @param URI    the uri
   * @param player the player
   */
  public void commandDispatcher(final String URI, final Player player) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
            this.legecySerializer.serialize(this.miniMessage.deserialize(URI, Placeholder.component("player", player.name()))));
  }

  /**
   * String to component list.
   *
   * @param stringList       the string list
   * @param placeholder      the placeholder
   * @param placeholderValue the placeholder value
   * @return the list
   */
  public List<Component> stringToComponentList(final List<String> stringList, final String placeholder, final int placeholderValue) {
    final List<Component> formattedList = new ArrayList<>();
    for (final String list : stringList) {
      formattedList.add(this.miniMessage.deserialize(list,
              Placeholder.component(placeholder, Component.text(placeholderValue))));
    }
    return formattedList;
  }

  /**
   * String to component list.
   *
   * @param stringList the string list
   * @param noItalics  the no italics
   * @return the list
   */
  public List<Component> stringToComponentList(final List<String> stringList, final boolean noItalics) {
    final List<Component> formattedList = new ArrayList<>();
    for (final String list : stringList) {
      if (noItalics) formattedList.add(this.miniMessage.deserialize(list).decoration(TextDecoration.ITALIC, false));
      else formattedList.add(this.miniMessage.deserialize(list));
    }
    return formattedList;
  }

  /**
   * Format string component.
   *
   * @param string the string
   * @return the component
   */
  public Component formatString(final String string) {
    return this.miniMessage.deserialize(string);
  }

  /**
   * Give heart effects.
   *
   * @param target    the target
   * @param heartMeta the heart meta
   * @param instance  the instance
   */
  public void giveHeartEffects(final Player target, final ItemMeta heartMeta, final JavaPlugin instance) {
    final String itemType = heartMeta.getPersistentDataContainer().get
            (this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_itemtype"), PersistentDataType.STRING);
    final String itemIndex = heartMeta.getPersistentDataContainer().get
            (this.lifeSteal.getNamespacedKeyBuilder().getNewKey("heart_itemindex"), PersistentDataType.STRING);
    final String effectsPath = "Hearts.Types." + itemType + "." + itemIndex + ".Properties.Effects";
    final Set<String> indexSet = Objects.requireNonNull(this.lifeSteal.getHeartConfig().getSection(effectsPath)).getRoutesAsStrings(false);

    for (int i = 0; i < indexSet.size(); i++) {
      final String[] indexList = indexSet.toArray(new String[0]);
      target.addPotionEffect(new PotionEffect(Objects.requireNonNull
              (PotionEffectType.getByName(Objects.requireNonNull(this.lifeSteal.getHeartConfig().getString(effectsPath + "." + indexList[i] + ".Type")))),
              this.lifeSteal.getHeartConfig().getInt(effectsPath + "." + indexList[i] + ".Duration") * 20,
              this.lifeSteal.getHeartConfig().getInt(effectsPath + "." + indexList[i] + ".Power"),
              this.lifeSteal.getHeartConfig().getBoolean(effectsPath + "." + indexList[i] + ".ShowParticles", false),
              this.lifeSteal.getHeartConfig().getBoolean(effectsPath + "." + indexList[i] + ".ShowParticles", false)));
    }
  }

  /**
   * Spawn particles.
   *
   * @param player the player
   * @param type   the type
   */
  public void spawnParticles(final Player player, String type) {
//    Will work on this more later.
    switch (type) {
      case "heart":
        if (player.isPermissionSet("lifesteal.particles.heart")) {
          ParticleEffect.HEART.display(player.getLocation().add(0, 2, 0));
          ParticleEffect.HEART.display(player.getLocation().subtract(1, 0, 0));
          ParticleEffect.HEART.display(player.getLocation().add(1, 0, 0));
          ParticleEffect.HEART.display(player.getLocation().subtract(0, 0, 1));
          ParticleEffect.HEART.display(player.getLocation().add(0, 0, 1));
        }
      case "soul":
        if (player.isPermissionSet("lifesteal.particles.soul")) {
          ParticleEffect.SOUL.display(player.getLocation().add(0, 2, 0));
        }
    }
  }

  /**
   * Handle elimination.
   *
   * @param player the player
   */
  public void handleElimination(final Player player) {
    switch (this.lifeSteal.getConfig().getString("InventoryMode")) {
      case "DROP" -> {
        if (player.getInventory().isEmpty()) return;
        for (final ItemStack stuff : player.getInventory().getContents()) {
          assert stuff != null;
          player.getWorld().dropItemNaturally(player.getLocation(), stuff);
        }
      }
      case "SAVE_TO_RESTORE" -> this.lifeSteal.getSpiritFactory().saveInventory(player);
      case "CLEAR" -> player.getInventory().clear();
      case "NONE" -> player.saveData();
    }

    switch (this.lifeSteal.getConfig().getString("ExperienceMode")) {
      case "DROP" -> {
        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class).setExperience(ExperienceManager.getExp(player));
        player.setLevel(0);
        player.setExp(0);
        this.lifeSteal.getSpiritFactory().saveXP(player);
      }
      case "SAVE_TO_RESTORE" -> this.lifeSteal.getSpiritFactory().saveXP(player);
      case "CLEAR" -> {
        player.setExp(0);
        player.setLevel(0);
        this.lifeSteal.getSpiritFactory().saveXP(player);
      }
      case "NONE" -> {
        player.saveData();
        this.lifeSteal.getSpiritFactory().saveXP(player);
      }
    }

    switch (this.lifeSteal.getConfig().getString("Elimination")) {
      case "BANNED" -> {
        commandDispatcher(this.lifeSteal.getConfig().getString("Ban-Command-URI"), player);
        this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLifeState((LifeState.BANNED));
      }
      case "DEAD" -> {
        player.setGameMode(GameMode.ADVENTURE);
        this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLifeState((LifeState.DEAD));
      }
      case "SPIRIT" -> {
        this.lifeSteal.getSpiritFactory().addSpirit(player);
        this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLifeState((LifeState.SPIRIT));
      }
      case "AfterLife" -> {
        this.lifeSteal.getLogger().log(Logger.Level.DEBUG, Component.text("TTPP"));
        this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLifeState((LifeState.DEAD));
      }
    }
  }

  /**
   * Handle elimination.
   *
   * @param player the player
   * @param event  the event
   */
  public void handleElimination(final Player player, final PlayerDeathEvent event) {
    switch (this.lifeSteal.getConfig().getString("InventoryMode")) {
      case "DROP" -> event.setKeepInventory(false);
      case "SAVE_TO_RESTORE" -> this.lifeSteal.getSpiritFactory().saveInventory(player);
      case "CLEAR" -> player.getInventory().clear();
      case "NONE" -> player.saveData();
    }

    switch (this.lifeSteal.getConfig().getString("ExperienceMode")) {
      case "DROP" -> {
        event.setShouldDropExperience(true);
        event.setKeepLevel(false);
        this.lifeSteal.getSpiritFactory().saveXP(player);
      }
      case "SAVE_TO_RESTORE" -> this.lifeSteal.getSpiritFactory().saveXP(player);
      case "CLEAR" -> {
        player.setExp(0);
        player.setLevel(0);
        this.lifeSteal.getSpiritFactory().saveXP(player);
      }
      case "NONE" -> {
        player.saveData();
        this.lifeSteal.getSpiritFactory().saveXP(player);
      }
    }

    switch (this.lifeSteal.getConfig().getString("Elimination")) {
      case "BANNED" -> {
        commandDispatcher(this.lifeSteal.getConfig().getString("Ban-Command-URI"), player);
        this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLifeState((LifeState.BANNED));
      }
      case "DEAD" -> {
        player.setGameMode(GameMode.ADVENTURE);
        this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLifeState((LifeState.DEAD));
      }
      case "SPIRIT" -> {
        this.lifeSteal.getSpiritFactory().addSpirit(player);
        this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLifeState((LifeState.SPIRIT));
      }
      case "AfterLife" -> {
        this.lifeSteal.getLogger().log(Logger.Level.DEBUG, Component.text("TTPP"));
        this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLifeState((LifeState.DEAD));
      }
    }
  }

  /**
   * Handle revive.
   *
   * @param player the player
   */
  public void handleRevive(final Player player) {
    switch (this.lifeSteal.getConfig().getString("Elimination")) {
      case "BANNED" -> {
        commandDispatcher(this.lifeSteal.getConfig().getString("UnBan-Command-URI"), player);
        Bukkit.broadcast(this.miniMessage.deserialize(this.lifeSteal.getKey("Messages.Revive.ByUnban"),
                Placeholder.component("player", player.displayName())));
      }
      case "DEAD" -> player.setGameMode(GameMode.SURVIVAL);
      case "SPIRIT" -> this.lifeSteal.getSpiritFactory().removeSpirit(player);
      case "AfterLife" -> this.lifeSteal.getLogger().log(Logger.Level.DEBUG, Component.text("TTPP"));
    }

    this.lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).setLifeState((LifeState.LIVING));
  }

  /**
   * Gets elimination message.
   *
   * @param damageCause the damage cause
   * @return the elimination message
   */
  public String getEliminationMessage(final EntityDamageEvent.DamageCause damageCause) {
    return switch (damageCause) {
      case CONTACT -> "Messages.Elimination.ByDamagingBlocks";
      case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> "Messages.Elimination.ByMob";
      case PROJECTILE -> "Messages.Elimination.ByProjectile";
      case SUFFOCATION -> "Messages.Elimination.BySuffocation";
      case FALL -> "Messages.Elimination.ByFallDamage";
      case FIRE, FIRE_TICK -> "Messages.Elimination.ByBurn";
      case LAVA -> "Messages.Elimination.ByLava";
      case DROWNING -> "Messages.Elimination.ByDrowning";
      case BLOCK_EXPLOSION -> "Messages.Elimination.ByExplosion";
      case ENTITY_EXPLOSION -> "Messages.Elimination.ByCreeper";
      case SUICIDE -> "Messages.Elimination.BySuicide";
      case VOID -> "Messages.Elimination.ByVoid";
      case LIGHTNING -> "Messages.Elimination.ByLightning";
      case STARVATION -> "Messages.Elimination.ByStarvation";
      case POISON -> "Messages.Elimination.ByPoison";
      case MAGIC -> "Messages.Elimination.ByMagic";
      case WITHER -> "Messages.Elimination.ByWither";
      case FALLING_BLOCK -> "Messages.Elimination.ByFallingBlock";
      case THORNS -> "Messages.Elimination.ByThorns";
      case DRAGON_BREATH -> "Messages.Elimination.ByDragonBreath";
      case FLY_INTO_WALL -> "Messages.Elimination.ByKineticEnergy";
      case HOT_FLOOR -> "Messages.Elimination.ByMagmaBlock";
      case CRAMMING -> "Messages.Elimination.ByCramming";
      case FREEZE -> "Messages.Elimination.ByFreeze";
      default -> "Messages.Elimination.Other";
    };
  }
}
