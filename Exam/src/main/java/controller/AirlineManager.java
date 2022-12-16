package controller;

import model.Airline;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AirlineManager {
    private final List<Airline> airlines;

    public AirlineManager(List<Airline> airlines) {
        this.airlines = airlines;
    }

    public synchronized void addAirline(Airline airline){
        airlines.add(airline);
    }

    public synchronized List<Airline> getAirlines() {
        return airlines;
    }

    public synchronized List<Airline> getSortedAirlines() {
        List<Airline> result = new ArrayList<>(airlines);
        result.sort(Comparator.comparing(Airline::getType));
        return result;
    }

    public synchronized List<Airline> getAirlinesByDestination(String destination) {
        List<Airline> result = new ArrayList<>();
        airlines.forEach(airline -> {
            if (Objects.equals(airline.getDestination(), destination))
                result.add(airline);
        });
        return result;
    }

}
