import dataStructures.Frequency;
import dataStructures.LetterGroup1D;
import dataStructures.LetterGroup2D;
import dataStructures.Turn;
import print.Messages;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class App {
    public static void main(String[] args) throws Exception {

        //  SETUP:
        print.Messages.welcome();
        transactSQL.DatabaseConnection.getProperties();
        transactSQL.Create.watsonDB();

        //  SETUP: Create LetterGroup objects to facilitate play...
        LetterGroup1D knownIn = new LetterGroup1D(5);
        LetterGroup1D knownOut = new LetterGroup1D(21);
        LetterGroup1D unknown = new LetterGroup1D(26);
        LetterGroup1D knownTogether = new LetterGroup1D(5);
        Frequency frequency = new Frequency();
        LinkedList<Turn> Turns = new LinkedList<>();

        //  SETUP: Populate tables with initial letter values...
        unknown.seed();

        //  SETUP: Load tables...
        transactSQL.Insert.loadKnownWords(frequency);
        frequency.loadLinkedHashMap();
        frequency.sort();
        System.out.println(frequency.frequency);

        //  PLAY the game...
        print.Messages.play();

        do {
//            if(Turns.isEmpty()) print.Messages.report(knownIn, knownOut, knownTogether, frequency, Turns);  //  PRINT a report of possible determinations
//            print.Messages.report(knownIn, knownOut, knownTogether, frequency, Turns);  //  PRINT a report of possible determinations
//            print.Messages.results(knownTogether, frequency, Turns);                                        //  PRINT the results of previous plays and determinations
//            Turns.add(new Turn(read.Keyboard.guess(), read.Keyboard.responseFromOpponent(), frequency));    //  TAKE a turn by making a guess
            Messages.reportNumber++;                                                                        //  INCREMENT the number of turns taken
        } while (Turns.getLast().response < 5);  //  While the most recent response is less than 5

        //  **END GAME***
//        print.Messages.endGame(guess, numTurns);  //  Once the response to the previous guess is 5...
    }
}