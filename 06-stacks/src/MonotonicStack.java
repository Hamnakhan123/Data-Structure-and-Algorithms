import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Module 06 · Session 03 — Monotonic stacks.
 *
 * A stack whose contents stay sorted lets each element be pushed and popped at
 * most once, turning many O(n^2) "look for the next bigger/smaller" problems into
 * O(n): next greater element, daily temperatures, stock span, largest rectangle
 * in a histogram and the min-stack design.
 *
 * Compile:  javac -d out MonotonicStack.java
 * Run:      java -cp out MonotonicStack
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class MonotonicStack {

    /** For each element, the first larger element to its right (-1 if none). O(n). */
    static int[] nextGreater(int[] a, boolean trace) {
        int[] res = new int[a.length];
        Arrays.fill(res, -1);
        Deque<Integer> st = new ArrayDeque<>();      // indices whose values are DECREASING from bottom to top
        for (int i = 0; i < a.length; i++) {
            while (!st.isEmpty() && a[st.peek()] < a[i]) res[st.pop()] = a[i];   // a[i] answers them
            st.push(i);
            if (trace) {
                StringBuilder sb = new StringBuilder();
                for (int idx : st) sb.insert(0, a[idx] + " ");
                System.out.printf("  i=%d a[i]=%2d stack(bottom->top)=[%s] res=%s%n", i, a[i], sb.toString().trim(), Arrays.toString(res));
            }
        }
        return res;
    }

    /** Days until a warmer temperature (0 if never). */
    static int[] dailyTemperatures(int[] t) {
        int[] wait = new int[t.length];
        Deque<Integer> st = new ArrayDeque<>();
        for (int i = 0; i < t.length; i++) {
            while (!st.isEmpty() && t[st.peek()] < t[i]) {
                int j = st.pop();
                wait[j] = i - j;
            }
            st.push(i);
        }
        return wait;
    }

    /** Stock span: number of consecutive days up to today with price <= today's price. */
    static int[] stockSpan(int[] price) {
        int[] span = new int[price.length];
        Deque<Integer> st = new ArrayDeque<>();      // indices of prices that are still "blocking"
        for (int i = 0; i < price.length; i++) {
            while (!st.isEmpty() && price[st.peek()] <= price[i]) st.pop();
            span[i] = st.isEmpty() ? i + 1 : i - st.peek();
            st.push(i);
        }
        return span;
    }

    /** Largest rectangle in a histogram: each bar is popped when a shorter bar ends its extent. O(n). */
    static long largestRectangle(int[] h) {
        Deque<Integer> st = new ArrayDeque<>();      // indices with INCREASING heights
        long best = 0;
        for (int i = 0; i <= h.length; i++) {
            int cur = (i == h.length) ? 0 : h[i];    // sentinel bar of height 0 flushes the stack
            while (!st.isEmpty() && h[st.peek()] >= cur) {
                int height = h[st.pop()];
                int left = st.isEmpty() ? -1 : st.peek();   // first shorter bar on the left
                best = Math.max(best, (long) height * (i - left - 1));
            }
            st.push(i);
        }
        return best;
    }

    /** Stack with O(1) getMin: a second stack stores the minimum at each depth. */
    static final class MinStack {
        private final Deque<Integer> values = new ArrayDeque<>();
        private final Deque<Integer> mins = new ArrayDeque<>();

        void push(int x) {
            values.push(x);
            mins.push(mins.isEmpty() ? x : Math.min(x, mins.peek()));
        }
        int pop() { mins.pop(); return values.pop(); }
        int getMin() { return mins.peek(); }
    }

    /** Brute force next-greater for comparison: O(n^2). */
    static int[] nextGreaterBrute(int[] a) {
        int[] res = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = -1;
            for (int j = i + 1; j < a.length; j++) if (a[j] > a[i]) { res[i] = a[j]; break; }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] a = {4, 5, 2, 25, 7, 8};
        System.out.println("Next greater element of " + Arrays.toString(a) + ":");
        System.out.println("result " + Arrays.toString(nextGreater(a, true)));

        int[] temps = {30, 28, 32, 31, 29, 35, 33};
        System.out.println("\ndaily temperatures " + Arrays.toString(temps) + " -> wait " + Arrays.toString(dailyTemperatures(temps)));
        int[] prices = {100, 80, 60, 70, 60, 75, 85};
        System.out.println("stock prices " + Arrays.toString(prices) + " -> span " + Arrays.toString(stockSpan(prices)));
        int[] bars = {2, 1, 5, 6, 2, 3};
        System.out.println("largest rectangle in histogram " + Arrays.toString(bars) + " = " + largestRectangle(bars));

        MinStack ms = new MinStack();
        for (int x : new int[]{5, 3, 7, 2, 8}) ms.push(x);
        System.out.print("\nMinStack after pushing 5 3 7 2 8: min=" + ms.getMin());
        ms.pop(); ms.pop();
        System.out.print(" | pop 8, 2 -> min=" + ms.getMin());
        ms.pop(); ms.pop();
        System.out.println(" | pop 7, 3 -> min=" + ms.getMin());

        int n = 200_000;
        int[] big = new int[n];
        java.util.Random r = new java.util.Random(9);
        for (int i = 0; i < n; i++) big[i] = n - i + r.nextInt(3);   // mostly decreasing: brute force's bad case
        long t0 = System.nanoTime();
        int[] fast = nextGreater(big, false);
        long t1 = System.nanoTime();
        int[] slow = nextGreaterBrute(Arrays.copyOf(big, 20_000));
        long t2 = System.nanoTime();
        int[] small = Arrays.copyOf(big, 20_000);
        System.out.printf("%nstack and brute force agree on the same 20,000 values: %b%n",
                Arrays.equals(nextGreater(small, false), slow));
        System.out.println("stack result computed for all " + String.format("%,d", fast.length) + " values");
        System.out.println("stack version faster despite 10x more data: " + ((t1 - t0) < (t2 - t1)));
    }
}
