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

    public Turn(String guess, int response, Unknown unknown) {

        turn.clear();

        this.guess = guess.toUpperCase();
        this.response = response;

        parseGuessToCollection(guess, unknown);
        System.out.println("Turn Constructor original parse: " + turn);
        sortTurn(turn);
    }
    public void parseGuessToCollection(String guess, Unknown unknown) {

        guess = guess.toUpperCase();

        for(int i = 0; i < guess.length(); i++) {

            switch(guess.charAt(i)) {
                case 'A':
                    turn.put('A', unknown.letters.get('A'));
                    break;
                case 'B':
                    turn.put('B', unknown.letters.get('B'));
                    break;
                case 'C':
                    turn.put('C', unknown.letters.get('C'));
                    break;
                case 'D':
                    turn.put('D', unknown.letters.get('D'));
                    break;
                case 'E':
                    turn.put('E', unknown.letters.get('E'));
                    break;
                case 'F':
                    turn.put('F', unknown.letters.get('F'));
                    break;
                case 'G':
                    turn.put('G', unknown.letters.get('G'));
                    break;
                case 'H':
                    turn.put('H', unknown.letters.get('H'));
                    break;
                case 'I':
                    turn.put('I', unknown.letters.get('I'));
                    break;
                case 'J':
                    turn.put('J', unknown.letters.get('J'));
                    break;
                case 'K':
                    turn.put('K', unknown.letters.get('K'));
                    break;
                case 'L':
                    turn.put('L', unknown.letters.get('L'));
                    break;
                case 'M':
                    turn.put('M', unknown.letters.get('M'));
                    break;
                case 'N':
                    turn.put('N', unknown.letters.get('N'));
                    break;
                case 'O':
                    turn.put('O', unknown.letters.get('O'));
                    break;
                case 'P':
                    turn.put('P', unknown.letters.get('P'));
                    break;
                case 'Q':
                    turn.put('Q', unknown.letters.get('Q'));
                    break;
                case 'R':
                    turn.put('R', unknown.letters.get('R'));
                    break;
                case 'S':
                    turn.put('S', unknown.letters.get('S'));
                    break;
                case 'T':
                    turn.put('T', unknown.letters.get('T'));
                    break;
                case 'U':
                    turn.put('U', unknown.letters.get('U'));
                    break;
                case 'V':
                    turn.put('V', unknown.letters.get('V'));
                    break;
                case 'W':
                    turn.put('W', unknown.letters.get('W'));
                    break;
                case 'X':
                    turn.put('X', unknown.letters.get('X'));
                    break;
                case 'Y':
                    turn.put('Y', unknown.letters.get('Y'));
                    break;
                case 'Z':
                    turn.put('Z', unknown.letters.get('Z'));
                    break;
                default:
                    break;
            }
        }
    }
    public static void sortTurn(LinkedHashMap<Character, Integer> turn) {
        System.out.println("sortTurn() original:             " + turn);
        LinkedHashMap<Character, Integer> sortedTurn = turn.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        System.out.println("sortTurn() sortedTurn:           " + sortedTurn);
        turn.clear();
        turn.putAll(sortedTurn);
        System.out.println("sortTurn() result:               " + turn);
        sortedTurn.clear();
        System.out.println();
    }
}