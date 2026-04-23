package dataStructures;
import java.util.*;

public class Turn {

    public String guess;
    public String updatedGuess;
    public int response;
    public int updatedResponse;
    public Set<Character> turn = new HashSet<>();

    public Turn(String guess, int response) {

        turn.clear();

        this.guess = guess.toUpperCase();
        this.updatedGuess = guess;
        this.response = response;
        this.updatedResponse = response;

        parseGuessToCollection(guess);
    }

    public void parseGuessToCollection(String guess) {

        guess = guess.toUpperCase();
        turn.clear();

        for(int i = 0; i < guess.length(); i++) {
            turn.add(guess.charAt(i));
        }
    }

    public void parseCollectionToString() {

        StringBuilder sb = new StringBuilder();

        for(Character c : turn) {
            sb.append(c);
        }

        this.updatedGuess = sb.toString();
    }
}