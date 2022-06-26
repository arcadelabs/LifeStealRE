package in.arcadelabs.lifesteal.database.profile;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import java.sql.SQLException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerLoginEvent(PlayerLoginEvent event) {

    if (event.getResult() != Result.ALLOWED) return;

    if (!(LifeStealPlugin.getInstance()
        .getServer()
        .getPluginManager()
        .isPluginEnabled(LifeStealPlugin.getInstance()))) {
      event.disallow(Result.KICK_OTHER, "Your account could not be loaded...");
    }
    try {
      LifeStealPlugin.getLifeSteal()
          .getProfileManager()
          .handleJoin(event.getPlayer().getUniqueId());
    } catch (SQLException e) {
      event.disallow(Result.KICK_OTHER, "Your account could not be loaded...");
      e.printStackTrace();
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerQuit(PlayerQuitEvent event) {
    LifeStealPlugin.getLifeSteal().getProfileManager().handleQuit(event.getPlayer().getUniqueId());
  }
}
