import java.util.List;

/**
 * Module 05 self-check: JUnit-free tests for all linked-list code.
 *   javac -d out *.java && java -ea -cp out SelfCheck
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class SelfCheck {
    static int passed = 0, failed = 0;

    static void check(boolean ok, String name) {
        if (ok) passed++;
        else { failed++; System.out.println("FAIL: " + name); }
    }

    public static void main(String[] args) {
        SinglyLinkedList<Integer> s = new SinglyLinkedList<>();
        for (int i = 1; i <= 5; i++) s.addLast(i);
        s.addFirst(0);
        check(s.size() == 6 && s.get(0) == 0 && s.get(5) == 5, "singly add");
        s.insertAt(3, 99);
        check(s.get(3) == 99 && s.indexOf(99) == 3, "insertAt");
        check(s.removeAt(6) == 5, "removeAt tail");
        s.addLast(7);
        check(s.get(s.size() - 1) == 7, "tail updated after removeAt tail");
        check(s.remove(Integer.valueOf(99)) && !s.remove(Integer.valueOf(42)), "remove value");
        s.reverse();
        check(s.toString().equals("head -> 7 -> 4 -> 3 -> 2 -> 1 -> 0 -> null"), "reverse");
        s.addLast(-1);
        check(s.get(s.size() - 1) == -1, "tail after reverse");

        DoublyCircularLists.DoublyList<String> d = new DoublyCircularLists.DoublyList<>();
        DoublyCircularLists.DoublyList.Node<String> b = d.addLast("b");
        d.addFirst("a");
        d.addLast("c");
        check(d.forward().equals("null <-> a <-> b <-> c <-> null"), "doubly forward");
        check(d.unlink(b).equals("b") && d.size() == 2, "doubly unlink");
        check(d.removeLast().equals("c") && d.removeFirst().equals("a") && d.isEmpty(), "doubly ends");

        check(DoublyCircularLists.josephus(7, 3).equals(List.of(3, 6, 2, 7, 5, 1, 4)), "josephus order");
        check(DoublyCircularLists.roundRobin(new String[]{"A", "B"}, new int[]{4, 2}, 2).equals(List.of("B@4", "A@6")), "round robin");

        check(LinkedListProblems.show(LinkedListProblems.reverse(LinkedListProblems.build(1, 2, 3))).equals("3 -> 2 -> 1 -> null"), "reverse iter");
        check(LinkedListProblems.reverse(null) == null, "reverse empty");
        check(LinkedListProblems.middle(LinkedListProblems.build(1, 2, 3, 4)).val == 3, "middle even");
        check(LinkedListProblems.cycleStart(LinkedListProblems.build(1)) == null, "no cycle single");
        LinkedListProblems.ListNode c = LinkedListProblems.build(1, 2);
        c.next.next = c;
        check(LinkedListProblems.cycleStart(c) == c, "cycle at head");
        check(LinkedListProblems.show(LinkedListProblems.mergeSorted(null, LinkedListProblems.build(1))).equals("1 -> null"), "merge with empty");
        check(LinkedListProblems.removeNthFromEnd(LinkedListProblems.build(1), 1) == null, "remove only node");
        check(LinkedListProblems.isPalindrome(LinkedListProblems.build(7)), "palindrome single");
        check(!LinkedListProblems.isPalindrome(LinkedListProblems.build(1, 2)), "palindrome no");

        check(Labs05.length(null) == 0, "length empty");
        check(LinkedListProblems.show(Labs05.addNumbers(LinkedListProblems.build(9, 9), LinkedListProblems.build(1))).equals("0 -> 0 -> 1 -> null"), "add with carry");
        check(LinkedListProblems.show(Labs05.oddEven(LinkedListProblems.build(1, 2, 3, 4))).equals("1 -> 3 -> 2 -> 4 -> null"), "oddEven");

        System.out.println("Module 05 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
