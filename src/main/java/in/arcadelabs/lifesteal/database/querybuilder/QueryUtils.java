package in.arcadelabs.lifesteal.database.querybuilder;

import java.util.Collection;

/*
 * This class is not the property of ArcadeLabs.
 * Original Source : https://github.com/Mrtenz/MySQLAPI/tree/master/src/main/java/me/mrten/mysqlapi/queries
 */

public class QueryUtils {

    /**
     * Turn a collection into a string separated by a separator.
     *
     * @param collection the collection to be separated
     * @param separator  the separator
     * @return
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
