/*
 * Q2. Build a payment system using an interface with default and static methods.
 *     Implement UPI, CreditCard and Wallet payments; add a processing fee through
 *     a default method that one class overrides.
 * Concepts: interface, default method, static method, polymorphism, anonymous class
 */
import java.util.*;

public class Q02_PaymentInterface {
    public static void main(String[] args) {
        List<PaymentMethod> methods = List.of(
                new UpiPayment("sreeram@upi"),
                new CreditCardPayment("4111111111111111"),
                new WalletPayment(500)
        );

        double amount = 1000;
        for (PaymentMethod m : methods) {
            if (PaymentMethod.isValidAmount(amount)) {          // static method
                m.pay(amount);
            }
            System.out.println("Fee charged: " + m.fee(amount));  // default / overridden
            System.out.println();
        }

        // Anonymous class implementing the interface on the spot
        PaymentMethod cash = new PaymentMethod() {
            public void pay(double amt) { System.out.println("Paid " + amt + " in CASH"); }
        };
        cash.pay(250);
        System.out.println("Fee charged: " + cash.fee(250));
    }
}

interface PaymentMethod {
    void pay(double amount);                                      // abstract

    default double fee(double amount) { return 0; }               // default: free

    static boolean isValidAmount(double amount) {                 // static utility
        return amount > 0;
    }
}

class UpiPayment implements PaymentMethod {
    private final String upiId;
    UpiPayment(String upiId) { this.upiId = upiId; }
    @Override public void pay(double amount) {
        System.out.println("Paid " + amount + " via UPI (" + upiId + ")");
    }
}

class CreditCardPayment implements PaymentMethod {
    private final String cardNo;
    CreditCardPayment(String cardNo) { this.cardNo = cardNo; }
    @Override public void pay(double amount) {
        String masked = "**** **** **** " + cardNo.substring(cardNo.length() - 4);
        System.out.println("Paid " + amount + " via Credit Card " + masked);
    }
    @Override public double fee(double amount) { return amount * 0.02; }   // 2% fee
}

class WalletPayment implements PaymentMethod {
    private double balance;
    WalletPayment(double balance) { this.balance = balance; }
    @Override public void pay(double amount) {
        if (amount > balance) {
            System.out.println("Wallet payment FAILED: balance " + balance + " < " + amount);
        } else {
            balance -= amount;
            System.out.println("Paid " + amount + " via Wallet. Balance left: " + balance);
        }
    }
}
