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

package in.arcadelabs.lifesteal.commands;

import in.arcadelabs.libs.aikar.acf.BaseCommand;
import in.arcadelabs.libs.aikar.acf.annotation.CommandAlias;
import in.arcadelabs.libs.aikar.acf.annotation.CommandPermission;
import in.arcadelabs.libs.aikar.acf.annotation.Subcommand;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.utils.HeartItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.withdraw")
public class Withdraw extends BaseCommand {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();

  @Subcommand("withdraw")
  public void onWithdraw(CommandSender sender, int hearts) {
    Player player = (Player) sender;
    if (hearts >= lifeSteal.getUtils().getPlayerBaseHealth(player)) {
      lifeSteal.getMessenger().sendMessage(player, "Chutiye, aukat hai tera itna?");
    } else {
      lifeSteal.getUtils().setPlayerBaseHealth(player, lifeSteal.getUtils().getPlayerBaseHealth(player) - hearts);

      Map<Integer, ItemStack> items = player.getInventory().addItem(new HeartItem(hearts).getHeartItemStack());
      for (Map.Entry<Integer, ItemStack> leftovers : items.entrySet()) {
        player.getWorld().dropItemNaturally(player.getLocation(), leftovers.getValue());
      }
    }
  }
}