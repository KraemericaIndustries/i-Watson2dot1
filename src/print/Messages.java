package print;

import dataStructures.LetterGroup1D;
import dataStructures.LetterGroup2D;

public class Messages {

    //  Introduce the game, and how it is played...
    public static void welcome() {

        System.out.println("*****************************************************************  WELCOME  *******************************************************************************************");
        System.out.println("""
                Welcome to the Word Guessing Game Helper!

                Your opponent will  choose a familiar 5 letter word, with each letter appearing ONLY ONCE. (Valid: 'GLYPH'.  Invalid: 'DROOP')
                See if you can guess the word!  Each time you make a guess, your opponent will respond with a number.
                The number represents the number of letters in your guess that appear in the word chosen by your opponent.
                Example:  If the opponent chooses 'LOSER' and you guess 'POSER' the response would be 4 ('O' makes 1, 'S' makes 2, 'E' makes 3, and 'R' makes 4.)
                Will you be able to identify your opponent's word?
                I'm going to help - by suggesting your most strategic plays possible!
                First - let me get myself set up...""");
        System.out.println("***********************************************************************************************************************************************************************");
        System.out.println();
    }

    //  START the game...
    public static void play() {
        System.out.println("*****************************************************************  THE GAME  ******************************************************************************************");
        System.out.println("Let's play!!!");
        System.out.println("***********************************************************************************************************************************************************************");
        System.out.println();
    }

    //  PRINT a report...
    public static void report(LetterGroup1D knownIn, LetterGroup1D knownOut, LetterGroup1D knownTogether, LetterGroup2D frequency, LetterGroup2D turns) {
        System.out.println("*****************************************************************  REPORT # " + (turns.playIndex + 1) + " *****************************************************************************************");

//        if(numTurns > 1) dataStructures.Matrix.analyzeAllTurns();
//        if(numTurns >=2 ) {
//            System.out.println("The number of letters changed in the previous two guesses is: " + Matrix.numLettersChanged/2);
//            System.out.print("Letters changed in the previous 2 guesses are: ");
//            morph.MatrixRowTo.commaDelimitedString(3);
//        }

        //  Print the Matrix...
        knownIn.print("Known IN: ");
        knownOut.print("Known OUT: ");
        knownTogether.print("Known TOGETHER: ");
        frequency.printFrequency();
        turns.printTurns();
        System.out.println();
        System.out.println();
        System.out.println("There are " + transactSQL.Connect.watson("getNumWordsInDB") + " words remaining in the database.");

        System.out.println("***********************************************************************************************************************************************************************");
        System.out.println();
    }

    //  PRINT the result(s) of a given turn...
    public static void results(LetterGroup2D turns, LetterGroup1D knownTogether, LetterGroup2D frequency) {

//        int numGuesses = 0;

        System.out.println("*****************************************************************  RESULT # " + (turns.playIndex + 1) + " *****************************************************************************************");
        System.out.println("ANALYSIS:");
        System.out.println(" - Previous guesses for which there is data available: " + turns.turnIndex);


        System.out.println("ADVICE:");
        System.out.println(" - Make the first guess possible using the 5 most common letters possible");
        //  ToDo: IMPLEMENT the getWords() method
//  SAMPLE recursive sql select...

        System.out.println(" - Searching the database, I suggest guessing: " + transactSQL.Connect.watson("getWords", knownTogether, frequency));  //  CONNECT to DB (to get guesses)
        System.out.println("***********************************************************************************************************************************************************************");
    }

//  ToDo: This is permitted to linger as a reference for future (re)implementation as needed...
//
//    public static void endGame(String guess, int counter) throws SQLException {
//
//        int count = 0;  //  Counter for the number of words remaining in the DB
//        boolean guessIsOpponentsWord;
//
//        System.out.println("The response was 5!!!");
//
//        do {
//
//
////            do {
//            guessIsOpponentsWord = isGuessOpponentsWord(guess);
//
//            if (guessIsOpponentsWord) {
//                System.out.println("Game over man!!!  The opponents word was determined in " + (counter - 1) + " turns.");
//                break;
//            } else {
//                //  DELETE all words that DO NOT contain the letters from the previous guess from the DB...
//                String query = "delete from Words_tbl where word NOT like '%" +
//                        guess.charAt(0) +
//                        "%' or word NOT like '%" +
//                        guess.charAt(1) +
//                        "%' or word NOT like '%" +
//                        guess.charAt(2) +
//                        "%' or word NOT like '%" +
//                        guess.charAt(3) +
//                        "%' or word NOT like '%" +
//                        guess.charAt(4) +
//                        "%'";
//                System.out.println("Deleting ALL words that DO NOT contain the letters in '" + guess + "' from the database...");
//                System.out.println(Database.statement(query) + " rows DELETED from the database.");
//
//                //  COUNT the number of words remaining in the DB...
//                ResultSet resultSet = Database.select("select count (*) from Words_tbl");  //  Execute the statement object
//                //  Process the result
//                while(resultSet.next()) {
//                    count =  ((Number) resultSet.getObject(1)).intValue();
//                }
//                System.out.println(count + " word(s) remaining in the database.");
//                System.out.println();
//
//                // PROCESS of ELIMINATION...
//                System.out.println("In the event the opponent advises that the previous guess (" + guess + ") is NOT their word, all that remains to be done is process of elimination.");
//                System.out.println("Deleting the previous guess (" + guess + ") from the database...");
//                System.out.println(Database.statement("delete from Words_tbl where word = '" + guess + "'") + " row(s) DELETED from the database.");
//
//
//
//                //  COUNT the number of words remaining in the DB...
//                resultSet = Database.select("select count (*) from Words_tbl");  //  Execute the statement object
//                //  Process the result
//                while(resultSet.next()) {
//                    count =  ((Number) resultSet.getObject(1)).intValue();
//                }
//
//                if(count == 0) break;
//
//                System.out.println();
//                System.out.println("Here are all the OTHER words in the database that can be made from these 5 letters:");
//                try {
//                    resultSet = Database.select("select * from Words_tbl");  //  Execute the statement object
//                    //  Process the result
//                    while(resultSet.next()) {  //  ITERATE over all results returned from the SELECT statement
//                        String word = resultSet.getString("word");  //  SET the value of the local String variable named word to the last value returned by the SQL select statement
//                        System.out.print(word + "\t");
//                    }
//                    System.out.println();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("Guess each of the above (in turn) to arrive at the answer.  ");
//                guess = Type.guess(counter);
//            }
//            counter++;
////            } while (!guessIsOpponentsWord);
//        } while (count != 0);
//
//        System.out.println("Ya got me!  I'm stumped (this time)!  But I'm adding your word to my database, so the next time I run I KNOW YOUR WORD!  What was your word?:");
//
//
//
//        guess = Turn.enterGuess();
//
//
//
//
//        try {
//            Files.write(Paths.get("C:/Users/bkraemer/OneDrive - Topcon/Development/Java/IntelliJ/i-Watson2dot0/FiveLetterWords.txt"), ("\n" + guess).getBytes(), StandardOpenOption.APPEND);
//        }catch (IOException e) {
//            //exception handling left as an exercise for the reader
//        }
//        System.out.println(" > Added " + guess + " to the data file used to generate the watson database.");
//    }
}