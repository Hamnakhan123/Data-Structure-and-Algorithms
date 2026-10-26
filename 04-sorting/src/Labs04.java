import java.util.Arrays;
import java.util.Comparator;

/**
 * Module 04 — reference solutions for the lab tasks of Sessions 1-3.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out Labs04.java
 * Run:      java -cp out Labs04 1    (Session 1 labs; other numbers for other sessions; no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs04 {

    // >>> genericInsertion
    /** Insertion sort for any type with a Comparator (stable, in place). */
    static <T> void insertionSort(T[] a, Comparator<? super T> cmp) {
        for (int i = 1; i < a.length; i++) {
            T key = a[i];
            int j = i - 1;
            while (j >= 0 && cmp.compare(a[j], key) > 0) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }
    // <<< genericInsertion

    // >>> bubbleSwaps
    /** Number of swaps bubble sort performs = number of inversions (each swap fixes exactly one). */
    static int bubbleSwapCount(int[] input) {
        int[] a = input.clone();
        int swaps = 0;
        for (int pass = 1; pass < a.length; pass++)
            for (int j = 0; j < a.length - pass; j++)
                if (a[j] > a[j + 1]) {
                    int t = a[j]; a[j] = a[j + 1]; a[j + 1] = t;
                    swaps++;
                }
        return swaps;
    }
    // <<< bubbleSwaps

    // >>> mergeTwo
    /** Merge two sorted arrays into a new sorted array: O(n + m). */
    static int[] mergeSorted(int[] x, int[] y) {
        int[] out = new int[x.length + y.length];
        int i = 0, j = 0, k = 0;
        while (i < x.length && j < y.length) out[k++] = (x[i] <= y[j]) ? x[i++] : y[j++];
        while (i < x.length) out[k++] = x[i++];
        while (j < y.length) out[k++] = y[j++];
        return out;
    }
    // <<< mergeTwo

    // >>> quickSelect
    /** k-th smallest (k = 0-based) in expected O(n) using quick sort's partition on one side only. */
    static int quickSelect(int[] input, int k) {
        int[] a = input.clone();
        int lo = 0, hi = a.length - 1;
        java.util.Random rnd = new java.util.Random(3);
        while (true) {
            int r = lo + rnd.nextInt(hi - lo + 1);
            int t = a[r]; a[r] = a[hi]; a[hi] = t;        // random pivot to the end
            int p = MergeQuickSort.partition(a, lo, hi);
            if (p == k) return a[p];
            if (p < k) lo = p + 1; else hi = p - 1;      // recurse into ONE side only
        }
    }
    // <<< quickSelect

    // >>> sortColors
    /** Sort an array containing only 0, 1, 2 in ONE pass (Dutch national flag). */
    static void sortColors(int[] a) {
        int lo = 0, i = 0, hi = a.length - 1;
        while (i <= hi) {
            if (a[i] == 0) { int t = a[lo]; a[lo++] = a[i]; a[i++] = t; }
            else if (a[i] == 2) { int t = a[hi]; a[hi--] = a[i]; a[i] = t; }
            else i++;
        }
    }
    // <<< sortColors

    // >>> sortDates
    /** Sort dates {day, month, year} chronologically with three STABLE passes (radix idea):
     *  least significant key first (day), then month, then year. */
    static void sortDates(int[][] dates) {
        Comparator<int[]> byDay = Comparator.comparingInt(d -> d[0]);
        Comparator<int[]> byMonth = Comparator.comparingInt(d -> d[1]);
        Comparator<int[]> byYear = Comparator.comparingInt(d -> d[2]);
        Arrays.sort(dates, byDay);     // Arrays.sort on objects is stable (TimSort)
        Arrays.sort(dates, byMonth);
        Arrays.sort(dates, byYear);
    }
    // <<< sortDates

    public static void main(String[] args) {
        int session = args.length > 0 ? Integer.parseInt(args[0]) : 0;   // 0 = all sessions
        if (session == 0 || session == 1) {
            System.out.println("-- Session 1 labs --");
            String[] cities = {"Quetta", "Lahore", "Karachi", "Multan", "Hyderabad", "Sukkur"};
            insertionSort(cities, Comparator.comparingInt(String::length));
            System.out.println("cities by length (stable): " + Arrays.toString(cities));
            System.out.println("bubble swaps for [3,1,2,5,4] = " + bubbleSwapCount(new int[]{3, 1, 2, 5, 4})
                    + ", inversions = " + MergeQuickSort.countInversions(new int[]{3, 1, 2, 5, 4}));
        }
        if (session == 0 || session == 2) {
            System.out.println("-- Session 2 labs --");
            System.out.println("mergeSorted = " + Arrays.toString(mergeSorted(new int[]{1, 4, 9}, new int[]{2, 3, 10, 12})));
            int[] marks = {67, 23, 89, 45, 91, 12, 56};
            System.out.println("3rd smallest of " + Arrays.toString(marks) + " = " + quickSelect(marks, 2)
                    + ", median = " + quickSelect(marks, marks.length / 2));
        }
        if (session == 0 || session == 3) {
            System.out.println("-- Session 3 labs --");
            int[] colors = {2, 0, 2, 1, 1, 0, 0, 2, 1};
            sortColors(colors);
            System.out.println("sortColors -> " + Arrays.toString(colors));
            int[][] dates = {{14, 8, 1947}, {23, 3, 1940}, {25, 12, 1876}, {14, 8, 1940}, {1, 1, 2000}};
            sortDates(dates);
            System.out.println("dates sorted -> " + Arrays.deepToString(dates));
        }
    }
}
