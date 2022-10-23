import java.util.concurrent.BlockingQueue;

public class ConsumerPutter implements Runnable {
    private final BlockingQueue<Item> items;
    private final BlockingQueue<Item> toCalculate;

    private int itemsProcessed;

    public ConsumerPutter(BlockingQueue<Item> items, BlockingQueue<Item> toCalculate) {
        this.items = items;
        this.toCalculate = toCalculate;
    }

    @Override
    public void run() {
        while (true) {
            if (itemsProcessed == EntryPointB.NUMBER_OF_ITEMS) {
                break;
            }
            if (!items.isEmpty()) {
                try {
                    Item item = items.take();
                    System.out.println("Item " + item.getName() + " stolen!");
                    toCalculate.add(item);
                    itemsProcessed++;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
