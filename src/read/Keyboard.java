package read;

import print.Messages;

import java.util.Scanner;

public class Keyboard {

    //  INPUT a guess...
    public static String guess() {

        String guess;

        do {
            System.out.print("Turn # " + (Messages.reportNumber) + ".  Guess a 5 letter word: ");
            Scanner input= new Scanner(System.in);
            guess = input.nextLine();
            guess = guess.toUpperCase();  //  Regardless of the case in the InputStream typed at the keyboard, convert the String contents of 'guess' variable to UPPERCASE
            if (guess.length() != 5) {    //  Discard guesses that are NOT 5 letters.
                System.out.println("HEY!  DUMMY!  Your guess is " + guess.length() + " letters long!  Try again, moron.");
            }
        } while (guess.length() != 5);
        return guess;
    }

    //  INPUT a response...
    public static int responseFromOpponent() {

        int response;

        do {
            System.out.print("What was the response?: ");
            Scanner input= new Scanner(System.in);
            response = input.nextInt();

            if (response > 5 || response < 0) {  //  Discard responses that are NOT 0 through 5
                System.out.println("HEY!  DUMMY!  Your response is " + response + ", but it MUST be 0, 1, 2, 3, 4, or 5!  Try again, moron.");
            }
        } while (response > 5 || response < 0);
        return response;
    }

    //  VERIFY last guess is opponents word...
    public static boolean verify(String lastGuess) {

        boolean guessIsWord = false;
        String yesOrNo;

        do {
            System.out.print("\nYour last guess was " + lastGuess + ".  Is " + lastGuess + " your opponents word? ('y' or 'n'): ");
            Scanner input= new Scanner(System.in);
            yesOrNo = input.nextLine();
            yesOrNo = yesOrNo.toUpperCase();  //  Regardless of the case in the InputStream typed at the keyboard, convert the String contents of 'guess' variable to UPPERCASE
        } while (!(yesOrNo.equals("Y")) && !(yesOrNo.equals("N")));

        if(yesOrNo.equals("Y")) guessIsWord = true;
        return guessIsWord;
    }

    //  The opponents word is not in the database.  Enter it for inclusion during subsequent plays...
    public static String enterUnknownWord() {
        //  System.out.println("Entering 'IO.Turn.enterInfo()' method...");  //  DEBUG
        Scanner input= new Scanner(System.in);
        String capture = input.nextLine();
        //  input.close();
        //  System.out.println("keyboard:capture = " + capture);            //  DEBUG
        //  System.out.println("Exiting 'MakeGuess:keyboard' method...");   //  DEBUG
        System.out.println();
        capture = capture.toUpperCase();  //  Regardless of the case in the InputStream typed at the keyboard, convert the String contents of the 'guessString' variable to UPPERCASE
        return capture;
    }
}