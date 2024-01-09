package dataStructures;

public class LetterGroup2D {

    boolean hasLetters = false;
    public int[][] array2D;

    public LetterGroup2D() {
        array2D = new int[2][26];
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

    public void print() {

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
    }
}