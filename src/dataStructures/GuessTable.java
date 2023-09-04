package dataStructures;

import print.Messages;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuessTable {
    public static ArrayList<String> fiveMostCommon = new ArrayList<>();  //  Store any words from the DB using the 5 most common letters
    public static ArrayList<String> fourMostCommon = new ArrayList<>();  //  Store any words from the DB using the 4 most common letters
    public static ArrayList<String> threeMostCommon = new ArrayList<>();  //  Store any words from the DB using the 3 most common letters
    public static ArrayList<String> nextMostCommon = new ArrayList<>();  //  Store any words from a recursive call  for the 2nd to 5th and 6th...26th most common letters

    //  PUSH words with most common letters from the DB to ArrayLists to minimize DB interactions
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
//DEBUG:
//        System.out.println("Five Most Common:");
//        for(String word:fiveMostCommon) {
//            System.out.println(word);
//        }

        resultSet = transactSQL.Query.select("select * from Words_tbl where word like '%" +
                Messages.mostCommonLetters.charAt(0) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(1) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(2) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(3) + "%'");

        while(resultSet.next()) {
            fourMostCommon.add(resultSet.getString(1));
        }

//DEBUG:
//        System.out.println("Four Most Common:");
//        for(String word:fourMostCommon) {
//            System.out.println(word);
//        }

        resultSet = transactSQL.Query.select("select * from Words_tbl where word like '%" +
                Messages.mostCommonLetters.charAt(0) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(1) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(2) + "%'");

        while(resultSet.next()) {
            threeMostCommon.add(resultSet.getString(1));
        }
//DEBUG:
//        System.out.println("Three Most Common:");
//        for(String word:threeMostCommon) {
//            System.out.println(word);
//        }
        getNextMostCommon(5);
    }
    public static void pop() {

    }
    //  RECURSIVELY query for the first word that can be made with the next most common letters (2~6, or 2~5...26)
    public static void getNextMostCommon(int c) throws SQLException {

        ResultSet resultSet = transactSQL.Query.select("select * from Words_tbl where word like '%" +
                Messages.mostCommonLetters.charAt(1) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(2) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(3) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(4) + "%' and word like '%" +
                Messages.mostCommonLetters.charAt(c) + "%'");

        if(resultSet.isBeforeFirst()) {
            while(resultSet.next()) {
                nextMostCommon.add(resultSet.getString(1));
            }
        } else {
                getNextMostCommon(c+1);
        }
//DEBUG:
//        System.out.println("nextMostCommon:");
//        for(String word:nextMostCommon) {
//            System.out.println(word);
//        }
    }
}