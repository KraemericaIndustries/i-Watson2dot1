package classify;

import dataStructures.Dashboard;

import java.util.LinkedList;

public class Strategies {

    public static LinkedList<String> buildStrategies(Dashboard dashboard) {

        LinkedList<String> strategies = new LinkedList<>();

        if(dashboard.numWordPairs ==0) {
            strategies.add("Now it's down to chance.  Make a guess from the remaining words.");
        } else {
            strategies.add("Eliminate the most common letter from word pairs that differ by one letter.");
            strategies.add("Try to make a determination on letters that are known to be together, whether IN or OUT (as this may identify more than 1 letter with a single play.)");
            strategies.add("Take a shot in the dark.");
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
        System.out.println("Select a strategy from the available list.  (They have been prioritized based on the greatest potential impact");
        System.out.println("***********************************************************************************************************************************************************************************");
    }
}