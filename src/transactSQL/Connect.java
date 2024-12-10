package transactSQL;

import dataStructures.LetterGroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static transactSQL.DatabaseConnection.*;

public class Connect {

    public static void watson(String reason, int numWords, char first, char second, char third, char fourth, char fifth) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            switch(reason) {
                case "getNumWordsInDB":
                    transactSQL.Query.getNumWordInDB();
                    break;
                case "createWordPairsTable":
                    transactSQL.Select.createPairsTable();
                    break;
                case "getWords":
                    transactSQL.Query.getWords(numWords, first, second, third, fourth, fifth);
                    break;
                case "deleteDups":
                    transactSQL.Delete.deleteDupsFromPairsTable();
                    break;
                default:
                    System.out.println("Reason for connecting to the DB not recognized.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void watson(char mostCommonLetter) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            transactSQL.Select.wordPairsDifferByLetter(mostCommonLetter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void watson(LetterGroup knownTogether) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            knownTogether.keySetToArray();

            transactSQL.Select.wordsContainingTwoLetters((Character) knownTogether.elements[0], (Character) knownTogether.elements[1]);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}