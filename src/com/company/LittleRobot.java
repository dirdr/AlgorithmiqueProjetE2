package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LittleRobot {

    public static void main(String[] args) {
        runsThenWrite(5000);
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
        Util.WriteTabToFile(collector, "LittleRobot.csv");
    }

    /**
     * calculate the distance d = (g - v*) /  v*
     * @return the distance
     */
    public static float calculateDistance() {
        int L = Util.randomNum(2, 1000);
        int C = Util.randomNum(2, 1000);
        int[][][] direction_cost = {
                        Util.randomMatrix(L, C, 1, 200),
                        Util.randomMatrix(L, C, 1, 200),
                        Util.randomMatrix(L, C, 1, 200),
        };
        int[][] dp = calculateM(L, C, direction_cost);
        //Util.printMatrix(dp, false);
        //acm(dp, L-1, C-1, L, C, direction_cost);
        int dp_cost = dp[L-1][C-1];
        int greedy_cost = calculateGreedy(L, C, direction_cost);
        return (float) (greedy_cost - dp_cost) / dp_cost;
    }

    /**
     * dynamic programming function to calculate the optimal value to the problem
     * at any time, m(l, c) is the lowest cost from (0,0) to (l, c)
     * @param L the number of lines in the matrix
     * @param C the number of column in the matrix
     * @param direction_cost a three dimension tab containing all the cost matrix,
     *                       direction_cost[0] = cost matrix for the direction "north"
     *                       direction_cost[1] = cost matrix for the direction "north-east"
     *                       direction_cost[2] = cost matrix for the direction "east"
     *                       direction_cost[i][l][c] is the cost to go in the corresponding direction from (l,c)
     * @return M -> M[L-1][C-1] the optimal value calculated by the dynamic programming
     */
    public static int[][] calculateM(int L, int C, int[][][] direction_cost) {
        int[][] M = new int[L][C];
        M[0][0] = 0;
        for (int c = 1; c < C; c++) {
            M[0][c] = M[0][c - 1] + e(0, c - 1, L, C, direction_cost[2]);
        }
        for (int l = 1; l < L; l++) {
            M[l][0] = M[l - 1][0] + n(l - 1, 0, L, direction_cost[0]);
        }
        for (int l = 1; l < L; l++) {
            for (int c = 1; c < C; c++) {
                int me = M[l][c - 1] + e(l, c - 1, L, C, direction_cost[2]);
                int mne = M[l - 1][c - 1] + ne(l - 1, c - 1, L, C, direction_cost[1]);
                int mn = M[l - 1][c] + n(l - 1, c, L, direction_cost[0]);
                M[l][c] = min(me, mne, mn);
            }
        }
        return M;
    }

    /**
     * Greedy method to calculate the cost from (0,0) to (L-1, C-1)
     * @param L number of line
     * @param C number of column
     * @param direction_cost a 3d array containing all the randomly generated cos matrix
     * @return the calculated cost
     */
    public static int calculateGreedy(int L, int C, int[][][] direction_cost) {
        HashMap<String, Integer> map = new HashMap<>(); // hashmap to link the cost with the direction associated
        int l = 0;
        int c = 0;
        int cost = 0;
        while (l != L - 1 || c != C - 1) {
            map.put("10", n(l, c, L, direction_cost[0])); //north -> l += 1 c += 0
            map.put("11", ne(l, c, L, C, direction_cost[1])); //north-east -> l += 1 c += 1
            map.put("01", e(l, c, L, C, direction_cost[2])); //east -> l += 0 c += 1

            // create an entry (direction, cost) which is the minimum between all the directions
            Map.Entry<String, Integer> etr = Collections.min(map.entrySet(), Map.Entry.comparingByValue());
            int min = etr.getValue();
            String direction = etr.getKey();
            l += Character.getNumericValue(direction.charAt(0)); // l is the first char in the string direction
            c += Character.getNumericValue(direction.charAt(1)); // c is the second char in the string direction
            cost += min;
        }
        return cost;
    }

    /*
        calculate and return the minimum between three integer
     */
    public static int min(int x, int y, int z){
        if (x <= y && x <= z) return x;
        return Math.min(y, z);
    }

    /**
     * return the cost to go north from a specific location
     * @param l the current line
     * @param c the current column
     * @param L the number of line in the map
     * @param n_cost a precalculated cost tab
     * @return the cost
     */
    public static int n(int l, int c, int L, int[][] n_cost){
        if (l == L-1) return Integer.MAX_VALUE;
        return n_cost[l][c];
    }

    /**
     * return the cost to go north est from a specific location
     * @param l the current line
     * @param c the current column
     * @param L the number of line in the map
     * @param C the number of column in the map
     * @param ne_cost a precalculated cost tab
     * @return the cost
     */
    public static int ne(int l, int c, int L, int C, int[][] ne_cost){
        if (l == L-1 || c == C-1) return Integer.MAX_VALUE;
        return ne_cost[l][c];
    }

    /**
     * return the cost to go est from a specific location
     * @param l the current line
     * @param c the current column
     * @param L the number of line in the map
     * @param C the number of column in the map
     * @param e_cost a precalculated cost tab
     * @return the cost
     */
    public static int e(int l, int c, int L, int C, int[][] e_cost){
        if (c == C-1) return Integer.MAX_VALUE;
        return e_cost[l][c];
    }

    /**
     * print function
     * call acm(l, c) for the print of the path starting from (0, 0) to (l, c)
     * @param M the m matrix calculated by the dynamic programming
     * @param l the line you want to end at
     * @param c the column you want to end at 
     */
    public static void acm(int[][] M, int l, int c, int L, int C, int[][][] direction_cost) {
        if (l == 0 && c == 0) {
            System.out.printf("(%d,%d)", l, c);
            return;
        }
        if (l == 0) {
            acm(M, 0, c - 1, L, C, direction_cost);
            System.out.printf(" -%d-> (%d,%d)", e(0, c - 1, L, C, direction_cost[2]), l, c);
            return;
        }
        if (c == 0) {
            acm(M, l - 1, 0, L, C, direction_cost);
            System.out.printf(" -%d-> (%d,%d)", n(l - 1, 0, L, direction_cost[0]), l, c);
            return;
        }

        int m1 = M[l][c - 1] + e(l, c - 1, L, C, direction_cost[2]);
        int m2 = M[l - 1][c - 1] + ne(l - 1, c - 1, L, C, direction_cost[1]);
        int m3 = M[l - 1][c] + n(l - 1, c, L, direction_cost[0]);

        if (m1 == min(m1, m2, m3)) {
            acm(M, l, c - 1, L, C, direction_cost);
            System.out.printf(" -%d-> (%d,%d)", e(l, c - 1, L, C, direction_cost[2]), l, c);
        } else if (m2 == min(m1, m2, m3)) {
            acm(M, l - 1, c - 1, L, C, direction_cost);
            System.out.printf(" -%d-> (%d,%d)", ne(l - 1, c - 1, L, C, direction_cost[1]), l, c);
        } else {
            acm(M, l - 1, c, L, C, direction_cost);
            System.out.printf(" -%d-> (%d,%d)", n(l - 1, c, L, direction_cost[0]), l, c);
        }

    }

}