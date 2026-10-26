import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * Session 2 demo: Huffman coding (build the tree with a priority queue, derive prefix-free codes,
 * encode, decode and measure compression) and interval scheduling (earliest-finish-time selection,
 * and why other greedy rules fail).
 *
 * Compile:  javac -d out HuffmanIntervals.java
 * Run:      java -cp out HuffmanIntervals
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class HuffmanIntervals {

    // ---------------- Huffman coding ----------------
    static final class Node {
        final char ch;            // meaningful only for leaves
        final int freq;
        final Node left, right;
        final int order;          // tie-breaker so the tree is deterministic
        Node(char ch, int freq, Node l, Node r, int order) { this.ch = ch; this.freq = freq; left = l; right = r; this.order = order; }
        boolean isLeaf() { return left == null; }
    }

    static Node buildTree(Map<Character, Integer> freq, List<String> log) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt((Node n) -> n.freq).thenComparingInt(n -> n.order));
        int order = 0;
        for (Map.Entry<Character, Integer> e : freq.entrySet()) pq.offer(new Node(e.getKey(), e.getValue(), null, null, order++));
        if (pq.size() == 1) {                                    // one distinct symbol: give it code "0"
            Node only = pq.poll();
            return new Node('\0', only.freq, only, null, order);
        }
        while (pq.size() > 1) {
            Node a = pq.poll(), b = pq.poll();                   // the two rarest subtrees
            Node parent = new Node('\0', a.freq + b.freq, a, b, order++);
            if (log != null) log.add("merge " + label(a) + " + " + label(b) + " -> " + parent.freq);
            pq.offer(parent);
        }
        return pq.poll();
    }

    static String label(Node n) { return n.isLeaf() ? "'" + n.ch + "':" + n.freq : "(" + n.freq + ")"; }

    static void codes(Node n, String prefix, Map<Character, String> out) {
        if (n == null) return;
        if (n.isLeaf()) { out.put(n.ch, prefix.isEmpty() ? "0" : prefix); return; }
        codes(n.left, prefix + "0", out);                        // left edge = 0
        codes(n.right, prefix + "1", out);                       // right edge = 1
    }

    static String encode(String s, Map<Character, String> code) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) sb.append(code.get(c));
        return sb.toString();
    }

    static String decode(String bits, Node root) {
        StringBuilder sb = new StringBuilder();
        Node n = root;
        for (char b : bits.toCharArray()) {
            n = b == '0' ? n.left : n.right;
            if (n.isLeaf()) { sb.append(n.ch); n = root; }        // prefix-free: a leaf ends a symbol
        }
        return sb.toString();
    }

    static Map<Character, Integer> frequencies(String s) {
        Map<Character, Integer> f = new TreeMap<>();
        for (char c : s.toCharArray()) f.merge(c, 1, Integer::sum);
        return f;
    }

    // ---------------- Interval scheduling ----------------
    record Talk(String name, int start, int end) { }

    /** Maximum number of non-overlapping talks using a given greedy order. */
    static List<Talk> select(List<Talk> talks, Comparator<Talk> order) {
        List<Talk> sorted = new ArrayList<>(talks);
        sorted.sort(order);
        List<Talk> chosen = new ArrayList<>();
        for (Talk t : sorted) {
            boolean fits = true;
            for (Talk c : chosen) if (t.start() < c.end() && c.start() < t.end()) { fits = false; break; }
            if (fits) chosen.add(t);
        }
        chosen.sort(Comparator.comparingInt(Talk::start));
        return chosen;
    }

    /** The optimal rule in O(n log n): sort by end time, take a talk if it starts after the last one ended. */
    static List<Talk> earliestFinish(List<Talk> talks) {
        List<Talk> sorted = new ArrayList<>(talks);
        sorted.sort(Comparator.comparingInt(Talk::end));
        List<Talk> chosen = new ArrayList<>();
        int lastEnd = Integer.MIN_VALUE;
        for (Talk t : sorted)
            if (t.start() >= lastEnd) { chosen.add(t); lastEnd = t.end(); }
        return chosen;
    }

    static String names(List<Talk> ts) {
        List<String> s = new ArrayList<>();
        for (Talk t : ts) s.add(t.name());
        return s.toString();
    }

    public static void main(String[] args) {
        String text = "abracadabra alakazam";
        Map<Character, Integer> freq = frequencies(text);
        System.out.println("== Huffman coding of \"" + text + "\" ==");
        System.out.println("frequencies: " + freq);
        List<String> log = new ArrayList<>();
        Node root = buildTree(freq, log);
        for (String line : log) System.out.println("  " + line);
        Map<Character, String> code = new TreeMap<>();
        codes(root, "", code);
        System.out.println("codes: " + code);
        String bits = encode(text, code);
        System.out.println("encoded: " + bits);
        System.out.println("decoded: \"" + decode(bits, root) + "\"");
        int fixedBits = text.length() * (32 - Integer.numberOfLeadingZeros(freq.size() - 1));
        System.out.println("bits: Huffman " + bits.length() + " vs fixed-length " + fixedBits + " vs 8-bit ASCII " + text.length() * 8
                + String.format(" (saves %.0f%% over ASCII)", 100.0 * (1 - bits.length() / (text.length() * 8.0))));

        System.out.println("\n== Interval scheduling: one seminar hall, many talk requests ==");
        List<Talk> talks = List.of(
                new Talk("Keynote", 9, 17), new Talk("Git", 9, 10), new Talk("Java", 10, 12),
                new Talk("Cloud", 11, 13), new Talk("AI", 12, 14), new Talk("Ethics", 14, 15),
                new Talk("Security", 13, 16), new Talk("Careers", 15, 17));
        System.out.println("shortest first  : " + names(select(talks, Comparator.comparingInt((Talk t) -> t.end() - t.start()))));
        System.out.println("earliest start  : " + names(select(talks, Comparator.comparingInt(Talk::start))));
        System.out.println("earliest finish : " + names(earliestFinish(talks)) + "  <- optimal");

        List<Talk> trap = List.of(new Talk("short-middle", 4, 6), new Talk("left", 0, 5), new Talk("right", 5, 10));
        System.out.println("\nshortest-first trap " + Arrays.asList("[0,5)", "[4,6)", "[5,10)") + ": shortest first picks "
                + names(select(trap, Comparator.comparingInt((Talk t) -> t.end() - t.start())))
                + ", earliest finish picks " + names(earliestFinish(trap)));
    }
}
