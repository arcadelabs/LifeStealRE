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

package in.arcadelabs.lifesteal.core.commands;

import in.arcadelabs.labaide.libs.aikar.acf.BaseCommand;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.CommandAlias;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.CommandCompletion;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.CommandPermission;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.Subcommand;
import in.arcadelabs.labaide.libs.aikar.acf.bukkit.contexts.OnlinePlayer;
import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.api.database.profile.IStatisticsManager;
import in.arcadelabs.lifesteal.core.LifeSteal;
import in.arcadelabs.lifesteal.core.LifeStealPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.removehearts")
public class RemoveHearts extends BaseCommand {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final IStatisticsManager IStatisticsManager = this.lifeSteal.getIStatisticsManager();

  @Subcommand("removehearts")
  @CommandCompletion("@players @nothing")
  @CommandAlias("removehearts")
  public void onRemoveHearts(final CommandSender sender, final OnlinePlayer target, final int hearts) {
    Player player = target.player;
    this.lifeSteal.getUtils().setPlayerHearts(player, this.lifeSteal.getUtils().getPlayerHearts(player) - (hearts * 2));
    player.setHealth(Math.min(player.getHealth() -
            hearts, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));

    final TagResolver.Single playerName = target.equals(sender) ?
            Placeholder.component("player", Component.text("you")) : Placeholder.component("player", target.player.name());

    final Component removeHeartsMsg = MiniMessage.miniMessage().deserialize(this.lifeSteal.getKey("Messages.RemoveHearts"),
            Placeholder.unparsed("hearts", String.valueOf(hearts)),
            playerName);
    this.lifeSteal.getInteraction().verbose(Logger.Level.INFO, removeHeartsMsg, player,
            this.lifeSteal.getKey("Sounds.RemoveHearts"));

    this.IStatisticsManager.setCurrentHearts(player, this.IStatisticsManager.getCurrentHearts(player) - hearts)
            .setLostHearts(player, this.IStatisticsManager.getLostHearts(player) + hearts)
            .update(player);
  }
}
