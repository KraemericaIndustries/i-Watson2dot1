package dataStructures;

public class Matrix {

    static int[][] truthTable = new int[10][28];
    static int turnIndex;

    public static void initialize() {
        System.out.println("Initializing truth table...");
        for(int i = 0; i < 27; i++) {
            truthTable[2][i] = (i+65);
            truthTable[4][i] = (i+65);
        }
        System.out.println(" > Success!");
    }

    public static void print() {

        System.out.print("Known IN:        ");
        for(int i = 1; i < truthTable[0].length-1; i++) System.out.print(truthTable[0][i]);
        System.out.println();
        System.out.print("Known OUT:       ");
        for(int i = 1; i < truthTable[0].length-1; i++) System.out.print(truthTable[1][i]);
        System.out.println();
        System.out.print("Unknown:         ");
        for(int i = 0; i < truthTable[0].length-2; i++) System.out.print((char)(truthTable[2][i]));
        System.out.println();
        System.out.print("Known Together:  ");
        for(int i = 1; i < truthTable[0].length-1; i++) System.out.print( + truthTable[3][i]);
        System.out.println();
        System.out.print("                 ");
        for(int i = 0; i < truthTable[0].length-2; i++) System.out.print((char)(truthTable[4][i]));
        System.out.println();

//        for (int[] ints : truthTable) {
//            if (ints[0] == 1 || ints[0] == 9) {
//                for (int j = 0; j < 28; j++) {
//                    System.out.print(ints[j]);
//                }
//                System.out.println();
//            }
//        }
//        System.out.println("DEBUG > yIndex is: " + turnIndex);
    }
    public void push (String guess, int response){

        for(int i = 0; i <5; i++) {
            char letter = guess.charAt(i);

            switch (letter) {

                case 'A' -> truthTable[turnIndex][1] = 1;
                case 'B' -> truthTable[turnIndex][2] = 1;
                case 'C' -> truthTable[turnIndex][3] = 1;
                case 'D' -> truthTable[turnIndex][4] = 1;
                case 'E' -> truthTable[turnIndex][5] = 1;
                case 'F' -> truthTable[turnIndex][6] = 1;
                case 'G' -> truthTable[turnIndex][7] = 1;
                case 'H' -> truthTable[turnIndex][8] = 1;
                case 'I' -> truthTable[turnIndex][9] = 1;
                case 'J' -> truthTable[turnIndex][10] = 1;
                case 'K' -> truthTable[turnIndex][11] = 1;
                case 'L' -> truthTable[turnIndex][12] = 1;
                case 'M' -> truthTable[turnIndex][13] = 1;
                case 'N' -> truthTable[turnIndex][14] = 1;
                case 'O' -> truthTable[turnIndex][15] = 1;
                case 'P' -> truthTable[turnIndex][16] = 1;
                case 'Q' -> truthTable[turnIndex][17] = 1;
                case 'R' -> truthTable[turnIndex][18] = 1;
                case 'S' -> truthTable[turnIndex][19] = 1;
                case 'T' -> truthTable[turnIndex][20] = 1;
                case 'U' -> truthTable[turnIndex][21] = 1;
                case 'V' -> truthTable[turnIndex][22] = 1;
                case 'W' -> truthTable[turnIndex][23] = 1;
                case 'X' -> truthTable[turnIndex][24] = 1;
                case 'Y' -> truthTable[turnIndex][25] = 1;
                case 'Z' -> truthTable[turnIndex][26] = 1;
            }
        }
        truthTable[turnIndex][0] = 1;
        truthTable[turnIndex][27] = response;
        turnIndex++;
//        System.out.println("DEBUG > yIndex is: " + y)
    }

}
