import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Session 1 demo: the greedy method. Coin change with canonical and non-canonical coin systems
 * (greedy vs optimal), fractional knapsack (greedy is optimal) vs 0/1 knapsack (greedy fails),
 * and the "assign cookies" matching problem.
 *
 * Compile:  javac -d out GreedyFundamentals.java
 * Run:      java -cp out GreedyFundamentals
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class GreedyFundamentals {

    /** Greedy change: always take the largest coin that fits. Returns the coins used. */
    static List<Integer> greedyChange(int[] coinsDesc, int amount) {
        List<Integer> used = new ArrayList<>();
        for (int c : coinsDesc)
            while (amount >= c) { used.add(c); amount -= c; }
        return amount == 0 ? used : null;
    }

    /** Optimal number of coins by dynamic programming (Module 16 preview), for comparison. */
    static int optimalCoins(int[] coins, int amount) {
        int[] best = new int[amount + 1];
        Arrays.fill(best, Integer.MAX_VALUE);
        best[0] = 0;
        for (int a = 1; a <= amount; a++)
            for (int c : coins)
                if (c <= a && best[a - c] != Integer.MAX_VALUE) best[a] = Math.min(best[a], best[a - c] + 1);
        return best[amount];
    }

    record Item(String name, int weight, int value) {
        double ratio() { return (double) value / weight; }
    }

    /** Fractional knapsack: take items by value/weight ratio; split the last one. O(n log n). */
    static double fractionalKnapsack(List<Item> items, int capacity, List<String> log) {
        List<Item> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparingDouble(Item::ratio).reversed());
        double total = 0;
        int left = capacity;
        for (Item it : sorted) {
            if (left == 0) break;
            int take = Math.min(left, it.weight());
            total += take * it.ratio();
            left -= take;
            if (log != null) log.add(String.format("%-8s ratio %.2f take %d/%d kg -> +%.1f", it.name(), it.ratio(), take, it.weight(), take * it.ratio()));
        }
        return total;
    }

    /** 0/1 knapsack: greedy by ratio (may be wrong) vs exact DP over capacity. */
    static int greedyZeroOne(List<Item> items, int capacity) {
        List<Item> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparingDouble(Item::ratio).reversed());
        int value = 0, left = capacity;
        for (Item it : sorted) if (it.weight() <= left) { value += it.value(); left -= it.weight(); }
        return value;
    }

    static int exactZeroOne(List<Item> items, int capacity) {
        int[] dp = new int[capacity + 1];
        for (Item it : items)
            for (int c = capacity; c >= it.weight(); c--) dp[c] = Math.max(dp[c], dp[c - it.weight()] + it.value());
        return dp[capacity];
    }

    /** Assign cookies: smallest cookie that satisfies the least greedy child first. Two pointers after sorting. */
    static int assignCookies(int[] greed, int[] cookies) {
        int[] g = greed.clone(), s = cookies.clone();
        Arrays.sort(g);
        Arrays.sort(s);
        int child = 0;
        for (int i = 0; i < s.length && child < g.length; i++)
            if (s[i] >= g[child]) child++;               // this cookie satisfies the current child
        return child;
    }

    public static void main(String[] args) {
        System.out.println("== Coin change with Pakistani coins and notes (1, 2, 5, 10, 20, 50, 100) ==");
        int[] pkr = {100, 50, 20, 10, 5, 2, 1};
        for (int amt : new int[]{37, 188, 99}) {
            List<Integer> g = greedyChange(pkr, amt);
            System.out.println("Rs " + amt + ": greedy " + g + " = " + g.size() + " pieces, optimal = " + optimalCoins(pkr, amt));
        }

        System.out.println("\n== A coin system where greedy fails: {1, 3, 4} ==");
        int[] odd = {4, 3, 1};
        for (int amt : new int[]{6, 10}) {
            List<Integer> g = greedyChange(odd, amt);
            System.out.println("amount " + amt + ": greedy " + g + " = " + g.size() + " coins, optimal = " + optimalCoins(odd, amt) + " coins");
        }

        System.out.println("\n== Fractional knapsack: a 50 kg relief truck ==");
        List<Item> goods = List.of(new Item("rice", 10, 60), new Item("lentils", 20, 100),
                new Item("oil", 30, 120), new Item("tea", 5, 50));
        List<String> log = new ArrayList<>();
        double best = fractionalKnapsack(goods, 50, log);
        for (String line : log) System.out.println("  " + line);
        System.out.printf("  total value = %.1f%n", best);

        System.out.println("\n== Same goods, but items cannot be split (0/1 knapsack) ==");
        System.out.println("greedy by ratio = " + greedyZeroOne(goods, 50) + ", exact (DP) = " + exactZeroOne(goods, 50));
        List<Item> trap = List.of(new Item("A", 10, 60), new Item("B", 20, 100), new Item("C", 30, 120));
        System.out.println("classic trap (cap 50): greedy = " + greedyZeroOne(trap, 50) + ", exact = " + exactZeroOne(trap, 50));

        System.out.println("\n== Assign cookies ==");
        System.out.println("greed [1, 2, 3], cookies [1, 1] -> " + assignCookies(new int[]{1, 2, 3}, new int[]{1, 1}) + " happy child");
        System.out.println("greed [1, 2], cookies [1, 2, 3] -> " + assignCookies(new int[]{1, 2}, new int[]{1, 2, 3}) + " happy children");
        System.out.println("greed [3, 1, 4, 2], cookies [2, 2, 5] -> " + assignCookies(new int[]{3, 1, 4, 2}, new int[]{2, 2, 5}) + " happy children");
    }
}
