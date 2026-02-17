package transactSQL;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataStructures.Dashboard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static transactSQL.DatabaseConnection.*;
public class Select {

//  QUERY the WordPairs table for any pairs that differ by the most commonly occurring letter in the database...
//    public static void wordPairsDifferByLetter(char mostCommonLetter) throws SQLException {
//
//        Connection conn = DriverManager.getConnection(url, user, password);
//        Statement statement = conn.createStatement(); {
//
//            ResultSet resultSet = transactSQL.Query.select("select TOP (6) word1, word2\n" +
//                                                                      "from WordPairs\n" +
//                                                                      "where word1 not like '%" + mostCommonLetter + "%'\n" +
//                                                                      "and word2 like '%" + mostCommonLetter + "%'");
//
//            while(resultSet.next()) {
//                System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2));
//            }
//        }
//    }

    public static void wordsContainingTwoLetters(char letter) throws SQLException {

        Connection conn = DriverManager.getConnection(urlToWatson, user, password);
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

        Connection conn = DriverManager.getConnection(urlToWatson, user, password);
        Statement statement = conn.createStatement(); {

            ResultSet resultSet = transactSQL.Query.select("select * from WordPairs");

            while(resultSet.next()) {
                System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2));
            }
        }
    }

    public static List<String> bestWordPair(Dashboard dashboard) {

        List<String> guesses = new ArrayList<>();

        String sql = "/* the T-SQL above without the DECLAREs, using ? placeholders */\n"
                + "SELECT TOP (1) wp.Id, CASE WHEN w1.HasMost = 1 THEN wp.Word1 ELSE wp.Word2 END AS ChosenWord, "
                + "CASE WHEN w1.HasMost = 1 THEN w1.TopCount ELSE w2.TopCount END AS ChosenTopCount, "
                + "CASE WHEN w1.HasMost = 1 THEN wp.Word2 ELSE wp.Word1 END AS OtherWord "
                + "FROM WordPairs wp "
                + "CROSS APPLY (SELECT CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END AS HasMost, "
                + "(CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END "
                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END "
                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END "
                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END "
                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word1)) > 0 THEN 1 ELSE 0 END) AS TopCount) w1 "
                + "CROSS APPLY (SELECT CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END AS HasMost, "
                + "(CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END "
                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END "
                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END "
                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END "
                + "+ CASE WHEN CHARINDEX(UPPER(?), UPPER(wp.Word2)) > 0 THEN 1 ELSE 0 END) AS TopCount) w2 "
                + "WHERE (w1.HasMost + w2.HasMost) = 1 "
                + "ORDER BY CASE WHEN w1.HasMost = 1 THEN w1.TopCount ELSE w2.TopCount END DESC, wp.Id ASC;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // bind parameters in order:
            // 1 = mostLetter for Word1 HasMost
            // 2..6 = t1..t5 for Word1 TopCount
            // 7 = mostLetter for Word2 HasMost
            // 8..12 = t1..t5 for Word2 TopCount
            ps.setString(1, String.valueOf(dashboard.unknownLetters.get(0).letter));
            ps.setString(2, String.valueOf(dashboard.unknownLetters.get(1).letter));
            ps.setString(3, String.valueOf(dashboard.unknownLetters.get(2).letter));
            ps.setString(4, String.valueOf(dashboard.unknownLetters.get(3).letter));
            ps.setString(5, String.valueOf(dashboard.unknownLetters.get(4).letter));
            ps.setString(6, String.valueOf(dashboard.unknownLetters.get(5).letter));
            ps.setString(7, String.valueOf(dashboard.unknownLetters.get(0).letter));
            ps.setString(8, String.valueOf(dashboard.unknownLetters.get(1).letter));
            ps.setString(9, String.valueOf(dashboard.unknownLetters.get(2).letter));
            ps.setString(10, String.valueOf(dashboard.unknownLetters.get(3).letter));
            ps.setString(11, String.valueOf(dashboard.unknownLetters.get(4).letter));
            ps.setString(12, String.valueOf(dashboard.unknownLetters.get(5).letter));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("Id");
                    String chosen = rs.getString("ChosenWord");
                    int count = rs.getInt("ChosenTopCount");
                    String other = rs.getString("OtherWord");
                    // use result
//                    System.out.println("id: " + id);
//                    System.out.println("chosen: " + chosen);
//                    System.out.println("other: " + other);
//                    System.out.println();
                    guesses.add(chosen);
                    guesses.add(other);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return guesses;
    }

    public static List<String> findTwoInOneOutFromWordPairs(Dashboard dashboard) throws SQLException {

        //  EXPLANATION of queryTwoInOneOutFromWordPairs()
        /*
        This method is meant to scour the dashboard.knwonTogether List of Sets of type <Character> looking for ANY rows in the WordPairs table
        where one word contains any two letters from a set while the other word only contains one.

        This is achieved by a stored procedure call to the dbo.FindWordPair stored procedure which was subsequently created for this purpose.

        The basis for all of this was a prompt sent in to Copilot asking the following:
        "I have a MSSQL database table created as follows:
        create table WordPairs (Id int IDENTITY(1,1) PRIMARY KEY, word1 varchar(5), word2 varchar(5));
        Each row in the table contains a pair of strings that differ by only one character.

        My java application provides a set of characters.
        I need to query this table for any rows where word1 contains any two of the characters from the set,
        while word2 contains only one of those two characters.  The reverse should also be true,
        word2 could contain any two characters in the set, while word1 contains only one of those 2 characters.
        Only one result should be returned.  Can you help me write the database query?"

         The original output for the stored procedure was:
         CREATE PROCEDURE FindWordPair
            @chars CharList READONLY
         AS
         BEGIN
            SELECT TOP 1 wp.*
            FROM WordPairs wp
            CROSS APPLY (
                SELECT
                    SUM(CASE WHEN CHARINDEX(ch, wp.word1) > 0 THEN 1 ELSE 0 END) AS w1count,
                    SUM(CASE WHEN CHARINDEX(ch, wp.word2) > 0 THEN 1 ELSE 0 END) AS w2count
                FROM @chars
            ) x
            WHERE (x.w1count = 2 AND x.w2count = 1)
               OR (x.w1count = 1 AND x.w2count = 2);
         END

         But this resulted in the following SQL run-time error:

           Msg 8124, Level 16, State 1, Procedure FindWordPair, Line 9 [Batch Start Line 99]
           Multiple columns are specified in an aggregated expression containing an outer reference. If an expression being aggregated contains an outer reference, then that outer reference must be the only column referenced in the expression.

        Which elicited still another prompt to Copilot in search of a solution:
        "This does not work: CREATE PROCEDURE FindWordPair @chars CharList READONLY AS BEGIN SELECT TOP 1 wp.* FROM WordPairs wp CROSS APPLY ( SELECT SUM(CASE WHEN CHARINDEX(ch, wp.word1) > 0 THEN 1 ELSE 0 END) AS w1count, SUM(CASE WHEN CHARINDEX(ch, wp.word2) > 0 THEN 1 ELSE 0 END) AS w2count FROM @chars ) x WHERE (x.w1count = 2 AND x.w2count = 1) OR (x.w1count = 1 AND x.w2count = 2); END
        When I run this in MSSQL, I get this: Msg 8124, Level 16, State 1, Procedure FindWordPair, Line 9 [Batch Start Line 99] Multiple columns are specified in an aggregated expression containing an outer reference. If an expression being aggregated contains an outer reference, then that outer reference must be the only column referenced in the expression. Why am I getting this error?"

        It is the response to this prompt that gave me the FindWordPair stored procedure as it presently stands.

        To accurately test the FindWordPair stored procedure, I found it necessary to MANUALLY STAGE some FALSE data.
        In separate iterations, I staged:
            insert into WordPairs values('TESAI', 'TESQI');
            insert into WordPairs values('TESQI', 'TESAI');
        Running the application on each.  Each time, the FindWordPair stored procedure returned a positive result - just as it should,
        based on the original prompt to Copilot.
         */

        List<String> guesses = new ArrayList<>();

        SQLServerDataTable charTable = new SQLServerDataTable();
        charTable.addColumnMetadata("ch", java.sql.Types.CHAR);

        for(Set<Character> kTset : dashboard.knownTogether) {
            for(char c : kTset) {
                charTable.addRow(String.valueOf(c));

                String sql = "{ call FindWordPair(?) }";

                try (Connection conn = DriverManager.getConnection(urlToWatson, user, password);
                     SQLServerCallableStatement stmt =
                             (SQLServerCallableStatement) conn.prepareCall(sql)) {

                    stmt.setStructured(1, "CharList", charTable);

                    boolean hasResult = stmt.execute();

                    if (hasResult) {
                        try (ResultSet rs = stmt.getResultSet()) {
                            if (rs.next()) {
                                int id = rs.getInt("Id");
                                String w1 = rs.getString("word1");
                                String w2 = rs.getString("word2");
                                // Use the result
                                guesses.add(w1);
                                guesses.add(w2);
                            }
                        }
                    }
                }
            }
        }
        return guesses;
    }

    public static List<String> findAsymmetricCharMatch(Dashboard dashboard) throws SQLException {

        /* I have a MSSQL database table created as follows:
            create table WordPairs (Id int IDENTITY(1,1) PRIMARY KEY, word1 varchar(5), word2 varchar(5));
           Each row in the table contains a pair of strings that differ by only one character.

           My java application provides a set of characters.  I need to query this table for any rows where either word1 or word2 contains any character from the set, while the other contains no character from the set.
           Only one result should be returned.  Can you help me write the database query?*/

        List<String> guesses = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for(Set<Character> set : dashboard.knownTogether) {

            for (char c : set) {
                switch (c) {
                    case ']':
                        sb.append("]]");   // escape closing bracket
                        break;
                    case '-':
                        sb.append("\\-");  // escape dash
                        break;
                    case '^':
                        sb.append("\\^");  // escape caret
                        break;
                    default:
                        sb.append(c);
                }
            }

            Connection conn = DriverManager.getConnection(urlToWatson, user, password);
            Statement statement = conn.createStatement(); {

                ResultSet resultSet = transactSQL.Query.select("DECLARE @chars VARCHAR(50) = '" + sb +"'; SELECT TOP 1 *\n" +
                        "FROM WordPairs\n" +
                        "WHERE\n" +
                        "(\n" +
                        "    word1 LIKE '%[' + @chars + ']%'   -- word1 contains at least one char from the set\n" +
                        "    AND word2 NOT LIKE '%[' + @chars + ']%'  -- word2 contains none\n" +
                        ")\n" +
                        "OR\n" +
                        "(\n" +
                        "    word2 LIKE '%[' + @chars + ']%'   -- word2 contains at least one char from the set\n" +
                        "    AND word1 NOT LIKE '%[' + @chars + ']%'  -- word1 contains none\n" +
                        ");\n");

                while(resultSet.next()) {
                    String w1 = resultSet.getString("word1");
                    String w2 = resultSet.getString("word2");
                    // Use the result
                    guesses.add(w1);
                    guesses.add(w2);
                }
            }
        }
        return guesses;
    }
}