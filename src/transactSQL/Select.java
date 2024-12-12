package transactSQL;

import java.sql.*;

import static transactSQL.DatabaseConnection.*;

public class Select {

    public static int countWordPairs() throws SQLException {

        int numPairs = 0;

        ResultSet resultSet = transactSQL.Query.select("select count (*) from WordPairs");

        while(resultSet.next()) {
            numPairs = ((Number) resultSet.getObject(1)).intValue();
        }
        return numPairs;
    }

    public static void wordPairsDifferByLetter(char mostCommonLetter) throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {

            ResultSet resultSet = transactSQL.Query.select("select TOP (6) word1, word2\n" +
                    "from WordPairs\n" +
                    "where word1 not like '%" + mostCommonLetter + "%'\n" +
                    "and word2 like '%" + mostCommonLetter + "%'");

            while(resultSet.next()) {
                System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2));
            }
        }
    }

    public static void wordsContainingTwoLetters(char knownTogether1, char knownTogether2) throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {

            ResultSet resultSet = transactSQL.Query.select("select TOP (6) word1, word2\n" +
                    "from WordPairs\n" +
                    "where word1 like '%" + knownTogether1 + "%'\n" +
                    "and word1 like '%" + knownTogether2 + "%'");

            while(resultSet.next()) {
                System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2));
            }
        }
    }
    public static void lastNumWordPairs() throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {

            ResultSet resultSet = transactSQL.Query.select("select * from WordPairs");

            while(resultSet.next()) {
                System.out.println(resultSet.getString(1) + ", " + resultSet.getString(2));
            }
        }
    }
}