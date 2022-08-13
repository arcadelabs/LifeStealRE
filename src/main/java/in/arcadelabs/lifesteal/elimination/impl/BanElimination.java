package in.arcadelabs.lifesteal.elimination.impl;

import in.arcadelabs.lifesteal.LifeSteal;
import in.arcadelabs.lifesteal.database.profile.Profile;
import in.arcadelabs.lifesteal.elimination.EliminationMethod;
import in.arcadelabs.lifesteal.elimination.EliminationReason;
import in.arcadelabs.lifesteal.utils.LifeState;
import lombok.RequiredArgsConstructor;
import ninja.smirking.events.bukkit.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor
public class BanElimination implements EliminationMethod {

  private final LifeSteal instance;

  @Override
  public void handleElimination(Player player, EliminationReason reason) {
    Profile profile = instance.getProfileManager().getProfileCache().get(player.getUniqueId());
    profile.setLifeState(LifeState.DEAD);

    // TODO: @Aniket announce elimination message
    player.kick(instance.getMiniMessage().deserialize("Eliminated! Lmfao bad L"));
  }

  @Override
  public void handleRevive(UUID uniqueID) {
    try {
      Profile profile = instance.getProfileManager().getProfile(uniqueID);
      profile.setLifeState(LifeState.LIVING);
      profile.setCurrentHearts(instance.getConfig().getInt("DefaultHearts"));
      instance.getProfileManager().saveProfile(profile);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void handleEvents() {
    Events.observeAll(PlayerJoinEvent.class, handler -> {
      Profile profile = instance.getProfileManager().getProfileCache().get(handler.getPlayer().getUniqueId());
      if (profile.getLifeState() == LifeState.LIVING) return;
      handler.getPlayer().kick(instance.getMiniMessage().deserialize("Eliminated! Lmfao bad L"));
    });
  }
}
