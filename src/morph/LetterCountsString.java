package morph;

public class LetterCountsString {
    // Create a String{} of the letters that occur most often in the DB to drive ideal guesses...
    public static String mostCommonLettersString(String letterCountsString) {

        int start = 0;
        int found = 0;

        StringBuilder sb = new StringBuilder();

        while(true) {
            found = letterCountsString.indexOf("=", start);
            if(found == -1) break;
            start = found -1;
            sb.append(letterCountsString.substring(start, found));
            start = start + 2;
        }
        return sb.toString();
    }
}
