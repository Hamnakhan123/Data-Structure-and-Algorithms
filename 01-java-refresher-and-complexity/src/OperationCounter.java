/**
 * Module 01 · Session 02 — Measuring growth by counting basic operations.
 *
 * Instead of timing (which depends on the machine), we count how many times the
 * innermost statement executes for different loop shapes. The counts reveal the
 * growth rate: O(1), O(log n), O(n), O(n log n), O(n^2) and O(2^n).
 *
 * Compile:  javac -d out OperationCounter.java
 * Run:      java -cp out OperationCounter
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class OperationCounter {

    /** O(1): the work does not depend on n. */
    static long constant(int n) {
        long ops = 0;
        ops++; // e.g. read arr[0]
        ops++; // e.g. compare with arr[n-1]
        return ops;
    }

    /** O(log n): the problem size halves every step (like binary search). */
    static long logarithmic(int n) {
        long ops = 0;
        for (int i = n; i > 1; i /= 2) ops++;
        return ops;
    }

    /** O(n): one pass over the input. */
    static long linear(int n) {
        long ops = 0;
        for (int i = 0; i < n; i++) ops++;
        return ops;
    }

    /** O(n log n): n passes, each of length log n (like merge sort levels). */
    static long linearithmic(int n) {
        long ops = 0;
        for (int i = 0; i < n; i++)
            for (int j = n; j > 1; j /= 2) ops++;
        return ops;
    }

    /** O(n^2): nested loop over all pairs (like bubble sort). */
    static long quadratic(int n) {
        long ops = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) ops++;
        return ops;
    }

    /** Still O(n^2): the triangular loop does n(n-1)/2 steps; constants are dropped. */
    static long triangular(int n) {
        long ops = 0;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) ops++;
        return ops;
    }

    /** O(2^n): every call makes two more calls (naive subsets). */
    static long exponential(int n) {
        if (n == 0) return 1;
        return 1 + exponential(n - 1) + exponential(n - 1);
    }

    /** Sequential blocks ADD: O(n) + O(n^2) = O(n^2). */
    static long sequential(int n) {
        return linear(n) + quadratic(n);
    }

    public static void main(String[] args) {
        int[] sizes = {8, 16, 32, 64, 128, 1024};
        System.out.printf("%6s %6s %8s %8s %10s %10s %10s%n",
                "n", "O(1)", "O(logn)", "O(n)", "O(nlogn)", "O(n^2)", "n(n-1)/2");
        for (int n : sizes) {
            System.out.printf("%6d %6d %8d %8d %10d %10d %10d%n", n, constant(n), logarithmic(n),
                    linear(n), linearithmic(n), quadratic(n), triangular(n));
        }

        System.out.println("\nDoubling test (ratio = ops(2n) / ops(n)):");
        int n = 512;
        System.out.printf("  O(n)      ratio = %.2f  (expect ~2)%n", (double) linear(2 * n) / linear(n));
        System.out.printf("  O(n log n) ratio = %.2f  (expect a bit above 2)%n",
                (double) linearithmic(2 * n) / linearithmic(n));
        System.out.printf("  O(n^2)    ratio = %.2f  (expect ~4)%n", (double) quadratic(2 * n) / quadratic(n));

        System.out.println("\nExponential growth (calls made by exponential(n)):");
        for (int k = 1; k <= 20; k += 4) {
            System.out.printf("  n=%2d -> %,d calls%n", k, exponential(k));
        }
        System.out.println("\nSequential O(n) + O(n^2) for n=100: " + sequential(100) + " ops (dominated by n^2 = 10000)");
    }
}
