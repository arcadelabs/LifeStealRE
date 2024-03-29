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
import in.arcadelabs.labaide.libs.aikar.acf.annotation.Subcommand;
import in.arcadelabs.labaide.libs.aikar.acf.bukkit.contexts.OnlinePlayer;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.revive")
public class Revive extends BaseCommand {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();

  /**
   * On revive command.
   *
   * @param target the target
   */
  @Subcommand("revive")
  @CommandCompletion("@players")
  @CommandAlias("revive")
  public void onRevive(final CommandSender sender, final OnlinePlayer target) {
    if (sender instanceof Player player) {
      this.lifeSteal.getUtils().handleRevive(target.getPlayer());
      this.lifeSteal.getInteraction().broadcast("Messages.Revive.ByCommand.Player", target.getPlayer(), player);
    } else {
      this.lifeSteal.getUtils().handleRevive(target.getPlayer());
      this.lifeSteal.getInteraction().broadcast("Messages.Revive.ByCommand.Console", target.getPlayer());
    }
  }
}