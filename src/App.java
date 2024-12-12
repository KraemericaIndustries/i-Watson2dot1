import assess.AllTurns;
import dataStructures.*;
import print.Messages;
import transactSQL.Insert;

import java.util.LinkedList;

public class App {

    public static void main(String[] args) throws Exception {

        //  SETUP...
        print.Messages.welcome();
        transactSQL.DatabaseConnection.getProperties();
        transactSQL.Create.watsonDB();

        //  SETUP: Create LetterGroup objects to facilitate play...
        Unknown unknown = new Unknown();  //  -> Sorting Unknown letters by number of occurrences in the database is done via a stream, requiring an instance of the class
        LetterGroup knownIn = new LetterGroup();
        LetterGroup knownOut = new LetterGroup();
        LetterGroup knownTogether = new LetterGroup();
        LinkedList<Turn> Turns = new LinkedList<>();

        //  SETUP: Load database tables...
        transactSQL.Insert.loadKnownWords();
        transactSQL.Connect.watson("createWordPairsTable");
        transactSQL.Connect.watson("deleteDups");
        transactSQL.Connect.watson("countWordPairs");

        //  SETUP: Use letter counts accumulated during database entry to SORT a list of letters based on how frequently they appear in the database...
        unknown.sort();
        unknown.loadSortedLetters(Unknown.letters);

        //  PLAY the game...
        print.Messages.play();

        do {
            print.Messages.report(knownIn, knownOut, knownTogether, Turns, unknown);  //  PRINT a report of possible determinations
            print.Messages.results(knownTogether, Turns);                             //  PRINT the results of previous plays and determinations

            //  Take a turn...
            Turn turn = new Turn(read.Keyboard.guess(), read.Keyboard.responseFromOpponent());

            //  Take action, based on the response...
            if(!(turn.response == 0)) Turns.add(turn);
            else AllTurns.responseOfZero(turn, knownOut, unknown, Turns, knownTogether, knownIn);

            //  ANALYZE all previous guesses (now that a new guess and response are available)...
            if(Turns.size() >= 2) AllTurns.makeDeterminations(Turns, knownTogether, knownIn, knownOut, unknown);

            Messages.reportNumber++;                                                  //  INCREMENT the number of turns taken
        } while (Insert.wordCount > 3);                                               //  While the most recent response is less than 5 (ToDo: RATES, STARE, TEARS remaining?... more logic needed to anticipate this.)

        //  **END GAME***
        System.out.println("Game over man!!!  The opponents word was determined in " + (Messages.reportNumber - 1) + " turns!");
    }
}