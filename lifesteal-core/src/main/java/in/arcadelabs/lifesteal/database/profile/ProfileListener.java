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

import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class ProfileListener implements Listener {

  private final LifeStealPlugin instance = LifeStealPlugin.getInstance();
  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();

  @EventHandler(priority = EventPriority.LOWEST)
  public void handleJoin(PlayerJoinEvent event) {

    if (!(this.instance.getServer().getPluginManager().isPluginEnabled(this.instance))) {
      event.getPlayer().kick(Component.text("Server still loading, please join after some time",
              TextColor.color(102, 0, 205)), PlayerKickEvent.Cause.TIMEOUT);
    }
    try {
      this.lifeSteal.getProfileManager().getProfileCache()
              .put(event.getPlayer().getUniqueId(),
                      this.lifeSteal.getProfileManager().getProfile(event.getPlayer().getUniqueId()));
    } catch (SQLException e) {
      event.getPlayer().kick(Component.text("FAILED TO LOAD YOUR ACCOUNT!",
              TextColor.color(255, 0, 0)), PlayerKickEvent.Cause.TIMEOUT);
      this.lifeSteal.getLogger().log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent event) {
    this.lifeSteal.getDatabaseHandler().getHikariExecutor().execute(() -> {
      try {
        this.lifeSteal.getProfileManager().saveProfile(
                this.lifeSteal.getProfileManager().getProfileCache().get(event.getPlayer().getUniqueId()));
      } catch (SQLException e) {
        this.lifeSteal.getLogger().log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
      }
    });
  }
}