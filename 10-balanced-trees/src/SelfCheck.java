import java.util.Random;
import java.util.TreeSet;

/**
 * Module 10 self-check: JUnit-free tests for AVL and red-black trees.
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
        Random r = new Random(10);
        AVLTree avl = new AVLTree();
        TreeSet<Integer> ref = new TreeSet<>();
        boolean ok = true, alwaysValid = true;
        for (int step = 0; step < 6000; step++) {
            int k = r.nextInt(1000);
            if (r.nextInt(3) > 0) { avl.insert(k); ref.add(k); } else { avl.delete(k); ref.remove(k); }
            ok &= avl.contains(k) == ref.contains(k) && avl.size() == ref.size();
            if (step % 100 == 0) alwaysValid &= avl.isAVL();
        }
        check(ok, "AVL: 6000 random inserts/deletes match TreeSet");
        check(alwaysValid && avl.isAVL(), "AVL invariants hold throughout");
        check(avl.inorder().equals(new java.util.ArrayList<>(ref)), "AVL inorder sorted");
        double bound = 1.44 * Math.log(avl.size() + 2) / Math.log(2);
        check(avl.height() <= bound, "AVL height <= 1.44 log2(n+2)");

        AVLTree sorted = new AVLTree();
        for (int i = 0; i < 4095; i++) sorted.insert(i);
        check(sorted.height() == 11, "AVL on 4095 sorted keys is perfect (height 11)");

        RedBlackTree rb = new RedBlackTree();
        TreeSet<Integer> ref2 = new TreeSet<>();
        boolean rbOk = true;
        for (int step = 0; step < 5000; step++) {
            int k = r.nextInt(100_000);
            rb.insert(k); ref2.add(k);
            if (step % 250 == 0) rbOk &= rb.isValid();
        }
        check(rbOk && rb.isValid(), "red-black invariants hold");
        check(rb.size() == ref2.size(), "red-black size");
        boolean found = true;
        for (int k : ref2) found &= rb.contains(k);
        check(found && !rb.contains(-5), "red-black contains");
        check(rb.height() <= 2 * Math.log(rb.size() + 1) / Math.log(2), "red-black height <= 2 log2(n+1)");

        check(Labs10.isBalanced(null), "empty tree balanced");
        check(Labs10.minNodes(0) == 1 && Labs10.minNodes(1) == 2 && Labs10.minNodes(2) == 4 && Labs10.minNodes(5) == 20, "min AVL nodes");
        Labs10.RoomCalendar cal = new Labs10.RoomCalendar();
        check(cal.book(10, 20) && !cal.book(19, 21) && cal.book(20, 21) && !cal.book(0, 11) && cal.book(0, 10), "room booking");
        check(Labs10.nearbyAlmostDuplicate(new int[]{1, 0, 1, 1}, 1, 2) && !Labs10.nearbyAlmostDuplicate(new int[]{1, 5, 9, 1, 5, 9}, 2, 3), "nearby almost duplicate");
        check(Labs10.nearbyAlmostDuplicate(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, 1, 1) == false, "no int overflow");

        System.out.println("Module 10 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
