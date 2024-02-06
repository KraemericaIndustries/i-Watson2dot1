package assess;

import dataStructures.LetterGroup;
import dataStructures.Turn;

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

    public static void makeDeterminations(LinkedList<Turn> Turns) {

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
            }
        }
    }
}