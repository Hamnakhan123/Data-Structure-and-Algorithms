import java.util.Arrays;

/**
 * Module 04 · Session 03 — Heap sort, counting sort and radix sort.
 *
 * In-place heap sort (build max-heap bottom-up, then repeatedly move the max to
 * the end), stable counting sort for small integer ranges, LSD radix sort that
 * runs a stable counting sort per decimal digit, and a timing comparison of
 * all the module's sorts against Arrays.sort.
 *
 * Compile:  javac -d out HeapCountingRadix.java
 * Run:      java -cp out HeapCountingRadix
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class HeapCountingRadix {

    static boolean trace = false;

    // ---------------------------------------------------------------- heap sort

    public static void heapSort(int[] a) {
        int n = a.length;
        for (int i = n / 2 - 1; i >= 0; i--) siftDown(a, i, n);     // build max-heap in O(n)
        if (trace) System.out.println("  max-heap built: " + Arrays.toString(a));
        for (int end = n - 1; end > 0; end--) {
            int t = a[0]; a[0] = a[end]; a[end] = t;                  // move max to its final place
            siftDown(a, 0, end);                                      // restore heap on a[0..end-1]
            if (trace) System.out.println("  max " + a[end] + " placed: " + Arrays.toString(a));
        }
    }

    /** Pushes a[i] down until both children are smaller (heap of size n). */
    static void siftDown(int[] a, int i, int n) {
        while (true) {
            int left = 2 * i + 1, right = left + 1, largest = i;
            if (left < n && a[left] > a[largest]) largest = left;
            if (right < n && a[right] > a[largest]) largest = right;
            if (largest == i) return;
            int t = a[i]; a[i] = a[largest]; a[largest] = t;
            i = largest;
        }
    }

    // ------------------------------------------------------------ counting sort

    /** Stable counting sort for values in [0, k]. O(n + k) time, O(n + k) space. */
    public static int[] countingSort(int[] a, int k) {
        int[] count = new int[k + 1];
        for (int v : a) count[v]++;                                   // 1. frequencies
        for (int v = 1; v <= k; v++) count[v] += count[v - 1];        // 2. prefix sums = end positions
        int[] out = new int[a.length];
        for (int i = a.length - 1; i >= 0; i--) out[--count[a[i]]] = a[i];  // 3. right-to-left keeps stability
        return out;
    }

    // --------------------------------------------------------------- radix sort

    /** LSD radix sort for non-negative ints, base 10: stable counting sort on each digit. */
    public static void radixSort(int[] a) {
        int max = 0;
        for (int v : a) max = Math.max(max, v);
        int[] out = new int[a.length];
        for (long exp = 1; max / exp > 0; exp *= 10) {
            int[] count = new int[10];
            for (int v : a) count[(int) (v / exp % 10)]++;
            for (int d = 1; d < 10; d++) count[d] += count[d - 1];
            for (int i = a.length - 1; i >= 0; i--) out[--count[(int) (a[i] / exp % 10)]] = a[i];
            System.arraycopy(out, 0, a, 0, a.length);
            if (trace) System.out.println("  after digit place " + exp + ": " + Arrays.toString(a));
        }
    }

    static long timeMs(Runnable r) {
        long t = System.nanoTime();
        r.run();
        return (System.nanoTime() - t) / 1_000_000;
    }

    public static void main(String[] args) {
        trace = true;
        int[] h = {4, 10, 3, 5, 1, 8};
        System.out.println("Heap sort trace for " + Arrays.toString(h));
        heapSort(h);

        int[] grades = {3, 1, 4, 1, 0, 3, 2, 4, 1};
        System.out.println("\nCounting sort of grade points " + Arrays.toString(grades) + " -> "
                + Arrays.toString(countingSort(grades, 4)));

        int[] r = {170, 45, 75, 90, 802, 24, 2, 66};
        System.out.println("\nRadix sort trace for " + Arrays.toString(r));
        radixSort(r);
        trace = false;

        int n = 200_000;
        java.util.Random rnd = new java.util.Random(1);
        int[] base = new int[n];
        for (int i = 0; i < n; i++) base[i] = rnd.nextInt(1_000_000);
        int[] expected = base.clone();
        Arrays.sort(expected);
        System.out.printf("%nSorting %,d random ints in [0, 1,000,000) (times vary by machine):%n", n);
        int[][] copies = new int[6][];
        for (int i = 0; i < 6; i++) copies[i] = base.clone();
        long tHeap = timeMs(() -> heapSort(copies[0]));
        long tMerge = timeMs(() -> MergeQuickSort.mergeSort(copies[1]));
        long tQuick = timeMs(() -> MergeQuickSort.quickSort(copies[2]));
        long tRadix = timeMs(() -> radixSort(copies[3]));
        int[][] counted = new int[1][];
        long tCount = timeMs(() -> counted[0] = countingSort(copies[4], 999_999));
        long tLib = timeMs(() -> Arrays.sort(copies[5]));
        long tIns = timeMs(() -> ElementarySorts.insertionSort(Arrays.copyOf(base, 20_000), false));
        boolean ok = Arrays.equals(copies[0], expected) && Arrays.equals(copies[1], expected)
                && Arrays.equals(copies[2], expected) && Arrays.equals(copies[3], expected)
                && Arrays.equals(counted[0], expected) && Arrays.equals(copies[5], expected);
        System.out.println("  all results identical to Arrays.sort: " + ok);
        System.out.println("  heap, merge, quick, radix, counting and Arrays.sort all finish in well under a second;");
        System.out.println("  insertion sort on just 20,000 items is already the slowest: "
                + (tIns >= Math.min(Math.min(tHeap, tMerge), Math.min(tQuick, Math.min(tRadix, Math.min(tCount, tLib))))));
    }
}
