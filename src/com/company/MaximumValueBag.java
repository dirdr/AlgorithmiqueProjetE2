package com.company;
import java.util.*;

class MaximumValueBag {

    public static void main(String[] Args){
        runsThenWrite(5000);
    }

    public static void runsThenWrite(int number_of_run) {
        ArrayList<Float> collector = new ArrayList<>();
        for (int i = 1; i <= number_of_run; i++) {
            collector.add(calculateDistance(false));
            Util.printProgress(number_of_run, i);
        }
        Util.WriteTabToFile(collector, "SacValMaxByValue.csv");
        collector.clear();
        for (int i = 1; i <= number_of_run; i++) {
            collector.add(calculateDistance(true));
        }
        Util.WriteTabToFile(collector, "SacValMaxByDensity.csv");
    }


    /**
     * calculate the relative distance between value from (dynamic programming | greedy) search
     * @param by_density == true, the method will be greedy by value
     *                   == false, the method will be greedy by value/size ratio
     * @return d -> the relative distance which is (v* - g) / v*
     * where v* is the optimal value, and g is the greedy value
     */
    public static float calculateDistance(boolean by_density) {
        int C = Util.randomNum(500, 10000);
        int n = Util.randomNum(1, 5000);
        int[] V  = Util.randomTab(n, 1, 500);
        int[] T = Util.randomTab(n, 1, 100);
        int[][] opti = calculateM(V, T, C);
        int[] glouton = calculateGreedy(V, T, C, by_density);
        int val_opti = opti[n][C];
        int val_glouton = glouton[1];
        return (float) (val_opti-val_glouton)/val_opti;
    }

    /**
     * print in a proper way the M tab
     * @param M the pre-calculated M tab
     */
    public static void print(int[][] M){
        int n = M.length;
        System.out.println("\t[");
        for (int i = n-1; i>=0; i--)
            System.out.println("\t\t" + Arrays.toString(M[i]));
        System.out.println("\t]");
    }

    /**
     * Calculate the sum of all element in T
     * @param T the tab in which the sum is to be calculated
     * @return the sum of all element in T
     */
     public static int sum(int[] T){
        int s = 0;
        for (int j : T) s = s + j;
        return s;
    }

    /**
     * Dynamic Programing function to calculate the optimal value to the problem
     * at any time, m(k, C) is the  max value of a bag of capacity C, with k objects inside
     * @param V the Value tab -> [0, 1, 2, i, ..., n]  V[i] the value of the ith object
     * @param T the Size tab -> [0, 1, 2, i, ..., n]  T[i] the Size of the ith object
     * @param C the Capacity of the bag
     * @return M -> M[n][C] is the optimal value calculated by the Dynamic programming
     */
    public static int[][] calculateM(int[] V, int[] T, int C){
        int n = V.length;
        int[][] M = new int[n+1][C+1];
        for (int c = 0; c < C+1; c++) M[0][c] = 0;
        for (int k = 1; k < n+1; k++)
            for (int c = 0; c < C+1; c++)
                if (c-T[k-1] < 0)
                    M[k][c] = M[k-1][c];
                else
                    M[k][c] = Math.max(M[k-1][c], V[k-1]+M[k-1][c-T[k-1]]);
        return M;
    }

    /**
     * the greedy by value method will put the objects in the bag in decreasing order of their value,
     * greedy by density will put the objects in the bag in decreasing order of the value/size ratio.
     * the method chosen depends on the by_density variable
     * @param V the Value tab -> [0, 1, 2, i, ..., n]  V[i] the value of the ith object
     * @param T the Size tab -> [0, 1, 2, i, ..., n]  V[i] the size of the ith object
     * @param C the capacity of the bag
     * @param by_density == true, the greedy search will take the max value/size ratio
     *                   == false, the greedy search will take the max value
     * @return a {total_size, total_value} tab
     */
    public static int[] calculateGreedy(int[] V, int[] T, int C, boolean by_density) {
        int[] answer = new int[2];
        Set<Integer> banned_index = new HashSet<>();
        while (answer[0] < C) {
            int index = -1;
            int max = 0;
            for (int j = 0; j < V.length; j++) {
                if (!banned_index.contains(j) && T[j] + answer[0] <= C) {
                    int comparable = by_density ? V[j]/T[j]: V[j];
                    if (comparable > max) {
                        max = comparable;
                        index = j;
                    }
                }
            }
            if (index == -1) break;
            banned_index.add(index);
            answer[0] += T[index];
            answer[1] += V[index];
        }
        return answer;
    }

    /**
     * print the content of the bag
     * @param M the precalculated M tab
     * @param V the Value tab -> [0, 1, 2, i, ..., n]  V[i] the value of the ith object
     * @param T the Size tab -> [0, 1, 2, i, ..., n]  V[i] the size of the ith object
     * @param k the number of object considered
     * @param c the current bag capacity
     */
    public static void asm(int[][] M, int[] V, int[] T, int k, int c){
        if (k == 0)
            return;
        if (M[k][c] == M[k-1][c])
            asm(M, V, T, k-1, c) ;
        else {
            asm(M,V,T,k-1,c-T[k-1]);
            System.out.printf("objet, valeur, taille = %d, %d, %d\n",
                    k-1, V[k-1], T[k-1]);
        }
    }

}