package dataStructures;

public class Matrix {

    public static int lettersRemaining = 29;  //  26 letters, plus a print mask, response, and sort column equals 29
    public static int[][] truthTable;
    public static int printMask = 0;
    public static int knownIn = 0;
    public static int knownOut= 1;
    public static int unknown= 2;
    public static int knownTogether= 3;
    public static int columns = 4;
    public static int frequency = 5;
    public static int turnIndex = 6;
    public static int numTurns = 0;
    public static int numLettersChanged;

    //  INITIALIZE the Matrix...
    public static void initialize() {
        System.out.println("Initializing truth table...");

        truthTable  = new int[15][lettersRemaining];

        truthTable[knownIn][printMask] = 0;
        truthTable[knownOut][printMask] = 0;
        truthTable[unknown][printMask] = 1;
        truthTable[columns][printMask] = 1;
        truthTable[frequency][printMask] = 1;

        for(int i = 1; i < truthTable[0].length; i++) {
            truthTable[unknown][i] = (i+64);
            truthTable[columns][i] = (i+64);
            truthTable[frequency][i] = 0;
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
            while (index >= 0 && truthTable[frequency][index] < truthTable[frequency][truthTable[frequency].length-1]) {  //  WHILE sortCorrectly index >=0 AND check that the value at that index is LESS THAN the row to sorted in column 28
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
        if (truthTable[knownIn][printMask] == 1) {
            for (int i = 1; i < truthTable[0].length - 2; i++) System.out.print((char)truthTable[0][i]);
        }
        System.out.println();

        System.out.print("[1] Known OUT:       ");
        if (truthTable[knownOut][printMask] == 1) {
            for (int i = 1; i < truthTable[0].length - 2; i++) System.out.print((char)truthTable[1][i]);
        }
        System.out.println();

        if (truthTable[unknown][printMask] == 1) {
            System.out.print("[2] Unknown:         ");
            for (int i = 1; i < truthTable[0].length - 2; i++) System.out.print((char)(truthTable[2][i]));
            System.out.println();
        }  if (truthTable[knownTogether][printMask] == 1) {
            System.out.print("[3] Known Together:  ");
            morph.MatrixRowTo.commaDelimitedString(3);
        }  if (truthTable[columns][printMask] == 1) {
            System.out.print("[4] Columns:         ");
            System.out.print("|");
            for(int c = 1; c < 27; c++) {
                System.out.print(" " + (char)(truthTable[columns][c]) + " |");
                if(c == 26) System.out.println("Res|");
            }
//            System.out.println();
        }  if (truthTable[frequency][printMask] == 1) {
            System.out.print("[5] Frequency:       ");
            System.out.print("|");
            for(int c = 1; c < 27; c++) {
                System.out.printf(format, truthTable[frequency][c]);
                System.out.print("|");
                if(c == 26) System.out.println("Res|");
            }
//            System.out.println();
        }  if (truthTable[6][printMask] == 1) {
            System.out.print("[6] Turns:           ");
            System.out.print("|");
            for (int i = 1; i < truthTable[0].length-1; i++) System.out.print(" " + truthTable[6][i] + " |");
            System.out.println();
        }  if (truthTable[7][printMask] == 1) {
            System.out.print("[7] Turns:           ");
            System.out.print("|");
            for (int i = 1; i < truthTable[0].length-1; i++) System.out.print(" " + truthTable[7][i] + " |");
            System.out.println();
        }  if (truthTable[8][printMask] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[7][i]);
            System.out.println();
        }  if (truthTable[9][printMask] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[8][i]);
            System.out.println();
        }  if (truthTable[10][printMask] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[9][i]);
            System.out.println();
        }  if (truthTable[11][printMask] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[10][i]);
            System.out.println();
        }  if (truthTable[12][printMask] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[11][i]);
            System.out.println();
        }  if (truthTable[13][printMask] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[12][i]);
            System.out.println();
        }  if (truthTable[14][printMask] == 1) {
            System.out.print("                     ");
            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[13][i]);
            System.out.println();
        }
    }

    //  PUSH a guess into the Matrix...
    public static void pushGuess(String guess) {

        truthTable[turnIndex][printMask] = 1;  //  Set a print mask on the truthTable at the first column of every row

        for(int i = 0; i <5; i++) {                              //  FOR every letter in the guess
            int asciiChar = guess.charAt(i);                        //  CONVERT the letter to ASCII
            for(int c = 1; c < (truthTable[columns].length-2); c++) {     //  FOR every remaining letter in a row
                if(asciiChar == truthTable[columns][c]) {                     //  IF the ASCII value ==  the column title
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
        for(int r = 6; r < 7; r++) {
            for(int c = 1; c < 27; c++) {
                if((truthTable[r][c] ^ truthTable[r+1][c]) == 1) {
                    truthTable[knownTogether][0] = 1;  //  Enable print mask
                    truthTable[knownTogether][c] = 1;  //  SET bits to 1/true as needed
                    numLettersChanged++;   //  INCREMENT a counter of the number of letters changed
                }
            }
        }
    }
}