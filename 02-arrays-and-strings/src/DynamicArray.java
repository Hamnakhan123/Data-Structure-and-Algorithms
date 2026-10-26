import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Module 02 · Session 01 — A resizable array (our own mini ArrayList).
 *
 * Stores elements in a plain Object[] and DOUBLES its capacity when full, giving
 * amortised O(1) append. It also halves when only a quarter is used, so memory
 * stays proportional to size. Supports get/set/add/insert/remove/indexOf and for-each.
 *
 * Compile:  javac -d out DynamicArray.java
 * Run:      java -cp out DynamicArray
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class DynamicArray<T> implements Iterable<T> {

    private Object[] data;
    private int size;
    private long copies; // total element copies caused by resizing (for analysis)

    public DynamicArray() {
        this(2);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 1) throw new IllegalArgumentException("capacity must be >= 1");
        data = new Object[initialCapacity];
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public int capacity() { return data.length; }
    public long copies() { return copies; }

    /** O(1): direct index. */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    /** O(1): overwrite, returns the old value. */
    public T set(int index, T value) {
        T old = get(index);
        data[index] = value;
        return old;
    }

    /** Amortised O(1): append at the end, doubling capacity when full. */
    public void add(T value) {
        if (size == data.length) resize(2 * data.length);
        data[size++] = value;
    }

    /** O(n): insert at index, shifting elements right. */
    public void insert(int index, T value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("index " + index + ", size " + size);
        if (size == data.length) resize(2 * data.length);
        for (int i = size; i > index; i--) data[i] = data[i - 1]; // shift right, from the back
        data[index] = value;
        size++;
    }

    /** O(n): remove at index, shifting elements left; shrinks when 1/4 full. */
    public T removeAt(int index) {
        T old = get(index);
        for (int i = index; i < size - 1; i++) data[i] = data[i + 1]; // shift left
        data[--size] = null;            // avoid loitering (lets GC reclaim the object)
        if (size > 0 && size == data.length / 4) resize(data.length / 2);
        return old;
    }

    /** O(n): linear search using equals(). Returns -1 if absent. */
    public int indexOf(T value) {
        for (int i = 0; i < size; i++) {
            if (value == null ? data[i] == null : value.equals(data[i])) return i;
        }
        return -1;
    }

    private void resize(int newCapacity) {
        Object[] bigger = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            bigger[i] = data[i];
            copies++;
        }
        data = bigger;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index " + index + ", size " + size);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int i = 0;
            @Override public boolean hasNext() { return i < size; }
            @Override public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return get(i++);
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        System.out.println("=== growth trace (initial capacity 2) ===");
        DynamicArray<Integer> arr = new DynamicArray<>();
        System.out.printf("%-6s %-6s %-9s %-7s%n", "add", "size", "capacity", "copies");
        for (int v = 1; v <= 9; v++) {
            arr.add(v * 10);
            System.out.printf("%-6d %-6d %-9d %-7d%n", v * 10, arr.size(), arr.capacity(), arr.copies());
        }
        System.out.println("contents: " + arr);

        System.out.println("\n=== insert / remove / search ===");
        arr.insert(0, 5);
        System.out.println("insert(0, 5)   -> " + arr);
        arr.insert(5, 45);
        System.out.println("insert(5, 45)  -> " + arr);
        System.out.println("removeAt(2)    -> removed " + arr.removeAt(2) + ", now " + arr);
        System.out.println("indexOf(70)    -> " + arr.indexOf(70));
        System.out.println("indexOf(999)   -> " + arr.indexOf(999));
        arr.set(0, 1);
        int sum = 0;
        for (int v : arr) sum += v;  // uses our iterator
        System.out.println("after set(0,1), sum via for-each = " + sum);

        System.out.println("\n=== shrinking ===");
        while (arr.size() > 1) {
            arr.removeAt(arr.size() - 1);
            if (arr.size() <= 4) System.out.println("size " + arr.size() + " capacity " + arr.capacity());
        }

        System.out.println("\n=== amortised cost of n appends ===");
        for (int n : new int[]{1_000, 100_000, 1_000_000}) {
            DynamicArray<Integer> big = new DynamicArray<>(1);
            for (int i = 0; i < n; i++) big.add(i);
            System.out.printf("n = %,9d  copies = %,9d  copies/n = %.2f%n", n, big.copies(), (double) big.copies() / n);
        }
        try {
            arr.get(5);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("\nget(5) on size " + arr.size() + " -> " + e.getMessage());
        }
    }
}
