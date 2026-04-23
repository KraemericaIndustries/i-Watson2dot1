package transactSQL;

import dataStructures.Dashboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static transactSQL.DatabaseConnection.*;

public class Delete {

    public static void rowFromWordPairs(String word) throws SQLException {

//        System.out.println("transactSQL.Delete.rowFromWordPairs(): BEGIN");

        Connection conn = DriverManager.getConnection(urlToWatson, user, password); Statement statement = conn.createStatement(); {
            try {
                statement.addBatch("delete from WordPairs where word1 like '" + word +"'");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

//        System.out.println("transactSQL.Delete.rowFromWordPairs(): END");
    }

    public static void fromWordsTable(Dashboard dashboard) throws SQLException {

        Connection conn = DriverManager.getConnection(urlToWatson, user, password);
        Statement statement = conn.createStatement();
        {
            if(!process.DashboardChanges.changesToKnownIn.isEmpty()) {
                for (Character c : process.DashboardChanges.changesToKnownIn) {
                    statement.addBatch("delete from Words_tbl where word not like '%" + c + "%'");
                }
            }

            if(!process.DashboardChanges.changesToKnownOut.isEmpty()) {
                for (Character c : process.DashboardChanges.changesToKnownOut) {
                    statement.addBatch("delete from Words_tbl where word like '%" + c + "%'");
                }
            }
            statement.executeBatch();
        }
    }
}