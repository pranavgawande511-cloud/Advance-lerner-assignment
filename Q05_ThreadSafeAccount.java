/*
 * Q5. Show a race condition on a shared bank account and fix it with synchronized.
 *     Two threads each deposit 1 rupee 1,000,000 times.
 * Concepts: Thread, Runnable, race condition, synchronized, join
 * Note: the unsafe result differs on every run (always <= expected); the safe one is exact.
 */
public class Q05_ThreadSafeAccount {
    public static void main(String[] args) throws InterruptedException {
        final int TIMES = 1_000_000;

        UnsafeAccount unsafe = new UnsafeAccount();
        runTwoThreads(() -> { for (int i = 0; i < TIMES; i++) unsafe.deposit(1); });
        System.out.println("Unsafe balance : " + unsafe.getBalance() + "  (expected " + 2 * TIMES + ")");

        SafeAccount safe = new SafeAccount();
        runTwoThreads(() -> { for (int i = 0; i < TIMES; i++) safe.deposit(1); });
        System.out.println("Safe balance   : " + safe.getBalance() + "  (expected " + 2 * TIMES + ")");
    }

    static void runTwoThreads(Runnable task) throws InterruptedException {
        Thread t1 = new Thread(task, "T1");
        Thread t2 = new Thread(task, "T2");
        t1.start(); t2.start();
        t1.join();  t2.join();            // wait for both to finish
    }
}

class UnsafeAccount {
    private int balance = 0;
    void deposit(int amt) {                              // read-modify-write is NOT atomic
        int current = balance;
        Thread.yield();                                  // lets the other thread sneak in
        balance = current + amt;
    }
    int getBalance() { return balance; }
}

class SafeAccount {
    private int balance = 0;
    synchronized void deposit(int amt) { balance += amt; }   // only one thread at a time
    synchronized int getBalance() { return balance; }
}
