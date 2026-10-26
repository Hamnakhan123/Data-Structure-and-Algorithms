import java.util.Arrays;
import java.util.Random;

/**
 * Module 04 · Session 02 — Merge sort and quick sort (divide and conquer).
 *
 * Top-down merge sort with a printed merge trace, inversion counting as a
 * by-product of merging, Lomuto-partition quick sort with a partition trace,
 * randomised pivots, and 3-way (Dutch national flag) partitioning for duplicates.
 *
 * Compile:  javac -d out MergeQuickSort.java
 * Run:      java -cp out MergeQuickSort
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class MergeQuickSort {

    static long comparisons;
    static boolean trace = false;
    static final Random RND = new Random(7);

    // ------------------------------------------------------------ merge sort

    public static void mergeSort(int[] a) {
        int[] buf = new int[a.length];      // one shared buffer: O(n) extra space
        mergeSort(a, buf, 0, a.length - 1, 0);
    }

    /** Sorts a[lo..hi]; returns the number of inversions (pairs i<j with a[i]>a[j]). */
    static long mergeSort(int[] a, int[] buf, int lo, int hi, int depth) {
        if (lo >= hi) return 0;                        // 0 or 1 element: already sorted
        int mid = lo + (hi - lo) / 2;
        long inv = mergeSort(a, buf, lo, mid, depth + 1);
        inv += mergeSort(a, buf, mid + 1, hi, depth + 1);
        inv += merge(a, buf, lo, mid, hi);
        if (trace) System.out.println("  ".repeat(depth) + "merged [" + lo + ".." + hi + "] -> "
                + Arrays.toString(Arrays.copyOfRange(a, lo, hi + 1)));
        return inv;
    }

    /** Merges sorted a[lo..mid] and a[mid+1..hi]. */
    static long merge(int[] a, int[] buf, int lo, int mid, int hi) {
        System.arraycopy(a, lo, buf, lo, hi - lo + 1);
        int i = lo, j = mid + 1;
        long inv = 0;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = buf[j++];              // left exhausted
            else if (j > hi) a[k] = buf[i++];          // right exhausted
            else {
                comparisons++;
                if (buf[j] < buf[i]) {                 // strict '<' keeps it stable
                    a[k] = buf[j++];
                    inv += mid - i + 1;                // buf[j] is smaller than all remaining left items
                } else a[k] = buf[i++];
            }
        }
        return inv;
    }

    static long countInversions(int[] a) {
        int[] c = a.clone();
        return mergeSort(c, new int[c.length], 0, c.length - 1, 0);
    }

    // ------------------------------------------------------------ quick sort

    public static void quickSort(int[] a) {
        quickSort(a, 0, a.length - 1, false);
    }

    static void quickSort(int[] a, int lo, int hi, boolean randomPivot) {
        if (lo >= hi) return;
        if (randomPivot) swap(a, lo + RND.nextInt(hi - lo + 1), hi);   // random pivot moved to the end
        int p = partition(a, lo, hi);
        quickSort(a, lo, p - 1, randomPivot);
        quickSort(a, p + 1, hi, randomPivot);
    }

    /** Lomuto partition around pivot = a[hi]. Returns the pivot's final index. */
    static int partition(int[] a, int lo, int hi) {
        int pivot = a[hi];
        int i = lo - 1;                                 // a[lo..i] holds values <= pivot
        for (int j = lo; j < hi; j++) {
            comparisons++;
            if (a[j] <= pivot) swap(a, ++i, j);
        }
        swap(a, i + 1, hi);
        if (trace) System.out.println("  pivot " + pivot + " -> index " + (i + 1) + ": "
                + Arrays.toString(Arrays.copyOfRange(a, lo, hi + 1)));
        return i + 1;
    }

    /** 3-way quick sort: < pivot | == pivot | > pivot. Fast when there are many duplicates. */
    static void quickSort3(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int pivot = a[lo + RND.nextInt(hi - lo + 1)];
        int lt = lo, i = lo, gt = hi;
        while (i <= gt) {
            comparisons++;
            if (a[i] < pivot) swap(a, lt++, i++);
            else if (a[i] > pivot) swap(a, i, gt--);
            else i++;
        }
        quickSort3(a, lo, lt - 1);
        quickSort3(a, gt + 1, hi);
    }

    static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

    static int[] randomArray(int n, int bound) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = RND.nextInt(bound);
        return a;
    }

    public static void main(String[] args) {
        trace = true;
        int[] a = {38, 27, 43, 3, 9, 82, 10};
        System.out.println("Merge sort trace for " + Arrays.toString(a));
        mergeSort(a);
        System.out.println("sorted: " + Arrays.toString(a));

        int[] b = {10, 80, 30, 90, 40, 50, 70};
        System.out.println("\nQuick sort (Lomuto, last element pivot) trace for " + Arrays.toString(b));
        quickSort(b);
        System.out.println("sorted: " + Arrays.toString(b));
        trace = false;

        System.out.println("\ninversions in [2, 4, 1, 3, 5] = " + countInversions(new int[]{2, 4, 1, 3, 5}));
        System.out.println("inversions in [5, 4, 3, 2, 1] = " + countInversions(new int[]{5, 4, 3, 2, 1}) + " (n(n-1)/2)");

        int n = 20_000;
        int[] rnd = randomArray(n, 1_000_000);
        int[] sorted = rnd.clone();
        Arrays.sort(sorted);
        int[] dups = randomArray(n, 3);   // only values 0, 1, 2
        System.out.printf("%nComparisons for n = %,d:%n", n);
        System.out.printf("  %-34s %12s%n", "case", "comparisons");
        comparisons = 0; mergeSort(rnd.clone());
        System.out.printf("  %-34s %,12d%n", "merge sort, random", comparisons);
        comparisons = 0; mergeSort(sorted.clone());
        System.out.printf("  %-34s %,12d%n", "merge sort, sorted", comparisons);
        comparisons = 0; quickSort(rnd.clone());
        System.out.printf("  %-34s %,12d%n", "quick sort (last pivot), random", comparisons);
        int[] s2 = Arrays.copyOf(sorted, 3000);   // small n: the O(n^2) case recurses n deep
        comparisons = 0; quickSort(s2, 0, s2.length - 1, false);
        System.out.printf("  %-34s %,12d   <- n = 3,000 only! O(n^2)%n", "quick sort (last pivot), sorted", comparisons);
        comparisons = 0; quickSort(sorted.clone(), 0, n - 1, true);
        System.out.printf("  %-34s %,12d%n", "quick sort (random pivot), sorted", comparisons);
        comparisons = 0; quickSort(dups.clone(), 0, n - 1, true);
        System.out.printf("  %-34s %,12d%n", "2-way quick sort, 3 distinct keys", comparisons);
        int[] d3 = dups.clone();
        comparisons = 0; quickSort3(d3, 0, n - 1);
        System.out.printf("  %-34s %,12d%n", "3-way quick sort, 3 distinct keys", comparisons);
        System.out.printf("  reference n log2 n = %,d%n", (long) (n * (Math.log(n) / Math.log(2))));
    }
}
