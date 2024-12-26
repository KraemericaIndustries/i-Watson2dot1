import assess.AllTurns;
import dataStructures.*;
import print.Messages;
import read.Keyboard;
import transactSQL.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class App {

    public static void main(String[] args) throws Exception {

        //  SETUP...
        Messages.welcome();
        DatabaseConnection.getProperties();
        Create.watsonDB();

        //  SETUP: Create datastore objects to facilitate play...
        Unknown unknown = new Unknown();  //  <- Sorting Unknown letters by number of occurrences in the database is done via a stream, requiring an instance of the class
        Pairs pairs = new Pairs();
//        LinkedList<Character> knownIn = new LinkedList<>();
        Set<Character> knownIn = new HashSet<>();
//        LinkedList<Character> knownOut = new LinkedList<>();
        Set<Character> knownOut = new HashSet<>();
//        Set<Character> knownTogether = new HashSet<>();
        LinkedList<Turn> Turns = new LinkedList<>();

        //  SETUP: Load database tables...
        Insert.loadKnownWords();
        Connect.watson("createWordPairsTable");
        Connect.watson("deleteDups");

        //  SETUP: Use letter counts accumulated during database entry to SORT a list of letters based on how frequently they appear in the database...
        unknown.sort();
        unknown.loadSortedLetters(Unknown.letters);

        //  PLAY the game...
        String lastGuess = null;
        boolean guessIsWord = false;
        Messages.play();

        do {
            Messages.report(knownIn, knownOut, pairs, Turns, unknown);  //  PRINT a report of possible determinations
            Messages.results(pairs, Turns, unknown);                             //  PRINT the results of previous plays and determinations

            Turn turn = new Turn(Keyboard.guess(), Keyboard.responseFromOpponent());  //  Take a turn

            //  Take action, based on the response...
             if(turn.response == 5) {
                 Turns.add(turn);
                 lastGuess = turn.guess;
                 AllTurns.makeDeterminations(Turns, pairs, knownIn, knownOut, unknown);
                break;
            } else if(!(turn.response == 0)) {
                 lastGuess = turn.guess;
                 //  Process turn (remove kI/kO, if all kT & updatedResp < kT, all kT IS OUT) here
                 AllTurns.updateTurn(turn, knownIn, knownOut, pairs, unknown, Turns);
                 // if(updatedResponse == 0) AllTurns.responseOfZero(turn, knownOut, unknown, Turns, pairs, knownIn);
                 Turns.add(turn);
            } else {
                 lastGuess = turn.guess;
                 AllTurns.responseOfZero(turn, knownOut, unknown, Turns, pairs, knownIn);
             }

            if(Turns.size() >= 2) AllTurns.makeDeterminations(Turns, pairs, knownIn, knownOut, unknown);  //  ANALYZE all previous guesses (now that a new guess and response are available)

            Messages.reportNumber++;

            int numWordPairsRemaining = (Integer) Connect.watson("countWordPairs");
            if (numWordPairsRemaining == 0) break;

        } while (Insert.wordCount > 3);

        //  Response is 5 BUT last guess is NOT opponents word!!!
        guessIsWord = Keyboard.verify(lastGuess);

        if(guessIsWord) {
            Messages.victorySummary(lastGuess);
        } else {

            LinkedList<String> lastWords = new LinkedList<>();

            ResultSet rs = Query.select("select * from Words_tbl where word != '" + lastGuess + "'");

            while(rs.next()) {
                lastWords.add(rs.getString(1));
            }

            Connect.watson("deleteFromWordsTable");

            System.out.println("\n*****************************************************************  END GAME  *******************************************************************************************");
            do {
                if(lastWords.isEmpty()) break;

//                guessIsWord = Keyboard.verify(lastGuess);
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
                System.out.println("Ya got me!  I'm stumped (this time)!  But I'm adding your word to my database, so the next time I run I KNOW YOUR WORD!  What was your word?:");

                lastGuess = Keyboard.enterUnknownWord();

                try {
                    Files.write(Paths.get("C:/Users/Bob/IdeaProjects/i-Watson2dot1/FiveLetterWords.txt"), ("\n" + lastGuess).getBytes(), StandardOpenOption.APPEND);
                }catch (IOException e) {
                    //exception handling left as an exercise for the reader
                }
                System.out.println(" > Added " + lastGuess + " to the data file used to generate the watson database.");
            }
        }
    }
}