import java.util.concurrent.atomic.AtomicBoolean;

public class Pot {

    private final AtomicBoolean bearSemaphore;
    private final static int FULL_CAPACITY = 10;
    private int currentCapacity;

    public Pot(AtomicBoolean bearSemaphore) {
        this.bearSemaphore = bearSemaphore;
    }

    public void addHoney() {
        currentCapacity++;
        if (currentCapacity == FULL_CAPACITY) {
            bearSemaphore.set(false);
        }
        System.out.println("One portion of honey is generated.");
    }

    public void stealHoney() {
        currentCapacity = 0;
        bearSemaphore.set(true);
        System.out.println("Honey is stolen.");
    }
}
