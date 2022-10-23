import java.util.concurrent.atomic.AtomicBoolean;

public class BearRunnable implements Runnable {

    private final AtomicBoolean bearSemaphore;
    private final Pot pot;

    public BearRunnable(AtomicBoolean bearSemaphore, Pot pot) {
        super();
        this.bearSemaphore = bearSemaphore;
        this.pot = pot;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            if (!bearSemaphore.equals(true)) {
                System.out.println("Bear wakes up");
                pot.stealHoney();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
