package dataStructures;

import print.Colors;
import transactSQL.Connect;
import transactSQL.Create;
import transactSQL.Delete;

import java.sql.SQLException;
import java.util.*;

import static dataStructures.Unknown.letters;

public class Dashboard {

    // STATE
    public Pairs pairs;
    // GOSPEL
    public Set<Character> knownIn;
    public Set<Character> knownOut;
    // The source of truth for  changes that need to be processed
    // (If determined characters are immediately added to GOSPEL,  and GOSPEL is the source of truth while making changes, changes become redundant and needlessly expensive)
    public Set<Character> changesToKnownIn = new HashSet<>();
    public Set<Character> changesToKnownOut = new HashSet<>();
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


    public static void processChangesToKnownIn(Dashboard dashboard) throws SQLException {

        boolean reprocessingNeeded = false;

        do {
            //  PROCESS changes to KNOWN IN
            if(!dashboard.changesToKnownIn.isEmpty()) {                                             //  IF there are any changes to Known IN
                dashboard.knownIn.addAll(dashboard.changesToKnownIn);                               //  UPDATE Known IN (GOSPEL)
                //  UPDATE ALL Turns
                for (Turn t : dashboard.Turns) {                                                    //  FOR EVERY PREVIOUS TURN in the Turns collection
                    if (Dashboard.containsAny(t.updatedGuess, dashboard.changesToKnownIn)) {               //  IF the Turn contains ANY letter within changesToKnownIn
                        t.updatedGuess = Dashboard.removeChars(t.updatedGuess, dashboard.changesToKnownIn);                 //  REMOVE ALL letters in changesToKnownIn from the updatedGuess for that Turn
                        t.updatedResponse = t.updatedResponse - dashboard.changesToKnownIn.size();  //  CORRECT the updatedResponse (<-- Used to use the '--' operator, but this is more robust)
                        //  CHECK for Turns where updatedGuess.length == updatedResponse
                        if(t.updatedGuess.length() == t.updatedResponse) {                          //  IF updatedGuess.length == updatedResponse
                            System.out.println("We now know that every letter in " + t.updatedGuess + " is now Known IN!  Updating the dashboard...");
                            for (char c : t.updatedGuess.toCharArray()) {                           //  FOR EVERY CHARACTER in updatedGuess
                                dashboard.changesToKnownIn.add(c);                                  //  ADD the character to changesToKnownIn
                                reprocessingNeeded = true;                                          //  SET the reprocessingNeeded flag
                            }
                        }
                    }
                }
            }

        } while (reprocessingNeeded);

        //  UPDATE Words_tbl (drop words without Known IN, drop words with Known OUT)
        Delete.fromWordsTable(dashboard);

        //  REGENERATE Words_tbl...
        Create.rebuildWatsonDB(dashboard);

        //  REGENERATE WordPairs table...
        regenerateWordPairsTable();  // rebuild WordPairs table

        //  SORT UNKNOWN letters remaining...
        dashboard.sortUnknownLettersByFrequencyDescending();

        //  Clear changesTo sets
        dashboard.changesToKnownIn.clear();
    }


    public static void processChangesToKnownIn(Dashboard dashboard) throws SQLException {

        boolean reprocessingNeeded = false;

        do {
            //  PROCESS changes to KNOWN IN
            if(!dashboard.changesToKnownIn.isEmpty()) {                                             //  IF there are any changes to Known IN
                dashboard.knownIn.addAll(dashboard.changesToKnownIn);                               //  UPDATE Known IN (GOSPEL)
                //  UPDATE ALL Turns
                for (Turn t : dashboard.Turns) {                                                    //  FOR EVERY PREVIOUS TURN in the Turns collection
                    if (Dashboard.containsAny(t.updatedGuess, dashboard.changesToKnownIn)) {               //  IF the Turn contains ANY letter within changesToKnownIn
                        t.updatedGuess = Dashboard.removeChars(t.updatedGuess, dashboard.changesToKnownIn);                 //  REMOVE ALL letters in changesToKnownIn from the updatedGuess for that Turn
                        t.updatedResponse = t.updatedResponse - dashboard.changesToKnownIn.size();  //  CORRECT the updatedResponse (<-- Used to use the '--' operator, but this is more robust)
                        //  CHECK for Turns where updatedGuess.length == updatedResponse
                        if(t.updatedGuess.length() == t.updatedResponse) {                          //  IF updatedGuess.length == updatedResponse
                            System.out.println("We now know that every letter in " + t.updatedGuess + " is now Known IN!  Updating the dashboard...");
                            for (char c : t.updatedGuess.toCharArray()) {                           //  FOR EVERY CHARACTER in updatedGuess
                                dashboard.changesToKnownIn.add(c);                                  //  ADD the character to changesToKnownIn
                                reprocessingNeeded = true;                                          //  SET the reprocessingNeeded flag
                            }
                        }
                    }
                }
            }
            //  PROCESS changes to KNOWN OUT
            if(!dashboard.changesToKnownOut.isEmpty()) {                                            //  IF there are changes to Known OUT
                dashboard.knownIn.addAll(dashboard.changesToKnownOut);                              //  UPDATE Known OUT (GOSPEL)
                //  UPDATE ALL Turns
                for (Turn t : dashboard.Turns) {                                                    //  FOR EVERY PREVIOUS TURN in the Turns collection
                    if (Dashboard.containsAny(t.updatedGuess, dashboard.changesToKnownOut)) {       //  IF the Turn contains ANY letter within changesToKnownIn
                        System.out.println("Infinite loop?");
                        Dashboard.removeChars(t.updatedGuess, dashboard.changesToKnownOut);         //  REMOVE ALL letters in changesToKnownOut from the updatedGuess for that Turn
                        //  CHECK for Turns where updatedGuess IS NOT empty, and updatedResponse > 0
                        if((!t.updatedGuess.isEmpty()) && t.updatedResponse == 0) {
                            System.out.println("We now know that every letter in " + t.updatedGuess + " is now Known OUT!  Updating the dashboard...");
                            for (char c : t.updatedGuess.toCharArray()) {                           //  FOR EVERY CHARACTER in updatedGuess
                                dashboard.changesToKnownOut.add(c);                                 //  ADD the character to changesToKnownOut
                                reprocessingNeeded = true;                                          //  SET the reprocessingNeeded flag
                            }
                        }
                    }
                }
            }
        } while (reprocessingNeeded);

        //  UPDATE Words_tbl (drop words without Known IN, drop words with Known OUT)
        Delete.fromWordsTable(dashboard);

        //  REGENERATE Words_tbl...
        Create.rebuildWatsonDB(dashboard);

        //  REGENERATE WordPairs table...
        regenerateWordPairsTable();  // rebuild WordPairs table

        //  SORT UNKNOWN letters remaining...
        dashboard.sortUnknownLettersByFrequencyDescending();

        //  Clear changesTo sets
        dashboard.changesToKnownOut.clear();

    }


    //  REGENERATE the WordPairs table as previously unknown letters are determined to be KNOWN OUT...
    public static void regenerateWordPairsTable() {

        System.out.println("assess.AllTurns.regenerateWordPairsTable(): BEGIN");

        Connect.watson("truncateWordPairsTable");    //  DROP the WordPairs table...
        Connect.watson("createWordPairsTable");  //  REGENERATE the WordPairs table...
        Connect.watson("deleteDups");            //  DELETE dups from the WordPairs table...

        System.out.println("assess.AllTurns.regenerateWordPairsTable(): END");
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

