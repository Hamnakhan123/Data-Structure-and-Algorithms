import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Module 07 · Session 01 — The queue ADT: circular array and linked implementations.
 *
 * CircularQueue<T> stores elements in a fixed array with wrap-around front/rear
 * indices (and grows when full); LinkedQueue<T> enqueues at the tail and dequeues
 * at the head. The demo prints the internal array to show wrap-around, then runs
 * a bank-counter simulation that measures average waiting time.
 *
 * Compile:  javac -d out QueueImplementations.java
 * Run:      java -cp out QueueImplementations
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class QueueImplementations {

    /** Array-based FIFO queue with wrap-around indices. */
    static final class CircularQueue<T> {
        private Object[] data;
        private int front;   // index of the first element
        private int size;

        CircularQueue(int capacity) { data = new Object[capacity]; }

        boolean isEmpty() { return size == 0; }
        int size() { return size; }

        /** rear position = (front + size) % capacity. Amortised O(1). */
        void enqueue(T x) {
            if (size == data.length) grow();
            data[(front + size) % data.length] = x;
            size++;
        }

        /** O(1): take from front, advance front with wrap-around. */
        @SuppressWarnings("unchecked")
        T dequeue() {
            if (size == 0) throw new NoSuchElementException("queue underflow");
            T x = (T) data[front];
            data[front] = null;
            front = (front + 1) % data.length;
            size--;
            return x;
        }

        @SuppressWarnings("unchecked")
        T peek() {
            if (size == 0) throw new NoSuchElementException("queue is empty");
            return (T) data[front];
        }

        /** Copies elements in queue order into a twice-as-large array, front at index 0. */
        private void grow() {
            Object[] bigger = new Object[data.length * 2];
            for (int i = 0; i < size; i++) bigger[i] = data[(front + i) % data.length];
            data = bigger;
            front = 0;
        }

        String internals() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < data.length; i++) {
                sb.append(data[i] == null ? "_" : data[i]);
                if (i < data.length - 1) sb.append(' ');
            }
            int rear = (front + size - 1 + data.length) % data.length;
            return sb.append("] front=").append(front).append(" rear=").append(size == 0 ? "-" : String.valueOf(rear))
                    .append(" size=").append(size).toString();
        }
    }

    /** Linked FIFO queue: enqueue at tail, dequeue at head, both O(1) worst case. */
    static final class LinkedQueue<T> {
        private static final class Node<T> {
            T value;
            Node<T> next;
            Node(T value) { this.value = value; }
        }

        private Node<T> head, tail;
        private int size;

        void enqueue(T x) {
            Node<T> n = new Node<>(x);
            if (tail == null) head = tail = n;
            else { tail.next = n; tail = n; }
            size++;
        }

        T dequeue() {
            if (head == null) throw new NoSuchElementException("queue underflow");
            T x = head.value;
            head = head.next;
            if (head == null) tail = null;   // queue became empty
            size--;
            return x;
        }

        boolean isEmpty() { return head == null; }
        int size() { return size; }
    }

    /** Single-counter bank: customers arrive at given minutes and need given service minutes. */
    static double averageWait(int[] arrival, int[] service) {
        LinkedQueue<int[]> line = new LinkedQueue<>();
        for (int i = 0; i < arrival.length; i++) line.enqueue(new int[]{arrival[i], service[i]});
        int clock = 0;
        long totalWait = 0;
        while (!line.isEmpty()) {
            int[] c = line.dequeue();
            clock = Math.max(clock, c[0]);        // counter idles until the customer arrives
            totalWait += clock - c[0];
            clock += c[1];
        }
        return (double) totalWait / arrival.length;
    }

    public static void main(String[] args) {
        System.out.println("=== CircularQueue (capacity 4) ===");
        CircularQueue<Integer> q = new CircularQueue<>(4);
        q.enqueue(10); System.out.println("enqueue 10 -> " + q.internals());
        q.enqueue(20); System.out.println("enqueue 20 -> " + q.internals());
        q.enqueue(30); System.out.println("enqueue 30 -> " + q.internals());
        System.out.println("dequeue " + q.dequeue() + " -> " + q.internals());
        System.out.println("dequeue " + q.dequeue() + " -> " + q.internals());
        q.enqueue(40); System.out.println("enqueue 40 -> " + q.internals());
        q.enqueue(50); System.out.println("enqueue 50 -> " + q.internals() + "   <- wrapped around");
        q.enqueue(60); System.out.println("enqueue 60 -> " + q.internals());
        q.enqueue(70); System.out.println("enqueue 70 -> " + q.internals() + "   <- grew, unwrapped");
        StringBuilder order = new StringBuilder();
        while (!q.isEmpty()) order.append(q.dequeue()).append(' ');
        System.out.println("dequeue all: " + order.toString().trim() + "  (FIFO order preserved)");

        System.out.println("\n=== LinkedQueue: print jobs ===");
        LinkedQueue<String> printer = new LinkedQueue<>();
        for (String job : new String[]{"assignment.pdf", "timetable.xlsx", "notes.docx"}) printer.enqueue(job);
        while (!printer.isEmpty()) System.out.println("printing " + printer.dequeue() + " (" + printer.size() + " waiting)");

        System.out.println("\n=== Bank counter simulation ===");
        int[] arrive = {0, 1, 2, 6, 7, 15};
        int[] serve = {4, 3, 2, 5, 1, 2};
        System.out.printf("average wait = %.2f minutes%n", averageWait(arrive, serve));

        Queue<String> lib = new ArrayDeque<>();
        lib.offer("first"); lib.offer("second");
        System.out.println("\njava.util.Queue via ArrayDeque: poll -> " + lib.poll() + ", peek -> " + lib.peek()
                + ", poll -> " + lib.poll() + ", poll on empty -> " + lib.poll() + " (no exception)");
    }
}
