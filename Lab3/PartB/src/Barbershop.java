import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Barbershop {

    private final static int MAX_SEATS = 4;

    public int currentCapacity;
    private final Barber barber;

    private final Semaphore customerReady;
    private final Semaphore barberReady;

    private final Semaphore customerDone;
    private final Semaphore barberDone;

    private final ReentrantLock mutex;

    public Barbershop() {
        currentCapacity = 0;

        customerReady = new Semaphore(0);
        barberReady = new Semaphore(0);
        customerDone = new Semaphore(0);
        barberDone = new Semaphore(0);

        mutex = new ReentrantLock();
        barber = new Barber();
    }

    public Void startService() {

        // wait for a customer to arrive
        try {
            customerReady.acquire();
        } catch (InterruptedException e) {
            System.out.println("Barber wait task is interrupted: " + e.getMessage());
        }

        // signal that barber is ready
        barberReady.release();
        barber.cutHair();


        // wait for a customer to approve done
        try {
            customerDone.acquire();
        } catch (InterruptedException e) {
            System.out.println("Barber wait task is interrupted: " + e.getMessage());
        }
        // signal the customer that barber is done
        barberDone.release();
        System.out.println("Haircut is done");
        return null;
    }

    public Void receiveNewCustomer() {

        Customer customer = new Customer();
        customer.enterBarbershop();
        mutex.lock();
        if (currentCapacity == MAX_SEATS) {
            mutex.unlock();
            customer.leave();
            return null;
        }
        currentCapacity++;
        mutex.unlock();
        customerReady.release();
        // wait for the barber to be available
        try {
            barberReady.acquire();
        } catch (InterruptedException e) {
            System.out.println("Customer wait task is interrupted: " + e.getMessage());
        }

        // get the haircut
        customer.getHaircut();

        // signal that customer is done
        customerDone.release();

        // wait for barber to approve done
        try {
            barberDone.acquire();
        } catch (InterruptedException e) {
            System.out.println("Customer wait task is interrupted: " + e.getMessage());
        }
        mutex.lock();
        currentCapacity -= 1;
        mutex.unlock();

        return null;
    }

}