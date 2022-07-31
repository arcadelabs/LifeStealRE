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

package in.arcadelabs.lifesteal.database.profile;

import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class ProfileListener implements Listener {

  private final LifeSteal instance = LifeStealPlugin.getLifeSteal();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerLoginEvent(PlayerLoginEvent event) {

    if (event.getResult() != Result.ALLOWED) return;

    if (!(LifeStealPlugin.getInstance()
            .getServer()
            .getPluginManager()
            .isPluginEnabled(LifeStealPlugin.getInstance()))) {
      event.disallow(Result.KICK_OTHER, "Server still loading, please join after some time");
    }
    try {
      instance.getProfileManager().getProfileCache()
          .put(event.getPlayer().getUniqueId(), instance.getProfileManager().getProfile(event.getPlayer().getUniqueId()));
    } catch (SQLException e) {
      event.disallow(Result.KICK_OTHER, "Your account could not be loaded...");
      e.printStackTrace();
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerQuit(PlayerQuitEvent event) {
    instance.getDatabaseHandler().getHikariExecutor().execute(() -> {
      try {
        instance.getProfileManager().saveProfile(instance.getProfileManager().getProfileCache().get(event.getPlayer().getUniqueId()));
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }
}
