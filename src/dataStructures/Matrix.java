package dataStructures;

public class Matrix {

//    public static int lettersRemaining = 29;  //  26 letters, plus a print mask, response, and sort column equals 29
    public static int[][] truthTable;
    public static int printMask = 0;
    public static int knownIn = 0;
    public static int knownOut= 1;
    public static int unknown= 2;
    public static int knownTogether= 3;
    public static int columns = 4;
    public static int frequency = 5;

    //  PRINT the Matrix...
//    public static void print() {
//
//        String format = "%1$3d";
//
//        System.out.print("[0] Known IN:        ");
//        if (truthTable[knownIn][printMask] == 1) {
//            for (int i = 1; i < truthTable[0].length - 2; i++) System.out.print((char)truthTable[0][i]);
//        }
//        System.out.println();
//
//        System.out.print("[1] Known OUT:       ");
//        if (truthTable[knownOut][printMask] == 1) {
//            for (int i = 1; i < truthTable[0].length - 2; i++) System.out.print((char)truthTable[1][i]);
//        }
//        System.out.println();
//
//        if (truthTable[unknown][printMask] == 1) {
//            System.out.print("[2] Unknown:         ");
//            for (int i = 1; i < truthTable[0].length - 2; i++) System.out.print((char)(truthTable[2][i]));
//            System.out.println();
//        }  if (truthTable[knownTogether][printMask] == 1) {
//            System.out.print("[3] Known Together:  ");
//            morph.MatrixRowTo.commaDelimitedString(3);
//        }  if (truthTable[columns][printMask] == 1) {
//            System.out.print("[4] Columns:         ");
//            System.out.print("|");
//            for(int c = 1; c < 27; c++) {
//                System.out.print(" " + (char)(truthTable[columns][c]) + " |");
//                if(c == 26) System.out.println("Res|");
//            }
////            System.out.println();
//        }  if (truthTable[frequency][printMask] == 1) {
//            System.out.print("[5] Frequency:       ");
//            System.out.print("|");
//            for(int c = 1; c < 27; c++) {
//                System.out.printf(format, truthTable[frequency][c]);
//                System.out.print("|");
//                if(c == 26) System.out.println("Res|");
//            }
////            System.out.println();
//        }  if (truthTable[6][printMask] == 1) {
//            System.out.print("[6] Turns:           ");
//            System.out.print("|");
//            for (int i = 1; i < truthTable[0].length-1; i++) System.out.print(" " + truthTable[6][i] + " |");
//            System.out.println();
//        }  if (truthTable[7][printMask] == 1) {
//            System.out.print("[7] Turns:           ");
//            System.out.print("|");
//            for (int i = 1; i < truthTable[0].length-1; i++) System.out.print(" " + truthTable[7][i] + " |");
//            System.out.println();
//        }  if (truthTable[8][printMask] == 1) {
//            System.out.print("                     ");
//            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[7][i]);
//            System.out.println();
//        }  if (truthTable[9][printMask] == 1) {
//            System.out.print("                     ");
//            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[8][i]);
//            System.out.println();
//        }  if (truthTable[10][printMask] == 1) {
//            System.out.print("                     ");
//            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[9][i]);
//            System.out.println();
//        }  if (truthTable[11][printMask] == 1) {
//            System.out.print("                     ");
//            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[10][i]);
//            System.out.println();
//        }  if (truthTable[12][printMask] == 1) {
//            System.out.print("                     ");
//            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[11][i]);
//            System.out.println();
//        }  if (truthTable[13][printMask] == 1) {
//            System.out.print("                     ");
//            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[12][i]);
//            System.out.println();
//        }  if (truthTable[14][printMask] == 1) {
//            System.out.print("                     ");
//            for (int i = 1; i < truthTable[0].length; i++) System.out.print(truthTable[13][i]);
//            System.out.println();
//        }
//    }
}