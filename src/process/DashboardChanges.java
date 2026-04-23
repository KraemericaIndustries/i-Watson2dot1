package process;

import dataStructures.Dashboard;
import dataStructures.Turn;
import print.Colors;
import transactSQL.Create;
import transactSQL.Delete;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static assess.AllTurns.removeDeterminedLettersFromAllTurns;
import static dataStructures.Dashboard.regenerateWordPairsTable;

public class DashboardChanges {

    // STATE
    // The source of truth for  changes that need to be processed
    // (If determined characters are immediately added to GOSPEL,  and GOSPEL is the source of truth while making changes, changes become redundant and needlessly expensive)
    public static Set<Character> changesToKnownIn = new HashSet<>();
    public static Set<Character> changesToKnownOut = new HashSet<>();
    public static Set<Character> changesToKnownTogether = new HashSet<>();

    // BEHAVIOR

    public static void updateDashboard(Dashboard dashboard) throws SQLException {

        boolean reprocessingNeeded = false;

        do {
            if(!changesToKnownIn.isEmpty()) reprocessingNeeded = processChangesToKnownIn(dashboard);
            System.out.println("AFTER processChangesToKnownIn:");
            print.object.dashboard(dashboard);
            System.out.println();
            if(!changesToKnownOut.isEmpty()) reprocessingNeeded = processChangesToKnownOut(dashboard);
            System.out.println("AFTER processChangesToKnownOut:");
            print.object.dashboard(dashboard);
            System.out.println();
            if(!changesToKnownTogether.isEmpty()) processChangesToKnownIn(dashboard);
        } while (reprocessingNeeded);

        Delete.fromWordsTable(dashboard);        //  UPDATE Words_tbl (drop words without Known IN, drop words with Known OUT)
        Create.rebuildWatsonDB(dashboard);//  REGENERATE Words_tbl...
        regenerateWordPairsTable();  // rebuild WordPairs table
        dashboard.sortUnknownLettersByFrequencyDescending();//  SORT UNKNOWN letters remaining...

        dashboard.buildUnknownLettersList();
        dashboard.sortUnknownLettersByFrequencyDescending();
        removeDeterminedLettersFromAllTurns(dashboard);

        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "The dashboard has been changed to:"));
        System.out.println("AFTER updateDashboard:");
        print.object.dashboard(dashboard);                             //  PRINT the dashboard
        System.out.println();
    }

    public static boolean processChangesToKnownIn(Dashboard dashboard) {

        boolean changesMade =  false;

        dashboard.knownIn.addAll(changesToKnownIn);  //  UPDATE Known IN (GOSPEL)
        for (Turn t : dashboard.Turns) {             //  FOR EVERY PREVIOUS TURN in the Turns collection

            StringBuilder sb = new StringBuilder(t.updatedGuess.length());

            for (char c : t.updatedGuess.toCharArray()) {
                if (!changesToKnownIn.contains(c)) {
                    sb.append(c);
                } else t.updatedResponse--;
            }
            t.updatedGuess = sb.toString();

            System.out.println("Turn is now: " + sb + t.updatedResponse);
            System.out.println();

            if(t.updatedGuess.length() == t.updatedResponse) {  //  CHECK for Turns where updatedGuess length is the same as updatedResponse (means all letters in updatedGuess are IN)
                System.out.println(t.updatedGuess + t.updatedResponse);
                System.out.println(" MEOW:  We now know that every letter in " + t.updatedGuess + " is now Known IN!  Updating the dashboard...");
                for (char c : t.updatedGuess.toCharArray()) {  //  FOR EVERY CHARACTER in updatedGuess
                    changesToKnownIn.add(c);                   //  ADD the character to changesToKnownIn
                }
                changesMade = true;
            } else if((!t.updatedGuess.isEmpty()) && t.updatedResponse == 0) {  //  CHECK for Turns where updatedGuess IS NOT empty, and updatedResponse > 0 (means all letters in updatedGuess are OUT)
                System.out.println("We now know that every letter in " + t.updatedGuess + " is now Known OUT!  Updating the dashboard...");
                for (char c : t.updatedGuess.toCharArray()) {  //  FOR EVERY CHARACTER in updatedGuess
                    changesToKnownOut.add(c);                  //  ADD the character to changesToKnownOut
                }
                changesMade = true;
            } else changesMade = false;
        }
        changesToKnownIn.clear();
        return changesMade;
    }

    public static boolean processChangesToKnownOut(Dashboard dashboard) {

        boolean changesMade =  false;

        dashboard.knownOut.addAll(changesToKnownOut);                         //  UPDATE Known OUT (GOSPEL)
        for (Turn t : dashboard.Turns) {                                     //  FOR EVERY PREVIOUS TURN in the Turns collection
            if (Dashboard.containsAny(t.updatedGuess, changesToKnownOut)) {  //  IF the Turn contains ANY letter within changesToKnownIn
                System.out.println("Infinite loop?");
                System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, " > Checking if " + t.updatedGuess + " contains " + changesToKnownOut + " is IN!"));
                System.out.println(Dashboard.removeChars(t.updatedGuess, changesToKnownOut));         //  REMOVE ALL letters in changesToKnownOut from the updatedGuess for that Turn
                //  CHECK for Turns where updatedGuess IS NOT empty, and updatedResponse > 0

                if(t.updatedGuess.length() == t.updatedResponse) {  //  CHECK for Turns where updatedGuess length is the same as updatedResponse (means all letters in updatedGuess are IN)
                    System.out.println(t.updatedGuess + t.updatedResponse);
                    System.out.println(" MEOW:  We now know that every letter in " + t.updatedGuess + " is now Known IN!  Updating the dashboard...");
                    for (char c : t.updatedGuess.toCharArray()) {  //  FOR EVERY CHARACTER in updatedGuess
                        changesToKnownIn.add(c);                   //  ADD the character to changesToKnownIn
                    }
                    changesMade = true;
                } else if((!t.updatedGuess.isEmpty()) && t.updatedResponse == 0) {  //  CHECK for Turns where updatedGuess IS NOT empty, and updatedResponse > 0 (means all letters in updatedGuess are OUT)
                    System.out.println("We now know that every letter in " + t.updatedGuess + " is now Known OUT!  Updating the dashboard...");
                    for (char c : t.updatedGuess.toCharArray()) {  //  FOR EVERY CHARACTER in updatedGuess
                        changesToKnownOut.add(c);                  //  ADD the character to changesToKnownOut
                    }
                    print.object.dashboard(dashboard);
                    System.out.println();
                    changesMade = true;
                } else changesMade = false;
            }
        }
        System.out.println("BREAK!!!");
        System.out.println();

        changesToKnownOut.clear();
        return changesMade;
    }
}