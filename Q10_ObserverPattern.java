/*
 * Q10. Implement the Observer design pattern for a stock price alert system,
 *      with a thread-safe Singleton logger and a Factory for creating observers.
 * Concepts: Observer, Singleton, Factory, interfaces, loose coupling, polymorphism
 */
import java.util.*;

public class Q10_ObserverPattern {
    public static void main(String[] args) {
        Stock infy = new Stock("INFY", 1500);

        // Factory creates different observers without exposing their classes
        infy.subscribe(SubscriberFactory.create("mobile", "Sreeram"));
        infy.subscribe(SubscriberFactory.create("email", "durga@example.com"));
        Subscriber ravi = SubscriberFactory.create("mobile", "Ravi");
        infy.subscribe(ravi);

        infy.setPrice(1520);
        System.out.println();

        infy.unsubscribe(ravi);                          // Ravi no longer notified
        infy.setPrice(1480);

        // Singleton: same instance everywhere
        System.out.println();
        System.out.println("Same logger instance? " + (AppLogger.getInstance() == AppLogger.getInstance()));
        System.out.println("Total log entries: " + AppLogger.getInstance().count());
    }
}

interface Subscriber {                                   // Observer
    void update(String symbol, double oldPrice, double newPrice);
}

class Stock {                                            // Subject
    private final String symbol;
    private double price;
    private final List<Subscriber> subscribers = new ArrayList<>();

    Stock(String symbol, double price) { this.symbol = symbol; this.price = price; }

    void subscribe(Subscriber s)   { subscribers.add(s); }
    void unsubscribe(Subscriber s) { subscribers.remove(s); }

    void setPrice(double newPrice) {
        double old = price;
        price = newPrice;
        AppLogger.getInstance().log(symbol + " changed " + old + " -> " + newPrice);
        for (Subscriber s : subscribers) s.update(symbol, old, newPrice);   // notify all
    }
}

class MobileSubscriber implements Subscriber {
    private final String user;
    MobileSubscriber(String user) { this.user = user; }
    @Override public void update(String symbol, double oldP, double newP) {
        String arrow = newP > oldP ? "UP" : "DOWN";
        System.out.println("[Push -> " + user + "] " + symbol + " is " + arrow + " to " + newP);
    }
}

class EmailSubscriber implements Subscriber {
    private final String email;
    EmailSubscriber(String email) { this.email = email; }
    @Override public void update(String symbol, double oldP, double newP) {
        System.out.printf("[Email -> %s] %s moved %.2f%%%n", email, symbol, (newP - oldP) * 100 / oldP);
    }
}

class SubscriberFactory {                                // Factory
    static Subscriber create(String type, String target) {
        switch (type.toLowerCase()) {
            case "mobile": return new MobileSubscriber(target);
            case "email":  return new EmailSubscriber(target);
            default: throw new IllegalArgumentException("Unknown subscriber type: " + type);
        }
    }
}

class AppLogger {                                        // Singleton (double-checked locking)
    private static volatile AppLogger instance;
    private final List<String> entries = new ArrayList<>();

    private AppLogger() {}                               // private constructor

    static AppLogger getInstance() {
        if (instance == null) {
            synchronized (AppLogger.class) {
                if (instance == null) instance = new AppLogger();
            }
        }
        return instance;
    }

    synchronized void log(String msg) { entries.add(msg); }
    synchronized int count() { return entries.size(); }
}
