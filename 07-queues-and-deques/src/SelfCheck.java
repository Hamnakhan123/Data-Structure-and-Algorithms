import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Module 07 self-check: JUnit-free tests for queues and deques.
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
        // random operations compared against java.util.ArrayDeque
        Random r = new Random(77);
        QueueImplementations.CircularQueue<Integer> cq = new QueueImplementations.CircularQueue<>(2);
        QueueImplementations.LinkedQueue<Integer> lq = new QueueImplementations.LinkedQueue<>();
        DequeApplications.CircularDeque<Integer> cd = new DequeApplications.CircularDeque<>();
        java.util.ArrayDeque<Integer> refQ = new java.util.ArrayDeque<>(), refD = new java.util.ArrayDeque<>();
        boolean ok = true;
        for (int step = 0; step < 5000; step++) {
            int op = r.nextInt(4), v = r.nextInt(1000);
            if (op < 2 || refQ.isEmpty()) { cq.enqueue(v); lq.enqueue(v); refQ.offer(v); }
            else { int e = refQ.poll(); ok &= cq.dequeue() == e && lq.dequeue() == e; }
            switch (r.nextInt(4)) {
                case 0: cd.addFirst(v); refD.addFirst(v); break;
                case 1: cd.addLast(v); refD.addLast(v); break;
                case 2: if (!refD.isEmpty()) ok &= cd.removeFirst().equals(refD.pollFirst()); break;
                default: if (!refD.isEmpty()) ok &= cd.removeLast().equals(refD.pollLast());
            }
            ok &= cq.size() == refQ.size() && lq.size() == refQ.size() && cd.size() == refD.size();
        }
        check(ok, "5000 random queue/deque operations match ArrayDeque");
        boolean threw = false;
        try { new QueueImplementations.LinkedQueue<String>().dequeue(); } catch (java.util.NoSuchElementException e) { threw = true; }
        check(threw, "underflow throws");
        check(Math.abs(QueueImplementations.averageWait(new int[]{0, 0}, new int[]{5, 5}) - 2.5) < 1e-9, "bank wait");

        for (int t = 0; t < 40; t++) {
            int n = 1 + r.nextInt(40), k = 1 + r.nextInt(n);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = r.nextInt(50) - 25;
            check(Arrays.equals(DequeApplications.slidingWindowMax(a, k, false), DequeApplications.slidingWindowMaxBrute(a, k)), "window max " + t);
        }
        check(DequeApplications.isPalindrome("A Santa at NASA") && !DequeApplications.isPalindrome("ab"), "palindrome");
        check(DequeApplications.shortestPath(new String[]{"S.E"}) == 2, "bfs simple");
        check(DequeApplications.shortestPath(new String[]{"S#E"}) == -1, "bfs unreachable");
        check(DequeApplications.zeroOneBfs(new String[]{".~", "~."}) == 1, "0-1 bfs");

        check(Labs07.binaryNumbers(5).equals(List.of("1", "10", "11", "100", "101")), "binary numbers");
        check(Labs07.hotPotato(new String[]{"a", "b"}, 1).equals("a"), "hot potato");
        check(Arrays.equals(Labs07.firstNegative(new int[]{1, 2}, 1), new int[]{0, 0}), "first negative none");

        System.out.println("Module 07 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
