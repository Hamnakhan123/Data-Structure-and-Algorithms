import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * Module 07 · Session 02 — Deques (double-ended queues) and their applications.
 *
 * CircularDeque<T> supports O(1) add/remove at both ends on a circular array.
 * Applications: palindrome checking, sliding-window maximum with a monotonic
 * deque (O(n) instead of O(n*k)), BFS shortest path in a grid maze using a queue,
 * and 0-1 BFS where a deque gives shortest paths when edge weights are 0 or 1.
 *
 * Compile:  javac -d out DequeApplications.java
 * Run:      java -cp out DequeApplications
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class DequeApplications {

    /** Circular-array deque with automatic growth. */
    static final class CircularDeque<T> {
        private Object[] data = new Object[4];
        private int head, size;   // head = index of the first element

        int size() { return size; }
        boolean isEmpty() { return size == 0; }

        void addFirst(T x) {
            if (size == data.length) grow();
            head = (head - 1 + data.length) % data.length;   // step backwards with wrap-around
            data[head] = x;
            size++;
        }

        void addLast(T x) {
            if (size == data.length) grow();
            data[(head + size) % data.length] = x;
            size++;
        }

        @SuppressWarnings("unchecked")
        T removeFirst() {
            if (size == 0) throw new java.util.NoSuchElementException();
            T x = (T) data[head];
            data[head] = null;
            head = (head + 1) % data.length;
            size--;
            return x;
        }

        @SuppressWarnings("unchecked")
        T removeLast() {
            if (size == 0) throw new java.util.NoSuchElementException();
            int tail = (head + size - 1) % data.length;
            T x = (T) data[tail];
            data[tail] = null;
            size--;
            return x;
        }

        private void grow() {
            Object[] b = new Object[data.length * 2];
            for (int i = 0; i < size; i++) b[i] = data[(head + i) % data.length];
            data = b;
            head = 0;
        }

        @Override public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++) sb.append(i == 0 ? "" : ", ").append(data[(head + i) % data.length]);
            return sb.append("]").toString();
        }
    }

    /** Palindrome: remove from both ends and compare. */
    static boolean isPalindrome(String s) {
        CircularDeque<Character> d = new CircularDeque<>();
        for (char c : s.toLowerCase().toCharArray()) if (Character.isLetterOrDigit(c)) d.addLast(c);
        while (d.size() > 1) if (!d.removeFirst().equals(d.removeLast())) return false;
        return true;
    }

    /** Maximum of every window of size k. The deque holds indices with DECREASING values. O(n). */
    static int[] slidingWindowMax(int[] a, int k, boolean trace) {
        int[] out = new int[a.length - k + 1];
        Deque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < a.length; i++) {
            if (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();      // front left the window
            while (!dq.isEmpty() && a[dq.peekLast()] <= a[i]) dq.pollLast();  // smaller ones can never be max
            dq.offerLast(i);
            if (i >= k - 1) out[i - k + 1] = a[dq.peekFirst()];
            if (trace) {
                List<Integer> vals = new ArrayList<>();
                for (int idx : dq) vals.add(a[idx]);
                System.out.printf("  i=%d a[i]=%2d deque values=%-12s %s%n", i, a[i], vals,
                        i >= k - 1 ? "window max=" + out[i - k + 1] : "");
            }
        }
        return out;
    }

    /** Brute force for comparison: O(n*k). */
    static int[] slidingWindowMaxBrute(int[] a, int k) {
        int[] out = new int[a.length - k + 1];
        for (int s = 0; s + k <= a.length; s++) {
            int m = Integer.MIN_VALUE;
            for (int j = s; j < s + k; j++) m = Math.max(m, a[j]);
            out[s] = m;
        }
        return out;
    }

    /** Fewest steps from S to E in a maze ('#' = wall) using BFS with a queue; -1 if unreachable. */
    static int shortestPath(String[] maze) {
        int r = maze.length, c = maze[0].length();
        int[][] dist = new int[r][c];
        for (int[] row : dist) Arrays.fill(row, -1);
        Deque<int[]> q = new ArrayDeque<>();
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                if (maze[i].charAt(j) == 'S') { q.offer(new int[]{i, j}); dist[i][j] = 0; }
        int[][] moves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!q.isEmpty()) {
            int[] p = q.poll();
            if (maze[p[0]].charAt(p[1]) == 'E') return dist[p[0]][p[1]];
            for (int[] m : moves) {
                int ni = p[0] + m[0], nj = p[1] + m[1];
                if (ni >= 0 && nj >= 0 && ni < r && nj < c && maze[ni].charAt(nj) != '#' && dist[ni][nj] == -1) {
                    dist[ni][nj] = dist[p[0]][p[1]] + 1;
                    q.offer(new int[]{ni, nj});
                }
            }
        }
        return -1;
    }

    /** 0-1 BFS: moving onto '.' costs 0 (paved road), onto '~' costs 1 (mud). Minimum total cost. */
    static int zeroOneBfs(String[] grid) {
        int r = grid.length, c = grid[0].length();
        int[][] cost = new int[r][c];
        for (int[] row : cost) Arrays.fill(row, Integer.MAX_VALUE);
        Deque<int[]> dq = new ArrayDeque<>();
        cost[0][0] = 0;
        dq.offerFirst(new int[]{0, 0});
        int[][] moves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!dq.isEmpty()) {
            int[] p = dq.pollFirst();
            for (int[] m : moves) {
                int ni = p[0] + m[0], nj = p[1] + m[1];
                if (ni < 0 || nj < 0 || ni >= r || nj >= c) continue;
                int w = grid[ni].charAt(nj) == '~' ? 1 : 0;
                if (cost[p[0]][p[1]] + w < cost[ni][nj]) {
                    cost[ni][nj] = cost[p[0]][p[1]] + w;
                    if (w == 0) dq.offerFirst(new int[]{ni, nj});   // free edge: front
                    else dq.offerLast(new int[]{ni, nj});           // cost-1 edge: back
                }
            }
        }
        return cost[r - 1][c - 1];
    }

    public static void main(String[] args) {
        CircularDeque<String> d = new CircularDeque<>();
        d.addLast("B"); d.addLast("C"); d.addFirst("A"); d.addFirst("Z");
        System.out.println("deque after addLast B, C, addFirst A, Z: " + d);
        System.out.println("removeFirst -> " + d.removeFirst() + ", removeLast -> " + d.removeLast() + ", now " + d);
        for (int i = 0; i < 6; i++) d.addFirst("x" + i);
        System.out.println("after 6 more addFirst (grew): " + d + " size " + d.size());

        System.out.println("\nisPalindrome(\"Never odd or even\") = " + isPalindrome("Never odd or even"));
        System.out.println("isPalindrome(\"deque\") = " + isPalindrome("deque"));

        int[] a = {1, 3, -1, -3, 5, 3, 6, 7};
        System.out.println("\nSliding window maximum, k = 3, a = " + Arrays.toString(a));
        System.out.println("result " + Arrays.toString(slidingWindowMax(a, 3, true)));

        String[] maze = {
                "S.#.....",
                ".##.###.",
                "....#...",
                "##.##.#.",
                "...#..#E"
        };
        System.out.println("\nMaze:");
        for (String row : maze) System.out.println("  " + row);
        System.out.println("BFS shortest path S->E = " + shortestPath(maze) + " steps");

        String[] city = {
                "..~~~",
                "~.~.~",
                "~...~",
                "~~~~.",
        };
        System.out.println("\n0-1 BFS: minimum mud cells crossed from top-left to bottom-right = " + zeroOneBfs(city));
    }
}
