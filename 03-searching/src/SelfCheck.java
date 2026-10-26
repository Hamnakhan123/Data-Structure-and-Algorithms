/**
 * Module 03 self-check: JUnit-free tests for searching.
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
        int[] s = {2, 4, 6, 8, 10, 12, 14};
        check(SearchAlgorithms.linearSearch(new int[]{5, 1, 9}, 9) == 2, "linear");
        check(SearchAlgorithms.sentinelSearch(new int[]{5, 1, 9}, 9) == 2, "sentinel last element");
        check(SearchAlgorithms.sentinelSearch(new int[]{5, 1, 9}, 7) == -1, "sentinel absent");
        // exhaustive test: every present and absent key, every method
        for (int k = 0; k <= 16; k++) {
            int expect = (k % 2 == 0 && k >= 2 && k <= 14) ? k / 2 - 1 : -1;
            check(SearchAlgorithms.binarySearch(s, k, false) == expect, "binary " + k);
            check(SearchAlgorithms.binarySearchRec(s, k, 0, s.length - 1) == expect, "rec " + k);
            check(SearchAlgorithms.jumpSearch(s, k) == expect, "jump " + k);
            check(SearchAlgorithms.interpolationSearch(s, k) == expect, "interp " + k);
            check(SearchAlgorithms.exponentialSearch(s, k) == expect, "expo " + k);
        }
        check(SearchAlgorithms.binarySearch(new int[0], 1, false) == -1, "empty array");

        int[] d = {1, 2, 2, 2, 3, 5};
        check(BinarySearchVariations.lowerBound(d, 2) == 1, "lowerBound");
        check(BinarySearchVariations.upperBound(d, 2) == 4, "upperBound");
        check(BinarySearchVariations.count(d, 2) == 3 && BinarySearchVariations.count(d, 4) == 0, "count");
        check(BinarySearchVariations.lowerBound(d, 9) == 6, "lowerBound past end");
        check(BinarySearchVariations.lastOccurrence(d, 2) == 3 && BinarySearchVariations.firstOccurrence(d, 4) == -1, "occurrences");
        int[] r = {6, 7, 8, 1, 2, 3, 4, 5};
        for (int i = 0; i < r.length; i++) check(BinarySearchVariations.searchRotated(r, r[i]) == i, "rotated " + r[i]);
        check(BinarySearchVariations.searchRotated(r, 9) == -1, "rotated absent");
        check(BinarySearchVariations.rotationPoint(r) == 3, "rotation point");
        check(BinarySearchVariations.findPeak(new int[]{1, 2, 3, 1}) == 2, "peak");
        check(BinarySearchVariations.intSqrt(0) == 0 && BinarySearchVariations.intSqrt(99) == 9
                && BinarySearchVariations.intSqrt(100) == 10, "intSqrt");
        check(BinarySearchVariations.minShipCapacity(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5) == 15, "ship capacity");

        check(Labs03.searchMatrix(new int[][]{{1, 3}, {5, 7}}, 5), "matrix search");
        check(Labs03.closest(new int[]{1, 5, 9}, 7) == 5, "closest tie -> smaller");
        check(Labs03.minSpeed(new int[]{30, 11, 23, 4, 20}, 5) == 30, "min speed");

        System.out.println("Module 03 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
