package task2;

import controller.AirlineManager;
import model.Airline;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerRMITask12 extends UnicastRemoteObject implements AirlineManagerRemote {
    private final AirlineManager manager;

    public ServerRMITask12(List<Airline> airlines) throws RemoteException {
        manager = new AirlineManager(airlines);
    }

    @Override
    public List<Airline> getAirlines() throws RemoteException {
        return manager.getAirlines();
    }

    @Override
    public List<Airline> getSortedAirlines() throws RemoteException {
        return manager.getSortedAirlines();
    }

    @Override
    public List<Airline> getAirlinesByDestination(String destination) throws RemoteException {
        return manager.getAirlinesByDestination(destination);
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("AirlineManager", new ServerRMITask12(
                    List.of(
                            new Airline("Berlin", "Standard", "Monday"),
                            new Airline("Berlin", "Standard", "Monday"),
                            new Airline("Stockholm", "Extended", "Tuesday"),
                            new Airline("Helsinki", "Extended", "Tuesday"),
                            new Airline("Kyiv", "Economy", "Saturday")
                    )
            ));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
