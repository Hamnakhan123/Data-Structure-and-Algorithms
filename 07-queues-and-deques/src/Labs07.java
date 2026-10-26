import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

/**
 * Module 07 — reference solutions for the lab tasks of Sessions 1-2.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out Labs07.java
 * Run:      java -cp out Labs07 1    (Session 1 labs; other numbers for other sessions; no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs07 {

    // >>> reverseQueue
    /** Reverse a queue using a stack: dequeue everything onto the stack, then pop back. */
    static <T> void reverse(Queue<T> q) {
        Deque<T> st = new ArrayDeque<>();
        while (!q.isEmpty()) st.push(q.poll());
        while (!st.isEmpty()) q.offer(st.pop());
    }
    // <<< reverseQueue

    // >>> binaryNumbers
    /** First n binary numbers using a queue: each number s generates s+"0" and s+"1". */
    static List<String> binaryNumbers(int n) {
        List<String> out = new ArrayList<>();
        Queue<String> q = new ArrayDeque<>();
        q.offer("1");
        while (out.size() < n) {
            String s = q.poll();
            out.add(s);
            q.offer(s + "0");
            q.offer(s + "1");
        }
        return out;
    }
    // <<< binaryNumbers

    // >>> hotPotato
    /** Hot potato: pass the potato 'passes' times (front to back), then the holder is out. */
    static String hotPotato(String[] names, int passes) {
        Queue<String> q = new ArrayDeque<>(List.of(names));
        while (q.size() > 1) {
            for (int i = 0; i < passes; i++) q.offer(q.poll());
            q.poll();
        }
        return q.peek();
    }
    // <<< hotPotato

    // >>> recentCalls
    /** Count requests in the last 3000 ms: old timestamps fall off the front of the queue. */
    static final class RecentCounter {
        private final Queue<Integer> times = new ArrayDeque<>();
        int ping(int t) {
            times.offer(t);
            while (times.peek() < t - 3000) times.poll();
            return times.size();
        }
    }
    // <<< recentCalls

    // >>> firstNegative
    /** First negative number in every window of size k (0 if none), using a queue of indices. */
    static int[] firstNegative(int[] a, int k) {
        int[] out = new int[a.length - k + 1];
        Deque<Integer> neg = new ArrayDeque<>();
        for (int i = 0; i < a.length; i++) {
            if (a[i] < 0) neg.offerLast(i);
            if (!neg.isEmpty() && neg.peekFirst() <= i - k) neg.pollFirst();
            if (i >= k - 1) out[i - k + 1] = neg.isEmpty() ? 0 : a[neg.peekFirst()];
        }
        return out;
    }
    // <<< firstNegative

    public static void main(String[] args) {
        int session = args.length > 0 ? Integer.parseInt(args[0]) : 0;   // 0 = all sessions
        if (session == 0 || session == 1) {
            System.out.println("-- Session 1 labs --");
            Queue<Integer> q = new ArrayDeque<>(List.of(1, 2, 3, 4, 5));
            reverse(q);
            System.out.println("reversed queue = " + q);
            System.out.println("first 10 binary numbers = " + binaryNumbers(10));
            System.out.println("hot potato survivor = " + hotPotato(new String[]{"Ali", "Bina", "Chand", "Dua", "Emaan", "Faiz"}, 7));
            RecentCounter rc = new RecentCounter();
            System.out.println("recent calls at t=1,100,3001,3002 -> " + rc.ping(1) + " " + rc.ping(100) + " " + rc.ping(3001) + " " + rc.ping(3002));
        }
        if (session == 0 || session == 2) {
            System.out.println("-- Session 2 labs --");
            System.out.println("first negative per window (k=3) of [-8, 2, 3, -6, 10] = "
                    + java.util.Arrays.toString(firstNegative(new int[]{-8, 2, 3, -6, 10}, 3)));
        }
    }
}
