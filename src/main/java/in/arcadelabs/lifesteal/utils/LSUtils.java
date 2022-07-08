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

package in.arcadelabs.lifesteal.utils;

import in.arcadelabs.libs.adventure.adventure.text.Component;
import in.arcadelabs.libs.adventure.adventure.text.minimessage.MiniMessage;
import in.arcadelabs.libs.adventure.adventure.text.minimessage.tag.resolver.Placeholder;
import in.arcadelabs.libs.adventure.adventure.text.serializer.legacy.LegacyComponentSerializer;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
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

public class LSUtils {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final LegacyComponentSerializer legecySerializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();
  private final int looseHearts = lifeSteal.getConfig().getInt("HeartsToLose", 2);
  private final int gainHearts = lifeSteal.getConfig().getInt("HeartsToGain", 2);

  /**
   * Gets player base health.
   *
   * @param player the player
   * @return the player base health
   */
  public double getPlayerBaseHealth(final Player player) {
    return Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
  }

  /**
   * Sets player base health.
   *
   * @param player the player
   * @param health the health
   */
  public void setPlayerBaseHealth(final Player player, final double health) {
    Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health);
  }

  /**
   * Transfer health.
   *
   * @param victim the victim
   * @param killer the killer
   */
  public void transferHealth(final Player victim, final Player killer) {
    setPlayerBaseHealth(killer, getPlayerBaseHealth(killer) + gainHearts);
    setPlayerBaseHealth(victim, getPlayerBaseHealth(victim) - looseHearts);
  }

  /**
   * Gets life state.
   *
   * @param player the player
   * @return the life state
   */
  public LifeState getLifeState(final Player player) {
    if (Objects.requireNonNull(lifeSteal.getConfig().getString("LifeState")).equalsIgnoreCase("SPECTATING")
            && player.getGameMode() == GameMode.SPECTATOR) return LifeState.SPECTATING;
    if (Objects.requireNonNull(lifeSteal.getConfig().getString("LifeState")).equalsIgnoreCase("DEAD"))
      return LifeState.DEAD;
    if (Objects.requireNonNull(lifeSteal.getConfig().getString("LifeState")).equalsIgnoreCase("BANNED")
            && player.isBanned()) return LifeState.BANNED;
    return LifeState.LIVING;
  }

  /**
   * Format string list + placeholder with MiniMessage.
   *
   * @param loreList         the lore list
   * @param placeholder      the placeholder
   * @param placeholderValue the placeholder value
   * @return the list
   */
  public List<String> formatStringList(final List<String> loreList, final String placeholder, final int placeholderValue) {
    final List<String> formattedList = new ArrayList<>();
    for (final String list : loreList) {
      formattedList.add(this.legecySerializer.serialize(MiniMessage.builder().build().deserialize(list,
              Placeholder.component(placeholder, Component.text(placeholderValue)))));
    }
    return formattedList;
  }

  /**
   * Format string list with MiniMessage.
   *
   * @param loreList the lore list
   * @return the list
   */
  public List<String> formatStringList(final List<String> loreList) {
    final List<String> formattedList = new ArrayList<>();
    for (final String list : loreList) {
      formattedList.add(this.legecySerializer.serialize(MiniMessage.builder().build().deserialize(list)));
    }
    return formattedList;
  }

  /**
   * Format string + placeholder with MiniMessage.
   *
   * @param string           the string
   * @param placeholder      the placeholder
   * @param placeholderValue the placeholder value
   * @return the string
   */
  public String formatString(final String string, final String placeholder, final int placeholderValue) {
    return this.legecySerializer.serialize(MiniMessage.builder().build().deserialize(string,
            Placeholder.component(placeholder, Component.text(placeholderValue))));
  }

  /**
   * Format string with MiniMessage.
   *
   * @param string the string
   * @return the string
   */
  public String formatString(final String string) {
    return this.legecySerializer.serialize(MiniMessage.builder().build().deserialize(string));
  }

  /**
   * Format Component with Minimessage.
   *
   * @param component the component
   * @return the string
   */
  public String formatString(final Component component) {
    return this.legecySerializer.serialize(component);
  }

  /**
   * Give heart item's effects.
   *
   * @param target    the target player
   * @param heartMeta the heart item meta
   * @param instance  the plugin instance
   */
  public void giveHeartEffects(final Player target, final ItemMeta heartMeta, final JavaPlugin instance) {
    final String itemType = heartMeta.getPersistentDataContainer().get
            (new NamespacedKey(instance, "lifesteal_heart_itemtype"), PersistentDataType.STRING);
    final String itemIndex = heartMeta.getPersistentDataContainer().get
            (new NamespacedKey(instance, "lifesteal_heart_itemindex"), PersistentDataType.STRING);
    final String effectsPath = "Hearts.Types." + itemType + "." + itemIndex + ".Properties.Effects";
    final Set<String> indexSet = Objects.requireNonNull(lifeSteal.getHeartConfig().getSection(effectsPath)).getRoutesAsStrings(false);

    for (int i = 0; i < indexSet.size(); i++) {
      final String[] indexList = indexSet.toArray(new String[0]);
      target.addPotionEffect(new PotionEffect(Objects.requireNonNull
              (PotionEffectType.getByName(Objects.requireNonNull(lifeSteal.getHeartConfig().getString(effectsPath + "." + indexList[i] + ".Type")))),
              lifeSteal.getHeartConfig().getInt(effectsPath + "." + indexList[i] + ".Duration") * 20,
              lifeSteal.getHeartConfig().getInt(effectsPath + "." + indexList[i] + ".Power"),
              lifeSteal.getHeartConfig().getBoolean(effectsPath + "." + indexList[i] + ".ShowParticles", false),
              lifeSteal.getHeartConfig().getBoolean(effectsPath + "." + indexList[i] + ".ShowParticles", false)));
    }
  }

  /**
   * Spawn particle effects.
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

  public String getEliminationMessage(final EntityDamageEvent.DamageCause damageCause, final Player victim) {
    if (victim.getKiller() == null) {
      switch (damageCause) {
        case CONTACT -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByDamagingBlocks"); }
        case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByMob"); }
        case PROJECTILE -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByProjectile"); }
        case SUFFOCATION -> { return lifeSteal.getI18n().getKey("Messages.Elimination.BySuffocation"); }
        case FALL -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByFallDamage"); }
        case FIRE, FIRE_TICK -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByBurn"); }
        case LAVA -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByLava"); }
        case DROWNING -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByDrowning"); }
        case BLOCK_EXPLOSION -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByExplosion"); }
        case ENTITY_EXPLOSION -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByCreeper"); }
        case SUICIDE -> { return lifeSteal.getI18n().getKey("Messages.Elimination.BySuicide"); }
        case VOID -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByVoid"); }
        case LIGHTNING -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByLightning"); }
        case STARVATION -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByStarvation"); }
        case POISON -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByPoison"); }
        case MAGIC -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByMagic"); }
        case WITHER -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByWither"); }
        case FALLING_BLOCK -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByFallingBlock"); }
        case THORNS -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByThorns"); }
        case DRAGON_BREATH -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByDragonBreath"); }
        case FLY_INTO_WALL -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByKineticEnergy"); }
        case HOT_FLOOR -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByMagmaBlock"); }
        case CRAMMING -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByCramming"); }
        case FREEZE -> { return lifeSteal.getI18n().getKey("Messages.Elimination.ByFreeze"); }
        default -> { return lifeSteal.getI18n().getKey("Messages.Elimination.Other"); }
      }
    } else {
      { return lifeSteal.getI18n().getKey("Messages.Elimination.ByPlayer"); }
    }
  }
}