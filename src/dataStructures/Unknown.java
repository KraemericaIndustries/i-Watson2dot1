package dataStructures;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Unknown {

    public static LinkedHashMap<Character, Integer> letters = new LinkedHashMap<>();
    public char[] unsortedLettersFromMap;  //  To create char[] sortedLetters, a temporary array is declared and only initialized once the map has been populated with letter occurrences read in from all words
    public char[] sortedLetters;  //  DOWNSTREAM DB query for words unable to get key from map by index.  Creating this sorted array to accommodate that
    public Object[] elements;  //  Needed for keySetToArray

    //  Ternary operator evaluates IF a key in the Character map already exists.  If yes, increment the value, if no, add key with value = 1
    //  https://stackoverflow.com/questions/81346/most-efficient-way-to-increment-a-map-value-in-java
    public static void letterEnumerator(String word) {
        for(int i = 0; i <word.length(); i++) {
            int count = letters.containsKey(word.charAt(i)) ? letters.get(word.charAt(i)) : 0;
            letters.put(word.charAt(i), count + 1);
        }
    }

    //  SORT the letter-frequency key-value map by value (so we know which letters are the most frequent, in descending order)...
    public void sort() {
        LinkedHashMap<Character, Integer> sortedMap = letters.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        letters.clear();
        letters.putAll(sortedMap);
        sortedMap.clear();
    }

    //  LOAD the sorted letters into the char[] sortedLetters field giving it greater scope...
    public void loadSortedLetters(LinkedHashMap<Character, Integer> letters) {

        unsortedLettersFromMap = new char[letters.size()];
        int i = 0;

        for(Map.Entry<Character, Integer> entry : letters.entrySet()) {
            unsortedLettersFromMap[i] = entry.getKey();
            i++;
        }
        sortedLetters = unsortedLettersFromMap;
    }
    public void keySetToArray() {
        Set<Character> letterKeys = letters.keySet();
        elements = letterKeys.toArray();
//        System.out.println();
    }
}