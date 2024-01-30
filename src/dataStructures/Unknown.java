package dataStructures;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Unknown {
    public LinkedHashMap<Character, Integer> letters = new LinkedHashMap<>();
    static private int[] letterCounts = new int[26];


    public static void letterEnumerator(String word) {

        for(int i = 0; i < word.length(); i++) {
            switch (word.charAt(i)) {
                case 'A' -> letterCounts[0]++;
                case 'B' -> letterCounts[1]++;
                case 'C' -> letterCounts[2]++;
                case 'D' -> letterCounts[3]++;
                case 'E' -> letterCounts[4]++;
                case 'F' -> letterCounts[5]++;
                case 'G' -> letterCounts[6]++;
                case 'H' -> letterCounts[7]++;
                case 'I' -> letterCounts[8]++;
                case 'J' -> letterCounts[9]++;
                case 'K' -> letterCounts[10]++;
                case 'L' -> letterCounts[11]++;
                case 'M' -> letterCounts[12]++;
                case 'N' -> letterCounts[13]++;
                case 'O' -> letterCounts[14]++;
                case 'P' -> letterCounts[15]++;
                case 'Q' -> letterCounts[16]++;
                case 'R' -> letterCounts[17]++;
                case 'S' -> letterCounts[18]++;
                case 'T' -> letterCounts[19]++;
                case 'U' -> letterCounts[20]++;
                case 'V' -> letterCounts[21]++;
                case 'W' -> letterCounts[22]++;
                case 'X' -> letterCounts[23]++;
                case 'Y' -> letterCounts[24]++;
                case 'Z' -> letterCounts[25]++;
                default -> System.out.println("Unknown letter, or some other flaw");
            }
        }
    }  //  End-of-letterEnumerator()

    public void loadLinkedHashMap() {
        for (int i = 0; i < letterCounts.length; i++) {
            letters.put((char)(i + 65), letterCounts[i]);
        }
    }
    
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