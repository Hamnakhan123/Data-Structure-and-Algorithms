# Source code · Module 17 · Interview Problem Patterns

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`Labs17.java`](Labs17.java) | Module 17 — reference solutions for the lab tasks of Sessions 1-2. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs17` |
| [`MorePatterns.java`](MorePatterns.java) | Session 2 demo: more interview patterns, each in a few lines. Fast/slow pointers (Floyd's cycle detection on an array), binary search on the answer (minimum truck capacity), prefix sum + hash map, monotonic stack (next warmer day), cyclic sort (missing and duplicate roll numbers) and bit tricks (the unpaired value, subsets as bitmasks). | `java -ea -cp out MorePatterns` |
| [`SelfCheck.java`](SelfCheck.java) | Module 17 self-check: JUnit-free tests for interview patterns, each against a brute force. javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |
| [`TwoPointersWindow.java`](TwoPointersWindow.java) | Session 1 demo: the two-pointer and sliding-window patterns. Opposite-end pointers (pair sum, container with most water, 3-sum), same-direction pointers (in-place deduplication), fixed windows (best k-day total) and variable windows (longest unique substring, shortest window with sum &gt;= target, longest window with at most k distinct values), each compared with its brute force. | `java -ea -cp out TwoPointersWindow` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out Labs17           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 17 self-check: 19 passed, 0 failed
```
