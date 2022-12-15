import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class EntryPointB {

    private static final int NUM_ITERATION = 8;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newWorkStealingPool();
        Barbershop barbershop = new Barbershop();

        Callable<Void> barbershopTask = barbershop::startService;
        Callable<Void> receiveCustomerTask = barbershop::receiveNewCustomer;

        List <Future<Void>> barberFutures = new ArrayList<>();
        List<Future<Void>> customerFutures = new ArrayList<>();


        for (int i = 0; i < NUM_ITERATION; i++) {
            Future <Void> barberFuture = executor.submit(barbershopTask);
            barberFutures.add(barberFuture);

            Future <Void> customerFuture = executor.submit(receiveCustomerTask);
            customerFutures.add(customerFuture);
        }

        traverseFutures(barberFutures);
        traverseFutures(customerFutures);
    }

    public static void traverseFutures(List<Future<Void>> futures) {
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
