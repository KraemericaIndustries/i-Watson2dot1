package dataStructures;

import assess.AllTurns;
import transactSQL.Connect;
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

    public void checkPairsForWordExists(Unknown unknown, Set<Character> knownOut, LinkedList<Turn> Turns) throws SQLException {

        //  ToDo: Need iterator here so we can remove the set
        for (Set<Character> s : knownTogether) {
            if (s.size() > 2) {

                StringBuilder sb = new StringBuilder();

                sb.append("select * from Words_tbl " +
                        "where " +
                        "word like '%");
                for (Character c : s) {
                    sb.append(c).append("%' and word like '%");
                }
                sb.delete((sb.length() - 19), (sb.length() - 1));
                sb.append("';");
                //connect watson  - send this - send reason
                Connect.watson(sb.toString(), s, unknown);
                //  Remove set from AllTurns
                knownOut.addAll(s);
                AllTurns.removeKnownOutFromAllTurns(knownOut, Turns);
                //  ToDo: NEED a ASSESS ALL TURNS HERE!!!
                System.out.println();
            }
        }
    }
}