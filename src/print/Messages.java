package print;
public class Messages {
    public static void welcome() {
        //  Introduce the game, and how it is played
        System.out.println("*****************************************************************  WELCOME  *******************************************************************************************");
        System.out.println("Welcome to the Word Guessing Game Helper!\n" +
                "\n" +
                "Your opponent will  choose a familiar 5 letter word, with each letter appearing ONLY ONCE. (Valid: 'GLYPH'.  Invalid: 'DROOP')\n" +
                "See if you can guess the word!  Each time you make a guess, your opponent will respond with a number.\n" +
                "The number represents the number of letters in your guess that appear in the word chosen by your opponent.\n" +
                "Example:  If the opponent chooses 'LOSER' and you guess 'POSER' the response would be 4 ('O' makes 1, 'S' makes 2, 'E' makes 3, and 'R' makes 4.)\n" +
                "Will you be able to identify your opponent's word?\n" +
                "I'm going to help - by suggesting your most strategic plays possible!\n" +
                "First - let me get myself set up...");
        System.out.println("***********************************************************************************************************************************************************************");
        System.out.println();
    }
}