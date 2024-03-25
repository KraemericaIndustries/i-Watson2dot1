package controllers;

import models.LetterGroup;
import models.Turn;
import models.Unknown;
import views.Report;
import views.Result;

import java.util.LinkedList;

import static controllers.Create.createWatsonDB;
import static controllers.DatabaseConnection.getDatabaseServerProperties;
import static views.LetsPlay.printLetsPlay;
import static views.Welcome.printWelcomeMessage;

public class App {

    public static void main(String[] args) throws Exception {

        //  SETUP...
        printWelcomeMessage();
        getDatabaseServerProperties();
        createWatsonDB();

        //  SETUP: Create LetterGroup objects to facilitate play...
        Unknown unknown = new Unknown();  //  Is this even necessary? -> Sorting Unknown is done via a stream, requiring an instance of the class
        LetterGroup knownIn = new LetterGroup();
        LetterGroup knownOut = new LetterGroup();
        LetterGroup knownTogether = new LetterGroup();
        LinkedList<Turn> Turns = new LinkedList<>();

        //  SETUP: Load tables...
        Insert.loadKnownWords();
        unknown.sort();
        unknown.loadSortedLetters(Unknown.letters);

        //  PLAY the game...
        printLetsPlay();

        do {
            Report.report(knownIn, knownOut, knownTogether, Turns, unknown);           //  PRINT a report of possible determinations
            Result.results(knownIn, knownOut, knownTogether, unknown, Turns);                             //  PRINT the results of previous plays and determinations
            Turns.add(new Turn(Keyboard.guess(), Keyboard.responseFromOpponent()));    //  TAKE a turn by making a guess

            if(Turns.size() >= 2) AllTurns.compareAllTurns(Turns, knownTogether, knownIn, knownOut, unknown);

            Report.reportNumber++;                                                             //  INCREMENT the number of turns taken
        } while (Turns.getLast().response < 5);                                                //  While the most recent response is less than 5

        //  **END GAME***
//        print.Messages.endGame(guess, numTurns);  //  Once the response to the previous guess is 5...
    }
}