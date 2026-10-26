import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * Session 2 demo: open addressing (linear probing with tombstones), probe-sequence comparison,
 * the equals/hashCode contract, and Java's HashMap, HashSet, LinkedHashMap and TreeMap.
 *
 * Compile:  javac -d out OpenAddressing.java
 * Run:      java -cp out OpenAddressing
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class OpenAddressing {

    /** Linear-probing map with String keys and int values. Deleted slots become tombstones. */
    static final class LinearProbingMap {
        private static final String TOMBSTONE = new String("<deleted>");   // unique object, compared by ==
        private String[] keys;
        private int[] vals;
        private int size, used;          // used = live keys + tombstones
        long probes;                     // total slots inspected, for measurement

        LinearProbingMap(int capacity) { keys = new String[capacity]; vals = new int[capacity]; }

        private int home(String k, int m) { return (k.hashCode() & 0x7fffffff) % m; }

        /** Index of key k, or -1. Skips tombstones but stops at a truly empty slot. */
        private int find(String k) {
            int m = keys.length;
            for (int i = home(k, m), step = 0; step < m; i = (i + 1) % m, step++) {
                probes++;
                if (keys[i] == null) return -1;
                if (keys[i] != TOMBSTONE && keys[i].equals(k)) return i;
            }
            return -1;
        }

        Integer get(String k) { int i = find(k); return i < 0 ? null : vals[i]; }

        void put(String k, int v) {
            int i = find(k);
            if (i >= 0) { vals[i] = v; return; }
            if (2 * (used + 1) > keys.length) rehash(4 * size + 4);    // keep (live + tombstones) <= 1/2
            int m = keys.length;
            int j = home(k, m);
            while (keys[j] != null && keys[j] != TOMBSTONE) { j = (j + 1) % m; probes++; }
            if (keys[j] == null) used++;
            keys[j] = k;
            vals[j] = v;
            size++;
        }

        boolean remove(String k) {
            int i = find(k);
            if (i < 0) return false;
            keys[i] = TOMBSTONE;            // NOT null: that would break the probe chains behind it
            size--;
            return true;
        }

        private void rehash(int m) {
            String[] ok = keys;
            int[] ov = vals;
            keys = new String[m];
            vals = new int[m];
            size = used = 0;
            for (int i = 0; i < ok.length; i++)
                if (ok[i] != null && ok[i] != TOMBSTONE) put(ok[i], ov[i]);
        }

        int size() { return size; }
        int capacity() { return keys.length; }

        String layout() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < keys.length; i++)
                sb.append(i).append(':').append(keys[i] == null ? "_" : keys[i] == TOMBSTONE ? "†" : keys[i]).append(' ');
            return sb.toString().trim();
        }
    }

    /** Average probes per successful insert for one probing scheme into a table of size m at load alpha. */
    static double averageProbes(String scheme, int m, double alpha, long seed) {
        Integer[] t = new Integer[m];
        Random r = new Random(seed);
        int n = (int) (alpha * m);
        long total = 0;
        for (int c = 0; c < n; c++) {
            int key = r.nextInt(1_000_000);
            int h1 = key % m, h2 = 1 + key % (m - 2);
            for (int i = 0; ; i++) {
                int idx;
                if (scheme.equals("linear")) idx = (h1 + i) % m;
                else if (scheme.equals("quadratic")) idx = (int) ((h1 + (long) i * i) % m);
                else idx = (int) ((h1 + (long) i * h2) % m);
                total++;
                if (t[idx] == null) { t[idx] = key; break; }
                if (i > m) break;           // quadratic may cycle; give up (counted)
            }
        }
        return (double) total / n;
    }

    /** A key class that follows the equals/hashCode contract. */
    static final class Student {
        final String roll;
        final String name;
        Student(String roll, String name) { this.roll = roll; this.name = name; }
        @Override public boolean equals(Object o) {
            return o instanceof Student && ((Student) o).roll.equals(roll);
        }
        @Override public int hashCode() { return Objects.hash(roll); }
        @Override public String toString() { return roll; }
    }

    /** Same fields, but overrides equals only: breaks HashSet. */
    static final class BrokenStudent {
        final String roll;
        BrokenStudent(String roll) { this.roll = roll; }
        @Override public boolean equals(Object o) {
            return o instanceof BrokenStudent && ((BrokenStudent) o).roll.equals(roll);
        }
        @Override public int hashCode() { return System.identityHashCode(this); }  // what you get by NOT overriding
    }

    public static void main(String[] args) {
        System.out.println("== 1. Linear probing, capacity 8 ==");
        LinearProbingMap m = new LinearProbingMap(8);
        // "Aa" and "BB" have the same String.hashCode (2112): a classic collision
        for (String k : new String[]{"Aa", "BB", "C"}) {
            m.put(k, k.length());
            System.out.printf("put %-3s home=%d -> %s%n", k, (k.hashCode() & 0x7fffffff) % 8, m.layout());
        }
        m.remove("Aa");
        System.out.println("remove Aa       -> " + m.layout());
        System.out.println("get(BB) after delete = " + m.get("BB") + "  (tombstone kept the chain intact)");
        m.put("Aa", 9);
        System.out.println("put Aa again    -> " + m.layout());

        System.out.println("\n== 2. Average probes per insert (m = 10007) ==");
        System.out.println("load    linear  quadratic  double");
        for (double a : new double[]{0.25, 0.5, 0.75, 0.9})
            System.out.printf("%.2f  %7.2f  %9.2f  %6.2f%n", a,
                    averageProbes("linear", 10007, a, 1), averageProbes("quadratic", 10007, a, 1),
                    averageProbes("double", 10007, a, 1));

        System.out.println("\n== 3. equals/hashCode contract ==");
        Set<Student> good = new HashSet<>();
        good.add(new Student("BCS-01", "Hira"));
        good.add(new Student("BCS-01", "Hira A."));
        Set<BrokenStudent> bad = new HashSet<>();
        bad.add(new BrokenStudent("BCS-01"));
        bad.add(new BrokenStudent("BCS-01"));
        System.out.println("HashSet<Student> size       = " + good.size() + "  (duplicates detected)");
        System.out.println("HashSet<BrokenStudent> size = " + bad.size() + "  (equal objects, different hash codes)");
        System.out.println("contains(new Student(BCS-01)) = " + good.contains(new Student("BCS-01", "?")));

        System.out.println("\n== 4. Word frequency with HashMap.merge ==");
        String text = "to be or not to be that is the question to ask";
        Map<String, Integer> freq = new HashMap<>();
        for (String w : text.split(" ")) freq.merge(w, 1, Integer::sum);
        System.out.println("freq(to) = " + freq.get("to") + ", freq(be) = " + freq.get("be")
                + ", freq(why) = " + freq.getOrDefault("why", 0));

        System.out.println("\n== 5. Same entries, three maps ==");
        String[] courses = {"DSA", "OOP", "AI", "DB"};
        Map<String, Integer> hm = new HashMap<>(), lhm = new LinkedHashMap<>(), tm = new TreeMap<>();
        for (int i = 0; i < courses.length; i++) { hm.put(courses[i], i); lhm.put(courses[i], i); tm.put(courses[i], i); }
        System.out.println("HashMap       " + hm.keySet() + "  (order not guaranteed)");
        System.out.println("LinkedHashMap " + lhm.keySet() + "  (insertion order)");
        System.out.println("TreeMap       " + tm.keySet() + "  (sorted)");
    }
}
