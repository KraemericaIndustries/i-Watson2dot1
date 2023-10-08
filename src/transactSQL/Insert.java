package transactSQL;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static dataStructures.Matrix.truthTable;

public class Insert extends DatabaseConnection{
//    static int[] letterCounts = new int[26];
    static int counter;
    static String line;
    static char[] animationChars = new char[] {'|', '/', '-', '\\'};  //  class fields

    //  COUNT the occurrence of every letter in every word...
    private static void letterEnumerator(String word) {

        for(int i = 0; i < word.length(); i++) {
            switch (word.charAt(i)) {
                case 'A' -> truthTable[5][1]++;
                case 'B' -> truthTable[5][2]++;
                case 'C' -> truthTable[5][3]++;
                case 'D' -> truthTable[5][4]++;
                case 'E' -> truthTable[5][5]++;
                case 'F' -> truthTable[5][6]++;
                case 'G' -> truthTable[5][7]++;
                case 'H' -> truthTable[5][8]++;
                case 'I' -> truthTable[5][9]++;
                case 'J' -> truthTable[5][10]++;
                case 'K' -> truthTable[5][11]++;
                case 'L' -> truthTable[5][12]++;
                case 'M' -> truthTable[5][13]++;
                case 'N' -> truthTable[5][14]++;
                case 'O' -> truthTable[5][15]++;
                case 'P' -> truthTable[5][16]++;
                case 'Q' -> truthTable[5][17]++;
                case 'R' -> truthTable[5][18]++;
                case 'S' -> truthTable[5][19]++;
                case 'T' -> truthTable[5][20]++;
                case 'U' -> truthTable[5][21]++;
                case 'V' -> truthTable[5][22]++;
                case 'W' -> truthTable[5][23]++;
                case 'X' -> truthTable[5][24]++;
                case 'Y' -> truthTable[5][25]++;
                case 'Z' -> truthTable[5][26]++;
                default -> System.out.println("Unknown letter, or some other flaw");
            }
        }
    }  //  End-of-letterEnumerator()

    //  READ FiveLetterWords.txt into the 'watson' database Words.tbl...
    public static void loadKnownWords() {
        System.out.println("Loading known words into the 'watson' database...");
        try {
            File file = new File("FiveLetterWords.txt");
            Scanner input = new Scanner(file);

            while (input.hasNextLine()) {
                line = (input.nextLine().toUpperCase());
                letterEnumerator(line.toUpperCase());  //  INVOKE letterEnumerator to count the occurrence of each letter in each word in the FiveLetterWords.txt file
                try {
                    Connection conn = DriverManager.getConnection(url, user, password);                               //  Establish Connection Object
                    Statement statement = conn.createStatement();                                                     //  Create a SQL statement object to send to the database
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
        System.out.println(" > Number of words successfully added to the Database: " + counter);
        System.out.println();
//  DEBUG:  Show letter counts:
//		for(int j = 0; j <= letterCounts.length - 1; j++) {
//			System.out.println("Letter count @ index " + j + ":" + letterCounts[j]);
//		}
//        Matrix.seedFrequency(letterCounts);

//        System.out.println("Seeding letter counts into the 'watson' database...");
//        try {
//            conn = DriverManager.getConnection(url, user, password);  //  Establish Connection Object
//            statement = conn.createStatement();                       //  Create a SQL statement object to send to the database
//            //  WORKS!
//
//            for(int i = 0; i < letterCounts.length; i++) {
//                statement.addBatch("insert into letterCounts_tbl values ('" + (char)(i+65) + "', " + letterCounts[i] + ");");
//            }
//
//            statement.executeBatch();
//
//        } catch (SQLException e) {
////					  e.printStackTrace();
//        }
//        System.out.println(" > Seeding letter counts into the 'watson' database was Successful");
//        System.out.println(" > 'watson' database populated!");
//        System.out.println();
    }
}