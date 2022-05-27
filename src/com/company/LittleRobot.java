package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LittleRobot {

    public static void main(String[] args) {
        runsThenWrite(5000);
    }

    public static void runsThenWrite(int number_of_run) {
        ArrayList<Float> collector = new ArrayList<>();
        for (int i = 1; i <= number_of_run; i++) {
            collector.add(calculateDistance());
            Util.printProgress(number_of_run, i);
        }
        Util.WriteTabToFile(collector, "LittleRobot.csv");
    }

    public static float calculateDistance() {
        int L = Util.randomNum(2, 1000);
        int C = Util.randomNum(2, 1000);
        int[][][] direction_cost = {
                        Util.randomMatrix(L, C, 1, 100),
                        Util.randomMatrix(L, C, 1, 100),
                        Util.randomMatrix(L, C, 1, 100),
        };
        int[][] dp = calculateM(L, C, direction_cost);
        int dp_cost = dp[L-1][C-1];
        int greedy_cost = calculateGreedy(L, C, direction_cost);
        return (float) (greedy_cost - dp_cost) / dp_cost;
    }

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
        HashMap<String, Integer> map = new HashMap<>();
        int l = 0;
        int c = 0;
        int cost = 0;
        while (l != L - 1 || c != C - 1) {
            map.put("10", n(l, c, L, direction_cost[0]));
            map.put("11", ne(l, c, L, C, direction_cost[1]));
            map.put("01", e(l, c, L, C, direction_cost[2]));
            Map.Entry<String, Integer> etr = Collections.min(map.entrySet(), Map.Entry.comparingByValue());
            int min = etr.getValue();
            String direction = etr.getKey();
            l += Character.getNumericValue(direction.charAt(0));
            c += Character.getNumericValue(direction.charAt(1));
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

}