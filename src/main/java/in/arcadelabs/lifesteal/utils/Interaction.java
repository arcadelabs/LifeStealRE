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

import in.arcadelabs.libs.adventure.adventure.key.Key;
import in.arcadelabs.libs.adventure.adventure.sound.Sound;
import in.arcadelabs.libs.adventure.adventure.text.Component;
import in.arcadelabs.libs.adventure.adventure.text.minimessage.MiniMessage;
import in.arcadelabs.libs.adventure.adventure.text.minimessage.tag.resolver.Placeholder;
import in.arcadelabs.libs.adventurelib.impl.SpigotMessenger;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Interaction {

  private final SpigotMessenger messenger;
  private final Logger logger;
  private final boolean cleanConsole;

  /**
   * Instantiates a new Interaction.
   *
   * @param logger       the logger
   * @param cleanConsole clean console boolean
   */
  public Interaction(final Logger logger, final boolean cleanConsole) {
    this.messenger = LifeStealPlugin.getLifeSteal().getMessenger();
    this.logger = logger;
    this.cleanConsole = cleanConsole;
  }

  /**
   * Returns message feedback.
   *
   * @param level   the logging level
   * @param message the message
   * @param player  the player
   */
  public void retuurn(final Level level, final String message, final Player player) {
    messenger.sendMessage(player, message);
    if (cleanConsole) return;
    logger.log(level, message);
  }

  /**
   * Broadcast message feedback.
   *
   * @param level     the level
   * @param component the component
   */
  public void broadcast(final Level level, final Component component) {
      messenger.broadcast(component);
    if (cleanConsole) return;
    logger.log(level, LifeStealPlugin.getLifeSteal().getUtils().formatString(component));
  }

  /**
   * Broadcast message feedback.
   *
   * @param key    the key
   * @param player the player
   */
  public void broadcast(final String key, final Player player) {
    Component component = MiniMessage.builder().build().deserialize(
            LifeStealPlugin.getLifeSteal().getI18n().getKey(key),
            Placeholder.component("player", Component.text(player.getDisplayName())));
    messenger.broadcast(component);
    if (cleanConsole) return;
    logger.log(Level.INFO, LifeStealPlugin.getLifeSteal().getUtils().formatString(component));
  }

  /**
   * Returns message feedback..
   *
   * @param level    the logging level
   * @param message  the message
   * @param player   the player
   * @param soundKey the sound key
   */
  public void retuurn(final Level level, final String message, final Player player, final String soundKey) {
    messenger.playSound(player, Sound.sound(Key.key(soundKey), Sound.Source.PLAYER, 1f, 1f));
    messenger.sendMessage(player, message);
    if (cleanConsole) return;
    logger.log(level, message);
  }

  /**
   * Returns message feedback..
   *
   * @param level    the logging level
   * @param messages  the messages
   * @param player   the player
   * @param soundKey the sound key
   */
  public void retuurn(final Level level, final List<String> messages, final Player player, final String soundKey) {
    messenger.playSound(player, Sound.sound(Key.key(soundKey), Sound.Source.PLAYER, 1f, 1f));
    for (final String message : messages) {
      messenger.sendMessage(player, message);
      if (cleanConsole) return;
      this.logger.log(level, message);
    }
  }
}