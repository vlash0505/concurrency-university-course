package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Subject {
    private final int id;
    private final String name;
    private final Weekday weekday;

    public Subject(int id, String name, Weekday weekday) {
        this.id = id;
        this.name = name;
        this.weekday = weekday;
    }

    public Subject(Element subjectElement, Element weekdayElement) {
        this.id = Integer.parseInt(subjectElement.getAttribute("id"));
        this.name = subjectElement.getAttribute("name");
        this.weekday = new Weekday(weekdayElement);
    }

    public Subject(String name, String weekdayName) {
        this.id = -1;
        this.name = name;
        this.weekday = new Weekday(weekdayName);
    }

    public Subject(List<String> arguments) {
        this.id = Integer.parseInt(arguments.get(0));
        this.name = arguments.get(1);
        this.weekday = new Weekday(arguments.subList(2, 4));
    }

    public static int listSize() {
        return 2;
    }

    public static Subject parseSubject(DataInputStream in) throws IOException {
        List<String> list = new ArrayList<>();

        for (int i = 0, n = Subject.listSize(); i < n; i++) {
            list.add(in.readUTF());
        }

        return new Subject(list);
    }

    public List<String> toList() {
        List<String> subjects = new ArrayList<>(List.of(Integer.toString(id), name));
        subjects.addAll(weekday.toList());

        return subjects;
    }

    public Element getElement(Document document) {
        Element element = document.createElement("Subject");

        element.setAttribute("id", Integer.toString(id));
        element.setAttribute("name", name);

        return element;
    }

    public String getName() {
        return name;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weekday=" + weekday +
                '}';
    }
}
