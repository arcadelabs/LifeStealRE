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
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LSUtils {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final LegacyComponentSerializer legecySerializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();

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
    setPlayerBaseHealth(killer, getPlayerBaseHealth(killer) + 1);
    setPlayerBaseHealth(victim, getPlayerBaseHealth(victim) - 1);
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
}