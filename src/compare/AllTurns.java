package compare;

import classify.Classification;
import dataStructures.Dashboard;
import print.Colors;

import java.sql.SQLException;

import static assess.AllTurns.prettyPrintLinkedHashMap;


public class AllTurns {

    public static void againstEachOther(Dashboard dashboard) throws SQLException {

        int comparisonNumber = 1;
        boolean changesMade = false;

        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "********************************************************************************  COMPARING ALL PREVIOUS TURNS AGAINST EACH OTHER  ***********************************************************************************"));

//  This do-while runs so long as changesMade is TRUE...
        do {
            for(int i = 0; i < dashboard.Turns.size() - 1; i++) {      //  Take the FIRST turn in 'Turns' (then the second, then the third, up until the SECOND LAST Turn in 'Turns')
                for(int j = i + 1; j < dashboard.Turns.size(); j++) {  //  Take the SECOND turn in 'Turns' (then the third, then the fourth, up until the LAST Turn in 'Turns')

                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "COMPARISON #" + comparisonNumber + ".  Now comparing turn #" + (i + 1) + " with turn #" + (j + 1) + ":"));
                    prettyPrintLinkedHashMap(dashboard.Turns, i, j);

                    //  CLASSIFICATION:
                    System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "    CLASSIFICATION:"));
                    Classification classification;
                    classification = new Classification(dashboard.Turns.get(i).updatedResponse, dashboard.Turns.get(j).updatedResponse, dashboard.Turns.get(i).updatedGuess, dashboard.Turns.get(j).updatedGuess);
                    classification.printClassification();
                    // Now that the selected pair of turns has been CLASSIFIED, Identify Findings, make Determinations, and take ACTION...
                    if(!classification.updatedGuessesSame) {
                        String assessment = assess.Classification.assessClassification(classification);
                        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "    FINDINGS:"));

                        // Now that we have an assessment, take action based on the assessment...
                        switch (assessment) {
                            case "    One letter changed, delta is 1":
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     > Only 1 letter has changed"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     > The (updated) responses are different by 1"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "    Determinations:"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     ROCK > We now know " + classification.onlyInFirst + " is IN, and " + classification.onlyInSecond + " is OUT!"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "    ACTIONS:"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     STEADY > Adding " + classification.onlyInFirst + " to Known IN, and " + classification.onlyInSecond + " to Known OUT!\n"));
                                process.DashboardChanges.changesToKnownIn.addAll(classification.onlyInFirst);
                                process.DashboardChanges.changesToKnownOut.addAll(classification.onlyInSecond);
                                process.DashboardChanges.updateDashboard(dashboard);
                                changesMade = true;
                                break;

                            case "One letter changed, delta is -1":
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     > Only 1 letter has changed"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     > The (updated) responses are different by 1"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "    Determinations:"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     BEE > We now know " + classification.onlyInFirst + " is OUT, and " + classification.onlyInSecond + " is IN!"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "    ACTIONS:"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     BOP > Adding " + classification.onlyInSecond + " to Known IN, and " + classification.onlyInFirst + " to Known OUT!\n"));
                                process.DashboardChanges.changesToKnownIn.addAll(classification.onlyInSecond);
                                process.DashboardChanges.changesToKnownOut.addAll(classification.onlyInFirst);
                                process.DashboardChanges.updateDashboard(dashboard);
                                changesMade = true;
                                break;

                            case "One letter changed, delta is 0":
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     > Only 1 letter has changed"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     > The (updated) response has NOT changed."));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     > We now know that " + classification.onlyInFirst + " and " + classification.onlyInSecond + " are either BOTH IN, or BOTH OUT (but can't be certain which is the case."));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "    ACTIONS:"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     > Adding " + classification.onlyInFirst + " and " + classification.onlyInSecond + " to a set of letters that are Known TOGETHER.\n"));
                                classification.onlyInFirst.addAll(classification.onlyInSecond);  //  Since these are now known to be together, ADD the second set to the first
                                dashboard.mergeSetToKnownTogether(classification.onlyInFirst);   //  MERGE the first set to the list of all sets known to be together
                                break;

                            // ToDo: Add cases for onlyInFirst or onlyInSecond = 0 (some determination is possible based on commonToBoth and only in other)...  What is the logic here??

                            // more cases...

                            default:
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     > More than 1 letter is different between these 2 turns"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "    Determinations:"));
                                System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "     > Since more than 1 letter has changed, few determinations are possible.  Continuing to the next pair...\n"));
                                break;
                        }
                        comparisonNumber++;
                    }
                }
            }
        } while (changesMade);
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, "*****************************************************************************************************************************************************************************"));
    }
}