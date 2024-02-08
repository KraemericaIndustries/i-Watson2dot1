package dataStructures;

import java.util.LinkedHashMap;
import java.util.Set;

public class LetterGroup {
    public LinkedHashMap<Character, Integer> letters = new LinkedHashMap<>();
    public Object[] elements;
    
//    public void sort() {
//        LinkedHashMap<Character, Integer> sortedMap = letters.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
//        letters.clear();
//        letters.putAll(sortedMap);
//        sortedMap.clear();
//    }

    public void keySetToArray() {
        Set<Character> letterKeys = letters.keySet();
        elements = letterKeys.toArray();
    }
}