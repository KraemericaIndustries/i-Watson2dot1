public class App {
    public static void main(String[] args) throws Exception {

        print.Messages.welcome();
        transactSQL.DatabaseConnection.getProperties();
        transactSQL.Create.watsonDB();
        dataStructures.Matrix.initialize();
        transactSQL.Insert.loadKnownWords();
        //  transactSQL.Query.wordsFromDB();
        print.Messages.play();

        do {
            if(dataStructures.Matrix.numTurns == 0) {
                print.Messages.report(dataStructures.Matrix.numTurns);  //  On the first iteration, PRINT a report
                print.Messages.strategies();                            //  On the first iteration, PRINT general strategy
            }
            dataStructures.GuessTable.push();     //  Pre-populate a table of probable guesses to minimize interaction with the DB
            print.Messages.specificStrategies();  //  PRINT specific strategies based on analyze.Report.previousGuesses()
            read.Keyboard.guess();                //  TYPE a guess
            read.Keyboard.response();             //  TYPE the response
            print.Messages.report(dataStructures.Matrix.numTurns);  //  PRINT a report
        } while (dataStructures.Matrix.truthTable[dataStructures.Matrix.turnIndex-1][27] < 5);  //  While the most recent response is less than 5

        //  **END GAME***
//        print.Messages.endGame(guess, numTurns);  //  Once the response to the previous guess is 5...
    }
}