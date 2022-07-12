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

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a table
 *
 * @author Mrtenz
 * <a href="https://github.com/Mrtenz/MySQLAPI/tree/master/src/main/java/me/mrten/mysqlapi/queries">...</a>
 **/

public class CreateTableQuery {

  private final String table;
  private final List<String> columns = new ArrayList<>();
  private boolean ifNotExists = false;
  private String primaryKey;

  /**
   * Create a create table query.
   *
   * @param table the table to be created
   */
  public CreateTableQuery(String table) {
    this.table = table;
  }

  /**
   * Add if not exists to the query.
   *
   * @return the CreateTableQuery object
   */
  public CreateTableQuery ifNotExists() {
    this.ifNotExists = true;
    return this;
  }

  /**
   * Add a column with settings.
   *
   * @param column   the column
   * @param settings the column settings
   * @return the CreateTableQuery object
   */
  public CreateTableQuery column(String column, String settings) {
    columns.add(column + " " + settings);
    return this;
  }

  /**
   * Set the primary key to column.
   *
   * @param column the column to be the primary key
   * @return the CreateTableQuery object
   */
  public CreateTableQuery primaryKey(String column) {
    this.primaryKey = column;
    return this;
  }

  /**
   * Build the query as a String.
   *
   * @return the query as a String
   */
  public String build() {
    StringBuilder builder = new StringBuilder();
    builder.append("CREATE TABLE ");

    if (ifNotExists) {
      builder.append("IF NOT EXISTS ");
    }

    builder.append(table)
            .append(" (")
            .append(QueryUtils.separate(columns, ","));

    if (primaryKey != null) {
      builder.append(",PRIMARY KEY(");
      builder.append(primaryKey);
      builder.append(")");
    }

    builder.append(")");

    return builder.toString();
  }

}
