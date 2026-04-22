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

