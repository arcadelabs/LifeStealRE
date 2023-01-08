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

package in.arcadelabs.lifesteal;

import in.arcadelabs.labaide.logger.Logger;
import java.sql.SQLException;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class LifeStealPlugin extends JavaPlugin {

  @Getter
  private static LifeStealPlugin instance;
  @Getter
  private static LifeSteal lifeSteal;

  private boolean labaideExist;

  @Override
  public void onEnable() {
    instance = this;

    if (Bukkit.getPluginManager().getPlugin("LabAide") == null) {
      this.labaideExist = false;
      this.getLogger().severe("LabAide was not found! Disabling LifeStealRE...");
      this.getLogger()
              .severe("Download LabAide at https://github.com/arcadelabs/LabAide/releases/latest");
      this.setEnabled(false);
      return;
    }

    this.labaideExist = true;
    lifeSteal = new LifeSteal();
    try {
      lifeSteal.init();
    } catch (Exception e) {
      this.getLogger()
              .warning(
                      "There was an error while loading LifeStealRE, gotta be a hooman error, blame Aniket#7102.");
      this.getLogger().warning(e.toString());
      e.printStackTrace();
    }
  }

  @Override
  public void onDisable() {

    if (this.labaideExist) {
      lifeSteal.getDatabaseManager().getHikariExecutor()
              .execute(() -> lifeSteal.getProfileManager().getProfileCache().values().forEach(profile -> {
                try {
                  if (!lifeSteal.getProfileManager().getProfileCache().isEmpty())
                    lifeSteal.getProfileManager().saveProfile(profile);
                } catch (SQLException e) {
                  lifeSteal.getLogger().log(Logger.Level.ERROR, Component.text(e.getMessage(),
                          NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
                }
                try {
                  lifeSteal.getDatabaseManager().disconnect();
                } catch (Exception e) {
                  lifeSteal.getLogger().log(Logger.Level.ERROR, Component.text(e.getMessage(),
                          NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
                }
              }));
    }
    lifeSteal.getLogger().log(Logger.Level.INFO,
            MiniMessage.miniMessage().deserialize("<b><gradient:#f58c67:#f10f5d>Adios...</gradient></b>"));
  }
}