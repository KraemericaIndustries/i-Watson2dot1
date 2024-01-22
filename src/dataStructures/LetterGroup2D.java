package dataStructures;

public class LetterGroup2D {

    boolean hasLetters = false;
    public int[][] array2D;

    public LetterGroup2D(int rows, int cols) {
        array2D = new int[rows][cols];
    }

    public void set(int row, int col, int value) {
        hasLetters = true;
        array2D[row][col] = value;
    }

    public void seed() {
        hasLetters = true;
        for(int i = 0; i < 26; i++) {  //  Seed UNKNOWN with all the letters of the alphabet
            array2D[0][i] = (char)(i+65);
        }
    }

    public void sortByFrequency() {
        System.out.println("Sorting truth table...");

        for (int c = 0; c < 26; c++) {                                                   //  FOR every column from index of 0 until index of 25 (our UNSORTED range of letters)
            for(int r = 0; r < array2D.length; r++) {                                    //  FOR each row in that column
                array2D[r][array2D[r].length-1] = array2D[r][c];                         //  COPY the values of that column to the last column (which is our temporary location used for sort)
            }
            int index = c - 1;                                                           //  The index position of the last data element known to be sorted correctly
            while (index >= 0 && array2D[1][index] < array2D[1][array2D[1].length-1]) {  //  WHILE sortCorrectly index >=0 AND check that the value at that index is LESS THAN the row to sort in the last column
                for(int r = 0; r < array2D.length; r++) {
                    array2D[r][index + 1] = array2D[r][index];                           //  COPY the values known to be sorted correctly one row to the RIGHT
                }
                index--;                                                                 //  DECREMENT the index (to check the index value against the value at the previous index)
            }
            for(int r = 0; r < array2D.length; r++) {
                array2D[r][index+1] = array2D[r][26];                                    //  COPY the values of the column to be sorted to the column FOLLOWING the last column known to be sorted correctly
            }
        }
        System.out.println(" > truth table sorted!");
    }

    public void printFrequency() {

        String format = "%1$3d";

        System.out.print("Letters:         ");
        System.out.print("|");
        for(int c = 0; c < 26; c++) {
            System.out.print(" " + (char)(array2D[0][c]) + " |");
        }
        System.out.println();

        System.out.print("Frequency:       ");
        System.out.print("|");
        for(int c = 0; c < 26; c++) {
            System.out.printf(format, array2D[1][c]);
            System.out.print("|");
        }
        System.out.println();
    }
}