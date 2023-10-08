package read;

import dataStructures.Matrix;

import java.util.Scanner;

public class Keyboard {

    //  INPUT a guess...
    public static void guess() {

        String guess;

        do {
            System.out.print("Turn # " + (Matrix.turnIndex - 5) + ".  Guess a 5 letter word: ");
            Scanner input= new Scanner(System.in);
            guess = input.nextLine();
            System.out.println();
            guess = guess.toUpperCase();  //  Regardless of the case in the InputStream typed at the keyboard, convert the String contents of the 'guessString' variable to UPPERCASE

            if (guess.length() != 5) {
                System.out.println("HEY!  DUMMY!  Your guess is " + guess.length() + " letters long!  Try again, moron.");
            }

        } while (guess.length() != 5);  //  Discard guesses that are NOT 5 letters.
        dataStructures.Matrix.pushGuess(guess);
    }

    //  INPUT a response...
    public static void response() {

        int response;

        do {
            System.out.println("What was the response?: ");
            Scanner input= new Scanner(System.in);
            response = input.nextInt();
            System.out.println();

            if (response > 5 || response < 0) {
                System.out.println("HEY!  DUMMY!  Your response is " + response + ", but it MUST be 0, 1, 2, 3, 4, or 5!  Try again, moron.");
            }

        } while (response > 5 || response < 0);  //  Discard responses that are NOT 0 through 5.
        dataStructures.Matrix.pushResponse(response);
    }
}