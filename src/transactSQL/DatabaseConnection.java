package transactSQL;

import print.Colors;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {
    static String url;
    static String urlToWatson;
    static String user;
    static String password;
    static Connection conn;
    static Statement statement;
    static ResultSet resultSet;

    //  READ the watson DB url and credentials from the configuration file...
    public static void getProperties() {
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_PURPLE, "********************************************************************************  SETUP  ***********************************************************************************"));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_PURPLE, "Reading the watson.properties file..."));
        java.util.Properties props = new java.util.Properties();
        try {
            props.load(new FileInputStream("watson.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        url = props.getProperty("url");
        urlToWatson = props.getProperty("urlToWatson");
        user = props.getProperty("user");
        password = props.getProperty("password");
        //  DEBUG:
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_PURPLE, " > Successfully read the following values: "));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_PURPLE, " > Database url: " + url));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_PURPLE, " > Database urlToWatson: " + urlToWatson));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_PURPLE, " > Username: " + user));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_PURPLE, " > Password: " + password));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, " > Success!\n"));
    }
}