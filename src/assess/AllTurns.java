package assess;

import classify.Classification;
import dataStructures.IdentifiedLetters;
import dataStructures.Pairs;
import dataStructures.Turn;
import dataStructures.Unknown;
import transactSQL.*;

import java.sql.SQLException;
import java.util.*;

public class AllTurns {

    //  MAKE all possible determinations on ALL previous turns taken...
    public static void makeDeterminations(LinkedList<Turn> Turns, Pairs pairs, IdentifiedLetters knownIn, IdentifiedLetters knownOut, Unknown unknown) throws SQLException {

        System.out.println("assess.AllTurns.makeDeterminations(): BEGIN");

        //  Checks if any one knownTogether pair is knownIn, if yes, the other must be knownIn (because we only process turns where the response varies by 1)
        if(!pairs.knownTogether.isEmpty()) {
            pairs.checkPairsForKnownIn(knownIn, unknown);
            pairs.checkPairsForKnownOut(knownOut, unknown);
            for (Turn t : Turns) updateTurn(t, knownIn, knownOut, unknown, Turns);
        }

        //  ANY turn with an (updated) size equal to the (updated) response are KNOWN IN...
        checkAllTurnsForSizeEqualsUpdatedResponse(Turns, knownIn, unknown);

        //  ToDo: ALL Turns where updatedResponse == 0 and .size() > 0, ALL letters are OUT!!!
        for (Turn t : Turns) updatedResponseIsZero(t, knownOut, unknown, Turns);

        //  COMPARE each (updated) turn against each other...
        compareAllTurnsAgainstEachOther(Turns, pairs, knownIn, knownOut, unknown);
        System.out.println("assess.AllTurns.makeDeterminations: END");
    }

    public static void updateTurn(Turn turn, IdentifiedLetters knownIn, IdentifiedLetters knownOut, Unknown unknown, LinkedList<Turn> Turns) {

        //  FOR every character in knownIn
        for (Character c : knownIn.letters) {
            if (turn.turn.contains(c)) {     //  IF the turn contains the character
                System.out.println("Removing all 'KNOWN IN' (" + c + ") from guess of " + turn.turn + "...");
                turn.turn.remove(c);        //  REMOVE the character from the turn
                System.out.println("Decrementing response from " + turn.updatedGuess + " to " + (turn.updatedResponse - 1) + " to account for the stripped letter known to be IN.");
                turn.updatedResponse -= 1;  //  DECREMENT the updatedResponse

                //  Having removed all letters known to be IN, if the remaining response is 0, the remainder of the guess is known to be OUT...
                updatedResponseIsZero(turn, knownOut, unknown, Turns);
            }
        }
        turn.parseCollectionToString();  //  PARSE the collection back to a String
        removeKnownOutFromAllTurns(knownOut, Turns);
    }

    private static void updatedResponseIsZero(Turn turn, IdentifiedLetters knownOut, Unknown unknown, LinkedList<Turn> Turns) {
        if(turn.updatedResponse == 0) {
            turn.response = turn.updatedResponse;
            turn.parseCollectionToString();  //  PARSE the collection back to a String
            AllTurns.responseOfZero(turn, knownOut, unknown, Turns);
        }
    }

    public static void removeKnownOutFromAllTurns(IdentifiedLetters knownOut, LinkedList<Turn> Turns) {
        for (Turn t : Turns) {              //  FOR every turn in Turns
            for (Character c : knownOut.letters) {  //  FOR every character in knownIn
                t.turn.remove(c);           //  REMOVE the character from the turn
            }
            t.parseCollectionToString();    //  PARSE the collection back to a String
        }
    }

    //  COMPARE each updated turn against each other...
    public static void compareAllTurnsAgainstEachOther(LinkedList<Turn> Turns, Pairs pairs, IdentifiedLetters knownIn, IdentifiedLetters knownOut, Unknown unknown) throws SQLException {

        removeDeterminedLettersFromAllTurns(Turns, knownIn, knownOut);

        System.out.println("assess.AllTurns.compareAllTurnsAgainstEachOther(): BEGIN");

        ArrayList<Character> letterChangedFrom = new ArrayList<>();
        ArrayList<Character> letterChangedTo = new ArrayList<>();

        int comparisonNumber = 1;

        for(int i = 0; i < Turns.size() - 1; i++) {      //  Take the FIRST turn in 'Turns' (then the second, then the third, up until the SECOND LAST Turn in 'Turns')
            for(int j = i + 1; j < Turns.size(); j++) {  //  Take the SECOND turn in 'Turns' (then the third, then the fourth, up until the LAST Turn in 'Turns')

                System.out.println("COMPARISON #" + comparisonNumber + ".  Now comparing turn #" + (i + 1) + " with turn #" + (j + 1) + ":");
                prettyPrintLinkedHashMap(Turns, i, j);

                //  CLASSIFICATION:
                System.out.println("CLASSIFICATION:");
                Classification classification;
                classification = new Classification(Turns.get(i).updatedResponse, Turns.get(j).updatedResponse, Turns.get(i).updatedGuess, Turns.get(j).updatedGuess);
                classification.printClassification();

                // Now that the selected pair of turns has been CLASSIFIED, Identify Findings, make Determinations, and take ACTION...
                if(!classification.updatedGuessesSame) {  // One (or more) letters has changed.  Letters in common are IN.  All others are OUT.
                    System.out.println("FINDINGS:");
                    classification.printFindings();
                    System.out.println("DETERMINATIONS:");
                    if(classification.updatedResponsesDifferByOne && classification.updatedGuessesSameLength) {
                        System.out.println(" - Since updated responses are the same length, and the responses differ by 1, we know:");
                        System.out.println("   " + classification.onlyInFirst + " is IN.");
                        System.out.println("   " + classification.onlyInSecond + " is OUT.");
                    }
                    System.out.println("ACTIONS:");
                }


                // Todo NEW turn comparison logic:
                // if differenceInUpdatedResponses == 0 & updatedGuesses different, take the smaller set away from the bigger.  What remains is OUT!  <-- (Kim thing)
                // if differenceInUpdatedResponses == 0, and updatedGuesses same, do nothing   <-- (DON'T write code for DO NOTHING scenarios)
                // if differenceInUpdatedResponses == 1 or -1, and the guesses differ by 1 letter, that letter is IN!
                // if differenceInUpdatedResponses == 2 or -2, nothing can be learned  <-- Not necessarily true!!!



                letterChangedFrom.clear();
                letterChangedTo.clear();
            }
        }
        System.out.println("assess.AllTurns.compareAllTurnsAgainstEachOther(): END");
    }



    private static String getLongerUpdatedGuessInCurrentPair(String iUpdatedGuess, String jUpdatedGuess) {
        if(iUpdatedGuess.length() > jUpdatedGuess.length()) return iUpdatedGuess;
        else return jUpdatedGuess;
    }

    private static void responseIsEqualWithOneLetterDifferent(LinkedList<Turn> Turns, IdentifiedLetters knownIn, IdentifiedLetters knownOut, Unknown unknown, int i, int j) throws SQLException {



        //  If what remains is size = 1, then the turns MUST have been 1 letter different
        //  Therefore, the remaining letter is OUT, so go ahead and do it

        //  CREATE a temp pair of collections for a pair of turns to make the assessment...
        Set<Character> a = Turns.get(i).turn;
        Set<Character> b = Turns.get(i).turn;

        //  Take the smaller (Set<Character>) away from the bigger
        if(a.size() > b.size()) {
            System.out.println("Removing " + b + " from " + a);
            a.removeAll(b);
            //  If what remains is size = 1, then the turns MUST have been 1 letter different
            //  Therefore, the remaining letter is OUT, so go ahead and do it
            if (a.size() == 1) Turns.get(i).turn = a;
            updatedResponseIsZero(Turns.get(i), knownOut, unknown, Turns);
            checkAllTurnsForSizeEqualsUpdatedResponse(Turns, knownIn, unknown);
        } else if (a.size() < b.size()){
            System.out.println("Removing " + Turns.get(i).turn + " from " + Turns.get(j).turn);
            b.removeAll(a);
            //  If what remains is size = 1, then the turns MUST have been 1 letter different
            //  Therefore, the remaining letter is OUT, so go ahead and do it
            if (b.size() == 1) Turns.get(i).turn = b;
            updatedResponseIsZero(Turns.get(j), knownOut, unknown, Turns);
            checkAllTurnsForSizeEqualsUpdatedResponse(Turns, knownIn, unknown);
        }
    }

    private static void removeDeterminedLettersFromAllTurns(LinkedList<Turn> Turns, IdentifiedLetters knownIn, IdentifiedLetters knownOut) {

        for(Character c : knownIn.letters) {  //  walks knownIn
            for(Turn t : Turns) {     //  walks a turn
                if(t.turn.contains(c)) {
                    t.turn.remove(c);
                    t.updatedResponse -= 1;
                }
                t.parseCollectionToString();
            }
        }

        for(Character c : knownOut.letters) {  //  walks knownIn
            for(Turn t : Turns) {      // walks a turn
                t.turn.remove(c);
                t.parseCollectionToString();
            }
        }
    }

    //  FOR any turn where size = updatedResponse, ALL letters in the updatedGuess are KNOWN IN
    public static void checkAllTurnsForSizeEqualsUpdatedResponse(LinkedList<Turn> Turns, IdentifiedLetters knownIn, Unknown unknown) throws SQLException {

        System.out.println("assess.AllTurns.checkAllTurnsForSizeEqualsUpdatedResponse(): BEGIN");

        for(Turn t : Turns) {
            if(t.turn.size() == t.updatedResponse) {
                knownIn.letters.addAll(t.turn);
            }
        }
        System.out.println("assess.AllTurns.checkAllTurnsForSizeEqualsUpdatedResponse(): END");
    }

    /*  Once a DETERMINATION on two letters has been made, UPDATE all data sources accordingly...
    PARAMETER HELL!!! I can't use this when checking if(knownTogether.contains(knownIn)) due to absent letterChanged params
    I will overload updateAllDataSources (for now) but this may be a candidate to be torn down
    Refactor updateAllDataSources to updateDataSources, and overload updateDataSources for if(knownTogether.contains(knownIn)) */
    private static void updateDataSources(LinkedList<Turn> Turns, IdentifiedLetters knownIn, IdentifiedLetters knownOut, ArrayList<Character> letterChangedFrom, ArrayList<Character> letterChangedTo, Unknown unknown) {

        System.out.println("assess.AllTurns.updateAllDataSources(Turns, knownTogether, knownIn, knownOut, letterChangedFrom, letterChangedTo, unknown): BEGIN");

        knownIn.letters.addAll(letterChangedFrom);
        knownOut.letters.addAll(letterChangedTo);

        //  ALL letters in KnownOut to be removed from UNKNOWN, ALL TURNS, DATABASE...
        for(Character c: knownOut.letters) {
            Unknown.letters.remove(c);

            for(Turn t : Turns) {
                t.turn.remove(c);
                t.parseCollectionToString();
            }

            try {
                Delete.wordsWith(String.valueOf(c), unknown, knownOut);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //  ALL letters in knownIn to be removed from UNKNOWN, ALL TURNS (while decrementing updatedResponse), DATABASE...
        for(Character c: knownIn.letters) {
            Unknown.letters.remove(c);

            for(Turn t : Turns) {
                if(t.turn.contains(c)) {
                    t.turn.remove(c);
                    t.updatedResponse--;
                    t.parseCollectionToString();
                }
            }

            try {
                Delete.wordsWithout(String.valueOf(c), unknown, knownIn.letters);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println();

        Create.rebuildWatsonDB(knownIn.letters, unknown);
        System.out.println("assess.AllTurns.updateAllDataSources(Turns, knownTogether, knownIn, knownOut, letterChangedFrom, letterChangedTo, unknown): END");
    }

    //  PRETTY-PRINT the UPDATED turns being compared...
    private static void prettyPrintLinkedHashMap(LinkedList<Turn> Turns, int i, int j) {

        //  System.out.println("assess.AllTurns.prettyPrintLinkedHashMap(): BEGIN");

        StringBuilder sb = new StringBuilder();

        sb.append("    ORIGINAL: ").append(Turns.get(i).guess).append(", ").append(Turns.get(i).response).append(" > UPDATED: [").append(Turns.get(i).updatedGuess).append("]").append(" = ").append(Turns.get(i).updatedResponse);
        System.out.println(sb);
        sb.setLength(0);
        sb.append("    ORIGINAL: ").append(Turns.get(j).guess).append(", ").append(Turns.get(j).response).append(" > UPDATED: [").append(Turns.get(j).updatedGuess).append("]").append(" = ").append(Turns.get(j).updatedResponse);
        System.out.println(sb);
        //  System.out.println("assess.AllTurns.prettyPrintLinkedHashMap(): END");
    }

    //  DETERMINE letters changed between turns...
    private static void identifyChangedLetters(LinkedList<Turn> Turns, ArrayList<Character> letterChangedFrom, int i, ArrayList<Character> letterChangedTo, int j) {

        //  System.out.println("assess.AllTurns.identifyChangedLetters(): BEGIN");

        //  CLEAR any existing entries...
        letterChangedFrom.clear();
        letterChangedTo.clear();

        //  ADD updated guesses to be compared...
        letterChangedFrom.addAll(Turns.get(i).turn);
        letterChangedTo.addAll(Turns.get(j).turn);

        //  ADD a temp ArrayList to preserve values to be deleted...
        ArrayList<Character> temp = new ArrayList<>(letterChangedFrom);

        letterChangedFrom.removeAll(letterChangedTo);
        letterChangedTo.removeAll(temp);

        System.out.println("    " + letterChangedTo + " was changed to " + letterChangedFrom + " in these two turns");

        //  System.out.println("assess.AllTurns.identifyChangedLetters(): END");
    }

    //  ANY guess with a response of zero is KNOWN OUT.  Remove all letters in the guess from ALL data sources...
    public static void responseOfZero(Turn turn, IdentifiedLetters knownOut, Unknown unknown, LinkedList<Turn> Turns) {

        System.out.println("assess.AllTurns.responseOfZero(): BEGIN");

        if(turn.response == 0) {
            // DELETE every letter from the String (handle length programmatically) from the DB
            try {
                Delete.wordsWith(turn.updatedGuess, unknown, knownOut);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            regenerateWordPairsTable();

            // DELETE every letter from the String from Unknown.letters
            Unknown.removeFromUnknown(turn.updatedGuess);
            removeStringFromAllTurns(turn.updatedGuess, Turns);
        }
        System.out.println("assess.AllTurns.responseOfZero(): END");
    }

    //  REGENERATE the WordPairs table as previously unknown letters are determined to be KNOWN OUT...
    public static void regenerateWordPairsTable() {

        System.out.println("assess.AllTurns.regenerateWordPairsTable(): BEGIN");

        Connect.watson("dropWordPairsTable");    //  DROP the WordPairs table...
        Connect.watson("createWordPairsTable");  //  REGENERATE the WordPairs table...
        Connect.watson("deleteDups");            //  DELETE dups from the WordPairs table...

        System.out.println("assess.AllTurns.regenerateWordPairsTable(): END");
    }

    //  UPDATE all turns as determinations on letters are made...
    public static void removeStringFromAllTurns(String guess, LinkedList<Turn> Turns) {

        System.out.println("assess.AllTurns.removeStringFromAllTurns(): BEGIN");

        char[] guessArray = guess.toCharArray();
        List<Character> guessList = new ArrayList<>();

        for (char c : guessArray) guessList.add(c);

        for (Turn t : Turns) {

            char[] turnArray = t.updatedGuess.toCharArray();
            List<Character> turnList = new ArrayList<>();

            for (char c : turnArray) turnList.add(c);

            turnList.removeAll(guessList);

            StringBuilder sb = new StringBuilder();

            for(Character c : turnList) sb.append(c);

            t.updatedGuess = sb.toString();

            System.out.println(t.updatedGuess);
        }

        for(Turn t : Turns) t.parseGuessToCollection(t.updatedGuess);

        System.out.println("assess.AllTurns.removeStringFromAllTurns END");
    }
}