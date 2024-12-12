package transactSQL;

import dataStructures.LetterGroup;
import dataStructures.Unknown;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

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

    public static void wordsWith(String s, Unknown unknown, LetterGroup knownIn) throws SQLException {

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
        try {
            Delete.fromWordsTbl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Insert.reloadKnownWords();
        removeKnownInFromUnknown(knownIn);
        unknown.sort();
        System.out.println("Delete.wordsWith: END");
    }

    public static void wordsWithout(String s, Unknown unknown, LetterGroup knownIn) throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {
            System.out.println("Deleting all words NOT containing '" + s + "' from the database...");
            try {
                for(int i = 0; i < s.length(); i++) {
                    statement.addBatch("delete from Words_tbl where word not like '%" + s.charAt(i) + "%'");
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
        try {
            Delete.fromWordsTbl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Insert.reloadKnownWords();
        removeKnownInFromUnknown(knownIn);
        unknown.sort();
        System.out.println("Delete.wordsWith: END");
    }

    public static void dropWordPairsTable() throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {
            //  DEBUG:  System.out.println("Deleting DUPLICATES from WordPairs table...");
            try {
                statement.executeUpdate("DROP TABLE IF EXISTS WordPairs");
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

    private static void removeKnownInFromUnknown(LetterGroup knownIn) {
        Object[] letters;
        Set<Character> letterKeys = knownIn.letters.keySet();
        letters = letterKeys.toArray();
        for(Object c : letters) {
            Unknown.letters.remove((Character)c);
        }
    }

    public static void fromWordsTbl() throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {
            System.out.println("Deleting all words from the Words_tbl table...");
            try {
                statement.addBatch("delete from Words_tbl");

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("fromWordsTbl()");
    }



}