package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {
    static String url;
    static String user;
    static String password;
    static Connection conn;
    static Statement statement;
    static ResultSet resultSet;

    //  READ the watson DB url and credentials from the configuration file...
    public static void getDatabaseServerProperties() {
        System.out.println("Reading the watson.properties file...");
        java.util.Properties props = new java.util.Properties();
        try {
            props.load(new FileInputStream("watson.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        url = props.getProperty("url");
        user = props.getProperty("user");
        password = props.getProperty("password");
        //  DEBUG:
        System.out.println(" > Successfully read the following values: ");
        System.out.println(" > Database url: " + url);
        System.out.println(" > Username: " + user);
        System.out.println(" > Password: " + password);
        System.out.println(" > Success!\n");
    }
}