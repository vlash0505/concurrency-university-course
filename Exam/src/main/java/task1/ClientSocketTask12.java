package task1;

import model.Airline;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientSocketTask12 {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientSocketTask12(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Airline> getAirlines() {
        try {
            out.writeObject("get airlines");
            return (List<Airline>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Airline> getSortedAirlines() {
        try {
            out.writeObject("get sorted airlines");
            return (List<Airline>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Airline> getAirlinesByDestination(String destination) {
        try {
            out.writeObject("get airlines by destination");
            out.writeObject(destination);
            return (List<Airline>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        ClientSocketTask12 client = new ClientSocketTask12("localhost", 25565);
        System.out.println(client.getAirlines());
        System.out.println(client.getSortedAirlines());
        System.out.println(client.getAirlinesByDestination("Berlin"));
    }
}
