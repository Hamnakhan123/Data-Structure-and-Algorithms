import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Capstone 1 starter: Campus Navigator. Loads a weighted campus map and exposes the operations the
 * project must implement. Every TODO method compiles and returns a placeholder so the project runs
 * from day one; NavigatorChecks reports which features are finished.
 *
 * Compile:  javac -d out *.java
 * Run:      java -cp out NavigatorChecks
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class CampusNavigator {

    /** A walkway between two places, with its length in metres and whether it is wheelchair accessible. */
    record Walkway(int from, int to, int metres, boolean accessible) { }

    final List<String> places = new ArrayList<>();
    final Map<String, Integer> idOf = new HashMap<>();            // name -> vertex id (Module 08)
    final List<List<Walkway>> adj = new ArrayList<>();            // adjacency list (Module 12)

    int addPlace(String name) {
        idOf.put(name, places.size());
        places.add(name);
        adj.add(new ArrayList<>());
        return places.size() - 1;
    }

    void addWalkway(String a, String b, int metres, boolean accessible) {
        int u = idOf.get(a), v = idOf.get(b);
        adj.get(u).add(new Walkway(u, v, metres, accessible));
        adj.get(v).add(new Walkway(v, u, metres, accessible));
    }

    /** Sample data: replace or extend with your own campus. */
    static CampusNavigator sample() {
        CampusNavigator c = new CampusNavigator();
        for (String p : new String[]{"Main Gate", "Library", "Cafeteria", "CS Block", "Admin", "Labs", "Hostel", "Sports Complex"})
            c.addPlace(p);
        c.addWalkway("Main Gate", "Library", 120, true);
        c.addWalkway("Main Gate", "Admin", 80, true);
        c.addWalkway("Library", "Cafeteria", 60, false);
        c.addWalkway("Library", "CS Block", 90, true);
        c.addWalkway("Cafeteria", "CS Block", 40, true);
        c.addWalkway("CS Block", "Labs", 70, false);
        c.addWalkway("Admin", "Labs", 150, true);
        c.addWalkway("Labs", "Hostel", 200, true);
        c.addWalkway("Hostel", "Sports Complex", 110, true);
        return c;
    }

    // ===================== TODO: Milestone 2 =====================

    /** Shortest walking distance in metres from a to b (Dijkstra), or -1 if unreachable. */
    int shortestDistance(String a, String b) {
        return -1;   // TODO: Dijkstra with a PriorityQueue (Module 12, Session 3)
    }

    /** The places along the shortest route, including both ends; empty list if unreachable. */
    List<String> shortestRoute(String a, String b) {
        return List.of();   // TODO: keep parent[] during Dijkstra and walk back
    }

    /** Fewest walkways between a and b ignoring length (BFS), or -1. */
    int fewestWalkways(String a, String b) {
        return -1;   // TODO: BFS (Module 12, Session 1)
    }

    // ===================== TODO: Milestone 3 =====================

    /** Shortest distance using only accessible walkways, or -1. */
    int accessibleDistance(String a, String b) {
        return -1;   // TODO: Dijkstra that skips walkways with accessible == false
    }

    /** Place names starting with a prefix, alphabetically (case-insensitive). */
    List<String> suggest(String prefix) {
        return List.of();   // TODO: build a trie of lower-case names (Module 13, Session 1)
    }

    /** Minimum total metres of cable to connect every place (minimum spanning tree), or -1 if disconnected. */
    int cablingCost() {
        return -1;   // TODO: Kruskal with union-find (Module 12, Session 4)
    }
}
