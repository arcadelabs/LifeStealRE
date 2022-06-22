package in.arcadelabs.lifesteal.database.profile;

import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.database.DatabaseHandler;
import in.arcadelabs.lifesteal.utils.LifeState;
import in.arcadelabs.lifesteal.utils.querybuilder.CreateTableQuery;
import in.arcadelabs.lifesteal.utils.querybuilder.InsertQuery;
import in.arcadelabs.lifesteal.utils.querybuilder.Query;
import in.arcadelabs.lifesteal.utils.querybuilder.SelectQuery;
import in.arcadelabs.lifesteal.utils.querybuilder.UpdateQuery;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class ProfileHandler {

  @Getter(AccessLevel.NONE)
  private final DatabaseHandler databaseHandler = LifeStealPlugin.getLifeSteal().getDatabaseHandler();

  private final Map<UUID, Profile> profileMap = new HashMap<>();
  private final ProfileThread profileThread;

  public ProfileHandler() {

    String query = new CreateTableQuery("lifesteal_data")
        .ifNotExists()
        .column("uniqueID", "VARCHAR(36)")
        .column("lifeState", "VARCHAR(20)")
        .primaryKey("uniqueID")
        .build();

    System.out.println(query);

    try (Connection connection = databaseHandler.getConnection()) {
      Statement statement = connection.createStatement();
      statement.execute(query);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    profileThread = new ProfileThread();
    profileThread.start();

  }

  public boolean hasProfile(UUID uuid) {
    String sql = new SelectQuery("lifesteal_data")
        .column("*")
        .where("uniqueID = ?")
        .build();

    System.out.println(sql);

    try (Connection connection = databaseHandler.getConnection()) {
      Query query = new Query(connection, sql);
      query.setParameter(1, uuid.toString());
      ResultSet resultSet = query.getStatement().executeQuery();
      return resultSet.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public Profile getProfile(UUID uuid) throws SQLException {
    Profile profile = new Profile(uuid);

    if (this.hasProfile(uuid)) {
      String sql = new SelectQuery("lifesteal_data")
          .column("*")
          .where("uniqueID = ?")
          .build();

      System.out.println(sql);

      try (Connection connection = databaseHandler.getConnection()) {

        Query query = new Query(connection, sql);
        query.setParameter(1, uuid.toString());
        ResultSet resultSet = query.getStatement().executeQuery();

        profile.setLifeState(LifeState.valueOf(resultSet.getString("lifeState")));

      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      this.saveProfile(profile);
    }

    return profile;
  }

  public Profile saveProfile(Profile profile) throws SQLException {
    if (hasProfile(profile.getUniqueID())) {
      String sql = new UpdateQuery("lifesteal_data")
          .set("lifeState", profile.getLifeState().toString())
          .where("uniqueID = ?")
          .build();

      System.out.println(sql);

      try (Connection connection = databaseHandler.getConnection()) {

        Query query = new Query(connection, sql);
        query.setParameter(1, profile.getUniqueID().toString());
        query.getStatement().executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      String sql = new InsertQuery("lifesteal_data")
          .value("uniqueID")
          .value("lifeState")
          .build();

      System.out.println(sql);
      try (Connection connection = databaseHandler.getConnection()) {
        Query query = new Query(connection, sql);
        query.getStatement().setString(1, profile.getUniqueID().toString());
        query.getStatement().setString(2, LifeState.LIVING.toString());
        query.getStatement().executeUpdate();
      }
    }
    return profile;
  }

  public void handleJoin(UUID uniqueID) throws SQLException {
    Profile profile = this.getProfile(uniqueID);
    profileMap.put(uniqueID, profile);
  }

  public void handleQuit(UUID uniqueID) {
    ForkJoinPool.commonPool().execute(() -> {
      try {
        this.saveProfile(profileMap.get(uniqueID));
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
    profileMap.remove(uniqueID);
  }


  public void saveAll() throws SQLException {
    for (Profile profile : profileMap.values()) {
      this.saveProfile(profile);
    }
  }
}