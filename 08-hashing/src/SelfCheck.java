import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Module 08 self-check: JUnit-free tests for hashing.
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
        check(HashChaining.polyHash("hello") == "hello".hashCode(), "polyHash equals String.hashCode");
        check(HashChaining.bucketOf(Integer.MIN_VALUE, 7) >= 0, "bucketOf never negative");

        // random operations against java.util.HashMap
        Random r = new Random(8);
        HashChaining.ChainedMap<String, Integer> cm = new HashChaining.ChainedMap<>(2);
        OpenAddressing.LinearProbingMap lp = new OpenAddressing.LinearProbingMap(2);
        Map<String, Integer> ref = new HashMap<>();
        boolean ok = true;
        for (int step = 0; step < 20000; step++) {
            String k = "k" + r.nextInt(500);
            int op = r.nextInt(3), v = r.nextInt(1000);
            if (op == 0) { cm.put(k, v); lp.put(k, v); ref.put(k, v); }
            else if (op == 1) {
                boolean had = ref.remove(k) != null;
                ok &= (cm.remove(k) != null) == had && lp.remove(k) == had;
            } else ok &= java.util.Objects.equals(cm.get(k), ref.get(k)) && java.util.Objects.equals(lp.get(k), ref.get(k));
            ok &= cm.size() == ref.size() && lp.size() == ref.size();
        }
        check(ok, "20000 random put/get/remove match HashMap (chaining + linear probing)");
        check(cm.loadFactor() <= 0.75, "chaining load factor kept <= 0.75");

        Set<OpenAddressing.Student> s = new HashSet<>();
        s.add(new OpenAddressing.Student("X", "a"));
        s.add(new OpenAddressing.Student("X", "b"));
        check(s.size() == 1, "equals/hashCode contract");

        check(Arrays.equals(Labs08.twoSum(new int[]{3, 2, 4}, 6), new int[]{1, 2}), "twoSum");
        check(Labs08.twoSum(new int[]{1, 2}, 10) == null, "twoSum none");
        check(Labs08.groupAnagrams(new String[]{"ab", "ba", "c"}).equals(List.of(List.of("ab", "ba"), List.of("c"))), "groupAnagrams");
        check(Labs08.firstUnique("leetcode") == 0 && Labs08.firstUnique("aabb") == -1, "firstUnique");
        check(Labs08.longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}) == 9, "longestConsecutive");
        check(Labs08.longestConsecutive(new int[]{}) == 0, "longestConsecutive empty");
        // subarraySum vs brute force
        for (int t = 0; t < 50; t++) {
            int[] a = new int[1 + r.nextInt(20)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(11) - 5;
            int k = r.nextInt(11) - 5, brute = 0;
            for (int i = 0; i < a.length; i++) { int sum = 0; for (int j = i; j < a.length; j++) { sum += a[j]; if (sum == k) brute++; } }
            check(Labs08.subarraySum(a, k) == brute, "subarraySum random " + t);
        }
        Labs08.LRUCache<Integer, Integer> c = new Labs08.LRUCache<>(2);
        c.put(1, 1); c.put(2, 2); c.get(1); c.put(3, 3);
        check(c.containsKey(1) && !c.containsKey(2) && c.containsKey(3), "LRU eviction");
        check(Labs08.isomorphic("paper", "title") && !Labs08.isomorphic("badc", "baba"), "isomorphic");

        System.out.println("Module 08 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
