package port;

public class Terminal {

    public static final int FULL_CAPACITY = 10;

    private int currentCapacity = 0;
    private final TerminalSemaphore semaphore = new TerminalSemaphore();

    public void receiveContainer() {
        if (isFull()) {
            System.out.println("Jetty is at it's full capacity.");
            return;
        }
        currentCapacity++;
        System.out.println("\n\nContainer was added to the jetty\n\n");
        conductMaintenance();
    }

    public void loadShipContainer() {
        if (isEmpty()) {
            System.out.println("Jetty doesn't have any containers.");
            return;
        }
        currentCapacity--;
        System.out.println("\n\nContainer was given to the ship\n\n");
        conductMaintenance();
    }

    private void conductMaintenance() {
        System.out.println("\n\nTerminal is being maintained\n\n");
        setClosed();
        pauseWork();
        open();
    }

    public void pauseWork() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setClosed() {
        semaphore.setClosed(true);
    }

    public void open() {
        semaphore.setClosed(false);
    }

    public boolean isClosed() {
        return semaphore.isClosed();
    }

    public boolean isFull() {
        return currentCapacity == FULL_CAPACITY;
    }

    public boolean isEmpty() {
        return currentCapacity == 0;
    }
}
