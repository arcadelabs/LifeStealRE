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
import in.arcadelabs.lifesteal.hearts.HeartItemManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.givehearts")
public class GiveHearts extends BaseCommand {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private HeartItemManager heartItemManager;
  private ItemStack replacementHeart;

  @Subcommand("givehearts")
  @CommandCompletion("@players Blessed|Normal|Cursed @nothing")
  @CommandAlias("givehearts")
  public void onGiveHearts(final CommandSender sender, final OnlinePlayer target, final String type, final int amount) {
    switch (type) {
      case "Blessed" -> giveHearts("blessed", HeartItemManager.Mode.RANDOM_BLESSED, target.player, amount);
      case "Normal" -> giveHearts("normal", HeartItemManager.Mode.RANDOM_NORMAL, target.player, amount);
      case "Cursed" -> giveHearts("cursed", HeartItemManager.Mode.RANDOM_CURSED, target.player, amount);
    }
  }

  public void giveHearts(final CommandSender sender, final String type, final HeartItemManager.Mode mode, final Player target, final int amount) {
    heartItemManager = new HeartItemManager(mode)
            .prepareIngedients()
            .cookHeart();
    replacementHeart = heartItemManager.getHeartItem();
    replacementHeart.setAmount(amount);

    final Map<Integer, ItemStack> items = player.getInventory().addItem(replacementHeart);
    for (final Map.Entry<Integer, ItemStack> leftovers : items.entrySet()) {
      player.getWorld().dropItemNaturally(player.getLocation(), leftovers.getValue());
    }

    TagResolver.Single playerName = target == sender ?
            Placeholder.component("player", Component.text("you")) : Placeholder.component("player", target.name());

    final Component giveHeartsMsg = MiniMessage.miniMessage().deserialize(lifeSteal.getKey("Messages.GiveHearts"),
            Placeholder.unparsed("hearts", String.valueOf(amount)),
            Placeholder.unparsed("type", type),
            playerName);
    lifeSteal.getInteraction().retuurn(Logger.Level.INFO, giveHeartsMsg, target,
            lifeSteal.getKey("Sounds.GiveHearts"));
  }
}