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
import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
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

  @Subcommand("removehearts")
  @CommandCompletion("@players @nothing")
  @CommandAlias("removehearts")
  public void onRemoveHearts(final CommandSender sender, final OnlinePlayer target, final int hearts) {
    Player player = target.player;
    lifeSteal.getUtils().setPlayerHearts(player, lifeSteal.getUtils().getPlayerHearts(player) - (hearts * 2));
    player.setHealth(Math.min(player.getHealth() -
            hearts, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));

    final TagResolver.Single playerName = target.equals(sender) ?
            Placeholder.component("player", Component.text("you")) : Placeholder.component("player", target.player.name());

    final Component removeHeartsMsg = MiniMessage.miniMessage().deserialize(lifeSteal.getKey("Messages.RemoveHearts"),
            Placeholder.unparsed("hearts", String.valueOf(hearts)),
            playerName);
    lifeSteal.getInteraction().retuurn(Logger.Level.INFO, removeHeartsMsg, player,
            lifeSteal.getKey("Sounds.RemoveHearts"));
    lifeSteal.getProfileManager().getProfileCache().get
            (player.getUniqueId()).setCurrentHearts(
            (lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).getCurrentHearts() - hearts));
    lifeSteal.getProfileManager().getProfileCache().get
            (player.getUniqueId()).setLostHearts(
            (lifeSteal.getProfileManager().getProfileCache().get(player.getUniqueId()).getLostHearts() + hearts));
  }
}