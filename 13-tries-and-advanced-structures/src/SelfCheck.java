import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * Module 13 self-check: JUnit-free tests for tries, segment trees and Fenwick trees (vs brute force).
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
        Random r = new Random(13);
        // Trie vs TreeSet
        Trie t = new Trie();
        TreeSet<String> ref = new TreeSet<>();
        boolean ok = true;
        for (int step = 0; step < 3000; step++) {
            StringBuilder sb = new StringBuilder();
            int len = 1 + r.nextInt(4);
            for (int i = 0; i < len; i++) sb.append((char) ('a' + r.nextInt(3)));
            String w = sb.toString();
            if (r.nextInt(3) > 0) { t.insert(w); ref.add(w); } else ok &= t.delete(w) == ref.remove(w);
            String p = w.substring(0, 1 + r.nextInt(w.length()));
            long cnt = ref.stream().filter(x -> x.startsWith(p)).count();
            ok &= t.contains(w) == ref.contains(w) && t.countPrefix(p) == cnt && t.size() == ref.size();
            ok &= t.autocomplete(p, 1000).equals(ref.stream().filter(x -> x.startsWith(p)).toList());
        }
        check(ok, "trie: 3000 random ops match TreeSet (contains, countPrefix, autocomplete, delete)");

        // Segment trees vs brute force
        long[] a = new long[200];
        for (int i = 0; i < a.length; i++) a[i] = r.nextInt(100);
        SegmentTree.PointTree sum = new SegmentTree.PointTree(a.clone(), false), min = new SegmentTree.PointTree(a.clone(), true);
        SegmentTree.LazyTree lazy = new SegmentTree.LazyTree(a.length);
        long[] b = a.clone(), c = a.clone();
        for (int i = 0; i < a.length; i++) lazy.add(i, i, a[i]);
        boolean segOk = true, lazyOk = true;
        for (int q = 0; q < 2000; q++) {
            int l = r.nextInt(a.length), h = l + r.nextInt(a.length - l);
            if (r.nextBoolean()) { int p = r.nextInt(a.length); long v = r.nextInt(100); b[p] = v; sum.set(p, v); min.set(p, v); }
            long s = 0, m = Long.MAX_VALUE;
            for (int i = l; i <= h; i++) { s += b[i]; m = Math.min(m, b[i]); }
            segOk &= sum.query(l, h) == s && min.query(l, h) == m;
            long v = r.nextInt(21) - 10;
            lazy.add(l, h, v);
            for (int i = l; i <= h; i++) c[i] += v;
            int x = r.nextInt(a.length), y = x + r.nextInt(a.length - x);
            long cs = 0;
            for (int i = x; i <= y; i++) cs += c[i];
            lazyOk &= lazy.sum(x, y) == cs;
        }
        check(segOk, "segment tree sum/min with point updates match brute force");
        check(lazyOk, "lazy segment tree range add/sum matches brute force");

        // Fenwick vs brute force
        FenwickTree f = new FenwickTree(a);
        long[] d = a.clone();
        boolean fOk = true;
        for (int q = 0; q < 2000; q++) {
            int p = 1 + r.nextInt(a.length);
            long v = r.nextInt(50);
            f.add(p, v); d[p - 1] += v;
            int l = 1 + r.nextInt(a.length), h = l + r.nextInt(a.length - l + 1);
            long s = 0;
            for (int i = l; i <= h; i++) s += d[i - 1];
            fOk &= f.rangeSum(l, h) == s;
        }
        check(fOk, "Fenwick point update / range sum match brute force");
        FenwickTree cnt = new FenwickTree(10);
        for (int v : new int[]{3, 3, 7, 1}) cnt.add(v, 1);
        check(cnt.lowerBound(1) == 1 && cnt.lowerBound(2) == 3 && cnt.lowerBound(3) == 3 && cnt.lowerBound(4) == 7, "Fenwick k-th");
        boolean invOk = true;
        for (int q = 0; q < 50; q++) {
            int[] x = new int[1 + r.nextInt(30)];
            for (int i = 0; i < x.length; i++) x[i] = 1 + r.nextInt(20);
            long brute = 0;
            for (int i = 0; i < x.length; i++) for (int j = i + 1; j < x.length; j++) if (x[i] > x[j]) brute++;
            invOk &= FenwickTree.inversions(x) == brute;
        }
        check(invOk, "inversions match O(n^2) count");

        check(Labs13.replaceWords(List.of("a", "aa"), "aaa b").equals("a b"), "replaceWords shortest root");
        check(Labs13.maxXor(new int[]{14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70}) == 127, "maxXor");
        check(Arrays.equals(Labs13.swings(new long[]{5}, new int[][]{{0, 0}}), new long[]{0}), "swing single");
        check(Arrays.equals(Labs13.countSmaller(new int[]{-1, -1}), new int[]{0, 0}), "countSmaller duplicates");
        check(Arrays.equals(Labs13.liveRank(new int[]{50, 50, 100, 0}), new int[]{0, 0, 0, 3}), "liveRank");

        System.out.println("Module 13 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
