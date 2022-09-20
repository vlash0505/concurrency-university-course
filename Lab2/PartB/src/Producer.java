import java.util.Stack;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private final BlockingQueue<Item> items;
    private final Stack<Item> stolenItems;

    public Producer(BlockingQueue<Item> items, Stack<Item> stolenItems) {
        this.items = items;
        this.stolenItems = stolenItems;
    }

    @Override
    public void run() {
        while (!stolenItems.isEmpty()) {
            items.add(stolenItems.pop());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
