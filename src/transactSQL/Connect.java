package transactSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static transactSQL.DatabaseConnection.*;

public class Connect {

    //  Establish a connection to the watson DB, to remain open while recursive sql select statements identify and return words from the DB...
    public static String watson(String reason) {

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement ignored = conn.createStatement()) {

            switch(reason) {
                case "getWords":

//                    if() {
//
//                    }


                    if(dataStructures.Matrix.truthTable[6][1] == 0) {
                        return transactSQL.Query.getWords(1, 1, 2, 3, 4, 5);
                    } else {
                        return transactSQL.Query.getWords(1, 2, 3, 4, 5, 6);
                    }






                case "getNumWordsInDB":
                    return transactSQL.Query.getNumWordInDB();
                default:
                    System.out.println("Reason for connecting to the DB not recognized.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}