package dataStructures;

import transactSQL.Create;

import java.util.*;

public class Pairs {

    public Set<Set<Character>> knownTogether = new HashSet<>();

    public void prettyPrintPairs() {
        System.out.println("Known TOGETHER: " + knownTogether);
//        for (Set<Character> s : knownTogether) {
//            System.out.println(s);
//        }
    }

    public void checkPairsForKnownIn(Set<Character> knownIn, Unknown unknown) {
        for(Set<Character> s : knownTogether) {  //  FOR every set in the list
            for (Character c : s) {  //  FOR every character in the set
                if(knownIn.contains(c)) knownIn.addAll(s);  //  IF knownIn contains the character, ADD the entire set
            }
            s.clear();  //
            Create.rebuildWatsonDB(s, unknown);
        }
    }

    public void checkPairsForKnownOut(Set<Character> knownOut, Unknown unknown) {
        for(Set<Character> s : knownTogether) {  //  FOR every set in the list
            for (Character c : s) {  //  FOR every character in the set
                if(knownOut.contains(c)) knownOut.addAll(s);  //  IF knownIn contains the character, ADD the entire set
            }
            s.clear();  //
            Create.rebuildWatsonDB(s, unknown);
        }
    }
}