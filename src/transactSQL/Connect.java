package transactSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static transactSQL.DatabaseConnection.*;

public class Connect {

    //  Todo: The parameterization of letters NO LONGER MATTERS with the inception of STRATEGY #1.  STRIP IT OUT...
    public static void watson(String reason, int numWords, char first, char second, char third, char fourth, char fifth) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            switch(reason) {
                case "getWords":
                    transactSQL.Query.getWords(numWords, first, second, third, fourth, fifth);
                    break;
                case "getNumWordsInDB":
                    transactSQL.Query.getNumWordInDB();
                    break;
                case "getAllWordsThatDifferByOneLetter":
                    //  ToDo: Resolve this:
                    transactSQL.Query.getAllWordsThatDifferByOneLetter();
                    break;
                default:
                    System.out.println("Reason for connecting to the DB not recognized.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}