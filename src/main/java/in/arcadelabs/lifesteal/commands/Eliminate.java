package in.arcadelabs.lifesteal.commands;

import in.arcadelabs.libs.aikar.acf.BaseCommand;
import in.arcadelabs.libs.aikar.acf.annotation.CommandAlias;
import in.arcadelabs.libs.aikar.acf.annotation.CommandCompletion;
import in.arcadelabs.libs.aikar.acf.annotation.CommandPermission;
import in.arcadelabs.libs.aikar.acf.annotation.Subcommand;
import org.bukkit.entity.Player;

import static in.arcadelabs.lifesteal.LifeSteal.utils;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.eliminate")
public class Eliminate extends BaseCommand {

  @Subcommand("Eliminate")
  @CommandCompletion("@players")
  public void onComplete(Player target){
    utils.setPlayerBaseHealth(target, 0);
  }
}
