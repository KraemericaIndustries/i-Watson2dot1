package assess;

public class Classification {

    public static String assessClassification(classify.Classification classification) {

        String assessment = "";

        if((classification.onlyInFirst.size() == 1 && classification.onlyInSecond.size() == 1) && classification.deltaUpdatedResponse == 1) {
            assessment = "One letter changed, delta is 1";
        } else if ((classification.onlyInFirst.size() == 1 && classification.onlyInSecond.size() == 1) && classification.deltaUpdatedResponse == -1) {
            assessment = "One letter changed, delta is -1";
        } else if ((classification.onlyInFirst.size() == 1 && classification.onlyInSecond.size() == 1) && classification.deltaUpdatedResponse == 0) {
            assessment = "One letter changed, delta is 0";
        } else if ((classification.updatedGuessesSame)) {
            assessment = "Updated guesses are the same";
        }
// ToDo: Add cases for onlyInFirst or only in second being 0 (some determination is possible based on commonToBoth and only in other...  What is the logic here??
        return assessment;
    }

}
