import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Module 11 self-check: JUnit-free tests for heaps and priority queues.
 *   javac -d out *.java && java -ea -cp out SelfCheck
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class SelfCheck {
    static int passed = 0, failed = 0;

    static void check(boolean ok, String name) {
        if (ok) passed++;
        else { failed++; System.out.println("FAIL: " + name); }
    }

    public static void main(String[] args) {
        Random r = new Random(11);
        BinaryHeap<Integer> h = BinaryHeap.minHeap();
        PriorityQueue<Integer> ref = new PriorityQueue<>();
        boolean ok = true;
        for (int step = 0; step < 20000; step++) {
            if (ref.isEmpty() || r.nextInt(3) > 0) { int x = r.nextInt(5000); h.insert(x); ref.offer(x); }
            else ok &= h.extract().equals(ref.poll());
            ok &= h.size() == ref.size();
        }
        check(ok, "20000 random insert/extract match PriorityQueue");
        check(h.isHeap(), "heap order holds");
        boolean threw = false;
        try { BinaryHeap.<Integer>minHeap().extract(); } catch (java.util.NoSuchElementException e) { threw = true; }
        check(threw, "extract on empty heap throws");

        Integer[] data = new Integer[1000];
        for (int i = 0; i < data.length; i++) data[i] = r.nextInt(100);
        BinaryHeap<Integer> b = BinaryHeap.heapify(data, Comparator.naturalOrder());
        check(b.isHeap() && b.size() == 1000, "heapify builds a valid heap");
        List<Integer> drained = new ArrayList<>();
        while (!b.isEmpty()) drained.add(b.extract());
        List<Integer> sorted = new ArrayList<>(Arrays.asList(data));
        Collections.sort(sorted);
        check(drained.equals(sorted), "heapify + extract sorts");

        check(PriorityQueueApps.topK(new int[]{5, 1, 9, 3, 7}, 2).equals(List.of(9, 7)), "topK");
        check(PriorityQueueApps.mergeK(List.of(List.of(1, 5), List.<Integer>of(), List.of(2, 3))).equals(List.of(1, 2, 3, 5)), "mergeK with empty list");
        PriorityQueueApps.MedianFinder mf = new PriorityQueueApps.MedianFinder();
        List<Integer> seen = new ArrayList<>();
        boolean medOk = true;
        for (int i = 0; i < 300; i++) {
            int x = r.nextInt(1000) - 500;
            mf.add(x); seen.add(x);
            List<Integer> s = new ArrayList<>(seen); Collections.sort(s);
            int n = s.size();
            double m = n % 2 == 1 ? s.get(n / 2) : (s.get(n / 2 - 1) + s.get(n / 2)) / 2.0;
            medOk &= Math.abs(m - mf.median()) < 1e-9;
        }
        check(medOk, "running median matches sorting");

        check(Labs11.isMinHeap(new int[]{}) && !Labs11.isMinHeap(new int[]{2, 1}), "isMinHeap");
        check(Labs11.kthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4) == 4, "kthLargest");
        check(Labs11.lastStone(new int[]{1}) == 1 && Labs11.lastStone(new int[]{2, 2}) == 0, "lastStone");
        for (int t = 0; t < 30; t++) {
            int[] a = new int[r.nextInt(50)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(40);
            int[] e = a.clone(); Arrays.sort(e);
            Labs11.heapSort(a);
            check(Arrays.equals(a, e), "heapSort random " + t);
        }
        check(Labs11.topKFrequent(new String[]{"b", "a", "b", "a", "c"}, 2).equals(List.of("a", "b")), "topKFrequent tie order");
        check(Labs11.minRooms(new int[][]{{1, 5}, {5, 6}}) == 1 && Labs11.minRooms(new int[][]{{1, 5}, {2, 6}, {3, 7}}) == 3, "minRooms");
        check(Labs11.connectRopes(new int[]{1, 2, 3, 4, 5}) == 33, "connectRopes");

        System.out.println("Module 11 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
