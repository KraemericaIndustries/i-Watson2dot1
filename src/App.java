import interact.Database;

public class App {
    public static void main(String[] args) throws Exception {
        print.Messages.welcome();
        interact.Database.getProperties();
        interact.Database.create();
        interact.Database.loadKnownWords();
    }
}
