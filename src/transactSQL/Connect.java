package transactSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static transactSQL.DatabaseConnection.*;

public class Connect {

    //  Establish a connection to the watson DB, to remain open while recursive sql select statements identify and return words from the DB...
    public static String watson() {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            return transactSQL.Query.getWord(1, 2, 3, 4, 5);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}