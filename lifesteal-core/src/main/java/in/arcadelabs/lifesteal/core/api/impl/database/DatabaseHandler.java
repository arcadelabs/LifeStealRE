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

package in.arcadelabs.lifesteal.core.api.impl.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import in.arcadelabs.labaide.libs.boostedyaml.YamlDocument;
import in.arcadelabs.lifesteal.api.database.IDatabaseHandler;
import in.arcadelabs.lifesteal.api.database.profile.Profile;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Getter
public class DatabaseHandler implements IDatabaseHandler {

  private final HikariDataSource hikariDataSource;
  private final Executor hikariExecutor = Executors.newFixedThreadPool(2);
  private final JavaPlugin instance;
  private final YamlDocument config;
  private Dao<Profile, UUID> profileDao;
  private ConnectionSource connectionSource;

  private String address, database, username, password;
  private int port;
  @Getter
  private boolean ssl, dbEnabled;

  public DatabaseHandler(final JavaPlugin instance, final YamlDocument config) {
    this.instance = instance;
    this.config = config;
    this.loadCredentials();
    HikariConfig hikariConfig = new HikariConfig();

    if (this.dbEnabled) {
      hikariConfig.setJdbcUrl("jdbc:mysql://" + this.address + ":" + this.port + "/" + this.database);
    } else {
      File database = new File(this.instance.getDataFolder(), "database.db");
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
    hikariConfig.setMaximumPoolSize(10);
    hikariConfig.setUsername(this.username);
    hikariConfig.setPassword(this.password);
    hikariConfig.setPoolName("LifeSteal-Pool");
    this.hikariDataSource = new HikariDataSource(hikariConfig);

    try {
      this.connectionSource = new DataSourceConnectionSource(this.hikariDataSource, hikariConfig.getJdbcUrl());
      this.profileDao = DaoManager.createDao(this.connectionSource, Profile.class);
      this.profileDao.setObjectCache(true);
      TableUtils.createTableIfNotExists(this.connectionSource, Profile.class);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void disconnect() {
    if (this.connectionSource != null) this.connectionSource.closeQuietly();
    if (this.hikariDataSource != null) this.hikariDataSource.close();
  }

  @Override
  public Connection getConnection() throws SQLException {
    if (this.hikariDataSource != null) {
      return this.hikariDataSource.getConnection();
    }
    return null;
  }
}
