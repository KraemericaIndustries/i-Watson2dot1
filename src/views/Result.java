package views;

import controllers.Connect;
import models.LetterGroup;
import models.Turn;
import models.Unknown;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

public class Result {

    public static void results(LetterGroup knownIn, LetterGroup knownOut, LetterGroup knownTogether, Unknown unknown, LinkedList<Turn> Turns) {

        System.out.println("*****************************************************************  RESULT # " + Report.reportNumber + " *****************************************************************************************");
        System.out.println("ANALYSIS:");
        System.out.println(" - Previous guesses for which there is data available: " + Turns.size() + "\n");
        System.out.println("ADVICE:");
        //  (knownIn, knownOut, knownTogether, Turns)
        if(knownIn.letters.isEmpty() && knownOut.letters.isEmpty() && knownTogether.letters.isEmpty() && Turns.isEmpty()) case0000();  //  0000 - Absolutely NO previous data
        else if(knownIn.letters.isEmpty() && knownOut.letters.isEmpty() && knownTogether.letters.isEmpty()) case0001(Turns);           //  0001 - Previous turns have values, all else not
        else if(knownIn.letters.isEmpty() && knownOut.letters.isEmpty() && Turns.isEmpty()) case0010(knownTogether);                   //  0010 - knownTogether has values, all else not
        else if(knownIn.letters.isEmpty() && knownOut.letters.isEmpty()) case0011(knownTogether, Turns);                               //  0011 - knownTogether, Previous turns has data, all else not
        else if(knownIn.letters.isEmpty() && knownTogether.letters.isEmpty() && Turns.isEmpty()) case0100(knownOut);                   //  0100 - knownOut has data, all else not
        else if(knownIn.letters.isEmpty() && knownTogether.letters.isEmpty()) case0101(knownOut, Turns);                               //  0101 - knownOut, Previous turns has data, all else not
        else if(knownIn.letters.isEmpty() && Turns.isEmpty()) case0110(knownOut, knownTogether);                                       //  0110 - knownOut, knownTogether has data, all else not
        else if(knownIn.letters.isEmpty()) case0111();                                                                                 //  0111 - knownIn is EMPTY
        else if(knownTogether.letters.isEmpty() && knownOut.letters.isEmpty() && Turns.isEmpty()) case1000(knownIn);                   //  1000 - knownIn has data, all else not
        else if(knownOut.letters.isEmpty() && knownTogether.letters.isEmpty()) case1001();                                             //  1001 - knownIn, Previous turns has data, all else not
        else if(knownOut.letters.isEmpty() && Turns.isEmpty()) case1010();                                                             //  1010 - knownIn, knownTogether has data, all else not
        else if(knownOut.letters.isEmpty()) case1011();                                                                                //  1011 - knownOut is EMPTY, all else not
        else if(knownTogether.letters.isEmpty() &&  Turns.isEmpty()) case1100();                                                       //  1100 - knownIn, knownOut has data, all else not
        else if(knownTogether.letters.isEmpty()) case1101();                                                                           //  1101 - knownTogether is EMPTY
        else if(Turns.isEmpty()) case1110();                                                                                           //  1110 - Previous turns is EMPTY
        else case1111();                                                                                                               //  1111 - knownOut, Previous turns has data, all else not
        System.out.println("***********************************************************************************************************************************************************************");
    }

    public static void case0000() {
        System.out.println("+++ Result.case0000 +++");  //  Absolutely NO previous data

        char[] unknown = keySetToArray(Unknown.letters);

        System.out.print("Make a word with as many of [");
        for(int i = 0; i < 4; i++) {
            System.out.print(unknown[i] + ", ");
        }
        System.out.print(unknown[4] + "] as possible.  I suggest:\n");

        Connect.watson("getWords", 1, unknown[0], unknown[1], unknown[2], unknown[3], unknown[4]);

        System.out.println("--- Result.case ---");
    }
    private static void case0001(LinkedList<Turn> Turns) {  //  Previous turns have values, all else not
        System.out.println("+++ Result.case0001 +++");

        int lastTurn = Turns.size() - 1;

        char[] unknown = keySetToArray(Unknown.letters);
        char[] lastTurnChars = keySetToArray(Turns.get(lastTurn).turn);

        //  Take ONE LESS letter than the turn contains, and 1 extra from unknown (in an effort to change only 1 letter between guesses)...
        if      (Turns.get(lastTurn).turn.size() == 5) Connect.watson("getWords", 1, lastTurnChars[1], lastTurnChars[2], lastTurnChars[3], lastTurnChars[4], unknown[5]);
        else if (Turns.get(lastTurn).turn.size() == 4) Connect.watson("getWords", 1, lastTurnChars[0], lastTurnChars[1], lastTurnChars[2], unknown[0], unknown[1]);
        else if (Turns.get(lastTurn).turn.size() == 3) Connect.watson("getWords", 1, lastTurnChars[0], lastTurnChars[1], unknown[0], unknown[1], unknown[2]);
        else if (Turns.get(lastTurn).turn.size() == 2) Connect.watson("getWords", 1, lastTurnChars[0], unknown[0], unknown[1], unknown[2], unknown[3]);

        System.out.println("--- Result.case0001 ---");
    }
    private static void case0010(LetterGroup knownTogether) {

        System.out.println(" Result.case0010 ");  //  knownTogether has values, all else not

        char[] unknown = keySetToArray(Unknown.letters);
        char[] knownTogetherChars = keySetToArray(knownTogether.letters);

        //  Make a guess with the 2nd MOST FREQUENT from knownTogether (to try and make a determination on the 1st)
        Connect.watson("getWords", 1, knownTogetherChars[1], unknown[0], unknown[1], unknown[2], unknown[3]);

        System.out.println("--- Result.case0010 ---");
    }
    private static void case0011(LetterGroup knownTogether, LinkedList<Turn> Turns) {

        System.out.println("+++ Result.case0011 +++");  //  knownTogether, Previous turns has data, all else not

        int lastTurn = Turns.size() - 1;

        char[] unknown = keySetToArray(Unknown.letters);
        char[] knownTogetherChars = keySetToArray(knownTogether.letters);
        char[] lastTurnChars = keySetToArray(Turns.get(lastTurn).turn);

        //  Make a guess with the 2nd MOST FREQUENT from knownTogether (to try and make a determination on the 1st), as many letters as possible from the previous guess, and 1 unknown letter...
        if      (Turns.get(lastTurn).turn.size() == 5) Connect.watson("getWords", 1, knownTogetherChars[1], lastTurnChars[0], lastTurnChars[1], lastTurnChars[2], unknown[0]);
        else if (Turns.get(lastTurn).turn.size() == 4) Connect.watson("getWords", 1, knownTogetherChars[1], lastTurnChars[0], lastTurnChars[1], unknown[0], unknown[1]);
        else if (Turns.get(lastTurn).turn.size() == 3) Connect.watson("getWords", 1, knownTogetherChars[1], lastTurnChars[0], unknown[0], unknown[1], unknown[2]);

        System.out.println("--- Result.case0011 ---");
    }
    private static void case0100(LetterGroup knownOut) {

        System.out.println("+++ Result.case0100 +++");  //  knownOut has data, all else not

        char[] unknown = keySetToArray(Unknown.letters);
        char[] knownOutChars = keySetToArray(knownOut.letters);

        //  Make a guess with as many letters as possible from knownOut, including 1 from unknown...
        if      (knownOut.letters.size() >= 4) Connect.watson("getWords", 1, knownOutChars[0], knownOutChars[1], knownOutChars[2], knownOutChars[3], unknown[0]);
        else if (knownOut.letters.size() == 3) Connect.watson("getWords", 1, knownOutChars[0], knownOutChars[1], knownOutChars[2], unknown[0], unknown[1]);
        else if (knownOut.letters.size() == 2) Connect.watson("getWords", 1, knownOutChars[0], knownOutChars[1], unknown[0], unknown[1], unknown[2]);
        else if (knownOut.letters.size() == 1) Connect.watson("getWords", 1, knownOutChars[0], unknown[0], unknown[1], unknown[2], unknown[3]);

        System.out.println("--- Result.case0100 ---");
    }
    private static void case0101(LetterGroup knownOut, LinkedList<Turn> Turns) {

        System.out.println("+++ Result.case0101 +++");  //  knownOut, Previous turns has data, all else not

        int lastTurn = Turns.size() - 1;

        char[] unknown = keySetToArray(Unknown.letters);
        char[] knownOutChars = keySetToArray(knownOut.letters);
        char[] lastTurnChars = keySetToArray(Turns.get(lastTurn).turn);

        //  Make a guess with as many letters from the last turn as possible, 1 known out, and the remainder being unknown...
        if      (knownOut.letters.size() >= 4 && Turns.get(lastTurn).turn.size() >= 3) Connect.watson("getWords", 1, knownOutChars[0], lastTurnChars[0], lastTurnChars[1], lastTurnChars[2], unknown[0]);
        else if (Turns.get(lastTurn).turn.size() == 3)                                 Connect.watson("getWords", 1, knownOutChars[0], lastTurnChars[0], lastTurnChars[1], unknown[0], unknown[1]);
        else if (Turns.get(lastTurn).turn.size() == 2)                                 Connect.watson("getWords", 1, knownOutChars[0], lastTurnChars[0], unknown[0], unknown[1], unknown[2]);
        else if (knownOut.letters.size() == 1)                                         Connect.watson("getWords", 1, knownOutChars[0], unknown[0], unknown[1], unknown[2], unknown[3]);

        System.out.println("--- Result.case0101 ---");
    }
    private static void case0110(LetterGroup knownOut, LetterGroup knownTogether) {

        System.out.println("+++ Result.case0110 +++");  //  knownOut, knownTogether has data, all else not

        char[] knownOutChars = keySetToArray(knownOut.letters);
        char[] knownTogetherChars = keySetToArray(knownTogether.letters);
        //  ToDo
        //  Make a guess with the 2nd MOST FREQUENT from knownTogether (to try and make a determination on the 1st), as many letters as possible from the previous guess, and 1 unknown letter...
        if      (knownTogether.letters.size() >= 4) Connect.watson("getWords", 1, knownTogetherChars[1], knownOutChars[0], knownOutChars[1], knownOutChars[2], knownOutChars[3]);
        else if (knownTogether.letters.size() == 3) Connect.watson("getWords", 1, knownTogetherChars[1], knownOutChars[0], knownOutChars[1], knownOutChars[2], knownOutChars[3]);
        else if (knownTogether.letters.size() == 2) Connect.watson("getWords", 1, knownTogetherChars[1], knownOutChars[0], knownOutChars[1], knownOutChars[2], knownOutChars[3]);

        System.out.println("--- Result.case0110 ---");
    }
    private static void case0111() {
        System.out.println("+++ Result.case0111 +++");  //  knownIn is EMPTY
        System.out.println("--- Result.case0111 ---");
    }
    private static void case1000(LetterGroup knownIn) {
        System.out.println("Scenario: 1000");  //  knownIn has data, all else not

        char[] unknown = keySetToArray(Unknown.letters);
        char[] knownInChars = keySetToArray(knownIn.letters);

        //  Make a guess with as many letters as possible from knownIn, and the rest from unknown...
        if      (knownIn.letters.size() == 4) Connect.watson("getWords", 1, knownInChars[0], knownInChars[1], knownInChars[2], knownInChars[3], unknown[1]);
        else if (knownIn.letters.size() == 3) Connect.watson("getWords", 1, knownInChars[0], knownInChars[1], knownInChars[2], unknown[0], unknown[1]);
        else if (knownIn.letters.size() == 2) Connect.watson("getWords", 1, knownInChars[0], knownInChars[1], unknown[0], unknown[1], unknown[2]);
        else if (knownIn.letters.size() == 1) Connect.watson("getWords", 1, knownInChars[0], unknown[0], unknown[1], unknown[2], unknown[3]);

        System.out.println("--- Result.case1000 ---");
    }
    private static void case1001() {
        System.out.println("+++ Result.case1001 +++");  //  knownIn, Previous turns has data, all else not
        System.out.println("--- Result.case1001 ---");
    }
    private static void case1010() {
        System.out.println("+++ Result.case1010 +++");  //  knownIn, knownTogether has data, all else not
        System.out.println("--- Result.case1010 ---");
    }
    private static void case1011() {

        System.out.println("+++ Result.case1011 +++");  //  knownOut is EMPTY, all else not
        System.out.println("--- Result.case1011 ---");
    }
    private static void case1100() {
        System.out.println("+++ Result.case1100 +++");  //  knownIn, knownOut has data, all else not
        System.out.println("--- Result.case1100 ---");
    }
    private static void case1101() {
        System.out.println("+++ Result.case1101 +++");  //  knownTogether is EMPTY
        System.out.println("--- Result.case1101 ---");
    }
    private static void case1110() {
        System.out.println("+++ Result.case1110 +++");  //  Previous turns is EMPTY
        System.out.println("--- Result.case1110 ---");
    }
    private static void case1111() {
        System.out.println("+++ Result.case1111 +++");  //  Data in ALL
        System.out.println("--- Result.case1111 ---");
    }

    public static char[] keySetToArray(LinkedHashMap<Character, Integer> map) {

        Object[] o;
        char[] sortedLetters = new char[map.size()];

        Set<Character> letterKeys = map.keySet();

        o = letterKeys.toArray();

        for(int i = 0; i < map.size(); i++) {
            sortedLetters[i] = (char) o[i];
        }
        return sortedLetters;
    }
}
