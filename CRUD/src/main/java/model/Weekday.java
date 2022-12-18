package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Weekday implements Serializable {

    private final int id;
    private final String name;

    public Weekday(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Weekday(Element element) {
        this.id = Integer.parseInt(element.getAttribute("id"));
        this.name = element.getAttribute("name");
    }

    public Weekday(List<String> arguments) {
        this.id = Integer.parseInt(arguments.get(0));
        this.name = arguments.get(1);
    }

    public List<String> toList() {
        return new ArrayList<>(List.of(Integer.toString(id), name));
    }

    public static int listSize() {
        return 2;
    }

    public Element getElement(Document document) {
        Element element = document.createElement("WeekDay");

        element.setAttribute("id", Integer.toString(id));
        element.setAttribute("name", name);

        return element;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weekday weekday = (Weekday) o;
        return id == weekday.id && Objects.equals(name, weekday.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Weekday{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
