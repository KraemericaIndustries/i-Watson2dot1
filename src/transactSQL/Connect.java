package transactSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static transactSQL.DatabaseConnection.*;

public class Connect {

    public static String watson() {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement();) {

            return transactSQL.Query.getWord(1, 2, 3, 4, 5);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}