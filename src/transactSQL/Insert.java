package transactSQL;

import dataStructures.Unknown;
import dataStructures.LetterGroup2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Insert extends DatabaseConnection{
//    static int[] letterCounts = new int[26];
    static int counter;
    static String line;
    static char[] animationChars = new char[] {'|', '/', '-', '\\'};  //  class fields

    //  COUNT the occurrence of every letter in every word...
    private static void letterEnumerator(String word, LetterGroup2D frequency) {

        for(int i = 0; i < word.length(); i++) {
            switch (word.charAt(i)) {
                case 'A' -> frequency.array2D[1][0]++;
                case 'B' -> frequency.array2D[1][1]++;
                case 'C' -> frequency.array2D[1][2]++;
                case 'D' -> frequency.array2D[1][3]++;
                case 'E' -> frequency.array2D[1][4]++;
                case 'F' -> frequency.array2D[1][5]++;
                case 'G' -> frequency.array2D[1][6]++;
                case 'H' -> frequency.array2D[1][7]++;
                case 'I' -> frequency.array2D[1][8]++;
                case 'J' -> frequency.array2D[1][9]++;
                case 'K' -> frequency.array2D[1][10]++;
                case 'L' -> frequency.array2D[1][11]++;
                case 'M' -> frequency.array2D[1][12]++;
                case 'N' -> frequency.array2D[1][13]++;
                case 'O' -> frequency.array2D[1][14]++;
                case 'P' -> frequency.array2D[1][15]++;
                case 'Q' -> frequency.array2D[1][16]++;
                case 'R' -> frequency.array2D[1][17]++;
                case 'S' -> frequency.array2D[1][18]++;
                case 'T' -> frequency.array2D[1][19]++;
                case 'U' -> frequency.array2D[1][20]++;
                case 'V' -> frequency.array2D[1][21]++;
                case 'W' -> frequency.array2D[1][22]++;
                case 'X' -> frequency.array2D[1][23]++;
                case 'Y' -> frequency.array2D[1][24]++;
                case 'Z' -> frequency.array2D[1][25]++;
                default -> System.out.println("Unknown letter, or some other flaw");
            }
        }
    }  //  End-of-letterEnumerator()

    //  READ FiveLetterWords.txt into the 'watson' database Words.tbl...
    public static void loadKnownWords(Unknown unknown) {
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