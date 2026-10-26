/**
 * Module 03 · Session 02 — Binary search variations and "binary search on the answer".
 *
 * lowerBound / upperBound, first and last occurrence, counting duplicates,
 * search in a rotated sorted array, peak finding, integer square root and the
 * minimum ship capacity problem (binary search over a monotone yes/no predicate).
 *
 * Compile:  javac -d out BinarySearchVariations.java
 * Run:      java -cp out BinarySearchVariations
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class BinarySearchVariations {

    /** First index i with a[i] >= key (a.length if none). The insertion point of key. */
    static int lowerBound(int[] a, int key) {
        int lo = 0, hi = a.length;            // half-open range [lo, hi)
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] < key) lo = mid + 1;
            else hi = mid;                    // a[mid] >= key: mid might be the answer
        }
        return lo;
    }

    /** First index i with a[i] > key (a.length if none). */
    static int upperBound(int[] a, int key) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] <= key) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    static int firstOccurrence(int[] a, int key) {
        int i = lowerBound(a, key);
        return (i < a.length && a[i] == key) ? i : -1;
    }

    static int lastOccurrence(int[] a, int key) {
        int i = upperBound(a, key) - 1;
        return (i >= 0 && a[i] == key) ? i : -1;
    }

    /** Number of copies of key in O(log n). */
    static int count(int[] a, int key) {
        return upperBound(a, key) - lowerBound(a, key);
    }

    /** Search in a sorted array rotated at an unknown pivot, e.g. {40,50,60,10,20,30}. Distinct values. */
    static int searchRotated(int[] a, int key) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] == key) return mid;
            if (a[lo] <= a[mid]) {                        // left half is sorted
                if (key >= a[lo] && key < a[mid]) hi = mid - 1;
                else lo = mid + 1;
            } else {                                      // right half is sorted
                if (key > a[mid] && key <= a[hi]) lo = mid + 1;
                else hi = mid - 1;
            }
        }
        return -1;
    }

    /** Index of the minimum of a rotated sorted array = the rotation count. */
    static int rotationPoint(int[] a) {
        int lo = 0, hi = a.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] > a[hi]) lo = mid + 1;             // minimum is right of mid
            else hi = mid;
        }
        return lo;
    }

    /** Any peak index (a[i] >= neighbours). Walk uphill: O(log n). */
    static int findPeak(int[] a) {
        int lo = 0, hi = a.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] < a[mid + 1]) lo = mid + 1;        // rising: a peak lies to the right
            else hi = mid;
        }
        return lo;
    }

    /** floor(sqrt(x)) by searching the answer space [0, x]. */
    static long intSqrt(long x) {
        long lo = 0, hi = Math.min(x, 3_037_000_499L), ans = 0;   // hi*hi must not overflow
        while (lo <= hi) {
            long mid = lo + (hi - lo) / 2;
            if (mid * mid <= x) { ans = mid; lo = mid + 1; }
            else hi = mid - 1;
        }
        return ans;
    }

    /** Can all packages be shipped within 'days' if the truck carries at most 'cap' kg per day? */
    static boolean canShip(int[] weights, int days, int cap) {
        int used = 1, load = 0;
        for (int w : weights) {
            if (w > cap) return false;
            if (load + w > cap) { used++; load = 0; }
            load += w;
        }
        return used <= days;
    }

    /** Smallest capacity that ships everything in 'days' days (order must be kept). */
    static int minShipCapacity(int[] weights, int days) {
        int lo = 0, hi = 0;
        for (int w : weights) { lo = Math.max(lo, w); hi += w; }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canShip(weights, days, mid)) hi = mid;    // feasible: try smaller
            else lo = mid + 1;                            // infeasible: need more
        }
        return lo;
    }

    public static void main(String[] args) {
        int[] marks = {35, 42, 42, 42, 58, 61, 61, 77, 90};
        System.out.println("marks = [35, 42, 42, 42, 58, 61, 61, 77, 90]");
        System.out.println("lowerBound(42) = " + lowerBound(marks, 42) + ", upperBound(42) = " + upperBound(marks, 42));
        System.out.println("first(42) = " + firstOccurrence(marks, 42) + ", last(42) = " + lastOccurrence(marks, 42)
                + ", count(42) = " + count(marks, 42));
        System.out.println("count(50) = " + count(marks, 50) + ", insertion point of 50 = " + lowerBound(marks, 50));
        System.out.println("students scoring >= 60: " + (marks.length - lowerBound(marks, 60)));

        int[] rotated = {40, 50, 60, 70, 10, 20, 30};
        System.out.println("\nrotated = [40, 50, 60, 70, 10, 20, 30]");
        System.out.println("searchRotated(20) = " + searchRotated(rotated, 20) + ", searchRotated(55) = " + searchRotated(rotated, 55));
        System.out.println("rotationPoint = " + rotationPoint(rotated) + " (minimum value " + rotated[rotationPoint(rotated)] + ")");

        int[] hills = {1, 3, 8, 12, 9, 4, 2};
        System.out.println("\nfindPeak([1,3,8,12,9,4,2]) = index " + findPeak(hills) + " (value " + hills[findPeak(hills)] + ")");
        System.out.println("intSqrt(80) = " + intSqrt(80) + ", intSqrt(1_000_000_007) = " + intSqrt(1_000_000_007L));

        int[] parcels = {10, 25, 15, 30, 20, 5, 35, 40};
        for (int d : new int[]{1, 3, 5, 8}) {
            System.out.println("min truck capacity to ship in " + d + " day(s) = " + minShipCapacity(parcels, d) + " kg");
        }
    }
}
