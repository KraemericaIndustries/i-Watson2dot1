package dataStructures;

import java.util.LinkedHashMap;
import java.util.Set;

public class LetterGroup {
    public LinkedHashMap<Character, Integer> letters = new LinkedHashMap<>();
    public Object[] elements;

    public void keySetToArray() {
        Set<Character> letterKeys = letters.keySet();
        elements = letterKeys.toArray();
    }

    public void loadLettersFromString(String s) {
        for(int i = 0; i < s.length(); i++) {
            this.letters.put(s.charAt(i), 1);
        }
    }
}