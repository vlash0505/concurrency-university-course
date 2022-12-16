package task2;

import model.Airline;

import java.rmi.RemoteException;
import java.util.List;

public interface AirlineManagerRemote {

    List<Airline> getAirlines() throws RemoteException;
    List<Airline> getSortedAirlines() throws RemoteException;
    List<Airline> getAirlinesByDestination(String destination) throws RemoteException;

}
