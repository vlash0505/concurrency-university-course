import java.util.concurrent.atomic.AtomicBoolean;

public class Pot {

    private final static int FULL_CAPACITY = 10;

    private final AtomicBoolean bearSemaphore;
    private int currentCapacity;

    public Pot(AtomicBoolean bearSemaphore) {
        this.bearSemaphore = bearSemaphore;
    }

    public synchronized void addHoney() {
        if (currentCapacity == FULL_CAPACITY) {
            bearSemaphore.set(false);
            return;
        }
        currentCapacity++;
        System.out.println("One portion of honey is generated.");
    }

    public void stealHoney() {
        currentCapacity = 0;
        bearSemaphore.set(true);
        System.out.println("Honey is stolen.");
    }
}
