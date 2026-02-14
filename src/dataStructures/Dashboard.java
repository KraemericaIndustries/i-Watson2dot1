package dataStructures;

import transactSQL.Connect;
import transactSQL.Create;
import transactSQL.Delete;

import java.sql.SQLException;
import java.util.*;

import static dataStructures.Unknown.letters;
import static print.Messages.prettyPrintPreviousGuesses;

public class Dashboard {

    // STATE
    public Pairs pairs;
    // GOSPEL
    public Set<Character> knownIn;
    public Set<Character> knownOut;
    // The source of truth for  changes that need to be processed
    // (If determined characters are immediately added to GOSPEL,  and GOSPEL is the source of truth while making changes, changes become redundant and needlessly expensive)
    public Set<Character> changesToKnownIn;
    public Set<Character> changesToKnownOut;
    // A List of Turns
    public LinkedList<Turn> Turns;
    // A number to be incremented iteratively to track the number of guesses needed to finish the game
    public int reportNumber = 1;
    // The number of pairs of words remaining in Word_tbl that differ by ONLY ONE LETTER
    public int numWordPairs = 0;
    // An array to back seeding of letter counts into the dashboard
    public int [] letterCounts = new int[26];
    public List<LetterScore> unknownLetters = new ArrayList<>();
//    public List<String> knownTogether = new ArrayList<>();
    public List<Set<Character>> knownTogether = new ArrayList<>();

    // CONSTRUCTOR
    public Dashboard() {
        pairs = new Pairs();
        knownIn = new HashSet<>();
        knownOut = new HashSet<>();
        Turns = new LinkedList<>();
        letters = new LinkedHashMap<>();
    }

    // BEHAVIOUR (methods)
    public void printDashboard(LinkedList<Turn> Turns) {

        System.out.println("*****************************************************************  DASHBOARD REPORT # " + reportNumber + " ************************************************************************************");

        //  PRINT the LinkedHashMaps...
        System.out.println("Known IN:  " + knownIn);
        System.out.println("Known OUT: " + knownOut);
        System.out.println("Known Together: " + knownTogether);
        prettyPrintUnknownLetters(unknownLetters);

        prettyPrintPreviousGuesses(Turns);  //  PRINT all previous guesses

        //  GET counts from database table to furnish report, and drive logic...
        int numWords = (int) Connect.watson("getNumWordsInDB");
        numWordPairs = (int) Connect.watson("countWordPairs");

        System.out.print("There are " + numWords + " words remaining in the database.\n");
        System.out.println("There are " + numWordPairs + " word pairs that differ by only 1 letter.");
        System.out.println("*******************************************************************************************************************************************************************************\n");

    }
    public static void prettyPrintUnknownLetters(List<LetterScore> unknownLetters) {

        System.out.println("------------------------------------------------------------------------UNKNOWN LETTERS AND FREQUENCY OF OCCURRENCE------------------------------------------------------");
        System.out.print("Unknown:   ");
        for(int i = 0; i < 26; i++) {
            System.out.printf(" | %3s", unknownLetters.get(i).letter);
        }
        System.out.println();

        System.out.print("Frequency: ");
        for(int i = 0; i < 26; i++) {
            System.out.printf(" | %3d", unknownLetters.get(i).score);
        }
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void buildUnknownLettersList() {
        unknownLetters.add(new LetterScore('A', letterCounts[0]));
        unknownLetters.add(new LetterScore('B', letterCounts[1]));
        unknownLetters.add(new LetterScore('C', letterCounts[2]));
        unknownLetters.add(new LetterScore('D', letterCounts[3]));
        unknownLetters.add(new LetterScore('E', letterCounts[4]));
        unknownLetters.add(new LetterScore('F', letterCounts[5]));
        unknownLetters.add(new LetterScore('G', letterCounts[6]));
        unknownLetters.add(new LetterScore('H', letterCounts[7]));
        unknownLetters.add(new LetterScore('I', letterCounts[8]));
        unknownLetters.add(new LetterScore('J', letterCounts[9]));
        unknownLetters.add(new LetterScore('K', letterCounts[10]));
        unknownLetters.add(new LetterScore('L', letterCounts[11]));
        unknownLetters.add(new LetterScore('M', letterCounts[12]));
        unknownLetters.add(new LetterScore('N', letterCounts[13]));
        unknownLetters.add(new LetterScore('O', letterCounts[14]));
        unknownLetters.add(new LetterScore('P', letterCounts[15]));
        unknownLetters.add(new LetterScore('Q', letterCounts[16]));
        unknownLetters.add(new LetterScore('R', letterCounts[17]));
        unknownLetters.add(new LetterScore('S', letterCounts[18]));
        unknownLetters.add(new LetterScore('T', letterCounts[19]));
        unknownLetters.add(new LetterScore('U', letterCounts[20]));
        unknownLetters.add(new LetterScore('V', letterCounts[21]));
        unknownLetters.add(new LetterScore('W', letterCounts[22]));
        unknownLetters.add(new LetterScore('X', letterCounts[23]));
        unknownLetters.add(new LetterScore('Y', letterCounts[24]));
        unknownLetters.add(new LetterScore('Z', letterCounts[25]));
    }

    public void sortUnknownLettersByFrequencyDescending() {
        unknownLetters.sort((a, b) -> b.score - a.score);
    }

    public void mergeSetToKnownTogether(Set<Character> incoming) {

        boolean merged = false;

        for (Set<Character> setsKnownToBeTogether : knownTogether) {
            if (!Collections.disjoint(setsKnownToBeTogether, incoming)) {
                setsKnownToBeTogether.addAll(incoming);
                merged = true;
                break;
            }
        }
        if (!merged) {
            knownTogether.add(incoming);
        }
    }

//    public void updateDashboard() throws SQLException {
//
//        if(!changesToKnownIn.isEmpty()) {
//            knownIn.addAll(changesToKnownIn);  //  UPDATE GOSPEL
//            for (Turn t : Turns) {
//                if (containsAny(t.guess, changesToKnownIn)) {
//                    removeChars(t.guess, changesToKnownIn);  //  REMOVE changesToKnownIn from t.guess
//                    t.updatedResponse--;  //  DECREMENT updatedResponse
//                }
//            }
//            // delete all words from DB that DO NOT contain letters in this set
//            // remove this set from all updatedTurns in 'Turns' AND decrement updatedResponse
//        }
//        if(!changesToKnownOut.isEmpty()) {
//            knownIn.addAll(changesToKnownIn);  //  UPDATE GOSPEL
//            for (Turn t : Turns) {
//                if (containsAny(t.guess, changesToKnownOut)) {
//                    removeChars(t.guess, changesToKnownOut);  //  REMOVE changesToKnownOut from t.guess
//                }
//            }
//            // delete all words from DB that DO contain letters in this set
//            // remove this set from all updatedTurns in 'Turns'
//        }
//
//        //  WHAT has to change??
//
//
//
//        //  Update Words table (drop words without, drop words with)  //  ToDo: Any subsequent invocations should use dashboard as a parameter.  Avoids PARAMETER HELL
//        Delete.fromWordsTable(changesToKnownIn, changesToKnownOut);
//
//        //  Regenerate/populate Words_tbl
//        Create.rebuildWatsonDB();
//        //  Re-generate WordPairs
//        //  Clear changesTo sets
//
//
//
//        // REBUILD THE DATABASE
//        // REBUILD WORD PAIRS
//
//    }

    public void printKnownTogether() {
        for(Set<Character> set : knownTogether) {
            for(Character c: set) {
                System.out.print(c);
            }
        }
    }

    public static String removeChars(String input, Set<Character> remove) {
        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (!remove.contains(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean containsAny(String input, Set<Character> chars) {
        for (char c : input.toCharArray()) {
            if (chars.contains(c)) {
                return true;
            }
        }
        return false;
    }
}

