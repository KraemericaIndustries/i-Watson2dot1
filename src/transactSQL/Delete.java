package transactSQL;

import dataStructures.Unknown;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static transactSQL.DatabaseConnection.*;

public class Delete {

    //  DELETE words containing a given letter from the database...
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

    //  DELETE words NOT containing a given letter from the database...
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

    //  DELETE all words containing EVERY LETTER in a String, and UPDATE ALL data sources...
    public static void wordsWith(String s, Unknown unknown, Set<Character> knownIn) throws SQLException {

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
        Connect.watson("deleteFromWordsTable");
        Insert.reloadKnownWords();
        removeKnownInFromUnknown(knownIn);
        unknown.sort();
        System.out.println("Delete.wordsWith: END");
    }

    //  DELETE letters in the provided String from the database.  Rebuild data sources...
    public static void wordsWithout(String s, Unknown unknown, Set<Character> knownIn) throws SQLException {

        System.out.println("transactSQL.Delete.wordsWithout(): BEGIN");

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
        Connect.watson("deleteFromWordsTable");
        Insert.reloadKnownWords();
        removeKnownInFromUnknown(knownIn);
        unknown.sort();

        System.out.println("transactSQL.Delete.wordsWithout(): END");
    }

    //  Following each REBUILD of the database, all letters KNOWN IN must be REMOVED from the list of UNKNOWN letters
    private static void removeKnownInFromUnknown(Set<Character> knownIn) {

        for(Character c : knownIn) {
            Unknown.letters.remove(c);
        }
    }
}