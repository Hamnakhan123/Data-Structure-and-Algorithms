# Source code · Module 16 · Dynamic Programming

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`AdvancedDP.java`](AdvancedDP.java) | Session 3 demo: advanced dynamic-programming patterns. Interval DP (matrix-chain multiplication with the optimal parenthesisation), bitmask DP (travelling salesperson over a few campuses), DP on trees (maximum-value guest list with no direct manager-report pairs), and weighted interval scheduling (DP + binary search) compared with the greedy rule from Module 15. | `java -ea -cp out AdvancedDP` |
| [`ClassicDP.java`](ClassicDP.java) | Session 2 demo: classic dynamic-programming problems with tables and reconstruction: 0/1 knapsack (2-D table, chosen items, 1-D space optimisation), longest common subsequence, coin change (minimum coins and number of ways), and longest increasing subsequence (O(n^2) and O(n log n)). | `java -ea -cp out ClassicDP` |
| [`DPIntro.java`](DPIntro.java) | Session 1 demo: from exponential recursion to dynamic programming. Fibonacci four ways (naive, memoised, tabulated, O(1) space) with call counts and timings, climbing stairs, grid paths with obstacles, and minimum-cost path with path reconstruction. | `java -ea -cp out DPIntro` |
| [`Labs16.java`](Labs16.java) | Module 16 — reference solutions for the lab tasks of Sessions 1-3. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs16` |
| [`SelfCheck.java`](SelfCheck.java) | Module 16 self-check: JUnit-free tests for dynamic programming (DP answers vs brute force). javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out AdvancedDP           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 16 self-check: 17 passed, 0 failed
```
