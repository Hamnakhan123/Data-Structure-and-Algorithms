import java.util.List;

/**
 * Capstone 1 checks: run after each milestone. Expected values are computed from the sample map.
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class NavigatorChecks {
    static int passed = 0, total = 0;

    static void check(String feature, boolean ok) {
        total++;
        if (ok) passed++;
        System.out.println((ok ? "[done] " : "[todo] ") + feature);
    }

    public static void main(String[] args) {
        CampusNavigator c = CampusNavigator.sample();
        System.out.println("Campus Navigator: " + c.places.size() + " places loaded");
        check("shortestDistance(Main Gate, Labs) = 230", c.shortestDistance("Main Gate", "Labs") == 230);
        check("shortestRoute(Main Gate, Labs) = [Main Gate, Admin, Labs]",
                c.shortestRoute("Main Gate", "Labs").equals(List.of("Main Gate", "Admin", "Labs")));
        check("fewestWalkways(Main Gate, Cafeteria) = 2", c.fewestWalkways("Main Gate", "Cafeteria") == 2);
        check("accessibleDistance(Main Gate, Cafeteria) = 250", c.accessibleDistance("Main Gate", "Cafeteria") == 250);
        check("suggest(\"c\") = [Cafeteria, CS Block]", c.suggest("c").equals(List.of("Cafeteria", "CS Block")));
        check("cablingCost() = 680", c.cablingCost() == 680);
        System.out.println(passed + "/" + total + " features complete");
    }
}
