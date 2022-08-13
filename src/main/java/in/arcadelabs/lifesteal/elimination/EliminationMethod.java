package in.arcadelabs.lifesteal.elimination;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface EliminationMethod {

  void handleElimination(Player player, EliminationReason reason);
  void handleRevive(UUID uniqueID);
  void handleEvents();
}
