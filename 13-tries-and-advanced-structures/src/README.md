# Source code · Module 13 · Tries and Advanced Structures

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`FenwickTree.java`](FenwickTree.java) | Session 3 demo: the Fenwick tree (binary indexed tree). Point update and prefix sum in O(log n) using the lowest set bit, O(n) construction, range sums, the "range update / point query" variant, finding the k-th element by prefix count, and counting inversions. | `java -ea -cp out FenwickTree` |
| [`Labs13.java`](Labs13.java) | Module 13 — reference solutions for the lab tasks of Sessions 1-3. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs13` |
| [`SegmentTree.java`](SegmentTree.java) | Session 2 demo: segment trees. A point-update / range-sum tree and a range-minimum tree built recursively over an array, plus a lazy-propagation tree supporting "add v to every element in [l, r]" and range-sum queries, each in O(log n). | `java -ea -cp out SegmentTree` |
| [`SelfCheck.java`](SelfCheck.java) | Module 13 self-check: JUnit-free tests for tries, segment trees and Fenwick trees (vs brute force). javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |
| [`Trie.java`](Trie.java) | Session 1 demo: a trie (prefix tree) over lowercase words with insert, search, startsWith, prefix counting, autocomplete (sorted, limited), and deletion that prunes unused nodes. | `java -ea -cp out Trie` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out FenwickTree           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 13 self-check: 11 passed, 0 failed
```
