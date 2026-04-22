package compare;

import classify.Classification;
import dataStructures.Dashboard;
import print.Colors;

import java.sql.SQLException;

public class MostRecentTurn {

    public static boolean againstAllOthers(Dashboard dashboard) throws SQLException {

        //        removeDeterminedLettersFromAllTurns(dashboard);
        int comparisonNumber = 1;

        //  ToDo: I think I can re-factor this to do a better job.  Lets add a section that does only the most recent turn against all turns (color = GREEN), and a boolean to track changes.
        boolean changesMade = false;

        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "********************************************************************************  COMPARING MOST RECENT TURN AGAINST ALL OTHERS  ***********************************************************************************"));
        for(int i = 0; i < dashboard.Turns.size() - 1; i++) {      //  FOR every turn in 'Turns' (up until the SECOND LAST Turn in 'Turns')
            System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "COMPARISON #" + comparisonNumber + ".  Now comparing turn #" + (i + 1) + " with turn #" + (dashboard.Turns.size()) + ":"));
            assess.AllTurns.prettyPrintLinkedHashMap(dashboard.Turns, i, dashboard.Turns.size() - 1);

            //  CLASSIFICATION:
            System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    CLASSIFICATION:"));
            Classification classification;
            classification = new Classification(dashboard.Turns.get(i).updatedResponse, dashboard.Turns.get(dashboard.Turns.size() - 1).updatedResponse, dashboard.Turns.get(i).updatedGuess, dashboard.Turns.get(dashboard.Turns.size() - 1).updatedGuess);
            classification.printClassification();
//  ToDo:  When does it make sense to check for updatedResponse = 0?  Make sure this is handled correctly...
//  ToDo:  As soon as changes are made, updates should happen, and the exercise (present turn v. allTurns) should be restarted.  How do I do this?  Make sure this is handled correctly...
            // Now that the selected pair of turns has been CLASSIFIED, Identify Findings, make Determinations, and take ACTION...
//            if(!classification.updatedGuessesSame) {
                String assessment = assess.Classification.assessClassification(classification);
                System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    FINDINGS:"));

                // Now that we have an assessment, take action based on the assessment...
                switch (assessment) {
                    case "Updated guesses are the same":
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > This pair of guesses each contain the same letters"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    Determinations:"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > No determinations are possible"));
                        break;

                    case "    One letter changed, delta is 1":
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > Only 1 letter has changed"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > The (updated) responses are different by 1"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    Determinations:"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > BAA We now know " + classification.onlyInFirst + " is IN, and " + classification.onlyInSecond + " is OUT!"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    ACTIONS:"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > SHEEP Adding " + classification.onlyInFirst + " to Known IN, and " + classification.onlyInSecond + " to Known OUT!\n"));
                        process.DashboardChanges.changesToKnownIn.addAll(classification.onlyInFirst);
                        process.DashboardChanges.changesToKnownOut.addAll(classification.onlyInSecond);
                        //  ToDo This invocation is where I left off.  Finish this!!!
//                        assess.AllTurns.updateDashboard(dashboard);
                        changesMade = true;
                        break;

                    case "One letter changed, delta is -1":
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > Only 1 letter has changed"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > The (updated) responses are different by 1"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    Determinations:"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "     > SIS We now know " + classification.onlyInFirst + " is OUT, and " + classification.onlyInSecond + " is IN!"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    ACTIONS:"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "     > BOOM Adding " + classification.onlyInSecond + " to Known IN, and " + classification.onlyInFirst + " to Known OUT!\n"));
                        process.DashboardChanges.changesToKnownIn.addAll(classification.onlyInSecond);
                        process.DashboardChanges.changesToKnownOut.addAll(classification.onlyInFirst);

                        process.DashboardChanges.updateDashboard(dashboard);

                        //  ToDo This invocation is where I left off.  Finish this!!!
                        //  ToDo PRIORITY ONE:  Turns are NOT updating!!!
                        //  ToDo PRIORITY TWO:  Need to check kT... if a letter matches, the REST OF kT is known!!!
//                        assess.AllTurns.updateDashboard(dashboard);
                        dashboard.buildUnknownLettersList();
                        dashboard.sortUnknownLettersByFrequencyDescending();
                        assess.AllTurns.removeDeterminedLettersFromAllTurns(dashboard);
                        print.object.dashboard(dashboard);                               //  PRINT the dashboard
                        changesMade = true;
                        break;

                    case "One letter changed, delta is 0":
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > Only 1 letter has changed"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > The (updated) response has NOT changed."));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > We now know that " + classification.onlyInFirst + " and " + classification.onlyInSecond + " are either BOTH IN, or BOTH OUT (but can't be certain which is the case)."));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    ACTIONS:"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "     > Adding " + classification.onlyInFirst + " and " + classification.onlyInSecond + " to a set of letters that are Known TOGETHER."));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "     > Changing this: Known Together: " + dashboard.knownTogether));
                        classification.onlyInFirst.addAll(classification.onlyInSecond);  //  Since these are now known to be together, ADD the second set to the first
                        dashboard.mergeSetToKnownTogether(classification.onlyInFirst);   //  MERGE the first set to the list of all sets known to be together
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "     > To this:       Known Together: " + dashboard.knownTogether));
                        break;

                    // ToDo: Add cases for onlyInFirst or onlyInSecond = 0 (some determination is possible based on commonToBoth and only in other)...  What is the logic here??

                    // more cases...

                    default:
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > More than 1 letter is different between these 2 turns"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    Determinations:"));
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > Since more than 1 letter has changed, few determinations are possible.  Continuing to the next pair...\n"));
                        break;
                }
//            }
            comparisonNumber++;
        }

        //  Todo: If a change is made, process the change IMMEDIATELY and set the boolean to TRUE
        //  Todo: If the boolean is true, then compareAllTurnsAgainstEachOther (color = BG_GREEN)
        //  Todo: PROCESS and changes IMMEDIATELY!
        //  Todo: When should we check for zeros?  When should we check for removing determined letters?

        return changesMade;
    }
}