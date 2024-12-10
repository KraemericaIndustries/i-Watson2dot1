package assess;

import dataStructures.LetterGroup;
import dataStructures.Turn;
import dataStructures.Unknown;
import transactSQL.Delete;
import transactSQL.Insert;
import transactSQL.Query;
import transactSQL.Select;

import java.sql.SQLException;
import java.util.*;

public class AllTurns {

    public static void updateKnownTogether(LinkedList<Turn> Turns, LetterGroup knownTogether, int i, int j) {

        knownTogether.letters.putAll(Turns.get(i).turn);
        knownTogether.letters.putAll(Turns.get(j).turn);

        Set<Character> turn1Keys = Turns.get(i).turn.keySet();

        for(Character c : turn1Keys) {
            if(Turns.get(i).turn.containsKey(c) && Turns.get(j).turn.containsKey(c)) knownTogether.letters.remove(c);
        }
    }

    public static void makeDeterminations(LinkedList<Turn> Turns, LetterGroup knownTogether, LetterGroup knownIn, LetterGroup knownOut, Unknown unknown) {

        //  size = updatedResponse here...
        for(Turn t : Turns) {
            if(t.turn.size() == t.updatedResponse) {
                knownIn.letters.putAll(t.turn);

                // delete t.turn from db
                StringBuilder sb = new StringBuilder();

                Set<Character> keys = t.turn.keySet();

                // printing the elements of LinkedHashMap
                for (Character c : keys) {
                    sb.append(c);
                }

                t.updatedGuess = sb.toString();

                try {
                    Delete.wordsWithout(t.updatedGuess, unknown, knownIn);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // rebuild db
                Query.wordsFromDB();
                try {
                    Delete.fromWordsTbl();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Insert.reloadKnownWords();

                // remove knownIn from unknown
                removeKnownInFromUnknown(knownIn);

                unknown.sort();

                // rebuild wordpairs table
                regenerateWordPairsTable();
            }
        }

        LetterGroup letterChangedFrom = new LetterGroup();
        LetterGroup letterChangedTo = new LetterGroup();

        int comparisonNumber = 1;

        for(int i = 0; i < Turns.size() - 1; i++) {      //  Take the FIRST turn in 'Turns' (then the second, then the third, up until the SECOND LAST Turn in 'Turns')
            for(int j = i + 1; j < Turns.size(); j++) {  //  Take the SECOND turn in 'Turns' (then the third, then the fourth, up until the LAST Turn in 'Turns')
                System.out.println("Comparison #" + comparisonNumber + ".  Now comparing turn #" + (i + 1) + " with turn #" + (j + 1) + ":");
                prettyPrintLinkedHashMap(Turns, i);
                prettyPrintLinkedHashMap(Turns, j);
                identifyChangedLetters(Turns, letterChangedFrom, i, letterChangedTo, j);
                comparisonNumber++;

                //  IF responses from compared turns are EQUAL...
                if(Turns.get(i).updatedResponse == Turns.get(j).updatedResponse) {
                    System.out.println("    Scenario: i.updatedResponse == j.updatedResponse");
                    //  AND IF Only 1 letter has changed between turns...
                    if(letterChangedTo.letters.size()==1 && letterChangedFrom.letters.size()==1) {
                        System.out.println("    Scenario: i.updatedResponse == j.updatedResponse + Only 1 letter changed between turns:");
                        System.out.println("    We now know that " + letterChangedTo.letters + " and " + letterChangedFrom.letters + " are either both IN, or both OUT (but cannot be sure which is the case)");
                        updateKnownTogether(Turns, knownTogether, i, j);
                        System.out.println();
                    } else {
                        System.out.println("    More than 1 letter changed between these 2 turns.  No conclusions may be drawn.");
                    }
                    System.out.println();

                //  ELSE-IF responses from compared turns are + 1...
                } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == 1) {
                    System.out.println("    Scenario: updatedResponse(i) - updatedResponse(j) = 1");
                    //  AND IF Only 1 letter has changed between turns...
                    if(letterChangedTo.letters.size()==1 && letterChangedFrom.letters.size()==1) {
                        System.out.println("    Scenario: updatedResponse(i) - updatedResponse(j) = 1:");
                        System.out.println("    With 1 letter changed, and the responses varying by 1, " + letterChangedFrom.letters + " is KNOWN IN, and " + letterChangedTo.letters + " is KNOWN OUT.  Updating all data sources...");
                        updateAllDataSources(Turns, knownTogether, knownIn, knownOut, letterChangedFrom, letterChangedTo, unknown);
                        System.out.println();
                    } else {
                        System.out.println("    More than 1 letter changed between these 2 turns.  No conclusions may be drawn.");
                    }
                    System.out.println();

                //  ELSE-IF responses from compared turns are + 1...
                } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == -1) {
                    System.out.println("    AllTurns.makeDeterminations: updatedResponse(i) - updatedResponse(j) = -1");
                    //  AND IF Only 1 letter has changed between turns...
                    if(letterChangedTo.letters.size()==1 && letterChangedFrom.letters.size()==1) {
                        System.out.println("    Scenario: updatedResponse(i) - updatedResponse(j) = -1:");
                        System.out.println("    With 1 letter changed, and the responses varying by 1, " + letterChangedTo.letters + " is KNOWN IN, and " + letterChangedFrom.letters + " is KNOWN OUT.  Updating all data sources...");
                        updateAllDataSources(Turns, knownTogether, knownIn, knownOut, letterChangedTo, letterChangedFrom, unknown);
                        System.out.println();
                    } else {
                        System.out.println("    More than 1 letter changed between these 2 turns.  No conclusions may be drawn.");
                    }
                    System.out.println();
                }
            }
            letterChangedFrom.letters.clear();
            letterChangedTo.letters.clear();
        }
    }

    private static void removeKnownInFromUnknown(LetterGroup knownIn) {
        Object[] letters;
        Set<Character> letterKeys = knownIn.letters.keySet();
        letters = letterKeys.toArray();
        for(Object c : letters) {
            Unknown.letters.remove((Character)c);
        }
    }

    private static void updateAllDataSources(LinkedList<Turn> Turns, LetterGroup knownTogether, LetterGroup knownIn, LetterGroup knownOut, LetterGroup letterChangedFrom, LetterGroup letterChangedTo, Unknown unknown) {
        knownIn.letters.putAll(letterChangedFrom.letters);
        knownOut.letters.putAll(letterChangedTo.letters);

        //  ALL letters in knownTogether may be COPIED to knownOut...
//        knownOut.letters.putAll(knownTogether.letters);

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

        for(Character c: knownInKeys) {
            Unknown.letters.remove(c);

            for(Turn t : Turns) {
                if(t.turn.containsKey(c)) {
                    t.turn.remove(c);
                    t.updatedResponse--;
                }
            }

            try {
                Delete.wordsWithout(c);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        //  CLEAR 'knownTogether'...
//        knownTogether.letters.clear();
        Query.wordsFromDB();
        try {
            Delete.fromWordsTbl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Insert.reloadKnownWords();
        removeKnownInFromUnknown(knownIn);
        unknown.sort();

    }

    private static void prettyPrintLinkedHashMap(LinkedList<Turn> Turns, int i) {

        StringBuilder sb = new StringBuilder();

        sb.append("    [");

        for (Map.Entry<Character, Integer> ite : Turns.get(i).turn.entrySet()) {
            sb.append(ite.getKey()).append(", ");
        }

        sb.setLength(sb.length() - 2);
        sb.append("]");

        System.out.println(sb + " = " + Turns.get(i).updatedResponse);
    }
    private static void identifyChangedLetters(LinkedList<Turn> Turns, LetterGroup letterChangedFrom, int i, LetterGroup letterChangedTo, int j) {

        letterChangedFrom.letters.clear();
        letterChangedTo.letters.clear();

        letterChangedFrom.letters.putAll(Turns.get(i).turn);
        letterChangedTo.letters.putAll(Turns.get(j).turn);

        Set<Character> turn1Keys = Turns.get(j).turn.keySet();
        Set<Character> turn2Keys = Turns.get(i).turn.keySet();

        for(Character c : turn1Keys) {
            letterChangedFrom.letters.remove(c);
        }

        for(Character c : turn2Keys) {
            letterChangedTo.letters.remove(c);
        }
        System.out.println("    " + letterChangedTo.letters + " was changed to " + letterChangedFrom.letters + " in these two turns");
    }

    public static void responseOfZero(Turn turn, LetterGroup knownOut, Unknown unknown, LinkedList<Turn> Turns, LetterGroup knownTogether, LetterGroup knownIn) {

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
            knownOut.loadLettersFromString(turn.guess);

            removeStringFromAllTurns(turn.guess, Turns);
            knownTogether.letters.clear();
        }
    }

    private static void regenerateWordPairsTable() {
        //  DROP the WordPairs table...
        try {
            Delete.dropWordPairsTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //  REGENERATE the WordPairs table...
        try {
            Select.createPairsTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //  DELETE dups from the WordPairs table...
        try {
            Delete.deleteDupsFromPairsTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeStringFromAllTurns(String guess, LinkedList<Turn> Turns) {

        System.out.println("Invoking AllTurns.removeStringFromAllTurns...");

        char[] guessArray = guess.toCharArray();

        List<Character> guessList = new ArrayList<>();
        for (char c : guessArray) {
            guessList.add(c);
        }

        for (Turn t : Turns) {
            char[] turnArray = t.guess.toCharArray();

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

        System.out.println("AllTurns.removeStringFromAllTurns END");
    }
}