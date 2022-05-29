package in.arcadelabs.lifesteal.profile;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Profile {

  private UUID uniqueID;
  private double currentHearts;
  private double totalHeartsGained;
  private double totalHeartsLost;
  private double totalHeartsConsumed;
  private double totalHeartsWithdrawn;
  private int deaths;
  private int kills;

  public Profile(UUID uniqueID) {
    this.uniqueID = uniqueID;
  }

  public String toJson() {
    return LifeStealPlugin.getLifeSteal().getGSON().toJson(this);
  }
}