import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Module 12 self-check: JUnit-free tests for graph algorithms (cross-checked against each other).
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
        Random r = new Random(12);

        // BFS distances equal Floyd-Warshall with unit weights; components agree with reachability
        boolean bfsOk = true, compOk = true, dfsOk = true;
        for (int t = 0; t < 30; t++) {
            int n = 2 + r.nextInt(15);
            GraphBasics.Graph g = new GraphBasics.Graph(n, false);
            ShortestPaths.WGraph w = new ShortestPaths.WGraph(n);
            for (int k = 0; k < n; k++) { int u = r.nextInt(n), v = r.nextInt(n); if (u != v) { g.addEdge(u, v); w.addBoth(u, v, 1); } }
            long[][] fw = ShortestPaths.floydWarshall(w);
            int[] comp = new int[n];
            GraphBasics.components(g, comp);
            for (int s = 0; s < n; s++) {
                int[] d = GraphBasics.bfs(g, s, new int[n]);
                for (int v = 0; v < n; v++) {
                    long expect = fw[s][v] >= ShortestPaths.INF / 2 ? -1 : fw[s][v];
                    bfsOk &= d[v] == expect;
                    compOk &= (comp[s] == comp[v]) == (d[v] != -1);
                }
                List<Integer> order = new ArrayList<>();
                GraphBasics.dfs(g, s, new boolean[n], order);
                dfsOk &= order.equals(GraphBasics.dfsIterative(g, s));
            }
        }
        check(bfsOk, "BFS distances = Floyd-Warshall on unit weights");
        check(compOk, "components = reachability");
        check(dfsOk, "iterative DFS order = recursive DFS order");

        // Topological orders respect every edge; cycles are detected
        GraphBasics.Graph cur = TopologicalSort.curriculum();
        for (List<Integer> order : List.of(TopologicalSort.kahn(cur), TopologicalSort.dfsOrder(cur), TopologicalSort.kahnSmallestFirst(cur))) {
            int[] pos = new int[cur.n];
            for (int i = 0; i < order.size(); i++) pos[order.get(i)] = i;
            boolean ok = order.size() == cur.n;
            for (int u = 0; u < cur.n; u++) for (int v : cur.neighbours(u)) ok &= pos[u] < pos[v];
            check(ok, "topological order valid");
        }
        check(TopologicalSort.findCycle(cur).isEmpty(), "DAG has no cycle");
        GraphBasics.Graph bad = TopologicalSort.curriculum();
        bad.addEdge(7, 0);
        check(!TopologicalSort.findCycle(bad).isEmpty() && TopologicalSort.kahn(bad).isEmpty(), "cycle detected");
        check(TopologicalSort.semesters(cur)[7] == 5, "FYP in semester 5");

        // Dijkstra = Bellman-Ford = Floyd-Warshall on random non-negative graphs
        boolean spOk = true;
        for (int t = 0; t < 40; t++) {
            int n = 2 + r.nextInt(12);
            ShortestPaths.WGraph g = new ShortestPaths.WGraph(n);
            for (int k = 0; k < 3 * n; k++) g.add(r.nextInt(n), r.nextInt(n), r.nextInt(20));
            long[][] fw = ShortestPaths.floydWarshall(g);
            for (int s = 0; s < n; s++) {
                long[] dj = ShortestPaths.dijkstra(g, s, new int[n], null), bf = ShortestPaths.bellmanFord(g, s);
                for (int v = 0; v < n; v++) spOk &= dj[v] == bf[v] && (dj[v] == fw[s][v] || (dj[v] >= ShortestPaths.INF / 2 && fw[s][v] >= ShortestPaths.INF / 2));
            }
        }
        check(spOk, "Dijkstra = Bellman-Ford = Floyd-Warshall (random graphs)");
        ShortestPaths.WGraph cyc = new ShortestPaths.WGraph(2);
        cyc.add(0, 1, -1); cyc.add(1, 0, -1);
        check(ShortestPaths.bellmanFord(cyc, 0) == null, "negative cycle detected");

        // Kruskal total = Prim total on random connected graphs; MST has n-1 edges
        boolean mstOk = true;
        for (int t = 0; t < 40; t++) {
            int n = 2 + r.nextInt(20);
            ShortestPaths.WGraph g = new ShortestPaths.WGraph(n);
            List<ShortestPaths.Edge> und = new ArrayList<>();
            for (int v = 1; v < n; v++) { int u = r.nextInt(v), w = r.nextInt(50); g.addBoth(u, v, w); und.add(new ShortestPaths.Edge(u, v, w)); }
            for (int k = 0; k < n; k++) { int u = r.nextInt(n), v = r.nextInt(n), w = r.nextInt(50); if (u != v) { g.addBoth(u, v, w); und.add(new ShortestPaths.Edge(u, v, w)); } }
            List<ShortestPaths.Edge> k = MstUnionFind.kruskal(n, und, null), p = MstUnionFind.prim(g);
            mstOk &= k.size() == n - 1 && p.size() == n - 1 && MstUnionFind.total(k) == MstUnionFind.total(p);
        }
        check(mstOk, "Kruskal and Prim agree on MST weight");
        MstUnionFind.UnionFind uf = new MstUnionFind.UnionFind(5);
        uf.union(0, 1); uf.union(3, 4);
        check(uf.sets() == 3 && uf.connected(1, 0) && !uf.connected(1, 3) && !uf.union(4, 3), "union-find basics");

        // labs
        check(Labs12.numIslands(new char[][]{"111".toCharArray(), "010".toCharArray(), "111".toCharArray()}) == 1, "one island");
        check(Labs12.canFinish(3, new int[][]{{1, 0}, {2, 1}}) && !Labs12.canFinish(3, new int[][]{{0, 1}, {1, 2}, {2, 0}}), "canFinish");
        check(Labs12.alienOrder(new String[]{"z", "x"}).equals("zx") && Labs12.alienOrder(new String[]{"abc", "ab"}).isEmpty(), "alienOrder");
        check(Labs12.networkDelay(new int[][]{{1, 2, 1}}, 2, 2) == -1, "network delay unreachable");
        check(Labs12.cheapestWithinK(3, new int[][]{{0, 1, 100}, {1, 2, 100}, {0, 2, 500}}, 0, 2, 0) == 500, "cheapest with 0 stops");
        check(java.util.Arrays.equals(Labs12.redundantConnection(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}}), new int[]{1, 4}), "redundant connection");
        check(Labs12.minCostConnectPoints(new int[][]{{3, 12}, {-2, 5}, {-4, 1}}) == 18, "min cost connect points");

        System.out.println("Module 12 self-check: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
