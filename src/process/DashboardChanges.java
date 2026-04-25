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

    // BEHAVIOR

    public static void updateDashboard(Dashboard dashboard) throws SQLException {

        boolean reprocessingNeeded = false;

//        System.out.println("Before: checkAgainstKnownInAndKnownOut:");
//        print.object.dashboard(dashboard);



        do {


            dashboard.knownIn.addAll(changesToKnownIn);  //  UPDATE Known IN (GOSPEL)
            dashboard.knownOut.addAll(changesToKnownOut);                         //  UPDATE Known OUT (GOSPEL)
            if(!dashboard.knownTogether.isEmpty()) checkAgainstKnownInAndKnownOut(dashboard);
//            System.out.println("AFTER checkAgainstKnownInAndKnownOut:");
//            print.object.dashboard(dashboard);



            if(!changesToKnownIn.isEmpty()) reprocessingNeeded = processChangesToKnownIn(dashboard);
//            System.out.println("AFTER processChangesToKnownIn:");
//            print.object.dashboard(dashboard);
            if(!changesToKnownOut.isEmpty()) reprocessingNeeded = processChangesToKnownOut(dashboard);
//            System.out.println("AFTER processChangesToKnownOut:");
//            print.object.dashboard(dashboard);
            System.out.println();
        } while (reprocessingNeeded);

        dashboard.knownIn.addAll(changesToKnownIn);
        dashboard.knownOut.addAll(changesToKnownOut);

        Delete.fromWordsTable(dashboard);        //  UPDATE Words_tbl (drop words without Known IN, drop words with Known OUT)

        changesToKnownIn.clear();
        changesToKnownOut.clear();

        Create.rebuildWatsonDB(dashboard);//  REGENERATE Words_tbl...
        regenerateWordPairsTable();  // rebuild WordPairs table
        dashboard.sortUnknownLettersByFrequencyDescending();//  SORT UNKNOWN letters remaining...

        dashboard.buildUnknownLettersList();
        dashboard.sortUnknownLettersByFrequencyDescending();
        removeDeterminedLettersFromAllTurns(dashboard);

        dashboard.knownIn.addAll(changesToKnownIn);
        dashboard.knownOut.addAll(changesToKnownOut);

        changesToKnownIn.clear();
        changesToKnownOut.clear();

        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_CYAN, "The dashboard has been changed to:"));
        print.object.dashboard(dashboard);                             //  PRINT the dashboard
        System.out.println();
    }

    public static boolean processChangesToKnownIn(Dashboard dashboard) {

        System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "           process.DashboardChanges.processChangesToKnownIn:"));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "           > Removing " + changesToKnownIn + " from any updated guesses that contain them, and DECREMENTING the updated response for EVERY letter removed..."));

        boolean changesMade =  false;

        for (Turn t : dashboard.Turns) {             //  FOR EVERY PREVIOUS TURN in the Turns collection

            if (Dashboard.containsAny(t.updatedGuess, changesToKnownIn)) {  //  IF the Turn contains ANY letter within changesToKnownOut

                StringBuilder sb = new StringBuilder(t.updatedGuess.length());
                int originalUpdatedResponse = t.updatedResponse;
                String originalUpdatedGuess = t.updatedGuess;

                for (char c : t.updatedGuess.toCharArray()) {
                    if (!changesToKnownIn.contains(c)) {
                        sb.append(c);
                    } else t.updatedResponse--;
                }
                t.updatedGuess = sb.toString();
                System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "           > Changed " + originalUpdatedGuess + ", " + originalUpdatedResponse + " to " + sb + ", " + t.updatedResponse));

                if(t.updatedGuess.length() == t.updatedResponse) {  //  CHECK for Turns where updatedGuess length is the same as updatedResponse (means all letters in updatedGuess are IN)
                    System.out.println(t.updatedGuess + t.updatedResponse);
                    System.out.println(" MEOW:  We now know that every letter in " + t.updatedGuess + " is now Known IN!  Updating the dashboard...");
                    for (char c : t.updatedGuess.toCharArray()) {  //  FOR EVERY CHARACTER in updatedGuess
                        changesToKnownIn.add(c);                   //  ADD the character to changesToKnownIn
                    }
                    changesMade = true;
                } else if((!t.updatedGuess.isEmpty()) && t.updatedResponse == 0) {  //  CHECK for Turns where updatedGuess IS NOT empty, and updatedResponse > 0 (means all letters in updatedGuess are OUT)
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "                 We now know that every letter in " + t.updatedGuess + " is Known OUT!  Updating the dashboard..."));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "                 Changing this: Pending changes to Known OUT: " + process.DashboardChanges.changesToKnownOut));
                    for (char c : t.updatedGuess.toCharArray()) {  //  FOR EVERY CHARACTER in updatedGuess
                        changesToKnownOut.add(c);                  //  ADD the character to changesToKnownOut
                    }
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "                 To this:       Pending changes to Known OUT: " + process.DashboardChanges.changesToKnownOut));
                    changesMade = true;
                } else changesMade = false;

            }




        }
//        changesToKnownIn.clear();
        return changesMade;
    }

    public static boolean processChangesToKnownOut(Dashboard dashboard) {

        boolean changesMade =  false;

        System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "           process.DashboardChanges.processChangesToKnownOut --> (No change to response necessary.)"));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "           > Removing " + changesToKnownOut + " from any updated guesses that contain them..."));

        for (Turn t : dashboard.Turns) {                                     //  FOR EVERY PREVIOUS TURN in the Turns collection
            if (Dashboard.containsAny(t.updatedGuess, changesToKnownOut)) {  //  IF the Turn contains ANY letter within changesToKnownOut
                Dashboard.removeChars(t.updatedGuess, changesToKnownOut);         //  REMOVE ALL letters in changesToKnownOut from the updatedGuess for that Turn
                //  CHECK for Turns where updatedGuess IS NOT empty, and updatedResponse > 0

                if(t.updatedGuess.length() == t.updatedResponse) {  //  CHECK for Turns where updatedGuess length is the same as updatedResponse (means all letters in updatedGuess are IN)
                    System.out.println(t.updatedGuess + t.updatedResponse);
                    System.out.println(" MEOW:  We now know that every letter in " + t.updatedGuess + " is Known IN!  Updating the dashboard...");
                    for (char c : t.updatedGuess.toCharArray()) {  //  FOR EVERY CHARACTER in updatedGuess
                        changesToKnownIn.add(c);                   //  ADD the character to changesToKnownIn
                    }
                    changesMade = true;
                } else if((!t.updatedGuess.isEmpty()) && t.updatedResponse == 0) {  //  CHECK for Turns where updatedGuess IS NOT empty, and updatedResponse > 0 (means all letters in updatedGuess are OUT)
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_RED, "                 We now know that every letter in " + t.updatedGuess + " is Known OUT!  Updating the dashboard..."));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "                 Changing this: Pending changes to Known OUT: " + process.DashboardChanges.changesToKnownOut));
                    for (char c : t.updatedGuess.toCharArray()) {  //  FOR EVERY CHARACTER in updatedGuess
                        changesToKnownOut.add(c);                  //  ADD the character to changesToKnownOut
                    }
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "                 To this:       Pending changes to Known OUT: " + process.DashboardChanges.changesToKnownOut));
                    changesMade = true;
                } else changesMade = false;
            }
        }
        return changesMade;
    }

    public static void checkAgainstKnownInAndKnownOut(Dashboard dashboard) {

        for (Set<Character> group : dashboard.knownTogether) {
            // Check if this group intersects with knownIn
            for (char c : group) {
                if (dashboard.knownIn.contains(c)) {
                    // If ANY character matches, merge the whole group
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "           checkAgainstKnownInAndKnownOut: Since Known IN " + dashboard.knownIn + " contains [" + c + "], we now know that ALL of " + group + " are Known IN!"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           Changing this: Pending changes to Known IN: " + process.DashboardChanges.changesToKnownIn));
                    changesToKnownIn.addAll(group);
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           Changing this:       Pending changes to Known IN: " + process.DashboardChanges.changesToKnownIn));
                    group.clear();
                    System.out.println();
                    break; // No need to keep checking this group
                }
            }
        }

        for (Set<Character> group : dashboard.knownTogether) {
            // Check if this group intersects with knownOut
            for (char c : group) {
                if (dashboard.knownOut.contains(c)) {
                    // If ANY character matches, merge the whole group
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "           checkAgainstKnownInAndKnownOut: Since " + dashboard.knownOut + " is Known OUT, we now know that ALL of " + group + " are Known OUT!"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           Changing this: Pending changes to Known OUT: " + process.DashboardChanges.changesToKnownOut));
                    changesToKnownOut.addAll(group);
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           To this:       Pending changes to Known OUT: " + process.DashboardChanges.changesToKnownOut));
                    group.clear();
                    break; // No need to keep checking this group
                }
            }
        }

    }
}