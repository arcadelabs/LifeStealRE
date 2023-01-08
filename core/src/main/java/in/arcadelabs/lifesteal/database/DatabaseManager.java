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
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

@Getter
public class DatabaseManager {

  private final HikariDataSource hikariDataSource;
  private final Executor hikariExecutor = Executors.newFixedThreadPool(6);
  private Dao<Profile, UUID> profileDao;
  private ConnectionSource connectionSource;

  private String address, database, username, password;
  private int port;
  private boolean ssl, dbEnabled;

  public DatabaseManager(LifeStealPlugin lifeStealPlugin) {
    this.loadCredentials();
    HikariConfig hikariConfig = new HikariConfig();

    if (this.dbEnabled) {
      hikariConfig.setJdbcUrl(
          "jdbc:mysql://" + this.address + ":" + this.port + "/" + this.database);
    } else {
      if (lifeStealPlugin.getDataFolder().listFiles(file -> file.getName().equals("database.db"))
          != null) {
        File oldFile = new File(lifeStealPlugin.getDataFolder(), "database.db");
        oldFile.renameTo(new File(lifeStealPlugin.getDataFolder(), "statistics.db"));
      }

      File database = new File(lifeStealPlugin.getDataFolder(), "statistics.db");
      if (!database.exists()) {
        try {
          database.createNewFile();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
      hikariConfig.setJdbcUrl("jdbc:sqlite:" + this.database);
      hikariConfig.setDriverClassName("org.sqlite.JDBC");
    }

    hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
    hikariConfig.addDataSourceProperty("useUnicode", true);
    hikariConfig.addDataSourceProperty("useSSL", this.ssl);
    hikariConfig.addDataSourceProperty("socketTimeout", TimeUnit.SECONDS.toMillis(30));
    hikariConfig.setMaximumPoolSize(10);
    hikariConfig.setMaxLifetime(1800000);
    hikariConfig.setMinimumIdle(10);
    hikariConfig.setKeepaliveTime(0);
    hikariConfig.setConnectionTimeout(5000);
    hikariConfig.setUsername(this.username);
    hikariConfig.setPassword(this.password);
    hikariConfig.setPoolName("LifeSteal-Pool");
    hikariConfig.setConnectionTestQuery("SELECT 1;");

    this.hikariDataSource = new HikariDataSource(hikariConfig);
    try {
      this.connectionSource = new DataSourceConnectionSource(this.hikariDataSource,
          hikariConfig.getJdbcUrl());
      this.profileDao = DaoManager.createDao(this.connectionSource, Profile.class);
      TableUtils.createTableIfNotExists(this.connectionSource, Profile.class);
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

  public void disconnect() {
    if (this.connectionSource != null) {
      this.connectionSource.closeQuietly();
    }
    if (this.hikariDataSource != null) {
      this.hikariDataSource.close();
    }
  }
}