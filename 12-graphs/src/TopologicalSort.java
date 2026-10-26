import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Session 2 demo: topological sorting of a directed acyclic graph (course prerequisites) with
 * Kahn's algorithm (in-degrees + queue) and with DFS finishing order, plus directed-cycle detection
 * using three colours and a semester planner (longest path in a DAG).
 *
 * Compile:  javac -d out GraphBasics.java TopologicalSort.java
 * Run:      java -cp out TopologicalSort
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class TopologicalSort {

    static final String[] COURSES = {"PF", "OOP", "DSA", "DB", "OS", "AI", "SE", "FYP"};

    /** Prerequisite graph: edge u -> v means "u must be passed before v". */
    static GraphBasics.Graph curriculum() {
        GraphBasics.Graph g = new GraphBasics.Graph(8, true);
        int[][] e = {{0, 1}, {1, 2}, {1, 3}, {2, 4}, {2, 5}, {3, 6}, {1, 6}, {5, 7}, {6, 7}, {4, 7}};
        for (int[] x : e) g.addEdge(x[0], x[1]);
        return g;
    }

    /** Kahn's algorithm. Returns a topological order, or an empty list if the graph has a cycle. */
    static List<Integer> kahn(GraphBasics.Graph g) {
        int[] indeg = new int[g.n];
        for (int u = 0; u < g.n; u++) for (int v : g.neighbours(u)) indeg[v]++;
        Queue<Integer> ready = new ArrayDeque<>();
        for (int v = 0; v < g.n; v++) if (indeg[v] == 0) ready.offer(v);       // no prerequisites
        List<Integer> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            int u = ready.poll();
            order.add(u);
            for (int v : g.neighbours(u)) if (--indeg[v] == 0) ready.offer(v);  // last prerequisite done
        }
        return order.size() == g.n ? order : List.of();
    }

    /** Kahn with a min-heap instead of a queue: the lexicographically smallest topological order. */
    static List<Integer> kahnSmallestFirst(GraphBasics.Graph g) {
        int[] indeg = new int[g.n];
        for (int u = 0; u < g.n; u++) for (int v : g.neighbours(u)) indeg[v]++;
        PriorityQueue<Integer> ready = new PriorityQueue<>();
        for (int v = 0; v < g.n; v++) if (indeg[v] == 0) ready.offer(v);
        List<Integer> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            int u = ready.poll();
            order.add(u);
            for (int v : g.neighbours(u)) if (--indeg[v] == 0) ready.offer(v);
        }
        return order;
    }

    /** DFS-based topological sort: reverse of the order in which vertices FINISH. */
    static List<Integer> dfsOrder(GraphBasics.Graph g) {
        boolean[] seen = new boolean[g.n];
        List<Integer> finished = new ArrayList<>();
        for (int s = 0; s < g.n; s++) if (!seen[s]) visit(g, s, seen, finished);
        Collections.reverse(finished);
        return finished;
    }

    private static void visit(GraphBasics.Graph g, int u, boolean[] seen, List<Integer> finished) {
        seen[u] = true;
        for (int v : g.neighbours(u)) if (!seen[v]) visit(g, v, seen, finished);
        finished.add(u);                         // all descendants are done
    }

    /** Directed cycle detection with colours: WHITE unvisited, GREY on the current path, BLACK finished. */
    static final int WHITE = 0, GREY = 1, BLACK = 2;

    static List<Integer> findCycle(GraphBasics.Graph g) {
        int[] color = new int[g.n], parent = new int[g.n];
        for (int s = 0; s < g.n; s++) {
            if (color[s] != WHITE) continue;
            List<Integer> c = cycleDfs(g, s, color, parent);
            if (!c.isEmpty()) return c;
        }
        return List.of();
    }

    private static List<Integer> cycleDfs(GraphBasics.Graph g, int u, int[] color, int[] parent) {
        color[u] = GREY;
        for (int v : g.neighbours(u)) {
            if (color[v] == GREY) {                 // back edge to an ancestor: cycle v -> ... -> u -> v
                List<Integer> cyc = new ArrayList<>();
                for (int x = u; x != v; x = parent[x]) cyc.add(x);
                cyc.add(v);
                Collections.reverse(cyc);
                cyc.add(v);
                return cyc;
            }
            if (color[v] == WHITE) {
                parent[v] = u;
                List<Integer> c = cycleDfs(g, v, color, parent);
                if (!c.isEmpty()) return c;
            }
        }
        color[u] = BLACK;
        return List.of();
    }

    /** Earliest semester for each course = 1 + max(semester of prerequisites): longest path in a DAG. */
    static int[] semesters(GraphBasics.Graph g) {
        int[] sem = new int[g.n];
        java.util.Arrays.fill(sem, 1);
        for (int u : kahn(g)) for (int v : g.neighbours(u)) sem[v] = Math.max(sem[v], sem[u] + 1);
        return sem;
    }

    static String names(List<Integer> ids) {
        List<String> s = new ArrayList<>();
        for (int i : ids) s.add(COURSES[i]);
        return String.join(" -> ", s);
    }

    public static void main(String[] args) {
        GraphBasics.Graph g = curriculum();
        System.out.println("== Prerequisites (u -> v: pass u before v) ==");
        for (int u = 0; u < g.n; u++) {
            List<String> to = new ArrayList<>();
            for (int v : g.neighbours(u)) to.add(COURSES[v]);
            System.out.printf("%-4s -> %s%n", COURSES[u], to);
        }

        System.out.println("\nKahn (queue)           : " + names(kahn(g)));
        System.out.println("Kahn (smallest id first): " + names(kahnSmallestFirst(g)));
        System.out.println("DFS finishing order rev : " + names(dfsOrder(g)));

        int[] sem = semesters(g);
        System.out.println("\n== Earliest semester for each course (one course per prerequisite chain per term) ==");
        for (int s = 1; s <= 5; s++) {
            List<String> in = new ArrayList<>();
            for (int v = 0; v < g.n; v++) if (sem[v] == s) in.add(COURSES[v]);
            if (!in.isEmpty()) System.out.println("semester " + s + ": " + in);
        }

        System.out.println("\n== A bad catalogue: OS also required before DSA ==");
        GraphBasics.Graph bad = curriculum();
        bad.addEdge(4, 2);
        System.out.println("Kahn result size = " + kahn(bad).size() + " of " + bad.n + "  -> not a DAG");
        System.out.println("cycle found: " + names(findCycle(bad)));
        System.out.println("original catalogue cycle: " + (findCycle(g).isEmpty() ? "none" : names(findCycle(g))));
    }
}
