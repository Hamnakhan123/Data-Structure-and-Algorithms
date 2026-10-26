# Source code · Module 07 · Queues and Deques

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`DequeApplications.java`](DequeApplications.java) | Module 07 · Session 02 — Deques (double-ended queues) and their applications. | `java -ea -cp out DequeApplications` |
| [`Labs07.java`](Labs07.java) | Module 07 — reference solutions for the lab tasks of Sessions 1-2. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs07` |
| [`QueueImplementations.java`](QueueImplementations.java) | Module 07 · Session 01 — The queue ADT: circular array and linked implementations. | `java -ea -cp out QueueImplementations` |
| [`SelfCheck.java`](SelfCheck.java) | Module 07 self-check: JUnit-free tests for queues and deques. javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out DequeApplications           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 07 self-check: 50 passed, 0 failed
```
