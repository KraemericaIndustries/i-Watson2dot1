package dataStructures;

import transactSQL.Create;
import transactSQL.Delete;
import java.sql.SQLException;
import java.util.*;

public class Pairs {

    public Set<Set<Character>> knownTogether = new HashSet<>();

    public void prettyPrintPairs() {
        System.out.println("Known TOGETHER: " + knownTogether);
//        for (Set<Character> s : knownTogether) {
//            System.out.println(s);
//        }
    }

    public void checkPairsForKnownIn(Set<Character> knownIn, Unknown unknown) throws SQLException {
        for (Character c : knownIn) {

            // Create an iterator for the set of sets
            Iterator<Set<Character>> iterator = knownTogether.iterator();

            // Loop through each set using the iterator
            while (iterator.hasNext()) {
                Set<Character> s = iterator.next();
                // Check if the set contains the desired character (e.g., 'a')
                if (s.contains(c)) {
                    knownIn.addAll(s);
                    Delete.wordsWithout(createStringFromSet(s), unknown, knownIn);
                    // Clear the set
                    s.clear();
                    // Remove the cleared set using the iterator
                    iterator.remove();
                }
            }
        }
    }

    public void checkPairsForKnownOut(Set<Character> knownOut, Unknown unknown) throws SQLException {
        for (Character c : knownOut) {

            // Create an iterator for the set of sets
            Iterator<Set<Character>> iterator = knownTogether.iterator();

            // Loop through each set using the iterator
            while (iterator.hasNext()) {
                Set<Character> s = iterator.next();
                // Check if the set contains the desired character (e.g., 'a')
                if (s.contains(c)) {
                    knownOut.addAll(s);
                    // Clear the set
                    s.clear();
                    // Remove the cleared set using the iterator
                    iterator.remove();
                    Delete.wordsWithout(createStringFromSet(s), unknown, knownOut);
                }
            }
        }
    }

    public static String createStringFromSet(Set<Character> set) {
        StringBuilder sb = new StringBuilder();

        for (Character c : set) {
            sb.append(c);
        }
        return sb.toString();
    }

    public void addPairsToSets(HashSet<Character> copy) {

        if (knownTogether.isEmpty()) {
            knownTogether.add(copy);
        } else {

            // Create an iterator for the set of sets
            Iterator<Set<Character>> iterator = knownTogether.iterator();

            for (Character c : copy) {

                while (iterator.hasNext()) {  // Loop through each set using the iterator
                    Set<Character> s = iterator.next();
                    if (s.contains(c)) {  // Check if the set contains the desired character (e.g., 'a')
                        s.addAll(copy);
                    }
                }
            }
        }
    }

    public void checkTurnAgainstPairs(Turn turn, LinkedList<Turn> Turns, Unknown unknown) {

        for (Set<Character> s : knownTogether) {  //  For EVERY SET in knownTogether
            for (Character c : s) {  //  For EVERY CHARACTER in the set
                if (turn.turn.contains(c) && turn.updatedResponse < s.size()) {
                    // ALL s MUST BE OUT!
                    // strip s from ALL TURNS
                    turn.turn.removeAll(s);

                    // DELETE all kT from the database
                    Create.rebuildWatsonDB(s, unknown);// rebuild the database
                }
            }
        }

    }
}