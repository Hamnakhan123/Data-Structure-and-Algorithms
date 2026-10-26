/**
 * Module 03 — reference solutions for the lab tasks of Sessions 1-2.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out Labs03.java
 * Run:      java -cp out Labs03 1    (Session 1 labs; other numbers for other sessions; no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs03 {

    // >>> lastIndexLinear
    /** Last occurrence by scanning from the right: stops early on the first hit. */
    static int lastIndexOf(int[] a, int key) {
        for (int i = a.length - 1; i >= 0; i--)
            if (a[i] == key) return i;
        return -1;
    }
    // <<< lastIndexLinear

    // >>> searchStrings
    /** Binary search in a sorted String array using compareTo. */
    static int searchName(String[] sorted, String name) {
        int lo = 0, hi = sorted.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int c = sorted[mid].compareTo(name);
            if (c == 0) return mid;
            if (c < 0) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }
    // <<< searchStrings

    // >>> matrixBinary
    /** Matrix where each row is sorted and each row starts after the previous row ends:
     *  treat it as one virtual sorted array of length r*c. O(log(r*c)). */
    static boolean searchMatrix(int[][] m, int key) {
        int r = m.length, c = m[0].length, lo = 0, hi = r * c - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int v = m[mid / c][mid % c];          // map 1-D index to (row, col)
            if (v == key) return true;
            if (v < key) lo = mid + 1;
            else hi = mid - 1;
        }
        return false;
    }
    // <<< matrixBinary

    // >>> closest
    /** Element closest to key in a sorted array (ties -> smaller value). */
    static int closest(int[] a, int key) {
        int i = BinarySearchVariations.lowerBound(a, key);
        if (i == 0) return a[0];
        if (i == a.length) return a[a.length - 1];
        return (key - a[i - 1] <= a[i] - key) ? a[i - 1] : a[i];
    }
    // <<< closest

    // >>> kokoBananas
    /** Minimum eating speed k so that all piles finish within h hours (each hour: one pile, up to k). */
    static int minSpeed(int[] piles, int h) {
        int lo = 1, hi = 0;
        for (int p : piles) hi = Math.max(hi, p);
        while (lo < hi) {
            int k = lo + (hi - lo) / 2;
            long hours = 0;
            for (int p : piles) hours += (p + k - 1) / k;   // ceil(p / k)
            if (hours <= h) hi = k; else lo = k + 1;
        }
        return lo;
    }
    // <<< kokoBananas

    public static void main(String[] args) {
        int session = args.length > 0 ? Integer.parseInt(args[0]) : 0;   // 0 = all sessions
        if (session == 0 || session == 1) {
            System.out.println("-- Session 1 labs --");
            System.out.println("lastIndexOf({4,7,4,9,4}, 4) = " + lastIndexOf(new int[]{4, 7, 4, 9, 4}, 4));
            String[] names = {"Ahmed", "Bushra", "Faraz", "Hamza", "Mehwish", "Rabia", "Zainab"};
            System.out.println("searchName(Mehwish) = " + searchName(names, "Mehwish") + ", searchName(Kashif) = " + searchName(names, "Kashif"));
            int[][] m = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
            System.out.println("searchMatrix(16) = " + searchMatrix(m, 16) + ", searchMatrix(13) = " + searchMatrix(m, 13));
        }
        if (session == 0 || session == 2) {
            System.out.println("-- Session 2 labs --");
            int[] prices = {120, 250, 400, 650, 999};
            System.out.println("closest price to 500 = " + closest(prices, 500) + ", to 600 = " + closest(prices, 600));
            System.out.println("minSpeed({3,6,7,11}, 8 hours) = " + minSpeed(new int[]{3, 6, 7, 11}, 8));
        }
    }
}
