import java.util.List;

/**
 * Capstone 3 checks: run after each milestone. Expected values are computed from the sample data.
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class PlannerChecks {
    static int passed = 0, total = 0;

    static void check(String feature, boolean ok) {
        total++;
        if (ok) passed++;
        System.out.println((ok ? "[done] " : "[todo] ") + feature);
    }

    public static void main(String[] args) {
        TimetablePlanner p = TimetablePlanner.sample();
        boolean[][] clash = p.clashGraph();
        int edges = 0;
        for (int a = 0; a < clash.length; a++) for (int b = a + 1; b < clash.length; b++) if (clash[a][b]) edges++;
        System.out.println("Exam Timetable Planner: " + p.exams.size() + " exams, " + edges + " clashing pairs");
        check("minimumSlots() = 3", p.minimumSlots() == 3);
        int[] slots = p.assignSlots();
        boolean valid = slots != null;
        if (valid) {
            for (int a = 0; a < clash.length; a++) for (int b = 0; b < clash.length; b++) valid &= !clash[a][b] || slots[a] != slots[b];
            for (int[] m : p.mustPrecede) valid &= slots[m[0]] < slots[m[1]];
        }
        check("assignSlots() has no clashes and Math before AI", valid);
        check("roomsNeeded([[540,660],[600,720],[660,780],[720,840]]) = 2",
                TimetablePlanner.roomsNeeded(new int[][]{{540, 660}, {600, 720}, {660, 780}, {720, 840}}) == 2);
        List<String> order = p.precedenceOrder();
        check("precedenceOrder() lists all 5 exams with Math before AI",
                order.size() == 5 && order.indexOf("Math") < order.indexOf("AI"));
        System.out.println(passed + "/" + total + " features complete");
    }
}
