package transactSQL;

import print.Colors;
import java.sql.*;

import static transactSQL.DatabaseConnection.*;

//  IDEALLY all database interactions are accomplished through a single class (this one)
//  PARAMETERIZED database invocations are found in OVERLOADED methods in this class
public class Connect {

    //  The VAST MAJORITY of database invocations (primarily NOT requiring a return) are handled through this method -
    //  (switching on the reason for the requested connection...)
    public static Object watson(String reason) {

        try (Connection conn = DriverManager.getConnection(urlToWatson, user, password); Statement ignored = conn.createStatement()) {

            switch(reason) {
                case "getNumWordsInDB":
                    ResultSet rs1 = Query.select("select count (*) from Words_tbl");

                    int numWords = 0;
                    while(rs1.next()) {
                        numWords += ((Number) rs1.getObject(1)).intValue();
                    }

                    return numWords;
                case "createWordPairsTable":
                    //  This query was generated with assistance from Microsoft Copilot:
                    String wordPairsTable = """
                    INSERT INTO WordPairs (word1, word2)
                    SELECT w1.word, w2.word
                    FROM Words_tbl w1
                    JOIN Words_tbl w2
                        ON w1.word < w2.word   -- prevents duplicates and self-pairs
                    WHERE
                        (CASE WHEN SUBSTRING(w1.word,1,1) = SUBSTRING(w2.word,1,1) THEN 0 ELSE 1 END +
                         CASE WHEN SUBSTRING(w1.word,2,1) = SUBSTRING(w2.word,2,1) THEN 0 ELSE 1 END +
                         CASE WHEN SUBSTRING(w1.word,3,1) = SUBSTRING(w2.word,3,1) THEN 0 ELSE 1 END +
                         CASE WHEN SUBSTRING(w1.word,4,1) = SUBSTRING(w2.word,4,1) THEN 0 ELSE 1 END +
                         CASE WHEN SUBSTRING(w1.word,5,1) = SUBSTRING(w2.word,5,1) THEN 0 ELSE 1 END
                        ) = 1;""";

                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_PURPLE, "Creating a table of word pairs that only DIFFER by 1 letter..."));
                    transactSQL.Query.runStatement(wordPairsTable);
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, " > Finished creating WordPairs table!"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_PURPLE, "*********************************************************************************************************************************************************************************\n"));
                    break;
                case "countWordPairs":

                    ResultSet rs2 = Query.select("select count (*) from WordPairs");

                    int numPairs = 0;
                    while(rs2.next()) {
                        numPairs += ((Number) rs2.getObject(1)).intValue();
                    }
                    return numPairs;
                case "deleteFromWordsTable":
                    System.out.println("Deleting all words from the Words_tbl table...");
                    Query.runStatement("delete from Words_tbl");
                    System.out.println("Finished deleting all words from the Words_tbl table!");
                    break;
                case "truncateWordPairsTable":
                    System.out.println("Truncating WordPairs table...");
                    Query.runStatement("truncate table WordPairs");
                    System.out.println("Finished truncating WordPairs table!");
                    break;
                case "selectAllFromWordsTable":
                    System.out.println("Selecting all remaining words from Words_tbl...");

                    ResultSet rs3 = Query.select("select * from Words_tbl...");

                    while(rs3.next()) {
                        System.out.println(rs3.getString(1));
                    }
                    break;
                default:
                    System.out.println("Reason for connecting to the DB not recognized.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}