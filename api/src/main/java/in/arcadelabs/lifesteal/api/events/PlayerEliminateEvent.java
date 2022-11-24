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

package in.arcadelabs.lifesteal.api.events;

import in.arcadelabs.lifesteal.api.enums.EliminationCause;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerEliminateEvent extends Event implements Cancellable {

  private final HandlerList handlers = new HandlerList();
  private final Player player;
  private Player killer;
  private EliminationCause eliminationCause;
  private boolean isCancelled;

  /**
   * Instantiates a new Player eliminate event.
   *
   * @param player           the player
   * @param killer           the killer
   * @param eliminationCause the elimination cause
   * @param isCancelled      the is cancelled
   */
  public PlayerEliminateEvent(final Player player, final @Nullable Player killer, final EliminationCause eliminationCause, final boolean isCancelled) {
    this.player = player;
    this.killer = killer;
    this.eliminationCause = eliminationCause;
    this.isCancelled = isCancelled;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return this.handlers;
  }

  /**
   * Gets player.
   *
   * @return the player
   */
  public Player getPlayer() {
    return this.player;
  }

  /**
   * Gets elimination cause.
   *
   * @return the elimination cause
   */
  public EliminationCause getEliminationCause() {
    return this.eliminationCause;
  }

  /**
   * Sets elimination cause.
   *
   * @param eliminationCause the elimination cause
   */
  public void setEliminationCause(final EliminationCause eliminationCause) {
    this.eliminationCause = eliminationCause;
  }

  /**
   * Gets killer.
   *
   * @return the killer
   */
  public @Nullable Player getKiller() {
    return this.killer;
  }

  /**
   * Sets killer.
   *
   * @param killer the killer
   */
  public void setKiller(final Player killer) {
    this.killer = killer;
  }

  public boolean isCancelled() {
    return this.isCancelled;
  }

  public void setCancelled(final boolean cancelled) {
    this.isCancelled = cancelled;
  }
}
