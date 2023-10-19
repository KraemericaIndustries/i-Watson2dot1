package morph;

import static dataStructures.Matrix.truthTable;

public class MatrixRowTo {

    //  Represent a truthTable row as a comma delimited string for pretty-print
    public static void commaDelimitedString(int row) {

        StringBuilder sb = new StringBuilder();

        for(int c = 1; c <27; c++) {
            if(truthTable[row][c] == 1) sb.append(((char) (truthTable[4][c]))).append(", ");
        }
        System.out.println(sb.substring(0, sb.length()-2));
    }
}