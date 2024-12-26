package dataStructures;

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
                    // Clear the set
                    s.clear();
                    // Remove the cleared set using the iterator
                    iterator.remove();
                    Delete.wordsWithout(createStringFromSet(s), unknown, knownIn);
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
}