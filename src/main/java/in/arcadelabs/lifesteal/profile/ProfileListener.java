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

package in.arcadelabs.lifesteal.profile;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class ProfileListener implements Listener {

  private final ProfileHandler profileHandler;

  public ProfileListener(ProfileHandler profileHandler) {
    this.profileHandler = profileHandler;
  }

  @EventHandler
  public final void onAsyncPlayerJoinEvent(AsyncPlayerPreLoginEvent event) throws IOException {
    profileHandler.createProfile(event.getUniqueId());
  }

  @EventHandler
  public final void onPlayerQuitEvent(PlayerQuitEvent event) throws IOException {
    final Player player = event.getPlayer();
    final Profile profile = profileHandler.getProfile(player.getUniqueId());

    profileHandler.handleRemove(profile);
  }
}
