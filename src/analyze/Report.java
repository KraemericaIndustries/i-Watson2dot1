package analyze;

import dataStructures.GuessTable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Report {
    public static void previousGuesses() throws SQLException {
        if (dataStructures.Matrix.truthTable[5][0] == 0) {
            System.out.println("There are no previous guesses.  I suggest making a guess using the 5 MOST COMMON letters:");
            
//            ResultSet resultSet = transactSQL.Query.select(morph.LetterCountsString.queryForWords());
//            System.out.println(morph.ResultSetTo.strings(resultSet));

        for(String word : GuessTable.fiveMostCommon) {
            System.out.print(word + ", ");
        }
        System.out.println();
        }
    }
}
