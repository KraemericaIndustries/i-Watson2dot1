package print;

import dataStructures.Dashboard;
import dataStructures.LetterScore;
import dataStructures.Turn;
import transactSQL.Connect;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static print.Messages.reportNumber;

public class object {

    public static void dashboard(Dashboard dashboard) {

        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "*****************************************************************  DASHBOARD REPORT # " + reportNumber + " ************************************************************************************"));

        //  PRINT the LinkedHashMaps...
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "Known IN:  " + dashboard.knownIn));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "Known OUT: " + dashboard.knownOut));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "Known Together: " + dashboard.knownTogether));
        unknownLetters(dashboard.unknownLetters);

        prettyPrintPreviousGuesses(dashboard.Turns);  //  PRINT all previous guesses

        //  GET counts from database table to furnish report, and drive logic...
        int numWords = (int) Connect.watson("getNumWordsInDB");
        dashboard.numWordPairs = (int) Connect.watson("countWordPairs");

        System.out.print(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "There are " + numWords + " words remaining in the database.\n"));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "There are " + dashboard.numWordPairs + " word pairs that differ by only 1 letter."));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "*******************************************************************************************************************************************************************************\n"));

    }

    public static void unknownLetters(List<LetterScore> unknownLetters) {

        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "------------------------------------------------------------------------UNKNOWN LETTERS AND FREQUENCY OF OCCURRENCE------------------------------------------------------"));
        System.out.print(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "Unknown:   "));
        for(int i = 0; i < 26; i++) {
            System.out.printf(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, " | %3s"), unknownLetters.get(i).letter);
        }
        System.out.println();

        System.out.print(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "Frequency: "));
        for(int i = 0; i < 26; i++) {
            System.out.printf(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, " | %3d"), unknownLetters.get(i).score);
        }
        System.out.println();
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------"));
    }

    //  PRETTY-PRINT the original and updated previous turns taken...
    public static void prettyPrintPreviousGuesses(LinkedList<Turn> Turns) {

        //  https://www.geeksforgeeks.org/how-to-print-all-keys-of-the-linkedhashmap-in-java/
        //  System.out.println("print.Messages.prettyPrintPreviousGuesses(): BEGIN");
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "Previous Guesses: "));

        for(Turn t : Turns) {
            System.out.println(t.guess + " = " + t.response + ".  We now know " + t.updatedResponse + " of [" + t.updatedGuess + "] are in your opponents word.");
        }
        System.out.println();
        //  System.out.println("print.Messages.prettyPrintPreviousGuesses(): END");
    }

    public static void knownTogether(Dashboard dashboard) {
        for(Set<Character> set : dashboard.knownTogether) {
            for(Character c: set) {
                System.out.print(c);
            }
        }
    }

}
