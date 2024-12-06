package transactSQL;

import dataStructures.LetterGroup;
import dataStructures.Unknown;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static transactSQL.DatabaseConnection.*;

public class Delete {

    public static void wordsWith(char c) throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {
            System.out.println("Deleting all words containing '" + c + "' from the database...");
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

    public static void wordsWithout(char c) throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {
            System.out.println("Deleting all words NOT containing '" + c + "' from the database...");
            try {
                statement.executeUpdate("delete from Words_tbl where word not like '%" + c + "%'");
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

    public static void deleteDupsFromPairsTable() throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {
        //  DEBUG:  System.out.println("Deleting DUPLICATES from WordPairs table...");
            try {
                //  This query was generated with assistance from Microsoft Copilot:
                statement.executeUpdate("DELETE a FROM WordPairs a " +
                                            "JOIN WordPairs b ON a.word1 = b.word2 " +
                                            "AND a.word2 = b.word1 " +
                                            "WHERE a.word1 < a.word2;"
                );
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

    public static void wordsWith(String s, Unknown unknown) throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {
            System.out.println("Deleting all words containing '" + s + "' from the database...");
            try {
                for(int i = 0; i < s.length(); i++) {
                    statement.addBatch("delete from Words_tbl where word like '%" + s.charAt(i) + "%'");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Query.wordsFromDB();
        Insert.reloadKnownWords();
        unknown.sort();
        System.out.println("Delete.wordsWith: END");
    }
}