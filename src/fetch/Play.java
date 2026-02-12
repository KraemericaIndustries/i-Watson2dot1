package fetch;

import dataStructures.Dashboard;
import dataStructures.Turn;
import transactSQL.Delete;
import transactSQL.Select;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Play {

    public static List<String> nextPlay(Dashboard dashboard, LinkedList<Turn> Turns) throws SQLException {

        List<String> guesses = new ArrayList<>();

        if(Turns.isEmpty() && dashboard.knownTogether.isEmpty()) {
            System.out.println("Since there are no previous turns, and I have no knowledge of any letters known to be together, Lets make a pair of guesses to try and eliminate the most common letter...");
            guesses = Select.bestWordPair(dashboard);
            System.out.println("Let's play these two guesses back to back: ");
            for(String s : guesses) {
                System.out.println(s);
            }
            Delete.rowFromWordPairs(guesses.get(0));  //  PREVENT previously selected bestWordPairs from being selected again
        } else if (!dashboard.knownTogether.isEmpty()) {
            // look for any word pairs where one of the words contains any letter in the set, and the other word contains no letters from the set  // DETERMINES ALL kT!!!
            // look for any word pairs where one of the words contains BOTH letters in the set, and the other word contains ONE letter from the set  //  DETERMINES ALL kT!!! (EXCLUSIVE TO kT set being a PAIR OF LETTERS
            // sout some pretty stuff, so the console reflects what has been done
            System.out.print("Checking to see if we can use a pair of guesses to determine if ");
            dashboard.printKnownTogether();
            System.out.println(" are IN or OUT...");
            guesses = Select.findTwoInOneOutFromWordPairs(dashboard);
            if(guesses.isEmpty()) {
                System.out.print(" > I don't know any pair of words that can eliminate ");
                dashboard.printKnownTogether();
                System.out.println("\nLet's check for any pair of words where one word contains ANY of: ");
                dashboard.printKnownTogether();
                System.out.println(" while the other word DOES NOT...");
                guesses = Select.findAsymmetricCharMatch(dashboard.knownTogether.get(0).toString());  //  ANY row in WordPairs where 1 word contains ANY letter in knownTogether, and the other word contains NO letter from knownTogether
                System.out.println("Business business business... Numbers... (Is this working?");


                //  ToDo:  Pull ALL from WordPairs where w1 or w2 contains ANY letter in kT Set, but the other DOES NOT CONTAIN any letter in the kT set
                //  ToDo:  If this fails, next tier is pull all words from words_tbl that contain kT Set, and as many letters in turn that has the highest updatedResponse as possible.  Play these guesses one at a time
                //  ToDo:       > order these guesses by most in set, descending.  Play in that order
                //  ToDo:  If this fails, next tier is pull all words from words_tbl that contain kT Set, and as many letters in turn that has the lowest updatedResponse as possible.  Play these guesses one at a time
                //  ToDo:       > order these guesses by fewest in set, ascending.  Play in that order
            }
        }
        return guesses;
    }

}

/* Some logic that may need a retrofit
            } else if (selectedStrategy.equals("ELIMINATE LETTERS KNOWN TOGETHER")) {

                if(!dashboard.knownOut.isEmpty()) {
                    System.out.println("knownOUT is Empty!");
                } else if (!dashboard.knownIn.isEmpty()) {
                    System.out.println("knownIN is Empty!");
                } else {
                    System.out.println("Since we are not certain of any letters yet, let's find the best turn to work against");

                    System.out.println(dashboard.knownTogether.get(0));


//                    for(int i = 0; i < dashboard.knownTogether.get(0).length(); i++) {
//                        System.out.println(dashboard.knownTogether.get(0).charAt(i));
//                    }
                    System.out.println("break");
                }
 */
