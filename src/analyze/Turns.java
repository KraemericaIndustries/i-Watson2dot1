package analyze;

import dataStructures.LetterGroup;
import dataStructures.Turn;

import java.util.LinkedList;
import java.util.Set;

public class Turns {

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
}