package classify;

import java.util.*;

public class Classification {

    // STATE of RESPONSES
    public boolean updatedResponsesSame;
    public boolean updatedResponsesDifferByOne;
    public boolean updatedResponsesDifferByMoreThanOne;

    // STATE of UPDATED GUESS LENGTH
    public boolean updatedGuessesSameLength;
    public boolean updatedGuessesDifferInLengthByOne;
    public boolean updatedGuessesDifferInLengthByMoreThanOne;

    // STATE of UPDATED GUESS CONTENTS
    public boolean updatedGuessesSame;
    public Set<Character> onlyInFirst;
    public Set<Character> inBoth;
    public Set<Character> onlyInSecond;

    // CONSTRUCTOR
    public Classification(int iUpdatedResponse, int jUpdatedResponse, String iUpdatedGuess, String jUpdatedGuess) {
        updatedResponsesSame = checkForUpdatedResponseSame(iUpdatedResponse, jUpdatedResponse);
        if (!updatedResponsesSame) updatedResponsesDifferByOne = checkForUpdatedResponsesDifferByOne(iUpdatedResponse, jUpdatedResponse);
        if (!updatedResponsesSame && !updatedResponsesDifferByOne) updatedResponsesDifferByMoreThanOne = checkForUpdatedResponsesDifferByMoreThanOne(iUpdatedResponse, jUpdatedResponse);

        updatedGuessesSameLength = checkForUpdatedGuessesSameLength (iUpdatedGuess, jUpdatedGuess);
        if (!updatedGuessesSameLength) updatedGuessesDifferInLengthByOne = checkForUpdatedGuessesDifferInLengthByOne(iUpdatedGuess, jUpdatedGuess);
        if (!updatedGuessesSameLength && !updatedGuessesDifferInLengthByOne) updatedGuessesDifferInLengthByMoreThanOne = checkForUpdatedGuessesDifferInLengthByMoreThanOne(iUpdatedGuess, jUpdatedGuess);

        if(iUpdatedGuess.equals(jUpdatedGuess)) updatedGuessesSame = true;

        //  COPILOT String comparison:
        Set<Character> set1 = toCharSet(iUpdatedGuess);
        Set<Character> temp = toCharSet(iUpdatedGuess);
        Set<Character> set2 = toCharSet(jUpdatedGuess);

        // Characters in s1 but not s2
//        Set<Character> onlyInFirstCons = new HashSet<>(set1);
//        onlyInFirstCons.removeAll(set2);
//        onlyInFirst = onlyInFirstCons;
//        onlyInFirst.addAll(onlyInFirstCons);
        set1.removeAll(set2);
        onlyInFirst = set1;

        //only in second
        set2.removeAll(temp);
        onlyInSecond = set2;


        // Characters in both
//        Set<Character> inBothCons = new HashSet<>(set1);
//        inBothCons.retainAll(set2);
//        inBoth = inBothCons;
//        inBoth.addAll(inBothCons);
        temp.removeAll(set1);
        temp.removeAll(set2);
        inBoth = temp;


        // Characters in s2 but not s1
//        Set<Character> onlyInSecondCons = new HashSet<>(set2);
//        onlyInSecondCons.removeAll(temp);
//        onlyInSecond = onlyInSecondCons;
//        onlyInSecond.addAll(onlyInSecondCons);
    }

    private static boolean checkForUpdatedGuessesDifferInLengthByMoreThanOne(String iUpdatedGuess, String jUpdatedGuess) {
        return iUpdatedGuess.length() - jUpdatedGuess.length() >= 2 || iUpdatedGuess.length() - jUpdatedGuess.length() <= -2;
    }

    private static boolean checkForUpdatedGuessesDifferInLengthByOne(String iUpdatedGuess, String jUpdatedGuess) {
        return iUpdatedGuess.length() - jUpdatedGuess.length() == 1 || iUpdatedGuess.length() - jUpdatedGuess.length() == -1;
    }

    private static boolean checkForUpdatedGuessesSameLength(String iUpdatedGuess, String jUpdatedGuess) {
        if(iUpdatedGuess.length() == jUpdatedGuess.length()) return true;
        else return false;
    }

    private static boolean checkForUpdatedResponsesDifferByMoreThanOne(int i, int j) {
        return i - j > 1 || i - j < -1;
    }

    private static boolean checkForUpdatedResponsesDifferByOne(int i, int j) {
        return i - j == 1 || i - j == -1;
    }

    private static boolean checkForUpdatedResponseSame(int i, int j) {
        if(i == j) return true;
        else return false;
    }

    private static Set<Character> toCharSet(String s) {
        Set<Character> set = new HashSet<>();
        for (char c : s.toCharArray()) {
            set.add(c);
        }
        return set;
    }

    public Set<Character> getOnlyInFirst() {
        return onlyInFirst;
    }

    public Set<Character> getInBoth() {
        return inBoth;
    }

    public Set<Character> getOnlyInSecond() {
        return onlyInSecond;
    }

    public void printClassification() {
        System.out.println("    Only in first:  " + onlyInFirst);
        System.out.println("    In both:        " + inBoth);
        System.out.println("    Only in second: " + onlyInSecond);
    }

    public void printFindings() {
        if(updatedResponsesSame) System.out.println(" - Updated responses are the same");;
        if(updatedResponsesDifferByOne) System.out.println(" - Updated responses differ by one");;
        if(updatedResponsesDifferByMoreThanOne) System.out.println(" - Updated responses differ by more than one");;
        if(updatedGuessesSameLength) System.out.println(" - Updated guesses are the same length");
        if(updatedGuessesDifferInLengthByOne) System.out.println(" - Updated guesses differ in length by one");;
        if(updatedGuessesDifferInLengthByMoreThanOne) System.out.println(" - Updated guesses differ in length by one");;
    }
}
