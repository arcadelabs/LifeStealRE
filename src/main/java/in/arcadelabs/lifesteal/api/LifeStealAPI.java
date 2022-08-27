package in.arcadelabs.lifesteal.api;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.database.profile.StatisticsManager;
import in.arcadelabs.lifesteal.hearts.HeartItemManager;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
@Getter
public class LifeStealAPI {

  private final StatisticsManager statisticsManager = LifeStealPlugin.getLifeSteal().getStatisticsManager();
  private final HeartItemManager heartItemManager = LifeStealPlugin.getLifeSteal().getHeartItemManager();

}
