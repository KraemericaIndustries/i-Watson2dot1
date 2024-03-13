package views;

import controllers.Connect;
import models.LetterGroup;
import models.Turn;
import models.Unknown;

import java.util.LinkedList;

public class Result {
    //  PRINT the result(s) of a given turn...
    public static void results(LetterGroup knownTogether, Unknown unknown, LinkedList<Turn> Turns) {

        System.out.println("*****************************************************************  RESULT # " + Report.reportNumber + " *****************************************************************************************");
        System.out.println("ANALYSIS:");
        System.out.println(" - Previous guesses for which there is data available: " + Turns.size() + "\n");
        System.out.println("ADVICE:");
        if(Turns.isEmpty()) {
            System.out.println(" - Make the first guess possible using the 5 most common letters possible");
            unknown.keySetToArray();
            System.out.print(" - Searching the database, I suggest guessing: ");  //  CONNECT to DB (to get guesses)
            Connect.watson("getWords", 1, (char)unknown.elements[0], (char)unknown.elements[1], (char)unknown.elements[2], (char)unknown.elements[3], (char)unknown.elements[4]);
        } else if (Turns.size() == 1) {
            System.out.println(" - With only " + Turns.size() + " previous play, very little can be learned.");
            System.out.println(" - I suggest making the first guess possible using the 2nd through 6th most common letters...");
            unknown.keySetToArray();
            System.out.print(" - Searching the database, I suggest guessing: ");  //  CONNECT to DB (to get guesses)
            Connect.watson("getWords", 1, (char)unknown.elements[1], (char)unknown.elements[2], (char)unknown.elements[3], (char)unknown.elements[4], (char)unknown.elements[5]);
        } else if(Turns.size() == 2) {
            System.out.println(" - Try to determine if "+ knownTogether.letters + " are both IN, or both OUT");
            knownTogether.keySetToArray();
            unknown.keySetToArray();
            System.out.println(" - I suggest trying to make a determination on the letter " + knownTogether.elements[0]);
            System.out.println(" - Searching the database, I suggest guessing: ");  //  CONNECT to DB (to get guesses)
            Connect.watson("getWords", 2, (char)unknown.elements[0], (char)unknown.elements[1], (char)unknown.elements[2], (char)unknown.elements[3], (char)unknown.elements[6]);
            System.out.println(" ~ Whichever contains the MOST COMMON LETTERS (as seen above)");
        }
//  SAMPLE recursive sql select...
        System.out.println("***********************************************************************************************************************************************************************");
    }
}
