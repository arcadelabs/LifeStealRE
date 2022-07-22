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
import in.arcadelabs.labaide.libs.aikar.acf.annotation.CommandPermission;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.Subcommand;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.hearts.HeartItemManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.withdraw")
public class Withdraw extends BaseCommand {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private HeartItemManager heartItemManager;
  private ItemStack replacementHeart;
  private List<String> disabledWorlds;

  /**
   * On withdraw command.
   *
   * @param sender the sender
   * @param hearts the hearts
   */
  @Subcommand("withdraw")
  public void onWithdraw(final CommandSender sender, final int hearts) {
    final Player player = (Player) sender;
    if (lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Withdraw").size() != 0) {
      disabledWorlds = lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Withdraw");
    }
    if (!(disabledWorlds.contains(player.getWorld().toString().toLowerCase()))) {
      if (hearts * 2 >= lifeSteal.getUtils().getPlayerBaseHealth(player)) {
        lifeSteal.getMessenger().sendMessage(player, "Chutiye, aukat hai tera itna?");
      } else {
        lifeSteal.getUtils().setPlayerBaseHealth(player, lifeSteal.getUtils().getPlayerBaseHealth(player) - hearts * 2);
        heartItemManager = new HeartItemManager(HeartItemManager.Mode.valueOf(lifeSteal.getHeartConfig().getString("Hearts.Mode.OnWithdraw")))
                .prepareIngedients()
                .cookHeart(hearts * 2);
        replacementHeart = heartItemManager.getHeartItem();

        final Map<Integer, ItemStack> items = player.getInventory().addItem(replacementHeart);
        for (final Map.Entry<Integer, ItemStack> leftovers : items.entrySet()) {
          player.getWorld().dropItemNaturally(player.getLocation(), leftovers.getValue());
        }
        lifeSteal.getUtils().spawnParticles(player, "soul");
        lifeSteal.getInteraction().retuurn(Level.FINE,
                lifeSteal.getUtils().formatString(lifeSteal.getI18n().getKey("Messages.HeartWithdraw"), "hearts", hearts), player,
                lifeSteal.getI18n().getKey("Sounds.HeartWithdraw"));
      }
    } else {
      lifeSteal.getMessenger().sendMessage(player, lifeSteal.getI18n().getKey("Messages.DisabledWorld.Heart-Withdraw"));
    }
  }
}