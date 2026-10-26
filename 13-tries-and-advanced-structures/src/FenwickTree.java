import java.util.Arrays;

/**
 * Session 3 demo: the Fenwick tree (binary indexed tree). Point update and prefix sum in O(log n)
 * using the lowest set bit, O(n) construction, range sums, the "range update / point query"
 * variant, finding the k-th element by prefix count, and counting inversions.
 *
 * Compile:  javac -d out FenwickTree.java
 * Run:      java -cp out FenwickTree
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class FenwickTree {
    private final long[] tree;      // 1-based: tree[i] stores the sum of a(i - lowbit(i), i]
    private final int n;

    FenwickTree(int n) { this.n = n; tree = new long[n + 1]; }

    /** O(n) build: each node pushes its total to its parent i + lowbit(i). */
    FenwickTree(long[] a) {
        this(a.length);
        for (int i = 1; i <= n; i++) {
            tree[i] += a[i - 1];
            int parent = i + (i & -i);
            if (parent <= n) tree[parent] += tree[i];
        }
    }

    static int lowbit(int i) { return i & -i; }       // value of the lowest set bit: 12 (1100) -> 4

    /** a[i] += delta (1-based). Climb: i += lowbit(i). */
    void add(int i, long delta) {
        for (; i <= n; i += lowbit(i)) tree[i] += delta;
    }

    /** a[1] + ... + a[i]. Descend: i -= lowbit(i). */
    long prefix(int i) {
        long s = 0;
        for (; i > 0; i -= lowbit(i)) s += tree[i];
        return s;
    }

    long rangeSum(int l, int r) { return prefix(r) - prefix(l - 1); }

    /** Smallest index i with prefix(i) >= k (all values non-negative). Binary lifting, O(log n). */
    int lowerBound(long k) {
        int pos = 0;
        for (int step = Integer.highestOneBit(n); step > 0; step >>= 1) {
            if (pos + step <= n && tree[pos + step] < k) {
                pos += step;
                k -= tree[pos];
            }
        }
        return pos + 1;
    }

    /** Number of pairs i < j with a[i] > a[j]: scan right to left, count smaller values seen. O(n log M). */
    static long inversions(int[] a) {
        int max = Arrays.stream(a).max().orElse(0);
        FenwickTree seen = new FenwickTree(max);
        long inv = 0;
        for (int i = a.length - 1; i >= 0; i--) {
            inv += seen.prefix(a[i] - 1);          // values smaller than a[i] already to its right
            seen.add(a[i], 1);
        }
        return inv;
    }

    /** Range update, point query: store differences; a[i] = prefix(i) of the difference array. */
    static final class RangeAdd {
        private final FenwickTree diff;
        RangeAdd(int n) { diff = new FenwickTree(n + 1); }
        void add(int l, int r, long v) { diff.add(l, v); diff.add(r + 1, -v); }
        long get(int i) { return diff.prefix(i); }
    }

    static String bits(int i) { return String.format("%5s", Integer.toBinaryString(i)).replace(' ', '0'); }

    public static void main(String[] args) {
        long[] a = {5, 8, 6, 3, 2, 7, 2, 6};                // same canteen data as Session 2
        FenwickTree f = new FenwickTree(a);
        System.out.println("a (1-based)  = " + Arrays.toString(a));
        System.out.println("tree[1..8]   = " + Arrays.toString(Arrays.copyOfRange(f.tree, 1, 9)));
        System.out.println("\n i  binary  lowbit  tree[i] covers a(i-lowbit, i]");
        for (int i = 1; i <= 8; i++)
            System.out.printf("%2d   %s   %4d   %7d   a[%d..%d]%n", i, bits(i), lowbit(i), f.tree[i], i - lowbit(i) + 1, i);

        System.out.println("\nprefix(7) visits 7 -> 6 -> 4: " + f.tree[7] + " + " + f.tree[6] + " + " + f.tree[4] + " = " + f.prefix(7));
        System.out.println("rangeSum(3, 7) = " + f.rangeSum(3, 7));
        f.add(4, 7);                                         // day 4 goes from 3 to 10
        System.out.println("add(4, +7) updates 4 -> 8; rangeSum(3, 7) = " + f.rangeSum(3, 7) + ", total = " + f.prefix(8));

        System.out.println("\n== k-th element via prefix counts ==");
        FenwickTree marks = new FenwickTree(100);           // count of students per mark 1..100
        for (int m : new int[]{55, 72, 72, 88, 91, 40, 67}) marks.add(m, 1);
        System.out.println("sorted marks: 40 55 67 72 72 88 91");
        System.out.println("1st smallest = " + marks.lowerBound(1) + ", 4th = " + marks.lowerBound(4)
                + ", 5th = " + marks.lowerBound(5) + ", median (4th of 7) = " + marks.lowerBound(4));
        System.out.println("students with marks <= 70: " + marks.prefix(70));

        System.out.println("\n== Inversions ==");
        int[] ranks = {3, 1, 4, 5, 2};
        System.out.println("inversions of " + Arrays.toString(ranks) + " = " + inversions(ranks));
        System.out.println("inversions of [1, 2, 3, 4, 5] = " + inversions(new int[]{1, 2, 3, 4, 5})
                + ", of [5, 4, 3, 2, 1] = " + inversions(new int[]{5, 4, 3, 2, 1}));

        System.out.println("\n== Range add, point query ==");
        RangeAdd ra = new RangeAdd(8);
        ra.add(2, 5, 10);
        ra.add(4, 8, 3);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 8; i++) sb.append(ra.get(i)).append(i < 8 ? ", " : "");
        System.out.println("after +10 on [2..5] and +3 on [4..8]: [" + sb + "]");
    }
}
