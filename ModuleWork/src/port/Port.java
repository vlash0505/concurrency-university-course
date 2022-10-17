package port;

import java.util.List;
import java.util.Random;

public class Port {

    private final List<ShipRunnable> ships;
    private final Terminal firstTerminal;
    private final Terminal secondTerminal;

    public Port(List<ShipRunnable> ships) {
        this.ships = ships;
        firstTerminal = new Terminal();
        secondTerminal = new Terminal();
    }

    public void init() {
        for (int i = 0; i < ships.size(); i++) {
            ships.get(i).setTask((i % 2) == 0 ? ShipTask.PASS : ShipTask.RECEIVE);
            ships.get(i).setTerminal(new Random().nextInt() % 2 == 0 ? firstTerminal : secondTerminal);
            new Thread(ships.get(i), "" + i + "th ship").start();
        }
    }
}
