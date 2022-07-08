package in.arcadelabs.lifesteal.database.profile;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
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
      event.disallow(Result.KICK_OTHER, "Server still loading, please join after some time");
    }
    try {
      Profile profile =
          LifeStealPlugin.getLifeSteal()
              .getProfileManager()
              .getProfile(event.getPlayer().getUniqueId());
      LifeStealPlugin.getLifeSteal()
          .getProfileManager()
          .getProfileMap()
          .put(profile.getUniqueID(), profile);
    } catch (SQLException e) {
      event.disallow(Result.KICK_OTHER, "Your account could not be loaded...");
      e.printStackTrace();
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerQuit(PlayerQuitEvent event) {
    Bukkit.getScheduler()
        .runTaskAsynchronously(
            LifeStealPlugin.getInstance(),
            () -> {
              try {
                LifeStealPlugin.getLifeSteal()
                    .getProfileManager()
                    .saveProfile(
                        LifeStealPlugin.getLifeSteal()
                            .getProfileManager()
                            .getProfileMap()
                            .get(event.getPlayer().getUniqueId()));
              } catch (SQLException e) {
                e.printStackTrace();
              }

              LifeStealPlugin.getLifeSteal()
                  .getProfileManager()
                  .getProfileMap()
                  .remove(event.getPlayer().getPlayer().getUniqueId());
            });
  }
}
