import classify.Strategies;
import dataStructures.*;
import print.Messages;
import read.Keyboard;
import transactSQL.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import static transactSQL.Select.bestWordPair;


public class App {

    public static void main(String[] args) throws Exception {

        //  SETUP...
        Messages.welcome();
        DatabaseConnection.getProperties();
        Create.watsonDB();

        //  SETUP: Create datastore objects to facilitate play...

//        Unknown unknown = new Unknown();  //  <- Sorting Unknown letters by number of occurrences in the database is done via a stream, requiring an instance of the class
//        Pairs pairs = new Pairs();
//        Set<Character> knownIn = new HashSet<>();
//        IdentifiedLetters knownIn = new IdentifiedLetters();
//        Set<Character> knownOut = new HashSet<>();
//        IdentifiedLetters knownOut = new IdentifiedLetters();
//        LinkedList<Turn> Turns = new LinkedList<>();
        //  CREATE a dashboard...
        Dashboard dashboard = new Dashboard();

        //  SETUP: Load database tables...
        Insert.loadKnownWords(dashboard);
        dashboard.buildUnknownLettersList();
        dashboard.sortUnknownLettersByFrequencyDescending();
        Connect.watson("createWordPairsTable");
        Connect.watson("deleteDups");

        //  SETUP: Use letter counts accumulated during database entry to SORT a list of letters based on how frequently they appear in the database...
//        dashboard.sort();
//        dashboard.loadSortedLetters();

        //  PLAY the game...
        String lastGuess;
        boolean guessIsWord;
        Messages.play();

        do {
            dashboard.printDashboard();  //  PRINT a report of possible determinations

            //  CREATE and PRESENT strategies, and make a selection...
            LinkedList<String> strategies = Strategies.buildStrategies(dashboard);
            Strategies.prettyPrintStrategies(strategies);
            String selectedStrategy = Keyboard.selectStrategy(strategies);

            if(selectedStrategy.equals("Eliminate the most common letter from word pairs that differ by one letter.")) {
                System.out.println("You have chosen: " + selectedStrategy);

                List<String> guesses = bestWordPair(dashboard);  //  SELECT the BEST pair of guesses from WordPairs
                for(String g : guesses) {
                    //  ToDo: iterate guesses here
                    System.out.println(g);
                }
                System.out.println("break");
            }


//            Messages.results(pairs, Turns, unknown);           //  PRINT the results of previous plays and determinations
//
//            Turn turn = new Turn(Keyboard.guess(), Keyboard.responseFromOpponent());  //  Take a turn
//
//            //  Take action, based on the response...
//             if(turn.response == 5) {
//                 Turns.add(turn);
//                 lastGuess = turn.guess;
//                 AllTurns.makeDeterminations(Turns, pairs, knownIn, knownOut, unknown);
//                break;
//            } else if(!(turn.response == 0)) {
//                 lastGuess = turn.guess;
//                 AllTurns.updateTurn(turn, knownIn, knownOut, unknown, Turns);  //  Process turn (remove knownIn/knownOut, if all knownTogether & (updatedResponse < knownTogether, all knownTogether IS OUT, etc.)
//                 Turns.add(turn);
//                 pairs.checkTurnAgainstPairs(turn, unknown);
//            } else {
//                 lastGuess = turn.guess;
//                 AllTurns.responseOfZero(turn, knownOut, unknown, Turns);
//             }
//
//            if(Turns.size() >= 2) AllTurns.makeDeterminations(Turns, pairs, knownIn, knownOut, unknown);  //  ANALYZE all previous guesses (now that a new guess and response are available)

            dashboard.reportNumber++;

            int numWordPairsRemaining = (Integer) Connect.watson("countWordPairs");
            if (numWordPairsRemaining == 0) break;

        } while (Insert.wordCount > 3);

        //  THE HOME STRETCH!
        //  Response is 5 BUT last guess is NOT opponents word!!!
        lastGuess = "ABC";
        guessIsWord = Keyboard.verify(lastGuess);

        if(guessIsWord) {
            Messages.victorySummary(lastGuess);
        } else if (Insert.wordCount == 0) {
            stumped();
        } else {
            System.out.println();
            LinkedList<String> lastWords = new LinkedList<>();

            ResultSet rs = Query.select("select * from Words_tbl where word != '" + lastGuess + "'");

            while(rs.next()) {
                lastWords.add(rs.getString(1));
            }

            Connect.watson("deleteFromWordsTable");

            System.out.println("\n*****************************************************************  END GAME  *******************************************************************************************\n");
//            Messages.report(knownIn, knownOut, pairs, Turns);  //  PRINT a report of possible determinations
//            Messages.results(pairs, Turns, unknown);                    //  PRINT the results of previous plays and determinations

            do {
                if(lastWords.isEmpty()) break;

                if(guessIsWord) {
                    break;
                } else {

                    Messages.reportNumber++;
                    System.out.println("Here are the last remaining words in the database:\n" + lastWords);
                    System.out.println("Make a guess from the choices, above.  ");
                    lastGuess = Keyboard.guess();
                    guessIsWord = Keyboard.verify(lastGuess);
                    lastWords.remove(lastGuess);
                }
            } while (!(lastWords.isEmpty()));

            if(guessIsWord) {
                Messages.victorySummary(lastGuess);
            } else {
                stumped();
            }
        }
    }

    private static void stumped() {
        String lastGuess;
        System.out.println("\nYa got me!  I'm stumped (this time)!  But I'm adding your word to my database, so the next time I run I KNOW YOUR WORD!  What was your word?:");

        lastGuess = Keyboard.enterUnknownWord();

        try {
            Files.write(Paths.get("C:/Users/Bob/IdeaProjects/i-Watson2dot1/FiveLetterWords.txt"), ("\n" + lastGuess).getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        System.out.println(" > Added " + lastGuess + " to the data file used to generate the watson database.");
    }
}