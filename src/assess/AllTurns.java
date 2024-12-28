package assess;

import dataStructures.Pairs;
import dataStructures.Turn;
import dataStructures.Unknown;
import transactSQL.*;

import java.sql.SQLException;
import java.util.*;

public class AllTurns {

    //  MAKE all possible determinations on ALL previous turns taken...
    public static void makeDeterminations(LinkedList<Turn> Turns, Pairs pairs, Set<Character> knownIn, Set<Character> knownOut, Unknown unknown) throws SQLException {

        System.out.println("assess.AllTurns.makeDeterminations(): BEGIN");

        //  Checks if any one knownTogether pair is knownIn, if yes, the other must be knownIn (because we only process turns where the response varies by 1)
        if(!pairs.knownTogether.isEmpty()) {
            pairs.checkPairsForKnownIn(knownIn, unknown);
            pairs.checkPairsForKnownOut(knownOut, unknown);
            for (Turn t : Turns) updateTurn(t, knownIn, knownOut, pairs, unknown, Turns);
        }

        //  ANY turn with an (updated) size equal to the (updated) response are KNOWN IN...
        checkAllTurnsForSizeEqualsUpdatedResponse(Turns, knownIn, unknown);

        //  COMPARE each (updated) turn against each other...
        compareAllTurnsAgainstEachOther(Turns, pairs, knownIn, knownOut, unknown);
        System.out.println("assess.AllTurns.makeDeterminations: END");
    }

    public static void updateTurn(Turn turn, Set<Character> knownIn, Set<Character> knownOut, Pairs pairs, Unknown unknown, LinkedList<Turn> Turns) {

            for (Character c : knownIn) {       //  FOR every character in knownIn
                if(turn.turn.contains(c)) {     //  IF the turn contains the character
                    System.out.println("Removing all 'KNOWN IN' (" + c + ") from guess of " + turn.turn + "...");
                    turn.turn.remove(c);        //  REMOVE the character from the turn
                    System.out.println("Decrementing response from " + turn.updatedGuess + " to " + (turn.updatedResponse - 1) + " to account for the stripped letter known to be IN.");
                    turn.updatedResponse -= 1;  //  DECREMENT the updatedResponse

                    //  Having removed all letters known to be IN, if the remaining response is 0, the remainder of the guess is known to be OUT...
                    if(turn.updatedResponse == 0) {
                        turn.response = turn.updatedResponse;
                        turn.parseCollectionToString();  //  PARSE the collection back to a String
                        AllTurns.responseOfZero(turn, knownOut, unknown, Turns, pairs);
                    }
                }
            }
            turn.parseCollectionToString();  //  PARSE the collection back to a String

        for (Turn t : Turns) {              //  FOR every turn in Turns
            for (Character c : knownOut) {  //  FOR every character in knownIn
                t.turn.remove(c);           //  REMOVE the character from the turn
            }
            t.parseCollectionToString();    //  PARSE the collection back to a String
        }
    }

    //  COMPARE each updated turn against each other...
    public static void compareAllTurnsAgainstEachOther(LinkedList<Turn> Turns, Pairs pairs, Set<Character> knownIn, Set<Character> knownOut, Unknown unknown) throws SQLException {


        removeDeterminedLettersFromAllTurns(Turns, knownIn, knownOut);

        System.out.println("assess.AllTurns.compareAllTurnsAgainstEachOther(): BEGIN");

        ArrayList<Character> letterChangedFrom = new ArrayList<>();
        ArrayList<Character> letterChangedTo = new ArrayList<>();

        int comparisonNumber = 1;

        for(int i = 0; i < Turns.size() - 1; i++) {      //  Take the FIRST turn in 'Turns' (then the second, then the third, up until the SECOND LAST Turn in 'Turns')
            for(int j = i + 1; j < Turns.size(); j++) {  //  Take the SECOND turn in 'Turns' (then the third, then the fourth, up until the LAST Turn in 'Turns')

                int simplifier = Turns.get(i).updatedResponse - Turns.get(j).updatedResponse;

                if(simplifier < 2 && simplifier > -2) {

                    System.out.println("Comparison #" + comparisonNumber + ".  Now comparing turn #" + (i + 1) + " with turn #" + (j + 1) + ":");
                    prettyPrintLinkedHashMap(Turns, i, j);

                    //  This 'if' statement prevents pairs of updated turns consisting of only 1 letter each from being treated as indeterminate...
                    if(Turns.get(i).updatedGuess.length() == 1 && Turns.get(j).updatedGuess.length() == 1 &&
                            Turns.get(i).updatedResponse == 1 && Turns.get(j).updatedResponse == 1) {
                        knownIn.addAll(Turns.get(i).turn);
                        knownIn.addAll(Turns.get(j).turn);
                    } else {
                        identifyChangedLetters(Turns, letterChangedFrom, i, letterChangedTo, j);
                    }

                    comparisonNumber++;

                    if(Turns.get(i).updatedResponse == Turns.get(j).updatedResponse) {  //  IF responses from compared turns are EQUAL...
                        System.out.println("    Scenario: i.updatedResponse == j.updatedResponse");
                        if(letterChangedTo.size()==1 && letterChangedFrom.size()==1) {  //  AND IF Only 1 letter has changed between turns...
                            System.out.println("    Scenario: i.updatedResponse == j.updatedResponse + Only 1 letter changed between turns:");
                            System.out.println("    We now know that " + letterChangedTo + " and " + letterChangedFrom + " are either both IN, or both OUT (but cannot be sure which is the case).\n");

                            //  CREATE a COPY of the pair set prior to adding the copy to the set of sets (prevents a cleared set from appearing in the set of sets)
                            Set<Character> tmp = new HashSet<>(Arrays.asList(letterChangedFrom.get(0), letterChangedTo.get(0)));
                            HashSet<Character> copy = new HashSet<>(tmp);

                            pairs.addPairsToSets(copy);

                            System.out.println();


                        } else {
                            System.out.println("    More than 1 letter changed between these 2 turns.  No conclusions may be drawn.\n");
                        }
                    } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == 1) {  //  ELSE-IF responses from compared turns are + 1...
                        System.out.println("    Scenario: updatedResponse(i) - updatedResponse(j) = 1");
                        if(letterChangedTo.size() == 1 && letterChangedFrom.size() == 1) {              //  AND IF Only 1 letter has changed between turns...
                            System.out.println("    Scenario: updatedResponse(i) - updatedResponse(j) = 1:");
                            System.out.println("    With 1 letter changed, and the responses varying by 1, " + letterChangedFrom + " is KNOWN IN, and " + letterChangedTo + " is KNOWN OUT.  Updating all data sources...\n");
                            //  HACK:  By changing the order of parameters in this invocation, I accomplish inversion of cases.
                            //  Clean this up (someday)
                            updateDataSources(Turns, pairs, knownIn, knownOut, letterChangedFrom, letterChangedTo, unknown);
                        } else {
                            System.out.println("    More than 1 letter changed between these 2 turns.  No conclusions may be drawn.\n");
                        }
                    } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == -1) {  //  ELSE-IF responses from compared turns are + 1...
                        System.out.println("    AllTurns.makeDeterminations: updatedResponse(i) - updatedResponse(j) = -1");
                        if(letterChangedTo.size() == 1 && letterChangedFrom.size() == 1) {               //  AND IF Only 1 letter has changed between turns...
                            System.out.println("    Scenario: updatedResponse(i) - updatedResponse(j) = -1:");
                            System.out.println("    With 1 letter changed, and the responses varying by 1, " + letterChangedTo + " is KNOWN IN, and " + letterChangedFrom + " is KNOWN OUT.  Updating all data sources...\n");
                            //  HACK:  By changing the order of parameters in this invocation, I accomplish inversion of cases.
                            //  Clean this up (someday)
                            updateDataSources(Turns, pairs, knownIn, knownOut, letterChangedTo, letterChangedFrom, unknown);
                            pairs.checkPairsForKnownIn(knownIn, unknown);
                            Create.rebuildWatsonDB(knownIn, unknown);
                        } else {
                            System.out.println("    More than 1 letter changed between these 2 turns.  No conclusions may be drawn.\n");
                        }
                    }
                }
                letterChangedFrom.clear();
                letterChangedTo.clear();
            }
        }
        System.out.println("assess.AllTurns.compareAllTurnsAgainstEachOther(): END");
    }

    private static void removeDeterminedLettersFromAllTurns(LinkedList<Turn> Turns, Set<Character> knownIn, Set<Character> knownOut) {
        for(Character c : knownIn) {  //  walks knownIn
            for(Turn t : Turns) {  // walks a turn
                if(t.turn.contains(c)) {
                    t.turn.remove(c);
                    t.updatedResponse -= 1;
                }
                t.parseCollectionToString();
            }
        }

        for(Character c : knownOut) {  //  walks knownIn
            for(Turn t : Turns) {  // walks a turn
                t.turn.remove(c);
                t.parseCollectionToString();
            }
        }
    }

    //  FOR any turn where size = updatedResponse, ALL letters in the updatedGuess are KNOWN IN
    private static void checkAllTurnsForSizeEqualsUpdatedResponse(LinkedList<Turn> Turns, Set<Character> knownIn, Unknown unknown) {

        System.out.println("assess.AllTurns.checkAllTurnsForSizeEqualsUpdatedResponse(): BEGIN");

        for(Turn t : Turns) {
            if(t.turn.size() == t.updatedResponse) {
                knownIn.addAll(t.turn);

                // delete t.turn from db
//                StringBuilder sb = new StringBuilder();

//                Set<Character> keys = t.turn.keySet();

                // printing the elements of LinkedHashMap
//                for (Character c : knownIn) {
//                    sb.append(c);
//                }
//
//                t.updatedGuess = sb.toString();

                try {
                    Delete.wordsWithout(t.updatedGuess, unknown, knownIn);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // rebuild db
                Query.wordsFromDB();
                Connect.watson("deleteFromWordsTable");
                Insert.reloadKnownWords();

                // remove knownIn from unknown
                removeKnownInFromUnknown(knownIn);

                unknown.sort();

                // rebuild wordpairs table
                regenerateWordPairsTable();
            }
        }
        System.out.println("assess.AllTurns.checkAllTurnsForSizeEqualsUpdatedResponse(): END");
    }

    //  REMOVE letters known to be IN, from the list of previously unknown letters...
    private static void removeKnownInFromUnknown(Set<Character> knownIn) {
        System.out.println("assess.AllTurns.removeKnownInFromUnknown(): BEGIN");
        for(Character c : knownIn) {
            Unknown.letters.remove(c);
        }
        System.out.println("assess.AllTurns.removeKnownInFromUnknown(): END");
    }

    //  Once a DETERMINATION on two letters has been made, UPDATE all data sources accordingly...
    //  ToDo FIX PARAMETER HELL!!! I can't use this when checking if(knownTogether.contains(knownIn)) due to absent letterChanged params
    //  I will overload updateAllDataSources (for now) but this may be a candidate to be torn down
    //  Refactor updateAllDataSources to updateDataSources, and overload updateDataSources for if(knownTogether.contains(knownIn))
    private static void updateDataSources(LinkedList<Turn> Turns, Pairs pairs, Set<Character> knownIn, Set<Character> knownOut, ArrayList<Character> letterChangedFrom, ArrayList<Character> letterChangedTo, Unknown unknown) {

        System.out.println("assess.AllTurns.updateAllDataSources(Turns, knownTogether, knownIn, knownOut, letterChangedFrom, letterChangedTo, unknown): BEGIN");

        knownIn.addAll(letterChangedFrom);
        knownOut.addAll(letterChangedTo);

        //  ALL letters in KnownOut to be removed from UNKNOWN, ALL TURNS, DATABASE...
        for(Character c: knownOut) {
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
        for(Character c: knownIn) {
            Unknown.letters.remove(c);

            for(Turn t : Turns) {
                if(t.turn.contains(c)) {
                    t.turn.remove(c);
                    t.updatedResponse--;
                    t.parseCollectionToString();
                }
            }

            try {
                Delete.wordsWithout(String.valueOf(c), unknown, knownIn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println();

        Create.rebuildWatsonDB(knownIn, unknown);
        System.out.println("assess.AllTurns.updateAllDataSources(Turns, knownTogether, knownIn, knownOut, letterChangedFrom, letterChangedTo, unknown): END");
    }

    //  PRETTY-PRINT the UPDATED turns being compared...
    private static void prettyPrintLinkedHashMap(LinkedList<Turn> Turns, int i, int j) {

//        System.out.println("assess.AllTurns.prettyPrintLinkedHashMap(): BEGIN");

        StringBuilder sb = new StringBuilder();

        sb.append("    ORIGINAL: ").append(Turns.get(i).guess).append(", ").append(Turns.get(i).response).append(" > UPDATED: [").append(Turns.get(i).updatedGuess).append("]").append(" = ").append(Turns.get(i).updatedResponse);
        System.out.println(sb);
        sb.setLength(0);
        sb.append("    ORIGINAL: ").append(Turns.get(j).guess).append(", ").append(Turns.get(j).response).append(" > UPDATED: [").append(Turns.get(j).updatedGuess).append("]").append(" = ").append(Turns.get(j).updatedResponse);
        System.out.println(sb);

//        System.out.println("assess.AllTurns.prettyPrintLinkedHashMap(): END");
    }

    //  DETERMINE letters changed between turns...
    private static void identifyChangedLetters(LinkedList<Turn> Turns, ArrayList<Character> letterChangedFrom, int i, ArrayList<Character> letterChangedTo, int j) {

//        System.out.println("assess.AllTurns.identifyChangedLetters(): BEGIN");

        //  CLEAR any existing entries...
        letterChangedFrom.clear();
        letterChangedTo.clear();

        //  ADD updated guesses to be compared...
        letterChangedFrom.addAll(Turns.get(i).turn);
        letterChangedTo.addAll(Turns.get(j).turn);

        //  ADD a temp ArrayList to preserve values to be deleted...
        ArrayList<Character> temp = new ArrayList<>();
        temp.addAll(letterChangedFrom);

        letterChangedFrom.removeAll(letterChangedTo);
        letterChangedTo.removeAll(temp);

        System.out.println("    " + letterChangedTo + " was changed to " + letterChangedFrom + " in these two turns");

//        System.out.println("assess.AllTurns.identifyChangedLetters(): END");
    }

    //  ANY guess with a response of zero is KNOWN OUT.  Remove all letters in the guess from ALL data sources...
    public static void responseOfZero(Turn turn, Set<Character> knownOut, Unknown unknown, LinkedList<Turn> Turns, Pairs pairs) {

        System.out.println("assess.AllTurns.responseOfZero(): BEGIN");

        if(turn.response == 0) {
//            System.out.println(turn.guess);
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

        //  DROP the WordPairs table...
        Connect.watson("dropWordPairsTable");
        //  REGENERATE the WordPairs table...
        Connect.watson("createWordPairsTable");
        //  DELETE dups from the WordPairs table...
        Connect.watson("deleteDups");

        System.out.println("assess.AllTurns.regenerateWordPairsTable(): END");
    }

    //  UPDATE all turns as determinations on letters are made...
    public static void removeStringFromAllTurns(String guess, LinkedList<Turn> Turns) {

        System.out.println("assess.AllTurns.removeStringFromAllTurns(): BEGIN");

        char[] guessArray = guess.toCharArray();

        List<Character> guessList = new ArrayList<>();
        for (char c : guessArray) {
            guessList.add(c);
        }

        for (Turn t : Turns) {
            char[] turnArray = t.updatedGuess.toCharArray();

            List<Character> turnList = new ArrayList<>();
            for (char c : turnArray) {
                turnList.add(c);
            }

            turnList.removeAll(guessList);

            StringBuilder sb = new StringBuilder();
            for(Character c : turnList) {
                sb.append(c);
            }

            t.updatedGuess = sb.toString();
            System.out.println(t.updatedGuess);
        }

        for(Turn t : Turns) {
            t.parseGuessToCollection(t.updatedGuess);
        }

        System.out.println("assess.AllTurns.removeStringFromAllTurns END");
    }
}