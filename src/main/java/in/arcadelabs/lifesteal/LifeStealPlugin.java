package in.arcadelabs.lifesteal;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class LifeStealPlugin extends JavaPlugin {

  @Getter
  private static LifeStealPlugin instance;
  @Getter
  private static LifeSteal lifeSteal;


  @Override
  public void onEnable() {
    instance = this;
    lifeSteal = new LifeSteal();
    try {
      lifeSteal.init();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onDisable() {
    lifeSteal.getMessenger().closeMessenger();
    lifeSteal.getProfileStorage().disconnect();
    getLogger().info(ChatColor.of("#f72585") + "  ___  _  _   __   ");
    getLogger().info(ChatColor.of("#b5179e") + " / __)( \\/ ) /__\\  ");
    getLogger().info(ChatColor.of("#7209b7") + "( (__  \\  / /(__)\\ ");
    getLogger().info(ChatColor.of("#560bad") + " \\___) (__)(__)(__)... on the other side");
    getLogger().info(ChatColor.of("#560bad") + " ");
  }
}
