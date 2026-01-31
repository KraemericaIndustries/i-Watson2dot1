package classify;

import dataStructures.Dashboard;

import java.util.LinkedList;

public class Strategies {

    public static LinkedList<String> buildStrategies(Dashboard dashboard) {

        LinkedList<String> strategies = new LinkedList<>();

        String tryEliminateMostCommon = "ELIMINATE THE MOST COMMON LETTER\n   > Selects a pair of words containing the most commonly occurring letters, where the first word CONTAINS the most common letter, and the second word DOES NOT.\n   > This *might* make a determination on ONE LETTER in a single play, or at least determine that a pair of letters are 'known together' (either IN, or OUT).";
        String tryDetermineKnownTogether = "ELIMINATE LETTERS KNOWN TOGETHER\n   > Letters 'known together' may either be IN, or OUT.  Another play is required to better determine the outcome.\n   > This *might* make a determination on ONE LETTER in a single play, or at least determine that a pair of letters are 'known together' (either IN, or OUT).";
        String takeShotInDark = "SHOT IN THE DARK\n   > Hey, who knows.  You might get lucky (but I doubt it.)";
        String downToChance = "Now it's down to chance.  Make a guess from the remaining words.\n   > Luck of the draw";

        if(dashboard.numWordPairs ==0  && dashboard.knownTogether.isEmpty()) {
            strategies.add(downToChance);
        } else if(!dashboard.knownTogether.isEmpty()) {
            strategies.add(tryDetermineKnownTogether);
            strategies.add(tryEliminateMostCommon);
            strategies.add(takeShotInDark);
        } else {
            strategies.add(tryEliminateMostCommon);
            strategies.add(takeShotInDark);
        }
        return strategies;
    }

    public static void prettyPrintStrategies(LinkedList<String> strategies) {

        int rank = 1;

        System.out.println("********************************************************************* SUGGESTED STRATEGIES ****************************************************************************************\n");

        System.out.println("Here is a prioritized list of suggested strategies.  Each strategy has been ranked based on it's potential impact:\n");

        for (String s : strategies) {
            System.out.println(rank + ". " + s);
            rank++;
        }
        System.out.println();
        System.out.println("Select a strategy from the available list.  (They have been prioritized based on the greatest potential impact.)  Which strategy would you like to try:");
        System.out.println("***********************************************************************************************************************************************************************************");
    }
}