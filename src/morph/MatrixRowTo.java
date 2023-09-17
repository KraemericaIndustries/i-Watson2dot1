package morph;

import dataStructures.GuessTable;

import java.sql.SQLException;

import static dataStructures.Matrix.truthTable;

public class MatrixRowTo {
    public static void commaDelimitedString(int row) {

        StringBuilder sb = new StringBuilder();

        for(int i = 1; i <27; i++) {
                if(truthTable[row][i] == 1) sb.append(((char) (i + 64))).append(", ");
        }
        System.out.println(sb.substring(0, sb.length()-2));
    }
    public static void nextGuess(int rowOne, int rowTwo) throws SQLException {

        char firstChar = 0;
        char otherChar = 0;
        char secondChar = 0;
        char thirdChar = 0;
        char fourthChar = 0;
        char ommittedChar = 0;
        char fifthChar = 0;

        StringBuilder sb = new StringBuilder();
        String letters;
        //  Initialize to null

        //  First, retrieve the changed letters and place them in a String{}...
        for (int c = 1; c < 27; c++) {
            if(truthTable[3][c] == 1) sb.append((char)(c+64));
            if(sb.length()>1) break;
        }
        letters = sb.toString();
        System.out.println("Changed Letters: " + letters);

        //  Next, determine which of the changed letters occurs more frequently against the mostCommonLetters String{} (to avoid hitting the DB for this...)
        sb.delete(0, sb.length());
        for (char c : print.Messages.mostCommonLetters.toCharArray()) {
            if (c == letters.charAt(0)) {
                firstChar = c;
                otherChar = letters.charAt(1);
                break;
            } else {
                firstChar = letters.charAt(1);
                otherChar = letters.charAt(0);
            }
        }
        System.out.println("1st, other: " + firstChar + otherChar);

        //  Next, AND the two guesses to a String{}, then determine which 3 of the 4 occur most frequently...
        for(int c = 1; c < 27; c++) {
            if((truthTable[rowOne][c] & truthTable[rowTwo][c]) ==1) sb.append((char)(c+64));
        }
        letters = sb.toString();
        System.out.println("ANDed Guesses: " + letters);

        //  Now, identify the three most common out of the four in the StringBuilder{}...
        for(char c : print.Messages.mostCommonLetters.toCharArray()) {

            final boolean b = c == letters.charAt(0) || c == letters.charAt(1) || c == letters.charAt(2) || c == letters.charAt(3);

            if(secondChar == 0 && b) secondChar = c;
            else if (thirdChar == 0 && b) thirdChar = c;
            else if (fourthChar == 0 && b) fourthChar = c;
            else if (ommittedChar == 0 && b) ommittedChar = c;
        }
        System.out.println("2nd, 3rd, 4th, omitted: " + secondChar + ", " + thirdChar + ", " + fourthChar + ", " + ommittedChar);

        //  Finally, select the next most common unknown letter (that is not letters 1 through 4) as the fifth letter...
//        for (char c : print.Messages.mostCommonLetters.toCharArray()) {
//            if(c != firstChar && c != secondChar && c != thirdChar && c != fourthChar && c != ommittedChar && c != otherChar) fifthChar = c;
//            if (fifthChar != 0) break;
//        }


        //  This usage drives an implementation change dataStructures.GuessTable.getNextMostCommon() to accept a string parameter as that method already has the functionality needed...
        int c = 0;

        char[] mostCommonChars =print.Messages.mostCommonLetters.toCharArray();

        sb.delete(0, sb.length());
        sb.append(firstChar).append(secondChar).append(thirdChar).append(fourthChar);

        for (int i = 0; i < mostCommonChars.length; i++) {
            if(mostCommonChars[i] != firstChar && mostCommonChars[i] != secondChar && mostCommonChars[i] != thirdChar && mostCommonChars[i] != fourthChar && mostCommonChars[i] != ommittedChar && mostCommonChars[i] != otherChar) {
                c = i;
                if(c != 0) break;
            }
        }
        dataStructures.GuessTable.nextMostCommon.clear();
        dataStructures.GuessTable.getNextMostCommon(sb.toString(), c);
        System.out.println(GuessTable.nextMostCommon);

        //  Now, query the DB in search of a word made up of these letters...

    }
}