import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

/**
 * Session 1 demo: graph representations (adjacency matrix and adjacency list), breadth-first search
 * with distances and path reconstruction, depth-first search (recursive and iterative), connected
 * components and cycle detection in an undirected graph.
 *
 * Compile:  javac -d out GraphBasics.java
 * Run:      java -cp out GraphBasics
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class GraphBasics {

    /** Adjacency-list graph on vertices 0..n-1, directed or undirected. */
    static final class Graph {
        final int n;
        final boolean directed;
        final List<List<Integer>> adj = new ArrayList<>();
        int edges;

        Graph(int n, boolean directed) {
            this.n = n;
            this.directed = directed;
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        }

        void addEdge(int u, int v) {
            adj.get(u).add(v);
            if (!directed) adj.get(v).add(u);
            edges++;
        }

        List<Integer> neighbours(int u) { return adj.get(u); }
        int degree(int u) { return adj.get(u).size(); }

        boolean[][] toMatrix() {
            boolean[][] m = new boolean[n][n];
            for (int u = 0; u < n; u++) for (int v : adj.get(u)) m[u][v] = true;
            return m;
        }
    }

    /** BFS from s: dist[v] = fewest edges from s (-1 if unreachable), parent[] encodes the BFS tree. */
    static int[] bfs(Graph g, int s, int[] parent) {
        int[] dist = new int[g.n];
        Arrays.fill(dist, -1);
        Arrays.fill(parent, -1);
        Queue<Integer> q = new ArrayDeque<>();
        dist[s] = 0;
        q.offer(s);
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : g.neighbours(u)) {
                if (dist[v] == -1) {            // first time seen = shortest distance
                    dist[v] = dist[u] + 1;
                    parent[v] = u;
                    q.offer(v);
                }
            }
        }
        return dist;
    }

    /** Walk parent links back from t to rebuild the path s -> t. */
    static List<Integer> path(int[] parent, int s, int t) {
        List<Integer> p = new ArrayList<>();
        for (int v = t; v != -1; v = parent[v]) p.add(v);
        Collections.reverse(p);
        return p.get(0) == s ? p : List.of();
    }

    /** Recursive DFS: records the visit (preorder) sequence. */
    static void dfs(Graph g, int u, boolean[] seen, List<Integer> order) {
        seen[u] = true;
        order.add(u);
        for (int v : g.neighbours(u)) if (!seen[v]) dfs(g, v, seen, order);
    }

    /** Iterative DFS with an explicit stack. Neighbours pushed in reverse so the order matches recursion. */
    static List<Integer> dfsIterative(Graph g, int s) {
        boolean[] seen = new boolean[g.n];
        List<Integer> order = new ArrayList<>();
        Deque<Integer> st = new ArrayDeque<>();
        st.push(s);
        while (!st.isEmpty()) {
            int u = st.pop();
            if (seen[u]) continue;
            seen[u] = true;
            order.add(u);
            List<Integer> nb = g.neighbours(u);
            for (int i = nb.size() - 1; i >= 0; i--) if (!seen[nb.get(i)]) st.push(nb.get(i));
        }
        return order;
    }

    /** Label every vertex with its component number; returns the number of components. */
    static int components(Graph g, int[] comp) {
        Arrays.fill(comp, -1);
        int c = 0;
        for (int s = 0; s < g.n; s++) {
            if (comp[s] != -1) continue;
            Deque<Integer> st = new ArrayDeque<>();
            st.push(s);
            comp[s] = c;
            while (!st.isEmpty()) {
                int u = st.pop();
                for (int v : g.neighbours(u)) if (comp[v] == -1) { comp[v] = c; st.push(v); }
            }
            c++;
        }
        return c;
    }

    /** Undirected cycle check: meeting an already-visited vertex that is not our parent closes a cycle. */
    static boolean hasCycleUndirected(Graph g) {
        boolean[] seen = new boolean[g.n];
        for (int s = 0; s < g.n; s++) if (!seen[s] && cycleFrom(g, s, -1, seen)) return true;
        return false;
    }

    private static boolean cycleFrom(Graph g, int u, int parent, boolean[] seen) {
        seen[u] = true;
        for (int v : g.neighbours(u)) {
            if (!seen[v]) { if (cycleFrom(g, v, u, seen)) return true; }
            else if (v != parent) return true;
        }
        return false;
    }

    static final String[] CAMPUS = {"Gate", "Library", "Cafe", "CS Block", "Admin", "Labs", "Hostel", "Sports"};

    /** The campus walkway graph used throughout the session. */
    static Graph campus() {
        Graph g = new Graph(8, false);
        int[][] e = {{0, 1}, {0, 4}, {1, 2}, {1, 3}, {2, 3}, {3, 5}, {4, 5}, {6, 7}};
        for (int[] x : e) g.addEdge(x[0], x[1]);
        return g;
    }

    public static void main(String[] args) {
        Graph g = campus();
        System.out.println("== Campus walkways: " + g.n + " places, " + g.edges + " walkways ==");
        for (int u = 0; u < g.n; u++) System.out.printf("%d %-8s -> %s (degree %d)%n", u, CAMPUS[u], g.neighbours(u), g.degree(u));

        System.out.println("\nAdjacency matrix (1 = walkway):");
        boolean[][] m = g.toMatrix();
        for (boolean[] row : m) {
            StringBuilder sb = new StringBuilder("  ");
            for (boolean b : row) sb.append(b ? "1 " : ". ");
            System.out.println(sb.toString().trim());
        }

        System.out.println("\n== BFS from Gate (0) ==");
        int[] parent = new int[g.n];
        int[] dist = bfs(g, 0, parent);
        System.out.println("dist   = " + Arrays.toString(dist));
        System.out.println("parent = " + Arrays.toString(parent));
        List<String> names = new ArrayList<>();
        for (int v : path(parent, 0, 5)) names.add(CAMPUS[v]);
        System.out.println("shortest walk Gate -> Labs: " + String.join(" -> ", names) + " (" + dist[5] + " walkways)");
        System.out.println("Hostel reachable from Gate? " + (dist[6] != -1));

        System.out.println("\n== DFS from Gate ==");
        List<Integer> order = new ArrayList<>();
        dfs(g, 0, new boolean[g.n], order);
        System.out.println("recursive DFS order = " + order);
        System.out.println("iterative DFS order = " + dfsIterative(g, 0));

        System.out.println("\n== Connected components ==");
        int[] comp = new int[g.n];
        System.out.println(components(g, comp) + " components, labels = " + Arrays.toString(comp));

        System.out.println("\n== Cycle detection ==");
        System.out.println("campus has a cycle? " + hasCycleUndirected(g) + "  (Library-Cafe-CS Block)");
        Graph tree = new Graph(4, false);
        tree.addEdge(0, 1); tree.addEdge(1, 2); tree.addEdge(1, 3);
        System.out.println("a tree has a cycle?  " + hasCycleUndirected(tree));
    }
}
