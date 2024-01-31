package transactSQL;

import dataStructures.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import static transactSQL.DatabaseConnection.*;

public class Connect {

    //  Overloaded method to establish a connection to the watson DB for a given reason.  Connection to remain open while sql  statements run their course...
    public static String watson(String reason) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            switch(reason) {
                case "getNumWordsInDB":
                    return transactSQL.Query.getNumWordInDB();
                default:
                    System.out.println("Reason for connecting to the DB not recognized.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String watson(String reason, LetterGroup knownTogether, Unknown unknown, LinkedList<Turn> Turns) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            switch(reason) {
                case "getWords":
                    if(Turns.isEmpty()) {
                        return transactSQL.Query.getWords(1, 0, 1, 2, 3, 4, unknown);

                    } else {
                        return transactSQL.Query.getWords(1, 2, 3, 4, 5, 6, unknown);
                    }
                case "getNumWordsInDB":
                    return transactSQL.Query.getNumWordInDB();
                default:
                    System.out.println("Reason for connecting to the DB not recognized.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}