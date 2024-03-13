package models;

import java.util.LinkedHashMap;
import java.util.Set;

public class LetterGroup {
    public LinkedHashMap<Character, Integer> letters = new LinkedHashMap<>();
    public Object[] elements;

    public void keySetToArray() {
        Set<Character> letterKeys = letters.keySet();
        elements = letterKeys.toArray();
    }
}