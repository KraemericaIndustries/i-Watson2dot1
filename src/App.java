import dataStructures.LetterGroup1D;
import dataStructures.LetterGroup2D;

import static dataStructures.Matrix.numTurns;

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
        LetterGroup2D frequency = new LetterGroup2D(2, 27);
        LetterGroup2D turns = new LetterGroup2D(10, 27);

        //  SETUP: Populate tables with initial letter values...
        unknown.seed();
        frequency.seed();

        //  SETUP: Load tables...
        transactSQL.Insert.loadKnownWords(frequency);
        frequency.sortByFrequency();
        print.Messages.play();

        do {
            if(turns.playIndex == 0) print.Messages.report(knownIn, knownOut, knownTogether, frequency, turns);  //  PRINT the report
            print.Messages.results(turns, knownTogether, frequency);      //  PRINT the results
            read.Keyboard.guess();                             //  TYPE a guess
            read.Keyboard.response();                          //  TYPE the response
            //  ToDo: dataStructures.Matrix.sanitizeLastTurn() needed
//            print.Messages.report(numTurns);                   //  PRINT a report
        } while (dataStructures.Matrix.truthTable[dataStructures.Matrix.turnIndex-1][27] < 5);  //  While the most recent response is less than 5

        //  **END GAME***
//        print.Messages.endGame(guess, numTurns);  //  Once the response to the previous guess is 5...
    }
}