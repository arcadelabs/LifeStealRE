package in.arcadelabs.lifesteal.database.querybuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.Getter;

/*
 * This class is not the property of ArcadeLabs.
 * Original Source : https://github.com/Mrtenz/MySQLAPI/tree/master/src/main/java/me/mrten/mysqlapi/queries
 */

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