import port.Port;
import port.ShipRunnable;

import java.util.ArrayList;
import java.util.List;

public class EntryPoint {

    public static void main(String[] args) {
        List<ShipRunnable> ships = generateShips(100);

        Port port = new Port(ships);
        port.init();
    }

    public static List<ShipRunnable> generateShips(int amount) {
        List<ShipRunnable> result = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            result.add(new ShipRunnable());
        }
        return result;
    }

}
