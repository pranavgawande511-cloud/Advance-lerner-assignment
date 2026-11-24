/*
 * Q1. Design an abstract class Shape with Circle, Rectangle and Triangle subclasses.
 *     Store them in one collection, sort them by area, and show runtime polymorphism.
 * Concepts: abstraction, inheritance, polymorphism, Comparable, encapsulation
 */
import java.util.*;

public class Q01_ShapeHierarchy {
    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(7));
        shapes.add(new Rectangle(10, 4));
        shapes.add(new Triangle(3, 4, 5));
        shapes.add(new Circle(2.5));

        System.out.println("--- Before sorting ---");
        for (Shape s : shapes) System.out.println(s);   

        Collections.sort(shapes);                   

        System.out.println("--- After sorting by area ---");
        for (Shape s : shapes) System.out.println(s);

        Shape largest = Collections.max(shapes);
        System.out.println("Largest shape: " + largest.getName());
    }
}

abstract class Shape implements Comparable<Shape> {
    private final String name;                        

    protected Shape(String name) { this.name = name; }

    public abstract double area();                  
    public abstract double perimeter();

    public String getName() { return name; }

    @Override
    public int compareTo(Shape other) { return Double.compare(this.area(), other.area()); }

    @Override
    public String toString() {
        return String.format("%-10s area = %8.2f   perimeter = %8.2f", name, area(), perimeter());
    }
}

class Circle extends Shape {
    private final double r;
    Circle(double r) { super("Circle"); this.r = r; }
    @Override public double area() { return Math.PI * r * r; }
    @Override public double perimeter() { return 2 * Math.PI * r; }
}

class Rectangle extends Shape {
    private final double l, b;
    Rectangle(double l, double b) { super("Rectangle"); this.l = l; this.b = b; }
    @Override public double area() { return l * b; }
    @Override public double perimeter() { return 2 * (l + b); }
}

class Triangle extends Shape {
    private final double a, b, c;
    Triangle(double a, double b, double c) {
        super("Triangle");
        if (a + b <= c || a + c <= b || b + c <= a)
            throw new IllegalArgumentException("Invalid triangle sides");
        this.a = a; this.b = b; this.c = c;
    }
    @Override public double area() {                 
        double s = perimeter() / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
    @Override public double perimeter() { return a + b + c; }
}
