package transactSQL;

import dataStructures.Dashboard;

import java.sql.*;

public class Create extends DatabaseConnection{

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
            s.execute("""
                    CREATE PROCEDURE FindWordPair
                        @chars CharList READONLY
                    AS
                    BEGIN
                        SET NOCOUNT ON;
                    
                        SELECT TOP 1 wp.*
                        FROM WordPairs wp
                        CROSS APPLY (
                            SELECT\s
                                SUM(w1match) AS w1count,
                                SUM(w2match) AS w2count
                            FROM (
                                SELECT\s
                                    CASE WHEN CHARINDEX(c.ch, wp.word1) > 0 THEN 1 ELSE 0 END AS w1match,
                                    CASE WHEN CHARINDEX(c.ch, wp.word2) > 0 THEN 1 ELSE 0 END AS w2match
                                FROM @chars AS c
                            ) d
                        ) x
                        WHERE (x.w1count = 2 AND x.w2count = 1)
                           OR (x.w1count = 1 AND x.w2count = 2);
                    END;
                    """);
            s.execute("CREATE TABLE letterCounts_tbl (Letter VARCHAR(1), Count INT);");

            String query =
                    """
                            DECLARE @CharList CharList;
                            INSERT INTO @CharList VALUES ('a'), ('b'), ('c');
                            
                            SELECT TOP 1 wp.*
                            FROM WordPairs wp
                            CROSS APPLY (
                                SELECT SUM(w1match) AS w1count
                                FROM (
                                    SELECT CASE WHEN CHARINDEX(cl.ch, wp.word1) > 0 THEN 1 ELSE 0 END AS w1match
                                    FROM @CharList cl
                                ) x
                            ) a
                            CROSS APPLY (
                                SELECT SUM(w2match) AS w2count
                                FROM (
                                    SELECT CASE WHEN CHARINDEX(cl.ch, wp.word2) > 0 THEN 1 ELSE 0 END AS w2match
                                    FROM @CharList cl
                                ) y
                            ) b
                            WHERE (a.w1count = 2 AND b.w2count = 1)
                               OR (a.w1count = 1 AND b.w2count = 2);""";

            s.execute(query);
        }


    }

    //  RELOAD remaining words into tables...
    //  (This is how we get our letter counts - by loading the tables.)
    public static void rebuildWatsonDB(Dashboard dashboard) {

        Query.wordsFromDB();                            //  QUERY remaining words out to a file
        Connect.watson("deleteFromWordsTable");  //  DELETE the remaining words from Words_tbl
        Insert.reloadKnownWords(dashboard);             //  RELOAD words from file
    }
}