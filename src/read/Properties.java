//package read;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//
//public class Properties {
//    static String url;
//    static String user;
//    static String password;
//    static String data;
//    public static void file() {
//        //  Read the DB url and credentials from the configuration file...
//        java.util.Properties props = new java.util.Properties();
//        try {
//            props.load(new FileInputStream("watson.properties"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        url = props.getProperty("url");
//        user = props.getProperty("user");
//        password = props.getProperty("password");
//        data = props.getProperty("data");
//    }
//}