package in.arcadelabs.lifesteal.database.profile;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import java.sql.SQLException;
import java.time.Duration;

public class ProfileThread extends Thread {

  @Override
  public void run() {
    do {
      try {
        LifeStealPlugin.getLifeSteal().getProfileHandler().saveAll();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        sleep(Duration.ofSeconds(5).toMillis());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } while (true);
  }
}

