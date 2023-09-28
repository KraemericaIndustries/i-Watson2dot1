package dataStructures;

public class Matrix {

    public static int lettersRemaining = 28;  //  26 letters, plus a print mask and response equals 28
    public static int[][] truthTable;
    public static int turnIndex = 5;
    public static int numTurns = 0;
    public static int numLettersChanged;

    //  INITIALIZE the Matrix...
    public static void initialize() {
        System.out.println("Initializing truth table...");

        truthTable  = new int[15][lettersRemaining];

        truthTable[0][0] = 1;  //  Set print mask
        truthTable[1][0] = 1;
        truthTable[2][0] = 1;
//        truthTable[3][0] = 1;
        truthTable[4][0] = 1;

        for(int i = 1; i < truthTable[0].length; i++) {
            truthTable[2][i] = (i+64);
            truthTable[4][i] = (i+64);
            truthTable[5][i] = 0;
        }
        System.out.println(" > Success!");
        System.out.println(" > Ready for play...");
    }

    //  PRINT the Matrix...
    public static void print() {

        if (truthTable[0][0] == 1) {
            System.out.print("[0] Known IN:        ");
            for (int i = 1; i < truthTable[0].length - 1; i++) System.out.print(truthTable[0][i]);
            System.out.println();
        }  if (truthTable[1][0] == 1) {
            System.out.print("[1] Known OUT:       ");
            for (int i = 1; i < truthTable[0].length - 1; i++) System.out.print(truthTable[1][i]);
            System.out.println();
        }  if (truthTable[2][0] == 1) {
            System.out.print("[2] Unknown:         ");
            for (int i = 1; i < truthTable[0].length - 1; i++) System.out.print((char) (truthTable[2][i]));
            System.out.println();
        }  if (truthTable[3][0] == 1) {
            System.out.print("[3] Known Together:  ");
            morph.MatrixRowTo.commaDelimitedString(3);
        }  if (truthTable[4][0] == 1) {
            System.out.print("[4] Columns:         ");
            System.out.print("|");
            for(int c = 1; c < 27; c++) {
                System.out.print(" " + (char)(truthTable[4][c]) + " |");
                if(c == 26) System.out.println("Res|");
            }
            System.out.println();
        }  if (truthTable[5][0] == 1) {
            System.out.print("[5] Turns:           ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[5][i]);
            System.out.println();
        }  if (truthTable[6][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[6][i]);
            System.out.println();
        }  if (truthTable[7][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[7][i]);
            System.out.println();
        }  if (truthTable[8][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[8][i]);
            System.out.println();
        }  if (truthTable[9][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[9][i]);
            System.out.println();
        }  if (truthTable[10][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[10][i]);
            System.out.println();
        }  if (truthTable[11][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[11][i]);
            System.out.println();
        }  if (truthTable[12][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[12][i]);
            System.out.println();
        }  if (truthTable[13][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[13][i]);
            System.out.println();
        }  if (truthTable[14][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[14][i]);
            System.out.println();
        }
    }

    //  PUSH a guess into the Matrix...
    public static void pushGuess(String guess) {

        truthTable[turnIndex][0] = 1;  //  Set a print mask on the truthTable at the first column of every row

        for(int i = 0; i <5; i++) {

            switch (guess.charAt(i)) {
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
    }

    //  PUSH a response into the Matrix...
    public static void pushResponse(int response) {
        truthTable[turnIndex][27] = response;
        turnIndex++;
        dataStructures.Matrix.numTurns++;
    }

    //  ANALYZE all previous turns taken...
    public static void analyzeAllTurns() {

        //  XOR all guesses... (WIP)
        for(int r = 5; r < 6; r++) {
            for(int c = 1; c < 27; c++) {
                if((truthTable[r][c] ^ truthTable[r+1][c]) == 1) {
                    truthTable[3][0] = 1;  //  Enable print mask
                    truthTable[3][c] = 1;  //  SET bits to 1/true as needed
                    numLettersChanged++;   //  INCREMENT a counter of the number of letters changed
                }
            }
        }
    }
}