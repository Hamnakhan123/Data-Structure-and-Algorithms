import java.util.ArrayList;
import java.util.List;

/**
 * Session 1 demo: a trie (prefix tree) over lowercase words with insert, search, startsWith,
 * prefix counting, autocomplete (sorted, limited), and deletion that prunes unused nodes.
 *
 * Compile:  javac -d out Trie.java
 * Run:      java -cp out Trie
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class Trie {

    static final class Node {
        final Node[] next = new Node[26];     // one slot per letter a..z
        boolean isWord;                       // a word ends exactly here
        int pass;                             // how many stored words pass through this node
    }

    private final Node root = new Node();
    private int words, nodes = 1;

    /** Insert a word: walk/create one node per character. O(L). */
    void insert(String w) {
        if (contains(w)) return;
        Node n = root;
        n.pass++;
        for (char ch : w.toCharArray()) {
            int i = ch - 'a';
            if (n.next[i] == null) { n.next[i] = new Node(); nodes++; }
            n = n.next[i];
            n.pass++;
        }
        n.isWord = true;
        words++;
    }

    private Node walk(String s) {
        Node n = root;
        for (char ch : s.toCharArray()) {
            n = n.next[ch - 'a'];
            if (n == null) return null;
        }
        return n;
    }

    boolean contains(String w) { Node n = walk(w); return n != null && n.isWord; }
    boolean startsWith(String p) { return walk(p) != null; }
    int countPrefix(String p) { Node n = walk(p); return n == null ? 0 : n.pass; }

    /** Up to 'limit' words starting with p, in alphabetical order (DFS visits letters a..z). */
    List<String> autocomplete(String p, int limit) {
        List<String> out = new ArrayList<>();
        Node n = walk(p);
        if (n != null) collect(n, new StringBuilder(p), out, limit);
        return out;
    }

    private void collect(Node n, StringBuilder sb, List<String> out, int limit) {
        if (out.size() >= limit) return;
        if (n.isWord) out.add(sb.toString());
        for (int i = 0; i < 26; i++) {
            if (n.next[i] == null) continue;
            sb.append((char) ('a' + i));
            collect(n.next[i], sb, out, limit);
            sb.deleteCharAt(sb.length() - 1);           // backtrack
        }
    }

    /** Delete a word; nodes whose pass count drops to 0 are unlinked (pruned). */
    boolean delete(String w) {
        if (!contains(w)) return false;
        Node n = root;
        n.pass--;
        for (char ch : w.toCharArray()) {
            int i = ch - 'a';
            if (--n.next[i].pass == 0) {               // nobody else uses this branch
                nodes -= countNodes(n.next[i]);
                n.next[i] = null;
                words--;
                return true;
            }
            n = n.next[i];
        }
        n.isWord = false;
        words--;
        return true;
    }

    private int countNodes(Node n) {
        int c = 1;
        for (Node x : n.next) if (x != null) c += countNodes(x);
        return c;
    }

    /** Longest common prefix of all stored words: follow the single-child chain from the root. */
    String longestCommonPrefix() {
        StringBuilder sb = new StringBuilder();
        Node n = root;
        while (!n.isWord) {
            int only = -1, kids = 0;
            for (int i = 0; i < 26; i++) if (n.next[i] != null) { only = i; kids++; }
            if (kids != 1) break;
            sb.append((char) ('a' + only));
            n = n.next[only];
        }
        return sb.toString();
    }

    int size() { return words; }
    int nodeCount() { return nodes; }

    public static void main(String[] args) {
        Trie t = new Trie();
        String[] dict = {"car", "card", "care", "careful", "cart", "cat", "dog", "do", "dot"};
        for (String w : dict) t.insert(w);
        System.out.println("inserted " + t.size() + " words using " + t.nodeCount() + " nodes"
                + " (total letters = " + String.join("", dict).length() + ")");

        System.out.println("\ncontains(car) = " + t.contains("car") + ", contains(ca) = " + t.contains("ca")
                + ", contains(cars) = " + t.contains("cars"));
        System.out.println("startsWith(ca) = " + t.startsWith("ca") + ", startsWith(cow) = " + t.startsWith("cow"));
        System.out.println("countPrefix(car) = " + t.countPrefix("car") + ", countPrefix(do) = " + t.countPrefix("do")
                + ", countPrefix(\"\") = " + t.countPrefix(""));

        System.out.println("\nautocomplete(\"car\", 10) = " + t.autocomplete("car", 10));
        System.out.println("autocomplete(\"car\", 3)  = " + t.autocomplete("car", 3));
        System.out.println("autocomplete(\"d\", 10)   = " + t.autocomplete("d", 10));
        System.out.println("autocomplete(\"x\", 10)   = " + t.autocomplete("x", 10));

        System.out.println("\ndelete(careful) = " + t.delete("careful") + " -> nodes now " + t.nodeCount()
                + ", autocomplete(car) = " + t.autocomplete("car", 10));
        System.out.println("delete(car)     = " + t.delete("car") + " -> contains(car) = " + t.contains("car")
                + ", contains(card) = " + t.contains("card") + ", nodes now " + t.nodeCount());
        System.out.println("delete(zebra)   = " + t.delete("zebra"));

        Trie lcp = new Trie();
        for (String w : new String[]{"interview", "internet", "internal", "interval"}) lcp.insert(w);
        System.out.println("\nlongest common prefix of interview/internet/internal/interval = \"" + lcp.longestCommonPrefix() + "\"");
    }
}
