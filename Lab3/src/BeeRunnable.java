import java.util.concurrent.atomic.AtomicBoolean;

public class BeeRunnable implements Runnable {
    private final Pot pot;

    public BeeRunnable(Pot pot) {
        super();
        this.pot = pot;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            pot.addHoney();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

}
