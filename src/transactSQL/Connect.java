package transactSQL;

import java.sql.*;
import java.util.Set;

import static transactSQL.DatabaseConnection.*;

//  IDEALLY all database interactions are accomplished through a single class (this one)
//  PARAMETERIZED database invocations are found in OVERLOADED methods in this class
public class Connect {

    //  The VAST MAJORITY of database invocations (primarily NOT requiring a return) are handled through this method - switching on the reason for the requested connection...
    public static Object watson(String reason) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

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
                    String wordPairsTable = "SELECT w1.word AS word1, w2.word AS word2 \n" +
                            "INTO WordPairs FROM Words_tbl w1, Words_tbl w2 \n" +
                            "WHERE w1.word <> w2.word \n" +
                            "AND ( \n" +
                            "\t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1)) \n" +
                            "OR \n" +
                            "\t(SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1)) \n" +
                            "OR \n" +
                            "\t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1)) \n" +
                            "OR \n" +
                            "\t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1)) \n" +
                            "OR \n" +
                            "\t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND \n" +
                            "\t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1))\n" +
                            ");";

                    System.out.println("Creating a table of word pairs that only DIFFER by 1 letter...");
                    transactSQL.Query.runStatement(wordPairsTable);
                    System.out.println("Finished creating WordPairs table!");
                    break;
//                case "getWords":
//                    transactSQL.Query.getWords(numWords, first, second, third, fourth, fifth);
//                    break;
                case "deleteDups":

                    String deleteDuplicates = "DELETE a FROM WordPairs a " +
                            "JOIN WordPairs b ON a.word1 = b.word2 " +
                            "AND a.word2 = b.word1 " +
                            "WHERE a.word1 < a.word2;";

                    System.out.println("Deleting duplicates from the WordPairs table...");
                    transactSQL.Query.runStatement(deleteDuplicates);
                    System.out.println("Finished deleting duplicates from the WordPairs table!");
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

                    ResultSet rs3 = Query.select("select * from Words_tbl");

                    while(rs3.next()) {
                        System.out.println(rs3.getString(1));
                    }

                    break;
                case "queryWordPairsByKnownTogether":
                    System.out.println("Selecting all remaining words from Words_tbl...");

                    ResultSet rs4 = Query.select("select * from Words_tbl");

                    while(rs4.next()) {
                        System.out.println(rs4.getString(1));
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
    public static void watson(char mostCommonLetter) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            transactSQL.Select.wordPairsDifferByLetter(mostCommonLetter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  In the event letters are known to be together, make a determination by leveraging the WordPairs table...
    public static void watson(Set<Character> knownTogether) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            StringBuilder sb = new StringBuilder();

            sb.append("SELECT TOP (5) Word " +
                    "FROM Words_tbl YT " +
                    "CROSS JOIN (VALUES('");
            for(Character c : knownTogether) {
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

            ResultSet rs5 = Query.select(query);

            while(rs5.next()) {
                System.out.println(rs5.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}