import assess.AllTurns;
import dataStructures.*;
import print.Messages;
import transactSQL.Connect;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class App {

    public static void main(String[] args) throws Exception {

        //  SETUP...
        print.Messages.welcome();
        transactSQL.DatabaseConnection.getProperties();
        transactSQL.Create.watsonDB();

        //  SETUP: Create LetterGroup objects to facilitate play...
        Unknown unknown = new Unknown();  //  Is this even necessary? -> Sorting Unknown is done via a stream, requiring an instance of the class
        LetterGroup knownIn = new LetterGroup();
        LetterGroup knownOut = new LetterGroup();
        LetterGroup knownTogether = new LetterGroup();
        LinkedList<Turn> Turns = new LinkedList<>();

        //  SETUP: Load tables...
        transactSQL.Insert.loadKnownWords();
        //  ToDo: Implement word pairs table
        //  transactSQL.Select.createPairsTable();
        transactSQL.Connect.watson("createWordPairsTable", 0, 'T', 'O', 'K', 'E', 'N');

        unknown.sort();
        unknown.loadSortedLetters(Unknown.letters);

        //  PLAY the game...
        print.Messages.play();

        do {
            print.Messages.report(knownIn, knownOut, knownTogether, Turns, unknown);           //  PRINT a report of possible determinations
            print.Messages.results(knownTogether, unknown, Turns);                             //  PRINT the results of previous plays and determinations

            Turns.add(new Turn(read.Keyboard.guess(), read.Keyboard.responseFromOpponent()));  //  TAKE a turn by making a guess

            if(Turns.size() >= 2) AllTurns.makeDeterminations(Turns, knownTogether, knownIn, knownOut, unknown);

            Messages.reportNumber++;                                                           //  INCREMENT the number of turns taken
        } while (Turns.getLast().response < 5);                                                //  While the most recent response is less than 5

        //  **END GAME***
//        print.Messages.endGame(guess, numTurns);  //  Once the response to the previous guess is 5...
    }
}