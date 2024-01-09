package dataStructures;

public class LetterGroup1D {

    public boolean hasLetters = false;
    char[] array1D;

    public LetterGroup1D(int size) {
        array1D = new char[size];
    }

    public void seed() {
        hasLetters = true;
        for(int i = 0; i < 26; i++) {  //  Seed UNKNOWN with all the letters of the alphabet
            array1D[i] = (char)(i+65);
        }
    }

    public void print() {
        if(hasLetters) System.out.print(array1D);
        System.out.println();
    }
}