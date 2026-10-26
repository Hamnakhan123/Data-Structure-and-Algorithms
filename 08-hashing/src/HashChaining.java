import java.util.ArrayList;
import java.util.List;

/**
 * Session 1 demo: hash functions, compression and a separate-chaining hash map built from scratch.
 *
 * Shows a polynomial string hash, how keys are compressed into bucket indices, how collisions form
 * chains, and how the table doubles when the load factor passes 0.75.
 *
 * Compile:  javac -d out HashChaining.java
 * Run:      java -cp out HashChaining
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class HashChaining {

    /** Polynomial rolling hash h = s0*31^(n-1) + ... + s(n-1), exactly what String.hashCode() computes. */
    static int polyHash(String s) {
        int h = 0;
        for (int i = 0; i < s.length(); i++) h = 31 * h + s.charAt(i);   // overflow wraps, which is fine
        return h;
    }

    /** A deliberately weak hash: sum of characters. Anagrams always collide. */
    static int additiveHash(String s) {
        int h = 0;
        for (char c : s.toCharArray()) h += c;
        return h;
    }

    /** Compression: map any int (possibly negative) to 0..m-1. */
    static int bucketOf(int hash, int m) {
        return (hash & 0x7fffffff) % m;     // clear the sign bit, never Math.abs (abs(MIN_VALUE) < 0)
    }

    /** Separate chaining: an array of singly linked lists of entries. */
    static final class ChainedMap<K, V> {
        private static final class Node<K, V> {
            final K key;
            V value;
            Node<K, V> next;
            Node(K k, V v, Node<K, V> n) { key = k; value = v; next = n; }
        }

        private Node<K, V>[] table;
        private int size;
        private int resizes;
        private static final double MAX_LOAD = 0.75;

        ChainedMap(int capacity) { table = newTable(capacity); }

        @SuppressWarnings("unchecked")
        private static <K, V> Node<K, V>[] newTable(int m) { return (Node<K, V>[]) new Node<?, ?>[m]; }

        private int index(Object key, int m) { return bucketOf(key.hashCode(), m); }

        V get(K key) {
            for (Node<K, V> n = table[index(key, table.length)]; n != null; n = n.next)
                if (n.key.equals(key)) return n.value;
            return null;
        }

        /** Insert or update. Returns the previous value or null. */
        V put(K key, V value) {
            int i = index(key, table.length);
            for (Node<K, V> n = table[i]; n != null; n = n.next)
                if (n.key.equals(key)) { V old = n.value; n.value = value; return old; }
            table[i] = new Node<>(key, value, table[i]);            // insert at head of chain: O(1)
            size++;
            if ((double) size / table.length > MAX_LOAD) resize(2 * table.length);
            return null;
        }

        V remove(K key) {
            int i = index(key, table.length);
            Node<K, V> prev = null;
            for (Node<K, V> n = table[i]; n != null; prev = n, n = n.next) {
                if (n.key.equals(key)) {
                    if (prev == null) table[i] = n.next; else prev.next = n.next;
                    size--;
                    return n.value;
                }
            }
            return null;
        }

        private void resize(int m) {
            Node<K, V>[] old = table;
            table = newTable(m);
            for (Node<K, V> head : old)
                for (Node<K, V> n = head; n != null; ) {
                    Node<K, V> next = n.next;
                    int i = index(n.key, m);                           // every key is rehashed
                    n.next = table[i];
                    table[i] = n;
                    n = next;
                }
            resizes++;
        }

        int size() { return size; }
        int capacity() { return table.length; }
        int resizes() { return resizes; }
        double loadFactor() { return (double) size / table.length; }

        int longestChain() {
            int best = 0;
            for (Node<K, V> head : table) {
                int len = 0;
                for (Node<K, V> n = head; n != null; n = n.next) len++;
                best = Math.max(best, len);
            }
            return best;
        }

        /** Text picture of the buckets, e.g. "[2] Sana -> Ali". */
        List<String> dump() {
            List<String> out = new ArrayList<>();
            for (int i = 0; i < table.length; i++) {
                StringBuilder sb = new StringBuilder("[" + i + "]");
                for (Node<K, V> n = table[i]; n != null; n = n.next) sb.append(n == table[i] ? " " : " -> ").append(n.key);
                out.add(sb.toString());
            }
            return out;
        }
    }

    public static void main(String[] args) {
        System.out.println("== 1. Hash functions ==");
        for (String s : new String[]{"cat", "act", "tac", "dog"})
            System.out.printf("%-4s poly=%-8d String.hashCode=%-8d additive=%d%n",
                    s, polyHash(s), s.hashCode(), additiveHash(s));

        System.out.println("\n== 2. Compression into 7 buckets ==");
        for (String s : new String[]{"Ali", "Sana", "Bilal", "Hira", "Zoya"})
            System.out.printf("%-6s hash=%-9d bucket=%d%n", s, s.hashCode(), bucketOf(s.hashCode(), 7));
        System.out.println("bucketOf(Integer.MIN_VALUE, 7) = " + bucketOf(Integer.MIN_VALUE, 7)
                + "   (Math.abs would give " + Math.abs(Integer.MIN_VALUE) + ")");

        System.out.println("\n== 3. Separate chaining (capacity 4, no resize yet) ==");
        ChainedMap<String, Integer> marks = new ChainedMap<>(4);
        marks.put("Ali", 71);
        marks.put("Sana", 88);
        marks.put("Bilal", 64);
        for (String line : marks.dump()) System.out.println(line);
        System.out.println("get(Sana) = " + marks.get("Sana") + ", get(Omar) = " + marks.get("Omar"));
        System.out.println("put(Ali, 75) returned old value " + marks.put("Ali", 75));
        System.out.println("load factor = " + marks.loadFactor());

        System.out.println("\n== 4. Fourth key pushes load past 0.75 -> resize to 8 ==");
        marks.put("Hira", 92);
        for (String line : marks.dump()) System.out.println(line);
        System.out.println("size=" + marks.size() + " capacity=" + marks.capacity() + " resizes=" + marks.resizes());

        System.out.println("\n== 5. remove(Bilal) ==");
        System.out.println("removed " + marks.remove("Bilal") + ", size now " + marks.size());

        System.out.println("\n== 6. 100,000 roll numbers: weak vs good hash ==");
        ChainedMap<String, Integer> good = new ChainedMap<>(16);
        ChainedMap<WeakKey, Integer> weak = new ChainedMap<>(16);
        for (int i = 0; i < 100_000; i++) {
            String roll = "FA24-BCS-" + i;
            good.put(roll, i);
            weak.put(new WeakKey(roll), i);
        }
        System.out.println("String.hashCode : capacity=" + good.capacity() + " longest chain=" + good.longestChain());
        System.out.println("additive hash   : capacity=" + weak.capacity() + " longest chain=" + weak.longestChain());
    }

    /** Wraps a String but uses the additive hash: shows how a poor hash function creates long chains. */
    record WeakKey(String s) {
        @Override public int hashCode() { return additiveHash(s); }
    }
}
