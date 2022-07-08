package in.arcadelabs.lifesteal.database.querybuilder;

import java.util.ArrayList;
import java.util.List;

/*
 * This class is not the property of ArcadeLabs.
 * Original Source : https://github.com/Mrtenz/MySQLAPI/tree/master/src/main/java/me/mrten/mysqlapi/queries
 */

public class DeleteQuery {

    private String table;
    private List<String> wheres = new ArrayList<String>();

    /**
     * Create a delete query.
     *
     * @param table - the table to be deleted from
     */
    public DeleteQuery(String table) {
        this.table = table;
    }

    /**
     * Add a where clause.
     *
     * @param expression the expression
     * @return the DeleteQuery object
     */
    public DeleteQuery where(String expression) {
        wheres.add(expression);
        return this;
    }

    /**
     * Add a where clause.
     *
     * @param expression the expression
     * @return the DeleteQuery object
     */
    public DeleteQuery and(String expression) {
        where(expression);
        return this;
    }

    /**
     * Build the query as a String.
     *
     * @return the query as a String
     */
    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ")
                .append(table);

        if (wheres.size() > 0) {
            builder.append(" WHERE ")
                    .append(QueryUtils.separate(wheres, " AND "));
        }

        return builder.toString();
    }

}
