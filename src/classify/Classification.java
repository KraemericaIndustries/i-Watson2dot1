package classify;

import java.util.*;

public class Classification {

    // STATE of RESPONSES
    boolean updatedResponsesSame;
    boolean updatedResponsesDifferByOne;
    boolean updatedResponsesDifferByMoreThanOne;

    // STATE of UPDATED GUESS LENGTH
    boolean updatedGuessesSameLength;
    boolean updatedGuessesDifferInLengthByOne;
    boolean updatedGuessesDifferInLengthByMoreThanOne;

    // STATE of UPDATED GUESS CONTENTS
    private final Set<Character> onlyInFirst = getOnlyInFirst();
    private final Set<Character> inBoth = getInBoth();
    private final Set<Character> onlyInSecond = getOnlyInSecond();

    // CONSTRUCTOR
    public Classification(int iUpdatedResponse, int jUpdatedResponse, String iUpdatedGuess, String jUpdatedGuess) {
        updatedResponsesSame = checkForUpdatedResponseSame(iUpdatedResponse, jUpdatedResponse);
        if (!updatedResponsesSame) updatedResponsesDifferByOne = checkForUpdatedResponsesDifferByOne(iUpdatedResponse, jUpdatedResponse);
        if (!updatedResponsesSame && !updatedResponsesDifferByOne) updatedResponsesDifferByMoreThanOne = checkForUpdatedResponsesDifferByMoreThanOne(iUpdatedResponse, jUpdatedResponse);

        updatedGuessesSameLength = checkForUpdatedGuessesSameLength (iUpdatedGuess, jUpdatedGuess);
        if (!updatedGuessesSameLength) updatedGuessesDifferInLengthByOne = checkForUpdatedGuessesDifferInLengthByOne(iUpdatedGuess, jUpdatedGuess);
        if (!updatedGuessesSameLength && !updatedGuessesDifferInLengthByOne) updatedGuessesDifferInLengthByMoreThanOne = checkForUpdatedGuessesDifferInLengthByMoreThanOne(iUpdatedGuess, jUpdatedGuess);

        //  COPILOT String comparison:
        Set<Character> set1 = toCharSet(iUpdatedGuess);
        Set<Character> set2 = toCharSet(jUpdatedGuess);

        // Characters in s1 but not s2
        Set<Character> onlyInFirst = new HashSet<>(set1);
        onlyInFirst.removeAll(set2);

        // Characters in both
        Set<Character> inBoth = new HashSet<>(set1);
        inBoth.retainAll(set2);

        // Characters in s2 but not s1
        Set<Character> onlyInSecond = new HashSet<>(set2);
        onlyInSecond.removeAll(set1);

        System.out.println("Only in first: " + onlyInFirst);
        System.out.println("In both: " + inBoth);
        System.out.println("Only in second: " + onlyInSecond);
    }

    private static boolean checkForUpdatedGuessesDifferInLengthByMoreThanOne(String iUpdatedGuess, String jUpdatedGuess) {
        return iUpdatedGuess.length() - jUpdatedGuess.length() >= 2 || iUpdatedGuess.length() - jUpdatedGuess.length() <= -2;
    }

    private static boolean checkForUpdatedGuessesDifferInLengthByOne(String iUpdatedGuess, String jUpdatedGuess) {
        return iUpdatedGuess.length() - jUpdatedGuess.length() == 1 || iUpdatedGuess.length() - jUpdatedGuess.length() == -1;
    }

    private static boolean checkForUpdatedGuessesSameLength(String iUpdatedGuess, String jUpdatedGuess) {
        if(iUpdatedGuess.length() == jUpdatedGuess.length()) {
            System.out.println(" - Updated guesses are the same length.");
            return true;
        } else {
            System.out.println(" - Updated guesses are the different in length.");
        }
        return false;
    }

    private static boolean checkForUpdatedResponsesDifferByMoreThanOne(int i, int j) {
        return i - j > 1 || i - j < -1;
    }

    private static boolean checkForUpdatedResponsesDifferByOne(int i, int j) {
        return i - j == 1 || i - j == -1;
    }

    private static boolean checkForUpdatedResponseSame(int i, int j) {
        if(i == j) {
            System.out.println(" - Updated responses are the same.");
            return true;
        } else {
            System.out.println(" - Updated responses are different.");
        }
        return false;
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
}
