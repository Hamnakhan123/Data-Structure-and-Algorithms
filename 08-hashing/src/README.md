# Source code · Module 08 · Hashing

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`HashChaining.java`](HashChaining.java) | Session 1 demo: hash functions, compression and a separate-chaining hash map built from scratch. | `java -ea -cp out HashChaining` |
| [`Labs08.java`](Labs08.java) | Module 08 — reference solutions for the lab tasks of Sessions 1-2. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. | `java -ea -cp out Labs08` |
| [`OpenAddressing.java`](OpenAddressing.java) | Session 2 demo: open addressing (linear probing with tombstones), probe-sequence comparison, the equals/hashCode contract, and Java's HashMap, HashSet, LinkedHashMap and TreeMap. | `java -ea -cp out OpenAddressing` |
| [`SelfCheck.java`](SelfCheck.java) | Module 08 self-check: JUnit-free tests for hashing. javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out HashChaining           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 08 self-check: 63 passed, 0 failed
```
