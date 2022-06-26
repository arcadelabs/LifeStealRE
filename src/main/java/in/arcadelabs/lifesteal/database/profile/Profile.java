package in.arcadelabs.lifesteal.database.profile;

import in.arcadelabs.lifesteal.utils.LifeState;
import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Profile {

  private final UUID uniqueID;
  private LifeState lifeState;

}
