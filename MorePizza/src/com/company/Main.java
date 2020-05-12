//*****************************************************************
// More Pizza
// Solution for the Practice Round of Google Hash Code 2020
// 02/16/2020
//*****************************************************************

 package com.company;
import javax.sound.midi.Soundbank;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;
import java.util.Random;



public class Main {

    public static String in = "/Users/yifengzhang/IdeaProjects/MorePizza/inFiles/e_also_big.in";
    public static String out = "/Users/yifengzhang/IdeaProjects/MorePizza/outFiles/e_also_big.out";;

    public static void main(String[] args) throws Exception {
	    // write your code here

        long startTime = System.currentTimeMillis();
        Scanner input = new Scanner(System.in);
        int T = input.nextInt();
        for (int ks = 1; ks <= T; ks++) {
            int a = input.nextInt();
            int b = input.nextInt();
            int n = input.nextInt();
            solve(input, a + 1, b);
        }

        /*

        Scanner scanner = new Scanner(System.in);

        int A = 0, B = 0;
        int T = 0;

        System.out.println("Please intput the range: ");
        String newLine = scanner.nextLine();
        String[] temp = newLine.split(" ");
        A = Integer.parseInt(temp[0]);
        B = Integer.parseInt(temp[1]);
        System.out.println("Please input the maxisum number of guesses: ");
        T = Integer.parseInt(scanner.nextLine());

        // create instance of Random class
        Random rand = new Random();
        int hiddenAnswer = rand.nextInt(B - A + 1) + A;
        int guess = -1;// A >= 0
        int cnt = 0;

        while( guess != hiddenAnswer || cnt >= T) {
            if (System.currentTimeMillis() - startTime >= 100000) {
                System.out.println("Time out!");
                break;
            }
            System.out.println("Please input your guess: ");
            String n = scanner.nextLine();

            if (Integer.parseInt(n) != Integer.valueOf(n)
                    || Integer.parseInt(n) < A || Integer.parseInt(n) > B)  {
                System.out.println("WRONG ANSWER");
            } else {
                guess = Integer.parseInt(n);
                if (guess == hiddenAnswer) {
                    System.out.println("CORRECT!");
                    break;
                }else if (guess < hiddenAnswer) {
                    System.out.println("TOO_SMALL");
                }else {
                    System.out.println("TOO_BIG");
                }
            }
            cnt++;

        }

        if (cnt >= T) System.out.println("Guess time out!");

        */
        /*
        File file = new File(in);
        Scanner scanner = new Scanner(file);

        //Read information from input file
        int M = scanner.nextInt();  // the maximum of pizza slice
        int N = scanner.nextInt();  // the number of different types of pizza
        // System.out.println( "input:     " + M +  "  " + N);

        // The sorted array - the number of slices in each type of pizza
        int[] nums = new int[N];
        // read data from the file
        for (int i = 0; i < N; i++) {
            nums[i] = scanner.nextInt();
        }
        // sloution
        List<Integer> answer = morePizza(M, nums);

        // Creating a File object that represents the disk file.
        PrintStream o = new PrintStream(out);

        // Store current System.out before assigning a new value
        PrintStream console = System.out;
        // Assign o to output stream
        System.setOut(o);

        System.out.println(answer.size());
        // System.out.println(answer);
        for (int i = 0; i < answer.size(); i++) {
            System.out.print(answer.get(i) + " ");
        }
        System.out.println();

        System.setOut(console);
        // System.out.println("Elapsed time = " + (System.currentTimeMillis() - startTime) + " ms.");


        // test
        String[] strs = {"IiOioIoO", "IiOOIo", "IoiOiO", "io", "IIIIOOOO"};
        List<String> list = InterleavedOutput(strs);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

         */



    }

    public static void solve(Scanner input, int a, int b) {
        int m = (a + b) / 2;
        System.out.println(m);
        String s = input.next();
        if (s.equals("CORRECT")) {
            return;
        } else if (s.equals("TOO_SMALL")) {
            solve(input, m + 1, b);
        } else {
            solve(input, a, m - 1);
        }
    }


    private static List<Integer> morePizza(int M, int[] nums) {

        List<Integer> result = new ArrayList<>();
        int size = nums.length;
        int max = 0;

        // reverse retrieve: find the closest answer quickly
        for (int i = size - 1; i >= 0; i--) {
            //
            int sum = nums[i];

            List<Integer> curList = new ArrayList<>();
            curList.add(i);

            for (int j = i - 1; j >= 0; j--) {
                int temp = sum + nums[j];

                if (temp > M) continue;
                sum = temp;
                curList.add(j);
                if (temp == M) break;

            }

            if (max < sum) {
                max = sum;
                result = curList;
                if (max == M) break;
            }

        }

        Collections.reverse(result);

        // System.out.println(max);
        // System.out.println(result.size());
        // System.out.println(result);
        return result;

    }

    public static List<String> InterleavedOutput(String[] strs) {
        List<String> ans = new ArrayList<>();

        for (int i = 0; i < strs.length; i++ ) {
            int cnt = 0;
            Queue<Character> qu = new LinkedList<>();

            for (char c : strs[i].toCharArray() ) {
                if (c == 'I' || c == 'i' ) {
                    qu.offer(c);
                }else {
                    // c == 'o' or  'O'
                    if (c == 'O' && qu.peek() == 'I' ) {
                        cnt++;
                    }
                    qu.poll();
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Case #");
            sb.append(Integer.toString(i + 1));
            sb.append(": " );
            sb.append(Integer.toString(cnt));
            ans.add(sb.toString());
        }

        return ans;
    }


}
