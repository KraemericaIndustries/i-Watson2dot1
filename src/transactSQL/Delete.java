package transactSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static transactSQL.DatabaseConnection.*;

public class Delete {

    public static void words(char c) throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {
            System.out.println(" > Connection established!");
            try {
                statement.executeUpdate("delete from Words_tbl where word like '%" + c + "%'");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}