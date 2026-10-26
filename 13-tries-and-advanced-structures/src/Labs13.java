import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Module 13 — reference solutions for the lab tasks of Sessions 1-3.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out *.java
 * Run:      java -cp out Labs13 1    (Session 1 labs; 2 or 3 for the others; no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs13 {

    // >>> replaceWords
    /** Replace each word of a sentence by its shortest dictionary root ("cattle" -> "cat"). */
    static String replaceWords(List<String> roots, String sentence) {
        Trie.Node root = new Trie.Node();
        for (String r : roots) {
            Trie.Node n = root;
            for (char c : r.toCharArray()) {
                if (n.next[c - 'a'] == null) n.next[c - 'a'] = new Trie.Node();
                n = n.next[c - 'a'];
            }
            n.isWord = true;
        }
        List<String> out = new ArrayList<>();
        for (String w : sentence.split(" ")) {
            Trie.Node n = root;
            String replacement = w;
            for (int i = 0; i < w.length() && n != null; i++) {
                n = n.next[w.charAt(i) - 'a'];
                if (n != null && n.isWord) { replacement = w.substring(0, i + 1); break; }   // shortest root
            }
            out.add(replacement);
        }
        return String.join(" ", out);
    }
    // <<< replaceWords

    // >>> maxXor
    /** Maximum a[i] XOR a[j] using a binary trie over 31 bits: at each bit, try to go the opposite way. */
    static int maxXor(int[] a) {
        int[][] child = new int[a.length * 31 + 1][2];
        int nodes = 1, best = 0;
        for (int x : a) {
            int n = 0;
            for (int b = 30; b >= 0; b--) {                 // insert x
                int bit = (x >> b) & 1;
                if (child[n][bit] == 0) child[n][bit] = nodes++;
                n = child[n][bit];
            }
            n = 0;
            int cur = 0;
            for (int b = 30; b >= 0; b--) {                 // greedy query: prefer the opposite bit
                int want = ((x >> b) & 1) ^ 1;
                if (child[n][want] != 0) { cur |= 1 << b; n = child[n][want]; }
                else n = child[n][want ^ 1];
            }
            best = Math.max(best, cur);
        }
        return best;
    }
    // <<< maxXor

    // >>> temperatureSwing
    /** Largest temperature swing (max - min) in [l, r]: one min-tree and one max-tree (stored negated). */
    static long[] swings(long[] temps, int[][] queries) {
        long[] neg = new long[temps.length];
        for (int i = 0; i < temps.length; i++) neg[i] = -temps[i];
        SegmentTree.PointTree mn = new SegmentTree.PointTree(temps, true);
        SegmentTree.PointTree mxNeg = new SegmentTree.PointTree(neg, true);
        long[] out = new long[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int l = queries[q][0], r = queries[q][1];
            out[q] = -mxNeg.query(l, r) - mn.query(l, r);
        }
        return out;
    }
    // <<< temperatureSwing

    // >>> seatBookings
    /** Flight seat bookings: bookings[i] = {first, last, seats} adds seats to flights first..last. Lazy tree. */
    static long[] seatTotals(int flights, int[][] bookings) {
        SegmentTree.LazyTree t = new SegmentTree.LazyTree(flights);
        for (int[] b : bookings) t.add(b[0] - 1, b[1] - 1, b[2]);
        long[] out = new long[flights];
        for (int i = 0; i < flights; i++) out[i] = t.sum(i, i);
        return out;
    }
    // <<< seatBookings

    // >>> countSmaller
    /** For each position, how many later elements are smaller? Coordinate-compress, then BIT from the right. */
    static int[] countSmaller(int[] a) {
        int[] sorted = Arrays.stream(a).distinct().sorted().toArray();
        FenwickTree f = new FenwickTree(sorted.length);
        int[] out = new int[a.length];
        for (int i = a.length - 1; i >= 0; i--) {
            int rank = Arrays.binarySearch(sorted, a[i]) + 1;      // 1-based rank
            out[i] = (int) f.prefix(rank - 1);
            f.add(rank, 1);
        }
        return out;
    }
    // <<< countSmaller

    // >>> liveRank
    /** Scores 0..100 arrive one by one; after each, report how many earlier scores were strictly higher. */
    static int[] liveRank(int[] scores) {
        FenwickTree f = new FenwickTree(101);                         // index = score + 1
        int[] out = new int[scores.length];
        for (int i = 0; i < scores.length; i++) {
            out[i] = (int) (i - f.prefix(scores[i] + 1));             // earlier scores minus those <= mine
            f.add(scores[i] + 1, 1);
        }
        return out;
    }
    // <<< liveRank

    public static void main(String[] args) {
        int s = args.length > 0 ? Integer.parseInt(args[0]) : 0;
        if (s == 0 || s == 1) {
            System.out.println("-- Session 1 labs --");
            System.out.println(replaceWords(List.of("cat", "bat", "rat", "ca"), "the cattle was rattled by the battery"));
            System.out.println("maxXor([3, 10, 5, 25, 2, 8]) = " + maxXor(new int[]{3, 10, 5, 25, 2, 8}));
        }
        if (s == 0 || s == 2) {
            System.out.println("-- Session 2 labs --");
            long[] temps = {31, 35, 29, 40, 38, 27, 33};
            System.out.println("swings for [0,2], [2,5], [0,6] = " + Arrays.toString(swings(temps, new int[][]{{0, 2}, {2, 5}, {0, 6}})));
            System.out.println("seat totals = " + Arrays.toString(seatTotals(5, new int[][]{{1, 2, 10}, {2, 3, 20}, {2, 5, 25}})));
        }
        if (s == 0 || s == 3) {
            System.out.println("-- Session 3 labs --");
            System.out.println("countSmaller([5, 2, 6, 1]) = " + Arrays.toString(countSmaller(new int[]{5, 2, 6, 1})));
            System.out.println("liveRank([70, 85, 60, 85, 90]) = " + Arrays.toString(liveRank(new int[]{70, 85, 60, 85, 90})));
        }
    }
}
