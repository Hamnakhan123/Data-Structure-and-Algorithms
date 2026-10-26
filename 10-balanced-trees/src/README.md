# Source code · Module 10 · Balanced Search Trees

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`AVLTree.java`](AVLTree.java) | Session 1 demo: an AVL tree (self-balancing BST) with insert, delete, the four rotation cases, a rotation log, and a height comparison against a plain BST on sorted input. | `java -ea -cp out AVLTree` |
| [`Labs10.java`](Labs10.java) | Module 10 — reference solutions for the lab tasks of Sessions 1-2. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs10` |
| [`RedBlackTree.java`](RedBlackTree.java) | Session 2 demo: a left-leaning red-black tree (LLRB), the simplest red-black variant to code. Insert uses three local fixes: rotate left, rotate right, flip colours. The demo validates the red-black invariants, counts fixes, compares heights with AVL, and shows java.util.TreeMap. | `java -ea -cp out RedBlackTree` |
| [`SelfCheck.java`](SelfCheck.java) | Module 10 self-check: JUnit-free tests for AVL and red-black trees. javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out AVLTree           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 10 self-check: 14 passed, 0 failed
```
