package print;

import dataStructures.*;
import transactSQL.Connect;
import transactSQL.Select;

import java.sql.SQLException;
import java.util.*;

public class Messages {

    public static int reportNumber = 1;
    public static int numWordPairs = 0;

    //  Introduce the game, and how it is played...
    public static void welcome() {
        System.out.println("""
                *****************************************************************  WELCOME  *******************************************************************************************
                Welcome to the Word Guessing Game Helper!

                Your opponent will  choose a familiar 5 letter word, with each letter appearing ONLY ONCE. (Valid: 'GLYPH'.  Invalid: 'DROOP')
                See if you can guess the word!  Each time you make a guess, your opponent will respond with a number.
                The number represents the number of letters in your guess that appear in the word chosen by your opponent.
                Example:  If the opponent chooses 'LOSER' and you guess 'POSER' the response would be 4 ('O' makes 1, 'S' makes 2, 'E' makes 3, and 'R' makes 4.)
                Will you be able to identify your opponent's word?
                I'm going to help - by suggesting your most strategic plays possible!
                First - let me get myself set up...
                ***********************************************************************************************************************************************************************
                """);
    }

    //  START the game...
    public static void play() {
        System.out.println("""
        *****************************************************************  THE GAME  ******************************************************************************************
        Let's play!!!
        ***********************************************************************************************************************************************************************
        """);
    }

    //  PRINT a report...
    public static void report(Set<Character> knownIn, Set<Character> knownOut, LinkedList<Turn> Turns) {
        System.out.println("*****************************************************************  REPORT # " + reportNumber + " *****************************************************************************************");

        //  PRINT the LinkedHashMaps...
        System.out.println("Known IN: " + knownIn);
        System.out.println("Known OUT: " + knownOut);
        System.out.println("Unknown: " + Unknown.letters + "\n");

        prettyPrintPreviousGuesses(Turns);  //  PRINT all previous guesses

        //  GET counts from database table to furnish report, and drive logic...
        int numWords = (int) Connect.watson("getNumWordsInDB");
        numWordPairs = (int) Connect.watson("countWordPairs");

        System.out.print("There are " + numWords + " words remaining in the database.\n");
        System.out.println("There are " + numWordPairs + " word pairs that differ by only 1 letter.");
        System.out.println("***********************************************************************************************************************************************************************\n");
    }

    //  PRINT the result(s) of a given turn...
    public static void results(LinkedList<Turn> Turns) throws SQLException {

        Object z = Connect.watson("countWordPairs");
        int numWordPairs = (int) z;

        System.out.println("*****************************************************************  RESULT # " + reportNumber + " *****************************************************************************************");
        System.out.println("ANALYSIS:");
        System.out.println(" - Previous guesses for which there is data available: " + Turns.size() + "\n");

        //  STRATEGY #1
        System.out.println("SUGGESTION:");
            if(numWordPairs < 6) {
                System.out.println("Condition: numWordPairs < 6\nTry to eliminate the most common letter among the remaining word pairs");
                try {
                    Select.lastNumWordPairs();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Try to eliminate the most common letter found in the database (which is " + Unknown.printFirstEntry() + ").\nConsider taking a pair of consecutive turns making these guesses:");
                Connect.watson();
            }
        System.out.println("***********************************************************************************************************************************************************************");
    }

    //  PRETTY-PRINT the original and updated previous turns taken...
    private static void prettyPrintPreviousGuesses(LinkedList<Turn> Turns) {

        //  https://www.geeksforgeeks.org/how-to-print-all-keys-of-the-linkedhashmap-in-java/
        //  System.out.println("print.Messages.prettyPrintPreviousGuesses(): BEGIN");
        System.out.println("Previous Guesses: ");

        for(Turn t : Turns) {
            System.out.println(t.guess + " = " + t.response + ".  We now know " + t.updatedResponse + " of [" + t.updatedGuess + "] are in your opponents word.");
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
}