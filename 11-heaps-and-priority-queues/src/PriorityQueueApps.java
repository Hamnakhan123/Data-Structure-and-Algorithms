import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Session 2 demo: java.util.PriorityQueue with comparators, top-k selection, merging k sorted
 * lists, a running median with two heaps, and a priority-based job scheduler simulation.
 *
 * Compile:  javac -d out PriorityQueueApps.java
 * Run:      java -cp out PriorityQueueApps
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class PriorityQueueApps {

    /** k largest values using a MIN-heap of size k: the root is the weakest of the current top k. */
    static List<Integer> topK(int[] a, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : a) {
            pq.offer(x);
            if (pq.size() > k) pq.poll();               // throw away the smallest
        }
        List<Integer> out = new ArrayList<>(pq);
        out.sort(Comparator.reverseOrder());
        return out;
    }

    /** Merge k sorted lists: the heap holds one "cursor" per list, ordered by its current value. */
    static List<Integer> mergeK(List<List<Integer>> lists) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt((int[] c) -> lists.get(c[0]).get(c[1])));
        for (int i = 0; i < lists.size(); i++) if (!lists.get(i).isEmpty()) pq.offer(new int[]{i, 0});
        List<Integer> out = new ArrayList<>();
        while (!pq.isEmpty()) {
            int[] c = pq.poll();                        // c = {list index, position}
            out.add(lists.get(c[0]).get(c[1]));
            if (c[1] + 1 < lists.get(c[0]).size()) pq.offer(new int[]{c[0], c[1] + 1});
        }
        return out;
    }

    /** Running median: max-heap for the lower half, min-heap for the upper half. */
    static final class MedianFinder {
        private final PriorityQueue<Integer> low = new PriorityQueue<>(Comparator.reverseOrder());
        private final PriorityQueue<Integer> high = new PriorityQueue<>();

        void add(int x) {
            low.offer(x);
            high.offer(low.poll());                     // largest of the low half moves up
            if (high.size() > low.size()) low.offer(high.poll());   // keep |low| = |high| or |high| + 1
        }

        double median() {
            return low.size() > high.size() ? low.peek() : (low.peek() + high.peek()) / 2.0;
        }
    }

    /** A print/compute job: lower priority number = more urgent; ties broken by arrival order. */
    record Job(String name, int priority, int arrival, int duration) { }

    static List<String> schedule(List<Job> jobs) {
        List<Job> byArrival = new ArrayList<>(jobs);
        byArrival.sort(Comparator.comparingInt(Job::arrival));
        PriorityQueue<Job> ready = new PriorityQueue<>(
                Comparator.comparingInt(Job::priority).thenComparingInt(Job::arrival));
        List<String> log = new ArrayList<>();
        int time = 0, next = 0;
        while (next < byArrival.size() || !ready.isEmpty()) {
            while (next < byArrival.size() && byArrival.get(next).arrival() <= time) ready.offer(byArrival.get(next++));
            if (ready.isEmpty()) { time = byArrival.get(next).arrival(); continue; }   // CPU idle
            Job j = ready.poll();
            log.add(String.format("t=%2d..%2d  %-8s (priority %d, arrived %d, waited %d)",
                    time, time + j.duration(), j.name(), j.priority(), j.arrival(), time - j.arrival()));
            time += j.duration();
        }
        return log;
    }

    public static void main(String[] args) {
        System.out.println("== PriorityQueue basics ==");
        PriorityQueue<Integer> pq = new PriorityQueue<>(List.of(42, 7, 19, 3, 25));
        System.out.println("internal array order (NOT sorted): " + pq);
        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) sb.append(pq.poll()).append(' ');
        System.out.println("poll order (sorted): " + sb.toString().trim());

        System.out.println("\n== Top-3 marks with a size-3 min-heap ==");
        System.out.println(topK(new int[]{67, 88, 45, 92, 71, 88, 59, 95}, 3));

        System.out.println("\n== Merge 3 sorted lists ==");
        System.out.println(mergeK(List.of(List.of(1, 4, 9), List.of(2, 3, 10, 11), List.of(5, 6))));

        System.out.println("\n== Running median ==");
        MedianFinder mf = new MedianFinder();
        sb = new StringBuilder();
        for (int x : new int[]{5, 15, 1, 3, 8, 7, 9, 10}) {
            mf.add(x);
            sb.append(x).append("->").append(mf.median()).append("  ");
        }
        System.out.println(sb.toString().trim());

        System.out.println("\n== Priority job scheduler (non-preemptive) ==");
        List<Job> jobs = List.of(
                new Job("backup", 3, 0, 4), new Job("grades", 1, 1, 3), new Job("email", 2, 2, 1),
                new Job("report", 1, 3, 2), new Job("logs", 3, 12, 2));
        for (String line : schedule(jobs)) System.out.println(line);
    }
}
