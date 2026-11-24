/*
 * Q3. Model different employee types with their own salary logic.
 *     Show method overriding, super calls, upcasting, instanceof,
 *     method overloading and a polymorphic payroll loop.
 * Concepts: overriding, super, overloading, runtime polymorphism, instanceof
 */
import java.util.*;

public class Q03_EmployeeSalary {
    public static void main(String[] args) {
        Employee[] staff = {
                new FullTimeEmployee("Durga", 50000),
                new PartTimeEmployee("Ravi", 80, 250),
                new ContractEmployee("Surya", 3, 40000),
                new Manager("Aditya", 70000, 15000)
        };

        double total = 0;
        for (Employee e : staff) {                       // upcasting: parent reference
            e.display();                                 // overridden: runtime dispatch
            System.out.println("   Salary = " + e.calculateSalary());
            total += e.calculateSalary();

            if (e instanceof Manager m) {                // pattern matching instanceof
                System.out.println("   (Manager bonus = " + m.getBonus() + ")");
            }
        }
        System.out.println("Total payroll = " + total);

        // Method overloading (compile-time polymorphism)
        System.out.printf("%.2f%n", staff[0].raise(10));  // percentage raise
        System.out.printf("%.2f%n", staff[0].raise(5000, true));  // fixed raise
    }
}

abstract class Employee {
    protected final String name;
    Employee(String name) { this.name = name; }

    abstract double calculateSalary();

    void display() { System.out.println("Employee: " + name + " [" + getClass().getSimpleName() + "]"); }

    // Overloaded methods
    double raise(double percent) { return calculateSalary() * (1 + percent / 100); }
    double raise(double amount, boolean fixed) { return calculateSalary() + amount; }
}

class FullTimeEmployee extends Employee {
    protected double monthly;
    FullTimeEmployee(String name, double monthly) { super(name); this.monthly = monthly; }
    @Override double calculateSalary() { return monthly; }
}

class PartTimeEmployee extends Employee {
    private final int hours; private final double rate;
    PartTimeEmployee(String name, int hours, double rate) { super(name); this.hours = hours; this.rate = rate; }
    @Override double calculateSalary() { return hours * rate; }
}

class ContractEmployee extends Employee {
    private final int months; private final double perMonth;
    ContractEmployee(String name, int months, double perMonth) { super(name); this.months = months; this.perMonth = perMonth; }
    @Override double calculateSalary() { return perMonth; }
    @Override void display() {
        super.display();                                 // reuse parent behaviour
        System.out.println("   Contract duration: " + months + " months");
    }
}

class Manager extends FullTimeEmployee {
    private final double bonus;
    Manager(String name, double monthly, double bonus) { super(name, monthly); this.bonus = bonus; }
    double getBonus() { return bonus; }
    @Override double calculateSalary() { return super.calculateSalary() + bonus; }
}
