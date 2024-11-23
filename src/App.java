import assess.AllTurns;
import dataStructures.*;
import print.Messages;
import transactSQL.Connect;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class App {

    public static void main(String[] args) throws Exception {

        //  SETUP...
        print.Messages.welcome();
        transactSQL.DatabaseConnection.getProperties();
        transactSQL.Create.watsonDB();

        //  SETUP: Create LetterGroup objects to facilitate play...
        Unknown unknown = new Unknown();  //  Is this even necessary? -> Sorting Unknown is done via a stream, requiring an instance of the class
        LetterGroup knownIn = new LetterGroup();
        LetterGroup knownOut = new LetterGroup();
        LetterGroup knownTogether = new LetterGroup();
        LinkedList<Turn> Turns = new LinkedList<>();

        //  SETUP: Load tables...
        transactSQL.Insert.loadKnownWords();
        unknown.sort();
        unknown.loadSortedLetters(Unknown.letters);

        //  STRATEGY #1: Attack all words that only differ by the most common letter possible...
        TreeMap<String, String> wordPairsThatDifferByOneLetter = new TreeMap<>();  //  CREATE a map for all words differing by 1 letter
        Connect.watson("getAllWordsThatDifferByOneLetter", wordPairsThatDifferByOneLetter);
        System.out.println();

        //  PLAY the game...
        print.Messages.play();

        do {
            print.Messages.report(knownIn, knownOut, knownTogether, Turns, unknown);           //  PRINT a report of possible determinations
            print.Messages.results(knownTogether, unknown, Turns);                             //  PRINT the results of previous plays and determinations

            //  STRATEGY #1
            if(!wordPairsThatDifferByOneLetter.isEmpty()) {
                System.out.println("STRATEGY #1:  DETERMINE if the most frequently occurring UNKNOWN letter in the database is IN or OUT");
                System.out.println("I suggest playing the following words, on consecutive turns:");
//                System.out.println("The first letter to consider is: " + unknown.sortedLetters[0]);  //  WORKING!!!

                for (Map.Entry<String, String> entry : wordPairsThatDifferByOneLetter.entrySet()) {
                    if(entry.getKey().contains("A")) System.out.println("[" + entry.getKey() + ", " + entry.getValue() + "]");
                }



                System.out.println();

            }

            Turns.add(new Turn(read.Keyboard.guess(), read.Keyboard.responseFromOpponent()));  //  TAKE a turn by making a guess

            if(Turns.size() >= 2) AllTurns.makeDeterminations(Turns, knownTogether, knownIn, knownOut, unknown);

            Messages.reportNumber++;                                                           //  INCREMENT the number of turns taken
        } while (Turns.getLast().response < 5);                                                //  While the most recent response is less than 5

        //  **END GAME***
//        print.Messages.endGame(guess, numTurns);  //  Once the response to the previous guess is 5...
    }
}