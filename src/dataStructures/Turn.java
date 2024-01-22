package dataStructures;

public class Turn {
    public String guess;         //  Used to save the original guess made
    public int response;         //  Used to save the original response from your opponent
    public int updatedResponse;  //  Letters determined to be KNOWN-IN can be removed from previous guesses.  updatedResponse DECREMENTS the response for previous guesses accordingly
    public int[][] sortedGuess= new int[2][6];  //  5 letters per guess, and a 6th column to support sorting guesses by frequency (used downstream for strategic play)
    public Turn(String guess, int response, LetterGroup2D frequency) {
        this.guess = guess.toUpperCase();  //  Uppercase reads nicer.  Lowercase is easier to type
        this.response = response;
        this.updatedResponse = response;

        int sortedGuessIndex = 0;

        for(int guessIndex = 0; guessIndex < guess.length(); guessIndex++) {

            int frequencyIndex;

            //  Used to save the guess made, having re-arranged the letters into the order of frequency-of-occurrence of letters as they appear in the database
            //  This will be used further  downstream for strategic play
            for(frequencyIndex = 0; frequencyIndex < frequency.array2D[0].length; frequencyIndex++) {

                int guessLetterAscii = (int)guess.charAt(guessIndex);
                int frequencyLetterAscii = frequency.array2D[0][frequencyIndex];

                if (guessLetterAscii ==  frequencyLetterAscii) {
                    sortedGuess[0][sortedGuessIndex] = (int)guess.charAt(guessIndex);
                    sortedGuess[1][sortedGuessIndex] = frequency.array2D[1][frequencyIndex];
                    sortedGuessIndex++;
                    break;
                }
            }
        }
        //  SORT guesses as they are made, re-arranging the letters of the guess into frequency-of-occurrence in the database
        sortByFrequency(sortedGuess);
    }

    //  PRINT a turn...
    public void printTurn() {

        System.out.print("You previously guessed " + guess + ", and the response was " + response + ".  If I remove letters for which a determination has been made, this means " + updatedResponse + " of ");
        for(int i = 0; i < sortedGuess[0].length - 1; i++) {
            System.out.print((char)sortedGuess[0][i]);
        }
        System.out.println(" are in your opponents word.");
    }

    //  SORT a guess by letter frequency (which will be needed to drive suggestions for future guesses)...
    public static void sortByFrequency(int[][] sortedGuess) {
        for (int c = 0; c < 5; c++) {                                            //  FOR every column (of our UNSORTED range of letters)
            for(int r = 0; r < sortedGuess.length; r++) {                        //  FOR each row in that column
                sortedGuess[r][sortedGuess[r].length-1] = sortedGuess[r][c];     //  COPY the values of that column to column index#5 (#5 being a temporary location)
            }
            int index = c - 1;                                                                       //  The index position of the last data element known to be sorted correctly
            while (index >= 0 && sortedGuess[1][index] < sortedGuess[1][sortedGuess[1].length-1]) {  //  WHILE sortCorrectly index >= 0 AND check that the value at that index is LESS THAN the row to sorted in column 5
                for(int r = 0; r < sortedGuess.length; r++) {
                    sortedGuess[r][index + 1] = sortedGuess[r][index];                               //  COPY the values known to be sorted correctly one row to the RIGHT
                }
                index--;                                                                             //  DECREMENT the index (to check the index value against the value at the previous index)
            }
            for(int r = 0; r < sortedGuess.length; r++) {
                sortedGuess[r][index+1] = sortedGuess[r][5];                                         //  COPY the values of the column to be sorted to the column FOLLOWING the last column known to be sorted correctly
            }
        }
    }
}