import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Session 2 demo: more interview patterns, each in a few lines. Fast/slow pointers (Floyd's cycle
 * detection on an array), binary search on the answer (minimum truck capacity), prefix sum + hash map,
 * monotonic stack (next warmer day), cyclic sort (missing and duplicate roll numbers) and bit tricks
 * (the unpaired value, subsets as bitmasks).
 *
 * Compile:  javac -d out MorePatterns.java
 * Run:      java -cp out MorePatterns
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class MorePatterns {

    /** a has n+1 values in 1..n, exactly one repeated (maybe many times). Treat i -> a[i] as a linked list. */
    static int findDuplicate(int[] a) {
        int slow = a[0], fast = a[a[0]];
        while (slow != fast) { slow = a[slow]; fast = a[a[fast]]; }   // phase 1: meet inside the cycle
        slow = 0;
        while (slow != fast) { slow = a[slow]; fast = a[fast]; }       // phase 2: meet at the cycle entrance
        return slow;
    }

    /** Smallest truck capacity that ships all parcels (in order) within d days. Binary search on capacity. */
    static int shipWithinDays(int[] w, int d) {
        int lo = Arrays.stream(w).max().orElse(0), hi = Arrays.stream(w).sum();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (daysNeeded(w, mid) <= d) hi = mid;      // feasible: try smaller
            else lo = mid + 1;                          // infeasible: need more capacity
        }
        return lo;
    }

    static int daysNeeded(int[] w, int cap) {
        int days = 1, load = 0;
        for (int x : w) {
            if (load + x > cap) { days++; load = 0; }
            load += x;
        }
        return days;
    }

    /** Longest subarray with sum exactly k (negatives allowed): first index of each prefix sum. */
    static int longestSumK(int[] a, int k) {
        Map<Integer, Integer> first = new HashMap<>();
        first.put(0, -1);
        int sum = 0, best = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
            Integer j = first.get(sum - k);
            if (j != null) best = Math.max(best, i - j);
            first.putIfAbsent(sum, i);
        }
        return best;
    }

    /** Days to wait for a warmer temperature: monotonic decreasing stack of indices. */
    static int[] dailyTemperatures(int[] t) {
        int[] wait = new int[t.length];
        Deque<Integer> st = new ArrayDeque<>();
        for (int i = 0; i < t.length; i++) {
            while (!st.isEmpty() && t[st.peek()] < t[i]) { int j = st.pop(); wait[j] = i - j; }
            st.push(i);
        }
        return wait;
    }

    /** Values 1..n with one missing and one duplicated: cyclic sort puts value v at index v-1. O(n), O(1) space. */
    static int[] missingAndDuplicate(int[] in) {
        int[] a = in.clone();
        int i = 0;
        while (i < a.length) {
            int target = a[i] - 1;
            if (a[i] != a[target]) { int t = a[i]; a[i] = a[target]; a[target] = t; }   // send a[i] home
            else i++;
        }
        for (int k = 0; k < a.length; k++) if (a[k] != k + 1) return new int[]{k + 1, a[k]};
        return new int[]{-1, -1};
    }

    /** Every value appears twice except one: XOR cancels pairs (x ^ x = 0). */
    static int unpaired(int[] a) {
        int x = 0;
        for (int v : a) x ^= v;
        return x;
    }

    public static void main(String[] args) {
        System.out.println("== Fast/slow pointers ==");
        int[] dup = {3, 1, 3, 4, 2};
        System.out.println("findDuplicate(" + Arrays.toString(dup) + ") = " + findDuplicate(dup) + "  (O(1) extra space, array untouched)");

        System.out.println("\n== Binary search on the answer ==");
        int[] parcels = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int cap = shipWithinDays(parcels, 5);
        System.out.println("parcels " + Arrays.toString(parcels) + " in 5 days -> min capacity " + cap
                + " (days needed at " + cap + " = " + daysNeeded(parcels, cap) + ", at " + (cap - 1) + " = " + daysNeeded(parcels, cap - 1) + ")");

        System.out.println("\n== Prefix sum + hash map ==");
        int[] balance = {3, -2, 1, 4, -3, 2, -1};
        System.out.println("longest stretch of " + Arrays.toString(balance) + " with net change 3 = " + longestSumK(balance, 3) + " days");

        System.out.println("\n== Monotonic stack ==");
        int[] temps = {30, 28, 33, 31, 29, 35, 34, 36};
        System.out.println("temps " + Arrays.toString(temps) + " -> wait " + Arrays.toString(dailyTemperatures(temps)));

        System.out.println("\n== Cyclic sort ==");
        int[] rolls = {4, 3, 6, 2, 1, 1};
        int[] md = missingAndDuplicate(rolls);
        System.out.println("roll numbers " + Arrays.toString(rolls) + " -> missing " + md[0] + ", duplicated " + md[1]);

        System.out.println("\n== Bit manipulation ==");
        System.out.println("unpaired([7, 3, 5, 3, 7]) = " + unpaired(new int[]{7, 3, 5, 3, 7}));
        String[] items = {"pen", "ruler", "eraser"};
        StringBuilder sb = new StringBuilder();
        for (int mask = 0; mask < 1 << items.length; mask++) {
            StringBuilder set = new StringBuilder("{");
            for (int b = 0; b < items.length; b++) if ((mask >> b & 1) == 1) set.append(set.length() > 1 ? "," : "").append(items[b]);
            sb.append(Integer.toBinaryString(mask | 8).substring(1)).append('=').append(set).append("} ");
        }
        System.out.println("subsets as bitmasks: " + sb.toString().trim());
        System.out.println("isPowerOfTwo(64) = " + ((64 & 63) == 0) + ", bitCount(13) = " + Integer.bitCount(13) + ", lowest set bit of 12 = " + (12 & -12));
    }
}
