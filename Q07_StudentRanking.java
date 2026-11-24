/*
 * Q7. Student result system using the Collections Framework:
 *     HashMap (marks), TreeMap (sorted report), PriorityQueue (top 3), Set, Comparator.
 * Concepts: List, Map, TreeMap, PriorityQueue, Comparator, Iterator
 */
import java.util.*;

public class Q07_StudentRanking {
    public static void main(String[] args) {
        // HashMap: name -> list of marks
        Map<String, List<Integer>> marks = new HashMap<>();
        marks.put("pranav", Arrays.asList(95, 92, 98));
        marks.put("shravanii",   Arrays.asList(88, 91, 85));
        marks.put("ankit",    Arrays.asList(72, 65, 80));
        marks.put("rushi",   Arrays.asList(90, 94, 89));
        marks.put("karan",  Arrays.asList(60, 70, 55));

        // Build Student objects with average
        List<Student> students = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> e : marks.entrySet()) {
            double avg = e.getValue().stream().mapToInt(Integer::intValue).average().orElse(0);
            students.add(new Student(e.getKey(), avg));
        }

        // TreeMap: alphabetical report
        Map<String, Double> sorted = new TreeMap<>();
        for (Student s : students) sorted.put(s.name, s.average);
        System.out.println("--- Alphabetical report (TreeMap) ---");
        sorted.forEach((n, a) -> System.out.printf("%-8s %.2f%n", n, a));

        // PriorityQueue with Comparator: highest average first
        PriorityQueue<Student> pq = new PriorityQueue<>(
                Comparator.comparingDouble((Student s) -> s.average).reversed());
        pq.addAll(students);

        System.out.println("--- Top 3 (PriorityQueue) ---");
        for (int rank = 1; rank <= 3 && !pq.isEmpty(); rank++) {
            Student s = pq.poll();
            System.out.printf("Rank %d: %-8s %.2f%n", rank, s.name, s.average);
        }

        // Remove failing students using Iterator (safe removal while iterating)
        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            if (it.next().average < 65) it.remove();
        }
        System.out.println("--- Students with average >= 65 ---");
        students.sort(Comparator.comparing((Student s) -> s.name));
        students.forEach(System.out::println);

        // Set removes duplicates, preserving insertion order
        Set<String> grades = new LinkedHashSet<>(Arrays.asList("A", "B", "A", "C", "B"));
        System.out.println("Distinct grades: " + grades);
    }
}

class Student {
    final String name;
    final double average;
    Student(String name, double average) { this.name = name; this.average = average; }
    @Override public String toString() { return String.format("%-8s avg = %.2f", name, average); }
}
