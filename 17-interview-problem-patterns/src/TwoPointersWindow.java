import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Session 1 demo: the two-pointer and sliding-window patterns. Opposite-end pointers (pair sum,
 * container with most water, 3-sum), same-direction pointers (in-place deduplication), fixed windows
 * (best k-day total) and variable windows (longest unique substring, shortest window with sum >= target,
 * longest window with at most k distinct values), each compared with its brute force.
 *
 * Compile:  javac -d out TwoPointersWindow.java
 * Run:      java -cp out TwoPointersWindow
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class TwoPointersWindow {
    static long steps;

    /** Sorted array: indices of two values summing to target. Move the pointer that fixes the sum. */
    static int[] pairSum(int[] a, int target) {
        int i = 0, j = a.length - 1;
        while (i < j) {
            steps++;
            int s = a[i] + a[j];
            if (s == target) return new int[]{i, j};
            if (s < target) i++; else j--;          // too small: need a bigger left value
        }
        return null;
    }

    /** Remove duplicates from a sorted array in place; returns the new length. Slow writer, fast reader. */
    static int dedupe(int[] a) {
        if (a.length == 0) return 0;
        int w = 1;
        for (int r = 1; r < a.length; r++) if (a[r] != a[w - 1]) a[w++] = a[r];
        return w;
    }

    /** Max water between two lines: always move the shorter line inward. O(n). */
    static int maxWater(int[] h) {
        int i = 0, j = h.length - 1, best = 0;
        while (i < j) {
            best = Math.max(best, Math.min(h[i], h[j]) * (j - i));
            if (h[i] < h[j]) i++; else j--;
        }
        return best;
    }

    /** All unique triples summing to 0: sort, fix one value, two-pointer the rest. O(n^2). */
    static List<List<Integer>> threeSum(int[] in) {
        int[] a = in.clone();
        Arrays.sort(a);
        List<List<Integer>> out = new ArrayList<>();
        for (int k = 0; k < a.length - 2; k++) {
            if (k > 0 && a[k] == a[k - 1]) continue;           // skip duplicate anchors
            int i = k + 1, j = a.length - 1;
            while (i < j) {
                int s = a[k] + a[i] + a[j];
                if (s == 0) {
                    out.add(List.of(a[k], a[i], a[j]));
                    while (i < j && a[i] == a[i + 1]) i++;
                    while (i < j && a[j] == a[j - 1]) j--;
                    i++; j--;
                } else if (s < 0) i++; else j--;
            }
        }
        return out;
    }

    /** Fixed window: best total of k consecutive days. Slide: add the new day, drop the old one. */
    static int bestWindow(int[] a, int k) {
        int sum = 0;
        for (int i = 0; i < k; i++) sum += a[i];
        int best = sum;
        for (int i = k; i < a.length; i++) {
            sum += a[i] - a[i - k];
            best = Math.max(best, sum);
        }
        return best;
    }

    /** Variable window: longest substring with no repeated character. */
    static String longestUnique(String s) {
        Map<Character, Integer> last = new HashMap<>();
        int left = 0, bestL = 0, bestLen = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (last.containsKey(c) && last.get(c) >= left) left = last.get(c) + 1;   // jump past the repeat
            last.put(c, right);
            if (right - left + 1 > bestLen) { bestLen = right - left + 1; bestL = left; }
        }
        return s.substring(bestL, bestL + bestLen);
    }

    /** Variable window: shortest subarray (positive values) with sum >= target; 0 if none. */
    static int shortestAtLeast(int[] a, int target) {
        int left = 0, sum = 0, best = Integer.MAX_VALUE;
        for (int right = 0; right < a.length; right++) {
            sum += a[right];
            while (sum >= target && left <= right) {         // shrink while still valid
                best = Math.min(best, right - left + 1);
                sum -= a[left++];
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }

    /** Variable window: longest stretch with at most k distinct values (e.g. fruit baskets). */
    static int longestAtMostK(int[] a, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        int left = 0, best = 0;
        for (int right = 0; right < a.length; right++) {
            count.merge(a[right], 1, Integer::sum);
            while (count.size() > k) {
                if (count.merge(a[left], -1, Integer::sum) == 0) count.remove(a[left]);
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println("== Opposite-end pointers ==");
        int[] prices = {120, 250, 310, 400, 480, 520, 650, 800};
        steps = 0;
        System.out.println("two books costing exactly 900 from " + Arrays.toString(prices) + " -> indices "
                + Arrays.toString(pairSum(prices, 900)) + " in " + steps + " steps (brute force: up to 28 pairs)");
        System.out.println("maxWater([1, 8, 6, 2, 5, 4, 8, 3, 7]) = " + maxWater(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
        System.out.println("threeSum([-1, 0, 1, 2, -1, -4]) = " + threeSum(new int[]{-1, 0, 1, 2, -1, -4}));

        System.out.println("\n== Same-direction pointers ==");
        int[] rolls = {101, 101, 102, 105, 105, 105, 108};
        int n = dedupe(rolls);
        System.out.println("dedupe -> length " + n + ", prefix " + Arrays.toString(Arrays.copyOf(rolls, n)));

        System.out.println("\n== Fixed window ==");
        int[] visitors = {40, 55, 30, 70, 65, 20, 90, 85};
        System.out.println("best 3-day visitor total in " + Arrays.toString(visitors) + " = " + bestWindow(visitors, 3));

        System.out.println("\n== Variable windows ==");
        for (String s : new String[]{"abcabcbb", "pwwkew", "datastructures"})
            System.out.println("longestUnique(\"" + s + "\") = \"" + longestUnique(s) + "\"");
        System.out.println("shortestAtLeast([2, 3, 1, 2, 4, 3], 7) = " + shortestAtLeast(new int[]{2, 3, 1, 2, 4, 3}, 7));
        System.out.println("longestAtMostK([1, 2, 1, 2, 3, 3, 3, 2], k=2) = " + longestAtMostK(new int[]{1, 2, 1, 2, 3, 3, 3, 2}, 2));

        System.out.println("\n== Brute force vs window: longest unique substring of 200,000 chars ==");
        StringBuilder sb = new StringBuilder();
        java.util.Random r = new java.util.Random(17);
        for (int i = 0; i < 200_000; i++) sb.append((char) ('a' + r.nextInt(26)));
        long t0 = System.nanoTime();
        int len = longestUnique(sb.toString()).length();
        long ms = (System.nanoTime() - t0) / 1_000_000;
        System.out.println("window answer length = " + len + " (one pass, " + (ms < 1000 ? "well under a second" : ms + " ms")
                + "); a check of all ~2*10^10 substrings would be infeasible");
    }
}
