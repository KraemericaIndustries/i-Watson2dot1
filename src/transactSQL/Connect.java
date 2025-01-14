package transactSQL;

import assess.AllTurns;
import dataStructures.Turn;
import dataStructures.Unknown;
import print.Messages;

import java.sql.*;
import java.util.*;

import static transactSQL.DatabaseConnection.*;

//  IDEALLY all database interactions are accomplished through a single class (this one)
//  PARAMETERIZED database invocations are found in OVERLOADED methods in this class
public class Connect {

    //  The VAST MAJORITY of database invocations (primarily NOT requiring a return) are handled through this method -
    //  (switching on the reason for the requested connection...)
    public static Object watson(String reason) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            switch (reason) {
                case "getNumWordsInDB":
                    ResultSet resultSet1 = Query.select("select count (*) from Words_tbl");

                    int numWords = 0;
                    while (resultSet1.next()) {
                        numWords += ((Number) resultSet1.getObject(1)).intValue();
                    }

                    return numWords;
                case "createWordPairsTable":
                    //  This query was generated with assistance from Microsoft Copilot:
                    String wordPairsTable = """
                            SELECT w1.word AS word1, w2.word AS word2\s
                            INTO WordPairs FROM Words_tbl w1, Words_tbl w2\s
                            WHERE w1.word <> w2.word\s
                            AND (\s
                            \t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND\s
                            \t SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND\s
                            \t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND\s
                            \t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1))\s
                            OR\s
                            \t(SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND\s
                            \t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND\s
                            \t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1) AND\s
                            \t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1))\s
                            OR\s
                            \t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND\s
                            \t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND\s
                            \t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1) AND\s
                            \t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1))\s
                            OR\s
                            \t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND\s
                            \t SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND\s
                            \t SUBSTRING(w1.word, 4, 1) = SUBSTRING(w2.word, 4, 1) AND\s
                            \t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1))\s
                            OR\s
                            \t(SUBSTRING(w1.word, 1, 1) = SUBSTRING(w2.word, 1, 1) AND\s
                            \t SUBSTRING(w1.word, 2, 1) = SUBSTRING(w2.word, 2, 1) AND\s
                            \t SUBSTRING(w1.word, 3, 1) = SUBSTRING(w2.word, 3, 1) AND\s
                            \t SUBSTRING(w1.word, 5, 1) = SUBSTRING(w2.word, 5, 1))
                            );""";

                    System.out.println("Creating a table of word pairs that only DIFFER by 1 letter...");
                    transactSQL.Query.runStatement(wordPairsTable);
                    System.out.println(" > Finished creating WordPairs table!");
                    break;
                case "deleteDups":

                    String deleteDuplicates = "DELETE a FROM WordPairs a " +
                            "JOIN WordPairs b ON a.word1 = b.word2 " +
                            "AND a.word2 = b.word1 " +
                            "WHERE a.word1 < a.word2;";
                    System.out.println("Deleting duplicates from the WordPairs table...");
                    transactSQL.Query.runStatement(deleteDuplicates);
                    System.out.println(" > Finished deleting duplicates from the WordPairs table!");
                    break;
                case "countWordPairs":

                    ResultSet resultSet2 = Query.select("select count (*) from WordPairs");

                    int numPairs = 0;
                    while (resultSet2.next()) {
                        numPairs += ((Number) resultSet2.getObject(1)).intValue();
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

                    ResultSet resultSet3 = Query.select("select * from Words_tbl...");

                    while (resultSet3.next()) {
                        System.out.println(resultSet3.getString(1));
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

    /*
    //  ToDo: Do NOT delete this method - contains valuable concept for keeping most common letters in play
    Using this reference:
    https://www.geeksforgeeks.org/how-to-iterate-linkedhashmap-in-java/
    Pull the TOP FIVE letters remaining in the database that are UNKNOWN
    The Unknown object passed by a parameter is backed by a LinkedHashMap which is ALREADY SORTED!
    */
//  ToDo: Do NOT delete this method - contains valuable concept for keeping most common letters in play
//    public static void watson(Unknown unknown) {
//
//        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {
//
//            Character[] fiveMostCommonLetters = new Character[5];                      //  DECLARE an array to hold the desired number (5) of keys from the LinkedHashMap
//            Set<Map.Entry<Character, Integer>> entrySet = Unknown.letters.entrySet();  // Get a set of all the entries (key - value pairs) contained in the LinkedHashMap
//            Iterator<Map.Entry<Character, Integer>> it = entrySet.iterator();          // Obtain an Iterator for the entries Set
//
//            // Iterate through LinkedHashMap entries
//            int i = 0;  //  Set an index to break out of the 'while=hasNext' loop once the desired number of keys is retrieved
//
//            while (it.hasNext()) {
//                fiveMostCommonLetters[i] = it.next().getKey();
//                i++;
//                if(i == 5) break;
//            }
//
//            String query = "SELECT TOP (5) word1, word2 " +
//                    "FROM WordPairs YT " +
//                    "CROSS JOIN (VALUES('" +
//                    fiveMostCommonLetters[0] + "'),('" +
//                    fiveMostCommonLetters[1] + "'),('" +
//                    fiveMostCommonLetters[2] + "'),('" +
//                    fiveMostCommonLetters[3] + "'),('" +
//                    fiveMostCommonLetters[4] + "'))L(Letter) " +
//                    "GROUP BY YT.word1, word2 " +
//                    "ORDER BY COUNT(" +
//                        "CASE WHEN YT.word1 LIKE '%' + L.Letter + '%' THEN " +
//                            "1 " +
//                        "END) " +
//                    "DESC";
//
//            ResultSet rs6 = Query.select(query);
//
//            while(rs6.next()) {
//                System.out.println(rs6.getString(1) + ", " + rs6.getString(2));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public static void watson(String sqlQuery, Set<Character> set, Unknown unknown) throws SQLException {

        ResultSet resultSet6 = Query.select(sqlQuery);

        while (!resultSet6.next()) {
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
        Create.rebuildWatsonDB(unknown);
        System.out.println();
    }

    public static void watson() throws SQLException {

        if (Messages.numWordPairs > 0) {

            char mostCommonUnknownLetter = Unknown.printFirstEntry();
            String query = "select TOP (5) word1, word2 " +
                    "FROM WordPairs " +
                    "where " +
                    "word1 like '%" + mostCommonUnknownLetter + "%' and " +
                    "word2 not like '%" + mostCommonUnknownLetter + "%' or " +
                    "word1 not like '%" + mostCommonUnknownLetter + "%' and " +
                    "word2 like '%" + mostCommonUnknownLetter + "%';";

            ResultSet resultSet7 = Query.select(query);

            while (resultSet7.next()) {
                System.out.println(resultSet7.getString(1) + ", " + resultSet7.getString(2));
            }
        } else {
            System.out.println();
        }
    }

    public static void watson(ArrayList<Character> al1, ArrayList<Character> al2, Unknown unknown, Set<Character> knownIn, LinkedList<Turn> Turns) throws SQLException {

        //  OBTAIN the 2 characters the database will be searched for...
        char a = al1.get(0);
        char b = al2.get(0);
        int numWordsContainingLetters = 0;

        String sqlQuery = "select count (*) " +
                "from Words_tbl " +
                "where " +
                "word like '%" + a + "%' and " +
                "word like '%" + b + "%';";

        ResultSet resultSet8 = Query.select(sqlQuery);

        while (resultSet8.next()) {
            numWordsContainingLetters = (resultSet8.getInt(1));
        }

        //  HOLE!  Logic Busted!  Can not just delete!  Still need knownTogether!!!
        if (numWordsContainingLetters == 0) {
            Delete.wordsWith(String.valueOf(a) + b, unknown);
            AllTurns.removeStringFromAllTurns(String.valueOf(a) + b, Turns);
        } else {
            Delete.wordsWithout(String.valueOf(a) + b, unknown, knownIn);
        }
    }
}