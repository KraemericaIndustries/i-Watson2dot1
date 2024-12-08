import assess.AllTurns;
import dataStructures.*;
import print.Messages;
import java.util.LinkedList;


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

        //  SETUP: Load database tables...
        transactSQL.Insert.loadKnownWords();
        transactSQL.Connect.watson("createWordPairsTable", 0, 'T', 'O', 'K', 'E', 'N');
        transactSQL.Connect.watson("deleteDups", 0, 'T', 'O', 'K', 'E', 'N');
        transactSQL.Connect.watson("countWordPairs", 0, 'T', 'O', 'K', 'E', 'N');

        //  SETUP: Use letter counts accumulated during database entry to SORT a list of letters based on how frequently they appear in the database...
        unknown.sort();
        unknown.loadSortedLetters(Unknown.letters);

        //  PLAY the game...
        print.Messages.play();

        do {
            print.Messages.report(knownIn, knownOut, knownTogether, Turns, unknown);           //  PRINT a report of possible determinations
            print.Messages.results(knownTogether, unknown, Turns);                             //  PRINT the results of previous plays and determinations

            Turn turn = new Turn(read.Keyboard.guess(), read.Keyboard.responseFromOpponent());
            if(!(turn.response == 0)) Turns.add(turn);
            else AllTurns.responseOfZero(turn, knownOut, unknown, Turns, knownTogether);

            if(Turns.size() >= 2) AllTurns.makeDeterminations(Turns, knownTogether, knownIn, knownOut, unknown);

            Messages.reportNumber++;                                                           //  INCREMENT the number of turns taken
        } while (Turns.getLast().response < 5);                                                //  While the most recent response is less than 5

        //  **END GAME***
//        print.Messages.endGame(guess, numTurns);  //  Once the response to the previous guess is 5...
    }
}