package assess;

import dataStructures.LetterGroup;
import dataStructures.Turn;
import dataStructures.Unknown;
import transactSQL.Delete;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class AllTurns {

    public static void forKnownTogether(LinkedList<Turn> Turns, LetterGroup knownTogether) {

        for(Turn t : Turns) {
            knownTogether.letters.putAll(t.turn);
        }

        Set<Character> turn1Keys = Turns.get(1).turn.keySet();

        for(Character c : turn1Keys) {
            if(Turns.get(0).turn.containsKey(c) && Turns.get(1).turn.containsKey(c)) knownTogether.letters.remove(c);
        }
    }

    public static void makeDeterminations(LinkedList<Turn> Turns, LetterGroup knownTogether, LetterGroup knownIn, LetterGroup knownOut, Unknown unknown) {

        LetterGroup letterChangedFrom = new LetterGroup();
        LetterGroup letterChangedTo = new LetterGroup();
        boolean determinationMade = false;
        int comparisonNumber = 1;

        for(int i = 0; i < Turns.size() - 1; i++) {      //  Take the FIRST turn in 'Turns' (then the second, then the third, up until the SECOND LAST Turn in 'Turns')
            for(int j = i + 1; j < Turns.size(); j++) {  //  Take the SECOND turn in 'Turns' (then the third, then the fourth, up until the LAST Turn in 'Turns')
                System.out.println("Comparison #" + comparisonNumber + ".  Now comparing turn #" + (i + 1) + " with turn #" + (j + 1) + ":");
                prettyPrintLinkedHashMap(Turns, i);
                prettyPrintLinkedHashMap(Turns, j);
                comparisonNumber++;

                if(Turns.get(i).updatedResponse == Turns.get(j).updatedResponse) {  //  IF responses from compared turns are EQUAL...
                    System.out.println("Scenario: i.updatedResponse == j.updatedResponse");
                    forKnownTogether(Turns, knownTogether);
                    identifyChangedLetters(Turns, letterChangedFrom, i, letterChangedTo, j);

                } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == 1) {  //  ELSE-IF responses from compared turns are + 1...
                    System.out.println("Scenario: i - j = 1");
                    identifyChangedLetters(Turns, letterChangedFrom, i, letterChangedTo, j);
                    if(letterChangedTo.letters.size()==1 && letterChangedFrom.letters.size()==1) {
                        System.out.println("With 1 letter changed, and the responses varying by 1, " + letterChangedFrom.letters + " is KNOWN IN, and " + letterChangedTo.letters + " is KNOWN OUT.  Updating all data sources...");

                        determinationMade = true;

                        knownIn.letters.putAll(letterChangedFrom.letters);
                        knownOut.letters.putAll(letterChangedTo.letters);

                        //  ALL letters in knownTogether may be COPIED to knownOut...
                        knownOut.letters.putAll(knownTogether.letters);

                        //  ALL letters in KnownOut to be removed from 'Unknown' & ALL 'TURNS' & the database...
                        Set<Character> knownOutKeys = knownOut.letters.keySet();

                        for(Character c: knownOutKeys) {
                            Unknown.letters.remove(c);

                            for(Turn t : Turns) {
                                t.turn.remove(c);
                            }

                            try {
                                Delete.words(c);
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
                        }
                        //  CLEAR 'knownTogether'...
                        knownTogether.letters.clear();

                    } else {
                        System.out.println("More than 1 letter changed between these 2 turns.  No conclusions may be drawn.");
                    }
                } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == -1) {
                    System.out.println("i - j = -1");
                    identifyChangedLetters(Turns, letterChangedFrom, i, letterChangedTo, j);
                }
            }
        }
    }
    private static void prettyPrintLinkedHashMap(LinkedList<Turn> Turns, int i) {

        StringBuilder sb = new StringBuilder();

        sb.append("[");

        for (Map.Entry<Character, Integer> ite : Turns.get(i).turn.entrySet()) {
            sb.append(ite.getKey()).append(", ");
        }

        sb.setLength(sb.length() - 2);
        sb.append("]");

        System.out.println(sb + " = " + Turns.get(i).updatedResponse);
    }
    private static void identifyChangedLetters(LinkedList<Turn> Turns, LetterGroup letterChangedFrom, int i, LetterGroup letterChangedTo, int j) {

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
        System.out.println(letterChangedTo.letters + " was changed to " + letterChangedFrom.letters + " in these two turns");


        if((letterChangedTo.letters.size()==1 && letterChangedFrom.letters.size()==1) && Turns.get(j).updatedResponse == Turns.get(i).updatedResponse) {
            System.out.println("We now know that " + letterChangedTo.letters + " and " + letterChangedFrom.letters + " are either both IN, or both OUT (but cannot be sure which is the case)");
        }

        letterChangedFrom.letters.clear();
        letterChangedTo.letters.clear();

        System.out.println();  //  Print a space after each turn in 'Turns is compared
    }
}