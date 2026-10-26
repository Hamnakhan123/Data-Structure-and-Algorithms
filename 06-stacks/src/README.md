# Source code · Module 06 · Stacks

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`ExpressionEvaluation.java`](ExpressionEvaluation.java) | Module 06 · Session 02 — Stack applications: brackets and expressions. | `java -ea -cp out ExpressionEvaluation` |
| [`Labs06.java`](Labs06.java) | Module 06 — reference solutions for the lab tasks of Sessions 1-3. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs06` |
| [`MonotonicStack.java`](MonotonicStack.java) | Module 06 · Session 03 — Monotonic stacks. | `java -ea -cp out MonotonicStack` |
| [`SelfCheck.java`](SelfCheck.java) | Module 06 self-check: JUnit-free tests for stacks and their applications. javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |
| [`StackImplementations.java`](StackImplementations.java) | Module 06 · Session 01 — The stack ADT with array and linked implementations. | `java -ea -cp out StackImplementations` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out ExpressionEvaluation           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 06 self-check: 68 passed, 0 failed
```
