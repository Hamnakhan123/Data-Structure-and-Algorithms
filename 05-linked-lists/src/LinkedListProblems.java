/**
 * Module 05 · Session 03 — Classic linked-list problems.
 *
 * Works on a minimal ListNode (int value + next) like most interview questions:
 * iterative and recursive reversal, middle node (slow/fast pointers), Floyd's
 * cycle detection and cycle start, merging two sorted lists with a dummy head,
 * removing the n-th node from the end, palindrome check and the intersection
 * point of two lists.
 *
 * Compile:  javac -d out LinkedListProblems.java
 * Run:      java -cp out LinkedListProblems
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class LinkedListProblems {

    static final class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    static ListNode build(int... values) {
        ListNode dummy = new ListNode(0), t = dummy;
        for (int v : values) { t.next = new ListNode(v); t = t.next; }
        return dummy.next;
    }

    static String show(ListNode h) {
        StringBuilder sb = new StringBuilder();
        for (int guard = 0; h != null && guard < 50; h = h.next, guard++) sb.append(h.val).append(" -> ");
        return sb.append("null").toString();
    }

    /** Iterative reversal: O(n) time, O(1) space. */
    static ListNode reverse(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }

    /** Recursive reversal: reverse the rest, then hook the current node at its end. O(n) stack. */
    static ListNode reverseRec(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseRec(head.next);
        head.next.next = head;   // the node after me now points back to me
        head.next = null;
        return newHead;
    }

    /** Middle node: fast moves 2 steps, slow 1. For even length returns the second middle. */
    static ListNode middle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /** Floyd's tortoise and hare: returns the node where the cycle starts, or null. */
    static ListNode cycleStart(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {                   // they met inside the cycle
                ListNode p = head;
                while (p != slow) { p = p.next; slow = slow.next; }   // equal distance to the start
                return p;
            }
        }
        return null;
    }

    /** Merge two sorted lists by relinking nodes (no new nodes). O(n + m). */
    static ListNode mergeSorted(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0), tail = dummy;  // dummy head removes "first node" special cases
        while (a != null && b != null) {
            if (a.val <= b.val) { tail.next = a; a = a.next; }
            else { tail.next = b; b = b.next; }
            tail = tail.next;
        }
        tail.next = (a != null) ? a : b;
        return dummy.next;
    }

    /** Remove the n-th node from the end in ONE pass: lead pointer is n nodes ahead. */
    static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode lead = dummy, lag = dummy;
        for (int i = 0; i <= n; i++) lead = lead.next;  // gap of n nodes between lag and lead
        while (lead != null) { lead = lead.next; lag = lag.next; }
        lag.next = lag.next.next;
        return dummy.next;
    }

    /** Palindrome in O(n) time, O(1) space: reverse the second half, compare, restore. */
    static boolean isPalindrome(ListNode head) {
        ListNode mid = middle(head);
        ListNode second = reverse(mid);
        boolean ok = true;
        for (ListNode p = head, q = second; q != null; p = p.next, q = q.next)
            if (p.val != q.val) { ok = false; break; }
        reverse(second);                                 // restore the original list
        return ok;
    }

    /** Intersection of two Y-shaped lists: walk A then B, and B then A; pointers meet at the join. */
    static ListNode intersection(ListNode a, ListNode b) {
        ListNode p = a, q = b;
        while (p != q) {
            p = (p == null) ? b : p.next;
            q = (q == null) ? a : q.next;
        }
        return p;   // the join node, or null if the lists never meet
    }

    public static void main(String[] args) {
        ListNode list = build(1, 2, 3, 4, 5);
        System.out.println("list              : " + show(list));
        list = reverse(list);
        System.out.println("reverse           : " + show(list));
        list = reverseRec(list);
        System.out.println("reverseRec        : " + show(list));
        System.out.println("middle of 1..5    : " + middle(list).val + ", middle of 1..6: " + middle(build(1, 2, 3, 4, 5, 6)).val);

        ListNode cyc = build(3, 2, 0, -4, 7);
        ListNode join = cyc.next;                // node with value 2
        ListNode last = cyc;
        while (last.next != null) last = last.next;
        last.next = join;                        // 7 -> 2 creates a cycle
        System.out.println("cycle start       : " + cycleStart(cyc).val + " (expected 2)");
        System.out.println("no cycle          : " + cycleStart(build(1, 2, 3)));

        System.out.println("mergeSorted       : " + show(mergeSorted(build(1, 4, 9, 12), build(2, 3, 10))));
        System.out.println("remove 2nd from end of 10..50: " + show(removeNthFromEnd(build(10, 20, 30, 40, 50), 2)));
        ListNode pal = build(1, 2, 3, 2, 1);
        System.out.println("isPalindrome(1 2 3 2 1) = " + isPalindrome(pal) + ", list restored: " + show(pal));
        System.out.println("isPalindrome(1 2 3 4)   = " + isPalindrome(build(1, 2, 3, 4)));

        ListNode common = build(8, 9);
        ListNode a = build(1, 2, 3);
        a.next.next.next = common;
        ListNode b = build(7);
        b.next = common;
        System.out.println("A: " + show(a) + " | B: " + show(b));
        System.out.println("intersection node : " + intersection(a, b).val);
    }
}
