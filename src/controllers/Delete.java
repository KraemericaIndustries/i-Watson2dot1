package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static controllers.DatabaseConnection.*;

public class Delete {

    public static void wordsWith(char c) throws SQLException {

        System.out.println("+++ Delete.wordsWith +++");

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
        System.out.println("--- Delete.wordsWith ---");
    }

    public static void wordsWithout(char c) throws SQLException {

        System.out.println(" Delete.wordsWithout ");

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
        System.out.println("--- Delete.wordsWithout ---");
    }
}