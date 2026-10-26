import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Session 1 demo: an array-based binary heap (min-heap by default, any Comparator) with
 * insert/sift-up, extract/sift-down, O(n) bottom-up heapify, and a comparison of the
 * one-by-one and bottom-up build costs.
 *
 * Compile:  javac -d out BinaryHeap.java
 * Run:      java -cp out BinaryHeap
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class BinaryHeap<T> {
    private Object[] a;
    private int size;
    private final Comparator<? super T> cmp;
    long comparisons;                                   // for measuring build costs

    BinaryHeap(Comparator<? super T> cmp) { this.cmp = cmp; a = new Object[8]; }

    static <E extends Comparable<? super E>> BinaryHeap<E> minHeap() { return new BinaryHeap<E>(Comparator.naturalOrder()); }

    // index arithmetic for a complete tree stored level by level
    static int parent(int i) { return (i - 1) / 2; }
    static int left(int i) { return 2 * i + 1; }
    static int right(int i) { return 2 * i + 2; }

    @SuppressWarnings("unchecked")
    private T at(int i) { return (T) a[i]; }

    private boolean less(int i, int j) { comparisons++; return cmp.compare(at(i), at(j)) < 0; }

    private void swap(int i, int j) { Object t = a[i]; a[i] = a[j]; a[j] = t; }

    /** Add at the end (keeps the tree complete), then bubble up while smaller than the parent. */
    void insert(T x) {
        if (size == a.length) a = Arrays.copyOf(a, 2 * size);
        a[size] = x;
        siftUp(size++);
    }

    private void siftUp(int i) {
        while (i > 0 && less(i, parent(i))) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    T peek() {
        if (size == 0) throw new NoSuchElementException("heap is empty");
        return at(0);
    }

    /** Remove the root: move the last element to the root, then sink it down. */
    T extract() {
        T top = peek();
        a[0] = a[--size];
        a[size] = null;                     // let the GC reclaim it
        if (size > 0) siftDown(0);
        return top;
    }

    private void siftDown(int i) {
        while (left(i) < size) {
            int c = left(i);
            if (right(i) < size && less(right(i), c)) c = right(i);    // smaller child
            if (!less(c, i)) break;                                    // heap order restored
            swap(i, c);
            i = c;
        }
    }

    /** Floyd's bottom-up heapify: sift down every internal node from the last one to the root. O(n). */
    static <E> BinaryHeap<E> heapify(E[] items, Comparator<? super E> cmp) {
        BinaryHeap<E> h = new BinaryHeap<>(cmp);
        h.a = Arrays.copyOf(items, Math.max(8, items.length), Object[].class);
        h.size = items.length;
        for (int i = parent(h.size - 1); i >= 0; i--) h.siftDown(i);
        return h;
    }

    int size() { return size; }
    boolean isEmpty() { return size == 0; }

    boolean isHeap() {
        for (int i = 1; i < size; i++) if (cmp.compare(at(i), at(parent(i))) < 0) return false;
        return true;
    }

    String array() { return Arrays.toString(Arrays.copyOf(a, size)); }

    public static void main(String[] args) {
        System.out.println("== Insert 50 30 40 10 20 5 into a min-heap ==");
        BinaryHeap<Integer> h = minHeap();
        for (int x : new int[]{50, 30, 40, 10, 20, 5}) {
            h.insert(x);
            System.out.printf("insert %-2d -> %s%n", x, h.array());
        }
        System.out.println("parent(5) = " + parent(5) + ", children of 1 = " + left(1) + " and " + right(1));

        System.out.println("\n== Extract-min three times ==");
        for (int k = 0; k < 3; k++) {
            int m = h.extract();
            System.out.printf("extract %-2d -> %s%n", m, h.array());
        }

        System.out.println("\n== Max-heap with a reversed comparator ==");
        BinaryHeap<String> names = new BinaryHeap<>(Comparator.<String>reverseOrder());
        for (String s : new String[]{"Hira", "Ali", "Zoya", "Bilal", "Sana"}) names.insert(s);
        StringBuilder sb = new StringBuilder();
        while (!names.isEmpty()) sb.append(names.extract()).append(' ');
        System.out.println("extract order: " + sb.toString().trim());

        System.out.println("\n== Bottom-up heapify of [9, 4, 7, 1, 8, 2, 6, 3, 5] ==");
        Integer[] data = {9, 4, 7, 1, 8, 2, 6, 3, 5};
        BinaryHeap<Integer> b = heapify(data, Comparator.naturalOrder());
        System.out.println("heap array = " + b.array() + ", valid = " + b.isHeap());

        System.out.println("\n== Build cost: n inserts vs bottom-up heapify (comparisons) ==");
        System.out.println("        n   n inserts   heapify   heapify/n");
        for (int n : new int[]{1_000, 100_000, 1_000_000}) {
            Integer[] keys = new Integer[n];
            for (int i = 0; i < n; i++) keys[i] = n - i;                 // descending: worst case for inserts
            BinaryHeap<Integer> one = minHeap();
            for (Integer k : keys) one.insert(k);
            BinaryHeap<Integer> bu = heapify(keys, Comparator.naturalOrder());
            System.out.printf("%9d   %9d   %7d   %9.2f%n", n, one.comparisons, bu.comparisons, (double) bu.comparisons / n);
        }
    }
}
