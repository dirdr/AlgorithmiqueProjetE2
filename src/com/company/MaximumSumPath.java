package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class MaximumSumPath {

    public static void main(String[] args) {
        runsThenWrite(5000);
    }

    /**
     * calculate the distance d = (v* - g) /  v*
     * @return the distance
     */
    public static float calculateDistance() {
        int LEVEL = Util.randomNum(1, 1000);
        int[] T = Util.randomTab(numberOfElements(LEVEL), 1, 100);
        int[] dp = calculateM(T, LEVEL);
        int dp_sum = dp[0];
        int greedy_sum = calculateGreedy(T, LEVEL);
        return (float) (dp_sum - greedy_sum) / dp_sum;
    }

    /**
     * call an Util function to write all the data that has been calculated
     * @param number_of_runs to perform
     */
    public static void runsThenWrite(int number_of_runs) {
        ArrayList<Float> collector = new ArrayList<>();
        for (int i = 1; i <= number_of_runs; i++) {
            collector.add(calculateDistance());
            Util.printProgress(number_of_runs, i);
        }
        Util.WriteTabToFile(collector, "MaximumSumPath.csv");
    }

    /**
     * get the index of the left sub-child of i -> g(i)
     * @param i the index you want to get the sub child of
     * @param LEVEL number of level in the triangle
     * @return g(i) the index of the left sub-child of i
     */
    public static int left(int i, int LEVEL) {
       return getLevel(i, 1, LEVEL) + i;
    }

    /**
     * get the index of the right sub child of i -> r(i)
     * @param i the starting index
     * @param LEVEL number of level in the triangle
     * @return r(i) the index of the right sub child
     */
    public static int right(int i, int LEVEL) {
        return left(i, LEVEL) + 1;
    }


    /**
     * Calculate the number of element in a triangle of l level using Gauss formula
     * @param level the level we are interested in
     * @return the sum of all element in the triangle of l level
     */
    public static int numberOfElements(int level) {
        return (level * (level + 1 )) / 2;
    }

    /**
     * Binary search function to search the level you are at
     * @param index the index you want to know the level of
     * @param l the left bound for the binary search
     * @param r the right bound for the binary search
     * @return the level of the index in the triangle
     */
    public static int getLevel(int index, int l, int r) {

        while (l <= r) {

            int middle = (l+r) /2;
            int N = numberOfElements(middle);

            if (index < N) {
                r = middle - 1;
            } else {
                l = middle + 1;
            }

        }
        return l;

    }

    /**
     * the greedy approach for this problem will choose the right or left sub-child
     * depending on which one yield the immediate maximum gain
     * @param T the triangle represented in an array -> T[0, 1, 2, ... , N] :
     *
     *                          0
     *                      1       2
     *                  3       4       5
     *              6       7       8       9
     *          10      11      12      13      14
     *
     * and so on, till N is reached
     * @param LEVEL number of level in the triangle
     * @return the greedy calculated cost
     */
    public static int calculateGreedy(int[] T, int LEVEL) {
        int n = T.length;
        int i = 0;
        int sum = 0;
        while (left(i, LEVEL) < n && right(i, LEVEL) < n) {
            sum += T[i]; // add the gain for the current position in the triangle
            i = T[left(i, LEVEL)] > T[right(i, LEVEL)] ? left(i, LEVEL): right(i, LEVEL);
        }
        return sum;
    }

    /**
     * Dynamic programming function to calculate the optimal value of the maximum sum path
     *
     * @param T the triangle represented in an array -> T[0, 1, 2, ... , N] :
     *
     *                          0
     *                      1       2
     *                  3       4       5
     *              6       7       8       9
     *          10      11      12      13      14
     *
     * and so on, till N is reached
     * @param LEVEL number of level in the triangle
     * @return M t-> m(0) the optimal gain calculated by the dynamic programming
     */
    public static int[] calculateM(int[] T, int LEVEL) {
        int n = T.length;
        int[] M = Arrays.copyOf(T, T.length);
        for (int i = n-1; i >= 0; i--) {
            if (left(i, LEVEL) < n && right(i, LEVEL) < n) {
                M[i] = Math.max(M[left(i, LEVEL)], M[right(i, LEVEL)]) + T[i]; // recurrence equation
            }
        }
        return M;
    }

    /**
     * print the problem in a proper way
     * @param M -> m(i) maximum sum path starting from i
     * @param T the triangle represented in an array
     * @param LEVEL number of level in the triangle
     */
     public static void print(int[] M, int[] T, int LEVEL) {
         System.out.println("Maximum sum path of the triangle : ");
         print_triangle(T, LEVEL);
         System.out.println("Starting from 0");
         acsm(M, T, 0, T.length, LEVEL);
     }

    /**
     * function to print the triangle in a proper way
     * @param T the representation of the triangle in an array
     * @param LEVEL number of level
     */
     public static void print_triangle(int[] T, int LEVEL) {
        int current_index = 0;
         for (int i = 0; i < LEVEL; i++) {
             for (int j = LEVEL + 1 - i; j > 1; j--) {
                 System.out.print(" ");
             }
             for (int j = 0; j <= i; j++ ) {
                 System.out.printf("%d ", T[current_index++]);
             }
             System.out.println();
         }
     }


    /**
     * print a maximum sum path starting from i
     * @param M -> m(i) maximum sum path starting from i
     * @param T the triangle represented in an array
     * @param i the index you want to start
     * @param n number of element in the triangle
     * @param LEVEL number of level in the triangle
     */
    public static void acsm(int[] M, int[] T, int i, int n, int LEVEL) {
        int level = getLevel(i, 1, LEVEL);

        if (level == LEVEL) {
            System.out.printf("%d : Path Complete! \n", T[i]);
            return;
        }

        int left = left(i, level);
        int right = right(i, level);

        if (left < n && right < n) {
            int max_dir = M[left] > M[right] ? left: right;
            System.out.printf("%d : we go %s \n", T[i], M[max_dir] == M[left] ? "Left": "Right");
            acsm(M, T, max_dir, n, LEVEL);
        } else {
            System.out.println();
        }

    }

}