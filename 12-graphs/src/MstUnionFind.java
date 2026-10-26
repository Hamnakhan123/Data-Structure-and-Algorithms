import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Session 4 demo: the disjoint-set union (union-find) structure with path compression and union by
 * rank, Kruskal's and Prim's minimum-spanning-tree algorithms on a campus fibre-cabling problem, and a
 * measurement of how flat path compression keeps the union-find trees.
 *
 * Compile:  javac -d out ShortestPaths.java MstUnionFind.java
 * Run:      java -cp out MstUnionFind
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class MstUnionFind {

    /** Disjoint-set union: each set is a tree; the root is the set's representative. */
    static final class UnionFind {
        private final int[] parent, rank;
        private int sets;

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            sets = n;
            for (int i = 0; i < n; i++) parent[i] = i;      // everyone starts alone
        }

        /** Representative of x's set, flattening the path on the way (path compression). */
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        /** Merge the sets of a and b; returns false if they were already together. Union by rank. */
        boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false;
            if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
            parent[rb] = ra;                                   // shorter tree hangs under the taller
            if (rank[ra] == rank[rb]) rank[ra]++;
            sets--;
            return true;
        }

        boolean connected(int a, int b) { return find(a) == find(b); }
        int sets() { return sets; }

        int depth(int x) { int d = 0; while (parent[x] != x) { x = parent[x]; d++; } return d; }
    }

    /** Kruskal: sort edges by weight, add each edge that does not close a cycle. O(E log E). */
    static List<ShortestPaths.Edge> kruskal(int n, List<ShortestPaths.Edge> edges, List<String> trace) {
        List<ShortestPaths.Edge> sorted = new ArrayList<>(edges);
        sorted.sort(Comparator.comparingInt(ShortestPaths.Edge::w));
        UnionFind uf = new UnionFind(n);
        List<ShortestPaths.Edge> mst = new ArrayList<>();
        for (ShortestPaths.Edge e : sorted) {
            boolean taken = uf.union(e.from(), e.to());
            if (trace != null) trace.add(String.format("%s-%s (%d) %s", B[e.from()], B[e.to()], e.w(), taken ? "take" : "skip: cycle"));
            if (taken) mst.add(e);
            if (mst.size() == n - 1) break;
        }
        return mst;
    }

    /** Prim (lazy): grow one tree from vertex 0, always adding the cheapest edge leaving it. O(E log E). */
    static List<ShortestPaths.Edge> prim(ShortestPaths.WGraph g) {
        boolean[] in = new boolean[g.n];
        PriorityQueue<ShortestPaths.Edge> pq = new PriorityQueue<>(Comparator.comparingInt(ShortestPaths.Edge::w));
        List<ShortestPaths.Edge> mst = new ArrayList<>();
        in[0] = true;
        pq.addAll(g.adj.get(0));
        while (!pq.isEmpty() && mst.size() < g.n - 1) {
            ShortestPaths.Edge e = pq.poll();
            if (in[e.to()]) continue;                          // both ends already in the tree
            in[e.to()] = true;
            mst.add(e);
            for (ShortestPaths.Edge f : g.adj.get(e.to())) if (!in[f.to()]) pq.offer(f);
        }
        return mst;
    }

    static int total(List<ShortestPaths.Edge> es) { int s = 0; for (ShortestPaths.Edge e : es) s += e.w(); return s; }

    static final String[] B = {"Admin", "Library", "CS", "Labs", "Hostel", "Cafe"};

    /** Possible fibre routes between campus buildings, cost in hundreds of metres of cable. */
    static ShortestPaths.WGraph campusCabling() {
        ShortestPaths.WGraph g = new ShortestPaths.WGraph(6);
        int[][] e = {{0, 1, 4}, {0, 2, 3}, {1, 2, 1}, {1, 3, 2}, {2, 3, 4}, {3, 4, 2}, {4, 5, 6}, {2, 5, 7}, {3, 5, 5}};
        for (int[] x : e) g.addBoth(x[0], x[1], x[2]);
        return g;
    }

    static String show(List<ShortestPaths.Edge> es) {
        List<String> s = new ArrayList<>();
        for (ShortestPaths.Edge e : es) s.add(B[e.from()] + "-" + B[e.to()] + "(" + e.w() + ")");
        return String.join(", ", s);
    }

    public static void main(String[] args) {
        System.out.println("== Union-find basics (10 students, study groups) ==");
        UnionFind uf = new UnionFind(10);
        int[][] pairs = {{0, 1}, {2, 3}, {1, 3}, {5, 6}, {7, 8}, {8, 9}, {3, 0}};
        for (int[] p : pairs)
            System.out.printf("union(%d, %d) -> %-5s groups now %d%n", p[0], p[1], uf.union(p[0], p[1]), uf.sets());
        System.out.println("connected(0, 2) = " + uf.connected(0, 2) + ", connected(4, 5) = " + uf.connected(4, 5));

        ShortestPaths.WGraph g = campusCabling();
        List<ShortestPaths.Edge> undirected = new ArrayList<>();
        for (ShortestPaths.Edge e : g.edges) if (e.from() < e.to()) undirected.add(e);

        System.out.println("\n== Kruskal on the campus cabling graph ==");
        List<String> trace = new ArrayList<>();
        List<ShortestPaths.Edge> k = kruskal(g.n, undirected, trace);
        for (String line : trace) System.out.println("  " + line);
        System.out.println("MST: " + show(k) + "  total = " + total(k));

        System.out.println("\n== Prim from Admin ==");
        List<ShortestPaths.Edge> p = prim(g);
        System.out.println("MST: " + show(p) + "  total = " + total(p));
        int all = 0;
        for (ShortestPaths.Edge e : undirected) all += e.w();
        System.out.println("cabling every route would cost " + all + "; the MST saves " + (all - total(p)));

        System.out.println("\n== Path compression keeps trees flat ==");
        int n = 1_000_000;
        UnionFind big = new UnionFind(n);
        java.util.Random r = new java.util.Random(12);
        for (int i = 0; i < n; i++) big.union(r.nextInt(n), r.nextInt(n));
        for (int i = 0; i < n; i++) big.find(i);
        int max = 0;
        for (int i = 0; i < n; i++) max = Math.max(max, big.depth(i));
        System.out.println(n + " random unions, then find on all: max depth = " + max + ", sets = " + big.sets());
    }
}
