package assess;

import classify.Classification;
import dataStructures.*;
import transactSQL.*;

import java.sql.SQLException;
import java.util.*;

public class AllTurns {

/*  MAKE all possible determinations on ALL previous turns taken...
    public static void makeDeterminations(LinkedList<Turn> Turns, Pairs pairs, IdentifiedLetters knownIn, IdentifiedLetters knownOut, Unknown unknown) throws SQLException {

        System.out.println("assess.AllTurns.makeDeterminations(): BEGIN");

        //  Checks if any one knownTogether pair is knownIn, if yes, the other must be knownIn (because we only process turns where the response varies by 1)
        if(!pairs.knownTogether.isEmpty()) {
            pairs.checkPairsForKnownIn(knownIn, unknown);
            pairs.checkPairsForKnownOut(knownOut, unknown);
            for (Turn t : Turns) updateTurn(t, knownIn, knownOut, unknown, Turns);
        }

        //  ANY turn with an (updated) size equal to the (updated) response are KNOWN IN...
        checkAllTurnsForSizeEqualsUpdatedResponse(Turns, knownIn, unknown);

        for (Turn t : Turns) updatedResponseIsZero(t, knownOut, unknown, Turns);

        //  COMPARE each (updated) turn against each other...
        compareAllTurnsAgainstEachOther(Turns, dashboard);
        System.out.println("assess.AllTurns.makeDeterminations: END");
    }*/

//    public static void updateTurn(Turn turn, IdentifiedLetters knownIn, IdentifiedLetters knownOut, Unknown unknown, LinkedList<Turn> Turns) {
//
//        //  FOR every character in knownIn
//        for (Character c : knownIn.letters) {
//            if (turn.turn.contains(c)) {     //  IF the turn contains the character
//                System.out.println("Removing all 'KNOWN IN' (" + c + ") from guess of " + turn.turn + "...");
//                turn.turn.remove(c);        //  REMOVE the character from the turn
//                System.out.println("Decrementing response from " + turn.updatedGuess + " to " + (turn.updatedResponse - 1) + " to account for the stripped letter known to be IN.");
//                turn.updatedResponse -= 1;  //  DECREMENT the updatedResponse
//
//                //  Having removed all letters known to be IN, if the remaining response is 0, the remainder of the guess is known to be OUT...
//                updatedResponseIsZero(turn, knownOut, unknown, Turns);
//            }
//        }
//        turn.parseCollectionToString();  //  PARSE the collection back to a String
//        removeKnownOutFromAllTurns(knownOut, Turns);
//    }

//    private static void updatedResponseIsZero(Turn turn, IdentifiedLetters knownOut, Unknown unknown, LinkedList<Turn> Turns) {
//        if(turn.updatedResponse == 0) {
//            turn.response = turn.updatedResponse;
//            turn.parseCollectionToString();  //  PARSE the collection back to a String
//            AllTurns.responseOfZero(turn, knownOut, unknown, Turns);
//        }
//    }

//    public static void removeKnownOutFromAllTurns(IdentifiedLetters knownOut, LinkedList<Turn> Turns) {
//        for (Turn t : Turns) {              //  FOR every turn in Turns
//            for (Character c : knownOut.letters) {  //  FOR every character in knownIn
//                t.turn.remove(c);           //  REMOVE the character from the turn
//            }
//            t.parseCollectionToString();    //  PARSE the collection back to a String
//        }
//    }

    //  COMPARE each updated turn against each other...
    public static void compareAllTurnsAgainstEachOther(LinkedList<Turn> Turns, Dashboard dashboard) throws SQLException {

        removeDeterminedLettersFromAllTurns(Turns, dashboard);

        System.out.println("assess.AllTurns.compareAllTurnsAgainstEachOther(): BEGIN");

        boolean changesMade = false;

        do {
            int comparisonNumber = 1;

            for(int i = 0; i < Turns.size() - 1; i++) {      //  Take the FIRST turn in 'Turns' (then the second, then the third, up until the SECOND LAST Turn in 'Turns')
                for(int j = i + 1; j < Turns.size(); j++) {  //  Take the SECOND turn in 'Turns' (then the third, then the fourth, up until the LAST Turn in 'Turns')

                    System.out.println("COMPARISON #" + comparisonNumber + ".  Now comparing turn #" + (i + 1) + " with turn #" + (j + 1) + ":");
                    prettyPrintLinkedHashMap(Turns, i, j);

                    //  CLASSIFICATION:
                    System.out.println("CLASSIFICATION:");
                    Classification classification;
                    classification = new Classification(Turns.get(i).updatedResponse, Turns.get(j).updatedResponse, Turns.get(i).updatedGuess, Turns.get(j).updatedGuess);
                    classification.printClassification();

                    // Now that the selected pair of turns has been CLASSIFIED, Identify Findings, make Determinations, and take ACTION...
                    if(!classification.updatedGuessesSame) {  // One (or more) letters has changed.  Letters in common are IN.  All others are OUT.

                        //  THE MAGIC GOES HERE!!!  Put FINDINGS/DETERMINATIONS/ACTIONS all in the same if-else ladder!
                        System.out.println("FINDINGS:");
                        if(classification.onlyInFirst.size() == 1 && classification.onlyInSecond.size() == 1) {  //  ONLY 1 LETTER HAS CHANGED
                            System.out.println(" > Only 1 letter has changed.");
                            if(classification.deltaUpdatedResponse == 1 || classification.deltaUpdatedResponse == -1) {  //  RESPONSE HAS CHANGED
                                System.out.println(" > The (updated) response has changed by 1.");
                                System.out.println("DETERMINATIONS:");
                                if(classification.deltaUpdatedResponse == 1) {
                                    System.out.println(" > We now know " + classification.onlyInFirst + " is IN, and " + classification.onlyInSecond + " is OUT!");
                                    dashboard.changesToKnownIn.addAll(classification.onlyInFirst);
                                    dashboard.changesToKnownOut.addAll(classification.onlyInSecond);
                                }
                                else {
                                    System.out.println(" > We now know " + classification.onlyInFirst + " is OUT, and " + classification.onlyInSecond + " is IN!");
                                    dashboard.changesToKnownIn.addAll(classification.onlyInSecond);
                                    dashboard.changesToKnownOut.addAll(classification.onlyInFirst);
                                }
                                System.out.println("ACTIONS:");
                                if(classification.deltaUpdatedResponse == 1) System.out.println(" > Adding " + classification.onlyInFirst + " to Known IN, and " + classification.onlyInSecond + " to Known OUT!");
                                else System.out.println(" > Adding " + classification.onlyInSecond + " to Known IN, and " + classification.onlyInFirst + " to Known OUT!");
                                //  ToDo This invocation is where I left off.  Finish this!!!
                                updateDashboard(dashboard);
                                changesMade = true;
                            } else if (classification.deltaUpdatedResponse == 0) {  //  RESPONSE IS SAME
                                System.out.println(" > The (updated) response has NOT changed.");
                                System.out.println(" > We now know that " + classification.onlyInFirst + " and " + classification.onlyInSecond + " are either BOTH IN, or BOTH OUT (but can't be certain which is the case.");
                                System.out.println("ACTIONS:");
                                System.out.println(" > Adding " + classification.onlyInFirst + " and " + classification.onlyInSecond + " to a set of letters that are Known TOGETHER.");
                                classification.onlyInFirst.addAll(classification.onlyInSecond);  //  Since these are now known to be together, ADD the second set to the first
                                dashboard.mergeSetToKnownTogether(classification.onlyInFirst);   //  MERGE the first set to the list of all sets known to be together
                            }
                        }

                        // LEGACY:
//                        if(classification.updatedResponsesDifferByOne && classification.updatedGuessesSameLength) {
//                            System.out.println(" - Since updated responses are the same length, and the responses differ by 1, we know:");
//                            System.out.println("   " + classification.onlyInFirst + " is IN.");
//                            System.out.println("   " + classification.onlyInSecond + " is OUT.");
//                        } else if (classification.onlyInFirst.size() == 1  && classification.onlyInSecond.size() == 1) {
//                            System.out.println("All we can say for certain, is that " + classification.onlyInFirst + " and " + classification.onlyInSecond + " are together.  (Could both be IN, could both be OUT.)");
//                        }
//                        System.out.println("ACTIONS:");
//                        System.out.println("Add " + classification.onlyInFirst + " and " + classification.onlyInSecond + " to 'knownTogether' on the dashboard");

                    }
                }
            }
        } while (changesMade);
        System.out.println("assess.AllTurns.compareAllTurnsAgainstEachOther(): END");
    }



//    private static String getLongerUpdatedGuessInCurrentPair(String iUpdatedGuess, String jUpdatedGuess) {
//        if(iUpdatedGuess.length() > jUpdatedGuess.length()) return iUpdatedGuess;
//        else return jUpdatedGuess;
//    }

//    private static void responseIsEqualWithOneLetterDifferent(LinkedList<Turn> Turns, IdentifiedLetters knownIn, IdentifiedLetters knownOut, Unknown unknown, int i, int j) throws SQLException {
//
//        //  If what remains is size = 1, then the turns MUST have been 1 letter different
//        //  Therefore, the remaining letter is OUT, so go ahead and do it
//
//        //  CREATE a temp pair of collections for a pair of turns to make the assessment...
//        Set<Character> a = Turns.get(i).turn;
//        Set<Character> b = Turns.get(i).turn;
//
//        //  Take the smaller (Set<Character>) away from the bigger
//        if(a.size() > b.size()) {
//            System.out.println("Removing " + b + " from " + a);
//            a.removeAll(b);
//            //  If what remains is size = 1, then the turns MUST have been 1 letter different
//            //  Therefore, the remaining letter is OUT, so go ahead and do it
//            if (a.size() == 1) Turns.get(i).turn = a;
//            updatedResponseIsZero(Turns.get(i), knownOut, unknown, Turns);
//            checkAllTurnsForSizeEqualsUpdatedResponse(Turns, knownIn, unknown);
//        } else if (a.size() < b.size()){
//            System.out.println("Removing " + Turns.get(i).turn + " from " + Turns.get(j).turn);
//            b.removeAll(a);
//            //  If what remains is size = 1, then the turns MUST have been 1 letter different
//            //  Therefore, the remaining letter is OUT, so go ahead and do it
//            if (b.size() == 1) Turns.get(i).turn = b;
//            updatedResponseIsZero(Turns.get(j), knownOut, unknown, Turns);
//            checkAllTurnsForSizeEqualsUpdatedResponse(Turns, knownIn, unknown);
//        }
//    }

    private static void removeDeterminedLettersFromAllTurns(LinkedList<Turn> Turns, Dashboard dashboard) {

        for(Character c : dashboard.knownIn) {  //  walks knownIn
            for(Turn t : Turns) {     //  walks a turn
                if(t.turn.contains(c)) {
                    t.turn.remove(c);
                    t.updatedResponse -= 1;
                }
                t.parseCollectionToString();
            }
        }

        for(Character c : dashboard.knownOut) {  //  walks knownIn
            for(Turn t : Turns) {      // walks a turn
                t.turn.remove(c);
                t.parseCollectionToString();
            }
        }
    }

    //  FOR any turn where size = updatedResponse, ALL letters in the updatedGuess are KNOWN IN
//    public static void checkAllTurnsForSizeEqualsUpdatedResponse(LinkedList<Turn> Turns, IdentifiedLetters knownIn, Unknown unknown) throws SQLException {
//
//        System.out.println("assess.AllTurns.checkAllTurnsForSizeEqualsUpdatedResponse(): BEGIN");
//
//        for(Turn t : Turns) {
//            if(t.turn.size() == t.updatedResponse) {
//                knownIn.letters.addAll(t.turn);
//            }
//        }
//        System.out.println("assess.AllTurns.checkAllTurnsForSizeEqualsUpdatedResponse(): END");
//    }

    //  PRETTY-PRINT the UPDATED turns being compared...
    private static void prettyPrintLinkedHashMap(LinkedList<Turn> Turns, int i, int j) {

        //  System.out.println("assess.AllTurns.prettyPrintLinkedHashMap(): BEGIN");

        StringBuilder sb = new StringBuilder();

        sb.append("    ORIGINAL: ").append(Turns.get(i).guess).append(", ").append(Turns.get(i).response).append(" > UPDATED: [").append(Turns.get(i).updatedGuess).append("]").append(" = ").append(Turns.get(i).updatedResponse);
        System.out.println(sb);
        sb.setLength(0);
        sb.append("    ORIGINAL: ").append(Turns.get(j).guess).append(", ").append(Turns.get(j).response).append(" > UPDATED: [").append(Turns.get(j).updatedGuess).append("]").append(" = ").append(Turns.get(j).updatedResponse);
        System.out.println(sb);
        //  System.out.println("assess.AllTurns.prettyPrintLinkedHashMap(): END");
    }

    //  ANY guess with a response of zero is KNOWN OUT.  Remove all letters in the guess from ALL data sources...
//    public static void responseOfZero(Turn turn, IdentifiedLetters knownOut, Unknown unknown, LinkedList<Turn> Turns) {
//
//        System.out.println("assess.AllTurns.responseOfZero(): BEGIN");
//
//        if(turn.response == 0) {
//            // DELETE every letter from the String (handle length programmatically) from the DB
//            try {
//                Delete.wordsWith(turn.updatedGuess, unknown, knownOut);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            regenerateWordPairsTable();
//
//            // DELETE every letter from the String from Unknown.letters
//            Unknown.removeFromUnknown(turn.updatedGuess);
//            removeStringFromAllTurns(turn.updatedGuess, Turns);
//        }
//        System.out.println("assess.AllTurns.responseOfZero(): END");
//    }

    //  REGENERATE the WordPairs table as previously unknown letters are determined to be KNOWN OUT...
    public static void regenerateWordPairsTable() {

        System.out.println("assess.AllTurns.regenerateWordPairsTable(): BEGIN");

        Connect.watson("dropWordPairsTable");    //  DROP the WordPairs table...
        Connect.watson("createWordPairsTable");  //  REGENERATE the WordPairs table...
        Connect.watson("deleteDups");            //  DELETE dups from the WordPairs table...

        System.out.println("assess.AllTurns.regenerateWordPairsTable(): END");
    }

    //  UPDATE all turns as determinations on letters are made...
//    public static void removeStringFromAllTurns(String guess, LinkedList<Turn> Turns) {
//
//        System.out.println("assess.AllTurns.removeStringFromAllTurns(): BEGIN");
//
//        char[] guessArray = guess.toCharArray();
//        List<Character> guessList = new ArrayList<>();
//
//        for (char c : guessArray) guessList.add(c);
//
//        for (Turn t : Turns) {
//
//            char[] turnArray = t.updatedGuess.toCharArray();
//            List<Character> turnList = new ArrayList<>();
//
//            for (char c : turnArray) turnList.add(c);
//
//            turnList.removeAll(guessList);
//
//            StringBuilder sb = new StringBuilder();
//
//            for(Character c : turnList) sb.append(c);
//
//            t.updatedGuess = sb.toString();
//
//            System.out.println(t.updatedGuess);
//        }
//
//        for(Turn t : Turns) t.parseGuessToCollection(t.updatedGuess);
//
//        System.out.println("assess.AllTurns.removeStringFromAllTurns END");
//    }



    public static void updateDashboard(Dashboard dashboard) throws SQLException {

        boolean reprocessingNeeded = false;

        do {
            //  PROCESS changes to KNOWN IN
            if(!dashboard.changesToKnownIn.isEmpty()) {                                             //  IF there are any changes to Known IN
                dashboard.knownIn.addAll(dashboard.changesToKnownIn);                               //  UPDATE Known IN (GOSPEL)
                //  UPDATE ALL Turns
                for (Turn t : dashboard.Turns) {                                                    //  FOR EVERY PREVIOUS TURN in the Turns collection
                    if (Dashboard.containsAny(t.updatedGuess, dashboard.changesToKnownIn)) {               //  IF the Turn contains ANY letter within changesToKnownIn
                        Dashboard.removeChars(t.updatedGuess, dashboard.changesToKnownIn);                 //  REMOVE ALL letters in changesToKnownIn from the updatedGuess for that Turn
                        t.updatedResponse = t.updatedResponse - dashboard.changesToKnownIn.size();  //  CORRECT the updatedResponse (<-- Used to use the '--' operator, but this is more robust)
                        //  CHECK for Turns where updatedGuess.length == updatedResponse
                        if(t.updatedGuess.length() == t.updatedResponse) {                          //  IF updatedGuess.length == updatedResponse
                            System.out.println("We now know that every letter in " + t.updatedGuess + " is now Known IN!  Updating the dashboard...");
                            for (char c : t.updatedGuess.toCharArray()) {                           //  FOR EVERY CHARACTER in updatedGuess
                                dashboard.changesToKnownIn.add(c);                                  //  ADD the character to changesToKnownIn
                                reprocessingNeeded = true;                                          //  SET the reprocessingNeeded flag
                            }
                        }
                    }
                }
            }
            //  PROCESS changes to KNOWN OUT
            if(!dashboard.changesToKnownOut.isEmpty()) {                                            //  IF there are changes to Known OUT
                dashboard.knownIn.addAll(dashboard.changesToKnownOut);                              //  UPDATE Known OUT (GOSPEL)
                //  UPDATE ALL Turns
                for (Turn t : dashboard.Turns) {                                                    //  FOR EVERY PREVIOUS TURN in the Turns collection
                    if (Dashboard.containsAny(t.updatedGuess, dashboard.changesToKnownOut)) {       //  IF the Turn contains ANY letter within changesToKnownIn
                        Dashboard.removeChars(t.updatedGuess, dashboard.changesToKnownOut);         //  REMOVE ALL letters in changesToKnownOut from the updatedGuess for that Turn
                        //  CHECK for Turns where updatedGuess IS NOT empty, and updatedResponse > 0
                        if((!t.updatedGuess.isEmpty()) && t.updatedResponse == 0) {
                            System.out.println("We now know that every letter in " + t.updatedGuess + " is now Known OUT!  Updating the dashboard...");
                            for (char c : t.updatedGuess.toCharArray()) {                           //  FOR EVERY CHARACTER in updatedGuess
                                dashboard.changesToKnownOut.add(c);                                 //  ADD the character to changesToKnownOut
                                reprocessingNeeded = true;                                          //  SET the reprocessingNeeded flag
                            }
                        }
                    }
                }
            }
        } while (reprocessingNeeded);

        //  UPDATE Words_tbl (drop words without Known IN, drop words with Known OUT)
        Delete.fromWordsTable(dashboard);

        //  REGENERATE Words_tbl...
        Create.rebuildWatsonDB(dashboard);

        //  REGENERATE WordPairs table...
        regenerateWordPairsTable();  // rebuild WordPairs table

        //  SORT UNKNOWN letters remaining...
        dashboard.sortUnknownLettersByFrequencyDescending();

        //  Clear changesTo sets
        dashboard.changesToKnownIn.clear();
        dashboard.changesToKnownOut.clear();
    }

    public static boolean compareLatestTurnAgainstAllOthers(Turn latestTurn, Dashboard dashboard) throws SQLException {

        boolean changesMade = false;
        int comparisonNumber = 1;
        
        for(int i = 0; i < dashboard.Turns.size(); i++) {  //  ITERATE over every Turn in 'Turns' collection)

            System.out.println("COMPARISON #" + comparisonNumber + ".  Now comparing most recent turn with turn #" + i + ":");

            StringBuilder sb = new StringBuilder();

            sb.append("    ORIGINAL: ").append(latestTurn.guess).append(", ").append(latestTurn.response).append(" > UPDATED: [").append(latestTurn.updatedGuess).append("]").append(" = ").append(latestTurn.updatedResponse);
            System.out.println(sb);
            sb.setLength(0);
            sb.append("    ORIGINAL: ").append(dashboard.Turns.get(i).guess).append(", ").append(dashboard.Turns.get(i).response).append(" > UPDATED: [").append(dashboard.Turns.get(i).updatedGuess).append("]").append(" = ").append(dashboard.Turns.get(i).updatedResponse);
            System.out.println(sb);
            

            //  CLASSIFICATION:
            System.out.println("CLASSIFICATION:");
            Classification classification;
            classification = new Classification(latestTurn.updatedResponse, dashboard.Turns.get(i).updatedResponse, latestTurn.updatedGuess, dashboard.Turns.get(i).updatedGuess);
            classification.printClassification();

            // Now that the selected pair of turns has been CLASSIFIED, Identify Findings, make Determinations, and take ACTION...
            if(!classification.updatedGuessesSame) {  // One (or more) letters has changed.  Letters in common are IN.  All others are OUT.

                //  THE MAGIC GOES HERE!!!  Put FINDINGS/DETERMINATIONS/ACTIONS all in the same if-else ladder!
                System.out.println("FINDINGS:");
                if(classification.onlyInFirst.size() == 1 && classification.onlyInSecond.size() == 1) {  //  ONLY 1 LETTER HAS CHANGED
                    System.out.println(" > Only 1 letter has changed.");
                    if(classification.deltaUpdatedResponse == 1 || classification.deltaUpdatedResponse == -1) {  //  RESPONSE HAS CHANGED
                        System.out.println(" > The (updated) response has changed by 1.");
                        System.out.println("DETERMINATIONS:");
                        if(classification.deltaUpdatedResponse == 1) {
                            System.out.println(" > We now know " + classification.onlyInFirst + " is IN, and " + classification.onlyInSecond + " is OUT!");
                            dashboard.changesToKnownIn.addAll(classification.onlyInFirst);
                            dashboard.changesToKnownOut.addAll(classification.onlyInSecond);
                        }
                        else {
                            System.out.println(" > We now know " + classification.onlyInFirst + " is OUT, and " + classification.onlyInSecond + " is IN!");
                            dashboard.changesToKnownIn.addAll(classification.onlyInSecond);
                            dashboard.changesToKnownOut.addAll(classification.onlyInFirst);
                        }
                        System.out.println("ACTIONS:");
                        if(classification.deltaUpdatedResponse == 1) System.out.println(" > Adding " + classification.onlyInFirst + " to Known IN, and " + classification.onlyInSecond + " to Known OUT!");
                        else System.out.println(" > Adding " + classification.onlyInSecond + " to Known IN, and " + classification.onlyInFirst + " to Known OUT!");
                        //  ToDo This invocation is where I left off.  Finish this!!!
                        updateDashboard(dashboard);
                        changesMade = true;
                    } else if (classification.deltaUpdatedResponse == 0) {  //  RESPONSE IS SAME
                        System.out.println(" > The (updated) response has NOT changed.");
                        System.out.println(" > We now know that " + classification.onlyInFirst + " and " + classification.onlyInSecond + " are either BOTH IN, or BOTH OUT (but can't be certain which is the case.");
                        System.out.println("ACTIONS:");
                        System.out.println(" > Adding " + classification.onlyInFirst + " and " + classification.onlyInSecond + " to a set of letters that are Known TOGETHER.");
                        classification.onlyInFirst.addAll(classification.onlyInSecond);  //  Since these are now known to be together, ADD the second set to the first
                        dashboard.mergeSetToKnownTogether(classification.onlyInFirst);   //  MERGE the first set to the list of all sets known to be together
                    }
                }
            }
        }
        return changesMade;
    }

}