package in.arcadelabs.lifesteal.commands;

import in.arcadelabs.libs.aikar.acf.BaseCommand;
import in.arcadelabs.libs.aikar.acf.annotation.CommandAlias;
import in.arcadelabs.libs.aikar.acf.annotation.CommandPermission;
import in.arcadelabs.libs.aikar.acf.annotation.Description;
import in.arcadelabs.libs.aikar.acf.annotation.Subcommand;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.reload")
public class Reload extends BaseCommand {

  @Subcommand("reload")
  @Description("Reloads the instance")
  public void onReload(CommandSender sender) throws IOException, InvalidConfigurationException {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      LifeStealPlugin.getLifeSteal().getConfigYML().reload();
      LifeStealPlugin.getLifeSteal().getMessenger().sendMessage(player, "<gradient:e01e37:f52486>LifeSteal Core reloaded.</gradient>");
      LifeStealPlugin.getLifeSteal().getMessenger().sendConsoleMessage("<gradient:e01e37:f52486>LifeSteal Core reloaded.</gradient>");
    } else {
      LifeStealPlugin.getLifeSteal().getConfigYML().reload();
      LifeStealPlugin.getLifeSteal().getMessenger().sendConsoleMessage("<gradient:e01e37:f52486>LifeSteal Core reloaded.</gradient>");
    }
  }
}
