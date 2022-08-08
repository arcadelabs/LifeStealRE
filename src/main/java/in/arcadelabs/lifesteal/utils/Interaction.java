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

import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Interaction {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final boolean cleanConsole;
  private final Audience audience = Audience.audience(Bukkit.getOnlinePlayers());

  /**
   * Instantiates a new Interaction.
   *
   * @param cleanConsole clean console boolean
   */
  public Interaction(final boolean cleanConsole) {
    this.cleanConsole = cleanConsole;
  }

  /**
   * Broadcast message feedback.
   *
   * @param level     the level
   * @param component the component
   */
  public void broadcast(final Logger.Level level, final Component component) {
    audience.sendMessage(component);
    if (cleanConsole) return;
    lifeSteal.getLogger().log(null, level, component);
  }

  /**
   * Broadcast message feedback.
   *
   * @param key    the key
   * @param player the player
   */
  public void broadcast(final String key, final Player player) {
    Component component = MiniMessage.builder().build().deserialize(
            lifeSteal.getKey(key),
            Placeholder.component("player", player.name()));
    audience.sendMessage(component);
    if (cleanConsole) return;
    lifeSteal.getLogger().log(null, Logger.Level.INFO, component);
  }

  public void broadcast(final String key, final Player player1, final Player player2) {
    Component component = MiniMessage.builder().build().deserialize(
            lifeSteal.getKey(key),
            Placeholder.component("player", player1.name()),
            Placeholder.component("commander", player2.name()));
    audience.sendMessage(component);
    if (cleanConsole) return;
    lifeSteal.getLogger().log(null, Logger.Level.INFO, component);
  }

  /**
   * Returns message feedback.
   *
   * @param level   the logging level
   * @param message the message
   * @param player  the player
   */
  public void retuurn(final Logger.Level level, final Component message, final Player player) {
    player.sendMessage(message);
    if (cleanConsole) return;
    lifeSteal.getLogger().log(player, level, message);
  }

  /**
   * Returns message feedback..
   *
   * @param level    the logging level
   * @param message  the message
   * @param player   the player
   * @param soundKey the sound key
   */
  public void retuurn(final Logger.Level level, final Component message, final Player player, final String soundKey) {
    player.sendMessage(message);
    player.playSound(Sound.sound(Key.key(soundKey), Sound.Source.PLAYER, 1f, 1f));
    if (cleanConsole) return;
    lifeSteal.getLogger().log(player, level, message);
  }

  /**
   * Returns message feedback..
   *
   * @param level    the logging level
   * @param messages the messages
   * @param player   the player
   * @param soundKey the sound key
   */
  public void retuurn(final Logger.Level level, final List<Component> messages, final Player player, final String soundKey) {
    player.playSound(Sound.sound(Key.key(soundKey), Sound.Source.PLAYER, 1f, 1f));
    for (final Component message : messages) {
      player.sendMessage(player, message);
      if (cleanConsole) return;
      lifeSteal.getLogger().log(player, level, message);
    }
  }
}