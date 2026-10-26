# Module 02 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes

## Part A · Multiple-choice questions

**Q1.** An `int[]` starts at address 2000. Where is `a[25]` (4-byte ints)?

- **A.** 2025
- **B.** 2100
- **C.** 2104
- **D.** 2096

**Q2.** Default value of elements in a new `boolean[5]`?

- **A.** true
- **B.** false
- **C.** null
- **D.** undefined

**Q3.** Deleting the first element of an n-element array (keeping order) costs:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n²)

**Q4.** A dynamic array starts at capacity 1 and doubles. After 100 appends, the capacity is:

- **A.** 100
- **B.** 128
- **C.** 200
- **D.** 101

**Q5.** Growing a dynamic array by a fixed +10 slots makes n appends cost:

- **A.** O(n)
- **B.** O(n log n)
- **C.** O(n²)
- **D.** O(1)

**Q6.** Which line prevents memory "loitering" after removing the last element?

- **A.** `size--;`
- **B.** `data[--size] = null;`
- **C.** `data = null;`
- **D.** `System.gc();`

**Q7.** Which technique rotates an array by k in O(1) extra space?

- **A.** Copy into a new array
- **B.** Three reversals
- **C.** Sorting
- **D.** Rotating one step k times with a temp array of size n

**Q8.** For `int[][] g = new int[5][]`, what is `g[2]`?

- **A.** an empty array
- **B.** `null`
- **C.** 0
- **D.** `int[5]`

**Q9.** Spiral traversal of a 4×4 matrix visits how many cells?

- **A.** 12
- **B.** 16
- **C.** 8
- **D.** 20

**Q10.** Which loop order is most cache-friendly for summing a large Java `int[R][C]`?

- **A.** columns outer, rows inner
- **B.** rows outer, columns inner
- **C.** either, no difference
- **D.** diagonal order

**Q11.** Multiplying a 2×3 matrix by a 3×4 matrix gives a matrix of size:

- **A.** 3×3
- **B.** 2×4
- **C.** 4×2
- **D.** 2×3

**Q12.** Building a 2-D prefix-sum table for an R×C grid costs:

- **A.** O(1)
- **B.** O(R + C)
- **C.** O(R·C)
- **D.** O((R·C)²)

**Q13.** In a matrix sorted by rows and columns, staircase search starts at:

- **A.** top-left
- **B.** top-right
- **C.** the centre
- **D.** a random cell

**Q14.** Which statement about Java Strings is FALSE?

- **A.** They are immutable
- **B.** `s.concat(t)` changes `s`
- **C.** They can be used safely as HashMap keys
- **D.** `charAt` is O(1)

**Q15.** The best tool to build a long string inside a loop is:

- **A.** `String +=`
- **B.** `StringBuilder`
- **C.** `String.format` in the loop
- **D.** `char` addition

**Q16.** `"karachi".indexOf("ach")` returns:

- **A.** 1
- **B.** 2
- **C.** 3
- **D.** -1

**Q17.** LPS array of `AABAACAABAA` ends with:

- **A.** 3
- **B.** 5
- **C.** 2
- **D.** 0

**Q18.** Naive search's worst-case comparisons for n = 1000, m = 10 are about:

- **A.** 1,000
- **B.** 10,000
- **C.** 100
- **D.** 1,000,000

**Q19.** The anagram check using an int[26] array uses extra space:

- **A.** O(n)
- **B.** O(1)
- **C.** O(26n)
- **D.** O(log n)

**Q20.** Which is the time complexity of KMP's LPS construction for a pattern of length m?

- **A.** O(m²)
- **B.** O(m)
- **C.** O(m log m)
- **D.** O(1)

<details>
<summary><b>Show answers</b></summary>

1. **B**: 2000 + 25 × 4 = 2100.
2. **B**: Java zero-initialises arrays: false for boolean.
3. **C**: The remaining n − 1 elements shift left.
4. **B**: 1, 2, 4, …, 64, 128; 128 is the first power of two ≥ 100.
5. **C**: Copies 10 + 20 + … + n ≈ n²/20.
6. **B**: Clearing the slot drops the stale reference.
7. **B**: reverse(all), reverse(first k), reverse(rest).
8. **B**: Only the outer array exists; rows are unallocated.
9. **B**: Every cell exactly once.
10. **B**: Each row is a contiguous array.
11. **B**: Outer dimensions: (2×3)(3×4) → 2×4.
12. **C**: One constant-time formula per cell.
13. **B**: From top-right, one direction decreases and the other increases.
14. **B**: concat returns a new string; s is unchanged.
15. **B**: Amortised O(1) appends.
16. **C**: k0 a1 r2 a3 c4 h5 → "ach" starts at 3.
17. **B**: The whole string's longest proper border is "AABAA" (length 5).
18. **B**: Roughly (n − m + 1) × m ≈ 10⁴.
19. **B**: 26 counters regardless of input length.
20. **B**: `len` can fall back at most as many times as it has increased.

</details>

## Part B · Coding scenarios

### Scenario 1 · Ticket queue compaction

A cinema stores booked seat numbers in an `int[]`, using `0` for cancelled seats. Move all non-zero bookings to the front **in their original order**, zeros to the end, in O(n) time and O(1) space. Example: `[4, 0, 7, 0, 0, 2, 9]` → `[4, 7, 2, 9, 0, 0, 0]`.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.Arrays;

public class MoveZeros {
    static void moveZeros(int[] a) {
        int write = 0;
        for (int read = 0; read < a.length; read++)
            if (a[read] != 0) a[write++] = a[read];   // keep order of non-zeros
        while (write < a.length) a[write++] = 0;      // fill the tail
    }

    public static void main(String[] args) {
        int[] seats = {4, 0, 7, 0, 0, 2, 9};
        moveZeros(seats);
        System.out.println(Arrays.toString(seats));
    }
}
```

Read/write pointers, one pass: O(n) time, O(1) space.

</details>

### Scenario 2 · Image flip

A grayscale image is an `int[R][C]` matrix of pixel brightness. Write `mirror(int[][] img)` that flips it horizontally (left ↔ right) in place, and `invert(int[][] img)` that replaces each pixel `p` with `255 - p`. State the complexity.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.Arrays;

public class ImageOps {
    static void mirror(int[][] img) {
        for (int[] row : img)
            for (int l = 0, r = row.length - 1; l < r; l++, r--) {
                int t = row[l]; row[l] = row[r]; row[r] = t;
            }
    }

    static void invert(int[][] img) {
        for (int[] row : img)
            for (int j = 0; j < row.length; j++) row[j] = 255 - row[j];
    }

    public static void main(String[] args) {
        int[][] img = {{0, 50, 100}, {150, 200, 255}};
        mirror(img);
        invert(img);
        System.out.println(Arrays.deepToString(img));
    }
}
```

Both are O(R·C) time, O(1) extra space. Output: `[[155, 205, 255], [0, 55, 105]]`.

</details>

### Scenario 3 · Username validator

A sign-up form accepts a username only if it (a) is 5–15 characters, (b) contains only letters, digits and underscores, (c) does not start with a digit and (d) is not a rotation of the word `"admin"` (e.g. `"minad"`). Write `isValid(String u)`.

<details>
<summary>✅ Model solution</summary>

```java
public class UsernameValidator {
    static boolean isRotation(String a, String b) {
        return a.length() == b.length() && (b + b).contains(a);
    }

    static boolean isValid(String u) {
        if (u.length() < 5 || u.length() > 15) return false;
        if (Character.isDigit(u.charAt(0))) return false;
        for (char ch : u.toCharArray())
            if (!(Character.isLetterOrDigit(ch) || ch == '_')) return false;
        return !isRotation(u.toLowerCase(), "admin");
    }

    public static void main(String[] args) {
        for (String u : new String[]{"hamna_01", "minad", "9lives", "ok", "data-struct"})
            System.out.println(u + " -> " + isValid(u));
    }
}
```

The rotation trick: every rotation of `b` appears inside `b + b`. `String.contains` is a simple search (O(n²) in the worst case, fast on typical input); using `kmpSearch` from Session 3 makes it a guaranteed O(n).

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50.
