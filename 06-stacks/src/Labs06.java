import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Module 06 — reference solutions for the lab tasks of Sessions 1-3.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out Labs06.java
 * Run:      java -cp out Labs06 1    (Session 1 labs; other numbers for other sessions; no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs06 {

    // >>> decimalToBinary
    /** Repeated division by 2 produces bits in reverse; a stack puts them back in order. */
    static String toBinary(int n) {
        if (n == 0) return "0";
        Deque<Integer> st = new ArrayDeque<>();
        while (n > 0) { st.push(n % 2); n /= 2; }
        StringBuilder sb = new StringBuilder();
        while (!st.isEmpty()) sb.append(st.pop());
        return sb.toString();
    }
    // <<< decimalToBinary

    // >>> twoStacksQueue
    /** A queue built from two stacks: amortised O(1) per operation. */
    static final class QueueFromStacks<T> {
        private final Deque<T> in = new ArrayDeque<>(), out = new ArrayDeque<>();
        void enqueue(T x) { in.push(x); }
        T dequeue() {
            if (out.isEmpty()) while (!in.isEmpty()) out.push(in.pop());   // reverse once
            return out.pop();
        }
        boolean isEmpty() { return in.isEmpty() && out.isEmpty(); }
    }
    // <<< twoStacksQueue

    // >>> simplifyPath
    /** Simplify a Unix path: "/a/./b/../../c/" -> "/c". '..' pops a directory. */
    static String simplifyPath(String path) {
        Deque<String> st = new ArrayDeque<>();
        for (String part : path.split("/")) {
            if (part.isEmpty() || part.equals(".")) continue;
            if (part.equals("..")) { if (!st.isEmpty()) st.pop(); }
            else st.push(part);
        }
        StringBuilder sb = new StringBuilder();
        while (!st.isEmpty()) sb.append('/').append(st.pollLast());     // bottom of the stack first
        return sb.length() == 0 ? "/" : sb.toString();
    }
    // <<< simplifyPath

    // >>> htmlTags
    /** Checks that simple tags like <b>, </b> are properly nested. */
    static boolean tagsBalanced(String html) {
        Deque<String> st = new ArrayDeque<>();
        int i = 0;
        while ((i = html.indexOf('<', i)) != -1) {
            int j = html.indexOf('>', i);
            if (j == -1) return false;
            String tag = html.substring(i + 1, j);
            if (tag.startsWith("/")) {
                if (st.isEmpty() || !st.pop().equals(tag.substring(1))) return false;
            } else st.push(tag);
            i = j + 1;
        }
        return st.isEmpty();
    }
    // <<< htmlTags

    // >>> removeKDigits
    /** Remove k digits to make the smallest number: keep an increasing (monotonic) stack of digits. */
    static String removeKDigits(String num, int k) {
        StringBuilder st = new StringBuilder();              // used as a stack of chars
        for (char c : num.toCharArray()) {
            while (k > 0 && st.length() > 0 && st.charAt(st.length() - 1) > c) {
                st.deleteCharAt(st.length() - 1);
                k--;
            }
            st.append(c);
        }
        st.setLength(st.length() - k);                       // still need removals: drop from the end
        int z = 0;
        while (z < st.length() - 1 && st.charAt(z) == '0') z++;  // strip leading zeros
        return st.length() == 0 ? "0" : st.substring(z);
    }
    // <<< removeKDigits

    public static void main(String[] args) {
        int session = args.length > 0 ? Integer.parseInt(args[0]) : 0;   // 0 = all sessions
        if (session == 0 || session == 1) {
            System.out.println("-- Session 1 labs --");
            System.out.println("toBinary(37) = " + toBinary(37) + ", toBinary(0) = " + toBinary(0));
            QueueFromStacks<String> q = new QueueFromStacks<>();
            q.enqueue("a"); q.enqueue("b"); q.enqueue("c");
            System.out.print("queue from two stacks: " + q.dequeue() + " " + q.dequeue());
            q.enqueue("d");
            System.out.println(" " + q.dequeue() + " " + q.dequeue());
            System.out.println("simplifyPath(\"/home/./hamna/../dsa//stacks/\") = " + simplifyPath("/home/./hamna/../dsa//stacks/"));
        }
        if (session == 0 || session == 2) {
            System.out.println("-- Session 2 labs --");
            System.out.println("tagsBalanced(<p><b>hi</b></p>) = " + tagsBalanced("<p><b>hi</b></p>")
                    + ", (<p><b>hi</p></b>) = " + tagsBalanced("<p><b>hi</p></b>"));
        }
        if (session == 0 || session == 3) {
            System.out.println("-- Session 3 labs --");
            System.out.println("removeKDigits(\"1432219\", 3) = " + removeKDigits("1432219", 3)
                    + ", (\"10200\", 1) = " + removeKDigits("10200", 1));
        }
    }
}
