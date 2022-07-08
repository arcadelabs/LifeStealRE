package in.arcadelabs.lifesteal.database.profile;

import in.arcadelabs.lifesteal.utils.LifeState;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Profile {

  private final UUID uniqueID;
  private LifeState lifeState;
  private int currentHearts;
  private int lostHearts;
  private int normalHearts;
  private int blessedHearts;
  private int cursedHearts;
  private int peakHeartsReached;

}
