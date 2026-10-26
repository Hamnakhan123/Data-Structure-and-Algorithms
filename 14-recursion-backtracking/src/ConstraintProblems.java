/**
 * Session 2 demo: constraint-satisfaction with backtracking. N-Queens (one board and solution
 * counts, with O(1) conflict checks using column and diagonal flags) and a Sudoku solver that tries
 * the most constrained empty cell first, with counters showing how much each technique prunes.
 *
 * Compile:  javac -d out ConstraintProblems.java
 * Run:      java -cp out ConstraintProblems
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class ConstraintProblems {
    static long nodes;                                     // placements tried in the last search

    // ---------------- N-Queens ----------------
    /** Count all solutions; cols/diag/anti flags make each safety check O(1). */
    static int countQueens(int n) {
        nodes = 0;
        return place(0, n, new boolean[n], new boolean[2 * n], new boolean[2 * n], null);
    }

    /** Find the first solution; queenCol[r] = column of the queen in row r. */
    static int[] firstQueens(int n) {
        int[] q = new int[n];
        java.util.Arrays.fill(q, -1);
        nodes = 0;
        return place(0, n, new boolean[n], new boolean[2 * n], new boolean[2 * n], q) > 0 ? q : null;
    }

    private static int place(int r, int n, boolean[] col, boolean[] diag, boolean[] anti, int[] q) {
        if (r == n) return 1;
        int found = 0;
        for (int c = 0; c < n; c++) {
            if (col[c] || diag[r - c + n] || anti[r + c]) continue;   // attacked: prune this branch
            nodes++;
            col[c] = diag[r - c + n] = anti[r + c] = true;           // choose
            if (q != null) q[r] = c;
            found += place(r + 1, n, col, diag, anti, q);            // explore
            if (q != null && found > 0) return found;                // first solution is enough
            col[c] = diag[r - c + n] = anti[r + c] = false;          // un-choose
        }
        return found;
    }

    static void printBoard(int[] q) {
        for (int r = 0; r < q.length; r++) {
            StringBuilder sb = new StringBuilder("  ");
            for (int c = 0; c < q.length; c++) sb.append(q[r] == c ? "Q " : ". ");
            System.out.println(sb.toString().trim());
        }
    }

    // ---------------- Sudoku ----------------
    static boolean canPlace(int[][] g, int r, int c, int d) {
        for (int i = 0; i < 9; i++) {
            if (g[r][i] == d || g[i][c] == d) return false;
            if (g[3 * (r / 3) + i / 3][3 * (c / 3) + i % 3] == d) return false;
        }
        return true;
    }

    /** Backtracking solver. If mrv is true, always fill the empty cell with the fewest legal digits. */
    static boolean solve(int[][] g, boolean mrv) {
        int br = -1, bc = -1, bestCount = 10;
        for (int r = 0; r < 9 && bestCount > 1; r++)
            for (int c = 0; c < 9; c++) {
                if (g[r][c] != 0) continue;
                if (!mrv) { br = r; bc = c; bestCount = 0; break; }
                int cnt = 0;
                for (int d = 1; d <= 9; d++) if (canPlace(g, r, c, d)) cnt++;
                if (cnt < bestCount) { bestCount = cnt; br = r; bc = c; }
                if (cnt == 0) return false;                         // dead end: no digit fits
            }
        if (br == -1) return true;                                  // no empty cell: solved
        for (int d = 1; d <= 9; d++) {
            if (!canPlace(g, br, bc, d)) continue;
            nodes++;
            g[br][bc] = d;
            if (solve(g, mrv)) return true;
            g[br][bc] = 0;                                          // backtrack
        }
        return false;
    }

    static int[][] parse(String[] rows) {
        int[][] g = new int[9][9];
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) {
            char ch = rows[r].charAt(c);
            g[r][c] = ch == '.' ? 0 : ch - '0';
        }
        return g;
    }

    static void printGrid(int[][] g) {
        for (int r = 0; r < 9; r++) {
            StringBuilder sb = new StringBuilder("  ");
            for (int c = 0; c < 9; c++) sb.append(g[r][c] == 0 ? "." : String.valueOf(g[r][c])).append(c % 3 == 2 && c < 8 ? " | " : " ");
            System.out.println(sb.toString().trim());
            if (r % 3 == 2 && r < 8) System.out.println("  ------+-------+------");
        }
    }

    public static void main(String[] args) {
        System.out.println("== 6-Queens: first solution ==");
        int[] q = firstQueens(6);
        printBoard(q);
        System.out.println("queen column per row = " + java.util.Arrays.toString(q) + ", placements tried = " + nodes);

        System.out.println("\n== Number of N-Queens solutions ==");
        System.out.println(" n   solutions   placements tried   n^n (brute force)");
        for (int n = 4; n <= 10; n++) {
            int s = countQueens(n);
            System.out.printf("%2d   %9d   %16d   %17d%n", n, s, nodes, (long) Math.pow(n, n));
        }

        String[] puzzle = {
            "53..7....", "6..195...", ".98....6.",
            "8...6...3", "4..8.3..1", "7...2...6",
            ".6....28.", "...419..5", "....8..79"};
        System.out.println("\n== Sudoku puzzle ==");
        printGrid(parse(puzzle));
        int[][] g1 = parse(puzzle);
        nodes = 0;
        boolean ok = solve(g1, false);
        long plain = nodes;
        int[][] g2 = parse(puzzle);
        nodes = 0;
        solve(g2, true);
        System.out.println("\n== Solved (" + ok + ") ==");
        printGrid(g2);
        System.out.println("digits placed: first-empty-cell order = " + plain + ", most-constrained-cell order = " + nodes);
        System.out.println("both solutions identical: " + java.util.Arrays.deepEquals(g1, g2));
    }
}
