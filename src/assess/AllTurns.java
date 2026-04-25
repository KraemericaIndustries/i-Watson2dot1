package assess;

import dataStructures.*;
import transactSQL.*;
import java.util.*;

public class AllTurns {

    public static void removeDeterminedLettersFromAllTurns(Dashboard dashboard) {

        for(Character c : dashboard.knownIn) {  //  walks knownIn
            for(Turn t : dashboard.Turns) {     //  walks a turn
                if(t.turn.contains(c)) {
                    t.turn.remove(c);
                    t.updatedResponse -= 1;
                }
                t.parseCollectionToString();
            }
        }

        for(Character c : dashboard.knownOut) {  //  walks knownIn
            for(Turn t : dashboard.Turns) {      // walks a turn
                t.turn.remove(c);
                t.parseCollectionToString();
            }
        }
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