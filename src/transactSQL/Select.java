package transactSQL;

import java.sql.*;

import static transactSQL.DatabaseConnection.*;

public class Select {

    public static void createPairsTable() throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {
        //  DEBUG: System.out.println("Creating table of all word pairs in the database that differ by ONLY 1 LETTER...");
            try {
                //  This query was generated with assistance from Microsoft Copilot:
                statement.executeUpdate("SELECT w1.word AS word1, w2.word AS word2 \n" +
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
                        ");"
                );
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void countWordPairs() throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {

            ResultSet resultSet = transactSQL.Query.select("select count (*) from WordPairs");

            while(resultSet.next()) {
                System.out.println(" > Number of word pairs in the database that differ by only 1 letter: " + ((Number) resultSet.getObject(1)).intValue() + "\n");
            }
        }
    }

    public static int returnCountWordPairs() throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {

            ResultSet resultSet = transactSQL.Query.select("select count (*) from WordPairs");

            int size = 0;
            try {
                resultSet.last();
                size = resultSet.getRow();
                resultSet.beforeFirst();
            }
            catch(Exception ex) {
                return 0;
            }
            return size;
        }
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