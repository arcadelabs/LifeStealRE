/**
 *                  Copyright 2017 MrTenz
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *            http://www.apache.org/licenses/LICENSE-2.0
 **/

package in.arcadelabs.lifesteal.database.querybuilder;

import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Creates a table
 *
 * @author Mrtenz
 * <a href="https://github.com/Mrtenz/MySQLAPI/tree/master/src/main/java/me/mrten/mysqlapi/queries">...</a>
 **/

@Getter
public class Query {

  private PreparedStatement statement;

  public Query(Connection connection, String sql) throws SQLException {
    statement = connection.prepareStatement(sql);
  }

  public void setParameter(int index, Object value) throws SQLException {
    statement.setObject(index, value);
  }

}