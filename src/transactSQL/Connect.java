package transactSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;

import static transactSQL.DatabaseConnection.*;

public class Connect {

    //  Todo: The parameterization of letters NO LONGER MATTERS with the inception of STRATEGY #1.  STRIP IT OUT...
    public static void watson(String reason, TreeMap<String, String> wordPairsThatDifferByOneLetter) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            switch(reason) {
                case "getAllWordsThatDifferByOneLetter":  //  STRATEGY #1
                    //  ToDo: Resolve this:
                    transactSQL.Query.getWordPairsThatDifferByOneLetter(wordPairsThatDifferByOneLetter);
                    break;
                case "getNumWordsInDB":
                    transactSQL.Query.getNumWordInDB();
                    break;
                default:
                    System.out.println("Reason for connecting to the DB not recognized.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}