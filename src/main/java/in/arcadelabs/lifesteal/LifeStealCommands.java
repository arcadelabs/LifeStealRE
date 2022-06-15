package in.arcadelabs.lifesteal;

import in.arcadelabs.libs.aikar.acf.BaseCommand;
import in.arcadelabs.libs.aikar.acf.annotation.CommandAlias;
import in.arcadelabs.libs.aikar.acf.annotation.CommandCompletion;
import in.arcadelabs.libs.aikar.acf.annotation.CommandPermission;
import in.arcadelabs.libs.aikar.acf.annotation.Description;
import in.arcadelabs.libs.aikar.acf.annotation.Subcommand;
import in.arcadelabs.lifesteal.utils.HeartItem;
import java.io.IOException;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("lifesteal|ls")
@CommandPermission("lifesteal.admin")
public class LifeStealCommands extends BaseCommand {

  private final LifeStealManager lifeSteal = LifeStealPlugin.getLifeSteal();

  @Subcommand("Eliminate")
  @CommandCompletion("@players")
  @CommandPermission("lifesteal.eliminate")
  public void onComplete(Player target) {
    lifeSteal.getUtils().setPlayerBaseHealth(target, 0);
  }

  @Subcommand("reload")
  @Description("Reloads the instance")
  @CommandPermission("lifesteal.reload")
  public void onReload(CommandSender sender) throws IOException, InvalidConfigurationException {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      lifeSteal.getConfigYML().reload();
      lifeSteal.getMessenger().sendMessage(player, "<gradient:e01e37:f52486>LifeSteal Core reloaded.</gradient>");
      lifeSteal.getMessenger().sendConsoleMessage("<gradient:e01e37:f52486>LifeSteal Core reloaded.</gradient>");
    } else {
      lifeSteal.getConfigYML().reload();
      lifeSteal.getMessenger().sendConsoleMessage("<gradient:e01e37:f52486>LifeSteal Core reloaded.</gradient>");
    }
  }

  @Subcommand("withdraw")
  @CommandPermission("lifesteal.withdraw")
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
