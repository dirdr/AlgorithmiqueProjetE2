package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class GoogleChainReaction {

    public static void testcase() {
        int[] N = {4, 5, 8};
        int[][] F = {
                {60, 20, 40, 50},
                {3, 2, 1, 4, 5},
                {100, 100, 100, 90, 80, 100, 90, 100}
        };
        int[][] P = {
                {0, 1, 1, 1, 0},
                {0, 1, 2, 1, 2, 3, 1, 3,}
        };
        System.out.println(setup(N[0], P[0]));
        //for (int Case = 0; Case < 3; Case++) {
        //    int ans = calculateM(N[Case], F[Case], P[Case]);
        //    System.out.printf("Cas : %d  résultat : %d \n", Case + 1, ans);
        //}

    }

    public static void main(String[] args) {
        testcase();
    }

    public static HashMap<Integer, ArrayList<Integer>> setup(int N, int[] P) {
        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        for (int n = N; n >= 0; n--) {
            if (map.get(P[n]) == null) {
                map.put(P[n], new ArrayList<>());
            } else {
                if (P[n] != 0) {
                    map.get(P[n]).add(n);
                }
            }
        }
        return map;
    }

    public static int calculateM(int N, int[] F_s, int[] P_s) {
        int answer = 0;
        int[] F = new int[N + 1];
        int[] P = new int[N + 1];
        for (int i = 1; i < N + 1; i++) {
            F[i] = F_s[i - 1];
        }
        for (int i = 1; i < N + 1; i++) {
            P[i] = P_s[i - 1];
        }
        int[] M = new int[N + 1];
        for (int n = N; n >= 1; n--) M[n] = F[n];
        for (int n = N; n >= 1; n--) {
            int[] pb = pointedBy(n, N, P); // on récupère le tableau pb qui représente les noeuds qui pointent vers n
            //System.out.println(Arrays.toString(pb));
            if (pb.length > 1) { // on est a une intersection
                int min_index = -1;
                int min = Integer.MAX_VALUE;
                //System.out.println(Arrays.toString(M));
                for (int i = 0; i < pb.length; i++) {
                    if (M[pb[i]] < min) {
                        min = M[pb[i]];
                        min_index = i;
                    }
                }
                //System.out.println("l'index minimum est : " + pb[min_index] + " avec une valeur de : " + min);
                M[n] = Math.max(M[n], M[pb[min_index]]);
                for (int i = 0; i < pb.length; i++) {
                    if (i != min_index) {
                        answer += M[pb[i]];
                        //System.out.println("On ajoute : " + M[pb[i]] + " a la réponse ");
                    }
                }
            } else if (pb.length == 1) { // on est sur un chemin
                M[n] = Math.max(M[n], F[pb[0]]);
            } else { // on est sur un noeud final (début de la reaction en chaine)
                if (P[n] == 0) answer += F[n]; // cas d'une réaction indépendante
                M[n] = Math.max(M[n], F[n]);
            }
        }
        //System.out.println(Arrays.toString(M));
        answer += M[1]; // il faut ajouter la root de l'arbre a la réponse
        //  System.out.println("On ajoute : " + M[1] + " a la réponse ");
        return answer;
    }

    public static int[] pointedBy(int n, int N, int[] P) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int ni = N; ni >= n; ni--) {
            if (P[ni] == n) {
                list.add(ni);
            }
        }
        return list.stream().mapToInt(i -> i).toArray();
    }
}