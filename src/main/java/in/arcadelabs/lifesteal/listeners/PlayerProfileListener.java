package in.arcadelabs.lifesteal.listeners;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import java.sql.SQLException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerProfileListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        if (!(LifeStealPlugin.getInstance()
                .getServer()
                .getPluginManager()
                .isPluginEnabled(LifeStealPlugin.getInstance()))) {
            event.disallow(Result.KICK_OTHER, "Your account could not be loaded...");
        }
        try {
            LifeStealPlugin.getLifeSteal().getProfileHandler().handleJoin(event.getUniqueId());
        } catch (SQLException e) {
            event.disallow(Result.KICK_OTHER, "Your account could not be loaded...");
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            LifeStealPlugin.getLifeSteal()
                    .getProfileHandler()
                    .handleQuit(event.getPlayer().getUniqueId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
