import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * Module 09 self-check: JUnit-free tests for binary trees and BSTs.
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
        BinaryTrees.Node s = BinaryTrees.sample();
        List<Integer> pre = new ArrayList<>(), in = new ArrayList<>(), post = new ArrayList<>();
        BinaryTrees.preorder(s, pre); BinaryTrees.inorder(s, in); BinaryTrees.postorder(s, post);
        check(pre.equals(List.of(1, 2, 4, 5, 7, 3, 6)), "preorder");
        check(in.equals(List.of(4, 2, 7, 5, 1, 3, 6)), "inorder");
        check(post.equals(List.of(4, 7, 5, 2, 6, 3, 1)), "postorder");
        check(BinaryTrees.inorderIterative(s).equals(in), "iterative inorder");
        check(BinaryTrees.levelOrder(s).toString().equals("[[1], [2, 3], [4, 5, 6], [7]]"), "level order");
        check(BinaryTrees.size(s) == 7 && BinaryTrees.height(s) == 3 && BinaryTrees.leaves(s) == 3, "size/height/leaves");
        check(BinaryTrees.height(null) == -1, "empty height");

        // BST vs TreeSet, random operations
        Random r = new Random(9);
        BinarySearchTree t = new BinarySearchTree();
        TreeSet<Integer> ref = new TreeSet<>();
        boolean ok = true;
        for (int step = 0; step < 4000; step++) {
            int k = r.nextInt(300);
            switch (r.nextInt(3)) {
                case 0: t.insert(k); ref.add(k); break;
                case 1: t.delete(k); ref.remove(k); break;
                default:
                    ok &= t.contains(k) == ref.contains(k);
                    ok &= java.util.Objects.equals(t.floor(k), ref.floor(k));
                    ok &= java.util.Objects.equals(t.ceiling(k), ref.ceiling(k));
            }
            ok &= t.size() == ref.size();
        }
        check(ok, "4000 random insert/delete/contains/floor/ceiling match TreeSet");
        check(t.inorder().equals(new ArrayList<>(ref)), "inorder is sorted and complete");
        check(t.isValid(), "still a valid BST");
        check(t.range(100, 150).equals(new ArrayList<>(ref.subSet(100, true, 150, true))), "range query");

        check(Labs09.isSymmetric(Labs09.n(1, Labs09.n(2), Labs09.n(2))) && !Labs09.isSymmetric(s), "symmetric");
        check(Labs09.hasPathSum(s, 10) && !Labs09.hasPathSum(s, 8), "path sum");   // 1+3+6 = 10
        check(Labs09.diameter(s) == 5, "diameter");
        BinarySearchTree b = Labs09.sampleBst();
        check(Labs09.lcaBst(b.rootNode(), 20, 45) == 30 && Labs09.lcaBst(b.rootNode(), 65, 80) == 70, "LCA");
        check(Labs09.kthSmallest(b.rootNode(), 3) == 35, "kth smallest");
        int[] sorted = new int[100];
        for (int i = 0; i < 100; i++) sorted[i] = i;
        BinaryTrees.Node bal = Labs09.sortedArrayToBst(sorted, 0, 99);
        List<Integer> balIn = new ArrayList<>();
        BinaryTrees.inorder(bal, balIn);
        check(BinaryTrees.height(bal) == 6 && balIn.size() == 100 && balIn.get(99) == 99, "balanced from sorted array");

        System.out.println("Module 09 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
