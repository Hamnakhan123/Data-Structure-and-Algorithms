import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Module 01 · Session 01 — Java refresher for Data Structures.
 *
 * Demonstrates the Java features we lean on for the rest of the course:
 *   1. primitive vs reference types (and why "==" can surprise you)
 *   2. arrays and their fixed size
 *   3. generic classes (Pair<K, V>) and generic methods (maxOf)
 *   4. interfaces: Comparable (natural order) and Comparator (custom order)
 *   5. ArrayList, autoboxing and the enhanced for-loop
 *
 * Compile:  javac -d out JavaRefresher.java
 * Run:      java -cp out JavaRefresher
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class JavaRefresher {

    /** A tiny immutable generic pair: the "two-slot box" we use for (key, value) results. */
    static final class Pair<K, V> {
        private final K first;
        private final V second;

        Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }

        K first() { return first; }
        V second() { return second; }

        @Override
        public String toString() { return "(" + first + ", " + second + ")"; }
    }

    /** A student record with a natural ordering by roll number. */
    static final class Student implements Comparable<Student> {
        final int roll;
        final String name;
        final double cgpa;

        Student(int roll, String name, double cgpa) {
            this.roll = roll;
            this.name = name;
            this.cgpa = cgpa;
        }

        /** Natural order: ascending roll number. Negative = this comes first. */
        @Override
        public int compareTo(Student other) {
            return Integer.compare(this.roll, other.roll);
        }

        @Override
        public String toString() { return roll + ":" + name + "(" + cgpa + ")"; }
    }

    /**
     * Generic method: returns the largest element of a non-empty array.
     * The bound <T extends Comparable<T>> guarantees compareTo exists.
     */
    static <T extends Comparable<T>> T maxOf(T[] items) {
        if (items.length == 0) throw new IllegalArgumentException("empty array");
        T best = items[0];
        for (T item : items) {
            if (item.compareTo(best) > 0) best = item;
        }
        return best;
    }

    /** Returns (min, max) of an int array in ONE pass, packed in a Pair. */
    static Pair<Integer, Integer> minMax(int[] a) {
        int min = a[0], max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (a[i] < min) min = a[i];
            if (a[i] > max) max = a[i];
        }
        return new Pair<>(min, max);
    }

    /** Arrays are objects: a method receiving an array can change the caller's data. */
    static void doubleAll(int[] a) {
        for (int i = 0; i < a.length; i++) a[i] *= 2;
    }

    /** Primitives are copied: the caller's variable is NOT changed. */
    static void tryToChange(int x) {
        x = 999;
    }

    public static void main(String[] args) {
        System.out.println("=== 1. Primitive vs reference ===");
        int x = 5;
        tryToChange(x);
        System.out.println("x after tryToChange(x): " + x);
        int[] marks = {10, 20, 30};
        doubleAll(marks);
        System.out.println("marks after doubleAll: " + Arrays.toString(marks));

        String s1 = new String("dsa");
        String s2 = new String("dsa");
        System.out.println("s1 == s2      -> " + (s1 == s2) + "   (different objects)");
        System.out.println("s1.equals(s2) -> " + s1.equals(s2) + "    (same content)");

        System.out.println("\n=== 2. Generics ===");
        Integer[] nums = {42, 7, 99, 15};
        String[] words = {"queue", "array", "tree", "stack"};
        System.out.println("maxOf(nums)  = " + maxOf(nums));
        System.out.println("maxOf(words) = " + maxOf(words));
        System.out.println("minMax({8,3,11,-2,6}) = " + minMax(new int[]{8, 3, 11, -2, 6}));

        System.out.println("\n=== 3. Comparable and Comparator ===");
        List<Student> cls = new ArrayList<>();
        cls.add(new Student(103, "Ayesha", 3.4));
        cls.add(new Student(101, "Bilal", 3.9));
        cls.add(new Student(104, "Sana", 3.9));
        cls.add(new Student(102, "Danish", 2.8));

        List<Student> byRoll = new ArrayList<>(cls);
        byRoll.sort(null); // null comparator = natural order (compareTo)
        System.out.println("by roll           : " + byRoll);

        List<Student> byCgpa = new ArrayList<>(cls);
        byCgpa.sort(Comparator.comparingDouble((Student st) -> st.cgpa).reversed()
                .thenComparing(st -> st.name));
        System.out.println("by cgpa desc, name: " + byCgpa);
        System.out.println("top student       : " + maxOf(cls.toArray(new Student[0])) + " (max by natural order = highest roll)");

        System.out.println("\n=== 4. ArrayList and autoboxing ===");
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) list.add(i * i); // int -> Integer (autoboxing)
        int sum = 0;
        for (int v : list) sum += v;                  // Integer -> int (unboxing)
        System.out.println("squares = " + list + ", sum = " + sum);
        list.remove(Integer.valueOf(9)); // remove the VALUE 9
        list.remove(0);                  // remove the element at INDEX 0
        System.out.println("after remove(value 9) and remove(index 0): " + list);

        Integer big1 = 1000, big2 = 1000;
        System.out.println("Integer 1000 == 1000 ? " + (big1 == big2) + "  -> always use equals() for objects");
    }
}
