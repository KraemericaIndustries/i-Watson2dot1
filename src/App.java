import assess.AllTurns;
import dataStructures.*;
import print.Messages;
import read.Keyboard;
import transactSQL.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;


public class App {

    public static void main(String[] args) throws Exception {

        //  SETUP...
        Messages.welcome();
        DatabaseConnection.getProperties();
        Create.copilotWatsonDB();

        //  SETUP: Create datastore objects to facilitate play...
        LinkedList<Turn> Turns = new LinkedList<>();

        //  CREATE a dashboard...
        Dashboard dashboard = new Dashboard();

        //  SETUP: Load database tables...
        Insert.loadKnownWords(dashboard);
        dashboard.buildUnknownLettersList();
        dashboard.sortUnknownLettersByFrequencyDescending();
        Connect.watson("createWordPairsTable");

        //  PLAY the game...
        String latestGuess = "ABC";
        int latestResponse = 0;
        boolean guessIsWord;
        Messages.play();

        do {
            dashboard.printDashboard(Turns);                               //  PRINT the dashboard
            List<String> guesses = fetch.Play.nextPlay(dashboard, Turns);  //  FETCH the next play

            for(String g : guesses) {                                      //  ITERATE all guesses, submit the guess, prompt for the response, add the play to 'Turns' collection
                System.out.println("Your guess is: " + g);                 //  VERIFY the guess
                latestGuess = g;
                latestResponse = Keyboard.responseFromOpponent();          //  GET the latest response
                if(latestResponse ==5) break;                              //  BREAK the loop if the latest response is 5
                Turn turn = new Turn(g, latestResponse);                   //  CREATE a turn from a guess
                Turns.add(turn);                                           //  ADD the turn to the Turns collection
            }

            AllTurns.compareAllTurnsAgainstEachOther(Turns, dashboard);    //  COMPARE all previous turns against each other

            dashboard.reportNumber++;                                      //  INCREMENT the report number (lets us know how many turns we've taken)

        } while (!(latestResponse == 5));                                  //  WHILE the latest response is not 5

        //  THE HOME STRETCH.  (The latest response was 5, but the last guess is NOT the word)
        guessIsWord = Keyboard.verify(latestGuess);

        if(guessIsWord) {
            Messages.victorySummary(latestGuess);
        } else if (Insert.wordCount == 0) {
            stumped();
        } else {
            System.out.println();
            LinkedList<String> lastWords = new LinkedList<>();

            ResultSet rs = Query.select("select * from Words_tbl where word != '" + latestGuess + "'");

            while(rs.next()) {
                lastWords.add(rs.getString(1));
            }

            Connect.watson("deleteFromWordsTable");

            System.out.println("\n*****************************************************************  END GAME  *******************************************************************************************\n");
            do {
                if(lastWords.isEmpty()) break;

                if(guessIsWord) {
                    break;
                } else {

                    Messages.reportNumber++;
                    System.out.println("Here are the last remaining words in the database:\n" + lastWords);
                    System.out.println("Make a guess from the choices, above.  ");
                    latestGuess = Keyboard.guess();
                    guessIsWord = Keyboard.verify(latestGuess);
                    lastWords.remove(latestGuess);
                }
            } while (!(lastWords.isEmpty()));

            if(guessIsWord) {
                Messages.victorySummary(latestGuess);
            } else {
                stumped();
            }
        }
    }

    private static void stumped() {
        String lastGuess;
        System.out.println("\nYa got me!  I'm stumped (this time)!  But I'm adding your word to my database, so the next time I run I KNOW YOUR WORD!  What was your word?:");

        lastGuess = Keyboard.enterUnknownWord();

        try {
            Files.write(Paths.get("C:/Users/Bob/IdeaProjects/i-Watson2dot1/FiveLetterWords.txt"), ("\n" + lastGuess).getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        System.out.println(" > Added " + lastGuess + " to the data file used to generate the watson database.");
    }
}