package transactSQL;

import dataStructures.LetterGroup1D;
import dataStructures.LetterGroup2D;
import dataStructures.Matrix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static dataStructures.Matrix.truthTable;
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

    public static String watson(String reason, LetterGroup1D knownTogether, LetterGroup2D frequency) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            switch(reason) {
                case "getWords":
                    if(knownTogether.hasLetters == false) {
//                        Todo: determine the index positions of letters in knownTogether (XOR?)  //  Pass a row?  build.wordsQuery()?
//                        Todo: search DB for a word using the mostCommonLetters that are not those letters
                        return transactSQL.Query.getWords(1, 0, 1, 2, 3, 4, frequency);

                    } else {
                        return transactSQL.Query.getWords(1, 2, 3, 4, 5, 6, frequency);
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