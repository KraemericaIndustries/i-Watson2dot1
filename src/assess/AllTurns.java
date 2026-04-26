package assess;

import dataStructures.*;
import transactSQL.*;
import java.util.*;

public class AllTurns {

    public static void removeDeterminedLettersFromAllTurns(Dashboard dashboard) {

        for (Character c : dashboard.knownIn) {
            for (Turn t : dashboard.Turns) {

                if (t.updatedGuess != null && t.updatedGuess.indexOf(c) >= 0) {

                    // Remove all occurrences of c from updatedGuess
                    t.updatedGuess = t.updatedGuess.replace(c.toString(), "");

                    t.updatedResponse--;
                }
            }
        }

        for (Character c : dashboard.knownOut) {
            for (Turn t : dashboard.Turns) {

                if (t.updatedGuess != null && t.updatedGuess.indexOf(c) >= 0) {

                    // Remove all occurrences of c from updatedGuess
                    t.updatedGuess = t.updatedGuess.replace(c.toString(), "");
                }
            }
        }
        System.out.println();
    }

    //  PRETTY-PRINT the UPDATED turns being compared...
    public static void prettyPrintLinkedHashMap(LinkedList<Turn> Turns, int i, int j) {

        StringBuilder sb = new StringBuilder();

        sb.append("    ORIGINAL: ").append(Turns.get(i).guess).append(", ").append(Turns.get(i).response).append(" > UPDATED: [").append(Turns.get(i).updatedGuess).append("]").append(" = ").append(Turns.get(i).updatedResponse);
        System.out.println(sb);
        sb.setLength(0);
        sb.append("    ORIGINAL: ").append(Turns.get(j).guess).append(", ").append(Turns.get(j).response).append(" > UPDATED: [").append(Turns.get(j).updatedGuess).append("]").append(" = ").append(Turns.get(j).updatedResponse);
        System.out.println(sb);
    }
}