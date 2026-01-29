package transactSQL;

import java.sql.*;

import static transactSQL.DatabaseConnection.*;

public class Select {

    //  QUERY the WordPairs table for any pairs that differ by the most commonly occurring letter in the database...
    public static void wordPairsDifferByLetter(char mostCommonLetter) throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password);
        Statement statement = conn.createStatement(); {

            ResultSet resultSet = transactSQL.Query.select("select TOP (6) word1, word2\n" +
                                                                      "from WordPairs\n" +
                                                                      "where word1 not like '%" + mostCommonLetter + "%'\n" +
                                                                      "and word2 like '%" + mostCommonLetter + "%'");

            while(resultSet.next()) {
                System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2));
            }
        }
    }

    public static void wordsContainingTwoLetters(char letter) throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password);
        Statement statement = conn.createStatement(); {

            ResultSet resultSet = transactSQL.Query.select("select TOP (6) word1, word2\n" +
                                                                      "from WordPairs\n" +
                                                                      "where word1 like '%" + letter + "%'\n" +
                                                                      "and word1 not like '%" + letter + "%'" +
                                                                      "or word1 not like '%" + letter + "%'" +
                                                                      "and word2 like '%" + letter + "%'");

            while(resultSet.next()) {
                System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2));
            }
        }
    }
    public static void lastNumWordPairs() throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password);
        Statement statement = conn.createStatement(); {

            ResultSet resultSet = transactSQL.Query.select("select * from WordPairs");

            while(resultSet.next()) {
                System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2));
            }
        }
    }

//    public static void selectBestWordPair() {
//
//        //  ToDo:  Well, crap.  This query from Copilot seems solid - but currently unknown letters is backed by a LinkedHashMap<Character, Integer> collection, and this is NOT INDEXED.  Seeking alternatives to LinkedHashMap
//
//        String sql = "/* the T-SQL above without the DECLAREs, using ? placeholders */\n"
//                + "SELECT TOP (1) wp.Id, CASE WHEN w1.HasMost = 1 THEN wp.Word1 ELSE wp.Word2 END AS ChosenWord, "
//                + "CASE WHEN w1.HasMost = 1 THEN w1.TopCount ELSE w2.TopCount END AS ChosenTopCount, "
//                + "CASE WHEN w1.HasMost = 1 THEN wp.Word2 ELSE wp.Word1 END AS OtherWord "
//                + "FROM WordPairs wp "
//                + "CROSS APPLY (SELECT CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END AS HasMost, "
//                + "(CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END "
//                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END "
//                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END "
//                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END "
//                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END) AS TopCount) w1 "
//                + "CROSS APPLY (SELECT CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END AS HasMost, "
//                + "(CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END "
//                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END "
//                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END "
//                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END "
//                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END) AS TopCount) w2 "
//                + "WHERE (w1.HasMost + w2.HasMost) = 1 "
//                + "ORDER BY CASE WHEN w1.HasMost = 1 THEN w1.TopCount ELSE w2.TopCount END DESC, wp.Id ASC;";
//
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            // bind parameters in order:
//            // 1 = mostLetter for Word1 HasMost
//            // 2..6 = t1..t5 for Word1 TopCount
//            // 7 = mostLetter for Word2 HasMost
//            // 8..12 = t1..t5 for Word2 TopCount
//            ps.setString(1, String.valueOf(mostLetter));
//            ps.setString(2, String.valueOf(t1));
//            ps.setString(3, String.valueOf(t2));
//            ps.setString(4, String.valueOf(t3));
//            ps.setString(5, String.valueOf(t4));
//            ps.setString(6, String.valueOf(t5));
//            ps.setString(7, String.valueOf(mostLetter));
//            ps.setString(8, String.valueOf(t1));
//            ps.setString(9, String.valueOf(t2));
//            ps.setString(10, String.valueOf(t3));
//            ps.setString(11, String.valueOf(t4));
//            ps.setString(12, String.valueOf(t5));
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    int id = rs.getInt("Id");
//                    String chosen = rs.getString("ChosenWord");
//                    int count = rs.getInt("ChosenTopCount");
//                    String other = rs.getString("OtherWord");
//                    // use result
//                }
//            }
//        }
//
//    }
}