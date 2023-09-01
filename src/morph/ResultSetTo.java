package morph;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetTo {

    static StringBuilder sb = new StringBuilder();

    //  Use StringBuilder to concatenate results, and exclude the trailing delimiter with Substring()
    public static String letterCountsString(ResultSet resultSet) throws SQLException {

        sb = new StringBuilder();

        while(resultSet.next()) {
            sb.append(resultSet.getString(1)).append("=").append(resultSet.getInt(2)).append(", ");
        }
        return (sb).substring(0, sb.length()-2);
    }

    public static int numWords(ResultSet resultSet) throws SQLException {

        int numWords = 0;

        while(resultSet.next()) {
            numWords = ((Number) resultSet.getObject(1)).intValue();
        }
        return numWords;
    }

    public static String strings(ResultSet resultSet) throws SQLException {

        sb = new StringBuilder();

        while(resultSet.next()) {
            sb.append(resultSet.getString(1)).append(", ");
        }
        return (sb).substring(0, sb.length()-2);
    }

}