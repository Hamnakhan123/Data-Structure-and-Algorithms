# Module 01 · Practice Test

Prepared by **Hamna Zahid Khan**, Junior Lecturer

[🏠 Course home](../README.md) · [📘 Module overview](./README.md)

> 20 multiple-choice questions + 3 coding scenarios · Suggested time: 75 minutes · Answers are hidden; attempt first!

## Part A · Multiple-choice questions

**Q1.** Which statement about Java parameter passing is correct?

- **A.** Objects are passed by reference, primitives by value
- **B.** Everything is passed by value; for objects the value is a reference
- **C.** Everything is passed by reference
- **D.** Arrays are copied element by element when passed

**Q2.** `String a = new String("x"), b = new String("x");` What is `a == b`?

- **A.** true
- **B.** false
- **C.** compile error
- **D.** depends on the JVM

**Q3.** Which method signature correctly finds the maximum of any comparable type?

- **A.** `static Object max(Object[] a)`
- **B.** `static <T> T max(T[] a)`
- **C.** `static <T extends Comparable<T>> T max(T[] a)`
- **D.** `static Comparable max(Comparable a)`

**Q4.** `compareTo` should return a negative value when:

- **A.** the objects are equal
- **B.** `this` should come before the argument
- **C.** `this` should come after the argument
- **D.** an error occurs

**Q5.** Which call sorts `List<Student> list` by name without modifying `Student`?

- **A.** `list.sort(null)`
- **B.** `list.sort(Comparator.comparing(s -> s.name))`
- **C.** `Collections.sort(list)`
- **D.** `list.compareTo(name)`

**Q6.** The complexity of appending to the end of an `ArrayList` is:

- **A.** O(n) always
- **B.** O(1) amortised
- **C.** O(log n)
- **D.** O(n²)

**Q7.** Which is the tightest bound for f(n) = 4n log n + 10n + 7?

- **A.** Θ(n)
- **B.** Θ(n log n)
- **C.** Θ(n²)
- **D.** Θ(log n)

**Q8.** What is the complexity of this code? `for (i = 0; i < n; i++) for (j = 0; j < n; j += 2) x++;`

- **A.** O(n)
- **B.** O(n log n)
- **C.** O(n²)
- **D.** O(n²/2), which is different from O(n²)

**Q9.** `for (i = n; i >= 1; i /= 2) for (j = 0; j < n; j++) x++;` is:

- **A.** O(n)
- **B.** O(log n)
- **C.** O(n log n)
- **D.** O(n²)

**Q10.** Which notation describes a lower bound?

- **A.** Big-O
- **B.** Big-Ω
- **C.** Big-Θ
- **D.** little-o

**Q11.** Linear search on an unsorted array of n items has worst-case time:

- **A.** O(1)
- **B.** O(log n)
- **C.** O(n)
- **D.** O(n log n)

**Q12.** A function allocates an n × n matrix. Its auxiliary space is:

- **A.** O(1)
- **B.** O(n)
- **C.** O(n²)
- **D.** O(2ⁿ)

**Q13.** The recurrence T(n) = 2T(n/2) + O(n) solves to:

- **A.** O(n)
- **B.** O(log n)
- **C.** O(n log n)
- **D.** O(n²)

**Q14.** What is the essential role of the base case?

- **A.** To make recursion faster
- **B.** To stop the recursion with a directly computed answer
- **C.** To allocate memory
- **D.** To handle exceptions

**Q15.** What does this print? `static int f(int n){ if(n==0) return 0; return n + f(n-1); }` with `f(4)`

- **A.** 4
- **B.** 10
- **C.** 24
- **D.** StackOverflowError

**Q16.** `f(-1)` with the method from the previous question results in:

- **A.** 0
- **B.** -1
- **C.** `StackOverflowError`
- **D.** compile error

**Q17.** How many calls does naive `fib(5)` make (counting the first call)?

- **A.** 5
- **B.** 8
- **C.** 15
- **D.** 32

**Q18.** Fast exponentiation `power(b, e)` uses how many recursive calls (roughly)?

- **A.** e
- **B.** e/2
- **C.** log₂ e
- **D.** 1

**Q19.** Which technique converts naive Fibonacci from exponential to linear time?

- **A.** Tail recursion
- **B.** Memoisation
- **C.** Using `long` instead of `int`
- **D.** Adding more base cases

**Q20.** An algorithm must handle n = 10⁶ in under a second. Which complexity is safe?

- **A.** O(n²)
- **B.** O(n log n)
- **C.** O(2ⁿ)
- **D.** O(n³)

<details>
<summary><b>Show answers</b></summary>

1. **B**: Java copies the value of the argument; for an object that value is its reference.
2. **B**: `new` always creates distinct objects; `==` compares identity.
3. **C**: The bound makes `compareTo` available and keeps type safety.
4. **B**: Negative means "this is smaller", i.e. comes first in ascending order.
5. **B**: A Comparator supplies an order from outside the class.
6. **B**: Occasional O(n) resizes average out to O(1) per add.
7. **B**: n log n dominates the linear and constant terms.
8. **C**: n × n/2 iterations; constants are dropped.
9. **C**: log n outer iterations × n inner iterations.
10. **B**: Ω(g) means f grows at least as fast as g.
11. **C**: When the key is absent all n elements are checked.
12. **C**: n² cells.
13. **C**: log n levels, each doing O(n) total work (merge sort).
14. **B**: Without it the recursion never terminates.
15. **B**: 4 + 3 + 2 + 1 + 0 = 10.
16. **C**: n never reaches 0 when it starts negative: guard with `n <= 0`.
17. **C**: calls(n) = calls(n−1) + calls(n−2) + 1 → 1, 1, 3, 5, 9, 15.
18. **C**: The exponent halves on each call.
19. **B**: Each sub-problem is computed once and reused.
20. **B**: ~2 × 10⁷ operations is well within ~10⁸ per second.

</details>

## Part B · Coding scenarios

### Scenario 1 · Merit list

A department stores `(name, marks, age)` for applicants. Write a `Comparator<Applicant>` that sorts by **marks descending**, then **age ascending** (younger first), then **name** alphabetically. Sort a list of 5 sample applicants and print it.

<details>
<summary>✅ Model solution</summary>

```java
import java.util.*;

public class MeritList {
    record Applicant(String name, int marks, int age) {}

    public static void main(String[] args) {
        List<Applicant> list = new ArrayList<>(List.of(
            new Applicant("Hira", 88, 19), new Applicant("Asad", 92, 20),
            new Applicant("Noor", 88, 18), new Applicant("Fahad", 92, 20),
            new Applicant("Iqra", 75, 21)));
        Comparator<Applicant> merit = Comparator.comparingInt(Applicant::marks).reversed()
            .thenComparingInt(Applicant::age)
            .thenComparing(Applicant::name);
        list.sort(merit);
        list.forEach(System.out::println);
    }
}
```

Order: Asad(92,20), Fahad(92,20), Noor(88,18), Hira(88,19), Iqra(75,21). Time O(n log n).

</details>

### Scenario 2 · Fix the slow report

The following method builds a report of n roll numbers and is painfully slow for n = 100,000. State its complexity, explain why, and rewrite it.

```java
static String report(int[] rolls) {
    String s = "";
    for (int r : rolls) s += r + ",";
    return s;
}
```

<details>
<summary>✅ Model solution</summary>

Strings are immutable, so each `+=` copies the whole string built so far: 1 + 2 + … + n characters ⇒ **O(n²)** time. `StringBuilder` appends in amortised O(1):

```java
static String report(int[] rolls) {
    StringBuilder sb = new StringBuilder();
    for (int r : rolls) sb.append(r).append(',');
    return sb.toString();
}
```

New complexity: **O(n)** time, O(n) space.

</details>

### Scenario 3 · Recursive digital root

The *digital root* of a number is obtained by summing its digits repeatedly until one digit remains (e.g. 9875 → 29 → 11 → 2). Write it recursively, give its base case, and state its complexity.

<details>
<summary>✅ Model solution</summary>

```java
static int sumDigits(int n) { return n < 10 ? n : n % 10 + sumDigits(n / 10); }

static int digitalRoot(int n) {
    if (n < 10) return n;              // base case: already one digit
    return digitalRoot(sumDigits(n));  // recursive case: strictly smaller number
}
```

`sumDigits` is O(d) for d digits; after one pass the number has at most about log₁₀(9d) digits, so the total work is dominated by the first pass: **O(d) = O(log n)**. (Bonus: the closed form is `1 + (n - 1) % 9` for n > 0.)

</details>

---

**Scoring guide:** Part A = 1 mark each (20). Part B = 10 marks each (30). Total 50. 40+ excellent · 30–39 good · below 30: revisit the sessions and re-run the [self-check](src/README.md).
