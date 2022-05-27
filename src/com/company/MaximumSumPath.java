package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class MaximumSumPath {

    public static void main(String[] args) {
        runsThenWrite(5000);
    }


    public static float calculateDistance() {
        int[] T = Util.randomTab(100, 1, 100);
        int dp_sum = calculateM(T)[0];
        int greedy_sum = calculateGreedy(T);
        return (float) (dp_sum - greedy_sum) / dp_sum;
    }

    public static void runsThenWrite(int number_of_runs) {
        ArrayList<Float> collector = new ArrayList<>();
        for (int i = 1; i <= number_of_runs; i++) {
            collector.add(calculateDistance());
        }
        Util.WriteTabToFile(collector, "MaximumSumPath.csv");
    }


    /**
     * get the index of the left subchild of i -> g(i), which is :
     * si(l(g(i))) + p_i with:
     * si -> starting index
     * l -> level
     * g(i) left subchild of i
     * p_i the relative position of the index i in his level
     * @param i the starting index
     * @return g(i) the index of the left subchild of i
     */
    public static int left(int i) {
        return getIndexRange(getLevel(i)+1)[0] + getP(i);
    }


    /**
     * get the relative position p of an index i in his level l
     * @param i the index
     * @return p the relative position of i in his level
     */
    public static int getP(int i) {
        int card = numberOfElements(getLevel(i)-1);
        return i - card;
    }

    /**
     * Calculate the number of element in a triangle of l level using Gauss formula
     * @param level 0-indexed level (we have to put (level + 1) in the gauss formula)
     * @return the sum of all element in the triangle of l level
     */
    public static int numberOfElements(int level) {
        return ((level+1) * ((level+1) + 1) ) / 2;
    }

    /**
     * get the index of the right subchild of i -> r(i)
     * @param i the starting index
     * @return r(i) the index of the left subchild
     */
    public static int right(int i) {
        return left(i)+1;
    }

    /**
     * calculate the triangle level of the index i, which is:
     * @param i the index
     * @return l the triangle level, starting from the bottom
     */
    public static int getLevel(int i) {
        int l = 0;
        while (l <= i) {
            int sr = getIndexRange(l)[0];
            int er = getIndexRange(l)[1];
            if (sr <= i && i < er) {
                return l;
            }
            l++;
        }
        return -1; // the level of the given index has not been found in the triangle
    }



    /**
     * return the index range {start, end} for a given level,
     * the function is recursive, based on the equation range(l) = {range(l-1)[1], range(l-1)[1] + level + 1}
     * @param level the level we want to get the index range
     * @return a {start, end} range int array
     */
    public static int[] getIndexRange(int level) {
        if (level == 0) return new int[] {0, 1};
        int starting_range = getIndexRange(level-1)[1];
        return new int[] {starting_range, starting_range + level + 1};
    }


    public static int calculateGreedy(int[] T) {
        int n = T.length;
        int i = 0;
        int sum = 0;
        while (left(i) < n && right(i) < n) {
            sum += T[i];
            i = T[left(i)] > T[right(i)] ? left(i): right(i);
        }
        return sum;
    }


    /**
     * Dynamic programming function to calculate the optimal value of the maximum sum path
     *
     * @param T
     * @return M
     */
    public static int[] calculateM(int[] T) {
        int n = T.length;
        int[] M = Arrays.copyOf(T, T.length);
        for (int i = n-1; i >= 0; i--) {
            if (left(i) < n && right(i) < n) {
                M[i] = Math.max(M[left(i)], M[right(i)]) + T[i];
            }
        }
        return M;
    }

}