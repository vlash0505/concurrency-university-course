import controller.SubjectManager;
import model.Subject;
import model.Weekday;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, SAXException {
        String xml = "src/main/java/xml/subject_manager.xml";
        String xml_mod = "src/main/java/xml/output.xml";
        String xsd = "src/main/java/xml/mapSubjectManager.xsd";

        SubjectManager subjectManager = new SubjectManager(xml, xsd);

        List<Weekday> weekdays = new ArrayList<>(Arrays.asList(
                new Weekday(1,"Monday"),
                new Weekday(2,"Tuesday"),
                new Weekday(3,"Wednesday"),
                new Weekday(4,"Thursday")
        ));

        weekdays.forEach(subjectManager::addWeekday);

        subjectManager.printCurrentState("Initial state");

        subjectManager.addSubject(new Subject(5, "Subject5", new Weekday(1,"Monday")));
        subjectManager.addSubject(new Subject(6, "Subject6", new Weekday(2,"Tuesday")));

        subjectManager.printCurrentState("After adding subjects");

        subjectManager.deleteSubject(1);
        subjectManager.printCurrentState("After deleting subject");

        subjectManager.editSubject(2, new Subject(2, "Updated subject", new Weekday(2,"Tuesday")));
        subjectManager.printCurrentState("After editing subject");

        subjectManager.saveToFile(xml_mod);
    }
}