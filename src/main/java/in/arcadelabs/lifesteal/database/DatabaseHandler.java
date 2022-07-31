/*
 *          LifeSteal - Yet another lifecore smp core.
 *                Copyright (C) 2022  Arcade Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package in.arcadelabs.lifesteal.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import in.arcadelabs.labaide.libs.boostedyaml.YamlDocument;
import in.arcadelabs.lifesteal.LifeStealPlugin;
import in.arcadelabs.lifesteal.database.profile.Profile;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@Getter
public class DatabaseHandler {

  private final HikariDataSource hikariDataSource;
  private final Executor hikariExecutor = Executors.newFixedThreadPool(2);
  private Dao<Profile, UUID> profileDao;
  private ConnectionSource  connectionSource;

  private String address, database, username, password;
  private int port;
  private boolean ssl, dbEnabled;

  public DatabaseHandler(LifeStealPlugin lifeStealPlugin) {
    this.loadCredentials();
    HikariConfig hikariConfig = new HikariConfig();

    if (dbEnabled) {
      hikariConfig.setJdbcUrl("jdbc:mysql://" + address + ":" + port + "/" + database);
    } else {
      File database = new File(LifeStealPlugin.getInstance().getDataFolder(), "database.db");
      if (!database.exists()) {
        try {
          database.createNewFile();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
      hikariConfig.setJdbcUrl("jdbc:sqlite:" + database);
      hikariConfig.setDriverClassName("org.sqlite.JDBC");
    }

    hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
    hikariConfig.addDataSourceProperty("useUnicode", true);
    hikariConfig.addDataSourceProperty("useSSL", ssl);
    hikariConfig.setMaximumPoolSize(10);
    hikariConfig.setUsername(username);
    hikariConfig.setPassword(password);
    hikariConfig.setPoolName("LifeSteal-Pool");
    hikariDataSource = new HikariDataSource(hikariConfig);

    if (hikariDataSource.isRunning()) {
      lifeStealPlugin.getLogger().info("Successfully initialized connection to MySQL database...");
    } else {
      lifeStealPlugin
          .getLogger()
          .severe("Failed to initialize connection to MySQL database! Shutting down...");
      lifeStealPlugin.getServer().getPluginManager().disablePlugin(lifeStealPlugin);
    }
    try {
      this.connectionSource = new DataSourceConnectionSource(hikariDataSource, hikariConfig.getJdbcUrl());
      this.profileDao = DaoManager.createDao(connectionSource, Profile.class);
      TableUtils.createTableIfNotExists(connectionSource, Profile.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void loadCredentials() {
    YamlDocument configuration = LifeStealPlugin.getLifeSteal().getConfig();
    this.dbEnabled = configuration.getBoolean("DATABASE.ENABLED");
    this.address = configuration.getString("DATABASE.ADDRESS");
    this.port = configuration.getInt("DATABASE.PORT");
    this.database = configuration.getString("DATABASE.DATABASE");
    this.username = configuration.getString("DATABASE.USERNAME");
    this.password = configuration.getString("DATABASE.PASSWORD");
    this.ssl = configuration.getBoolean("DATABASE.SSL");
  }

  public void disconnect() throws Exception {
    if (connectionSource != null) connectionSource.closeQuietly();
    if (hikariDataSource != null) hikariDataSource.close();
  }

  public Connection getConnection() throws SQLException {
    if (hikariDataSource != null) {
      return hikariDataSource.getConnection();
    }
    return null;
  }
}
