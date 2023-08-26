package transactSQL;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Insert extends DatabaseConnection{
    static int[] letterCounts = new int[26];
    static int counter;
    static String line;
    static char[] animationChars = new char[] {'|', '/', '-', '\\'};  //  class fields
    private static void letterEnumerator(String word) {
        //  COUNT the occurrence of every letter in every word...
        for(int i = 0; i < word.length(); i++) {
            switch (word.charAt(i)) {
                case 'A' -> letterCounts[0]++;
                case 'B' -> letterCounts[1]++;
                case 'C' -> letterCounts[2]++;
                case 'D' -> letterCounts[3]++;
                case 'E' -> letterCounts[4]++;
                case 'F' -> letterCounts[5]++;
                case 'G' -> letterCounts[6]++;
                case 'H' -> letterCounts[7]++;
                case 'I' -> letterCounts[8]++;
                case 'J' -> letterCounts[9]++;
                case 'K' -> letterCounts[10]++;
                case 'L' -> letterCounts[11]++;
                case 'M' -> letterCounts[12]++;
                case 'N' -> letterCounts[13]++;
                case 'O' -> letterCounts[14]++;
                case 'P' -> letterCounts[15]++;
                case 'Q' -> letterCounts[16]++;
                case 'R' -> letterCounts[17]++;
                case 'S' -> letterCounts[18]++;
                case 'T' -> letterCounts[19]++;
                case 'U' -> letterCounts[20]++;
                case 'V' -> letterCounts[21]++;
                case 'W' -> letterCounts[22]++;
                case 'X' -> letterCounts[23]++;
                case 'Y' -> letterCounts[24]++;
                case 'Z' -> letterCounts[25]++;
                default -> System.out.println("Unknown letter, or some other flaw");
            }
        }
    }  //  End-of-letterEnumerator()
    public static void loadKnownWords() {
        //  READ FiveLetterWords.txt into the 'watson' database Words.tbl...
        System.out.println("Loading known words into the 'watson' database...");
        try {
            File file = new File("FiveLetterWords.txt");
            Scanner input = new Scanner(file);
            System.out.println("Seeding known 5 letter words into the 'watson' database...");
            while (input.hasNextLine()) {
                line = (input.nextLine().toUpperCase());
                letterEnumerator(line.toUpperCase());  //  INVOKE letterEnumerator to count the occurrence of each letter in each word in the FiveLetterWords.txt file
                try {
                    Connection conn = DriverManager.getConnection(url, user, password);  //  Establish Connection Object
                    Statement statement = conn.createStatement();  						 //  Create a SQL statement object to send to the database
                    counter = counter + statement.executeUpdate("insert into Words_tbl values('" + line + "')");  //  Execute the statement object
                } catch (SQLException e) {
//					  e.printStackTrace();
                }
                System.out.print("Words added: " + counter + " " + animationChars[counter % 4] + '\r');  //  println
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
//			e.printStackTrace();
        }
        System.out.println("Number of words successfully added to the Database: " + counter);
        System.out.println();
//  DEBUG:  Show letter counts:
//		for(int j = 0; j <= letterCounts.length - 1; j++) {
//			System.out.println("Letter count @ index " + j + ":" + letterCounts[j]);
//		}
//        Matrix.seedFrequency(letterCounts);

        System.out.println("Seeding letter counts into the 'watson' database...");
        try {
            conn = DriverManager.getConnection(url, user, password);  //  Establish Connection Object
            statement = conn.createStatement();                       //  Create a SQL statement object to send to the database
            //  WORKS!

            for(int i = 0; i < letterCounts.length; i++) {
                statement.addBatch("insert into letterCounts_tbl values ('" + (char)(i+65) + "', " + letterCounts[i] + ");");
            }

            statement.executeBatch();

        } catch (SQLException e) {
//					  e.printStackTrace();
        }
        System.out.println("'watson' database created!");
        System.out.println();


    }
}
