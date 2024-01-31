import dataStructures.*;
import print.Messages;

import java.util.LinkedList;

public class App {

    public static void main(String[] args) throws Exception {

        //  SETUP:
        print.Messages.welcome();
        transactSQL.DatabaseConnection.getProperties();
        transactSQL.Create.watsonDB();

        //  SETUP: Create LetterGroup objects to facilitate play...
        Unknown unknown = new Unknown();
        LetterGroup knownIn = new LetterGroup();
        LetterGroup knownOut = new LetterGroup();
        LetterGroup knownTogether = new LetterGroup();
        LinkedList<Turn> Turns = new LinkedList<>();

        //  SETUP: Load tables...
        transactSQL.Insert.loadKnownWords(unknown);
        unknown.sort();
        unknown.loadSortedLetters(unknown.letters);
//        System.out.println("Unknown: " + unknown.letters);
//
//        System.out.print("sortedLetters[] of 'Unknown': ");
//        for(int i = 0; i < unknown.sortedLetters.length; i++) {
//            System.out.print(unknown.sortedLetters[i]);
//        }
//        System.out.println();

        //  PLAY the game...
        print.Messages.play();

        do {
//            if(Turns.isEmpty()) print.Messages.report(knownIn, knownOut, knownTogether, unknown, Turns);  //  PRINT a report of possible determinations
            print.Messages.report(knownIn, knownOut, knownTogether, unknown, Turns);  //  PRINT a report of possible determinations
            print.Messages.results(knownTogether, unknown, Turns);                                        //  PRINT the results of previous plays and determinations
            Turns.add(new Turn(read.Keyboard.guess(), read.Keyboard.responseFromOpponent(), unknown));    //  TAKE a turn by making a guess
            Messages.reportNumber++;                                                                        //  INCREMENT the number of turns taken
        } while (Turns.getLast().response < 5);  //  While the most recent response is less than 5

        //  **END GAME***
//        print.Messages.endGame(guess, numTurns);  //  Once the response to the previous guess is 5...
    }
}