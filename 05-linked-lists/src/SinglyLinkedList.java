import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Module 05 · Session 01 — A generic singly linked list built from scratch.
 *
 * Keeps head, tail and size so addFirst, addLast and removeFirst are O(1).
 * Also supports get, insertAt, removeAt, remove(value), indexOf, in-place
 * reverse and for-each iteration.
 *
 * Compile:  javac -d out SinglyLinkedList.java
 * Run:      java -cp out SinglyLinkedList
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class SinglyLinkedList<T> implements Iterable<T> {

    /** A node holds one value and a reference to the next node (null at the end). */
    private static final class Node<T> {
        T value;
        Node<T> next;
        Node(T value, Node<T> next) { this.value = value; this.next = next; }
    }

    private Node<T> head, tail;
    private int size;

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    /** O(1): new node points at the old head. */
    public void addFirst(T value) {
        head = new Node<>(value, head);
        if (tail == null) tail = head;      // list was empty
        size++;
    }

    /** O(1) thanks to the tail reference. */
    public void addLast(T value) {
        Node<T> node = new Node<>(value, null);
        if (tail == null) head = tail = node;
        else { tail.next = node; tail = node; }
        size++;
    }

    /** O(index): walk to the node before the insertion point. */
    public void insertAt(int index, T value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("index " + index + ", size " + size);
        if (index == 0) { addFirst(value); return; }
        if (index == size) { addLast(value); return; }
        Node<T> prev = nodeAt(index - 1);
        prev.next = new Node<>(value, prev.next);   // 1) new node -> successor, 2) prev -> new node
        size++;
    }

    /** O(1). */
    public T removeFirst() {
        if (head == null) throw new NoSuchElementException("list is empty");
        T v = head.value;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return v;
    }

    /** O(index): unlink the node after 'prev'. Removing the LAST node is O(n) in a singly list. */
    public T removeAt(int index) {
        checkIndex(index);
        if (index == 0) return removeFirst();
        Node<T> prev = nodeAt(index - 1);
        Node<T> target = prev.next;
        prev.next = target.next;              // bypass target; the GC reclaims it
        if (target == tail) tail = prev;
        size--;
        return target.value;
    }

    /** Removes the first node equal to value. O(n). Returns true if something was removed. */
    public boolean remove(T value) {
        Node<T> prev = null, cur = head;
        while (cur != null && !java.util.Objects.equals(cur.value, value)) {
            prev = cur;
            cur = cur.next;
        }
        if (cur == null) return false;
        if (prev == null) head = cur.next; else prev.next = cur.next;
        if (cur == tail) tail = prev;
        size--;
        return true;
    }

    public T get(int index) { checkIndex(index); return nodeAt(index).value; }

    public int indexOf(T value) {
        int i = 0;
        for (Node<T> n = head; n != null; n = n.next, i++)
            if (java.util.Objects.equals(n.value, value)) return i;
        return -1;
    }

    /** Reverses the list in place in O(n) time, O(1) space, by flipping every next pointer. */
    public void reverse() {
        Node<T> prev = null, cur = head;
        tail = head;
        while (cur != null) {
            Node<T> next = cur.next;   // 1. remember the rest
            cur.next = prev;           // 2. flip the arrow
            prev = cur;                // 3. advance prev
            cur = next;                // 4. advance cur
        }
        head = prev;
    }

    private Node<T> nodeAt(int index) {
        Node<T> n = head;
        for (int i = 0; i < index; i++) n = n.next;
        return n;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index " + index + ", size " + size);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> cur = head;
            @Override public boolean hasNext() { return cur != null; }
            @Override public T next() {
                if (cur == null) throw new NoSuchElementException();
                T v = cur.value;
                cur = cur.next;
                return v;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("head -> ");
        for (Node<T> n = head; n != null; n = n.next) sb.append(n.value).append(" -> ");
        return sb.append("null").toString();
    }

    public static void main(String[] args) {
        SinglyLinkedList<String> stops = new SinglyLinkedList<>();
        stops.addLast("Saddar");
        stops.addLast("Tower");
        stops.addLast("Clifton");
        System.out.println("addLast x3          : " + stops);
        stops.addFirst("Gulshan");
        System.out.println("addFirst(Gulshan)   : " + stops);
        stops.insertAt(2, "Empress Market");
        System.out.println("insertAt(2, ...)    : " + stops + "  size=" + stops.size());
        System.out.println("get(3) = " + stops.get(3) + ", indexOf(Clifton) = " + stops.indexOf("Clifton"));
        System.out.println("removeAt(1) -> " + stops.removeAt(1) + " : " + stops);
        System.out.println("remove(Tower) -> " + stops.remove("Tower") + " : " + stops);
        System.out.println("remove(Lyari) -> " + stops.remove("Lyari"));
        stops.reverse();
        System.out.println("reverse()           : " + stops);
        stops.addLast("Seaview");
        System.out.println("addLast after reverse (tail still correct): " + stops);
        StringBuilder upper = new StringBuilder();
        for (String s : stops) upper.append(s.toUpperCase()).append(' ');
        System.out.println("for-each            : " + upper.toString().trim());
        while (!stops.isEmpty()) stops.removeFirst();
        System.out.println("after removing all  : " + stops + "  size=" + stops.size());
        try {
            stops.removeFirst();
        } catch (NoSuchElementException e) {
            System.out.println("removeFirst on empty -> " + e.getMessage());
        }
    }
}
