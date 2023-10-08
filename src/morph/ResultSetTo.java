package morph;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetTo {

    //  RETURN the number of words remaining in the database
    public static int numWords(ResultSet resultSet) throws SQLException {

        int numWords = 0;

        while(resultSet.next()) {
            numWords = ((Number) resultSet.getObject(1)).intValue();
        }
        return numWords;
    }
}