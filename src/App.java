import static dataStructures.Matrix.numTurns;

public class App {
    public static void main(String[] args) throws Exception {

        print.Messages.welcome();
        transactSQL.DatabaseConnection.getProperties();
        transactSQL.Create.watsonDB();
        dataStructures.Matrix.initialize();
        transactSQL.Insert.loadKnownWords();
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