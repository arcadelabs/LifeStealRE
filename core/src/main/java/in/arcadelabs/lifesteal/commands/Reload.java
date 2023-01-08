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

package in.arcadelabs.lifesteal.commands;

import in.arcadelabs.labaide.libs.aikar.acf.BaseCommand;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.CommandAlias;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.CommandCompletion;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.CommandPermission;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.Description;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.Subcommand;
import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import java.io.IOException;
import java.sql.SQLException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.reload")
public class Reload extends BaseCommand {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();

  /**
   * On reload command.
   *
   * @param sender the sender
   * @throws IOException the io exception
   */
  @Subcommand("reload")
  @Description("Reloads the instance")
  @CommandCompletion("All|Language.yml|Config.yml|Hearts.yml|Database")
  public void onReload(final CommandSender sender, final String file) throws IOException {
    if (sender instanceof final Player player) {
      switch (file) {
        case "All" -> {
          this.lifeSteal.getLanguage().reload();
          this.lifeSteal.getConfig().reload();
          this.lifeSteal.getHeartConfig().reload();
          this.lifeSteal.reInit();
          this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>LifeSteal Reimagined reloaded.</gradient>"));
          player.sendMessage(this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>LifeSteal Reimagined reloaded.</gradient>"));
        }
        case "Language.yml" -> {
          this.lifeSteal.getLanguage().reload();
          this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Language.yml reloaded.</gradient>"));
          player.sendMessage(this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Language.yml reloaded.</gradient>"));
        }
        case "Config.yml" -> {
          this.lifeSteal.getConfig().reload();
          this.lifeSteal.reInit();
          this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Config.yml reloaded.</gradient>"));
          player.sendMessage(this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Config.yml reloaded.</gradient>"));
        }
        case "Hearts.yml" -> {
          this.lifeSteal.getHeartConfig().reload();
          this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Hearts.yml reloaded.</gradient>"));
          player.sendMessage(this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Hearts.yml reloaded.</gradient>"));
        }
        case "Database" -> {
          this.lifeSteal.getDatabaseManager().getHikariExecutor()
                  .execute(() -> this.lifeSteal.getProfileManager().getProfileCache().values().forEach(profile -> {
                    try {
                      if (!this.lifeSteal.getProfileManager().getProfileCache().isEmpty())
                        this.lifeSteal.getProfileManager().saveProfile(profile);
                    } catch (SQLException e) {
                      this.lifeSteal.getLogger().log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
                    }
                  }));
          this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Database reloaded.</gradient>"));
          player.sendMessage(this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Database reloaded.</gradient>"));
        }
      }
    } else {
      switch (file) {
        case "All" -> {
          this.lifeSteal.getLanguage().reload();
          this.lifeSteal.getConfig().reload();
          this.lifeSteal.getHeartConfig().reload();
          this.lifeSteal.reInit();
          this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>LifeSteal Reimagined reloaded.</gradient>"));
        }
        case "Language.yml" -> {
          this.lifeSteal.getLanguage().reload();
          this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Language.yml reloaded.</gradient>"));
        }
        case "Config.yml" -> {
          this.lifeSteal.getConfig().reload();
          this.lifeSteal.reInit();
          this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Config.yml reloaded.</gradient>"));
        }
        case "Hearts.yml" -> {
          this.lifeSteal.getHeartConfig().reload();
          this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Hearts.yml reloaded.</gradient>"));
        }
        case "Database" -> {
          this.lifeSteal.getDatabaseManager().getHikariExecutor()
                  .execute(() -> this.lifeSteal.getProfileManager().getProfileCache().values().forEach(profile -> {
                    try {
                      if (!this.lifeSteal.getProfileManager().getProfileCache().isEmpty())
                        this.lifeSteal.getProfileManager().saveProfile(profile);
                    } catch (SQLException e) {
                      this.lifeSteal.getLogger().log(Logger.Level.ERROR, Component.text(e.getMessage(), NamedTextColor.DARK_PURPLE), e.fillInStackTrace());
                    }
                  }));
          this.lifeSteal.getLogger().log(Logger.Level.INFO, this.lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Database reloaded.</gradient>"));
        }
      }
    }
  }
}