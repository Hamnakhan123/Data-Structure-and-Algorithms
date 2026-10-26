import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Module 15 — reference solutions for the lab tasks of Sessions 1-2.
 * Each solution sits between "// >>> name" and "// <<< name" markers.
 *
 * Compile:  javac -d out *.java
 * Run:      java -cp out Labs15 1    (Session 1 labs; use 2 for Session 2, no argument for all)
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Labs15 {

    // >>> jumpGame
    /** Can you reach the last index? a[i] = maximum jump from i. Track the farthest reachable index. */
    static boolean canReach(int[] a) {
        int far = 0;
        for (int i = 0; i < a.length; i++) {
            if (i > far) return false;                  // stuck before reaching i
            far = Math.max(far, i + a[i]);
        }
        return true;
    }

    /** Minimum number of jumps (reachable input): BFS by "levels" of reach. */
    static int minJumps(int[] a) {
        int jumps = 0, curEnd = 0, far = 0;
        for (int i = 0; i < a.length - 1; i++) {
            far = Math.max(far, i + a[i]);
            if (i == curEnd) { jumps++; curEnd = far; }  // must jump: choose the best reach so far
        }
        return jumps;
    }
    // <<< jumpGame

    // >>> gasStation
    /** Start index to drive around a circular route, or -1. If the tank goes negative, start after that point. */
    static int gasStation(int[] gas, int[] cost) {
        int total = 0, tank = 0, start = 0;
        for (int i = 0; i < gas.length; i++) {
            int d = gas[i] - cost[i];
            total += d;
            tank += d;
            if (tank < 0) { start = i + 1; tank = 0; }  // no start in [start..i] can work
        }
        return total >= 0 ? start : -1;
    }
    // <<< gasStation

    // >>> minPlatforms
    /** Minimum railway platforms: sweep arrivals and departures in time order. */
    static int minPlatforms(int[] arr, int[] dep) {
        int[] a = arr.clone(), d = dep.clone();
        Arrays.sort(a);
        Arrays.sort(d);
        int i = 0, j = 0, cur = 0, best = 0;
        while (i < a.length) {
            if (a[i] <= d[j]) { cur++; i++; best = Math.max(best, cur); }   // a train arrives before the next leaves
            else { cur--; j++; }
        }
        return best;
    }
    // <<< minPlatforms

    // >>> mergeIntervals
    /** Merge overlapping intervals: sort by start, extend the last merged interval when they overlap. */
    static List<int[]> merge(int[][] iv) {
        int[][] s = iv.clone();
        Arrays.sort(s, Comparator.comparingInt((int[] x) -> x[0]));
        List<int[]> out = new ArrayList<>();
        for (int[] x : s) {
            if (!out.isEmpty() && x[0] <= out.get(out.size() - 1)[1])
                out.get(out.size() - 1)[1] = Math.max(out.get(out.size() - 1)[1], x[1]);
            else out.add(new int[]{x[0], x[1]});
        }
        return out;
    }
    // <<< mergeIntervals

    // >>> minArrows
    /** Fewest points that touch every interval (arrows through balloons): earliest end first. */
    static int minArrows(int[][] iv) {
        int[][] s = iv.clone();
        Arrays.sort(s, Comparator.comparingInt((int[] x) -> x[1]));
        int arrows = 0;
        long last = Long.MIN_VALUE;
        for (int[] x : s)
            if (x[0] > last) { arrows++; last = x[1]; }  // shoot at the end of this interval
        return arrows;
    }
    // <<< minArrows

    // >>> eraseOverlap
    /** Fewest intervals to remove so the rest do not overlap = n - (max non-overlapping set). */
    static int eraseOverlap(int[][] iv) {
        int[][] s = iv.clone();
        Arrays.sort(s, Comparator.comparingInt((int[] x) -> x[1]));
        int keep = 0;
        long lastEnd = Long.MIN_VALUE;
        for (int[] x : s) if (x[0] >= lastEnd) { keep++; lastEnd = x[1]; }
        return iv.length - keep;
    }
    // <<< eraseOverlap

    static String show(List<int[]> l) {
        StringBuilder sb = new StringBuilder("[");
        for (int[] x : l) sb.append(sb.length() > 1 ? ", " : "").append(Arrays.toString(x));
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        int s = args.length > 0 ? Integer.parseInt(args[0]) : 0;
        if (s == 0 || s == 1) {
            System.out.println("-- Session 1 labs --");
            System.out.println("canReach([2, 3, 1, 1, 4]) = " + canReach(new int[]{2, 3, 1, 1, 4})
                    + ", canReach([3, 2, 1, 0, 4]) = " + canReach(new int[]{3, 2, 1, 0, 4})
                    + ", minJumps([2, 3, 1, 1, 4]) = " + minJumps(new int[]{2, 3, 1, 1, 4}));
            System.out.println("gasStation(gas [1,2,3,4,5], cost [3,4,5,1,2]) = " + gasStation(new int[]{1, 2, 3, 4, 5}, new int[]{3, 4, 5, 1, 2}));
            System.out.println("minPlatforms = " + minPlatforms(new int[]{900, 940, 950, 1100, 1500, 1800}, new int[]{910, 1200, 1120, 1130, 1900, 2000}));
        }
        if (s == 0 || s == 2) {
            System.out.println("-- Session 2 labs --");
            System.out.println("merge([1,3],[2,6],[8,10],[15,18],[17,20]) = " + show(merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}, {17, 20}})));
            System.out.println("minArrows([10,16],[2,8],[1,6],[7,12]) = " + minArrows(new int[][]{{10, 16}, {2, 8}, {1, 6}, {7, 12}}));
            System.out.println("eraseOverlap([1,2],[2,3],[3,4],[1,3]) = " + eraseOverlap(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 3}}));
        }
    }
}
