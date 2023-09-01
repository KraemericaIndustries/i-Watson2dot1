public class App {
    public static void main(String[] args) throws Exception {

        String guess;
        int response;
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
            //            String mostToLeastFrequentLetters = Messages.report(numTurns);  //  PRINT The Matrix.  Return the letter counts from the database sorted from most to least frequent in a SET.
//            Messages.printGeneralStrategies();
//            words = reportAnalysis(mostToLeastFrequentLetters);  //  ASSESS the Report, suggest strategies, take action (Matrix.size == 0. Matrix.size == 1 and so on).
            print.Messages.strategies(numTurns);





            guess = read.Keyboard.guess();                        //  TYPE a guess
            response = read.Keyboard.response();                  //  TYPE the response
            numTurns++;                                          //  INCREMENT the counter for the number of turns taken.
//            insertTurn(guess, response);                         //  Take a turn, and INSERT it into the Matrix.
//            ResultEngine.printResults(words, numTurns);          //  REWORK NEEDED!! PRINT AND ASSESS AGAINST MATRIX, NOT to the DB.
            
        } while (response < 5);

        //  **END GAME***
//        print.Messages.endGame(guess, numTurns);  //  Once the response to the previous guess is 5...
    }
}