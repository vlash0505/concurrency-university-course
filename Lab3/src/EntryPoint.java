import java.util.concurrent.atomic.AtomicBoolean;

public class EntryPoint {
    public static final int NUMBER_OF_BEES = 5;

    public static void main(String[] args) {
        AtomicBoolean bearSemaphore = new AtomicBoolean();
        bearSemaphore.set(true);
        Pot pot = new Pot(bearSemaphore);

        for (int i = 0; i < NUMBER_OF_BEES; i++) {
            new Thread(new BeeRunnable(pot), "" + i + "th bee").start();
        }
        new Thread(new BearRunnable(bearSemaphore, pot)).start();
    }

}
