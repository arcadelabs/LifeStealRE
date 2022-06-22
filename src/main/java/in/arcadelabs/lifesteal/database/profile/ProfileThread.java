package in.arcadelabs.lifesteal.database.profile;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.ForkJoinPool;

public class ProfileThread extends Thread {

  @Override
  public void run() {
    do {
      ForkJoinPool.commonPool().execute(() -> {
        try {
          LifeStealPlugin.getLifeSteal().getProfileHandler().saveAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
      });
      try {
        sleep(Duration.ofSeconds(3).toMillis());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } while (true);
  }
}


