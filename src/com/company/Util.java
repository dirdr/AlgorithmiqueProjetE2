package com.company;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Util {

    /**
     * Create a csv file and write to it all the distances values
     * Use of the library csv writer,
     * @param result a float ArrayList containing all the calculated distances
     * @param file_path a String representing the name of the csv file
     */
    public static void WriteTabToFile(ArrayList<Float> result, String file_path) {
        File file = new File("../AlgoProjetR/" + file_path);
        try {
            FileWriter output = new FileWriter(file);
            CSVWriter writer = new CSVWriter(output);
            writer.writeNext(new String[] {"distance"});
            String[] iterable = result
                    .stream()
                    .map(String::valueOf)
                    .toArray(String[]::new);
            for (String val: iterable) {
                writer.writeNext(new String[] {val});
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print a matrix in a proper way
     *                                   [
     *                                      [1, 2, 3, 4],
     *                      ->              [4, 3, 2, 1],
     *                                      [1, 2, 3, 4],
     *                                   ]
     * @param G the matrix to print
     */
    public static void printMatrix(int[][] G, boolean reverse) {
        System.out.println("[");
        if (!reverse) {
            for (int[] array: G) {
                System.out.println(Arrays.toString(array));
            }
        } else {
            for (int i = G.length-1; i >= 0; i--) {
                System.out.println(Arrays.toString(G[i]));
            }
        }

        System.out.println("]");
    }

    /**
     * print the percentage of run achieved
     * @param number_of_run the total number of run (for the project 5000)
     * @param current_run the current run the program is calculating
     */
    public static void printProgress(int number_of_run, int current_run) {
        int percentage = (current_run*100)/number_of_run;
        if (percentage != 0) System.out.println(percentage + "% achieved!");
    }

    /**
     * Generate a random tab
     * @param n the tab length
     * @param lb low bound
     * @param hb high bound
     * @return the randomly generated tab with the specified parameter
     */
    public static int[] randomTab(int n, int lb, int hb) {
        int[] returnable = new int[n];
        for (int i = 0; i < n; i++) {
            returnable[i] = randomNum(lb, hb);
        }
        return returnable;
    }

    /**
     * create and return a random matrix
     * @param r the number of lines
     * @param c the number of columns
     * @param lb low bound for the number generation
     * @param hb high bound for the number generation
     * @return the generated matrix with specified parameters
     */
    public static int[][] randomMatrix(int r, int c, int lb, int hb) {
        int[][] returnable = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                returnable[i][j] = Util.randomNum(lb, hb);
            }
        }
        return returnable;
    }

    /**
     * generate a random  matrix
     * @param r number of row
     * @param c number of col
     * @param hb high bound
     * @return the randomly generate matrix with specified parameter
     */
    public static int[][] randomWarehouse(int r, int c, int lb, int hb) {
        int[][] returnable = new int[r][c+1];
        for (int i = 0; i < r; i++) {
            returnable[i][0] = 0;
            for (int j = 1; j < c+1; j++) {
                returnable[i][j] = Util.randomNum(lb, hb);
            }
            Arrays.sort(returnable[i]);
        }
        return returnable;
    }

    /**
     * create and return a mark distribution (0 -> 20)
     * @param n the number of unit
     * @param H the number of work hours
     * @return the generated distribution
     */
    public static int[][] randomMarkDistribution(int n, int H) {
        int[][] returnable = new int[n][H+1];
        for (int i = 0; i < n; i++) {
            returnable[i][0] = 0;
            for (int j = 0; j < H+1; j++) {
                returnable[i][j] = Util.randomNum(1, 20);
            }
            Arrays.sort(returnable[i]);
        }
        return returnable;
    }

    /**
     * Generate a Pseudo-random number
     * @param lb low bound
     * @param hb high bound
     * @return a pseudo-random integer from lb to hb
     */
    public static int randomNum(int lb, int hb) {
        return ThreadLocalRandom.current().nextInt(lb, hb + 1);
    }

}
