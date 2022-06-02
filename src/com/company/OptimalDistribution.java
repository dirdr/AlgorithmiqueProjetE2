package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class OptimalDistribution {

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
        Util.WriteTabToFile(collector, "OptimalDistribution.csv");
    }

    /**
     * calculate the distance d = (v* - g) /  v*
     * @return the distance
     */
    public static float calculateDistance() {
        int n = Util.randomNum(2, 10);
        int H = Util.randomNum(10, 60);
        int[][] E = Util.randomMarkDistribution(n, H);
        Util.printMatrix(E, false);
        int dp_sum = calculerMA(E)[0][n][H];
        int greedy_sum = calculateGreedy(E);
        return (float) (dp_sum - greedy_sum) / dp_sum;
    }

    /**
     * the greedy approach for this problem is the following
     * hour of work are allocated one by one to subject that yield the maximum mark gain
     * @param E the E matrix -> E(n, h) is the expected mark for the subject n with h hours of work
     * @return the total mark, for n subject and H hours of work
     */
    public static int calculateGreedy(int[][] E) {
        int H = E[0].length - 1;
        int n = E.length;
        int[] attribution = new int[n]; // the attribution array store how many hours of work that can be allocated
        Arrays.fill(attribution, 1);
        int total_mark = 0;
        for (int[] courses: E) total_mark += courses[0]; // even with 0 Hours of work, the mark will not be 0
        while (H > 0) {
            int max = 0;
            int index = 0;
            for (int i = 1; i < n; i++) {
                int gain = E[i][attribution[i]] - E[i][attribution[i]-1];
                if (gain > max) {
                    max = gain;
                    index = i; // we keep track of the maximum index
                }
            }
            attribution[index]++; // allocate the hour of work
            total_mark += max;
            H--;
        }
        return total_mark;
    }

    /**
     * dynamic programming function to calculate the optimal value for this problem
     * at any time m(n, h) is the optimal mark sum for n subject and h hours of work
     * @param E the E matrix -> E(n, h) is the expected mark for the subject n with h hours of work
     * @return a three dimension tab {M, A},
     *                            M(n, H) is the optimal mark sum
     *                            A(n, h) represent the optimal distribution of work
     */
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

    /**
     * print function for the expected mark matrix
     * @param E the expected mark matrix
     */
    static void print(int[][] E){
        int n = E.length;
        int H = E[0].length - 1;
        System.out.println("[");
        for (int i = 0; i < n; i++) {
            int h = 0;
            while (h < H+1 && E[i][h] < 20) h++;
            if (h == H+1)
                System.out.printf("subject %d %s\n",i,Arrays.toString(E[i]));
            else {
                int[] Ei = Arrays.copyOfRange(E[i],0,h+1);
                String Si = Arrays.toString(Ei);
                int li = Si.length();
                Si = Si.substring(0,li-1) + ", ...]";
                System.out.printf("i = %d %s\n",i,Si);
            }
        }
        System.out.println("]");
    }

    /**
     * print the optimal distribution of hour
     * @param A the matrix containing the distribution of work
     * @param E the expected mark matrix
     * @param k the current subject
     * @param h the current number of hours allocated to the subject
     */
    public static void aro(int[][] A, int[][] E, int k, int h){
        if (k == 0) return;
        int akh = A[k][h];
        aro(A,E,k-1,h-akh);
        System.out.printf("subject %d, <-- %d hours, estimated mark %d\n", k-1, akh, E[k-1][akh]);
    }

}

