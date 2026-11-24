/*
 * Q8. Generics: a generic Pair<K,V>, a custom generic Stack<T>,
 *     a bounded generic method and a wildcard method.
 * Concepts: generic class, bounded type, wildcard, type safety, generic method
 */
import java.util.*;

public class Q08_Generics {
    public static void main(String[] args) {
        // Generic class with two type parameters
        Pair<String, Integer> p = new Pair<>("Sreeram", 21);
        System.out.println(p + "  swapped -> " + p.swap());

        // Custom generic Stack
        Stack<String> st = new Stack<>();
        st.push("A"); st.push("B"); st.push("C");
        System.out.println("Pop: " + st.pop() + ", Peek: " + st.peek() + ", Size: " + st.size());

        // Bounded generic method: T must be Comparable
        System.out.println("Max int    : " + max(Arrays.asList(4, 19, 7, 2)));
        System.out.println("Max string : " + max(Arrays.asList("pune", "delhi", "vijayawada")));

        // Wildcard: accepts List<Integer>, List<Double> ...
        System.out.println("Sum ints   : " + sumAll(Arrays.asList(1, 2, 3, 4)));
        System.out.println("Sum doubles: " + sumAll(Arrays.asList(1.5, 2.5, 3.0)));

        // Generic method with array
        Integer[] nums = {1, 2, 3};
        String[] words = {"x", "y", "z"};
        printArray(nums);
        printArray(words);

        // Type safety: next line would NOT compile
        // st.push(100);
    }

    static <T extends Comparable<T>> T max(List<T> list) {       // bounded type
        T best = list.get(0);
        for (T item : list) if (item.compareTo(best) > 0) best = item;
        return best;
    }

    static double sumAll(List<? extends Number> list) {          // upper-bounded wildcard
        double total = 0;
        for (Number n : list) total += n.doubleValue();
        return total;
    }

    static <E> void printArray(E[] arr) {
        System.out.println(Arrays.toString(arr));
    }
}

class Pair<K, V> {
    private final K key;
    private final V value;
    Pair(K key, V value) { this.key = key; this.value = value; }
    K getKey() { return key; }
    V getValue() { return value; }
    Pair<V, K> swap() { return new Pair<>(value, key); }
    @Override public String toString() { return "(" + key + ", " + value + ")"; }
}

class Stack<T> {
    private static class Node<T> {
        T data; Node<T> next;
        Node(T data, Node<T> next) { this.data = data; this.next = next; }
    }
    private Node<T> top;
    private int size;

    void push(T item) { top = new Node<>(item, top); size++; }

    T pop() {
        if (top == null) throw new RuntimeException("Stack is empty");
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    T peek() {
        if (top == null) throw new RuntimeException("Stack is empty");
        return top.data;
    }

    int size() { return size; }
    boolean isEmpty() { return top == null; }
}
