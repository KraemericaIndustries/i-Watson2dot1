package print;

import dataStructures.*;
import transactSQL.Connect;
import transactSQL.Select;

import java.sql.SQLException;
import java.util.*;

public class Messages {

    public static int reportNumber = 1;

    //  Introduce the game, and how it is played...
    public static void welcome() {
        //  ToDo: Convert to string block:
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
        System.out.println("***********************************************************************************************************************************************************************\n");
    }

    //  START the game...
    public static void play() {
        //  ToDo: Convert to string block:
        System.out.println("*****************************************************************  THE GAME  ******************************************************************************************");
        System.out.println("Let's play!!!");
        System.out.println("***********************************************************************************************************************************************************************\n");
    }

    //  PRINT a report...
    public static void report(Set<Character> knownIn, Set<Character> knownOut, Pairs pairs, LinkedList<Turn> Turns, Unknown unknown) {
        System.out.println("*****************************************************************  REPORT # " + reportNumber + " *****************************************************************************************");

    //  if(Turns.size() >= 2) assess.AllTurns.makeDeterminations(Turns, knownTogether, knownIn, knownOut, unknown);

    //  PRINT the LinkedHashMaps...
        System.out.println("Known IN: " + knownIn);
        System.out.println("Known OUT: " + knownOut);
        pairs.prettyPrintPairs();
        System.out.println("Unknown: " + Unknown.letters + "\n");

        //  PRINT all previous guesses...
        prettyPrintPreviousGuesses(Turns);

        //  GET counts from database table to furnish report, and drive logic...
        int numWords = (int) Connect.watson("getNumWordsInDB");
        int numWordPairs = (int) Connect.watson("countWordPairs");

        System.out.print("There are " + numWords + " words remaining in the database.\n");
        System.out.println("There are " + numWordPairs + " word pairs that differ by only 1 letter.");
        System.out.println("***********************************************************************************************************************************************************************\n");
    }

    //  PRINT the result(s) of a given turn...
    public static void results(Pairs pairs, LinkedList<Turn> Turns, Unknown unknown) throws SQLException {

        Object z = Connect.watson("countWordPairs");
        int numWordPairs = (int) z;

        System.out.println("*****************************************************************  RESULT # " + reportNumber + " *****************************************************************************************");
        System.out.println("ANALYSIS:");
        System.out.println(" - Previous guesses for which there is data available: " + Turns.size() + "\n");

        //  STRATEGY #1
        System.out.println("SUGGESTION:");

        //                        ref. var       LL

            if(!pairs.knownTogether.isEmpty()) {
                System.out.println("Condition: !knownTogether.isEmpty()");
                System.out.println("Try to make a determination on letters known to be together.");
                System.out.println("Consider a turn making any of these guesses:");
                Connect.watson(pairs);
            }  else if(numWordPairs < 6) {
                System.out.println("Condition: numWordPairs < 6");
                try {
                    Select.lastNumWordPairs();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if(Turns.size() <= 1) {
                System.out.println("Condition: Turns.size() <= 1");
                System.out.println(" - With 0 previous plays to draw information from, try to make a determination on the most commonly occurring letter in the database, which is: " + Unknown.printFirstEntry());
                System.out.println("Consider taking a pair of consecutive turns making these guesses:");
                Connect.watson(unknown);
            } else {
                System.out.println("Condition: else");
                System.out.println("Consider taking a pair of consecutive turns making these guesses:");
                Connect.watson(unknown);
            }
        System.out.println("***********************************************************************************************************************************************************************");
    }

    //  PRETTY-PRINT the original and updated previous turns taken...
    private static void prettyPrintPreviousGuesses(LinkedList<Turn> Turns) {

        //  https://www.geeksforgeeks.org/how-to-print-all-keys-of-the-linkedhashmap-in-java/
        //  System.out.println("print.Messages.prettyPrintPreviousGuesses(): BEGIN");

        System.out.println("Previous Guesses: ");

        for(Turn t : Turns) {
            StringBuilder test = new StringBuilder();
            test.append(t.guess).append(" = ").append(t.response).append(".  We now know ").append(t.updatedResponse).append(" of [").append(t.updatedGuess).append("] are in your opponents word.");
            System.out.println(test);
        }
        System.out.println();
        //  System.out.println("print.Messages.prettyPrintPreviousGuesses(): END");
    }

    //  SUCCESS!  The opponents word has been determined...
    public static void victorySummary(String lastGuess) {
        System.out.println("\nGame over man!!!  The opponents word was determined in " + (Messages.reportNumber - 1) + " turns!");
        System.out.println("Your opponents word was: " + lastGuess);
        System.out.println("It took you " + (Messages.reportNumber) + " turns to determine your opponents word!");
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