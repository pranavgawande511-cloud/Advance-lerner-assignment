/*
 * Q6. Producer-Consumer problem using a bounded buffer and wait()/notifyAll().
 * Concepts: inter-thread communication, wait, notifyAll, synchronized, guarded blocks
 */
import java.util.*;

public class Q06_ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(3);       // small capacity to force waiting
        final int ITEMS = 8;

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= ITEMS; i++) {
                buffer.put(i);
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= ITEMS; i++) {
                buffer.take();
            }
        }, "Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        System.out.println("All items produced and consumed.");
    }
}

class BoundedBuffer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    BoundedBuffer(int capacity) { this.capacity = capacity; }

    public synchronized void put(int item) {
        while (queue.size() == capacity) {                  // loop guards against spurious wakeups
            System.out.println("  Buffer FULL, producer waiting...");
            try { wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
        }
        queue.add(item);
        System.out.println("Produced " + item + "  " + queue);
        notifyAll();                                        // wake consumer
    }

    public synchronized int take() {
        while (queue.isEmpty()) {
            System.out.println("  Buffer EMPTY, consumer waiting...");
            try { wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return -1; }
        }
        int item = queue.remove();
        System.out.println("Consumed " + item + "  " + queue);
        notifyAll();                                        // wake producer
        return item;
    }
}
