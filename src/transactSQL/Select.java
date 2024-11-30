package transactSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static transactSQL.DatabaseConnection.*;

public class Select {

    public static void createPairsTable() throws SQLException {

        Connection conn = DriverManager.getConnection(url, user, password); Statement statement = conn.createStatement(); {
            System.out.println("Creating table of all word pairs in the database that differ by ONLY 1 LETTER...");
            try {
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

}