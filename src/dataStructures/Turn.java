package dataStructures;
import java.util.*;
import java.util.stream.Collectors;

public class Turn {

    public String guess;
    public String updatedGuess;
    public int response;
    public int updatedResponse;
//    public ArrayList<Character> turn = new ArrayList<>();
    public Set<Character> turn = new HashSet<>();

    public Turn(String guess, int response) {

        turn.clear();

        this.guess = guess.toUpperCase();
        this.updatedGuess = guess;
        this.response = response;
        this.updatedResponse = response;

        parseGuessToCollection(guess);
//        System.out.println("Turn Constructor original parse: " + turn);  DEBUG
//        sortTurn(turn);
    }
    public void parseGuessToCollection(String guess) {

        guess = guess.toUpperCase();
        turn.clear();

        for(int i = 0; i < guess.length(); i++) {
            turn.add(guess.charAt(i));
        }

    }
    public static void sortTurn(LinkedHashMap<Character, Integer> turn) {
//        System.out.println("sortTurn() original:             " + turn);  DEBUG
        LinkedHashMap<Character, Integer> sortedTurn = turn.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
//        System.out.println("sortTurn() sortedTurn:           " + sortedTurn);  DEBUG
        turn.clear();
        turn.putAll(sortedTurn);
//        System.out.println("sortTurn() result:               " + turn);  DEBUG
        sortedTurn.clear();
        System.out.println();
    }
}