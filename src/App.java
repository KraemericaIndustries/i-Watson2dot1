public class App {
    public static void main(String[] args) throws Exception {
        print.Messages.welcome();
        transactSQL.DatabaseConnection.getProperties();
        transactSQL.Create.watsonDB();
        transactSQL.Insert.loadKnownWords();
        transactSQL.Query.wordsFromDB();
    }
}