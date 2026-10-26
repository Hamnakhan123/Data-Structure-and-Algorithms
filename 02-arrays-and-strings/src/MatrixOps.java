import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Module 02 · Session 02 — Two-dimensional arrays and matrix algorithms.
 *
 * Row-major traversal, transpose, 90-degree rotation in place, spiral order,
 * matrix multiplication, jagged arrays and a 2-D prefix-sum table for O(1)
 * rectangle-sum queries.
 *
 * Compile:  javac -d out MatrixOps.java
 * Run:      java -cp out MatrixOps
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class MatrixOps {

    static void print(String title, int[][] m) {
        System.out.println(title);
        for (int[] row : m) {
            StringBuilder sb = new StringBuilder("  ");
            for (int v : row) sb.append(String.format("%4d", v));
            System.out.println(sb);
        }
    }

    /** Returns a new matrix whose rows are the columns of m. O(r*c). */
    static int[][] transpose(int[][] m) {
        int r = m.length, c = m[0].length;
        int[][] t = new int[c][r];
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                t[j][i] = m[i][j];
        return t;
    }

    /** Rotates a square matrix 90 degrees clockwise IN PLACE: transpose, then reverse each row. */
    static void rotateClockwise(int[][] m) {
        int n = m.length;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {        // swap across the main diagonal
                int tmp = m[i][j]; m[i][j] = m[j][i]; m[j][i] = tmp;
            }
        for (int[] row : m)
            for (int l = 0, r = n - 1; l < r; l++, r--) {
                int tmp = row[l]; row[l] = row[r]; row[r] = tmp;
            }
    }

    /** Spiral (clockwise) order using four shrinking boundaries. O(r*c). */
    static List<Integer> spiral(int[][] m) {
        List<Integer> out = new ArrayList<>();
        int top = 0, bottom = m.length - 1, left = 0, right = m[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int j = left; j <= right; j++) out.add(m[top][j]);
            top++;
            for (int i = top; i <= bottom; i++) out.add(m[i][right]);
            right--;
            if (top <= bottom) {
                for (int j = right; j >= left; j--) out.add(m[bottom][j]);
                bottom--;
            }
            if (left <= right) {
                for (int i = bottom; i >= top; i--) out.add(m[i][left]);
                left++;
            }
        }
        return out;
    }

    /** Classic triple loop: (r x k) * (k x c). O(r*k*c). */
    static int[][] multiply(int[][] a, int[][] b) {
        if (a[0].length != b.length) throw new IllegalArgumentException("inner dimensions differ");
        int r = a.length, k = b.length, c = b[0].length;
        int[][] p = new int[r][c];
        for (int i = 0; i < r; i++)
            for (int t = 0; t < k; t++)          // i-t-j order is cache friendly
                for (int j = 0; j < c; j++)
                    p[i][j] += a[i][t] * b[t][j];
        return p;
    }

    /** prefix[i][j] = sum of m[0..i-1][0..j-1]; built in O(r*c). */
    static long[][] prefixSums(int[][] m) {
        int r = m.length, c = m[0].length;
        long[][] p = new long[r + 1][c + 1];
        for (int i = 1; i <= r; i++)
            for (int j = 1; j <= c; j++)
                p[i][j] = m[i - 1][j - 1] + p[i - 1][j] + p[i][j - 1] - p[i - 1][j - 1];
        return p;
    }

    /** Sum of the rectangle (r1,c1)..(r2,c2) inclusive in O(1) using inclusion-exclusion. */
    static long rectSum(long[][] p, int r1, int c1, int r2, int c2) {
        return p[r2 + 1][c2 + 1] - p[r1][c2 + 1] - p[r2 + 1][c1] + p[r1][c1];
    }

    public static void main(String[] args) {
        int[][] m = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        };
        print("Original 3x4:", m);
        print("Transpose 4x3:", transpose(m));
        System.out.println("Spiral order: " + spiral(m));

        int[][] sq = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        rotateClockwise(sq);
        print("3x3 rotated 90 degrees clockwise:", sq);

        int[][] a = {{1, 2}, {3, 4}, {5, 6}};       // 3x2
        int[][] b = {{7, 8, 9}, {10, 11, 12}};       // 2x3
        print("A(3x2) * B(2x3):", multiply(a, b));

        int[][] jagged = new int[4][];
        for (int i = 0; i < jagged.length; i++) {
            jagged[i] = new int[i + 1];               // Pascal's triangle rows have i+1 cells
            jagged[i][0] = jagged[i][i] = 1;
            for (int j = 1; j < i; j++) jagged[i][j] = jagged[i - 1][j - 1] + jagged[i - 1][j];
        }
        System.out.println("Jagged array (Pascal's triangle): " + Arrays.deepToString(jagged));

        int[][] sales = {        // rows = branches, columns = months
                {12, 15, 9, 20},
                {8, 11, 14, 6},
                {17, 4, 10, 13}
        };
        long[][] p = prefixSums(sales);
        System.out.println("Total sales, branches 0-1, months 1-2 = " + rectSum(p, 0, 1, 1, 2));
        System.out.println("Total sales, whole table            = " + rectSum(p, 0, 0, 2, 3));
        System.out.println("Row-major address of m[2][1] in a 3x4 int matrix = base + (2*4 + 1)*4 bytes = base + "
                + ((2 * 4 + 1) * 4));
    }
}
