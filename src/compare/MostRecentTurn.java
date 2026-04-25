package compare;

import classify.Classification;
import dataStructures.Dashboard;
import print.Colors;

import java.sql.SQLException;

public class MostRecentTurn {

    public static boolean againstAllOthers(Dashboard dashboard) throws SQLException {

        int comparisonNumber = 1;

        boolean changesMade = false;

        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "********************************************************************************  COMPARING MOST RECENT TURN AGAINST ALL OTHERS  ***********************************************************************************"));
        System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "compare.MostRecentTurn.againstAllOthers()"));
        for(int i = 0; i < dashboard.Turns.size() - 1; i++) {      //  FOR every turn in 'Turns' (up until the SECOND LAST Turn in 'Turns')
            System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "COMPARISON #" + comparisonNumber + ".  Now comparing turn #" + (i + 1) + " with turn #" + (dashboard.Turns.size()) + ":"));
            assess.AllTurns.prettyPrintLinkedHashMap(dashboard.Turns, i, dashboard.Turns.size() - 1);

            //  CLASSIFICATION:
            System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    CLASSIFICATION:"));
            Classification classification;
            classification = new Classification(dashboard.Turns.get(i).updatedResponse, dashboard.Turns.get(dashboard.Turns.size() - 1).updatedResponse, dashboard.Turns.get(i).updatedGuess, dashboard.Turns.get(dashboard.Turns.size() - 1).updatedGuess);
            classification.printClassification();
            // Now that the selected pair of turns has been CLASSIFIED, Identify Findings, make Determinations, and take ACTION...
            String assessment = assess.Classification.assessClassification(classification);
            System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    FINDINGS:"));

            // Now that we have an assessment, take action based on the assessment...
            switch (assessment) {
                case "Updated guesses are the same":
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    compare.MostRecentTurn.againstAllOthers(), case Updated guesses are the same"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > This pair of guesses each contain the same letters"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    Determinations:"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > No determinations are possible"));
                    break;

                case "    One letter changed, delta is 1":
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    compare.MostRecentTurn.againstAllOthers(), case One letter changed, delta is 1"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > Only 1 letter has changed"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > The (updated) responses are different by 1"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    Determinations:"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > We now know " + classification.onlyInFirst + " is IN, and " + classification.onlyInSecond + " is OUT!"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    ACTIONS:"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > Adding " + classification.onlyInFirst + " to Known IN, and " + classification.onlyInSecond + " to Known OUT!"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "         > Changing this:"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           Pending changes to Known IN: " + process.DashboardChanges.changesToKnownIn));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           Pending changes to Known OUT: " + process.DashboardChanges.changesToKnownOut));
                    process.DashboardChanges.changesToKnownIn.addAll(classification.onlyInSecond);
                    process.DashboardChanges.changesToKnownOut.addAll(classification.onlyInFirst);
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "         > To this:"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           Pending changes to Known IN: " + process.DashboardChanges.changesToKnownIn));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           Pending changes to Known OUT: " + process.DashboardChanges.changesToKnownOut));

                    process.DashboardChanges.updateDashboard(dashboard);

                    dashboard.buildUnknownLettersList();
                    dashboard.sortUnknownLettersByFrequencyDescending();
//                    assess.AllTurns.removeDeterminedLettersFromAllTurns(dashboard);
                    print.object.dashboard(dashboard);                               //  PRINT the dashboard
                    changesMade = true;
                    break;

                case "One letter changed, delta is -1":
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    compare.MostRecentTurn.againstAllOthers(), case One letter changed, delta is -1"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > Only 1 letter has changed"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > The (updated) responses are different by 1"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    Determinations:"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "         > We now know " + classification.onlyInFirst + " is Known OUT, and " + classification.onlyInSecond + " is Known IN!"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    ACTIONS:"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "         > Adding " + classification.onlyInSecond + " to changesToKnownIn, and " + classification.onlyInFirst + " to changesToKnownOut!"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "         > Changing this:"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           Pending changes to Known IN: " + process.DashboardChanges.changesToKnownIn));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           Pending changes to Known OUT: " + process.DashboardChanges.changesToKnownOut));
                    process.DashboardChanges.changesToKnownIn.addAll(classification.onlyInSecond);
                    process.DashboardChanges.changesToKnownOut.addAll(classification.onlyInFirst);
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.RED, "         > To this:"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           Pending changes to Known IN: " + process.DashboardChanges.changesToKnownIn));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE, "           Pending changes to Known OUT: " + process.DashboardChanges.changesToKnownOut));

                    process.DashboardChanges.updateDashboard(dashboard);

                    dashboard.buildUnknownLettersList();
                    dashboard.sortUnknownLettersByFrequencyDescending();
                    print.object.dashboard(dashboard);
                    changesMade = true;
                    break;

                case "One letter changed, delta is 0":
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "compare.MostRecentTurn.againstAllOthers(), case One letter changed, delta is 0"));
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
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    compare.MostRecentTurn.againstAllOthers(), case default"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > More than 1 letter is different between these 2 turns"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "    Determinations:"));
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.GREEN, "     > Since more than 1 letter has changed, few determinations are possible.  Continuing to the next pair...\n"));
                    break;
                }
//            }
            comparisonNumber++;
        }
        return changesMade;
    }
}