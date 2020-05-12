package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;


public class Main {

    public static String in = "/Users/yifengzhang/IdeaProjects/HashCode/inFiles/c_incunabula.txt";
    public static String out = "/Users/yifengzhang/IdeaProjects/HashCode/out4/c_incunabula.txt";



    public static void main(String[] args) throws Exception {
        // write your code here

        long startTime = System.currentTimeMillis();

        File file = new File(in);
        Scanner scanner = new Scanner(file);

        //Read information from input file
        int B = scanner.nextInt();  // the number of different books
        int L = scanner.nextInt();  // the number of libraries
        int D = scanner.nextInt();  // the number of days
        System.out.println( "input:    books =  " + B +  "   libraries = " + L + "   days = " + D);

        // read more information
        int[] books = new int[B]; // the number of books in library
        int maxValue = 0;
        for (int i = 0; i < B; i++) {
            books[i] = scanner.nextInt();
            maxValue += books[i];
        }
        System.out.println("Possible max value = " + maxValue);

        List<Integer>[] lists = new ArrayList[L]; // the number of days takes to finish sign up
        for (int i = 0; i < L; i++) {
            List<Integer> list = new ArrayList<>();
            list.add(scanner.nextInt()); // add number of books in the library-i
            list.add(scanner.nextInt()); // add signup process days for library-i
            list.add(scanner.nextInt()); // add how many books can be shipped in one day
            for (int j = 0; j < list.get(0); j++) {
                list.add(scanner.nextInt()); // add the book number in library-i
            }
            lists[i] = list;
        }

        // sortedSocres
        int[] sortedBookScores = new int[B];
        for (int i = 0; i < B; i++) {
            sortedBookScores[i] = books[i] * 10000 + i;
        }

        Arrays.sort(sortedBookScores);
        for (int i = 0; i < sortedBookScores.length; i++) {
            sortedBookScores[i] = sortedBookScores[i] % 10000;
        }

        for (int i = 0; i < sortedBookScores.length/2; i++) {
            int t = sortedBookScores[i];
            sortedBookScores[i] = sortedBookScores[sortedBookScores.length - 1 - i];
            sortedBookScores[sortedBookScores.length - 1 - i] = t;
        }

        // sloution
        List<List<Integer>> answer = slution(books, lists, D, sortedBookScores);

        // Creating a File object that represents the disk file.
        PrintStream o = new PrintStream(out);

        // Store current System.out before assigning a new value
        PrintStream console = System.out;
        // Assign o to output stream
        System.setOut(o);


        for (int i = 0; i < answer.size(); i++) {
            List<Integer> list = answer.get(i);
            for (int j = 0; j < list.size(); j++) {
                System.out.print(list.get(j) + " ");
            }
            System.out.println();

        }

        //  System.out.println();

        System.setOut(console);
        // System.out.println("Elapsed time = " + (System.currentTimeMillis() - startTime) + " ms.");

    }

    private static List<List<Integer>> slution(int[] values, List<Integer>[] database, int D, int[] sortedBookScores) {

        int L = database.length; // number of libraries

        List<List<Integer>> result = new ArrayList<>(); // return variable
        List<Integer> orderSignedUpLibrary = new ArrayList<>(); // signedUp Libraries in order
        List<List<Integer>> scannedBookLists = new ArrayList<>();   // scanned book lists according to signed up libraries

        boolean[] scannedBooks = new boolean[values.length]; // if the book is scanned, marked it
        boolean[] signedUpLibraries = new boolean[L];

        /*
        int[] mappedValue = new int[books.length];
        for (int i = 0; i < books.length; i++) {
            mappedValue[i] = (books[i] << 16) + i;
        }
        */


        int curValues = 0;
        // find the next library to sign up and scan the books
        while (D > 0 && orderSignedUpLibrary.size() < L) {
            // System.out.println("left days =    " + D  + "    library = " + orderSignedUpLibrary.size());
            int nextLibrary = nextSignedLibrary(values, scannedBooks, signedUpLibraries, database, D, sortedBookScores);
            if (nextLibrary == -1) break;

            List<Integer> info = database[nextLibrary];
            int signUpDay = info.get(1);
            int scanBooksPerDay = info.get(2);
            D -= signUpDay;
            if (D < 1) break;

            orderSignedUpLibrary.add(nextLibrary);
            signedUpLibraries[nextLibrary] = true;
            List<Integer> toScan = new ArrayList<>();
            int total = D * scanBooksPerDay;
            if (total < 0 || total > info.get(0)) total = info.get(0);
            // System.out.println("total books can be scanned = " + total);

            Set<Integer> booksInStock = new HashSet<>();
            for (int j = 3; j < info.size(); j++) {
                booksInStock.add(info.get(j));
            }

            for (int i = 0; i < sortedBookScores.length; i++) {
                int book = sortedBookScores[i];
                if (!scannedBooks[book] && booksInStock.contains((book))) {
                    toScan.add(book);
                    scannedBooks[book] = true;
                    curValues += values[book];
                    total--;

                }
                if (total == 0) break;
            }
           // System.out.println(toScan);

            scannedBookLists.add(new ArrayList<>(toScan));
           //  System.out.println(nextLibrary + "         " + signUpDay);
           //  System.out.println("Curr values =  " + curValues);
        }

        //System.out.println("orderL : " + orderSignedUpLibrary);

        List<Integer> totalLibraries = new ArrayList<>();
        totalLibraries.add(orderSignedUpLibrary.size());

        result.add(totalLibraries);

        for (int i = 0; i < orderSignedUpLibrary.size(); i++) {
            List<Integer> l1 = new ArrayList<>();
            l1.add(orderSignedUpLibrary.get(i));
            l1.add(scannedBookLists.get(i).size());
            // System.out.println("l1 = " + l1);
            result.add(new ArrayList<>(l1));
            result.add(scannedBookLists.get(i));

        }

        for (List<Integer> o : result) {
            // System.out.println(o);
        }


        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            if (scannedBooks[i]) sum += values[i];
        }
        System.out.println("sum = " + sum);
        return result;

    }

    private static int nextSignedLibrary(int[] values, boolean[] scannedBooks, boolean[] signedUpLibraries,
                                         List<Integer>[] database, int leftDay, int[] sortedBookSocres) {
        int ans = -1;
        double max = 0.0;

        for (int i = 0; i < database.length; i++) {
            if (!signedUpLibraries[i]) {
                List<Integer> info = database[i];
                Set<Integer> booksInStock = new HashSet<>();
                for (int j = 3; j < info.size(); j++) {
                    booksInStock.add(info.get(j));
                }
                int sum = 0;
                int signUpDay = info.get(1);
                int scanBooksPerDay = info.get(2);
                int total = (leftDay - signUpDay) * scanBooksPerDay;
                if (total < 0 || total > info.get(0) ) total = info.get(0);
                int cnt = 0;

                for (int j = 0; j < sortedBookSocres.length; j++) {
                    int book = sortedBookSocres[j];
                    if (!scannedBooks[book] && booksInStock.contains((book))) {
                        sum += values[book];
                        cnt++;
                    }
                    if (cnt == total) break;
                }
                double x = (double)sum/(double)signUpDay;
                //  System.out.println(x + "  " + max);
                if (x > max) {
                    max = x;
                    ans = i;
                }
            }
        }

        return ans;
    }


    private static int nextSignedLibrary2(int[] values, boolean[] scannedBooks, boolean[] signedUpLibraries, List<Integer>[] database, int leftDay) {
        int ans = -1;
        int max = 0;

        for (int i = 0; i < database.length; i++) {
            if (!signedUpLibraries[i]) {
                List<Integer> info = database[i];
                int sum = 0;
                int signUpDay = info.get(1);
                int scanBooksPerDay = info.get(2);
                int total = (leftDay - signUpDay) * scanBooksPerDay;
                if (total < 0 || total > info.get(0) ) total = info.get(0);
                int cnt = 0;

                for (int j = 3; j < info.size(); j++) {
                    if (!scannedBooks[info.get(j)]) {
                        sum += values[info.get(j)];
                        cnt++;
                    }
                    if (cnt == total) break;
                }
                // double x = (double)sum/(double)signUpDay;
                //  System.out.println(x + "  " + max);
                if (sum > max) {
                    max = sum;
                    ans = i;
                }
            }
        }

        return ans;
    }
}
