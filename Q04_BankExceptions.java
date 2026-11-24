/*
 * Q4. Bank withdrawal system with custom checked and unchecked exceptions,
 *     exception chaining, multi-catch, finally and try-with-resources.
 * Concepts: user-defined exceptions, throw/throws, finally, AutoCloseable, chaining
 */
public class Q04_BankExceptions {
    public static void main(String[] args) {
        Account acc = new Account("ACC-101", 5000);

        double[] attempts = {2000, 4000, -50, 1000};
        for (double amt : attempts) {
            // try-with-resources: log is closed automatically, even on exception
            try (TransactionLog log = new TransactionLog(acc.getId())) {
                acc.withdraw(amt);
                log.record("Withdrew " + amt + ", balance = " + acc.getBalance());
            } catch (InsufficientFundsException e) {            // checked
                System.out.println("DECLINED: " + e.getMessage()
                        + " (short by " + e.getShortfall() + ")");
            } catch (InvalidAmountException e) {                // unchecked
                System.out.println("INVALID : " + e.getMessage());
            } finally {
                System.out.println("-- transaction attempt finished --");
            }
        }

        // Exception chaining: wrap a low-level exception in a high-level one
        try {
            processStatement("abc");
        } catch (StatementException e) {
            System.out.println("Top-level : " + e.getMessage());
            System.out.println("Root cause: " + e.getCause());
        }

        // Multi-catch
        try {
            Object o = null;
            o.toString();
        } catch (NullPointerException | ArithmeticException e) {
            System.out.println("Multi-catch handled: " + e.getClass().getSimpleName());
        }
    }

    static void processStatement(String month) throws StatementException {
        try {
            Integer.parseInt(month);
        } catch (NumberFormatException e) {
            throw new StatementException("Could not process statement", e);
        }
    }
}

class InsufficientFundsException extends Exception {            // CHECKED
    private final double shortfall;
    InsufficientFundsException(String msg, double shortfall) { super(msg); this.shortfall = shortfall; }
    double getShortfall() { return shortfall; }
}

class InvalidAmountException extends RuntimeException {         // UNCHECKED
    InvalidAmountException(String msg) { super(msg); }
}

class StatementException extends Exception {
    StatementException(String msg, Throwable cause) { super(msg, cause); }
}

class Account {
    private final String id;
    private double balance;
    Account(String id, double balance) { this.id = id; this.balance = balance; }
    String getId() { return id; }
    double getBalance() { return balance; }

    void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive: " + amount);
        if (amount > balance)
            throw new InsufficientFundsException("Balance " + balance + " is less than " + amount, amount - balance);
        balance -= amount;
    }
}

class TransactionLog implements AutoCloseable {
    private final String id;
    TransactionLog(String id) { this.id = id; System.out.println("[log opened for " + id + "]"); }
    void record(String msg) { System.out.println("[log] " + msg); }
    @Override public void close() { System.out.println("[log closed for " + id + "]"); }
}
