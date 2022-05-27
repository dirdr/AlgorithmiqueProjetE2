package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Warehouses {

    public static void main(String[] args) {
        runsThenWrite(5000);
    }

    public static void runsThenWrite(int number_of_run) {
        ArrayList<Float> collector = new ArrayList<>();
        for (int i = 1; i <= number_of_run; i++) {
            collector.add(calculateDistance());
            Util.printProgress(number_of_run, i);
        }
        Util.WriteTabToFile(collector, "Entrepots.csv");
    }

    public static float calculateDistance() {
        int n = Util.randomNum(1, 500);
        int S = Util.randomNum(250, 700);
        int[][] G = Util.randomWarehouse(
                n,
                S,
                2*n
        );
        int dp_gain = calculateM(G)[G.length][G[0].length - 1];
        int greedy_gain = calculateGreedy(G);
        return (float) (dp_gain - greedy_gain) / dp_gain;
    }

    /**
     * Dynamic programing function to calculate the optimal value to the problem
     * at any time m(k, s) is the max gain for s stock and k warehouses
     *
     * @param G The gain matrix -> g(k, s) is the gain for giving s stock to the k'th warehouse
     * @return M -> M[n][S] is the optimal value calculated by the Dynamic programming
     */
    public static int[][] calculateM(int[][] G) {
        int n = G.length;
        int S = G[0].length - 1;
        int[][] M = new int[n + 1][S + 1];
        for (int k = 0; k <= n; k++) {
            M[k][0] = 0;
        }
        for (int s = 0; s <= S; s++) {
            M[0][s] = 0;
        }
        for (int k = 1; k <= n; k++) {
            for (int s = 1; s <= S; s++) {
                for (int sprime = 0; sprime <= s; sprime++) {
                    int mksprime = M[k - 1][s - sprime] + G[k - 1][sprime];
                    if (mksprime > M[k][s]) {
                        M[k][s] = mksprime;
                    }
                }
            }
        }
        return M;
    }

    /**
     * The greedy approach to this problem is the following
     * the stock is distributed one by one among the warehouses
     * The allocated stock is the one that yields a maximum gain
     *
     * @param G The gain matrix -> g(k, s) is the gain for giving s stock to the k'th warehouse
     * @return the total gain calculated by the greedy method
     */
    public static int calculateGreedy(int[][] G) {
        int S = G[0].length - 1;
        int n = G.length;
        int[] warehouses = new int[n];
        int total_gain = 0;
        Arrays.fill(warehouses, 1);
        while (S > 0) {
            int max = 0;
            int index = 0;
            for (int i = 0; i < n; i++) {
                int gain = G[i][warehouses[i]] - G[i][warehouses[i] - 1];
                if (gain > max) {
                    max = gain;
                    index = i;
                }
            }
            warehouses[index]++;
            total_gain += max;
            S--;
        }
        return total_gain;
    }

}