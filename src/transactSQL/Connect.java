package transactSQL;

import dataStructures.IdentifiedLetters;
import dataStructures.Pairs;
import dataStructures.Unknown;
import print.Messages;

import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

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

                    System.out.println("Creating a table of word pairs that only DIFFER by 1 letter...");
                    transactSQL.Query.runStatement(wordPairsTable);
                    System.out.println(" > Finished creating WordPairs table!");
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
                case "dropWordPairsTable":
                    System.out.println("Dropping the WordPairs table (so it can be re-created)...");
                    Query.runStatement("DROP TABLE IF EXISTS WordPairs");
                    System.out.println("Finished dropping the WordPairs table!");
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

    //  QUERY the WordPairs table for any pairs that differ by the most commonly occurring letter in the database...
//    public static void watson(char mostCommonLetter) {
//
//        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {
//
//            transactSQL.Select.wordPairsDifferByLetter(mostCommonLetter);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    //  In the event letters are known to be together, make a determination by leveraging the WordPairs table...
    public static ResultSet watson(Set<Character> knownTogether) {

        ResultSet rs5 = null;
        try (Connection conn = DriverManager.getConnection(urlToWatson, user, password); Statement ignored = conn.createStatement()) {

            StringBuilder sb = new StringBuilder();

            sb.append("SELECT TOP (5) Word " +
                      "FROM Words_tbl YT " +
                      "CROSS JOIN (VALUES('");
                      for (Character c : knownTogether) {
                          sb.append(c).append("'),('");
                      }
                      String query = sb.substring(0, sb.length() - 3) +  //  Strip the last 3 characters
                      ")L(Letter)" +
                      "GROUP BY YT.Word " +
                      "ORDER BY COUNT(" +
                      "CASE WHEN YT.Word LIKE '%' + L.Letter + '%' THEN " +
                      "1 " +
                      "END) " +
                      "DESC";

            rs5 = Query.select(query);

            while (rs5.next()) {
                System.out.println(rs5.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs5;
    }

    /*
    Using this reference:
    https://www.geeksforgeeks.org/how-to-iterate-linkedhashmap-in-java/
    Pull the TOP FIVE letters remaining in the database that are UNKNOWN
    The Unknown object passed by a parameter is backed by a LinkedHashMap which is ALREADY SORTED!
    */
    public static void watson(Unknown unknown) {

        try (Connection conn = DriverManager.getConnection(urlToWatson, user, password); Statement ignored = conn.createStatement()) {

            Character[] fiveMostCommonLetters = new Character[5];                      //  DECLARE an array to hold the desired number (5) of keys from the LinkedHashMap
            Set<Map.Entry<Character, Integer>> entrySet = Unknown.letters.entrySet();  // Get a set of all the entries (key - value pairs) contained in the LinkedHashMap
            Iterator<Map.Entry<Character, Integer>> it = entrySet.iterator();          // Obtain an Iterator for the entries Set

            // Iterate through LinkedHashMap entries
            int i = 0;  //  Set an index to break out of the 'while=hasNext' loop once the desired number of keys is retrieved

            while (it.hasNext()) {
                fiveMostCommonLetters[i] = it.next().getKey();
                i++;
                if(i == 5) break;
            }

            String query = "SELECT TOP (5) word1, word2 " +
                    "FROM WordPairs YT " +
                    "CROSS JOIN (VALUES('" +
                    fiveMostCommonLetters[0] + "'),('" +
                    fiveMostCommonLetters[1] + "'),('" +
                    fiveMostCommonLetters[2] + "'),('" +
                    fiveMostCommonLetters[3] + "'),('" +
                    fiveMostCommonLetters[4] + "'))L(Letter) " +
                    "GROUP BY YT.word1, word2 " +
                    "ORDER BY COUNT(" +
                        "CASE WHEN YT.word1 LIKE '%' + L.Letter + '%' THEN " +
                            "1 " +
                        "END) " +
                    "DESC";

            ResultSet rs6 = Query.select(query);

            while(rs6.next()) {
                System.out.println(rs6.getString(1) + ", " + rs6.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  OBJECTIVE: Traverse all pairs, create a ResultSet for all pairs, print output from all pairs...
    public static void watson(Pairs pairs) {

        LinkedList<ResultSet> rsList= new LinkedList<>();

        for (Set<Character> s : pairs.knownTogether) {
            System.out.println();
            System.out.println();

            rsList.add(Connect.watson(s));
        }
    }

    public static void watson(String sqlQuery, Set<Character> set, Unknown unknown) throws SQLException {

        ResultSet rs6 = Query.select(sqlQuery);

        while(!rs6.next()) {
            StringBuilder sb = new StringBuilder();
            sb.append("delete from Words_tbl " +
                      "where " +
                      "word like '%");
                      for (Character c : set) {
                          sb.append(c).append("%' or word like '%");
                      }
                      sb.delete((sb.length() - 18), (sb.length() - 1));
                      sb.append("';");
            Query.runStatement(sb.toString());
            break;
        }
        Create.rebuildWatsonDB(set, unknown);
        System.out.println();
    }

    public static void watson(String sqlQuery, Unknown unknown) throws SQLException {

        if(Messages.numWordPairs > 0) {

            char mostCommonUnknownLetter = Unknown.printFirstEntry();
            String query = "select * from WordPairs " +
                    "where " +
                    "word1 like '%" + mostCommonUnknownLetter + "%' and " +
                    "word2 not like '%" + mostCommonUnknownLetter + "%' or " +
                    "word1 not like '%" + mostCommonUnknownLetter + "%' and " +
                    "word2 like '%" + mostCommonUnknownLetter + "%;'";
            ResultSet rs7 = Query.select(query);

            while(rs7.next()) {
                System.out.println(rs7.getString(1) + ", " + rs7.getString(2));
            }
        } else {
            System.out.println();
        }
    }
}