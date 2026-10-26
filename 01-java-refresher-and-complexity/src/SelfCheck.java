/**
 * Module 01 self-check: JUnit-free tests. Run with assertions enabled:
 *   javac -d out *.java && java -ea -cp out SelfCheck
 * Exits with status 1 if any check fails.
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
        // JavaRefresher
        check(JavaRefresher.maxOf(new Integer[]{3, 9, 2}) == 9, "maxOf integers");
        check(JavaRefresher.maxOf(new String[]{"b", "z", "a"}).equals("z"), "maxOf strings");
        check(JavaRefresher.minMax(new int[]{5, -1, 7}).first() == -1, "minMax min");
        check(JavaRefresher.minMax(new int[]{5, -1, 7}).second() == 7, "minMax max");
        int[] a = {1, 2};
        JavaRefresher.doubleAll(a);
        check(a[0] == 2 && a[1] == 4, "arrays are passed by reference value");

        // OperationCounter
        check(OperationCounter.linear(100) == 100, "linear count");
        check(OperationCounter.quadratic(30) == 900, "quadratic count");
        check(OperationCounter.triangular(10) == 45, "triangular n(n-1)/2");
        check(OperationCounter.logarithmic(1024) == 10, "log2(1024) = 10 halvings");
        check(OperationCounter.exponential(10) == 2047, "2^(n+1)-1 calls");

        // RecursionBasics
        check(RecursionBasics.sumDigits(98765) == 35, "sumDigits");
        check(RecursionBasics.power(2, 30) == 1L << 30, "power");
        check(RecursionBasics.power(7, 0) == 1, "power base case");
        check(RecursionBasics.fibMemo(50, new long[51]) == 12586269025L, "fib(50)");
        check(RecursionBasics.isPalindrome("level", 0, 4), "palindrome yes");
        check(!RecursionBasics.isPalindrome("levels", 0, 5), "palindrome no");
        check(RecursionBasics.toBinary(10).equals("1010"), "toBinary");
        check(RecursionBasics.hanoi(10, 'A', 'C', 'B', false) == 1023, "hanoi moves");

        System.out.println("Module 01 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
