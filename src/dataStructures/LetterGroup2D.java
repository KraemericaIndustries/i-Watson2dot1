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

    public void print() {
        if(hasLetters) {
            for(int row = 0; row < 2; row++) {
                for(int col = 0; col < 26; col++) {
                    System.out.print(array2D[row][col]);
                }
                System.out.println();
            }
        }
    }
}