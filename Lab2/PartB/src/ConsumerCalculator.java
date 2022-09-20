import java.util.concurrent.BlockingQueue;

public class ConsumerCalculator implements Runnable {
    private final BlockingQueue<Item> items;
    private int totalSum;

    public ConsumerCalculator(BlockingQueue<Item> items) {
        this.items = items;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!items.isEmpty()) {
                    final int itemValue = items.take().getValue();
                    totalSum += itemValue;
                    System.out.println("Item stolen for " + itemValue);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getTotalSum() {
        return totalSum;
    }
}
