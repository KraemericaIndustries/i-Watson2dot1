package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static controllers.DatabaseConnection.*;

public class Connect {

    public static void watson(String reason, int numWords, char first, char second, char third, char fourth, char fifth) {

        System.out.println("+++ Connect.watson +++");

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            switch(reason) {
                case "getWords":
                    Query.getWords(numWords, first, second, third, fourth, fifth);
                    break;
                case "getNumWordsInDB":
                    Query.getNumWordInDB();
                    break;
                default:
                    System.out.println("Reason for connecting to the DB not recognized.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("--- Connect.watson ---");
    }
}