package socket;

import model.Subject;
import model.Weekday;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private final DataOutputStream out;
    private final DataInputStream in;

    public Client(String ip, int port) throws IOException {
        socket = null;

        while (true) {
            try {
                System.out.println("Connecting to server...");
                socket = new Socket(ip, port);
                System.out.println("Connected");
                break;
            } catch (IOException e) {
                int timeout = 5000;
                System.out.printf("Failed. Waiting %d ms...%n", timeout);
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public boolean addWeekday(Weekday weekday) throws IOException {
        return sendQuery(Query.ADD_WEEKDAY, List.of(weekday.getName()));
    }

    public boolean deleteWeekday(Weekday weekday) throws IOException {
        return sendQuery(Query.DELETE_WEEKDAY, List.of(weekday.getName()));
    }

    public boolean addSubject(Subject subject) throws IOException {
        return sendQuery(Query.ADD_SUBJECT, subject.toList());
    }

    public boolean deleteSubject(Subject subject) throws IOException {
        return sendQuery(Query.DELETE_SUBJECT, subject.toList());
    }

    public boolean updateSubject(Subject oldSubject, Subject newSubject) throws IOException {
        List<String> arguments = oldSubject.toList();
        arguments.addAll(newSubject.toList());

        return sendQuery(Query.UPDATE_SUBJECT, arguments);
    }

    public int countSubjectsByWeekday(Weekday weekday) throws IOException {
        if (sendQuery(Query.COUNT_SUBJECTS_BY_WEEKDAY, List.of(weekday.getName()))) {
            return in.readInt();
        }

        throw new RuntimeException("Error while counting subjects");
    }

    public Subject getSubjectByName(Subject subject) throws IOException {
        if (sendQuery(Query.GET_SUBJECT_BY_NAME, subject.toList())) {
            return Subject.parseSubject(in);
        }

        throw new RuntimeException("Error while getting subject");
    }

    public List<Subject> getSubjectsByWeekday(Weekday weekday) throws IOException {
        if (sendQuery(Query.GET_SUBJECTS_BY_WEEKDAY, weekday.toList())) {
            int count = in.readInt();

            List<Subject> subjects = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                subjects.add(Subject.parseSubject(in));
            }

            return subjects;
        }

        throw new RuntimeException("Error while getting subjects by weekday");
    }

    public List<Weekday> getWeekdays() throws IOException {
        if (sendQuery(Query.GET_WEEKDAYS, List.of())) {

            int count = in.readInt();

            List<Weekday> weekdays = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                weekdays.add(Weekday.parseWeekday(in));
            }

            return weekdays;
        }

        throw new RuntimeException("Error while getting weekdays");
    }

    public void disconnect() throws IOException {
        System.out.println("Disconnected from server");
        socket.close();
    }

    private boolean sendQuery(Query query, List<String> arguments) throws IOException {
        out.writeUTF(query.name());

        for (String argument : arguments) {
            out.writeUTF(argument);
        }

        return in.readInt() == 0;
    }

    public static void main(String[] args) throws IOException {
        Client client = null;
        try {
            client = new Client("localhost", 2710);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        String weekdayName;
        String subjectName;

        while (true) {
            assert client != null;
            for (Weekday weekday : client.getWeekdays()) {
                System.out.println(weekday);

                for(Subject subject : client.getSubjectsByWeekday(weekday)) {
                    System.out.println(subject);
                }
            }
            System.out.print("Enter a weekday: ");
            weekdayName = sc.nextLine();

            if (Objects.equals(weekdayName, "stop")) {
                break;
            }

            client.addWeekday(new Weekday(weekdayName));

            for (Weekday weekday : client.getWeekdays()) {
                System.out.println(weekday);
            }

            System.out.print("Enter a subject's name: ");
            subjectName = sc.nextLine();

            if (Objects.equals(subjectName, "stop")) {
                break;
            }

            System.out.print("Enter a subject's weekday: ");
            weekdayName = sc.nextLine();

            client.addSubject(new Subject(subjectName, weekdayName));
        }

        try {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
