package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

    public Element getElement(Document document) {
        Element element = document.createElement("Subject");

        element.setAttribute("id", Integer.toString(id));
        element.setAttribute("name", name);

        return element;
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
