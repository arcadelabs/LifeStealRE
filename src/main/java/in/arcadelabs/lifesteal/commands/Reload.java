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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

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
  @CommandCompletion("All|Language.yml|Config.yml|Hearts.yml")
  public void onReload(final CommandSender sender, final String file) throws IOException {
    if (sender instanceof final Player player) {
      switch (file) {
        case "All" -> {
          lifeSteal.getLanguage().reload();
          lifeSteal.getConfig().reload();
          lifeSteal.getHeartConfig().reload();
          lifeSteal.getLogger().logger(Logger.Level.INFO, lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>LifeSteal Core reloaded.</gradient>"));
          player.sendMessage(lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>LifeSteal Core reloaded.</gradient>"));
        }
        case "Language.yml" -> {
          lifeSteal.getLanguage().reload();
          lifeSteal.getLogger().logger(Logger.Level.INFO, lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Language.yml reloaded.</gradient>"));
          player.sendMessage(lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Language.yml reloaded.</gradient>"));
        }
        case "Config.yml" -> {
          lifeSteal.getConfig().reload();
          lifeSteal.getLogger().logger(Logger.Level.INFO, lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Config.yml reloaded.</gradient>"));
          player.sendMessage(lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Config.yml reloaded.</gradient>"));
        }
        case "Hearts.yml" -> {
          lifeSteal.getHeartConfig().reload();
          lifeSteal.getLogger().logger(Logger.Level.INFO, lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Hearts.yml reloaded.</gradient>"));
          player.sendMessage(lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Hearts.yml reloaded.</gradient>"));
        }
      }
    } else {
      switch (file) {
        case "All" -> {
          lifeSteal.getLanguage().reload();
          lifeSteal.getConfig().reload();
          lifeSteal.getHeartConfig().reload();
          lifeSteal.getLogger().logger(Logger.Level.INFO, lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>LifeSteal Core reloaded.</gradient>"));
        }
        case "Language.yml" -> {
          lifeSteal.getLanguage().reload();
          lifeSteal.getLogger().logger(Logger.Level.INFO, lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Language.yml reloaded.</gradient>"));
        }
        case "Config.yml" -> {
          lifeSteal.getConfig().reload();
          lifeSteal.getLogger().logger(Logger.Level.INFO, lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Config.yml reloaded.</gradient>"));
        }
        case "Hearts.yml" -> {
          lifeSteal.getHeartConfig().reload();
          lifeSteal.getLogger().logger(Logger.Level.INFO, lifeSteal.getUtils().formatString("<gradient:#e01e37:#f52486>Hearts.yml reloaded.</gradient>"));
        }
      }
    }
  }
}