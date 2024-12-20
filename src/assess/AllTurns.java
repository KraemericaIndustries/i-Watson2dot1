package assess;

import dataStructures.Turn;
import dataStructures.Unknown;
import transactSQL.*;

import java.sql.SQLException;
import java.util.*;

public class AllTurns {

    //  MAKE all possible determinations on ALL previous turns taken...
    public static void makeDeterminations(LinkedList<Turn> Turns, Set<Character> knownTogether, Set<Character> knownIn, Set<Character> knownOut, Unknown unknown) {

        System.out.println("assess.AllTurns.makeDeterminations(): BEGIN");

        if(!knownTogether.isEmpty()) {

            boolean knownTogetherInKnownIn = false;

            for (Character c : knownTogether) {
                if(knownIn.contains(c)) {
                    knownTogetherInKnownIn = true;
                }
            }

            if(knownTogetherInKnownIn) {
                knownIn.addAll(knownTogether);
                AllTurns.updateDataSources(Turns, knownIn, unknown);
                knownTogether.clear();
            }

            //  CHECK remaining letters of most recent turn against letters knownTogether.  IF the updatedResponse < knownTogether.size, knownTogether is KNOWN OUT...
            checkLastTurnAgainstKnownTogether(Turns, knownTogether, unknown, knownIn);
            //ToDo include check here for if nIN.contains(kT), then all kT is IN + update all data sources
        }

        //  ANY turn with an (updated) size equal to the (updated) response are KNOWN IN...
        checkAllTurnsForSizeEqualsUpdatedResponse(Turns, knownIn, unknown);

        //  COMPARE each (updated) turn against each other...
        compareAllTurnsAgainstEachOther(Turns, knownTogether, knownIn, knownOut, unknown);
        System.out.println("assess.AllTurns.makeDeterminations: END");
    }

    //  COMPARE each updated turn against each other...
    private static void compareAllTurnsAgainstEachOther(LinkedList<Turn> Turns, Set<Character> knownTogether, Set<Character> knownIn, Set<Character> knownOut, Unknown unknown) {


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
                            knownTogether.addAll(letterChangedFrom);
                            knownTogether.addAll(letterChangedTo);
                        } else {
                            System.out.println("    More than 1 letter changed between these 2 turns.  No conclusions may be drawn.\n");
                        }
                    } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == 1) {  //  ELSE-IF responses from compared turns are + 1...
                        System.out.println("    Scenario: updatedResponse(i) - updatedResponse(j) = 1");
                        if(letterChangedTo.size()==1 && letterChangedFrom.size()==1) {              //  AND IF Only 1 letter has changed between turns...
                            System.out.println("    Scenario: updatedResponse(i) - updatedResponse(j) = 1:");
                            System.out.println("    With 1 letter changed, and the responses varying by 1, " + letterChangedFrom + " is KNOWN IN, and " + letterChangedTo + " is KNOWN OUT.  Updating all data sources...\n");
                            updateDataSources(Turns, knownTogether, knownIn, knownOut, letterChangedFrom, letterChangedTo, unknown);
                        } else {
                            System.out.println("    More than 1 letter changed between these 2 turns.  No conclusions may be drawn.\n");
                        }
                    } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == -1) {  //  ELSE-IF responses from compared turns are + 1...
                        System.out.println("    AllTurns.makeDeterminations: updatedResponse(i) - updatedResponse(j) = -1");
                        if(letterChangedTo.size()==1 && letterChangedFrom.size()==1) {               //  AND IF Only 1 letter has changed between turns...
                            System.out.println("    Scenario: updatedResponse(i) - updatedResponse(j) = -1:");
                            System.out.println("    With 1 letter changed, and the responses varying by 1, " + letterChangedTo + " is KNOWN IN, and " + letterChangedFrom + " is KNOWN OUT.  Updating all data sources...\n");
                            updateDataSources(Turns, knownTogether, knownIn, knownOut, letterChangedTo, letterChangedFrom, unknown);
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
    private static void updateDataSources(LinkedList<Turn> Turns, Set<Character> knownTogether, Set<Character> knownIn, Set<Character> knownOut, ArrayList<Character> letterChangedFrom, ArrayList<Character> letterChangedTo, Unknown unknown) {

        System.out.println("assess.AllTurns.updateAllDataSources(Turns, knownTogether, knownIn, knownOut, letterChangedFrom, letterChangedTo, unknown): BEGIN");

        knownIn.addAll(letterChangedFrom);
        knownOut.addAll(letterChangedTo);

        //  ALL letters in knownTogether may be COPIED to knownOut...
//        knownOut.letters.putAll(knownTogether.letters);

        //  ALL letters in KnownOut to be removed from 'Unknown' & ALL 'TURNS' & the database...
//        Set<Character> knownOutKeys = knownOut.keySet();

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

        //  ALL letters in knownIn to be removed from unknown AND ALL TURNS while decrementing updatedResponse...
//        Set<Character> knownInKeys = knownIn.keySet();

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
        //  CLEAR 'knownTogether'...
//        knownTogether.letters.clear();
        Query.wordsFromDB();
        Connect.watson("deleteFromWordsTable");
        Insert.reloadKnownWords();
        removeKnownInFromUnknown(knownIn);
        unknown.sort();
        regenerateWordPairsTable();
        System.out.println("assess.AllTurns.updateAllDataSources(Turns, knownTogether, knownIn, knownOut, letterChangedFrom, letterChangedTo, unknown): END");
    }

    public static void updateDataSources(LinkedList<Turn> Turns, Set<Character> knownIn, Unknown unknown) {

        System.out.println("assess.AllTurns.updateDataSources(): BEGIN");

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
        //  CLEAR 'knownTogether'...
//        knownTogether.letters.clear();
        Query.wordsFromDB();
        Connect.watson("deleteFromWordsTable");
        Insert.reloadKnownWords();
        removeKnownInFromUnknown(knownIn);
        unknown.sort();
        regenerateWordPairsTable();
        System.out.println("assess.AllTurns.updateAllDataSources(): END");
    }

    //  PRETTY-PRINT the UPDATED turns being compared...
    private static void prettyPrintLinkedHashMap(LinkedList<Turn> Turns, int i, int j) {

//        System.out.println("assess.AllTurns.prettyPrintLinkedHashMap(): BEGIN");

        StringBuilder sb = new StringBuilder();

//        sb.append("    [").append(Turns.get(i).updatedGuess).append("]").append(" = ").append(Turns.get(i).updatedResponse);
        sb.append("    ORIGINAL: ").append(Turns.get(i).guess).append(", ").append(Turns.get(i).response).append(" > UPDATED: [").append(Turns.get(i).updatedGuess).append("]").append(" = ").append(Turns.get(i).updatedResponse);
        System.out.println(sb);
        sb.setLength(0);
//        sb.append("    [").append(Turns.get(j).updatedGuess).append("]").append(" = ").append(Turns.get(j).updatedResponse);
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
    public static void responseOfZero(Turn turn, Set<Character> knownOut, Unknown unknown, LinkedList<Turn> Turns, Set<Character> knownTogether, Set<Character> knownIn) {

        System.out.println("assess.AllTurns.responseOfZero(): BEGIN");

        if(turn.response == 0) {
            System.out.println(turn.guess);
            // DELETE every letter from the String (handle length programmatically) from the DB
            try {
                Delete.wordsWith(turn.guess, unknown, knownIn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            regenerateWordPairsTable();

            // DELETE every letter from the String from Unknown.letters
            Unknown.removeFromUnknown(turn.guess);
            // ADD every letter from the String to KNOWN OUT
//            knownOut.loadLettersFromString(turn.guess);

            removeStringFromAllTurns(turn.guess, Turns);
            knownTogether.clear();
        }

        System.out.println("assess.AllTurns.responseOfZero(): END");
    }

    //  REGENERATE the WordPairs table as previously unknown letters are determined to be KNOWN OUT...
    private static void regenerateWordPairsTable() {

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

    //  CHECK remaining letters of most recent turn against letters knownTogether.  IF the updatedResponse < knownTogether.size, knownTogether is KNOWN OUT...
    private static void checkLastTurnAgainstKnownTogether(LinkedList<Turn> turns, Set<Character> knownTogether, Unknown unknown, Set<Character> knownIn) {

        System.out.println("assess.AllTurns.checkLastTurnAgainstKnownTogether(): BEGIN");

        if(turns.get(turns.size() - 2).turn.containsAll(knownTogether)) {
            if(turns.getLast().updatedResponse < knownTogether.size()) {

                try {
                    for (Character c: knownTogether) {
                        // Todo: this method takes a string to delete - but the for loop invokes on a per-char basis... refactor to build a String and send it ONCE.
                        // ToDo: this method should check ALL TURNS for this condition - not just the previous turn...
                        Delete.wordsWith(String.valueOf(c), unknown, knownIn);
                    }
                    //                    Delete.wordsWith();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                Query.wordsFromDB();
                Connect.watson("deleteFromWordsTable");
                Insert.reloadKnownWords();

                // remove knownTogether(set) from unknown(linkedHashMap)
                for (Character c : knownTogether) {
                    Unknown.letters.remove(c);
                }

                unknown.sort();

                // rebuild wordpairs table
                regenerateWordPairsTable();

                StringBuilder sb = new StringBuilder();
                for(Character c : knownTogether) {
                    sb.append(c);
                }
                removeStringFromAllTurns(sb.toString(), turns);

                knownTogether.clear();
            }
        }
        System.out.println("assess.AllTurns.checkLastTurnAgainstKnownTogether(): END");
    }
}