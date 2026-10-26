import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Module 11 — reference solutions for the lab tasks of Sessions 1-2.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out Labs11.java
 * Run:      java -cp out Labs11 1    (Session 1 labs; use 2 for Session 2, no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs11 {

    // >>> isMinHeap
    /** Is the array a valid min-heap? Every element must be >= its parent. */
    static boolean isMinHeap(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i] < a[(i - 1) / 2]) return false;
        return true;
    }
    // <<< isMinHeap

    // >>> kthLargest
    /** k-th largest element in O(n log k): keep the k largest seen so far in a min-heap. */
    static int kthLargest(int[] a, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : a) {
            pq.offer(x);
            if (pq.size() > k) pq.poll();
        }
        return pq.peek();
    }
    // <<< kthLargest

    // >>> lastStone
    /** Smash the two heaviest stones repeatedly; return the last stone's weight (0 if none). Max-heap. */
    static int lastStone(int[] stones) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for (int s : stones) pq.offer(s);
        while (pq.size() > 1) {
            int y = pq.poll(), x = pq.poll();
            if (y != x) pq.offer(y - x);
        }
        return pq.isEmpty() ? 0 : pq.peek();
    }
    // <<< lastStone

    // >>> heapSortInPlace
    /** In-place heap sort (ascending) with a max-heap inside the array itself. O(n log n), O(1) space. */
    static void heapSort(int[] a) {
        int n = a.length;
        for (int i = n / 2 - 1; i >= 0; i--) sink(a, i, n);       // build max-heap bottom-up
        for (int end = n - 1; end > 0; end--) {
            int t = a[0]; a[0] = a[end]; a[end] = t;               // largest goes to its final place
            sink(a, 0, end);
        }
    }

    private static void sink(int[] a, int i, int n) {
        while (2 * i + 1 < n) {
            int c = 2 * i + 1;
            if (c + 1 < n && a[c + 1] > a[c]) c++;
            if (a[i] >= a[c]) break;
            int t = a[i]; a[i] = a[c]; a[c] = t;
            i = c;
        }
    }
    // <<< heapSortInPlace

    // >>> topKFrequent
    /** The k most frequent words (ties: alphabetical). Count with a map, select with a size-k heap. */
    static List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> count = new HashMap<>();
        for (String w : words) count.merge(w, 1, Integer::sum);
        Comparator<String> worseFirst = Comparator.<String>comparingInt(count::get)
                .thenComparing(Comparator.<String>reverseOrder());
        PriorityQueue<String> pq = new PriorityQueue<>(worseFirst);
        for (String w : count.keySet()) {
            pq.offer(w);
            if (pq.size() > k) pq.poll();
        }
        String[] out = new String[pq.size()];
        for (int i = out.length - 1; i >= 0; i--) out[i] = pq.poll();
        return Arrays.asList(out);
    }
    // <<< topKFrequent

    // >>> minRooms
    /** Minimum lab rooms for lectures [start, end): sort by start, min-heap of end times of busy rooms. */
    static int minRooms(int[][] lectures) {
        int[][] s = lectures.clone();
        Arrays.sort(s, Comparator.comparingInt((int[] x) -> x[0]));
        PriorityQueue<Integer> ends = new PriorityQueue<>();
        for (int[] l : s) {
            if (!ends.isEmpty() && ends.peek() <= l[0]) ends.poll();    // reuse the room that frees first
            ends.offer(l[1]);
        }
        return ends.size();
    }
    // <<< minRooms

    // >>> connectRopes
    /** Join ropes; joining lengths a and b costs a + b. Always join the two shortest (greedy, Module 15). */
    static long connectRopes(int[] ropes) {
        PriorityQueue<Long> pq = new PriorityQueue<>();
        for (int r : ropes) pq.offer((long) r);
        long cost = 0;
        while (pq.size() > 1) {
            long joined = pq.poll() + pq.poll();
            cost += joined;
            pq.offer(joined);
        }
        return cost;
    }
    // <<< connectRopes

    public static void main(String[] args) {
        int session = args.length > 0 ? Integer.parseInt(args[0]) : 0;
        if (session == 0 || session == 1) {
            System.out.println("-- Session 1 labs --");
            System.out.println("isMinHeap([1, 3, 2, 7, 4]) = " + isMinHeap(new int[]{1, 3, 2, 7, 4})
                    + ", isMinHeap([1, 3, 2, 0]) = " + isMinHeap(new int[]{1, 3, 2, 0}));
            System.out.println("kthLargest([3, 2, 1, 5, 6, 4], k=2) = " + kthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
            System.out.println("lastStone([2, 7, 4, 1, 8, 1]) = " + lastStone(new int[]{2, 7, 4, 1, 8, 1}));
            int[] a = {9, 4, 7, 1, 8, 2, 6, 3, 5};
            heapSort(a);
            System.out.println("heapSort([9, 4, 7, 1, 8, 2, 6, 3, 5]) = " + Arrays.toString(a));
        }
        if (session == 0 || session == 2) {
            System.out.println("-- Session 2 labs --");
            String[] w = {"dsa", "ai", "dsa", "oop", "ai", "dsa", "db", "oop", "ai"};
            System.out.println("topKFrequent(k=2) = " + topKFrequent(w, 2));
            System.out.println("minRooms([[9,11],[10,12],[11,13],[9,10],[12,14]]) = "
                    + minRooms(new int[][]{{9, 11}, {10, 12}, {11, 13}, {9, 10}, {12, 14}}));
            System.out.println("connectRopes([4, 3, 2, 6]) = " + connectRopes(new int[]{4, 3, 2, 6}));
        }
    }
}
