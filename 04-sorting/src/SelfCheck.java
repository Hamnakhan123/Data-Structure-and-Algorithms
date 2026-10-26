import java.util.Arrays;
import java.util.Random;

/**
 * Module 04 self-check: every sort is tested against Arrays.sort on many random
 * arrays (including empty, single-element, sorted, reversed and duplicate-heavy).
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
        Random rnd = new Random(2024);
        for (int trial = 0; trial < 60; trial++) {
            int n = trial < 3 ? trial : rnd.nextInt(300);
            int bound = trial % 4 == 0 ? 3 : 1000;            // some trials are duplicate-heavy
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(bound);
            if (trial % 5 == 1) Arrays.sort(a);                 // already sorted
            if (trial % 5 == 2) { Arrays.sort(a); for (int i = 0; i < n / 2; i++) { int t = a[i]; a[i] = a[n - 1 - i]; a[n - 1 - i] = t; } }
            int[] exp = a.clone();
            Arrays.sort(exp);

            int[] b = a.clone(); ElementarySorts.bubbleSort(b, false);       check(Arrays.equals(b, exp), "bubble " + trial);
            b = a.clone(); ElementarySorts.selectionSort(b, false);          check(Arrays.equals(b, exp), "selection " + trial);
            b = a.clone(); ElementarySorts.insertionSort(b, false);          check(Arrays.equals(b, exp), "insertion " + trial);
            b = a.clone(); MergeQuickSort.mergeSort(b);                      check(Arrays.equals(b, exp), "merge " + trial);
            b = a.clone(); MergeQuickSort.quickSort(b);                      check(Arrays.equals(b, exp), "quick " + trial);
            b = a.clone(); MergeQuickSort.quickSort3(b, 0, b.length - 1);    check(Arrays.equals(b, exp), "quick3 " + trial);
            b = a.clone(); HeapCountingRadix.heapSort(b);                    check(Arrays.equals(b, exp), "heap " + trial);
            check(Arrays.equals(HeapCountingRadix.countingSort(a, 999), exp), "counting " + trial);
            b = a.clone(); HeapCountingRadix.radixSort(b);                   check(Arrays.equals(b, exp), "radix " + trial);
            if (n > 0) check(Labs04.quickSelect(a, n / 2) == exp[n / 2], "quickSelect " + trial);
        }
        check(MergeQuickSort.countInversions(new int[]{1, 20, 6, 4, 5}) == 5, "inversions");
        check(Labs04.bubbleSwapCount(new int[]{1, 20, 6, 4, 5}) == 5, "bubble swaps = inversions");
        check(Arrays.equals(Labs04.mergeSorted(new int[]{}, new int[]{1}), new int[]{1}), "merge with empty");
        int[] c = {1, 0, 2, 0};
        Labs04.sortColors(c);
        check(Arrays.equals(c, new int[]{0, 0, 1, 2}), "sortColors");
        String[] s = {"bb", "a", "cc", "d"};
        Labs04.insertionSort(s, java.util.Comparator.comparingInt(String::length));
        check(Arrays.equals(s, new String[]{"a", "d", "bb", "cc"}), "generic insertion is stable");

        System.out.println("Module 04 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
