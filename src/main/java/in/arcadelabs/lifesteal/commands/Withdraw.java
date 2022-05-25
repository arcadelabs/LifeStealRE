package in.arcadelabs.lifesteal.commands;

import in.arcadelabs.libs.aikar.acf.BaseCommand;
import in.arcadelabs.libs.aikar.acf.annotation.CommandAlias;
import in.arcadelabs.libs.aikar.acf.annotation.CommandPermission;
import in.arcadelabs.libs.aikar.acf.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static in.arcadelabs.lifesteal.LifeSteal.utils;
import static in.arcadelabs.lifesteal.hooks.LifeStealHook.messenger;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.withdraw")
public class Withdraw extends BaseCommand {

  @Subcommand("withdraw")
  public void onWithdraw(CommandSender sender, double hearts) {
    Player player = (Player) sender;
    if (hearts >= utils.getPlayerBaseHealth(player)) {
      messenger.sendMessage(player, "Chutiye, aukat hai tera itna?");
    } else {
      utils.setPlayerBaseHealth(player, utils.getPlayerBaseHealth(player) - hearts);
      //      TODO - Drop heart at @player 's position
    }
  }
}
