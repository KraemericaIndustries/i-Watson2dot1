package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class Create extends DatabaseConnection {

    //  CREATE to watson DB...
    public static void createWatsonDB() throws Exception {
        System.out.println("Preparing to create the watson database...");
        //  RESTART the mssqlserver service (to assure there is no other process accessing the DB)
        System.out.println("Restarting the MSSQLSERVER Service (to ensure individual access to the database)...");
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "net stop mssqlserver && ping 127.0.0.1 -n 2 > nul && net start mssqlserver");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }

        System.out.println("Dropping and re-creating the 'watson' database...");
        System.out.println("Connecting to: " + url + "...");
        try (Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement()) {
            System.out.println(" > Connection established!");
            statement.addBatch("drop database watson;" +
                                   "WAITFOR DELAY '00:00:05';"  +
                                   "create database watson;" +
                                   "use watson;"  +
                                   "create table Words_tbl (word varchar(5) primary key(word));" +
                                   "create table letterCounts_tbl (Letter varchar, Count int);");
            try {
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(" > 'watson' database created!");
        System.out.println();

        url = url + "DatabaseName=watson";
    }
}