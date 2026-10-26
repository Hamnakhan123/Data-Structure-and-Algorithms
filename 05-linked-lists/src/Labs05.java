/**
 * Module 05 — reference solutions for the lab tasks of Sessions 1-3.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 * Uses LinkedListProblems.ListNode for the node type.
 *
 * Compile:  javac -d out Labs05.java LinkedListProblems.java
 * Run:      java -cp out Labs05 1    (Session 1 labs; other numbers for other sessions; no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs05 {
    // short alias for readability
    static LinkedListProblems.ListNode node(int v) { return new LinkedListProblems.ListNode(v); }

    // >>> lengthRec
    /** Length of a list, recursively. */
    static int length(LinkedListProblems.ListNode h) {
        return h == null ? 0 : 1 + length(h.next);
    }
    // <<< lengthRec

    // >>> sumList
    /** Sum of all values, iteratively. */
    static int sum(LinkedListProblems.ListNode h) {
        int s = 0;
        for (LinkedListProblems.ListNode p = h; p != null; p = p.next) s += p.val;
        return s;
    }
    // <<< sumList

    // >>> dedupSortedList
    /** Remove duplicates from a SORTED list in place: skip a node equal to its predecessor. */
    static LinkedListProblems.ListNode dedup(LinkedListProblems.ListNode h) {
        LinkedListProblems.ListNode cur = h;
        while (cur != null && cur.next != null) {
            if (cur.next.val == cur.val) cur.next = cur.next.next;   // unlink the duplicate
            else cur = cur.next;
        }
        return h;
    }
    // <<< dedupSortedList

    // >>> insertSorted
    /** Insert a value into a sorted list, keeping it sorted. Returns the (possibly new) head. */
    static LinkedListProblems.ListNode insertSorted(LinkedListProblems.ListNode h, int v) {
        LinkedListProblems.ListNode n = node(v);
        if (h == null || v <= h.val) { n.next = h; return n; }
        LinkedListProblems.ListNode p = h;
        while (p.next != null && p.next.val < v) p = p.next;
        n.next = p.next;
        p.next = n;
        return h;
    }
    // <<< insertSorted

    // >>> addNumbers
    /** Add two numbers stored as digit lists in REVERSE order: (2->4->3) + (5->6->4) = 7->0->8 (342 + 465 = 807). */
    static LinkedListProblems.ListNode addNumbers(LinkedListProblems.ListNode a, LinkedListProblems.ListNode b) {
        LinkedListProblems.ListNode dummy = node(0), t = dummy;
        int carry = 0;
        while (a != null || b != null || carry != 0) {
            int s = carry + (a != null ? a.val : 0) + (b != null ? b.val : 0);
            t.next = node(s % 10);
            t = t.next;
            carry = s / 10;
            if (a != null) a = a.next;
            if (b != null) b = b.next;
        }
        return dummy.next;
    }
    // <<< addNumbers

    // >>> oddEven
    /** Group odd-POSITION nodes before even-position nodes, O(n) time, O(1) space. */
    static LinkedListProblems.ListNode oddEven(LinkedListProblems.ListNode h) {
        if (h == null) return null;
        LinkedListProblems.ListNode odd = h, even = h.next, evenHead = even;
        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = odd.next;
            even.next = odd.next;
            even = even.next;
        }
        odd.next = evenHead;
        return h;
    }
    // <<< oddEven

    public static void main(String[] args) {
        int session = args.length > 0 ? Integer.parseInt(args[0]) : 0;   // 0 = all sessions
        if (session == 0 || session == 1) {
            System.out.println("-- Session 1 labs --");
            LinkedListProblems.ListNode l = LinkedListProblems.build(4, 8, 15, 16, 23, 42);
            System.out.println("length = " + length(l) + ", sum = " + sum(l));
            System.out.println("dedup(1 1 2 3 3 3 4) = " + LinkedListProblems.show(dedup(LinkedListProblems.build(1, 1, 2, 3, 3, 3, 4))));
            LinkedListProblems.ListNode s = LinkedListProblems.build(10, 20, 40);
            s = insertSorted(s, 30);
            s = insertSorted(s, 5);
            System.out.println("insertSorted 30, 5 = " + LinkedListProblems.show(s));
        }
        if (session == 0 || session == 3) {
            System.out.println("-- Session 3 labs --");
            System.out.println("342 + 465 as lists = " + LinkedListProblems.show(addNumbers(
                    LinkedListProblems.build(2, 4, 3), LinkedListProblems.build(5, 6, 4))));
            System.out.println("oddEven(1..7) = " + LinkedListProblems.show(oddEven(LinkedListProblems.build(1, 2, 3, 4, 5, 6, 7))));
        }
    }
}
