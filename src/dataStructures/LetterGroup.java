package dataStructures;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LetterGroup {
    public LinkedHashMap<Character, Integer> letters = new LinkedHashMap<>();
    
    public void sort() {
//        System.out.print("Sorted:   ");
        LinkedHashMap<Character, Integer> sortedMap = letters.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
//        System.out.println(sortedMap);

        letters.clear();
        letters.putAll(sortedMap);
//        System.out.println("sortTurn() result:               " + turn);
        sortedMap.clear();

    }
}