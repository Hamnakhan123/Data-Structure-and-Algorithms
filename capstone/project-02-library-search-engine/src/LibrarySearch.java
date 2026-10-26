import java.util.ArrayList;
import java.util.List;

/**
 * Capstone 2 starter: Library Search Engine. Indexes book titles and supports keyword search with
 * ranking, prefix autocomplete, typo suggestions and a cache of recent queries. TODO methods compile
 * and return placeholders; SearchChecks reports progress.
 *
 * Compile:  javac -d out *.java
 * Run:      java -cp out SearchChecks
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class LibrarySearch {

    record Book(int id, String title, int borrowCount) { }

    final List<Book> books = new ArrayList<>();

    void addBook(String title, int borrowCount) { books.add(new Book(books.size(), title, borrowCount)); }

    /** Lower-case words of a title, split on anything that is not a letter or digit. */
    static List<String> words(String text) {
        List<String> out = new ArrayList<>();
        for (String w : text.toLowerCase().split("[^a-z0-9]+")) if (!w.isEmpty()) out.add(w);
        return out;
    }

    static LibrarySearch sample() {
        LibrarySearch s = new LibrarySearch();
        s.addBook("Introduction to Algorithms", 95);
        s.addBook("Algorithms in Java", 60);
        s.addBook("Data Structures and Algorithms in Java", 120);
        s.addBook("Java: The Complete Reference", 80);
        s.addBook("Clean Code", 70);
        s.addBook("Database System Concepts", 50);
        s.addBook("Artificial Intelligence: A Modern Approach", 90);
        s.addBook("Data Science from Scratch", 40);
        return s;
    }

    // ===================== TODO: Milestone 2 =====================

    /** Build the inverted index: word -> list of book ids containing it (Module 08). */
    void buildIndex() {
        // TODO: HashMap<String, List<Integer>>
    }

    /** Ids of books containing ALL query words, in increasing id order. */
    List<Integer> search(String query) {
        return List.of();   // TODO: intersect the posting lists of each word
    }

    /** Titles of the k matches with the highest borrowCount (ties: lower id first). */
    List<String> topK(String query, int k) {
        return List.of();   // TODO: size-k min-heap over search(query) (Module 11)
    }

    // ===================== TODO: Milestone 3 =====================

    /** Indexed words starting with prefix, alphabetically, at most limit of them. */
    List<String> autocomplete(String prefix, int limit) {
        return List.of();   // TODO: trie of all indexed words (Module 13)
    }

    /** The indexed word with the smallest edit distance to a misspelt word (ties: alphabetical). */
    String didYouMean(String word) {
        return "";   // TODO: edit distance DP (Module 16, Session 2)
    }

    /** Remember the last 'capacity' queries and their results; return how many cache hits so far. */
    int cacheHits() {
        return -1;   // TODO: LRU cache with LinkedHashMap(accessOrder = true) (Module 08, Session 2)
    }
}
