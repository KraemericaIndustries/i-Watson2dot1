package dataStructures;

import transactSQL.Connect;
import java.util.*;

import static dataStructures.Unknown.letters;
import static java.lang.System.out;
import static print.Messages.prettyPrintPreviousGuesses;

public class Dashboard {

    // STATE
    public Pairs pairs;
    public Set<Character> knownIn;
    public Set<Character> knownOut;
    public LinkedList<Turn> Turns;
    public int reportNumber = 1;
    public int numWordPairs = 0;
    public int [] letterCounts = new int[26];
    public List<LetterScore> unknownLetters = new ArrayList<>();
    public List<String> knownTogether = new ArrayList<>();

    // CONSTRUCTOR
    public Dashboard() {
        pairs = new Pairs();
        knownIn = new HashSet<>();
        knownOut = new HashSet<>();
        Turns = new LinkedList<>();
        letters = new LinkedHashMap<>();
    }

    // BEHAVIOUR (methods)
    public void printDashboard() {

        out.println("*****************************************************************  DASHBOARD REPORT # " + reportNumber + " ************************************************************************************");

        //  PRINT the LinkedHashMaps...
        out.println("Known IN: " + knownIn);
        out.println("Known OUT: " + knownOut);
        pairs.prettyPrintPairs();
        prettyPrintUnknownLetters(unknownLetters);

        prettyPrintPreviousGuesses(Turns);  //  PRINT all previous guesses

        //  GET counts from database table to furnish report, and drive logic...
        int numWords = (int) Connect.watson("getNumWordsInDB");
        numWordPairs = (int) Connect.watson("countWordPairs");

        out.print("There are " + numWords + " words remaining in the database.\n");
        out.println("There are " + numWordPairs + " word pairs that differ by only 1 letter.");
        out.println("*******************************************************************************************************************************************************************************\n");

    }
    public static void prettyPrintUnknownLetters(List<LetterScore> unknownLetters) {

        out.println("------------------------------------------------------------------------UNKNOWN LETTERS AND FREQUENCY OF OCCURRENCE------------------------------------------------------");
        out.print("Unknown:   ");
        for(int i = 0; i < 26; i++) {
            out.printf(" | %3s", unknownLetters.get(i).letter);
        }
        out.println();

        out.print("Frequency: ");
        for(int i = 0; i < 26; i++) {
            out.printf(" | %3d", unknownLetters.get(i).score);
        }
        out.println();
        out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
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
}

