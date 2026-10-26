import java.util.Arrays;

/**
 * Module 04 · Session 01 — Bubble, selection and insertion sort.
 *
 * Each sort prints the array after every pass (when trace is on) and counts
 * comparisons and writes/swaps so their behaviour on random, sorted and
 * reversed input can be compared side by side.
 *
 * Compile:  javac -d out ElementarySorts.java
 * Run:      java -cp out ElementarySorts
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class ElementarySorts {

    static long comparisons, moves;

    static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
        moves++;
    }

    /** Bubble sort: adjacent swaps push the largest remaining value to the end each pass.
     *  Stops early if a pass makes no swap (already sorted -> O(n)). */
    static void bubbleSort(int[] a, boolean trace) {
        int n = a.length;
        for (int pass = 1; pass < n; pass++) {
            boolean swapped = false;
            for (int j = 0; j < n - pass; j++) {      // last 'pass-1' items are already in place
                comparisons++;
                if (a[j] > a[j + 1]) { swap(a, j, j + 1); swapped = true; }
            }
            if (trace) System.out.println("  pass " + pass + ": " + Arrays.toString(a));
            if (!swapped) break;
        }
    }

    /** Selection sort: find the minimum of the unsorted part and swap it into place.
     *  Always n(n-1)/2 comparisons but at most n-1 swaps. */
    static void selectionSort(int[] a, boolean trace) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                comparisons++;
                if (a[j] < a[min]) min = j;
            }
            if (min != i) swap(a, i, min);
            if (trace) System.out.println("  i=" + i + " min->" + a[i] + ": " + Arrays.toString(a));
        }
    }

    /** Insertion sort: grow a sorted prefix; shift bigger items right and drop the key into the gap.
     *  O(n) on nearly sorted input, stable, in place. */
    static void insertionSort(int[] a, boolean trace) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= 0) {
                comparisons++;
                if (a[j] <= key) break;   // '<=' keeps equal keys in order (stability)
                a[j + 1] = a[j];          // shift right
                moves++;
                j--;
            }
            a[j + 1] = key;
            if (trace) System.out.println("  insert " + key + ": " + Arrays.toString(a));
        }
    }

    interface Sorter { void sort(int[] a, boolean trace); }

    static void measure(String name, Sorter s, int[] input) {
        int[] a = input.clone();
        comparisons = moves = 0;
        s.sort(a, false);
        System.out.printf("  %-10s comparisons %,8d   swaps/shifts %,8d%n", name, comparisons, moves);
    }

    public static void main(String[] args) {
        int[] data = {29, 10, 14, 37, 13};
        System.out.println("Bubble sort " + Arrays.toString(data));
        bubbleSort(data.clone(), true);
        System.out.println("Selection sort " + Arrays.toString(data));
        selectionSort(data.clone(), true);
        System.out.println("Insertion sort " + Arrays.toString(data));
        insertionSort(data.clone(), true);

        int n = 1000;
        java.util.Random rnd = new java.util.Random(42);
        int[] random = new int[n], sorted = new int[n], reversed = new int[n];
        for (int i = 0; i < n; i++) {
            random[i] = rnd.nextInt(10_000);
            sorted[i] = i;
            reversed[i] = n - i;
        }
        String[] labels = {"random", "already sorted", "reversed"};
        int[][] inputs = {random, sorted, reversed};
        for (int k = 0; k < 3; k++) {
            System.out.println("\nn = " + n + ", " + labels[k] + " input:");
            measure("bubble", ElementarySorts::bubbleSort, inputs[k]);
            measure("selection", ElementarySorts::selectionSort, inputs[k]);
            measure("insertion", ElementarySorts::insertionSort, inputs[k]);
        }
    }
}
