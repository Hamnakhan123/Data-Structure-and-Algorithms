import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EmptyStackException;

/**
 * Module 06 · Session 01 — The stack ADT with array and linked implementations.
 *
 * Defines a small Stack<T> interface (push, pop, peek, isEmpty, size) and two
 * implementations: ArrayStack (resizable array, amortised O(1)) and LinkedStack
 * (singly linked nodes, worst-case O(1)). The demo also simulates browser
 * "Back", an undo history, and shows Java's recommended ArrayDeque.
 *
 * Compile:  javac -d out StackImplementations.java
 * Run:      java -cp out StackImplementations
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class StackImplementations {

    /** The stack abstract data type: Last In, First Out. */
    interface Stack<T> {
        void push(T x);
        T pop();          // removes and returns the top; throws if empty
        T peek();         // returns the top without removing it
        boolean isEmpty();
        int size();
    }

    /** Array-based stack: top is at index size-1; doubles when full, halves at 1/4. */
    static final class ArrayStack<T> implements Stack<T> {
        private Object[] data = new Object[2];
        private int size;

        @Override public void push(T x) {
            if (size == data.length) resize(2 * data.length);
            data[size++] = x;
        }

        @Override @SuppressWarnings("unchecked")
        public T pop() {
            if (size == 0) throw new EmptyStackException();
            T x = (T) data[--size];
            data[size] = null;                         // avoid loitering
            if (size > 0 && size == data.length / 4) resize(data.length / 2);
            return x;
        }

        @Override @SuppressWarnings("unchecked")
        public T peek() {
            if (size == 0) throw new EmptyStackException();
            return (T) data[size - 1];
        }

        @Override public boolean isEmpty() { return size == 0; }
        @Override public int size() { return size; }
        int capacity() { return data.length; }

        private void resize(int cap) {
            Object[] d = new Object[cap];
            System.arraycopy(data, 0, d, 0, size);
            data = d;
        }

        @Override public String toString() {
            StringBuilder sb = new StringBuilder("[bottom");
            for (int i = 0; i < size; i++) sb.append(' ').append(data[i]);
            return sb.append(" <- top]").toString();
        }
    }

    /** Linked stack: top is the head of a singly linked list. Every operation is worst-case O(1). */
    static final class LinkedStack<T> implements Stack<T> {
        private static final class Node<T> {
            final T value;
            final Node<T> next;
            Node(T value, Node<T> next) { this.value = value; this.next = next; }
        }

        private Node<T> top;
        private int size;

        @Override public void push(T x) { top = new Node<>(x, top); size++; }

        @Override public T pop() {
            if (top == null) throw new EmptyStackException();
            T x = top.value;
            top = top.next;
            size--;
            return x;
        }

        @Override public T peek() {
            if (top == null) throw new EmptyStackException();
            return top.value;
        }

        @Override public boolean isEmpty() { return top == null; }
        @Override public int size() { return size; }

        @Override public String toString() {
            StringBuilder sb = new StringBuilder("[top ->");
            for (Node<T> n = top; n != null; n = n.next) sb.append(' ').append(n.value);
            return sb.append("]").toString();
        }
    }

    /** Reverses a string using any Stack implementation. */
    static String reverse(String s, Stack<Character> st) {
        for (char c : s.toCharArray()) st.push(c);
        StringBuilder sb = new StringBuilder();
        while (!st.isEmpty()) sb.append(st.pop());
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== ArrayStack ===");
        ArrayStack<Integer> a = new ArrayStack<>();
        for (int i = 1; i <= 5; i++) {
            a.push(i * 10);
            System.out.println("push " + (i * 10) + " -> " + a + "  capacity " + a.capacity());
        }
        System.out.println("peek = " + a.peek());
        while (a.size() > 1) System.out.println("pop  " + a.pop() + " -> " + a + "  capacity " + a.capacity());

        System.out.println("\n=== LinkedStack ===");
        LinkedStack<String> l = new LinkedStack<>();
        l.push("A"); l.push("B"); l.push("C");
        System.out.println(l + " size " + l.size() + ", pop -> " + l.pop() + ", now " + l);

        System.out.println("\nreverse(\"stack\") with ArrayStack  = " + reverse("stack", new ArrayStack<>()));
        System.out.println("reverse(\"LIFO!\") with LinkedStack = " + reverse("LIFO!", new LinkedStack<>()));

        System.out.println("\n=== Browser back button ===");
        LinkedStack<String> back = new LinkedStack<>();
        String current = "home";
        for (String page : new String[]{"courses", "dsa", "stacks"}) {
            back.push(current);
            current = page;
            System.out.println("visit " + page + "   back stack " + back);
        }
        for (int i = 0; i < 2; i++) {
            current = back.pop();
            System.out.println("Back -> now on " + current + "   back stack " + back);
        }

        System.out.println("\n=== Undo in a text box ===");
        ArrayStack<String> undo = new ArrayStack<>();
        StringBuilder text = new StringBuilder();
        for (String w : new String[]{"Data ", "Structures ", "in ", "Java"}) {
            undo.push(text.toString());
            text.append(w);
            System.out.println("type \"" + w.trim() + "\" -> \"" + text + "\"");
        }
        text = new StringBuilder(undo.pop());
        System.out.println("undo -> \"" + text + "\"");
        text = new StringBuilder(undo.pop());
        System.out.println("undo -> \"" + text + "\"");

        System.out.println("\n=== Java's recommended stack: ArrayDeque ===");
        Deque<Integer> dq = new ArrayDeque<>();
        dq.push(1); dq.push(2); dq.push(3);
        System.out.println("ArrayDeque " + dq + " pop -> " + dq.pop() + " peek -> " + dq.peek());
        try {
            new ArrayStack<Integer>().pop();
        } catch (EmptyStackException e) {
            System.out.println("pop on empty stack -> EmptyStackException (stack underflow)");
        }
    }
}
