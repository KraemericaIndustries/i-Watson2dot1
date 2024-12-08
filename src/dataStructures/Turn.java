package dataStructures;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Turn {

    public String guess;
    public int response;
    public int updatedResponse;
    public LinkedHashMap<Character, Integer> turn = new LinkedHashMap<>();

    public Turn(String guess, int response) {

        turn.clear();

        this.guess = guess.toUpperCase();
        this.response = response;
        this.updatedResponse = response;

        parseGuessToCollection(guess);
//        System.out.println("Turn Constructor original parse: " + turn);  DEBUG
        sortTurn(turn);
    }
    public void parseGuessToCollection(String guess) {

        guess = guess.toUpperCase();
        turn.clear();

        for(int i = 0; i < guess.length(); i++) {

            switch(guess.charAt(i)) {
                case 'A':
                    turn.put('A', Unknown.letters.get('A'));
                    break;
                case 'B':
                    turn.put('B', Unknown.letters.get('B'));
                    break;
                case 'C':
                    turn.put('C', Unknown.letters.get('C'));
                    break;
                case 'D':
                    turn.put('D', Unknown.letters.get('D'));
                    break;
                case 'E':
                    turn.put('E', Unknown.letters.get('E'));
                    break;
                case 'F':
                    turn.put('F', Unknown.letters.get('F'));
                    break;
                case 'G':
                    turn.put('G', Unknown.letters.get('G'));
                    break;
                case 'H':
                    turn.put('H', Unknown.letters.get('H'));
                    break;
                case 'I':
                    turn.put('I', Unknown.letters.get('I'));
                    break;
                case 'J':
                    turn.put('J', Unknown.letters.get('J'));
                    break;
                case 'K':
                    turn.put('K', Unknown.letters.get('K'));
                    break;
                case 'L':
                    turn.put('L', Unknown.letters.get('L'));
                    break;
                case 'M':
                    turn.put('M', Unknown.letters.get('M'));
                    break;
                case 'N':
                    turn.put('N', Unknown.letters.get('N'));
                    break;
                case 'O':
                    turn.put('O', Unknown.letters.get('O'));
                    break;
                case 'P':
                    turn.put('P', Unknown.letters.get('P'));
                    break;
                case 'Q':
                    turn.put('Q', Unknown.letters.get('Q'));
                    break;
                case 'R':
                    turn.put('R', Unknown.letters.get('R'));
                    break;
                case 'S':
                    turn.put('S', Unknown.letters.get('S'));
                    break;
                case 'T':
                    turn.put('T', Unknown.letters.get('T'));
                    break;
                case 'U':
                    turn.put('U', Unknown.letters.get('U'));
                    break;
                case 'V':
                    turn.put('V', Unknown.letters.get('V'));
                    break;
                case 'W':
                    turn.put('W', Unknown.letters.get('W'));
                    break;
                case 'X':
                    turn.put('X', Unknown.letters.get('X'));
                    break;
                case 'Y':
                    turn.put('Y', Unknown.letters.get('Y'));
                    break;
                case 'Z':
                    turn.put('Z', Unknown.letters.get('Z'));
                    break;
                default:
                    break;
            }
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