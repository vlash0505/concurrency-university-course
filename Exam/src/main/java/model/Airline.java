package model;

public class Airline {

    private int id;
    private static int currentId = 0;
    private String destination;
    private String type;
    private String weekday;

    public Airline(String destination, String type, String weekday) {
        this.destination = destination;
        this.type = type;
        this.weekday = weekday;
    }

    public Airline() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(int currentId) {
        Airline.currentId = currentId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    @Override
    public String toString() {
        return "model.Airline{" +
                "id=" + id +
                ", destination='" + destination + '\'' +
                ", type='" + type + '\'' +
                ", weekday='" + weekday + '\'' +
                '}';
    }
}
