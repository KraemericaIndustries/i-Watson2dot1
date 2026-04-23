package print;

public class Messages {

    public static int reportNumber = 1;

    //  Introduce the game, and how it is played...
    public static void welcome() {
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_BLUE,"""
                *****************************************************************  WELCOME  *******************************************************************************************
                Welcome to the Word Guessing Game Helper!

                Your opponent will  choose a familiar 5 letter word, with each letter appearing ONLY ONCE. (Valid: 'GLYPH'.  Invalid: 'DROOP')
                See if you can guess the word!  Each time you make a guess, your opponent will respond with a number.
                The number represents the number of letters in your guess that appear in the word chosen by your opponent.
                Example:  If the opponent chooses 'LOSER' and you guess 'POSER' the response would be 4 ('O' makes 1, 'S' makes 2, 'E' makes 3, and 'R' makes 4.)
                Will you be able to identify your opponent's word?
                I'm going to help - by suggesting your most strategic plays possible!
                First - let me get myself set up...
                ***********************************************************************************************************************************************************************
                """));
    }

    //  START the game...
    public static void play() {
        System.out.println(Colors.Ansi.paint(Colors.Ansi.BRIGHT_GREEN, """
        ********************************************************************************  THE GAME  ***********************************************************************************
        Let's play!!!
        *******************************************************************************************************************************************************************************
        """));
    }

    //  SUCCESS!  The opponents word has been determined...
    public static void victorySummary(String lastGuess) {
        System.out.println("\nGame over man!!!  The opponents word was determined in " + (Messages.reportNumber - 1) + " turns!");
        System.out.println("Your opponents word was: " + lastGuess);
        System.out.println("It took you " + (Messages.reportNumber) + " turns to determine your opponents word!");
    }
}