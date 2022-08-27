package in.arcadelabs.lifesteal.api;

import in.arcadelabs.lifesteal.database.profile.StatisticsManager;
import in.arcadelabs.lifesteal.hearts.HeartItemManager;

public interface LifeStealAPI {

  StatisticsManager getStatisticsManager();

  HeartItemManager getHeartItemManager();

}

