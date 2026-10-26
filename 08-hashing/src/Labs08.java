import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Module 08 — reference solutions for the lab tasks of Sessions 1-2.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out Labs08.java
 * Run:      java -cp out Labs08 1    (Session 1 labs; use 2 for Session 2, no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs08 {

    // >>> twoSum
    /** Indices i < j with a[i] + a[j] == target, or null. One pass: remember what we have seen. */
    static int[] twoSum(int[] a, int target) {
        Map<Integer, Integer> seen = new HashMap<>();      // value -> index
        for (int j = 0; j < a.length; j++) {
            Integer i = seen.get(target - a[j]);
            if (i != null) return new int[]{i, j};
            seen.put(a[j], j);
        }
        return null;
    }
    // <<< twoSum

    // >>> groupAnagrams
    /** Group words that are anagrams: the sorted letters are the key. */
    static List<List<String>> groupAnagrams(String[] words) {
        Map<String, List<String>> groups = new LinkedHashMap<>();
        for (String w : words) {
            char[] c = w.toCharArray();
            Arrays.sort(c);
            groups.computeIfAbsent(new String(c), k -> new ArrayList<>()).add(w);
        }
        return new ArrayList<>(groups.values());
    }
    // <<< groupAnagrams

    // >>> firstUnique
    /** Index of the first character that appears exactly once, or -1. Two passes over a count table. */
    static int firstUnique(String s) {
        Map<Character, Integer> count = new HashMap<>();
        for (char c : s.toCharArray()) count.merge(c, 1, Integer::sum);
        for (int i = 0; i < s.length(); i++) if (count.get(s.charAt(i)) == 1) return i;
        return -1;
    }
    // <<< firstUnique

    // >>> longestConsecutive
    /** Length of the longest run of consecutive integers in an unsorted array, O(n) expected. */
    static int longestConsecutive(int[] a) {
        Set<Integer> s = new HashSet<>();
        for (int x : a) s.add(x);
        int best = 0;
        for (int x : s) {
            if (s.contains(x - 1)) continue;          // only start counting at the beginning of a run
            int len = 1;
            while (s.contains(x + len)) len++;
            best = Math.max(best, len);
        }
        return best;
    }
    // <<< longestConsecutive

    // >>> subarraySum
    /** Number of contiguous subarrays summing to k (negatives allowed): prefix sums + hash map. */
    static int subarraySum(int[] a, int k) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1);                      // empty prefix
        int sum = 0, count = 0;
        for (int x : a) {
            sum += x;
            count += prefixCount.getOrDefault(sum - k, 0);
            prefixCount.merge(sum, 1, Integer::sum);
        }
        return count;
    }
    // <<< subarraySum

    // >>> lruCache
    /** LRU cache: LinkedHashMap in access order evicts the least recently used entry. */
    static final class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private static final long serialVersionUID = 1L;
        private final int capacity;
        LRUCache(int capacity) { super(16, 0.75f, true); this.capacity = capacity; }
        @Override protected boolean removeEldestEntry(Map.Entry<K, V> eldest) { return size() > capacity; }
    }
    // <<< lruCache

    // >>> isomorphic
    /** "egg" and "add" are isomorphic: a consistent one-to-one letter mapping exists. */
    static boolean isomorphic(String s, String t) {
        if (s.length() != t.length()) return false;
        Map<Character, Character> st = new HashMap<>(), ts = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char a = s.charAt(i), b = t.charAt(i);
            if (st.getOrDefault(a, b) != b || ts.getOrDefault(b, a) != a) return false;
            st.put(a, b);
            ts.put(b, a);
        }
        return true;
    }
    // <<< isomorphic

    public static void main(String[] args) {
        int session = args.length > 0 ? Integer.parseInt(args[0]) : 0;
        if (session == 0 || session == 1) {
            System.out.println("-- Session 1 labs --");
            System.out.println("twoSum([2, 7, 11, 15], 9) = " + Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)));
            System.out.println("groupAnagrams = " + groupAnagrams(new String[]{"listen", "silent", "enlist", "rat", "tar", "art", "dog"}));
            System.out.println("firstUnique(\"swiss\") = " + firstUnique("swiss") + ", firstUnique(\"aabb\") = " + firstUnique("aabb"));
        }
        if (session == 0 || session == 2) {
            System.out.println("-- Session 2 labs --");
            System.out.println("longestConsecutive([100, 4, 200, 1, 3, 2]) = " + longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
            System.out.println("subarraySum([1, 2, 3, -2, 2], 3) = " + subarraySum(new int[]{1, 2, 3, -2, 2}, 3));
            LRUCache<String, String> cache = new LRUCache<>(2);
            cache.put("/home", "page1");
            cache.put("/dsa", "page2");
            cache.get("/home");                       // /home becomes most recent
            cache.put("/ai", "page3");                // evicts /dsa
            System.out.println("LRU cache keys after access + insert = " + cache.keySet());
            System.out.println("isomorphic(egg, add) = " + isomorphic("egg", "add") + ", isomorphic(foo, bar) = " + isomorphic("foo", "bar"));
        }
    }
}
