/*
 * Q9. Create an immutable class and demonstrate the equals()/hashCode() contract.
 *     Show what breaks in a HashSet when hashCode() is NOT overridden.
 * Concepts: immutability, final, defensive copy, equals, hashCode, HashSet
 */
import java.util.*;

public class Q09_ImmutableAndEquals {
    public static void main(String[] args) {
        // --- Part A: equals() without hashCode() breaks hash-based collections ---
        Set<BadPoint> badSet = new HashSet<>();
        badSet.add(new BadPoint(1, 2));
        badSet.add(new BadPoint(1, 2));
        System.out.println("BadPoint  equals true? " + new BadPoint(1, 2).equals(new BadPoint(1, 2)));
        System.out.println("BadPoint  set size   : " + badSet.size() + "  (expected 1, usually prints 2)");

        Set<GoodPoint> goodSet = new HashSet<>();
        goodSet.add(new GoodPoint(1, 2));
        goodSet.add(new GoodPoint(1, 2));
        System.out.println("GoodPoint set size   : " + goodSet.size() + "  (correct)");
        System.out.println("Same hash codes      : " + (new GoodPoint(1, 2).hashCode() == new GoodPoint(1, 2).hashCode()));

        // --- Part B: immutable class with a mutable field (defensive copies) ---
        List<String> subjects = new ArrayList<>(List.of("DAA", "Java"));
        ImmutableStudent s = new ImmutableStudent("Sreeram", 20250802351L, subjects);

        subjects.add("RDBMS");                           // modify original list
        System.out.println("After changing source list: " + s.getSubjects());

        try {
            s.getSubjects().add("MSET");                 // try to modify returned list
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify returned list -> UnsupportedOperationException");
        }

        ImmutableStudent s2 = s.withName("Sushanth");    // "modify" = create a new object
        System.out.println(s + "  |  " + s2);
    }
}

class BadPoint {
    final int x, y;
    BadPoint(int x, int y) { this.x = x; this.y = y; }
    @Override public boolean equals(Object o) {          // equals overridden, hashCode NOT
        if (!(o instanceof BadPoint)) return false;
        BadPoint p = (BadPoint) o;
        return x == p.x && y == p.y;
    }
}

class GoodPoint {
    final int x, y;
    GoodPoint(int x, int y) { this.x = x; this.y = y; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoodPoint p = (GoodPoint) o;
        return x == p.x && y == p.y;
    }
    @Override public int hashCode() { return Objects.hash(x, y); }
}

final class ImmutableStudent {                           // 1. final class: cannot be subclassed
    private final String name;                           // 2. private final fields
    private final long prn;
    private final List<String> subjects;

    ImmutableStudent(String name, long prn, List<String> subjects) {
        this.name = name;
        this.prn = prn;
        this.subjects = new ArrayList<>(subjects);       // 3. defensive copy on the way in
    }

    String getName() { return name; }                    // 4. no setters
    long getPrn() { return prn; }

    List<String> getSubjects() {
        return Collections.unmodifiableList(subjects);   // 5. read-only view on the way out
    }

    ImmutableStudent withName(String newName) {
        return new ImmutableStudent(newName, prn, subjects);
    }

    @Override public String toString() { return name + " " + subjects; }
}
