# Source code · Module 01 · Java Refresher and Complexity

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`JavaRefresher.java`](JavaRefresher.java) | Module 01 · Session 01 — Java refresher for Data Structures. | `java -ea -cp out JavaRefresher` |
| [`Labs01.java`](Labs01.java) | Module 01 — reference solutions for the lab tasks of Sessions 1-3. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers so the lesson notes can quote exactly the code that is compiled and tested here. | `java -ea -cp out Labs01` |
| [`OperationCounter.java`](OperationCounter.java) | Module 01 · Session 02 — Measuring growth by counting basic operations. | `java -ea -cp out OperationCounter` |
| [`RecursionBasics.java`](RecursionBasics.java) | Module 01 · Session 03 — Recursion fundamentals. | `java -ea -cp out RecursionBasics` |
| [`SelfCheck.java`](SelfCheck.java) | Module 01 self-check: JUnit-free tests. Run with assertions enabled: javac -d out *.java && java -ea -cp out SelfCheck Exits with status 1 if any check fails. | `java -ea -cp out SelfCheck` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out JavaRefresher           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 01 self-check: 18 passed, 0 failed
```
