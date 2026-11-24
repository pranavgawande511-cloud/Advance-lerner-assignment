# Sample Outputs
Run any file with `javac QXX_*.java && java QXX_<Name>` (or `java QXX_<Name>.java` on JDK 11+).
Q05 and Q06 depend on thread scheduling, so their exact output differs from run to run.

## Q01_ShapeHierarchy.java
```
--- Before sorting ---
Circle     area =   153.94   perimeter =    43.98
Rectangle  area =    40.00   perimeter =    28.00
Triangle   area =     6.00   perimeter =    12.00
Circle     area =    19.63   perimeter =    15.71
--- After sorting by area ---
Triangle   area =     6.00   perimeter =    12.00
Circle     area =    19.63   perimeter =    15.71
Rectangle  area =    40.00   perimeter =    28.00
Circle     area =   153.94   perimeter =    43.98
Largest shape: Circle
```

## Q02_PaymentInterface.java
```
Paid 1000.0 via UPI (sreeram@upi)
Fee charged: 0.0

Paid 1000.0 via Credit Card **** **** **** 1111
Fee charged: 20.0

Wallet payment FAILED: balance 500.0 < 1000.0
Fee charged: 0.0

Paid 250.0 in CASH
Fee charged: 0.0
```

## Q03_EmployeeSalary.java
```
Employee: Durga [FullTimeEmployee]
   Salary = 50000.0
Employee: Ravi [PartTimeEmployee]
   Salary = 20000.0
Employee: Surya [ContractEmployee]
   Contract duration: 3 months
   Salary = 40000.0
Employee: Aditya [Manager]
   Salary = 85000.0
   (Manager bonus = 15000.0)
Total payroll = 195000.0
55000.00
55000.00
```

## Q04_BankExceptions.java
```
[log opened for ACC-101]
[log] Withdrew 2000.0, balance = 3000.0
[log closed for ACC-101]
-- transaction attempt finished --
[log opened for ACC-101]
[log closed for ACC-101]
DECLINED: Balance 3000.0 is less than 4000.0 (short by 1000.0)
-- transaction attempt finished --
[log opened for ACC-101]
[log closed for ACC-101]
INVALID : Amount must be positive: -50.0
-- transaction attempt finished --
[log opened for ACC-101]
[log] Withdrew 1000.0, balance = 2000.0
[log closed for ACC-101]
-- transaction attempt finished --
Top-level : Could not process statement
Root cause: java.lang.NumberFormatException: For input string: "abc"
Multi-catch handled: NullPointerException
```

## Q05_ThreadSafeAccount.java
```
Unsafe balance : 1000001  (expected 2000000)
Safe balance   : 2000000  (expected 2000000)
```

## Q06_ProducerConsumer.java
```
Produced 1  [1]
Consumed 1  []
  Buffer EMPTY, consumer waiting...
Produced 2  [2]
Consumed 2  []
  Buffer EMPTY, consumer waiting...
Produced 3  [3]
Consumed 3  []
  Buffer EMPTY, consumer waiting...
Produced 4  [4]
Consumed 4  []
  Buffer EMPTY, consumer waiting...
Produced 5  [5]
Consumed 5  []
Produced 6  [6]
Consumed 6  []
Produced 7  [7]
Consumed 7  []
Produced 8  [8]
Consumed 8  []
All items produced and consumed.
```

## Q07_StudentRanking.java
```
--- Alphabetical report (TreeMap) ---
Aditya   61.67
Durga    88.00
Ravi     72.33
Sreeram  95.00
Surya    91.00
--- Top 3 (PriorityQueue) ---
Rank 1: Sreeram  95.00
Rank 2: Surya    91.00
Rank 3: Durga    88.00
--- Students with average >= 65 ---
Durga    avg = 88.00
Ravi     avg = 72.33
Sreeram  avg = 95.00
Surya    avg = 91.00
Distinct grades: [A, B, C]
```

## Q08_Generics.java
```
(Sreeram, 21)  swapped -> (21, Sreeram)
Pop: C, Peek: B, Size: 2
Max int    : 19
Max string : vijayawada
Sum ints   : 10.0
Sum doubles: 7.0
[1, 2, 3]
[x, y, z]
```

## Q09_ImmutableAndEquals.java
```
BadPoint  equals true? true
BadPoint  set size   : 2  (expected 1, usually prints 2)
GoodPoint set size   : 1  (correct)
Same hash codes      : true
After changing source list: [DAA, Java]
Cannot modify returned list -> UnsupportedOperationException
Sreeram [DAA, Java]  |  Sushanth [DAA, Java]
```

## Q10_ObserverPattern.java
```
[Push -> Sreeram] INFY is UP to 1520.0
[Email -> durga@example.com] INFY moved 1.33%
[Push -> Ravi] INFY is UP to 1520.0

[Push -> Sreeram] INFY is DOWN to 1480.0
[Email -> durga@example.com] INFY moved -2.63%

Same logger instance? true
Total log entries: 2
```
