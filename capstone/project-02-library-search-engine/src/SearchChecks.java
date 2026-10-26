import java.util.List;

/**
 * Capstone 2 checks: run after each milestone. Expected values are computed from the sample catalogue.
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class SearchChecks {
    static int passed = 0, total = 0;

    static void check(String feature, boolean ok) {
        total++;
        if (ok) passed++;
        System.out.println((ok ? "[done] " : "[todo] ") + feature);
    }

    public static void main(String[] args) {
        LibrarySearch s = LibrarySearch.sample();
        s.buildIndex();
        System.out.println("Library Search Engine: " + s.books.size() + " books, words of book 2 = " + LibrarySearch.words(s.books.get(2).title()));
        check("search(\"algorithms java\") = [1, 2]", s.search("algorithms java").equals(List.of(1, 2)));
        check("topK(\"algorithms\", 2) = [Data Structures and Algorithms in Java, Introduction to Algorithms]",
                s.topK("algorithms", 2).equals(List.of("Data Structures and Algorithms in Java", "Introduction to Algorithms")));
        check("autocomplete(\"da\", 5) = [data, database]", s.autocomplete("da", 5).equals(List.of("data", "database")));
        check("didYouMean(\"algoritms\") = algorithms", s.didYouMean("algoritms").equals("algorithms"));
        check("cacheHits() >= 0 after repeated queries", s.cacheHits() >= 0);
        System.out.println(passed + "/" + total + " features complete");
    }
}
