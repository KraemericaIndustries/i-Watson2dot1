package morph;

import static dataStructures.Matrix.truthTable;

public class MatrixRowTo {

    //  Represent a truthTable row as a comma delimited string for pretty-print
    public static void commaDelimitedString(int row) {

        StringBuilder sb = new StringBuilder();

        for(int i = 1; i <27; i++) {
                if(truthTable[row][i] == 1) sb.append(((char) (i + 64))).append(", ");
        }
        System.out.println(sb.substring(0, sb.length()-2));
    }
}