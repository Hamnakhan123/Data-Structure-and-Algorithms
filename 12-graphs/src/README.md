# Source code · Module 12 · Graphs

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`GraphBasics.java`](GraphBasics.java) | Session 1 demo: graph representations (adjacency matrix and adjacency list), breadth-first search with distances and path reconstruction, depth-first search (recursive and iterative), connected components and cycle detection in an undirected graph. | `java -ea -cp out GraphBasics` |
| [`Labs12.java`](Labs12.java) | Module 12 — reference solutions for the lab tasks of Sessions 1-4. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs12` |
| [`MstUnionFind.java`](MstUnionFind.java) | Session 4 demo: the disjoint-set union (union-find) structure with path compression and union by rank, Kruskal's and Prim's minimum-spanning-tree algorithms on a campus fibre-cabling problem, and a measurement of how flat path compression keeps the union-find trees. | `java -ea -cp out MstUnionFind` |
| [`SelfCheck.java`](SelfCheck.java) | Module 12 self-check: JUnit-free tests for graph algorithms (cross-checked against each other). javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |
| [`ShortestPaths.java`](ShortestPaths.java) | Session 3 demo: single-source shortest paths in weighted graphs. Dijkstra's algorithm with a priority queue (lazy deletion) and path reconstruction, Bellman-Ford with negative-cycle detection, Floyd-Warshall for all pairs, and a demonstration of why Dijkstra fails with negative edges. | `java -ea -cp out ShortestPaths` |
| [`TopologicalSort.java`](TopologicalSort.java) | Session 2 demo: topological sorting of a directed acyclic graph (course prerequisites) with Kahn's algorithm (in-degrees + queue) and with DFS finishing order, plus directed-cycle detection using three colours and a semester planner (longest path in a DAG). | `java -ea -cp out TopologicalSort` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out GraphBasics           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 12 self-check: 20 passed, 0 failed
```
