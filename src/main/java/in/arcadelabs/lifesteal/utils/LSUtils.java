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

import in.arcadelabs.labaide.libs.kyori.adventure.text.Component;
import in.arcadelabs.labaide.libs.kyori.adventure.text.minimessage.MiniMessage;
import in.arcadelabs.labaide.libs.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import in.arcadelabs.labaide.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.xenondevs.particle.ParticleEffect;

import java.sql.SQLException;
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
    try {
      return lifeSteal.getProfileManager().getProfile(player.getUniqueId()).getLifeState();
    } catch (SQLException e) {
      LifeStealPlugin.getInstance().getLogger().warning(e.toString());
      return null;
    }
  }

  /**
   * Handle ban.
   *
   * @param URI the ban command uri
   */
  public void handleBan(final String URI, final Player player) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), new in.arcadelabs.labaide.placeholder.Placeholder().replace(URI, player));
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

  public void handleElimination(final Player player) {
    switch (lifeSteal.getConfig().getString("Elimination")) {
      case "BANNED" -> handleBan(lifeSteal.getConfig().getString("Ban-Command-URI"), player);
      case "DEAD" -> player.setGameMode(GameMode.ADVENTURE);
      case "SPIRIT" -> lifeSteal.getSpiritFactory().addSpirit(player);
      case "AfterLife" -> lifeSteal.getMessenger().sendConsoleMessage("TTPP");
    }
  }

  public void handleElimination(final Player player, final PlayerDeathEvent event) {
    switch (lifeSteal.getConfig().getString("InventoryMode")) {
      case "DROP" -> event.setKeepInventory(false);
      case "SAVE_TO_RESTORE" -> lifeSteal.getSpiritFactory().saveInventory(player);
      case "CLEAR" -> player.getInventory().clear();
      case "NONE" -> player.saveData();
    }

    switch (lifeSteal.getConfig().getString("Elimination")) {
      case "BANNED" -> handleBan(lifeSteal.getConfig().getString("Ban-Command-URI"), player);
      case "DEAD" -> player.setGameMode(GameMode.ADVENTURE);
      case "SPIRIT" -> lifeSteal.getSpiritFactory().addSpirit(player);
      case "AfterLife" -> lifeSteal.getMessenger().sendConsoleMessage("TTPP");
    }
  }

  public void handleRevive(final Player player) {
    switch (lifeSteal.getConfig().getString("Elimination")) {
      case "BANNED" -> {
        handleBan(lifeSteal.getConfig().getString("UnBan-Command-URI"), player);
        lifeSteal.getMessenger().sendMessage(player, lifeSteal.getI18n().getKey("Messages.Revive.ByUnban"));
      }
      case "DEAD" -> player.setGameMode(GameMode.SURVIVAL);
      case "SPIRIT" -> lifeSteal.getSpiritFactory().removeSpirit(player);
      case "AfterLife" -> lifeSteal.getMessenger().sendConsoleMessage("PPTT");
    }
  }

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