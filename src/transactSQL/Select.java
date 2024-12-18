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
}