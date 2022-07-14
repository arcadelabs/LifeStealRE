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

import java.util.Collection;

/**
 * Creates a table
 *
 * @author Mrtenz
 * <a href="https://github.com/Mrtenz/MySQLAPI/tree/master/src/main/java/me/mrten/mysqlapi/queries">...</a>
 **/

public class QueryUtils {

  /**
   * Turn a collection into a string separated by a separator.
   *
   * @param collection the collection to be separated
   * @param separator  the separator
   * @return saperated string
   */
  public static String separate(Collection<String> collection, String separator) {
    StringBuilder builder = new StringBuilder();
    String sep = "";
    for (String item : collection) {
      builder.append(sep)
              .append(item);
      sep = separator;
    }
    return builder.toString();
  }

}
