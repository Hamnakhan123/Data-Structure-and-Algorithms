import java.util.Arrays;
import java.util.List;

/**
 * Module 06 self-check: JUnit-free tests for stacks and their applications.
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
        StackImplementations.ArrayStack<Integer> a = new StackImplementations.ArrayStack<>();
        StackImplementations.LinkedStack<Integer> l = new StackImplementations.LinkedStack<>();
        for (int i = 0; i < 100; i++) { a.push(i); l.push(i); }
        boolean same = true;
        for (int i = 99; i >= 0; i--) same &= a.pop() == i && l.pop() == i;
        check(same && a.isEmpty() && l.isEmpty(), "LIFO order, both implementations");
        boolean threw = false;
        try { l.peek(); } catch (java.util.EmptyStackException e) { threw = true; }
        check(threw, "underflow throws");
        check(StackImplementations.reverse("abc", new StackImplementations.LinkedStack<>()).equals("cba"), "reverse");

        check(ExpressionEvaluation.isBalanced("{[()]}()") && !ExpressionEvaluation.isBalanced("{[}]")
                && !ExpressionEvaluation.isBalanced("(("), "balanced brackets");
        check(ExpressionEvaluation.toPostfix("1+2*3", false).equals(List.of("1", "2", "3", "*", "+")), "precedence");
        check(ExpressionEvaluation.toPostfix("(1+2)*3", false).equals(List.of("1", "2", "+", "3", "*")), "parentheses");
        check(ExpressionEvaluation.calculate("2^3^2") == 512, "right associativity");
        check(ExpressionEvaluation.calculate("20-5-3") == 12, "left associativity");
        check(ExpressionEvaluation.calculate("(2+3)*(7-2)/5") == 5, "calculator");
        check(ExpressionEvaluation.evalPrefix("+ 2 * 3 4".split(" ")) == 14, "prefix");

        check(Arrays.equals(MonotonicStack.nextGreater(new int[]{2, 1, 3}, false), new int[]{3, 3, -1}), "next greater");
        check(Arrays.equals(MonotonicStack.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}),
                new int[]{1, 1, 4, 2, 1, 1, 0, 0}), "daily temperatures");
        check(Arrays.equals(MonotonicStack.stockSpan(new int[]{100, 80, 60, 70, 60, 75, 85}),
                new int[]{1, 1, 1, 2, 1, 4, 6}), "stock span");
        check(MonotonicStack.largestRectangle(new int[]{2, 4}) == 4 && MonotonicStack.largestRectangle(new int[]{}) == 0, "histogram");
        java.util.Random r = new java.util.Random(1);
        for (int t = 0; t < 50; t++) {
            int[] x = new int[r.nextInt(30)];
            for (int i = 0; i < x.length; i++) x[i] = r.nextInt(10);
            check(Arrays.equals(MonotonicStack.nextGreater(x, false), MonotonicStack.nextGreaterBrute(x)), "random next greater " + t);
        }

        check(Labs06.toBinary(10).equals("1010"), "binary");
        check(Labs06.simplifyPath("/../").equals("/") && Labs06.simplifyPath("/a/./b/../../c/").equals("/c"), "simplify path");
        check(Labs06.removeKDigits("10", 2).equals("0"), "remove k digits all");
        check(!Labs06.tagsBalanced("<a>"), "tags unbalanced");

        System.out.println("Module 06 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
