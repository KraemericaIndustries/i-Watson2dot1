package fetch;

import dataStructures.Dashboard;
import dataStructures.Turn;
import transactSQL.Delete;
import transactSQL.Select;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Play {

    public static List<String> nextPlay(Dashboard dashboard, LinkedList<Turn> Turns) throws SQLException {

        List<String> guesses = new ArrayList<>();

        //  SELECT WordPair from most frequent letters (to try and eliminate a most common letter)...
        if(Turns.isEmpty() && dashboard.knownTogether.isEmpty()) {
            System.out.println("Since there are no previous turns, and I have no knowledge of any letters known to be together, Lets make a pair of guesses to try and eliminate the most common letter...");
            guesses = Select.bestWordPair(dashboard);
            playWordPairConsecutively(guesses);

            //  At least 1 pair of letters are knownTogether...
        } else if (!dashboard.knownTogether.isEmpty()) {

            //  Look for any word pairs where one of the words contains ANY TWO letters in the set, and the other word contains ONE letter of those two
            //  (This WILL eliminate TWO of the most common letters)
            //  ToDo: findTwoInOneOutFromWordPairs() NEEDS COPILOT WORK!!!  The IMPLEMENTATION is not QUITE right
            System.out.print("Checking to see if we can use a pair of guesses to determine if any TWO of ");
            dashboard.printKnownTogether();
            System.out.println(" are IN or OUT...");
            guesses = Select.findTwoInOneOutFromWordPairs(dashboard);

            //  Check ALL sets knownTogether for ANY WordPair where one word contains ANY letter from the set, and the other word contains NO letters from the set...
            //  (This would eliminate ALL letters in a given set)
            //  ToDo:  Can this be made to iterate over sets from largest to smallest?  (This would ensure the most letters possible are attempted first)
            if(guesses.isEmpty()) {
                printDunno(dashboard);
                System.out.println("\nLet's check for any pair of words where one word contains ANY of: ");
                dashboard.printKnownTogether();
                System.out.println(" while the other word DOES NOT...");
                //  Check ALL sets knownTogether for ANY WordPair where one word contains ANY letter from the set, and the other word contains NO letters from the set...
                for(Set<Character> ktSet: dashboard.knownTogether) {
                    guesses = Select.findAsymmetricCharMatch(ktSet);
                }

                if(guesses.isEmpty()) {
                    printDunno(dashboard);
                    System.out.println("\nLet's check for any words that contains ANY of: ");
                    dashboard.printKnownTogether();
                    System.out.println(" and as many letters in the turn that has the HIGHEST (updated) response as possible...");  //  <-- ToDo: Write the code that identifies this
                    //  ToDo:  Next tier is pull all words from words_tbl that contain kT Set, and as many letters in turn that has the highest updatedResponse as possible.  Play these guesses one at a time
                    //  ToDo:       > order these guesses by most in set, descending.  Play in that order
                }  if (guesses.isEmpty()) {
                    System.out.print(" > I don't know any pair of words that can DOE DEE DOE DEE DOE ");  //  <-- ToDo: Write a fancy print statement, here
                    dashboard.printKnownTogether();
                    System.out.println("\nLet's check for any words that contains ANY of: ");
                    dashboard.printKnownTogether();
                    System.out.println(" and as many letters in the turn that has the LOWEST (updated) response as possible...");  //  <-- ToDo: Write the code that identifies this
                    //  ToDo:  Next tier is pull all words from words_tbl that contain kT Set, and as many letters in turn that has the lowest updatedResponse as possible.  Play these guesses one at a time
                    //  ToDo:       > order these guesses by fewest in set, ascending.  Play in that order
                }  else {
                    System.out.println("I am unable to draw any conclusions from previous turns, or any letters known to be together.  Lets make a pair of guesses to try and eliminate the most common letter, and bolster what we know...");
                    guesses = Select.bestWordPair(dashboard);
                    playWordPairConsecutively(guesses);
                }
            }
        }
        return guesses;
    }

    private static void printDunno(Dashboard dashboard) {
        System.out.print(" > I don't know any pair of words that can eliminate ");
        dashboard.printKnownTogether();
    }

    private static void playWordPairConsecutively(List<String> guesses) throws SQLException {
        System.out.println("Let's play these two guesses back to back: ");
        for(String s : guesses) {
            System.out.println(s);
        }
        Delete.rowFromWordPairs(guesses.get(0));  //  PREVENT previously selected bestWordPairs from being selected again
    }
}