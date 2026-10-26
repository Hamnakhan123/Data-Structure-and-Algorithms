# Source code · Module 02 · Arrays and Strings

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`DynamicArray.java`](DynamicArray.java) | Module 02 · Session 01 — A resizable array (our own mini ArrayList). | `java -ea -cp out DynamicArray` |
| [`Labs02.java`](Labs02.java) | Module 02 — reference solutions for the lab tasks of Sessions 1-3. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs02` |
| [`MatrixOps.java`](MatrixOps.java) | Module 02 · Session 02 — Two-dimensional arrays and matrix algorithms. | `java -ea -cp out MatrixOps` |
| [`SelfCheck.java`](SelfCheck.java) | Module 02 self-check: JUnit-free tests for arrays, matrices and strings. javac -d out *.java && java -ea -cp out SelfCheck Exits with status 1 if any check fails. | `java -ea -cp out SelfCheck` |
| [`StringAlgorithms.java`](StringAlgorithms.java) | Module 02 · Session 03 — Strings, StringBuilder and pattern matching. | `java -ea -cp out StringAlgorithms` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out DynamicArray           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 02 self-check: 23 passed, 0 failed
```
