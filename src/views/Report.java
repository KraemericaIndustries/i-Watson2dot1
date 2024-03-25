package views;

import controllers.AllTurns;
import controllers.Connect;
import models.LetterGroup;
import models.Turn;
import models.Unknown;

import java.util.LinkedList;
import java.util.Map;

public class Report {

    public static int reportNumber = 1;

    //  PRINT a report...
    public static void report(LetterGroup knownIn, LetterGroup knownOut, LetterGroup knownTogether, LinkedList<Turn> Turns, Unknown unknown) {
        System.out.println("*****************************************************************  REPORT # " + reportNumber + " *****************************************************************************************");

        if(Turns.size() >= 2) AllTurns.compareAllTurns(Turns, knownTogether, knownIn, knownOut, unknown);

        printLetterGroups(knownIn, knownOut, knownTogether);

        //  PRINT all previous guesses...
        prettyPrintPreviousGuesses(Turns);

        System.out.print("There are ");
        Connect.watson("getNumWordsInDB", 1, 'T', 'O', 'K', 'E', 'N');
        System.out.println(" words remaining in the database.");
        System.out.println("***********************************************************************************************************************************************************************\n");
    }

    private static void printLetterGroups(LetterGroup knownIn, LetterGroup knownOut, LetterGroup knownTogether) {
        //  PRINT the LinkedHashMaps...
        System.out.println("Known IN: " + knownIn.letters);
        System.out.println("Known OUT: " + knownOut.letters);
        System.out.println("Known TOGETHER: " + knownTogether.letters);
        System.out.println("Unknown: " + Unknown.letters + "\n");
    }

    private static void prettyPrintPreviousGuesses(LinkedList<Turn> Turns) {
        //  https://www.geeksforgeeks.org/how-to-print-all-keys-of-the-linkedhashmap-in-java/
        System.out.println("Previous Guesses: ");

        for(Turn t : Turns) {
            StringBuilder test = new StringBuilder();
            test.append(t.guess).append(" = ").append(t.response).append(".  We now know ").append(t.updatedResponse).append(" of [");
            for (Map.Entry<Character, Integer> ite : t.turn.entrySet()) {
                test.append(ite.getKey()).append(", ");
            }

            test.setLength(test.length() - 2);
            test.append("] are in your opponents word.");
//            System.out.println(test + "\n");
            System.out.println(test);
        }
        System.out.println();
    }
}