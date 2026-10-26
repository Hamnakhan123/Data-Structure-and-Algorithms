/**
 * Module 01 · Session 03 — Recursion fundamentals.
 *
 * Every recursive method needs:
 *   (1) a BASE CASE that stops the recursion, and
 *   (2) a RECURSIVE CASE that moves strictly closer to the base case.
 *
 * Compile:  javac -d out RecursionBasics.java
 * Run:      java -cp out RecursionBasics
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class RecursionBasics {

    static long fibCalls = 0; // counts calls made by the naive Fibonacci

    /** n! with a visible call trace; depth controls indentation. */
    static long factorial(int n, int depth) {
        String pad = "  ".repeat(depth);
        System.out.println(pad + "factorial(" + n + ") called");
        if (n <= 1) {                       // base case
            System.out.println(pad + "base case -> 1");
            return 1;
        }
        long result = n * factorial(n - 1, depth + 1); // recursive case
        System.out.println(pad + "factorial(" + n + ") returns " + result);
        return result;
    }

    /** Sum of digits: 4096 -> 4 + 0 + 9 + 6 = 19. */
    static int sumDigits(int n) {
        if (n < 10) return n;
        return n % 10 + sumDigits(n / 10);
    }

    /** Naive Fibonacci: O(2^n) calls because sub-problems are recomputed. */
    static long fibNaive(int n) {
        fibCalls++;
        if (n < 2) return n;
        return fibNaive(n - 1) + fibNaive(n - 2);
    }

    /** Memoised Fibonacci: each value computed once -> O(n). */
    static long fibMemo(int n, long[] memo) {
        if (n < 2) return n;
        if (memo[n] != 0) return memo[n];
        return memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
    }

    /** Fast power by squaring: O(log e) multiplications instead of O(e). */
    static long power(long base, int exp) {
        if (exp == 0) return 1;
        long half = power(base, exp / 2);
        return (exp % 2 == 0) ? half * half : half * half * base;
    }

    /** Checks a palindrome by comparing the two ends and recursing inward. */
    static boolean isPalindrome(String s, int lo, int hi) {
        if (lo >= hi) return true;
        if (s.charAt(lo) != s.charAt(hi)) return false;
        return isPalindrome(s, lo + 1, hi - 1);
    }

    /** Binary representation built recursively: 13 -> "1101". */
    static String toBinary(int n) {
        if (n < 2) return Integer.toString(n);
        return toBinary(n / 2) + (n % 2);
    }

    /** Tower of Hanoi: moves n disks from 'from' to 'to' using 'via'. Returns the move count. */
    static int hanoi(int n, char from, char to, char via, boolean print) {
        if (n == 0) return 0;
        int moves = hanoi(n - 1, from, via, to, print);
        if (print) System.out.println("  move disk " + n + " : " + from + " -> " + to);
        moves++;
        moves += hanoi(n - 1, via, to, from, print);
        return moves;
    }

    /** Depth of recursion we can reach before the JVM stack overflows (illustration only). */
    static int depthProbe(int d) {
        try {
            return depthProbe(d + 1);
        } catch (StackOverflowError e) {
            return d;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== factorial(4) call trace ===");
        System.out.println("answer = " + factorial(4, 0));

        System.out.println("\n=== small recursive functions ===");
        System.out.println("sumDigits(4096)       = " + sumDigits(4096));
        System.out.println("power(3, 13)          = " + power(3, 13));
        System.out.println("isPalindrome(racecar) = " + isPalindrome("racecar", 0, 6));
        System.out.println("isPalindrome(karachi) = " + isPalindrome("karachi", 0, 6));
        System.out.println("toBinary(13)          = " + toBinary(13));

        System.out.println("\n=== naive vs memoised Fibonacci ===");
        for (int n : new int[]{10, 20, 30}) {
            fibCalls = 0;
            long v = fibNaive(n);
            System.out.printf("fib(%d) = %d  naive calls = %,d  memo value = %d%n",
                    n, v, fibCalls, fibMemo(n, new long[n + 1]));
        }
        System.out.println("fibMemo(90) = " + fibMemo(90, new long[91]) + " (naive would need ~10^18 calls)");

        System.out.println("\n=== Tower of Hanoi, 3 disks ===");
        int m = hanoi(3, 'A', 'C', 'B', true);
        System.out.println("total moves = " + m + " (2^3 - 1)");
        for (int n = 5; n <= 20; n += 5) {
            System.out.println("hanoi(" + n + ") needs " + hanoi(n, 'A', 'C', 'B', false) + " moves");
        }
        int depth = depthProbe(0);
        System.out.println("\nStack depth reached before StackOverflowError: " + (depth > 1000 ? "more than 1000 frames" : depth));
    }
}
