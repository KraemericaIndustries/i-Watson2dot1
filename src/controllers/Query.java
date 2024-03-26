package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query extends DatabaseConnection {
    static File file = new File("test.txt");

    //  QUERY the DB for numWords with the first through fifth MOST COMMON letters, to use as a guess...
    /*
    https://stackoverflow.com/questions/77309450/sql-query-to-return-only-the-most-exclusive-pattern-matches-for-varchar-data-t
    */
    public static void getWords(int numWords, char first, char second, char third, char fourth, char fifth) throws SQLException {

        System.out.println("+++ Query.getWords +++");

        ResultSet resultSet = Query.select(
"SELECT TOP (" + numWords + ") Word " +
          "FROM Words_tbl YT " +
          "CROSS JOIN (VALUES('" +
          first + "'),('" +
          second + "'),('" +
          third + "'),('" +
          fourth + "'),('" +
          fifth + "'))L(Letter) " +
          "GROUP BY YT.Word " +
          "ORDER BY COUNT(CASE WHEN YT.Word LIKE '%' + L.Letter + '%' THEN 1 END) DESC");

        while(resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
        System.out.println("--- Query.getWords ---");
    }

    public static void getNumWordInDB() throws SQLException {

        System.out.println("+++ Query.getNumWordInDB +++");

        ResultSet resultSet = Query.select("select count (*) from Words_tbl");

        while(resultSet.next()) {
            System.out.print(((Number) resultSet.getObject(1)).intValue());
        }

//        resultSet.next();
//        System.out.print(((Number) resultSet.getObject(1)).intValue());
//        }
//        return String.valueOf(numWords);
        System.out.println("--- Query.getNumWordInDB ---");
    }

    //  wordsFromDB() ToDo: This method needs to DELETE the "test.txt" file from the filesystem prior to each run.  A non-hardcoded absolute path to the file would be preferable
    //  wordsFromDB() ToDo: Refactor this method.  Resultset parameter to write.File.XYZ to improve structure
    public static void wordsFromDB() {

        System.out.println("+++ Query.wordsFromDB +++");

        System.out.println("Retrieving all remaining words from the 'watson' database...");
        int counter = 0;
        try {
            conn = DriverManager.getConnection(url, user, password);  		       //  Establish Connection Object
            statement = conn.createStatement();  								   //  Create a SQL statement object to send to the database
            resultSet = statement.executeQuery("select * from Words_tbl");     //  Execute the statement object

                try(BufferedWriter br = new BufferedWriter(new FileWriter(file))) {

                    while(resultSet.next()) {
                        br.write(resultSet.getString("word"));
                        br.newLine();
                        counter++;
                    }

                } catch (IOException e) {
                    System.out.println("Unable to read file: " + file);
                }
        } catch (SQLException e) {
//            e.printStackTrace();
        }
        System.out.println(" > " + counter + " words have been retrieved from the 'watson' database.");
        System.out.println("--- Query.wordsFromDB ---");
    }

    //  Generic method that takes a String{} of a sql query, and returns a result...
    public static ResultSet select(String selectQuery) {

        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(selectQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}