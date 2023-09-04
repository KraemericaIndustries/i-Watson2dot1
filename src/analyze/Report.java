package analyze;

import dataStructures.GuessTable;

import java.sql.SQLException;

public class Report {
    //  ANALYZE any previous turns taken to be able to suggest strategies...
    public static void previousGuesses() throws SQLException {
        if (dataStructures.Matrix.truthTable[5][0] == 0) {  //  IF no previous guess exists
            System.out.println("There are no previous guesses.  I suggest making a guess using the 5 MOST COMMON letters:");
            for(String word : GuessTable.fiveMostCommon) {
                System.out.print(word + ", ");
            }
        System.out.println();
        }

        if (dataStructures.Matrix.truthTable[5][0] == 1 && dataStructures.Matrix.truthTable[6][0] == 0) {  //  IF only 1 previous guess exists
            System.out.println("I suggest guessing a word using using the next most letters:");
            for(String word : GuessTable.nextMostCommon) {
                System.out.print(word + ", ");
            }
            System.out.println();
        }
    }
}