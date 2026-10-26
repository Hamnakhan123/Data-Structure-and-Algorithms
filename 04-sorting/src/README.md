# Source code · Module 04 · Sorting

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`ElementarySorts.java`](ElementarySorts.java) | Module 04 · Session 01 — Bubble, selection and insertion sort. | `java -ea -cp out ElementarySorts` |
| [`HeapCountingRadix.java`](HeapCountingRadix.java) | Module 04 · Session 03 — Heap sort, counting sort and radix sort. | `java -ea -cp out HeapCountingRadix` |
| [`Labs04.java`](Labs04.java) | Module 04 — reference solutions for the lab tasks of Sessions 1-3. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs04` |
| [`MergeQuickSort.java`](MergeQuickSort.java) | Module 04 · Session 02 — Merge sort and quick sort (divide and conquer). | `java -ea -cp out MergeQuickSort` |
| [`SelfCheck.java`](SelfCheck.java) | Module 04 self-check: every sort is tested against Arrays.sort on many random arrays (including empty, single-element, sorted, reversed and duplicate-heavy). javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out ElementarySorts           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 04 self-check: 604 passed, 0 failed
```
