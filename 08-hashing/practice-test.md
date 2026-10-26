# Module 08 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** The expected cost of `get` in a well-designed hash table is:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n log n)

**Q2.** `"ab".hashCode()` equals (a = 97, b = 98):

- **A.** 195
- **B.** 3105
- **C.** 9798
- **D.** 6014

**Q3.** The load factor of a table with 30 entries and 64 buckets is about:

- **A.** 0.47
- **B.** 2.13
- **C.** 30
- **D.** 0.75

**Q4.** Java's `HashMap` resizes when the load factor exceeds:

- **A.** 0.5
- **B.** 0.75
- **C.** 1.0
- **D.** 2.0

**Q5.** Doubling the table on resize makes insertion:

- **A.** O(n) every time
- **B.** O(1) amortised
- **C.** O(log n)
- **D.** O(n²) overall

**Q6.** Which compression is always safe for any int h?

- **A.** Math.abs(h) % m
- **B.** h % m
- **C.** (h & 0x7fffffff) % m
- **D.** h / m

**Q7.** In separate chaining, each bucket typically stores:

- **A.** one key only
- **B.** a linked list (or tree) of entries
- **C.** a sorted array of all keys
- **D.** a stack of hash codes

**Q8.** All n keys hash to one bucket. Search becomes:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** impossible

**Q9.** Linear probing suffers from:

- **A.** primary clustering
- **B.** stack overflow
- **C.** tombstone loss
- **D.** no collisions

**Q10.** Double hashing probes slots:

- **A.** h, h+1, h+2, …
- **B.** h, h+1, h+4, h+9, …
- **C.** h₁, h₁+h₂, h₁+2h₂, … (mod m)
- **D.** randomly

**Q11.** Why must h₂ never be 0 in double hashing?

- **A.** it would be too fast
- **B.** the probe sequence would never leave the home slot
- **C.** Java forbids it
- **D.** it causes clustering

**Q12.** Deleting from a linear-probing table should:

- **A.** set the slot to null
- **B.** leave a tombstone
- **C.** shift every key left
- **D.** rebuild the table immediately

**Q13.** With open addressing, the load factor must be:

- **A.** above 1
- **B.** below 1
- **C.** exactly 1
- **D.** irrelevant

**Q14.** If `a.equals(b)` is true, Java requires:

- **A.** a == b
- **B.** a.hashCode() == b.hashCode()
- **C.** a.compareTo(b) == 0
- **D.** a.toString().equals(b.toString())

**Q15.** Which key type is safest for a HashMap?

- **A.** an object whose hash uses a mutable field
- **B.** an immutable record or String
- **C.** an array
- **D.** a StringBuilder

**Q16.** `LinkedHashMap` with accessOrder = true is the basis of:

- **A.** a priority queue
- **B.** an LRU cache
- **C.** a trie
- **D.** a bloom filter

**Q17.** Which collection keeps keys sorted?

- **A.** HashSet
- **B.** LinkedHashSet
- **C.** TreeSet
- **D.** ArrayDeque

**Q18.** Finding whether an array has a duplicate is fastest (expected) with:

- **A.** nested loops
- **B.** a HashSet
- **C.** binary search without sorting
- **D.** a stack

**Q19.** The prefix-sum + hash map technique counts subarrays with sum k in:

- **A.** O(n²)
- **B.** O(n log n)
- **C.** O(n) expected
- **D.** O(k·n)

**Q20.** Java 8 `HashMap` converts a long chain into:

- **A.** an array
- **B.** a red-black tree
- **C.** a skip list
- **D.** a heap

<details>
<summary><b>Show answers</b></summary>

1. **A**: Short chains / few probes on average.
2. **B**: 31·97 + 98 = 3105.
3. **A**: 30 / 64 ≈ 0.47.
4. **B**: The default load factor is 0.75.
5. **B**: Total copying over n inserts is under 2n.
6. **C**: It clears the sign bit.
7. **B**: Colliding entries share the bucket.
8. **C**: One long chain is a linked list.
9. **A**: Occupied runs grow and merge.
10. **C**: Each key has its own step size h₂.
11. **B**: A step of zero revisits one slot forever.
12. **B**: null breaks the probe chains of keys stored after it.
13. **B**: Every entry needs its own slot.
14. **B**: This is the equals/hashCode contract.
15. **B**: The hash must not change while the key is in the map.
16. **B**: Access moves an entry to the end; the eldest is evicted.
17. **C**: Red-black tree.
18. **B**: O(n): `if (!set.add(x)) return true;`.
19. **C**: One pass, one map lookup per element.
20. **B**: Tree bins bound the worst case at O(log n).

</details>

---

## Part B · Coding scenarios

### Scenario 1 · Duplicate CNIC detection

The admissions office receives a list of applicant CNIC numbers. Write `firstDuplicate(String[] ids)` that returns the first ID that appears a second time (scanning left to right), or `null`. Required: O(n) expected time.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.HashSet;
import java.util.Set;

public class DuplicateIds {
    static String firstDuplicate(String[] ids) {
        Set<String> seen = new HashSet<>();
        for (String id : ids)
            if (!seen.add(id)) return id;       // add returns false if already present
        return null;
    }

    public static void main(String[] args) {
        System.out.println(firstDuplicate(new String[]{"42101-1", "42201-7", "42101-9", "42201-7", "42101-1"}));
        System.out.println(firstDuplicate(new String[]{"a", "b"}));
    }
}
```

Output: `42201-7` then `null`. Each `add` is O(1) expected ⇒ O(n) time, O(n) space.

</details>

### Scenario 2 · Course-pair popularity

The timetable team has a list of student registrations, each `"roll,course"`. Write `topCourse(String[] regs)` that returns the course with the most **distinct** students (ties: alphabetically smallest). A student may appear twice for the same course by mistake.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TopCourse {
    static String topCourse(String[] regs) {
        Map<String, Set<String>> students = new HashMap<>();
        for (String r : regs) {
            String[] p = r.split(",");
            students.computeIfAbsent(p[1], k -> new HashSet<>()).add(p[0]);
        }
        String best = null;
        for (Map.Entry<String, Set<String>> e : students.entrySet()) {
            int n = e.getValue().size();
            if (best == null || n > students.get(best).size()
                    || (n == students.get(best).size() && e.getKey().compareTo(best) < 0))
                best = e.getKey();
        }
        return best;
    }

    public static void main(String[] args) {
        String[] regs = {"1,DSA", "2,DSA", "2,DSA", "3,AI", "4,AI", "5,OOP", "1,AI"};
        System.out.println(topCourse(regs));
    }
}
```

Output: `AI` (3 distinct students; DSA has 2 because roll 2 is counted once). O(n) expected.

</details>

### Scenario 3 · Longest stretch of balanced attendance

A lab records each day as `+1` (present) or `-1` (absent). Find the length of the longest stretch of consecutive days with **equal** present and absent days.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.HashMap;
import java.util.Map;

public class BalancedStretch {
    static int longest(int[] days) {
        Map<Integer, Integer> firstIndex = new HashMap<>();
        firstIndex.put(0, -1);                   // prefix sum 0 before day 0
        int sum = 0, best = 0;
        for (int i = 0; i < days.length; i++) {
            sum += days[i];
            Integer j = firstIndex.putIfAbsent(sum, i);
            if (j != null) best = Math.max(best, i - j);   // same prefix sum ⇒ zero-sum stretch
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(longest(new int[]{1, -1, 1, 1, -1, -1, 1, 1, 1}));
        System.out.println(longest(new int[]{1, 1, 1}));
    }
}
```

Output: `6` then `0`. Two equal prefix sums mark a stretch summing to zero; storing only the **first** index of each sum maximises its length. O(n) expected.

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
