import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Module 12 — reference solutions for the lab tasks of Sessions 1-4.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out *.java
 * Run:      java -cp out Labs12 1    (Session 1 labs; 2, 3 or 4 for the others; no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs12 {

    // >>> numIslands
    /** Count islands of '1' cells (4-directional). Each DFS sinks one whole island. O(R*C). */
    static int numIslands(char[][] grid) {
        int count = 0;
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++)
                if (grid[r][c] == '1') { count++; sink(grid, r, c); }
        return count;
    }

    private static void sink(char[][] g, int r, int c) {
        if (r < 0 || c < 0 || r >= g.length || c >= g[0].length || g[r][c] != '1') return;
        g[r][c] = '0';                                   // mark visited by changing the cell
        sink(g, r + 1, c); sink(g, r - 1, c); sink(g, r, c + 1); sink(g, r, c - 1);
    }
    // <<< numIslands

    // >>> isBipartite
    /** Can the vertices be split into two groups with every edge going across? BFS 2-colouring. */
    static boolean isBipartite(List<List<Integer>> adj) {
        int[] color = new int[adj.size()];               // 0 = uncoloured, 1 / -1 = the two sides
        for (int s = 0; s < adj.size(); s++) {
            if (color[s] != 0) continue;
            Queue<Integer> q = new ArrayDeque<>();
            q.offer(s);
            color[s] = 1;
            while (!q.isEmpty()) {
                int u = q.poll();
                for (int v : adj.get(u)) {
                    if (color[v] == 0) { color[v] = -color[u]; q.offer(v); }
                    else if (color[v] == color[u]) return false;   // edge inside one group
                }
            }
        }
        return true;
    }
    // <<< isBipartite

    // >>> canFinish
    /** Can all courses be finished? pre[i] = {course, prerequisite}. Kahn's algorithm counts processed courses. */
    static boolean canFinish(int n, int[][] pre) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        int[] indeg = new int[n];
        for (int[] p : pre) { adj.get(p[1]).add(p[0]); indeg[p[0]]++; }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.offer(i);
        int done = 0;
        while (!q.isEmpty()) {
            int u = q.poll();
            done++;
            for (int v : adj.get(u)) if (--indeg[v] == 0) q.offer(v);
        }
        return done == n;
    }
    // <<< canFinish

    // >>> alienOrder
    /** Letter order of an unknown alphabet from a sorted word list ("" if impossible). */
    static String alienOrder(String[] words) {
        Map<Character, List<Character>> adj = new TreeMap<>();
        Map<Character, Integer> indeg = new HashMap<>();
        for (String w : words) for (char c : w.toCharArray()) { adj.putIfAbsent(c, new ArrayList<>()); indeg.putIfAbsent(c, 0); }
        for (int i = 0; i + 1 < words.length; i++) {
            String a = words[i], b = words[i + 1];
            int j = 0;
            while (j < a.length() && j < b.length() && a.charAt(j) == b.charAt(j)) j++;
            if (j == Math.min(a.length(), b.length())) {
                if (a.length() > b.length()) return "";   // "abc" before "ab" is invalid
                continue;
            }
            adj.get(a.charAt(j)).add(b.charAt(j));        // first difference: a[j] comes before b[j]
            indeg.merge(b.charAt(j), 1, Integer::sum);
        }
        PriorityQueue<Character> q = new PriorityQueue<>();
        for (char c : adj.keySet()) if (indeg.get(c) == 0) q.offer(c);
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            char c = q.poll();
            sb.append(c);
            for (char d : adj.get(c)) if (indeg.merge(d, -1, Integer::sum) == 0) q.offer(d);
        }
        return sb.length() == adj.size() ? sb.toString() : "";
    }
    // <<< alienOrder

    // >>> networkDelay
    /** Time for a signal from k to reach all n nodes (1-based), or -1. times[i] = {u, v, w}. Dijkstra. */
    static int networkDelay(int[][] times, int n, int k) {
        ShortestPaths.WGraph g = new ShortestPaths.WGraph(n + 1);
        for (int[] t : times) g.add(t[0], t[1], t[2]);
        long[] d = ShortestPaths.dijkstra(g, k, new int[n + 1], null);
        long worst = 0;
        for (int v = 1; v <= n; v++) worst = Math.max(worst, d[v]);
        return worst >= ShortestPaths.INF / 2 ? -1 : (int) worst;
    }
    // <<< networkDelay

    // >>> cheapestWithinK
    /** Cheapest fare from src to dst using at most k stops: Bellman-Ford limited to k + 1 rounds. */
    static int cheapestWithinK(int n, int[][] flights, int src, int dst, int k) {
        long[] cost = new long[n];
        Arrays.fill(cost, Long.MAX_VALUE / 4);
        cost[src] = 0;
        for (int round = 0; round <= k; round++) {
            long[] next = cost.clone();                  // use LAST round's values: at most one more edge
            for (int[] f : flights)
                if (cost[f[0]] + f[2] < next[f[1]]) next[f[1]] = cost[f[0]] + f[2];
            cost = next;
        }
        return cost[dst] >= Long.MAX_VALUE / 8 ? -1 : (int) cost[dst];
    }
    // <<< cheapestWithinK

    // >>> redundantConnection
    /** The edge that, added last, creates a cycle in an otherwise-tree network. Union-find. */
    static int[] redundantConnection(int[][] edges) {
        MstUnionFind.UnionFind uf = new MstUnionFind.UnionFind(edges.length + 1);
        for (int[] e : edges) if (!uf.union(e[0], e[1])) return e;
        return new int[0];
    }
    // <<< redundantConnection

    // >>> minCostConnectPoints
    /** Connect all points with minimum total Manhattan length: Prim's O(n^2) for a dense graph. */
    static int minCostConnectPoints(int[][] p) {
        int n = p.length, total = 0;
        int[] best = new int[n];
        boolean[] in = new boolean[n];
        Arrays.fill(best, Integer.MAX_VALUE);
        best[0] = 0;
        for (int step = 0; step < n; step++) {
            int u = -1;
            for (int v = 0; v < n; v++) if (!in[v] && (u == -1 || best[v] < best[u])) u = v;
            in[u] = true;
            total += best[u];
            for (int v = 0; v < n; v++) {
                int d = Math.abs(p[u][0] - p[v][0]) + Math.abs(p[u][1] - p[v][1]);
                if (!in[v] && d < best[v]) best[v] = d;
            }
        }
        return total;
    }
    // <<< minCostConnectPoints

    static List<List<Integer>> adj(int n, int[][] edges) {
        List<List<Integer>> a = new ArrayList<>();
        for (int i = 0; i < n; i++) a.add(new ArrayList<>());
        for (int[] e : edges) { a.get(e[0]).add(e[1]); a.get(e[1]).add(e[0]); }
        return a;
    }

    public static void main(String[] args) {
        int s = args.length > 0 ? Integer.parseInt(args[0]) : 0;
        if (s == 0 || s == 1) {
            System.out.println("-- Session 1 labs --");
            char[][] grid = {"11000".toCharArray(), "11010".toCharArray(), "00100".toCharArray(), "00011".toCharArray()};
            System.out.println("numIslands = " + numIslands(grid));
            System.out.println("isBipartite(square 0-1-2-3-0) = " + isBipartite(adj(4, new int[][]{{0, 1}, {1, 2}, {2, 3}, {3, 0}}))
                    + ", isBipartite(triangle) = " + isBipartite(adj(3, new int[][]{{0, 1}, {1, 2}, {2, 0}})));
        }
        if (s == 0 || s == 2) {
            System.out.println("-- Session 2 labs --");
            System.out.println("canFinish(4, [[1,0],[2,1],[3,2]]) = " + canFinish(4, new int[][]{{1, 0}, {2, 1}, {3, 2}})
                    + ", canFinish(2, [[1,0],[0,1]]) = " + canFinish(2, new int[][]{{1, 0}, {0, 1}}));
            System.out.println("alienOrder([wrt, wrf, er, ett, rftt]) = " + alienOrder(new String[]{"wrt", "wrf", "er", "ett", "rftt"}));
        }
        if (s == 0 || s == 3) {
            System.out.println("-- Session 3 labs --");
            System.out.println("networkDelay = " + networkDelay(new int[][]{{2, 1, 1}, {2, 3, 1}, {3, 4, 1}}, 4, 2));
            int[][] flights = {{0, 1, 100}, {1, 2, 100}, {2, 3, 100}, {0, 2, 500}, {1, 3, 600}};
            System.out.println("cheapest 0->3 with <=1 stop = " + cheapestWithinK(4, flights, 0, 3, 1)
                    + ", with <=2 stops = " + cheapestWithinK(4, flights, 0, 3, 2));
        }
        if (s == 0 || s == 4) {
            System.out.println("-- Session 4 labs --");
            System.out.println("redundantConnection([[1,2],[1,3],[2,3]]) = "
                    + Arrays.toString(redundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}})));
            System.out.println("minCostConnectPoints([[0,0],[2,2],[3,10],[5,2],[7,0]]) = "
                    + minCostConnectPoints(new int[][]{{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}}));
        }
    }
}
