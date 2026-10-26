import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Session 3 demo: single-source shortest paths in weighted graphs. Dijkstra's algorithm with a
 * priority queue (lazy deletion) and path reconstruction, Bellman-Ford with negative-cycle detection,
 * Floyd-Warshall for all pairs, and a demonstration of why Dijkstra fails with negative edges.
 *
 * Compile:  javac -d out ShortestPaths.java
 * Run:      java -cp out ShortestPaths
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class ShortestPaths {
    static final long INF = Long.MAX_VALUE / 4;           // large but safe to add to

    record Edge(int from, int to, int w) { }

    /** Weighted adjacency list. */
    static final class WGraph {
        final int n;
        final List<List<Edge>> adj = new ArrayList<>();
        final List<Edge> edges = new ArrayList<>();
        WGraph(int n) { this.n = n; for (int i = 0; i < n; i++) adj.add(new ArrayList<>()); }
        void add(int u, int v, int w) { Edge e = new Edge(u, v, w); adj.get(u).add(e); edges.add(e); }
        void addBoth(int u, int v, int w) { add(u, v, w); add(v, u, w); }
    }

    /** Dijkstra: repeatedly settle the closest unsettled vertex. Non-negative weights only. */
    static long[] dijkstra(WGraph g, int s, int[] parent, List<String> trace) {
        long[] dist = new long[g.n];
        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);
        dist[s] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));  // {dist, vertex}
        pq.offer(new long[]{0, s});
        boolean[] done = new boolean[g.n];
        while (!pq.isEmpty()) {
            long[] top = pq.poll();
            int u = (int) top[1];
            if (done[u]) continue;                 // stale entry (lazy deletion)
            done[u] = true;
            if (trace != null) trace.add("settle " + u + " at " + dist[u]);
            for (Edge e : g.adj.get(u)) {
                if (!done[e.to()] && dist[u] + e.w() < dist[e.to()]) {   // relaxation
                    dist[e.to()] = dist[u] + e.w();
                    parent[e.to()] = u;
                    pq.offer(new long[]{dist[e.to()], e.to()});
                }
            }
        }
        return dist;
    }

    /** Bellman-Ford: relax every edge n-1 times. Returns null if a negative cycle is reachable. */
    static long[] bellmanFord(WGraph g, int s) {
        long[] dist = new long[g.n];
        Arrays.fill(dist, INF);
        dist[s] = 0;
        for (int round = 1; round < g.n; round++) {
            boolean changed = false;
            for (Edge e : g.edges)
                if (dist[e.from()] != INF && dist[e.from()] + e.w() < dist[e.to()]) {
                    dist[e.to()] = dist[e.from()] + e.w();
                    changed = true;
                }
            if (!changed) break;                   // early exit: already optimal
        }
        for (Edge e : g.edges)                     // one more round: any improvement = negative cycle
            if (dist[e.from()] != INF && dist[e.from()] + e.w() < dist[e.to()]) return null;
        return dist;
    }

    /** Floyd-Warshall: all-pairs shortest paths in O(n^3) by allowing intermediate vertices 0..k. */
    static long[][] floydWarshall(WGraph g) {
        long[][] d = new long[g.n][g.n];
        for (long[] row : d) Arrays.fill(row, INF);
        for (int i = 0; i < g.n; i++) d[i][i] = 0;
        for (Edge e : g.edges) d[e.from()][e.to()] = Math.min(d[e.from()][e.to()], e.w());
        for (int k = 0; k < g.n; k++)
            for (int i = 0; i < g.n; i++)
                for (int j = 0; j < g.n; j++)
                    if (d[i][k] + d[k][j] < d[i][j]) d[i][j] = d[i][k] + d[k][j];
        return d;
    }

    static List<Integer> path(int[] parent, int t) {
        List<Integer> p = new ArrayList<>();
        for (int v = t; v != -1; v = parent[v]) p.add(v);
        Collections.reverse(p);
        return p;
    }

    static final String[] PLACES = {"Home", "Bus Stop", "Market", "Bridge", "Park", "University"};

    /** Travel times in minutes between places (two-way roads). */
    static WGraph city() {
        WGraph g = new WGraph(6);
        g.addBoth(0, 1, 4);   // Home - Bus Stop
        g.addBoth(0, 2, 2);   // Home - Market
        g.addBoth(1, 2, 1);   // Bus Stop - Market
        g.addBoth(1, 3, 5);   // Bus Stop - Bridge
        g.addBoth(2, 3, 8);   // Market - Bridge
        g.addBoth(2, 4, 10);  // Market - Park
        g.addBoth(3, 4, 2);   // Bridge - Park
        g.addBoth(3, 5, 6);   // Bridge - University
        g.addBoth(4, 5, 3);   // Park - University
        return g;
    }

    static String fmt(long[] d) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < d.length; i++) sb.append(i > 0 ? ", " : "").append(d[i] >= INF / 2 ? "∞" : String.valueOf(d[i]));
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        WGraph g = city();
        int[] parent = new int[g.n];
        List<String> trace = new ArrayList<>();
        long[] d = dijkstra(g, 0, parent, trace);
        System.out.println("== Dijkstra from Home ==");
        System.out.println(String.join(" | ", trace));
        for (int v = 0; v < g.n; v++) {
            List<String> names = new ArrayList<>();
            for (int x : path(parent, v)) names.add(PLACES[x]);
            System.out.printf("%-10s %2d min via %s%n", PLACES[v], d[v], String.join(" -> ", names));
        }

        System.out.println("\n== Bellman-Ford gives the same distances: " + fmt(bellmanFord(g, 0)));

        System.out.println("\n== Negative edge: Dijkstra goes wrong, Bellman-Ford does not ==");
        WGraph neg = new WGraph(4);
        neg.add(0, 1, 2); neg.add(0, 2, 4); neg.add(2, 1, -3); neg.add(1, 3, 2);
        System.out.println("edges: 0->1 (2), 0->2 (4), 2->1 (-3), 1->3 (2)");
        System.out.println("Dijkstra     : " + fmt(dijkstra(neg, 0, new int[4], null)) + "  <- wrong for 1 and 3");
        System.out.println("Bellman-Ford : " + fmt(bellmanFord(neg, 0)));

        System.out.println("\n== Negative cycle ==");
        WGraph cyc = new WGraph(3);
        cyc.add(0, 1, 1); cyc.add(1, 2, -2); cyc.add(2, 1, 1);
        System.out.println("edges: 0->1 (1), 1->2 (-2), 2->1 (1): cycle 1->2->1 has weight -1");
        System.out.println("Bellman-Ford returns " + (bellmanFord(cyc, 0) == null ? "null: negative cycle detected" : "distances"));

        System.out.println("\n== Floyd-Warshall: all-pairs travel times ==");
        long[][] all = floydWarshall(g);
        System.out.print("            ");
        for (int j = 0; j < g.n; j++) System.out.printf("%4d", j);
        System.out.println();
        for (int i = 0; i < g.n; i++) {
            System.out.printf("%d %-10s", i, PLACES[i]);
            for (int j = 0; j < g.n; j++) System.out.printf("%4d", all[i][j]);
            System.out.println();
        }
    }
}
