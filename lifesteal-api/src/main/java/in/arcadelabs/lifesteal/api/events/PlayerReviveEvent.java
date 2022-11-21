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

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerReviveEvent extends Event {
  private final HandlerList handlers = new HandlerList();
  private final Player player;
  private Player reviver;
  private boolean isCancelled;

  public PlayerReviveEvent(final Player player, @Nullable final Player reviver, final boolean isCancelled) {
    this.player = player;
    this.reviver = reviver;
    this.isCancelled = isCancelled;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return this.handlers;
  }

  public Player getPlayer() {
    return this.player;
  }

  public @Nullable Player getReviver() {
    return this.reviver;
  }

  public void setReviver(final Player reviver) {
    this.reviver = reviver;
  }

  public boolean isCancelled() {
    return this.isCancelled;
  }

  public void setCancelled(final boolean cancelled) {
    this.isCancelled = cancelled;
  }
}
