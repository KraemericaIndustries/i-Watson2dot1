import dataStructures.Matrix;

public class App {
    public static void main(String[] args) throws Exception {

        int numTurns = 0;

        print.Messages.welcome();
        transactSQL.DatabaseConnection.getProperties();
        transactSQL.Create.watsonDB();
        transactSQL.Insert.loadKnownWords();
        dataStructures.Matrix.initialize();
        //        transactSQL.Query.wordsFromDB();
        print.Messages.play();

        do {
            print.Messages.report(numTurns);
            print.Messages.strategies(numTurns);
            print.Messages.specificStrategies();

            read.Keyboard.guess();                        //  TYPE a guess
            read.Keyboard.response();                  //  TYPE the response
            print.Messages.report(numTurns);
//            insertTurn(guess, response);                         //  Take a turn, and INSERT it into the Matrix.
//            ResultEngine.printResults(words, numTurns);          //  REWORK NEEDED!! PRINT AND ASSESS AGAINST MATRIX, NOT to the DB.
            
        } while (Matrix.truthTable[Matrix.turnIndex-1][27] < 5);

        //  **END GAME***
//        print.Messages.endGame(guess, numTurns);  //  Once the response to the previous guess is 5...
    }
}