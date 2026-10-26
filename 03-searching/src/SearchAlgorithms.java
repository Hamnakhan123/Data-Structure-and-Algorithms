import java.util.Arrays;

/**
 * Module 03 · Session 01 — Linear search, binary search and friends.
 *
 * Linear and sentinel search, iterative and recursive binary search (with a
 * printed trace), jump search, interpolation search and exponential search.
 * Every method counts its comparisons so we can compare them on the same data.
 *
 * Compile:  javac -d out SearchAlgorithms.java
 * Run:      java -cp out SearchAlgorithms
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class SearchAlgorithms {

    static long comparisons;

    /** O(n): check every element left to right. Works on unsorted data. */
    static int linearSearch(int[] a, int key) {
        for (int i = 0; i < a.length; i++) {
            comparisons++;
            if (a[i] == key) return i;
        }
        return -1;
    }

    /** Sentinel search: place the key at the end so the loop needs no bounds test. */
    static int sentinelSearch(int[] a, int key) {
        int n = a.length;
        if (n == 0) return -1;
        int last = a[n - 1];
        a[n - 1] = key;                // sentinel guarantees the loop stops
        int i = 0;
        while (a[i] != key) { i++; comparisons++; }
        a[n - 1] = last;               // restore the array
        if (i < n - 1 || last == key) return i;
        return -1;
    }

    /** O(log n) iterative binary search on a sorted array. Prints each step if trace is true. */
    static int binarySearch(int[] a, int key, boolean trace) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;          // avoids int overflow of (lo + hi)
            comparisons++;
            if (trace) System.out.printf("  lo=%2d hi=%2d mid=%2d a[mid]=%3d%n", lo, hi, mid, a[mid]);
            if (a[mid] == key) return mid;
            if (a[mid] < key) lo = mid + 1;        // key is in the right half
            else hi = mid - 1;                     // key is in the left half
        }
        return -1;
    }

    /** Recursive binary search on a[lo..hi]. */
    static int binarySearchRec(int[] a, int key, int lo, int hi) {
        if (lo > hi) return -1;                    // empty range: not found
        int mid = lo + (hi - lo) / 2;
        comparisons++;
        if (a[mid] == key) return mid;
        return a[mid] < key ? binarySearchRec(a, key, mid + 1, hi)
                            : binarySearchRec(a, key, lo, mid - 1);
    }

    /** Jump search: jump sqrt(n) blocks, then scan linearly inside one block. O(sqrt n). */
    static int jumpSearch(int[] a, int key) {
        int n = a.length, step = (int) Math.sqrt(n), prev = 0, cur = step;
        while (cur < n && a[cur - 1] < key) {      // find the block that may contain key
            comparisons++;
            prev = cur;
            cur += step;
        }
        for (int i = prev; i < Math.min(cur, n); i++) {
            comparisons++;
            if (a[i] == key) return i;
        }
        return -1;
    }

    /** Interpolation search: guess the position from the value. O(log log n) on uniform data. */
    static int interpolationSearch(int[] a, int key) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi && key >= a[lo] && key <= a[hi]) {
            comparisons++;
            if (a[hi] == a[lo]) return a[lo] == key ? lo : -1;
            int pos = lo + (int) ((long) (key - a[lo]) * (hi - lo) / (a[hi] - a[lo]));
            if (a[pos] == key) return pos;
            if (a[pos] < key) lo = pos + 1;
            else hi = pos - 1;
        }
        return -1;
    }

    /** Exponential search: find a range [2^(k-1), 2^k] containing key, then binary search it. */
    static int exponentialSearch(int[] a, int key) {
        if (a.length == 0) return -1;
        comparisons++;
        if (a[0] == key) return 0;
        int bound = 1;
        while (bound < a.length && a[bound] < key) {
            comparisons++;
            bound *= 2;
        }
        int lo = bound / 2, hi = Math.min(bound, a.length - 1);
        return binarySearchRec(a, key, lo, hi);
    }

    public static void main(String[] args) {
        int[] unsorted = {42, 17, 8, 99, 23, 4, 15};
        comparisons = 0;
        System.out.println("linearSearch(unsorted, 23) = " + linearSearch(unsorted, 23) + " after " + comparisons + " comparisons");
        comparisons = 0;
        System.out.println("sentinelSearch(unsorted, 15) = " + sentinelSearch(unsorted, 15) + ", array intact: " + Arrays.toString(unsorted));

        int[] sorted = {3, 8, 12, 17, 23, 31, 38, 45, 52, 60, 71, 85, 90};
        System.out.println("\nsorted = " + Arrays.toString(sorted));
        System.out.println("Binary search trace for 71:");
        comparisons = 0;
        System.out.println("  -> index " + binarySearch(sorted, 71, true) + " in " + comparisons + " comparisons");
        System.out.println("Binary search trace for 20 (absent):");
        comparisons = 0;
        System.out.println("  -> index " + binarySearch(sorted, 20, true) + " in " + comparisons + " comparisons");
        System.out.println("recursive binarySearch(45) = " + binarySearchRec(sorted, 45, 0, sorted.length - 1));

        int n = 1_000_000;
        int[] big = new int[n];
        for (int i = 0; i < n; i++) big[i] = 3 * i;   // uniformly spaced values
        int key = 3 * 777_777;
        System.out.printf("%nSearching %,d sorted values for %,d:%n", n, key);
        String[] names = {"linear", "binary", "jump", "interpolation", "exponential"};
        for (String name : names) {
            comparisons = 0;
            int idx;
            switch (name) {
                case "linear": idx = linearSearch(big, key); break;
                case "binary": idx = binarySearch(big, key, false); break;
                case "jump": idx = jumpSearch(big, key); break;
                case "interpolation": idx = interpolationSearch(big, key); break;
                default: idx = exponentialSearch(big, key);
            }
            System.out.printf("  %-14s index %,8d  comparisons %,9d%n", name, idx, comparisons);
        }
        System.out.println("\nJava library: Arrays.binarySearch(sorted, 60) = " + Arrays.binarySearch(sorted, 60)
                + ", for absent 20 = " + Arrays.binarySearch(sorted, 20) + "  (-(insertion point) - 1)");
    }
}
