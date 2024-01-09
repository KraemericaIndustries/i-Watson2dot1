package dataStructures;

public class LetterGroup2D {

    boolean hasLetters = false;
    int[] array2D;

    public LetterGroup2D() {
        int[][] array2D = new int[2][26];
    }

    public void print() {
        if(hasLetters) System.out.print(array2D);
    }
}