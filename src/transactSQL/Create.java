package transactSQL;

import dataStructures.IdentifiedLetters;
import dataStructures.Unknown;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Set;

import static assess.AllTurns.regenerateWordPairsTable;

public class Create extends DatabaseConnection{

    //  CREATE to watson DB...
    public static void watsonDB() throws Exception {
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
            statement.addBatch("DROP DATABASE watson;");
            statement.addBatch("WAITFOR DELAY '00:00:05';");
            statement.addBatch("CREATE DATABASE watson;");
            statement.addBatch("USE watson;");
            statement.addBatch("CREATE TABLE Words_tbl (word varchar(5) PRIMARY KEY);");
            statement.addBatch("CREATE TABLE WordPairs (Id INT IDENTITY(1,1) PRIMARY KEY, word1 varchar(5), word2 varchar(5));");
            statement.addBatch("CREATE TYPE CharList AS TABLE (ch char(1));");
            statement.addBatch(
                    "DECLARE @CharList CharList;" +
                            "INSERT INTO @CharList VALUES ('a'), ('b'), ('c');" +   // example
                            "SELECT TOP 1 wp.*\n" +
                            "FROM WordPairs wp\n" +
                            "CROSS APPLY (\n" +
                            "    SELECT SUM(CASE WHEN CHARINDEX(ch, wp.word1) > 0 THEN 1 ELSE 0 END) AS w1count\n" +
                            "    FROM @CharList\n" +
                            ") a\n" +
                            "CROSS APPLY (\n" +
                            "    SELECT SUM(CASE WHEN CHARINDEX(ch, wp.word2) > 0 THEN 1 ELSE 0 END) AS w2count\n" +
                            "    FROM @CharList\n" +
                            ") b\n" +
                            "WHERE (a.w1count = 2 AND b.w2count = 1)\n" +
                            "   OR (a.w1count = 1 AND b.w2count = 2);"
            );
            statement.addBatch("CREATE TABLE letterCounts_tbl (Letter varchar(1), Count int);");
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

    public static void copilotWatsonDB() throws Exception {

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement s = conn.createStatement()) {

            s.execute("IF DB_ID('watson') IS NOT NULL DROP DATABASE watson;");
            s.execute("CREATE DATABASE watson;");
        }

        try (Connection conn = DriverManager.getConnection(urlToWatson, user, password);
             Statement s = conn.createStatement()) {

            s.execute("CREATE TABLE Words_tbl (word VARCHAR(5) PRIMARY KEY);");
            s.execute("CREATE TABLE WordPairs (Id INT IDENTITY(1,1) PRIMARY KEY, word1 VARCHAR(5), word2 VARCHAR(5));");
            s.execute("CREATE TYPE CharList AS TABLE (ch CHAR(1));");
            s.execute("CREATE PROCEDURE FindWordPair\n" +
                    "    @chars CharList READONLY\n" +
                    "AS\n" +
                    "BEGIN\n" +
                    "    SET NOCOUNT ON;\n" +
                    "\n" +
                    "    SELECT TOP 1 wp.*\n" +
                    "    FROM WordPairs wp\n" +
                    "    CROSS APPLY (\n" +
                    "        SELECT \n" +
                    "            SUM(w1match) AS w1count,\n" +
                    "            SUM(w2match) AS w2count\n" +
                    "        FROM (\n" +
                    "            SELECT \n" +
                    "                CASE WHEN CHARINDEX(c.ch, wp.word1) > 0 THEN 1 ELSE 0 END AS w1match,\n" +
                    "                CASE WHEN CHARINDEX(c.ch, wp.word2) > 0 THEN 1 ELSE 0 END AS w2match\n" +
                    "            FROM @chars AS c\n" +
                    "        ) d\n" +
                    "    ) x\n" +
                    "    WHERE (x.w1count = 2 AND x.w2count = 1)\n" +
                    "       OR (x.w1count = 1 AND x.w2count = 2);\n" +
                    "END;\n");
            s.execute("CREATE TABLE letterCounts_tbl (Letter VARCHAR(1), Count INT);");

            String query =
                    "DECLARE @CharList CharList;\n" +
                            "INSERT INTO @CharList VALUES ('a'), ('b'), ('c');\n" +
                            "\n" +
                            "SELECT TOP 1 wp.*\n" +
                            "FROM WordPairs wp\n" +
                            "CROSS APPLY (\n" +
                            "    SELECT SUM(w1match) AS w1count\n" +
                            "    FROM (\n" +
                            "        SELECT CASE WHEN CHARINDEX(cl.ch, wp.word1) > 0 THEN 1 ELSE 0 END AS w1match\n" +
                            "        FROM @CharList cl\n" +
                            "    ) x\n" +
                            ") a\n" +
                            "CROSS APPLY (\n" +
                            "    SELECT SUM(w2match) AS w2count\n" +
                            "    FROM (\n" +
                            "        SELECT CASE WHEN CHARINDEX(cl.ch, wp.word2) > 0 THEN 1 ELSE 0 END AS w2match\n" +
                            "        FROM @CharList cl\n" +
                            "    ) y\n" +
                            ") b\n" +
                            "WHERE (a.w1count = 2 AND b.w2count = 1)\n" +
                            "   OR (a.w1count = 1 AND b.w2count = 2);";

            s.execute(query);
        }


    }

    //  RELOAD remaining words into tables...
    //  (This is how we get our letter counts - by loading the tables.)
    public static void rebuildWatsonDB(Set<Character> s, Unknown unknown) {

        Query.wordsFromDB();                            //  QUERY remaining words out to a file
        Connect.watson("deleteFromWordsTable");  //  DELETE the remaining words from Words_tbl
        Insert.reloadKnownWords();                      //  RELOAD words from file

        // remove knownTogether(set) from unknown(linkedHashMap)
        for (Character c : s) {
            Unknown.letters.remove(c);
        }
        unknown.sort();  //  SORT unknown letters by frequency of occurrence in the database

        regenerateWordPairsTable();  // rebuild WordPairs table
    }
}