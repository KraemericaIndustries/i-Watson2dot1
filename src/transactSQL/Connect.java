package transactSQL;

import dataStructures.LetterGroup;

import java.sql.*;

import static transactSQL.DatabaseConnection.*;

public class Connect {

    public static void watson(String reason) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            switch(reason) {
                case "getNumWordsInDB":
                    transactSQL.Query.getNumWordInDB();
                    break;
                case "createWordPairsTable":
                    //  This query was generated with assistance from Microsoft Copilot:
                    String wordPairsTable = "SELECT w1.word AS word1, w2.word AS word2 \n" +
                            "INTO WordPairs FROM Words_tbl w1, Words_tbl w2 \n" +
                            "WHERE w1.word <> w2.word \n" +
                            "AND ( \n" +
                            "\t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1)) \n" +
                            "OR \n" +
                            "\t(SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1)) \n" +
                            "OR \n" +
                            "\t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1)) \n" +
                            "OR \n" +
                            "\t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1)) \n" +
                            "OR \n" +
                            "\t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1))\n" +
                            ");";

                    System.out.println("Creating a table of word pairs that only DIFFER by 1 letter...");
                    transactSQL.Query.runStatement(wordPairsTable);
                    System.out.println("Finished creating WordPairs table!");
                    break;
//                case "getWords":
//                    transactSQL.Query.getWords(numWords, first, second, third, fourth, fifth);
//                    break;
                case "deleteDups":
                    transactSQL.Delete.deleteDupsFromPairsTable();
                    break;
                case "countWordPairs":
                    int num = transactSQL.Select.countWordPairs();
                    System.out.println(" > There are " + num + " word pairs that differ by 1 letter in the database.");
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