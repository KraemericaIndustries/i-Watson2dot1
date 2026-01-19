package dataStructures;

import transactSQL.Connect;
import transactSQL.Create;
import transactSQL.Delete;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static dataStructures.Unknown.letters;
import static print.Messages.prettyPrintPreviousGuesses;

public class Dashboard {

    // STATE
    public Unknown unknown;
    public Pairs pairs;
    public Set<Character> knownIn;
    public Set<Character> knownOut;
    public LinkedList<Turn> Turns;
    public char[] unsortedLettersFromMap;  //  To create char[] sortedLetters, a temporary array is declared and only initialized once the map has been populated with letter occurrences read in from all words
    public char[] sortedLetters;           //  DOWNSTREAM DB query for words unable to get key from map by index.  Creating this sorted array to accommodate that
    public int reportNumber = 1;
    public static int numWordPairs = 0;
    public static LinkedHashMap<Character, Integer> unknownLetters;

    // CONSTRUCTOR
    public Dashboard() {
        unknown = new Unknown();  //  <- Sorting Unknown letters by number of occurrences in the database is done via a stream, requiring an instance of the class
        pairs = new Pairs();
        knownIn = new HashSet<>();
        knownOut = new HashSet<>();
        Turns = new LinkedList<>();
        letters = new LinkedHashMap<>();
        unknownLetters = new LinkedHashMap<>();
    }

    // BEHAVIOUR (methods)
//    public void addSet(Set<Character> determined, Unknown unknown) throws SQLException {
//        Commenting OUT as there are no current usages
//
//        letters.addAll(determined);
//
//        String toAdd = Pairs.createStringFromSet(determined);
//
//        Delete.wordsWithout(toAdd, unknown, letters);
//
//        Create.rebuildWatsonDB(letters, unknown);
//
//    }
    public void letterEnumerator(String word) {
        for(int i = 0; i <word.length(); i++) {
            int count = letters.getOrDefault(word.charAt(i), 0);
            unknownLetters.put(word.charAt(i), count + 1);
        }
    }

    public void sort() {
        LinkedHashMap<Character, Integer> sortedMap = letters.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        letters.clear();
        letters.putAll(sortedMap);
        sortedMap.clear();
    }

    public void loadSortedLetters() {

        unsortedLettersFromMap = new char[letters.size()];
        int i = 0;

        for(Map.Entry<Character, Integer> entry : letters.entrySet()) {
            unsortedLettersFromMap[i] = entry.getKey();
            i++;
        }
        sortedLetters = unsortedLettersFromMap;
    }


    public void printDashboard() {


        System.out.println("*****************************************************************  DASHBOARD REPORT # " + reportNumber + " *****************************************************************************************");

        //  PRINT the LinkedHashMaps...
        System.out.println("Known IN: " + knownIn);
        System.out.println("Known OUT: " + knownOut);
        pairs.prettyPrintPairs();
        System.out.println("Unknown: " + unknownLetters + "\n");

        prettyPrintPreviousGuesses(Turns);  //  PRINT all previous guesses

        //  GET counts from database table to furnish report, and drive logic...
        int numWords = (int) Connect.watson("getNumWordsInDB");
        numWordPairs = (int) Connect.watson("countWordPairs");

        System.out.print("There are " + numWords + " words remaining in the database.\n");
        System.out.println("There are " + numWordPairs + " word pairs that differ by only 1 letter.");
        System.out.println("***********************************************************************************************************************************************************************\n");

    }

}
