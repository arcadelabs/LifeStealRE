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

import in.arcadelabs.lifesteal.api.enums.Mode;
import in.arcadelabs.labaide.libs.aikar.acf.BaseCommand;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.CommandAlias;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.CommandPermission;
import in.arcadelabs.labaide.libs.aikar.acf.annotation.Subcommand;
import in.arcadelabs.labaide.logger.Logger;
import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.database.profile.StatisticsManager;
import in.arcadelabs.lifesteal.hearts.HeartItemManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.withdraw")
public class Withdraw extends BaseCommand {

  private final LifeSteal lifeSteal = LifeStealPlugin.getLifeSteal();
  private final StatisticsManager statisticsManager = this.lifeSteal.getStatisticsManager();
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
  @CommandAlias("withdraw")
  public void onWithdraw(final CommandSender sender, final int hearts) {
    final Player player = (Player) sender;
    if (this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Withdraw").size() != 0) {
      this.disabledWorlds = this.lifeSteal.getConfig().getStringList("Disabled-Worlds.Heart-Withdraw");
    }
    if (!(this.disabledWorlds.contains(player.getWorld().getName()))) {
      if (hearts >= this.lifeSteal.getUtils().getPlayerHearts(player)) {
        player.sendMessage(this.lifeSteal.getUtils().formatString(this.lifeSteal.getKey("Messages.NotEnoughHearts")));
      } else {
        if (!this.lifeSteal.getWithdrawCooldown().isOnCooldown(player.getUniqueId())) {
          this.lifeSteal.getUtils().setPlayerHearts(player, this.lifeSteal.getUtils().getPlayerHearts(player) - hearts);
          this.heartItemManager = new HeartItemManager()
                  .setMode(Mode.valueOf(this.lifeSteal.getHeartConfig().getString("Hearts.Mode.OnWithdraw")))
                  .prepareIngedients()
                  .cookHeart();
          this.replacementHeart = this.heartItemManager.getHeartItem();
          this.replacementHeart.setAmount(hearts);

          final Map<Integer, ItemStack> items = player.getInventory().addItem(this.replacementHeart);
          for (final Map.Entry<Integer, ItemStack> leftovers : items.entrySet()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftovers.getValue());
          }
          this.lifeSteal.getUtils().spawnParticles(player, "soul");
          final Component withdrawMsg = MiniMessage.miniMessage().deserialize(this.lifeSteal.getKey("Messages.HeartWithdraw"),
                  Placeholder.unparsed("hearts", String.valueOf(hearts)));
          this.lifeSteal.getInteraction().retuurn(Logger.Level.INFO, withdrawMsg, player,
                  this.lifeSteal.getKey("Sounds.HeartWithdraw"));

          this.statisticsManager.setCurrentHearts(player, this.statisticsManager.getCurrentHearts(player) - hearts)
                  .setLostHearts(player, this.statisticsManager.getLostHearts(player) + hearts)
                  .update(player);

          if (this.lifeSteal.getConfig().getInt("Cooldowns.Heart-Withdraw") >= 0)
            this.lifeSteal.getWithdrawCooldown().setCooldown(player.getUniqueId());
        } else
          player.sendMessage(this.lifeSteal.getMiniMessage().deserialize(this.lifeSteal.getKey("Messages.CooldownMessage.Heart-Withdraw"),
                  Placeholder.component("seconds", Component.text(this.lifeSteal.getWithdrawCooldown().getRemainingTime(player.getUniqueId())))));
      }
    } else
      player.sendMessage(MiniMessage.miniMessage().deserialize(this.lifeSteal.getKey("Messages.DisabledStuff.Worlds.Heart-Withdraw")));
  }
}
