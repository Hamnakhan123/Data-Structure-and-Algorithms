# Source code · Module 05 · Linked Lists

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[⬅ Back to module](../README.md) · [🏠 Course home](../../README.md)

All programs are plain Java 17 in the **default package** (no build tool, no external libraries).
Every file has a `main()` demo; [`SelfCheck.java`](SelfCheck.java) is a JUnit-free test runner for the whole module.

## Files

| File | What it does | Run (after compiling) |
|---|---|---|
| [`DoublyCircularLists.java`](DoublyCircularLists.java) | Module 05 · Session 02 — Doubly linked lists and circular linked lists. | `java -ea -cp out DoublyCircularLists` |
| [`Labs05.java`](Labs05.java) | Module 05 — reference solutions for the lab tasks of Sessions 1-3. Each solution sits between "// &gt;&gt;&gt; name" and "// &lt;&lt;&lt; name" markers. Uses LinkedListProblems.ListNode for the node type. | `java -ea -cp out Labs05` |
| [`LinkedListProblems.java`](LinkedListProblems.java) | Module 05 · Session 03 — Classic linked-list problems. | `java -ea -cp out LinkedListProblems` |
| [`SelfCheck.java`](SelfCheck.java) | Module 05 self-check: JUnit-free tests for all linked-list code. javac -d out *.java && java -ea -cp out SelfCheck | `java -ea -cp out SelfCheck` |
| [`SinglyLinkedList.java`](SinglyLinkedList.java) | Module 05 · Session 01 — A generic singly linked list built from scratch. | `java -ea -cp out SinglyLinkedList` |

## How to compile and run

From this `src/` folder:

```bash
javac -d out *.java              # compile every file of the module into ./out
java -ea -cp out SelfCheck        # run the module's self-check tests (-ea enables assertions)
java -cp out DoublyCircularLists           # run any demo by class name
```

On Windows PowerShell the commands are identical. `out/` and `*.class` are ignored by git.

## Self-check result

```text
$ java -ea -cp out SelfCheck
Module 05 self-check: 24 passed, 0 failed
```
