package dataStructures;

import assess.AllTurns;
import transactSQL.Connect;
import transactSQL.Create;
import transactSQL.Delete;
import java.sql.SQLException;
import java.util.*;

import static assess.AllTurns.checkAllTurnsForSizeEqualsUpdatedResponse;

public class Pairs {

    public Set<Set<Character>> knownTogether = new HashSet<>();

    public void prettyPrintPairs() {
        System.out.println("Known TOGETHER: " + knownTogether);
    }

    public void checkPairsForKnownIn(IdentifiedLetters knownIn, Unknown unknown) throws SQLException {
        for (Character c : knownIn.letters) {

            Iterator<Set<Character>> iterator = knownTogether.iterator();  // Create an iterator for the set of sets

            // Loop through each set using the iterator
            while (iterator.hasNext()) {
                Set<Character> s = iterator.next();
                if (s.contains(c)) {  // Check if the set contains the desired character (e.g., 'a')
                    knownIn.letters.addAll(s);
                    Delete.wordsWithout(createStringFromSet(s), unknown, knownIn.letters);
                    s.clear();          // Clear the set
                    iterator.remove();  // Remove the cleared set using the iterator
                }
            }
        }
    }

    public void checkPairsForKnownOut(IdentifiedLetters knownOut, Unknown unknown) throws SQLException {
        for (Character c : knownOut.letters) {

            Iterator<Set<Character>> iterator = knownTogether.iterator();  // Create an iterator for the set of sets

            // Loop through each set using the iterator
            while (iterator.hasNext()) {
                Set<Character> s = iterator.next();
                if (s.contains(c)) {  // Check if the set contains the desired character (e.g., 'a')
                    knownOut.letters.addAll(s); //  << WORKS
                    Delete.wordsWith(createStringFromSet(s), unknown, knownOut);  //  CHANGED to .wordsWith()
                    s.clear();          // Clear the set
                    iterator.remove();  // Remove the cleared set using the iterator
                }
                return;  //  <<  KLUDGE fixes NYMPH Crash
            }
        }
    }

    public static String createStringFromSet(Set<Character> set) {

        StringBuilder sb = new StringBuilder();

        for (Character c : set) sb.append(c);
        return sb.toString();
    }

    public void addPairsToSets(HashSet<Character> copy) {

        if (knownTogether.isEmpty()) knownTogether.add(copy);
        else {

            Iterator<Set<Character>> iterator = knownTogether.iterator();  // Create an iterator for the set of sets

            for (Character c : copy) {
                while (iterator.hasNext()) {  // Loop through each set using the iterator
                    Set<Character> s = iterator.next();
                    if (s.contains(c)) s.addAll(copy);  // Check if the set contains the desired character (e.g., 'a')
                }
            }
        }
    }

    public void checkTurnAgainstPairs(Turn turn, Unknown unknown) {

        for (Set<Character> s : knownTogether) {  //  For EVERY SET in knownTogether
            for (Character c : s) {               //  For EVERY CHARACTER in the set
                if (turn.turn.contains(c) && turn.updatedResponse < s.size()) {  // ALL s must be OUT!  strip s from ALL TURNS
                    turn.turn.removeAll(s);
                    Create.rebuildWatsonDB(s, unknown);  // REBUILD the database
                }
            }
        }
    }

    public void checkPairsForWordExists(Unknown unknown, IdentifiedLetters knownOut, LinkedList<Turn> Turns, IdentifiedLetters knownIn) throws SQLException {

        Iterator<Set<Character>> iterator = knownTogether.iterator();  // Create an iterator for the set of sets

        while (iterator.hasNext()) {

            Set<Character> s = iterator.next();

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

                Connect.watson(sb.toString(), s, unknown);  //  DELETE from database where word like String

                knownOut.letters.addAll(s);  //  ADD the set to the list of characters KNOWN to be OUT
                AllTurns.removeKnownOutFromAllTurns(knownOut, Turns);

                s.clear();          // Clear the set
                iterator.remove();  // Remove the cleared set using the iterator

                checkAllTurnsForSizeEqualsUpdatedResponse(Turns, knownIn, unknown);
            }
        }
    }
}