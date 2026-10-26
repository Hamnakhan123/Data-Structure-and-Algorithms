# Source code · Module 03 · Searching

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`BinarySearchVariations.java`](BinarySearchVariations.java) | Module 03 · Session 02 — Binary search variations and "binary search on the answer". | `java -ea -cp out BinarySearchVariations` |
| [`Labs03.java`](Labs03.java) | Module 03 — reference solutions for the lab tasks of Sessions 1-2. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs03` |
| [`SearchAlgorithms.java`](SearchAlgorithms.java) | Module 03 · Session 01 — Linear search, binary search and friends. | `java -ea -cp out SearchAlgorithms` |
| [`SelfCheck.java`](SelfCheck.java) | Module 03 self-check: JUnit-free tests for searching. javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out BinarySearchVariations           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 03 self-check: 110 passed, 0 failed
```
