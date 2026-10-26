import java.util.ArrayList;
import java.util.List;

/**
 * Capstone 3 starter: Exam Timetable Planner. Builds the clash graph between exams from student
 * registrations and must assign time slots (graph colouring by backtracking), rooms (greedy with a
 * heap) and an order that respects "must be before" constraints (topological sort). TODO methods
 * compile and return placeholders; PlannerChecks reports progress.
 *
 * Compile:  javac -d out *.java
 * Run:      java -cp out PlannerChecks
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class TimetablePlanner {

    record Exam(String code, int students) { }

    final List<Exam> exams = new ArrayList<>();
    final List<int[]> registrations = new ArrayList<>();   // {studentId, examIndex}
    final List<int[]> mustPrecede = new ArrayList<>();      // {a, b}: exam a must be in an earlier slot than b

    int exam(String code, int students) { exams.add(new Exam(code, students)); return exams.size() - 1; }

    static TimetablePlanner sample() {
        TimetablePlanner p = new TimetablePlanner();
        int dsa = p.exam("DSA", 120), oop = p.exam("OOP", 90), ai = p.exam("AI", 60), db = p.exam("DB", 80), math = p.exam("Math", 150);
        int[][] reg = {{1, dsa}, {1, oop}, {2, dsa}, {2, ai}, {3, oop}, {3, ai}, {4, ai}, {4, db}, {5, db}, {5, math}, {6, dsa}, {6, math}};
        for (int[] r : reg) p.registrations.add(r);
        p.mustPrecede.add(new int[]{math, ai});   // the AI exam reuses Math invigilators, so Math goes first
        return p;
    }

    /** clash[a][b] is true if some student takes both exams (already implemented for you). */
    boolean[][] clashGraph() {
        int n = exams.size();
        boolean[][] clash = new boolean[n][n];
        java.util.Map<Integer, List<Integer>> byStudent = new java.util.HashMap<>();
        for (int[] r : registrations) byStudent.computeIfAbsent(r[0], k -> new ArrayList<>()).add(r[1]);
        for (List<Integer> es : byStudent.values())
            for (int a : es) for (int b : es) if (a != b) clash[a][b] = true;
        return clash;
    }

    // ===================== TODO: Milestone 2 =====================

    /** Fewest slots so that no student has two exams in one slot (exact backtracking, Module 14). */
    int minimumSlots() {
        return -1;   // TODO: try m = 1, 2, ... with backtracking colouring
    }

    /** Slot (1-based) of each exam using minimumSlots() slots and respecting mustPrecede; null if impossible. */
    int[] assignSlots() {
        return null;   // TODO: backtracking with an extra check for mustPrecede pairs
    }

    // ===================== TODO: Milestone 3 =====================

    /** Rooms needed for exams given as {start, end} minutes on one day (min-heap of end times, Module 11). */
    static int roomsNeeded(int[][] sittings) {
        return -1;   // TODO
    }

    /** One order of exams consistent with mustPrecede (Kahn's algorithm, Module 12); empty if cyclic. */
    List<String> precedenceOrder() {
        return List.of();   // TODO
    }
}
