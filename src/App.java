import dataStructures.LetterGroup1D;
import dataStructures.LetterGroup2D;

import static dataStructures.Matrix.numTurns;

public class App {
    public static void main(String[] args) throws Exception {

        //  SETUP...
        print.Messages.welcome();
        transactSQL.DatabaseConnection.getProperties();
        transactSQL.Create.watsonDB();

        //  CREATE LetterGroup Objects to facilitate play...
        LetterGroup1D knownIn = new LetterGroup1D(5);
        LetterGroup1D knownOut = new LetterGroup1D(21);
        LetterGroup1D unknown = new LetterGroup1D(26);
        LetterGroup1D knownTogether = new LetterGroup1D(5);
        LetterGroup2D frequency = new LetterGroup2D();
        unknown.seed();
        frequency.seed();
//        //DEBUG:
//        frequency.set(0,0,1);
//        frequency.set(1,2,3);

//        dataStructures.Matrix.initialize();
        transactSQL.Insert.loadKnownWords(frequency);
        frequency.sortByFrequency();
        frequency.print();
        dataStructures.Matrix.sortByFrequency();
        print.Messages.play();

        do {
            if(numTurns ==0) print.Messages.report(numTurns);  //  PRINT the report
            print.Messages.results(numTurns);                  //  PRINT the results
            read.Keyboard.guess();                             //  TYPE a guess
            read.Keyboard.response();                          //  TYPE the response
            //  ToDo: dataStructures.Matrix.sanitizeLastTurn() needed
            print.Messages.report(numTurns);                   //  PRINT a report
        } while (dataStructures.Matrix.truthTable[dataStructures.Matrix.turnIndex-1][27] < 5);  //  While the most recent response is less than 5

        //  **END GAME***
//        print.Messages.endGame(guess, numTurns);  //  Once the response to the previous guess is 5...
    }
}