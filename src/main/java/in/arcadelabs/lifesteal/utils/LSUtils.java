package in.arcadelabs.lifesteal.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Objects;

public class LSUtils {

  public double getPlayerBaseHealth(Player player) {
    return Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
  }

  public void setPlayerBaseHealth(Player player, double health) {
    Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(health);
  }

  public void transferHealth(Player victim, Player killer) {
    setPlayerBaseHealth(killer, getPlayerBaseHealth(killer) + 1);
    setPlayerBaseHealth(victim, getPlayerBaseHealth(victim) - 1);
  }
}
