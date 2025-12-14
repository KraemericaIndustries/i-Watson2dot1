package dataStructures;

import transactSQL.Create;
import transactSQL.Delete;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

//import static com.sun.tools.javac.code.TypeAnnotationPosition.unknown;

public class IdentifiedLetters {

    public Set<Character> letters = new HashSet<>();

    public void addSet(Set<Character> determined, Unknown unknown) throws SQLException {

        letters.addAll(determined);

        String toAdd = Pairs.createStringFromSet(determined);

        Delete.wordsWithout(toAdd, unknown, letters);

        Create.rebuildWatsonDB(letters, unknown);

    }

}
