package print;

import classify.Classification;

public class Findings {

    public static void ofPairs(Classification classification) {

        System.out.println("Only in iUpdatedGuess: " + classification.getOnlyInFirst());
        System.out.println("In both iUpdatedGuess & jUpdatedGuess" + classification.getInBoth());
        System.out.println("Only in jUpdatedGuess: " + classification.getOnlyInSecond());



    }

}


//boolean updatedResponsesSame;
//boolean updatedResponsesDifferByOne;
//boolean updatedResponsesDifferByMoreThanOne;
//
//boolean updatedGuessesSameLength;
//boolean updatedGuessesDifferInLengthByOne;
//boolean updatedGuessesDifferInLengthByMoreThanOne;