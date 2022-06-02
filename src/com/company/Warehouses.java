package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Warehouses {

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
        Util.WriteTabToFile(collector, "Entrepots.csv");
    }


    /**
     * calculate the distance d = (v* - g) /  v*
     * @return the distance
     */
    public static float calculateDistance() {
        int n = Util.randomNum(1, 150);
        int S = Util.randomNum(1, 500);
        int[][] G = Util.randomWarehouse(
                n,
                S,
                1,
                2000
        );
        int dp_gain = calculateM(G)[1][G.length][G[0].length - 1];
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
    public static int[][][] calculateM(int[][] G) {
        int n = G.length;
        int S = G[0].length - 1;
        int[][] M = new int[n + 1][S + 1];
        int[][] A = new int[n + 1][S + 1];
        for (int k = 0; k <= n; k++) {
            M[k][0] = 0;
        }
        for (int s = 0; s <= S; s++) {
            M[0][s] = 0;
        }
        for (int k = 1; k <= n; k++) {
            for (int s = 1; s <= S; s++) {
                for (int s_prime = 0; s_prime <= s; s_prime++) {
                    int mksprime = M[k - 1][s - s_prime] + G[k - 1][s_prime];
                    if (mksprime > M[k][s]) {
                        M[k][s] = mksprime;
                        A[k][s] = s_prime;
                    }
                }
            }
        }
        return new int[][][] {A, M};
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
        int[] warehouses = new int[n]; // the warehouses array store how many stock can be allocated to warehouse[i]
        int total_gain = 0;
        Arrays.fill(warehouses, 1); // we can allocate only one stock to each warehouse
        while (S > 0) {
            int max = 0;
            int index = 0;
            for (int i = 0; i < n; i++) {
                int gain = G[i][warehouses[i]] - G[i][warehouses[i] - 1];
                if (gain > max) {
                    max = gain;
                    index = i; // we keep the index-linked to the maximum value
                }
            }
            warehouses[index]++; //allocate the stock
            total_gain += max;
            S--;
        }
        return total_gain;
    }

    /**
     *
     * @param A the arg(m) matrix
     * @param k current warehouse
     * @param s number of stock
     */
    public static void aro(int[][] A, int k, int s) {
        if (k == 0) return;
        aro(A, k-1, s - A[k][s]);
        System.out.printf("warehouse :  %d stock : %d", k-1, A[k][s]);
    }

}