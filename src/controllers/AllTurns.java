package controllers;

import models.LetterGroup;
import models.Turn;
import models.Unknown;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class AllTurns {

    public static void updateKnownTogether(LinkedList<Turn> Turns, LetterGroup knownTogether, int i, int j) {
//  ToDo THIS is where L is getting added to knownTogether
        knownTogether.letters.putAll(Turns.get(i).turn);
        knownTogether.letters.putAll(Turns.get(j).turn);

        Set<Character> turn1Keys = Turns.get(i).turn.keySet();

        for(Character c : turn1Keys) {
            if(Turns.get(i).turn.containsKey(c) && Turns.get(j).turn.containsKey(c)) knownTogether.letters.remove(c);
        }
    }

    public static void compareAllTurns(LinkedList<Turn> Turns, LetterGroup knownTogether, LetterGroup knownIn, LetterGroup knownOut, Unknown unknown) {

        System.out.println("+++ AllTurns.compareAllTurns +++");  //  PRINT DEBUG message

        LetterGroup letterChangedFrom = new LetterGroup();    //  DECLARE and INITIALIZE a LetterGroup for use in comparing turns
        LetterGroup letterChangedTo = new LetterGroup();      //  DECLARE and INITIALIZE a second LetterGroup for use in comparing turns
        int comparisonNumber = 1;                             //  DECLARE and INITIALIZE a comparisonNumber value

        for(int i = 0; i < Turns.size() - 1; i++) {      //  Take the FIRST turn in 'Turns' (then the second, then the third, up until the SECOND LAST Turn in 'Turns')
            for(int j = i + 1; j < Turns.size(); j++) {  //  Take the SECOND turn in 'Turns' (then the third, then the fourth, up until the LAST Turn in 'Turns')

                System.out.println("Comparison #" + comparisonNumber + ".  Now comparing turn #" + (i + 1) + " with turn #" + (j + 1) + ":");  //  PRINT a header
                prettyPrintTurn(Turns, i);  //  PRINT the first of 2 turns to be compared
                prettyPrintTurn(Turns, j);  //  PRINT the second of 2 turns to be compared
                identifyChangedLetters(Turns, letterChangedFrom, i, letterChangedTo, j);  //  IDENTIFY the letters changed between the 2 turns
                comparisonNumber++;  //  INCREMENT comparisonNumber

                if(Turns.get(i).updatedResponse == Turns.get(j).updatedResponse) {                  //  IF responses from compared turns are EQUAL...
                    System.out.println("+++ i.updatedResponse == j.updatedResponse +++");           //  PRINT DEBUG message
                    if(letterChangedTo.letters.size()==1 && letterChangedFrom.letters.size()==1) {  //  IF Only 1 letter has changed between turns...
                        System.out.println("Scenario: i.updatedResponse == j.updatedResponse + Only 1 letter changed between turns:");
                        System.out.println("We now know that " + letterChangedTo.letters + " and " + letterChangedFrom.letters + " are either both IN, or both OUT (but cannot be sure which is the case)\n");
                        updateKnownTogether(Turns, knownTogether, i, j);
                    } else {
                        System.out.println("More than 1 letter changed between these 2 turns.  No conclusions may be drawn.");
                    }

                    System.out.println("--- i.updatedResponse == j.updatedResponse ---");


                //  ELSE-IF responses from compared turns are + 1...
                } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == 1) {
                    System.out.println("Scenario: updatedResponse(i) - updatedResponse(j) = 1");
                    //  AND IF Only 1 letter has changed between turns...
                    if(letterChangedTo.letters.size()==1 && letterChangedFrom.letters.size()==1) {
                        System.out.println("Scenario: updatedResponse(i) - updatedResponse(j) = 1:");
                        System.out.println("With 1 letter changed, and the responses varying by 1, " + letterChangedFrom.letters + " is KNOWN IN, and " + letterChangedTo.letters + " is KNOWN OUT.  Updating all data sources...");
                        updateAllDataSources(Turns, knownIn, knownOut, letterChangedFrom, letterChangedTo, unknown, knownTogether);
                        System.out.println();
                    } else {
                        System.out.println("More than 1 letter changed between these 2 turns.  No conclusions may be drawn.");
                    }

                //  ELSE-IF responses from compared turns are + 1...
                } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == -1) {
                    System.out.println("updatedResponse(i) - updatedResponse(j) = -1");
                    //  AND IF Only 1 letter has changed between turns...
                    if(letterChangedTo.letters.size() == 1 && letterChangedFrom.letters.size() == 1) {
                        System.out.println("Scenario: updatedResponse(i) - updatedResponse(j) = -1:");
                        System.out.println("With 1 letter changed, and the responses varying by 1, " + letterChangedFrom.letters + " is KNOWN IN, and " + letterChangedTo.letters + " is KNOWN OUT.  Updating all data sources...\n");
                        updateAllDataSources(Turns, knownIn, knownOut, letterChangedFrom, letterChangedTo, unknown, knownTogether);
                    } else {
                        System.out.println("More than 1 letter changed between these 2 turns.  No conclusions may be drawn.");
                    }
                }
            }
            letterChangedFrom.letters.clear();
            letterChangedTo.letters.clear();
        }
        System.out.println("--- AllTurns.compareAllTurns ---");
    }

    private static void updateAllDataSources(LinkedList<Turn> Turns, LetterGroup knownIn, LetterGroup knownOut, LetterGroup letterChangedFrom, LetterGroup letterChangedTo, Unknown unknown, LetterGroup knownTogether) {

        //  UPDATE LHMs with determined letters
        knownIn.letters.putAll(letterChangedFrom.letters);
        knownOut.letters.putAll(letterChangedTo.letters);


        //  ALL letters in KnownOut to be removed from 'Unknown' & ALL 'TURNS' & the database...
        Set<Character> knownOutKeys = knownOut.letters.keySet();

        for(Character c: knownOutKeys) {
            Unknown.letters.remove(c);

            for(Turn t : Turns) {
                t.turn.remove(c);
            }

            try {
                Delete.wordsWith(c);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //  ALL letters in knownIn to be removed from unknown AND ALL TURNS while decrementing updatedResponse...
        Set<Character> knownInKeys = knownIn.letters.keySet();
        Set<Character> knownTogetherKeys = knownTogether.letters.keySet();

        for(Character c: knownInKeys) {
            for(Turn t : Turns) {
                if(t.turn.containsKey(c)) {
                    t.turn.remove(c);
                    t.updatedResponse--;
                }
            }
            //  ALL words NOT containing letters 'knownIn' deleted from the database
            try {
                Delete.wordsWithout(c);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Query.wordsFromDB();
            Insert.reloadKnownWords();
            unknown.sort();
            Unknown.letters.remove(c);  //  <-- Querying the DB re-populates 'Unknown' with 'knownIn' letters.  Remove them.
        }

        //  REMOVE letters 'knownTogether' from Unknown...
        for(Character c: knownTogetherKeys) {
            Unknown.letters.remove(c);
        }

    }

    private static void prettyPrintTurn(LinkedList<Turn> Turns, int i) {

//        System.out.println("+++ AllTurns.prettyPrintLinkedHashMap +++");

        StringBuilder sb = new StringBuilder();

        sb.append("[");

        for (Map.Entry<Character, Integer> ite : Turns.get(i).turn.entrySet()) {
            sb.append(ite.getKey()).append(", ");
        }

        sb.setLength(sb.length() - 2);
        sb.append("]");

        System.out.println(sb + " = " + Turns.get(i).updatedResponse);

//        System.out.println("--- AllTurns.prettyPrintLinkedHashMap ---");
    }
    private static void identifyChangedLetters(LinkedList<Turn> Turns, LetterGroup letterChangedFrom, int i, LetterGroup letterChangedTo, int j) {

        System.out.println("+++ AllTurns.identifyChangedLetters +++");  //  PRINT DEBUG message

        letterChangedFrom.letters.clear();  //  CLEAR LetterGroup LHMs used for comparison
        letterChangedTo.letters.clear();    //  CLEAR LetterGroup LHMs used for comparison

        letterChangedFrom.letters.putAll(Turns.get(i).turn);  //  PUT ALL letters of turn under comparison
        letterChangedTo.letters.putAll(Turns.get(j).turn);    //  PUT ALL letters of turn under comparison

        Set<Character> turnJkeys = Turns.get(j).turn.keySet();  //  GENERATE a keyset
        Set<Character> turnIkeys = Turns.get(i).turn.keySet();  //  GENERATE a keyset

        for(Character c : turnJkeys) {
            letterChangedFrom.letters.remove(c);  //  REMOVE all keys from THE OTHER TURN from this one (leaving only the letter in this turn, not appearing in the other turn)
        }

        for(Character c : turnIkeys) {
            letterChangedTo.letters.remove(c);  //  REMOVE all keys from THE OTHER TURN from this one (leaving only the letter in this turn, not appearing in the other turn)
        }
        System.out.println(letterChangedTo.letters + " was changed to " + letterChangedFrom.letters + " in these two turns");
        System.out.println("--- AllTurns.identifyChangedLetters ---");
    }
}