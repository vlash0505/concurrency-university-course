import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class EntryPointB {
    public static void main(String[] args) {
        BlockingQueue<Item> firstBlockingQueue = new ArrayBlockingQueue<>(5);
        BlockingQueue<Item> secondBlockingQueue = new ArrayBlockingQueue<>(5);

        Stack<Item> items = new Stack<>();
        items.add(new Item("Gun", 500));
        items.add(new Item("Ammunition", 100));
        items.add(new Item("Armour", 200));
        items.add(new Item("Bike", 1000));
        items.add(new Item("Helmet", 100));

        ConsumerPutter putterRunnable = new ConsumerPutter(firstBlockingQueue, secondBlockingQueue);
        ConsumerCalculator calculatorRunnable = new ConsumerCalculator(secondBlockingQueue);

        Thread producerThread = new Thread(new Producer(firstBlockingQueue, items));
        Thread consumerPutterThread = new Thread(putterRunnable);
        Thread consumerCalculatorThread = new Thread(calculatorRunnable);

        producerThread.start();
        consumerPutterThread.start();
        consumerCalculatorThread.start();

        try {
            producerThread.join();
            consumerPutterThread.join();
            consumerCalculatorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
