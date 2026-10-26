import java.util.Arrays;

/**
 * Session 2 demo: segment trees. A point-update / range-sum tree and a range-minimum tree built
 * recursively over an array, plus a lazy-propagation tree supporting "add v to every element in
 * [l, r]" and range-sum queries, each in O(log n).
 *
 * Compile:  javac -d out SegmentTree.java
 * Run:      java -cp out SegmentTree
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class SegmentTree {

    /** Range-sum (or range-min) segment tree with point updates. Node i covers [lo, hi]; children 2i, 2i+1. */
    static final class PointTree {
        final int n;
        final long[] t;
        final boolean isMin;
        int visited;                                   // nodes touched by the last query (for teaching)

        PointTree(long[] a, boolean isMin) {
            n = a.length;
            this.isMin = isMin;
            t = new long[4 * n];
            build(a, 1, 0, n - 1);
        }

        private long combine(long x, long y) { return isMin ? Math.min(x, y) : x + y; }
        private long identity() { return isMin ? Long.MAX_VALUE : 0; }

        private void build(long[] a, int i, int lo, int hi) {
            if (lo == hi) { t[i] = a[lo]; return; }
            int mid = (lo + hi) / 2;
            build(a, 2 * i, lo, mid);
            build(a, 2 * i + 1, mid + 1, hi);
            t[i] = combine(t[2 * i], t[2 * i + 1]);
        }

        long query(int l, int r) { visited = 0; return query(1, 0, n - 1, l, r); }

        private long query(int i, int lo, int hi, int l, int r) {
            visited++;
            if (r < lo || hi < l) return identity();               // no overlap
            if (l <= lo && hi <= r) return t[i];                    // total overlap
            int mid = (lo + hi) / 2;                                // partial overlap: ask both children
            return combine(query(2 * i, lo, mid, l, r), query(2 * i + 1, mid + 1, hi, l, r));
        }

        void set(int pos, long v) { set(1, 0, n - 1, pos, v); }

        private void set(int i, int lo, int hi, int pos, long v) {
            if (lo == hi) { t[i] = v; return; }
            int mid = (lo + hi) / 2;
            if (pos <= mid) set(2 * i, lo, mid, pos, v); else set(2 * i + 1, mid + 1, hi, pos, v);
            t[i] = combine(t[2 * i], t[2 * i + 1]);                // recompute on the way up
        }
    }

    /** Range add + range sum with lazy propagation: pending additions wait in lazy[] until needed. */
    static final class LazyTree {
        final int n;
        final long[] sum, lazy;

        LazyTree(int n) { this.n = n; sum = new long[4 * n]; lazy = new long[4 * n]; }

        private void apply(int i, int lo, int hi, long v) { sum[i] += v * (hi - lo + 1); lazy[i] += v; }

        private void push(int i, int lo, int hi) {                  // hand the pending add to the children
            if (lazy[i] == 0) return;
            int mid = (lo + hi) / 2;
            apply(2 * i, lo, mid, lazy[i]);
            apply(2 * i + 1, mid + 1, hi, lazy[i]);
            lazy[i] = 0;
        }

        void add(int l, int r, long v) { add(1, 0, n - 1, l, r, v); }

        private void add(int i, int lo, int hi, int l, int r, long v) {
            if (r < lo || hi < l) return;
            if (l <= lo && hi <= r) { apply(i, lo, hi, v); return; }   // stop here, remember lazily
            push(i, lo, hi);
            int mid = (lo + hi) / 2;
            add(2 * i, lo, mid, l, r, v);
            add(2 * i + 1, mid + 1, hi, l, r, v);
            sum[i] = sum[2 * i] + sum[2 * i + 1];
        }

        long sum(int l, int r) { return sum(1, 0, n - 1, l, r); }

        private long sum(int i, int lo, int hi, int l, int r) {
            if (r < lo || hi < l) return 0;
            if (l <= lo && hi <= r) return sum[i];
            push(i, lo, hi);
            int mid = (lo + hi) / 2;
            return sum(2 * i, lo, mid, l, r) + sum(2 * i + 1, mid + 1, hi, l, r);
        }
    }

    public static void main(String[] args) {
        long[] sales = {5, 8, 6, 3, 2, 7, 2, 6};      // daily sales of a canteen item, days 0..7
        System.out.println("array = " + Arrays.toString(sales));

        PointTree st = new PointTree(sales, false);
        System.out.println("\n== Range-sum tree ==");
        System.out.println("tree[1..15] = " + Arrays.toString(Arrays.copyOfRange(st.t, 1, 16)));
        System.out.println("sum[2..6] = " + st.query(2, 6) + "  (nodes visited: " + st.visited + ")");
        System.out.println("sum[0..7] = " + st.query(0, 7) + "  (nodes visited: " + st.visited + ")");
        st.set(3, 10);
        System.out.println("set day 3 to 10 -> sum[2..6] = " + st.query(2, 6) + ", sum[0..3] = " + st.query(0, 3));

        PointTree mn = new PointTree(sales, true);
        System.out.println("\n== Range-minimum tree ==");
        System.out.println("min[0..3] = " + mn.query(0, 3) + ", min[4..7] = " + mn.query(4, 7) + ", min[1..2] = " + mn.query(1, 2));

        System.out.println("\n== Lazy propagation: range add ==");
        LazyTree lz = new LazyTree(8);
        for (int i = 0; i < 8; i++) lz.add(i, i, sales[i]);
        lz.add(2, 5, 10);                                // promotion: +10 on days 2..5
        System.out.println("after +10 on [2..5]: sum[0..7] = " + lz.sum(0, 7) + ", sum[4..6] = " + lz.sum(4, 6)
                + ", day 5 = " + lz.sum(5, 5));
        lz.add(0, 7, -1);
        System.out.println("after -1 on all:     sum[0..7] = " + lz.sum(0, 7) + ", day 0 = " + lz.sum(0, 0));

        System.out.println("\n== Nodes visited per query, n = 1,000,000 ==");
        long[] big = new long[1_000_000];
        Arrays.fill(big, 1);
        PointTree bt = new PointTree(big, false);
        java.util.Random r = new java.util.Random(13);
        int worst = 0;
        for (int q = 0; q < 10_000; q++) {
            int a = r.nextInt(big.length), b = r.nextInt(big.length);
            bt.query(Math.min(a, b), Math.max(a, b));
            worst = Math.max(worst, bt.visited);
        }
        System.out.println("worst nodes visited over 10,000 random queries = " + worst + "  (4*log2 n ≈ 80)");
    }
}
