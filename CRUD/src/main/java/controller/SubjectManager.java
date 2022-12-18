package controller;

import model.Subject;
import model.Weekday;
import org.xml.sax.SAXException;
import xml.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubjectManager {
    private List<Weekday> weekdays;
    private List<Subject> subjects;

    private Parser parser;

    public SubjectManager(String filename, String mapFilename) throws IOException, SAXException {
        this.parser = new Parser(filename, mapFilename);
        this.weekdays = new ArrayList<>();
        this.subjects = new ArrayList<>();

        loadFromFile();
    }

    public void changeFile(String filename, String mapFilename) throws IOException, SAXException {
        parser = new Parser(filename, mapFilename);
    }

    public void saveToFile(String filename) {
        parser.saveToFile(weekdays, subjects, filename);
    }

    public void loadFromFile() {
        weekdays = parser.getWeekdays();
        subjects = parser.getSubjects();
    }

    public int countWeekdays() {
        return weekdays.size();
    }

    public Weekday getWeekday(int id) {
        return weekdays.stream().filter(element -> element.getId() == id).findFirst().orElse(null);
    }

    public void addWeekday(Weekday weekday) {
        weekdays.add(weekday);
    }

    public void deleteWeekday(int id) {
        Weekday weekday = getWeekday(id);
        if (weekday != null) {
            weekdays.remove(weekday);
        }
    }

    public void editWeekday(int id, Weekday newWeekday) {
        Weekday weekday = getWeekday(id);
        if (weekday != null) {
            weekdays.remove(weekday);
            addWeekday(newWeekday);
        }
    }

    public int countSubjects() {
        return subjects.size();
    }

    public Subject getSubject(int id) {
        return subjects.stream().filter(subject -> subject.getId() == id).findFirst().orElse(null);
    }

    public void addSubject(Subject subject) {
        if (weekdays.contains(subject.getWeekday())) {
            subjects.add(subject);
        }
    }

    public void deleteSubject(int id) {
        Subject subject = getSubject(id);
        if (subject != null) {
            subjects.remove(subject);
        }
    }

    public void editSubject(int id, Subject newSubject) {
        Subject subject = getSubject(id);
        if (subject != null) {
            subjects.remove(subject);
            addSubject(newSubject);
        }
    }

    public void printCurrentState(String header) {
        System.out.println(header + ":");
        System.out.println("Subjects:");
        for (Subject subject : subjects) {
            System.out.println(subject);
        }

        System.out.println("Weekdays:");
        for (Weekday weekday : weekdays) {
            System.out.println(weekday);
        }
        System.out.println();
    }
}
