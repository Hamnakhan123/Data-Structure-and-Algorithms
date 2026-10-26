# Source code · Module 14 · Recursion and Backtracking

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`BacktrackingBasics.java`](BacktrackingBasics.java) | Session 1 demo: the backtracking template ("choose, explore, un-choose") applied to subsets, permutations, combinations and combination sum, with call counters that show how pruning shrinks the search tree. | `java -ea -cp out BacktrackingBasics` |
| [`ConstraintProblems.java`](ConstraintProblems.java) | Session 2 demo: constraint-satisfaction with backtracking. N-Queens (one board and solution counts, with O(1) conflict checks using column and diagonal flags) and a Sudoku solver that tries the most constrained empty cell first, with counters showing how much each technique prunes. | `java -ea -cp out ConstraintProblems` |
| [`Labs14.java`](Labs14.java) | Module 14 — reference solutions for the lab tasks of Sessions 1-2. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs14` |
| [`SelfCheck.java`](SelfCheck.java) | Module 14 self-check: JUnit-free tests for backtracking (counts checked against formulas). javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out BacktrackingBasics           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 14 self-check: 146 passed, 0 failed
```
