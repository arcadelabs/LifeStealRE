package in.arcadelabs.lifesteal.database.querybuilder;

import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Creates a table
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