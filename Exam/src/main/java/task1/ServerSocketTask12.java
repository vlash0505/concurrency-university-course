package task1;

import controller.AirlineManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerSocketTask12 {
    private final Executor executor;
    private final AirlineManager manager;

    private ServerSocket serverSocket;

    public ServerSocketTask12(int port, int size) {
        executor = Executors.newSingleThreadExecutor();
        manager = new AirlineManager(size);
        try {
            serverSocket = new ServerSocket(port, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                var client = serverSocket.accept();
                System.out.println(client + " connected!");
                executor.execute(new ClientRunnable(manager,client));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static class ClientRunnable implements Runnable{
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private final AirlineManager manager;
        private final Socket client;

        public ClientRunnable(AirlineManager manager, Socket client) {
            this.manager = manager;
            this.client = client;
            try {
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void getAirlines() throws IOException {
            var airlines = manager.getAirlines();
            out.writeObject(Objects.requireNonNullElseGet(airlines, ArrayList::new));
        }

        private void getSortedAirlines() throws IOException {
            var airlines = manager.getSortedAirlines();
            out.writeObject(Objects.requireNonNullElseGet(airlines, ArrayList::new));
        }

        private void getAirlinesByDestination() throws IOException, ClassNotFoundException {
            String destination = (String) in.readObject();
            var airlines = manager.getAirlinesByDestination(destination);
            out.writeObject(Objects.requireNonNullElseGet(airlines, ArrayList::new));
        }

        @Override
        public void run() {
            while (!Thread.interrupted()){
                try {
                    if(client.isClosed() || !client.isConnected()) break;
                    String code = (String)in.readObject();
                    System.out.println(">" + code);
                    switch (code) {
                        case "get airlines" -> getAirlines();
                        case "get sorted airlines" -> getSortedAirlines();
                        case "get airlines by destination" -> getAirlinesByDestination();
                        default -> out.writeObject(new ArrayList<>());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println(client + " disconnected!");
                    return;
                }
            }
        }
    }

}
