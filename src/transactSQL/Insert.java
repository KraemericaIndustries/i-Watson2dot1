package transactSQL;

import dataStructures.Unknown;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Insert extends DatabaseConnection{
    public static int wordCount;
    static String line;
    static char[] animationChars = new char[] {'|', '/', '-', '\\'};  //  class fields

    //  READ FiveLetterWords.txt into the 'watson' database Words.tbl...
    public static void loadKnownWords() {
        System.out.println("Loading known words into the 'watson' database...");
        try {
            File file = new File("FiveLetterWords.txt");
            Scanner input = new Scanner(file);

            while (input.hasNextLine()) {
                line = (input.nextLine().toUpperCase());
                Unknown.letterEnumerator(line.toUpperCase());  //  INVOKE letterEnumerator to count the occurrence of each letter in each word in the FiveLetterWords.txt file
                try {
                    Connection conn = DriverManager.getConnection(url, user, password);                               //  Establish Connection Object
                    Statement statement = conn.createStatement();                                                     //  Create a SQL statement object to send to the database
                    wordCount = wordCount + statement.executeUpdate("insert into Words_tbl values('" + line + "')");  //  Execute the statement object
                } catch (SQLException e) {
					  e.printStackTrace();
                }
                System.out.print("Words added: " + wordCount + " " + animationChars[wordCount % 4] + '\r');  //  println
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
			e.printStackTrace();
        }
        System.out.println(" > Number of words successfully added to the Database: " + wordCount);
    }

    public static void reloadKnownWords() {
        System.out.println("Reloading known words into the 'watson' database...");
        wordCount = 0;
        Unknown.letters.clear();
        try {
            File file = new File("test.txt");
            Scanner input = new Scanner(file);

            while (input.hasNextLine()) {
                line = (input.nextLine().toUpperCase());
                Unknown.letterEnumerator(line.toUpperCase());  //  INVOKE letterEnumerator to count the occurrence of each letter in each word in the FiveLetterWords.txt file
                try {
                    Connection conn = DriverManager.getConnection(url, user, password);                               //  Establish Connection Object
                    Statement statement = conn.createStatement();                                                     //  Create a SQL statement object to send to the database
                    wordCount = wordCount + statement.executeUpdate("insert into Words_tbl values('" + line + "')");  //  Execute the statement object
                } catch (SQLException e) {
					  e.printStackTrace();
                }
                System.out.print("Words added: " + wordCount + " " + animationChars[wordCount % 4] + '\r');  //  println
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
			e.printStackTrace();
        }
        System.out.println(" > Number of words successfully added to the Database: " + wordCount + "\n");
    }
}