package task2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientRMITask12 {

    public static void main(String[] args) {
        try {
            AirlineManagerRemote managerRemote = (AirlineManagerRemote) Naming.lookup("//localhost/AirlineManager");
            System.out.println(managerRemote.getAirlines());
            System.out.println(managerRemote.getSortedAirlines());
            System.out.println(managerRemote.getAirlinesByDestination("Berlin"));
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

}
