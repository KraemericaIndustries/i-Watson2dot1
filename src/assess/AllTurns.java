package assess;

import dataStructures.LetterGroup;
import dataStructures.Turn;
import dataStructures.Unknown;

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
        System.out.println("kT: " + knownTogether.letters);
    }

    public static void makeDeterminations(LinkedList<Turn> Turns, LetterGroup knownTogether, LetterGroup knownIn, LetterGroup knownOut, Unknown unknown) {

        LetterGroup letterChangedFrom = new LetterGroup();
        LetterGroup letterChangedTo = new LetterGroup();
        boolean determinationMade = false;
        int combination = 1;

        for(int i = 0; i < Turns.size() - 1; i++) {
            for(int j = i + 1; j < Turns.size(); j++) {
                System.out.println("Combination #" + combination + ".  Now comparing:");

                System.out.print("[");
                for (Map.Entry<Character, Integer> ite : Turns.get(i).turn.entrySet()) {
                    System.out.print(ite.getKey() + ", ");
                }
                System.out.println("] = " + Turns.get(i).updatedResponse);

                System.out.print("[");
                for (Map.Entry<Character, Integer> ite : Turns.get(j).turn.entrySet()) {
                    System.out.print(ite.getKey() + ", ");
                }
                System.out.println("] = " + Turns.get(j).updatedResponse);
                combination++;







                if(Turns.get(i).updatedResponse == Turns.get(j).updatedResponse) {
                    forKnownTogether(Turns, knownTogether);
                } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == 1) {

                    letterChangedFrom.letters.putAll(Turns.get(i).turn);
                    letterChangedTo.letters.putAll(Turns.get(j).turn);

                    Set<Character> turn1Keys = Turns.get(j).turn.keySet();
                    Set<Character> turn2Keys = Turns.get(i).turn.keySet();

                    for(Character c : turn1Keys) {
                        if(letterChangedFrom.letters.containsKey(c)) letterChangedFrom.letters.remove(c);
                    }

                    for(Character c : turn2Keys) {
                        if(letterChangedTo.letters.containsKey(c)) letterChangedTo.letters.remove(c);
                    }
                    System.out.println(letterChangedTo.letters + " was changed to MEOW" + letterChangedFrom.letters + " in these two turns");

                    if(letterChangedTo.letters.size()==1 && letterChangedFrom.letters.size()==1) {
                        System.out.println("With 1 letter changed, and the responses varying by 1, " + letterChangedFrom.letters + " is KNOWN-IN, and " + letterChangedTo.letters + " is KNOWN OUT.  Take appropriate action.");
                        determinationMade = true;
                        knownIn.letters.putAll(letterChangedFrom.letters);
                        knownOut.letters.putAll(letterChangedTo.letters);
                        //  ToDo: ALL letters in knownTogether may be moved to knownOut
                        //  ToDo: ALL letters in knownIn, KnownOut to be removed from unknown SOMEHOW
                        //  ToDo: Update ALL turns with changes (above)

                    } else {
                        System.out.println("More than 1 letter changed between these 2 turns.  No conclusions may be drawn.");
                    }





                } else if (Turns.get(i).updatedResponse - Turns.get(j).updatedResponse == -1) {

                    letterChangedFrom.letters.putAll(Turns.get(j).turn);
                    letterChangedTo.letters.putAll(Turns.get(i).turn);

                    Set<Character> turn1Keys = Turns.get(j).turn.keySet();
                    Set<Character> turn2Keys = Turns.get(i).turn.keySet();

                    for(Character c : turn2Keys) {
                        if(letterChangedFrom.letters.containsKey(c)) letterChangedFrom.letters.remove(c);
                    }

                    for(Character c : turn1Keys) {
                        if(letterChangedTo.letters.containsKey(c)) letterChangedTo.letters.remove(c);
                    }
                    System.out.println(letterChangedTo.letters + " was changed to BEEP" + letterChangedFrom.letters + " in these two turns");
                }
            }
        }
        System.out.println();
    }
}