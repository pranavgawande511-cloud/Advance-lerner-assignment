# Java OOP: Hard-Level Theory Questions (10 Non-Programming)

**Name:** Pranav Gawande  | **PRN:** 20250802330 | **Course:** Object Oriented Technology using Java (CSE2002)

---

## Q1. Differentiate between an abstract class and an interface. When would you choose each?

| Point | Abstract class | Interface |
|---|---|---|
| Keyword | `abstract class` | `interface` |
| Methods | Abstract + concrete methods | Abstract, `default`, `static`, `private` methods |
| Variables | Any kind (instance, static, final) | Only `public static final` constants |
| Constructors | Yes (called through `super()`) | No |
| Inheritance | A class extends only **one** abstract class | A class can implement **many** interfaces |
| Access modifiers | Any | Methods are `public` by default |
| Purpose | "IS-A" relationship with shared state/code | "CAN-DO" capability / contract |

**Choose an abstract class** when subclasses share common state and code (e.g., `Shape` with a `name` field and shared `toString()`).
**Choose an interface** when unrelated classes must follow the same contract (e.g., `Comparable`, `Runnable`, `PaymentMethod`) or when you need multiple inheritance of type.

```java
abstract class Animal { String name; abstract void sound(); void sleep(){ System.out.println("zzz"); } }
interface Swimmer { void swim(); }
class Duck extends Animal implements Swimmer { void sound(){...} public void swim(){...} }
```

---

## Q2. Explain method overloading vs overriding, and compile-time vs runtime polymorphism.

**Overloading**: same method name, **different parameter list**, in the same class (or subclass). The compiler picks the method using the argument types, so this is **compile-time (static) polymorphism**. Return type alone cannot distinguish overloaded methods.

**Overriding**: a subclass gives its own body to a superclass method with the **same signature**. The JVM picks the method from the **actual object type** at run time, so this is **runtime (dynamic) polymorphism**.

| Point | Overloading | Overriding |
|---|---|---|
| Where | Same class | Parent-child classes |
| Signature | Must differ | Must be same |
| Binding | Early (compile time) | Late (runtime) |
| `static`/`private`/`final` methods | Can be overloaded | Cannot be overridden |
| Access/return | Free to change | Cannot reduce access; covariant return allowed |

```java
class A { void show(int x){}  void show(String s){} }          // overloading
class P { void talk(){ System.out.println("P"); } }
class C extends P { @Override void talk(){ System.out.println("C"); } }
P ref = new C();  ref.talk();   // prints C  (runtime polymorphism)
```

---

## Q3. How do the `super` keyword and constructor chaining work?

- `super.method()` calls the parent's version of an overridden method; `super.field` accesses a hidden parent field.
- `super(...)` calls a parent constructor. It must be the **first statement** in a constructor.
- If you do not write `super(...)`, the compiler inserts `super();` automatically. If the parent has no no-arg constructor, a compile error occurs.
- `this(...)` calls another constructor of the **same class** (constructor chaining within a class). `this(...)` and `super(...)` cannot both be the first statement.

**Order of execution:** parent static blocks → child static blocks → parent instance block + constructor → child instance block + constructor.

```java
class Vehicle { Vehicle(){ System.out.println("Vehicle"); } }
class Car extends Vehicle {
    Car(){ this("Petrol"); System.out.println("Car()"); }
    Car(String fuel){ super(); System.out.println("Car(fuel)"); }
}
new Car();   // Vehicle, Car(fuel), Car()
```

---

## Q4. Why does Java not support multiple inheritance of classes? How do interfaces solve this?

Java avoids multiple class inheritance because of the **diamond problem**. If class `D` extends both `B` and `C`, and both inherit and override `show()` from `A`, the compiler cannot decide which `show()` `D` should inherit. It also complicates constructor chaining and object layout.

**Interfaces solve it** because they historically had no implementation, so there is nothing to conflict. A class may implement many interfaces. Since Java 8, if two interfaces provide the same `default` method, the class **must override it** and may pick one explicitly:

```java
interface A { default void hi(){ System.out.println("A"); } }
interface B { default void hi(){ System.out.println("B"); } }
class C implements A, B {
    public void hi(){ A.super.hi(); }     // resolves the conflict explicitly
}
```

---

## Q5. Differentiate checked and unchecked exceptions. Explain `throw`, `throws`, `try-with-resources`.

- **Checked exceptions** are subclasses of `Exception` (but not `RuntimeException`), e.g., `IOException`, `SQLException`. The compiler **forces** you to handle them (`try-catch`) or declare them (`throws`).
- **Unchecked exceptions** are subclasses of `RuntimeException` or `Error`, e.g., `NullPointerException`, `ArithmeticException`. They are not checked at compile time and usually show programming bugs.
- `throw` actually raises an exception object: `throw new IllegalArgumentException("bad");`
- `throws` declares in a method signature that the method may pass an exception to its caller.
- `try-with-resources` automatically closes any object that implements `AutoCloseable` when the block ends, even if an exception occurs, replacing a manual `finally { close(); }`.

```java
try (BufferedReader br = new BufferedReader(new FileReader("a.txt"))) {
    System.out.println(br.readLine());
} catch (IOException e) { e.printStackTrace(); }
```

**Best practice:** make a custom exception *checked* if the caller can recover (e.g., `InsufficientFundsException`) and *unchecked* if it signals a bug or invalid input.

---

## Q6. Explain `final`, `finally` and `finalize()`.

| Term | What it is | Use |
|---|---|---|
| `final` | Keyword (modifier) | Variable → constant; method → cannot be overridden; class → cannot be inherited |
| `finally` | Block after `try/catch` | Always runs (cleanup code), except if `System.exit()` is called or the JVM crashes |
| `finalize()` | Method of `Object` | Called by the garbage collector before reclaiming an object. **Deprecated** since Java 9 and for removal; use `try-with-resources` or `Cleaner` instead |

```java
final int MAX = 10;                       // cannot be reassigned
try { int x = 5 / 0; }
catch (ArithmeticException e) { System.out.println("error"); }
finally { System.out.println("always runs"); }
```

---

## Q7. Explain the thread life cycle. Differentiate `synchronized` and `volatile`.

**Thread states:** `NEW` → (`start()`) → `RUNNABLE` → may become `BLOCKED` (waiting for a monitor lock), `WAITING` (`wait()`, `join()`), or `TIMED_WAITING` (`sleep(ms)`, `wait(ms)`) → back to `RUNNABLE` → `TERMINATED` when `run()` ends.

| Point | `synchronized` | `volatile` |
|---|---|---|
| Applies to | Methods / blocks | Variables only |
| Mutual exclusion | **Yes**, one thread at a time | **No** |
| Visibility | Yes | Yes (always read from main memory) |
| Atomicity | Yes, for the whole block | Only for a single read/write, **not** `count++` |
| Can block threads | Yes | No |

Use `volatile` for simple flags (`volatile boolean running`). Use `synchronized` (or `AtomicInteger`, `ReentrantLock`) when a sequence like read-modify-write must be atomic.

---

## Q8. Explain the internal working of HashMap.

1. A `HashMap` is an array of **buckets** (default capacity 16, load factor 0.75).
2. `put(key, value)`: compute `key.hashCode()`, spread the bits, and find the index with `(n - 1) & hash`.
3. If the bucket is empty, store a new node. If not, compare keys with `equals()`. A matching key's value is **replaced**; otherwise the node is added to the bucket's **linked list**.
4. If a single bucket's chain grows beyond **8** nodes (and the table has at least 64 buckets), the chain is converted to a **balanced red-black tree**, so lookups become O(log n) instead of O(n) (Java 8+).
5. When the number of entries exceeds `capacity × 0.75`, the table is **resized to double** and entries are redistributed (rehashing).
6. `get(key)` finds the bucket through `hashCode()`, then uses `equals()` to find the exact key. Average time is **O(1)**.

**Important:** keys must have consistent `equals()` and `hashCode()`. If two equal objects return different hash codes, a `HashMap` cannot find them (see programming Q9). `HashMap` allows one `null` key and is **not thread-safe**; use `ConcurrentHashMap` instead.

---

## Q9. Differentiate encapsulation and abstraction with a real-life example.

| Point | Encapsulation | Abstraction |
|---|---|---|
| Idea | **Wrapping** data and methods together and **hiding data** | **Hiding implementation complexity**, showing only essentials |
| Question answered | "How is data protected?" | "What does it do (not how)?" |
| Implemented by | `private` fields + public getters/setters | `abstract` classes and interfaces |
| Level | Class/implementation level | Design level |

**Real-life example:** An ATM.
- *Abstraction*: you see only "Withdraw", "Deposit", "Check Balance". How the bank verifies the PIN and updates databases is hidden.
- *Encapsulation*: your account balance is not directly accessible; it changes only through controlled operations that validate input.

```java
abstract class Account { abstract void withdraw(double amt); }      // abstraction
class Savings extends Account {
    private double balance;                                         // encapsulated
    void withdraw(double amt){ if(amt>0 && amt<=balance) balance-=amt; }
}
```

---

## Q10. Explain the SOLID principles in the context of OOP.

| Letter | Principle | Meaning | Example |
|---|---|---|---|
| **S** | Single Responsibility | A class should have **one reason to change** | Separate `Invoice` (data), `InvoicePrinter` (printing), `InvoiceSaver` (storage) |
| **O** | Open/Closed | Open for **extension**, closed for **modification** | Add a new `Shape` subclass without editing the area calculator |
| **L** | Liskov Substitution | Subclass objects must work wherever the parent is expected | A `Square` that extends `Rectangle` but breaks `setWidth/setHeight` behaviour violates it |
| **I** | Interface Segregation | Many small, specific interfaces beat one large interface | Split `Worker { work(); eat(); }` into `Workable` and `Eatable` so a robot is not forced to `eat()` |
| **D** | Dependency Inversion | Depend on **abstractions**, not concrete classes | `OrderService` depends on a `PaymentMethod` interface, not on `UpiPayment` directly |

Together they make code easier to maintain, test and extend. The SplitMate project follows several: expense types extend `Expense` (O, L), `Settleable` is a small interface (I), and the managers work with abstractions (D).
