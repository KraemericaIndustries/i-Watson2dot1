package dataStructures;

public class Matrix {

    public static int lettersRemaining = 29;  //  26 letters, plus a print mask, response, and sort column equals 29
    public static int[][] truthTable;
    public static int turnIndex = 6;
    public static int numTurns = 0;
    public static int numLettersChanged;

    //  INITIALIZE the Matrix...
    public static void initialize() {
        System.out.println("Initializing truth table...");

        truthTable  = new int[15][lettersRemaining];

        truthTable[0][0] = 0;  //  Set print mask
        truthTable[1][0] = 0;
        truthTable[2][0] = 1;
        truthTable[4][0] = 1;
        truthTable[5][0] = 1;

        for(int i = 1; i < truthTable[0].length; i++) {
            truthTable[2][i] = (i+64);
            truthTable[4][i] = (i+64);
            truthTable[5][i] = 0;
        }
        System.out.println(" > Success!");
        System.out.println(" > Ready for play...");
    }

    //  SORT the Matrix (by letter frequency DESC, as found in the 5th row)...
    public static void sortByFrequency() {
        //  0 = print mask, and 1 is deemed to be sorted
        //  1 = deemed already sorted
        System.out.println("Sorting truth table...");
        for (int c = 2; c < 27; c++) {  //  FOR every column from index of 2 until index of 26 (our UNSORTED range of letters)

            for(int r = 0; r < truthTable.length; r++) {                      //  FOR each row in that column
                truthTable[r][truthTable[r].length-1] = truthTable[r][c];     //  COPY the values of that column to column 28 (which is our temporary location)
            }
            int index = c - 1;                                                                    //  The index position of the last data element known to be sorted correctly
            while (index >= 0 && truthTable[5][index] < truthTable[5][truthTable[5].length-1]) {  //  WHILE sortCorrectly index >=0 AND check that the value at that index is LESS THAN the row to sorted in column 28
                for(int r = 0; r < truthTable.length; r++) {
                    truthTable[r][index + 1] = truthTable[r][index];                              //  COPY the values known to be sorted correctly one row to the RIGHT
                }
                index--;                                                                          //  DECREMENT the index (to check the index value against the value at the previous index)
            }
            for(int r = 0; r < truthTable.length; r++) {
                truthTable[r][index+1] = truthTable[r][28];                                       //  COPY the values of the column to be sorted to the column FOLLOWING the last column known to be sorted correctly
            }
        }
        System.out.println(" > truth table sorted!");
    }

    //  PRINT the Matrix...
    public static void print() {

        String format = "%1$3d";

        System.out.print("[0] Known IN:        ");
        if (truthTable[0][0] == 1) {
            for (int i = 1; i < truthTable[0].length - 2; i++) System.out.print((char)truthTable[0][i]);
        }
        System.out.println();

        System.out.print("[1] Known OUT:       ");
        if (truthTable[1][0] == 1) {
            for (int i = 1; i < truthTable[0].length - 2; i++) System.out.print((char)truthTable[1][i]);
        }
        System.out.println();

        if (truthTable[2][0] == 1) {
            System.out.print("[2] Unknown:         ");
            for (int i = 1; i < truthTable[0].length - 2; i++) System.out.print((char)(truthTable[2][i]));
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
//            System.out.println();
        }  if (truthTable[5][0] == 1) {
            System.out.print("[5] Frequency:       ");
            System.out.print("|");
            for(int c = 1; c < 27; c++) {
                System.out.printf(format, truthTable[5][c]);
                System.out.print("|");
                if(c == 26) System.out.println("Res|");
            }
//            System.out.println();
        }  if (truthTable[6][0] == 1) {
            System.out.print("[6] Turns:           ");
            System.out.print("|");
            for (int i = 1; i < truthTable[0].length-1; i++) System.out.print(" " + truthTable[6][i] + " |");
            System.out.println();
        }  if (truthTable[7][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[6][i]);
            System.out.println();
        }  if (truthTable[8][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[7][i]);
            System.out.println();
        }  if (truthTable[9][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[8][i]);
            System.out.println();
        }  if (truthTable[10][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[9][i]);
            System.out.println();
        }  if (truthTable[11][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[10][i]);
            System.out.println();
        }  if (truthTable[12][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[11][i]);
            System.out.println();
        }  if (truthTable[13][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[12][i]);
            System.out.println();
        }  if (truthTable[14][0] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[13][i]);
            System.out.println();
        }
    }

    //  PUSH a guess into the Matrix...
    public static void pushGuess(String guess) {

        truthTable[turnIndex][0] = 1;  //  Set a print mask on the truthTable at the first column of every row

        for(int i = 0; i <5; i++) {                              //  FOR every letter in the guess
            int asciiChar = guess.charAt(i);                        //  CONVERT the letter to ASCII
            for(int c = 1; c < (truthTable[4].length-2); c++) {     //  FOR every remaining letter in a row
                if(asciiChar == truthTable[4][c]) {                     //  IF the ASCII value ==  the column title
                    truthTable[turnIndex][c] = 1;                       //  SET 1 (true) for that column/letter on that row/guess
                    break;
                }
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