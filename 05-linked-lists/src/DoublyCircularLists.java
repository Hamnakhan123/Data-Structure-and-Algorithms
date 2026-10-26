import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Module 05 · Session 02 — Doubly linked lists and circular linked lists.
 *
 * DoublyList<T> uses two sentinel (dummy) nodes so every insert/remove is the
 * same four-pointer operation with no special cases; it supports O(1) work at
 * both ends and O(1) removal of a known node. CircularList<T> keeps only a tail
 * reference (tail.next is the head) and powers a round-robin scheduler and the
 * Josephus problem.
 *
 * Compile:  javac -d out DoublyCircularLists.java
 * Run:      java -cp out DoublyCircularLists
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class DoublyCircularLists {

    // =============================================================== doubly linked list
    static final class DoublyList<T> {
        static final class Node<T> {
            T value;
            Node<T> prev, next;
            Node(T value) { this.value = value; }
        }

        private final Node<T> header = new Node<>(null);   // sentinel before the first element
        private final Node<T> trailer = new Node<>(null);  // sentinel after the last element
        private int size;

        DoublyList() {
            header.next = trailer;
            trailer.prev = header;
        }

        int size() { return size; }
        boolean isEmpty() { return size == 0; }

        /** Links a new node between a and b (which must be adjacent). O(1). */
        private Node<T> addBetween(T value, Node<T> a, Node<T> b) {
            Node<T> n = new Node<>(value);
            n.prev = a;
            n.next = b;
            a.next = n;
            b.prev = n;
            size++;
            return n;
        }

        /** Unlinks node n. O(1) — the big advantage over a singly linked list. */
        T unlink(Node<T> n) {
            n.prev.next = n.next;
            n.next.prev = n.prev;
            n.prev = n.next = null;   // help the GC and catch misuse
            size--;
            return n.value;
        }

        Node<T> addFirst(T v) { return addBetween(v, header, header.next); }
        Node<T> addLast(T v) { return addBetween(v, trailer.prev, trailer); }
        Node<T> insertAfter(Node<T> n, T v) { return addBetween(v, n, n.next); }

        T removeFirst() {
            if (isEmpty()) throw new NoSuchElementException();
            return unlink(header.next);
        }

        T removeLast() {
            if (isEmpty()) throw new NoSuchElementException();
            return unlink(trailer.prev);
        }

        Node<T> first() { return isEmpty() ? null : header.next; }

        String forward() {
            StringBuilder sb = new StringBuilder("null <-> ");
            for (Node<T> n = header.next; n != trailer; n = n.next) sb.append(n.value).append(" <-> ");
            return sb.append("null").toString();
        }

        String backward() {
            StringBuilder sb = new StringBuilder();
            for (Node<T> n = trailer.prev; n != header; n = n.prev) sb.append(n.value).append(n.prev == header ? "" : " , ");
            return "[" + sb + "]";
        }
    }

    // =============================================================== circular linked list
    static final class CircularList<T> {
        static final class Node<T> {
            T value;
            Node<T> next;
            Node(T value) { this.value = value; }
        }

        private Node<T> tail;   // tail.next is the head
        private int size;

        int size() { return size; }
        boolean isEmpty() { return size == 0; }

        void addLast(T v) {
            Node<T> n = new Node<>(v);
            if (tail == null) { n.next = n; tail = n; }      // single node points to itself
            else { n.next = tail.next; tail.next = n; tail = n; }
            size++;
        }

        /** Moves the head to the back: O(1). This is exactly one round-robin "tick". */
        void rotate() { if (tail != null) tail = tail.next; }

        T head() { if (tail == null) throw new NoSuchElementException(); return tail.next.value; }

        T removeHead() {
            if (tail == null) throw new NoSuchElementException();
            Node<T> h = tail.next;
            if (h == tail) tail = null;                      // removing the only node
            else tail.next = h.next;
            size--;
            return h.value;
        }

        @Override public String toString() {
            if (tail == null) return "(empty)";
            StringBuilder sb = new StringBuilder();
            Node<T> n = tail.next;
            do { sb.append(n.value).append(" -> "); n = n.next; } while (n != tail.next);
            return sb.append("(back to ").append(tail.next.value).append(")").toString();
        }
    }

    /** Round-robin CPU scheduling: each job gets 'quantum' ms per turn until finished. */
    static List<String> roundRobin(String[] jobs, int[] burst, int quantum) {
        CircularList<int[]> ready = new CircularList<>();   // {jobIndex, remainingTime}
        for (int i = 0; i < jobs.length; i++) ready.addLast(new int[]{i, burst[i]});
        List<String> finished = new ArrayList<>();
        int clock = 0;
        while (!ready.isEmpty()) {
            int[] job = ready.head();
            int run = Math.min(quantum, job[1]);
            clock += run;
            job[1] -= run;
            if (job[1] == 0) {
                ready.removeHead();
                finished.add(jobs[job[0]] + "@" + clock);
            } else {
                ready.rotate();                              // back of the queue
            }
        }
        return finished;
    }

    /** Josephus: n people in a circle, every k-th is eliminated; returns the elimination order. */
    static List<Integer> josephus(int n, int k) {
        CircularList<Integer> circle = new CircularList<>();
        for (int i = 1; i <= n; i++) circle.addLast(i);
        List<Integer> order = new ArrayList<>();
        while (!circle.isEmpty()) {
            for (int step = 1; step < k; step++) circle.rotate();
            order.add(circle.removeHead());
        }
        return order;
    }

    public static void main(String[] args) {
        System.out.println("=== Doubly linked list (browser history) ===");
        DoublyList<String> history = new DoublyList<>();
        history.addLast("google.com");
        DoublyList.Node<String> wiki = history.addLast("wikipedia.org");
        history.addLast("github.com");
        history.addFirst("home");
        System.out.println("forward : " + history.forward());
        System.out.println("backward: " + history.backward());
        history.insertAfter(wiki, "en.wikipedia.org/wiki/Linked_list");
        System.out.println("insertAfter(wiki): " + history.forward());
        System.out.println("unlink(wiki) in O(1) -> " + history.unlink(wiki));
        System.out.println("removeFirst -> " + history.removeFirst() + ", removeLast -> " + history.removeLast());
        System.out.println("now: " + history.forward() + "  size=" + history.size());

        System.out.println("\n=== Circular linked list ===");
        CircularList<String> players = new CircularList<>();
        for (String p : new String[]{"Ali", "Bano", "Chand", "Daud"}) players.addLast(p);
        System.out.println("players: " + players);
        players.rotate();
        System.out.println("after one turn: " + players);

        System.out.println("\nRound robin (quantum 3 ms), bursts P1=5 P2=3 P3=8 P4=2:");
        System.out.println("  finish order @time: " + roundRobin(new String[]{"P1", "P2", "P3", "P4"}, new int[]{5, 3, 8, 2}, 3));
        System.out.println("\nJosephus n=7, k=3 elimination order: " + josephus(7, 3));
        List<Integer> j = josephus(41, 3);
        System.out.println("Josephus n=41, k=3 survivor: " + j.get(j.size() - 1));
    }
}
