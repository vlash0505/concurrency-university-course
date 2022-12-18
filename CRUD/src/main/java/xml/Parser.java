package xml;

import model.Subject;
import model.Weekday;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final String SUBJECT_MANAGER = "SubjectManager";

    private final Document doc;
    private Element root;

    public Parser(String filename, String mapFilename) throws IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        this.root = null;

        initializeValidation(mapFilename, dbf);
        this.doc = createDocument(filename, dbf);
        initializeRoot();
    }

    public List<Weekday> getWeekdays() {
        List<Weekday> weekdays = new ArrayList<>();
        NodeList weekdayList = getWeekdaysList();

        if (weekdayList != null) {
            for (int i = 0, n = weekdayList.getLength(); i < n; i++) {
                Element weekdayElement = (Element) weekdayList.item(i);
                weekdays.add(new Weekday(weekdayElement));
            }
        }

        return weekdays;
    }

    public List<Subject> getSubjects() {
        List<Subject> subjects = new ArrayList<>();
        NodeList weekdaysList = getWeekdaysList();

        if (weekdaysList != null) {
            for (int i = 0, n = weekdaysList.getLength(); i < n; i++) {
                Element weekdayElement = (Element) weekdaysList.item(i);
                NodeList subjectsList = weekdayElement.getElementsByTagName("Subject");

                for (int j = 0, m = subjectsList.getLength(); j < m; j++) {
                    Element subjectElement = (Element) subjectsList.item(j);
                    subjects.add(new Subject(subjectElement, weekdayElement));
                }
            }
        }
        return subjects;
    }

    public void saveToFile(List<Weekday> weekdays, List<Subject> subjects, String filename) {
        Document document = null;

        try {
            document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        assert document != null;
        Element newRoot = document.createElement(SUBJECT_MANAGER);
        document.appendChild(newRoot);

        for (Weekday weekday : weekdays) {
            Element weekdayElement = weekday.getElement(document);
            newRoot.appendChild(weekdayElement);

            for (Subject subject : subjects) {
                if (subject.getWeekday().equals(weekday)) {
                    weekdayElement.appendChild(subject.getElement(document));
                }
            }
        }

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer;

        try {
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "WINDOWS-1251");
            transformer.transform(new DOMSource(document), new StreamResult(new File(filename)));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private NodeList getWeekdaysList() {
        if (root == null)
            return null;

        Element videoStore;

        if (root.getTagName().equals(SUBJECT_MANAGER)) {
            videoStore = root;
        } else {
            videoStore = (Element) root.getElementsByTagName(SUBJECT_MANAGER).item(0);
        }

        return videoStore.getElementsByTagName("Weekday");
    }

    private void initializeValidation(String mapFilename, DocumentBuilderFactory dbf) {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema s = null;

        try {
            s = sf.newSchema(new File(mapFilename));
        } catch (SAXException e) {
            e.printStackTrace();
        }

        dbf.setValidating(false);
        dbf.setSchema(s);
    }

    private Document createDocument(String filename, DocumentBuilderFactory dbf) throws IOException, SAXException {
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        assert db != null;
        db.setErrorHandler(new ParseErrorHandler());
        Document document;
        document = db.parse(new File(filename));

        return document;
    }

    private void initializeRoot() {
        root = doc.getDocumentElement();
    }
}
