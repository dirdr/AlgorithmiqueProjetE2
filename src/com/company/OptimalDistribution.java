package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class OptimalDistribution {

    public static void main(String[] args) {
        runsThenWrite(5000);
    }

    public static void runsThenWrite(int number_of_runs) {
        ArrayList<Float> collector = new ArrayList<>();
        for (int i = 1; i <= number_of_runs; i++) {
            collector.add(calculateDistance());
        }
        Util.WriteTabToFile(collector, "OptimalDistribution.csv");
    }

    public static float calculateDistance() {
        int n = Util.randomNum(2, 100);
        int H = Util.randomNum(2, 200);
        int[][] E = Util.randomMarkDistribution(n, H);
        int dp_sum = calculerMA(E)[0][n][H];
        int greedy_sum = calculateGreedy(E);
        return (float) (dp_sum - greedy_sum) / dp_sum;
    }

    public static int calculateGreedy(int[][] E) {
        int H = E[0].length - 1;
        int n = E.length;
        int[] attribution = new int[n];
        Arrays.fill(attribution, 1);
        int total_mark = 0;
        for (int[] courses: E) total_mark += courses[0];
        while (H > 0) {
            int max = 0;
            int index = 0;
            for (int i = 1; i < n; i++) {
                int gain = E[i][attribution[i]] - E[i][attribution[i]-1];
                if (gain > max) {
                    max = gain;
                    index = i;
                }
            }
            attribution[index]++;
            total_mark += max;
            H--;
        }
        return total_mark;
    }

    public static int[][][] calculerMA(int[][] E) {
        int n = E.length;
        int H = E[0].length - 1;
        int[][] M = new int[n + 1][H + 1];
        int[][] A = new int[n + 1][H + 1];
        int s0 = 0;
        for (int[] ints : E) s0 = s0 + ints[0];
        for (int h = 0; h < H + 1; h++)
            M[0][h] = s0;
        for (int k = 1; k < n + 1; k++) {
            for (int h = 0; h < H + 1; h++) {
                M[k][h] = -1;
                for (int h_k = 0; h_k < h + 1; h_k++) {
                    int mkhh_k = M[k - 1][h - h_k] + E[k - 1][h_k];
                    if (mkhh_k > M[k][h]) {
                        M[k][h] = mkhh_k;
                        A[k][h] = h_k;
                    }
                }
                M[k][h] = M[k][h] - E[k - 1][0];
            }
        }
        return new int[][][]{M, A};
    }

}

