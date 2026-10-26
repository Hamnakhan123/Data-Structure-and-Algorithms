# Module 12 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 90 minutes

## Part A · Multiple-choice questions

**Q1.** An adjacency matrix for a graph with V vertices needs:

- **A.** O(V) space
- **B.** O(E) space
- **C.** O(V²) space
- **D.** O(V + E) space

**Q2.** Checking whether edge (u, v) exists is O(1) in:

- **A.** an adjacency list
- **B.** an adjacency matrix
- **C.** an edge list
- **D.** none of these

**Q3.** BFS and DFS on an adjacency list take:

- **A.** O(V)
- **B.** O(E)
- **C.** O(V + E)
- **D.** O(V²)

**Q4.** In BFS, a vertex should be marked visited when it is:

- **A.** dequeued
- **B.** enqueued
- **C.** printed
- **D.** finished

**Q5.** Which traversal finds the minimum number of moves in a maze with equal-cost moves?

- **A.** DFS
- **B.** BFS
- **C.** inorder
- **D.** Prim

**Q6.** An undirected DFS meets a visited vertex that is not its parent. This shows:

- **A.** the graph is bipartite
- **B.** a cycle
- **C.** a bridge
- **D.** disconnection

**Q7.** A graph containing a triangle is:

- **A.** always bipartite
- **B.** never bipartite
- **C.** bipartite if connected
- **D.** a tree

**Q8.** Topological sorting is possible only for:

- **A.** trees
- **B.** DAGs
- **C.** complete graphs
- **D.** undirected graphs

**Q9.** Kahn's algorithm begins with vertices of:

- **A.** in-degree 0
- **B.** out-degree 0
- **C.** maximum degree
- **D.** even degree

**Q10.** DFS topological order is obtained from:

- **A.** discovery order
- **B.** reverse finishing order
- **C.** level order
- **D.** sorting vertex labels

**Q11.** In three-colour DFS on a directed graph, a cycle is found by an edge to a vertex that is:

- **A.** WHITE
- **B.** GREY
- **C.** BLACK
- **D.** isolated

**Q12.** Dijkstra's algorithm can produce wrong answers when:

- **A.** the graph is directed
- **B.** some edge weights are negative
- **C.** the graph has cycles
- **D.** weights are zero

**Q13.** With a binary heap, Dijkstra runs in:

- **A.** O(V²)
- **B.** O((V + E) log V)
- **C.** O(VE)
- **D.** O(V³)

**Q14.** Bellman-Ford detects a negative cycle by:

- **A.** running Dijkstra twice
- **B.** an improvement in an extra (V-th) round
- **C.** checking for zero in-degree
- **D.** sorting edges

**Q15.** The loop order in Floyd-Warshall must have which variable outermost?

- **A.** i
- **B.** j
- **C.** k (the intermediate vertex)
- **D.** any

**Q16.** The fastest correct algorithm for shortest paths in a weighted DAG with negative edges is:

- **A.** Dijkstra
- **B.** relaxation in topological order
- **C.** Floyd-Warshall
- **D.** BFS

**Q17.** A minimum spanning tree minimises:

- **A.** the longest edge on a path between two vertices
- **B.** the total weight of edges connecting all vertices
- **C.** the distance from one source to all others
- **D.** the number of edges

**Q18.** Kruskal's algorithm skips an edge when:

- **A.** its weight is the largest so far
- **B.** both endpoints are already in the same component
- **C.** it touches a leaf
- **D.** the queue is empty

**Q19.** Path compression in union-find:

- **A.** merges two sets
- **B.** points nodes on a find path directly to the root
- **C.** sorts the parent array
- **D.** deletes elements

**Q20.** For a dense graph with V = 5,000 and about V² edges, the best MST approach is:

- **A.** Kruskal with sorting
- **B.** Prim with an O(V²) array scan
- **C.** Bellman-Ford
- **D.** Floyd-Warshall

<details>
<summary><b>Show answers</b></summary>

1. **C**: One cell per ordered pair.
2. **B**: Direct array lookup.
3. **C**: Each vertex and edge is handled a constant number of times.
4. **B**: Otherwise it can be enqueued several times.
5. **B**: Fewest edges = BFS.
6. **B**: A second route back to an ancestor.
7. **B**: Odd cycles cannot be two-coloured.
8. **B**: A directed cycle has no valid first vertex.
9. **A**: They have no incoming dependencies.
10. **B**: Finished vertices have all descendants done.
11. **B**: GREY = on the current path.
12. **B**: Settled vertices could later improve.
13. **B**: Each edge may push once; each pop is log.
14. **B**: Without negative cycles, V − 1 rounds suffice.
15. **C**: d[i][j] uses intermediates ≤ k.
16. **B**: O(V + E).
17. **B**: It does not minimise individual routes.
18. **B**: It would form a cycle.
19. **B**: Flattens trees for later finds.
20. **B**: Avoids sorting ~25 million edges.

</details>

---

## Part B · Coding scenarios

### Scenario 1 · Fewest bus changes

A city's bus routes are listed as sets of stops. Travelling within one route is free; changing to another route counts as one transfer. Write `minBuses(int[][] routes, int src, int dst)` returning the fewest buses needed (0 if src == dst, -1 if impossible). Hint: BFS over **routes**, not stops.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class FewestBuses {
    static int minBuses(int[][] routes, int src, int dst) {
        if (src == dst) return 0;
        Map<Integer, List<Integer>> routesAt = new HashMap<>();           // stop -> routes through it
        for (int r = 0; r < routes.length; r++)
            for (int s : routes[r]) routesAt.computeIfAbsent(s, k -> new ArrayList<>()).add(r);
        boolean[] usedRoute = new boolean[routes.length];
        Queue<Integer> q = new ArrayDeque<>();
        for (int r : routesAt.getOrDefault(src, List.of())) { usedRoute[r] = true; q.offer(r); }
        for (int buses = 1; !q.isEmpty(); buses++) {
            for (int i = q.size(); i > 0; i--) {
                int r = q.poll();
                for (int s : routes[r]) {
                    if (s == dst) return buses;
                    for (int next : routesAt.get(s)) if (!usedRoute[next]) { usedRoute[next] = true; q.offer(next); }
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[][] routes = {{1, 2, 7}, {3, 6, 7}, {6, 9, 12}};
        System.out.println(minBuses(routes, 1, 6) + " " + minBuses(routes, 1, 12) + " " + minBuses(routes, 1, 99));
    }
}
```

Output: `2 3 -1`. Each route is enqueued once: O(total stops).

</details>

### Scenario 2 · Cheapest delivery route

A courier company has one-way roads `(from, to, minutes)` between 6 hubs. Write a Dijkstra-based `fastest(int n, int[][] roads, int s, int t)` that prints the minimum time and the route.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class CourierRoute {
    static String fastest(int n, int[][] roads, int s, int t) {
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] r : roads) adj.get(r[0]).add(new int[]{r[1], r[2]});
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[s] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        pq.offer(new long[]{0, s});
        while (!pq.isEmpty()) {
            long[] top = pq.poll();
            int u = (int) top[1];
            if (top[0] > dist[u]) continue;                      // stale
            for (int[] e : adj.get(u))
                if (dist[u] + e[1] < dist[e[0]]) {
                    dist[e[0]] = dist[u] + e[1];
                    parent[e[0]] = u;
                    pq.offer(new long[]{dist[e[0]], e[0]});
                }
        }
        if (dist[t] == Long.MAX_VALUE) return "unreachable";
        List<Integer> path = new ArrayList<>();
        for (int v = t; v != -1; v = parent[v]) path.add(v);
        Collections.reverse(path);
        return dist[t] + " min via " + path;
    }

    public static void main(String[] args) {
        int[][] roads = {{0, 1, 7}, {0, 2, 9}, {0, 5, 14}, {1, 2, 10}, {1, 3, 15}, {2, 3, 11}, {2, 5, 2}, {3, 4, 6}, {5, 4, 9}};
        System.out.println(fastest(6, roads, 0, 4));
        System.out.println(fastest(6, roads, 4, 0));
    }
}
```

Output: `20 min via [0, 2, 5, 4]` then `unreachable` (roads are one-way).

</details>

### Scenario 3 · Merge duplicate student accounts

The registration system has accounts `[name, email1, email2, ...]`. Two accounts belong to the same person if they share **any** email. Write `mergeCount(String[][] accounts)` that returns the number of distinct people, using union-find over account indices.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.HashMap;
import java.util.Map;

public class MergeAccounts {
    static int[] parent;

    static int find(int x) { return parent[x] == x ? x : (parent[x] = find(parent[x])); }

    static int mergeCount(String[][] accounts) {
        parent = new int[accounts.length];
        for (int i = 0; i < parent.length; i++) parent[i] = i;
        Map<String, Integer> owner = new HashMap<>();                 // email -> first account seen
        int people = accounts.length;
        for (int i = 0; i < accounts.length; i++)
            for (int j = 1; j < accounts[i].length; j++) {
                Integer o = owner.putIfAbsent(accounts[i][j], i);
                if (o != null) {
                    int a = find(o), b = find(i);
                    if (a != b) { parent[a] = b; people--; }
                }
            }
        return people;
    }

    public static void main(String[] args) {
        String[][] acc = {
            {"Sara", "sara@x.pk", "s.k@y.pk"},
            {"Sara", "sara.k@z.pk"},
            {"Sara K", "s.k@y.pk", "sara.k@z.pk"},
            {"Umer", "umer@x.pk"}};
        System.out.println(mergeCount(acc));
    }
}
```

Output: `2`. Account 2 shares an email with account 0 and another with account 1, so all three Sara accounts merge; Umer stays separate.

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
