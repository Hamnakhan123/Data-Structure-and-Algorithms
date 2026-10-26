# Source code · Module 11 · Heaps and Priority Queues

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`BinaryHeap.java`](BinaryHeap.java) | Session 1 demo: an array-based binary heap (min-heap by default, any Comparator) with insert/sift-up, extract/sift-down, O(n) bottom-up heapify, and a comparison of the one-by-one and bottom-up build costs. | `java -ea -cp out BinaryHeap` |
| [`Labs11.java`](Labs11.java) | Module 11 — reference solutions for the lab tasks of Sessions 1-2. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs11` |
| [`PriorityQueueApps.java`](PriorityQueueApps.java) | Session 2 demo: java.util.PriorityQueue with comparators, top-k selection, merging k sorted lists, a running median with two heaps, and a priority-based job scheduler simulation. | `java -ea -cp out PriorityQueueApps` |
| [`SelfCheck.java`](SelfCheck.java) | Module 11 self-check: JUnit-free tests for heaps and priority queues. javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out BinaryHeap           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 11 self-check: 44 passed, 0 failed
```
