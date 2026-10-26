import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Module 15 self-check: JUnit-free tests for greedy algorithms (checked against brute force / DP).
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
        Random r = new Random(15);
        int[] pkr = {100, 50, 20, 10, 5, 2, 1};
        boolean canonical = true;
        for (int a = 1; a <= 500; a++) canonical &= GreedyFundamentals.greedyChange(pkr, a).size() == GreedyFundamentals.optimalCoins(pkr, a);
        check(canonical, "greedy is optimal for PKR denominations up to 500");
        check(GreedyFundamentals.greedyChange(new int[]{4, 3, 1}, 6).size() == 3 && GreedyFundamentals.optimalCoins(new int[]{1, 3, 4}, 6) == 2, "greedy fails for {1,3,4}");

        // fractional knapsack >= exact 0/1 and >= greedy 0/1
        boolean knap = true;
        for (int t = 0; t < 50; t++) {
            List<GreedyFundamentals.Item> items = new ArrayList<>();
            for (int i = 0; i < 6; i++) items.add(new GreedyFundamentals.Item("i" + i, 1 + r.nextInt(20), 1 + r.nextInt(100)));
            int cap = 10 + r.nextInt(40);
            double frac = GreedyFundamentals.fractionalKnapsack(items, cap, null);
            int exact = GreedyFundamentals.exactZeroOne(items, cap), greedy = GreedyFundamentals.greedyZeroOne(items, cap);
            knap &= frac + 1e-9 >= exact && exact >= greedy;
        }
        check(knap, "fractional >= exact 0/1 >= greedy 0/1");

        // Huffman: decode(encode) is identity, codes are prefix-free, cost <= fixed length
        boolean huff = true;
        for (int t = 0; t < 30; t++) {
            StringBuilder sb = new StringBuilder();
            int len = 1 + r.nextInt(60);
            for (int i = 0; i < len; i++) sb.append((char) ('a' + (int) Math.abs(r.nextGaussian() * 3) % 8));
            String s = sb.toString();
            Map<Character, Integer> f = HuffmanIntervals.frequencies(s);
            HuffmanIntervals.Node root = HuffmanIntervals.buildTree(f, null);
            Map<Character, String> code = new TreeMap<>();
            HuffmanIntervals.codes(root, "", code);
            String bits = HuffmanIntervals.encode(s, code);
            huff &= HuffmanIntervals.decode(bits, root).equals(s);
            for (String a : code.values()) for (String b : code.values()) if (a != b) huff &= !b.startsWith(a);
            int fixed = s.length() * Math.max(1, 32 - Integer.numberOfLeadingZeros(f.size() - 1));
            huff &= bits.length() <= fixed;
        }
        check(huff, "Huffman round-trip, prefix-free, never worse than fixed length");

        // earliest finish = brute-force maximum on small random inputs
        boolean sched = true;
        for (int t = 0; t < 200; t++) {
            int n = 1 + r.nextInt(8);
            List<HuffmanIntervals.Talk> ts = new ArrayList<>();
            for (int i = 0; i < n; i++) { int a = r.nextInt(20); ts.add(new HuffmanIntervals.Talk("t" + i, a, a + 1 + r.nextInt(6))); }
            int best = 0;
            for (int mask = 0; mask < 1 << n; mask++) {
                boolean ok = true;
                for (int i = 0; i < n && ok; i++) for (int j = i + 1; j < n && ok; j++)
                    if ((mask >> i & 1) == 1 && (mask >> j & 1) == 1
                            && ts.get(i).start() < ts.get(j).end() && ts.get(j).start() < ts.get(i).end()) ok = false;
                if (ok) best = Math.max(best, Integer.bitCount(mask));
            }
            sched &= HuffmanIntervals.earliestFinish(ts).size() == best;
        }
        check(sched, "earliest-finish selection is optimal (200 brute-force checks)");

        check(Labs15.canReach(new int[]{0}) && !Labs15.canReach(new int[]{0, 1}), "jump game edge cases");
        check(Labs15.minJumps(new int[]{2, 3, 0, 1, 4}) == 2, "min jumps");
        check(Labs15.gasStation(new int[]{2, 3, 4}, new int[]{3, 4, 3}) == -1, "gas station impossible");
        check(Labs15.minPlatforms(new int[]{100, 200}, new int[]{150, 250}) == 1, "platforms");
        check(Labs15.merge(new int[][]{{1, 4}, {4, 5}}).size() == 1, "merge touching");
        check(Labs15.minArrows(new int[][]{{1, 2}, {3, 4}, {5, 6}, {7, 8}}) == 4 && Labs15.minArrows(new int[][]{{1, 2}, {2, 3}}) == 1, "arrows");
        check(Labs15.eraseOverlap(new int[][]{{1, 2}, {1, 2}, {1, 2}}) == 2, "erase overlap");

        System.out.println("Module 15 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
