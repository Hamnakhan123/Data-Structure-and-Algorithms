# Source code · Module 09 · Trees and Binary Search Trees

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`BinarySearchTree.java`](BinarySearchTree.java) | Session 2 demo: a binary search tree of int keys with insert, search, min/max, floor/ceiling, delete (all three cases), range queries, and a demonstration of how insertion order controls height. | `java -ea -cp out BinarySearchTree` |
| [`BinaryTrees.java`](BinaryTrees.java) | Session 1 demo: binary-tree nodes, the four traversals (recursive and iterative), and basic recursive measurements: size, height, leaves, and a sideways picture of the tree. | `java -ea -cp out BinaryTrees` |
| [`Labs09.java`](Labs09.java) | Module 09 — reference solutions for the lab tasks of Sessions 1-2. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs09` |
| [`SelfCheck.java`](SelfCheck.java) | Module 09 self-check: JUnit-free tests for binary trees and BSTs. javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out BinarySearchTree           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 09 self-check: 17 passed, 0 failed
```
