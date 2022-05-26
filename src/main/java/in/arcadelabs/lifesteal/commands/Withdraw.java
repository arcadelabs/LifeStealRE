package in.arcadelabs.lifesteal.commands;

import in.arcadelabs.libs.aikar.acf.BaseCommand;
import in.arcadelabs.libs.aikar.acf.annotation.CommandAlias;
import in.arcadelabs.libs.aikar.acf.annotation.CommandPermission;
import in.arcadelabs.libs.aikar.acf.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static in.arcadelabs.lifesteal.LifeSteal.getUtils;
import static in.arcadelabs.lifesteal.hooks.LifeStealHook.getMessenger;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.withdraw")
public class Withdraw extends BaseCommand {

  @Subcommand("withdraw")
  public void onWithdraw(CommandSender sender, double hearts) {
    Player player = (Player) sender;
    if (hearts >= getUtils().getPlayerBaseHealth(player)) {
      getMessenger().sendMessage(player, "Chutiye, aukat hai tera itna?");
    } else {
      getUtils().setPlayerBaseHealth(player, getUtils().getPlayerBaseHealth(player) - hearts);
      //      TODO - Drop heart at @player 's position
    }
  }
}
