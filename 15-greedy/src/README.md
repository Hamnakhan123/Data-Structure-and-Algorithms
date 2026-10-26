# Source code · Module 15 · Greedy Algorithms

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`GreedyFundamentals.java`](GreedyFundamentals.java) | Session 1 demo: the greedy method. Coin change with canonical and non-canonical coin systems (greedy vs optimal), fractional knapsack (greedy is optimal) vs 0/1 knapsack (greedy fails), and the "assign cookies" matching problem. | `java -ea -cp out GreedyFundamentals` |
| [`HuffmanIntervals.java`](HuffmanIntervals.java) | Session 2 demo: Huffman coding (build the tree with a priority queue, derive prefix-free codes, encode, decode and measure compression) and interval scheduling (earliest-finish-time selection, and why other greedy rules fail). | `java -ea -cp out HuffmanIntervals` |
| [`Labs15.java`](Labs15.java) | Module 15 — reference solutions for the lab tasks of Sessions 1-2. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs15` |
| [`SelfCheck.java`](SelfCheck.java) | Module 15 self-check: JUnit-free tests for greedy algorithms (checked against brute force / DP). javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out GreedyFundamentals           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 15 self-check: 12 passed, 0 failed
```
