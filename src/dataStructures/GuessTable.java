package dataStructures;

import print.Messages;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuessTable {
    static ArrayList<String> fiveMostCommon = new ArrayList<>();
    static ArrayList<String> fourMostCommon = new ArrayList<>();
    static ArrayList<String> threeMostCommon = new ArrayList<>();
    ArrayList<String> nextMostCommon = new ArrayList<>();  //  recursive 2thru6~2thru5...26
    boolean previousGuessMade;


    public static void push() throws SQLException {

        ResultSet resultSet = transactSQL.Query.select("select * from Words_tbl where word like '%" +
                Messages.mostCommonLetters.charAt(0) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(1) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(2) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(3) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(4) + "%'");

        while(resultSet.next()) {
            fiveMostCommon.add(resultSet.getString(1));
        }

        System.out.println("Five Most Common:");
        for(String word:fiveMostCommon) {
            System.out.println(word);
        }

        resultSet = transactSQL.Query.select("select * from Words_tbl where word like '%" +
                Messages.mostCommonLetters.charAt(0) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(1) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(2) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(3) + "%'");

        while(resultSet.next()) {
            fourMostCommon.add(resultSet.getString(1));
        }

        System.out.println("Four Most Common:");
        for(String word:fourMostCommon) {
            System.out.println(word);
        }

        resultSet = transactSQL.Query.select("select * from Words_tbl where word like '%" +
                Messages.mostCommonLetters.charAt(0) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(1) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(2) + "%'");

        while(resultSet.next()) {
            threeMostCommon.add(resultSet.getString(1));
        }

        System.out.println("Three Most Common:");
        for(String word:threeMostCommon) {
            System.out.println(word);
        }
        System.out.println();
    }
    public static void pop() {

    }
}