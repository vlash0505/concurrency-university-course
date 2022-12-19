package socket;

import model.Subject;
import model.Weekday;
import persistance.SubjectManagerDAO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private final SubjectManagerDAO subjectManager;

    private ServerSocket server;
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public Server(int port, SubjectManagerDAO subjectManager) {
        this.server = null;
        this.out = null;
        this.in = null;
        this.subjectManager = subjectManager;

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        while (true) {
            acceptClient();

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            while (processQuery()) ;
        }
    }

    private boolean processQuery() throws IOException {
        try {
            Query query = Query.valueOf(in.readUTF());

            switch (query) {
                case ADD_WEEKDAY -> subjectManager.addWeekday(new Weekday(in.readUTF()));
                case DELETE_WEEKDAY -> subjectManager.deleteWeekday(in.readUTF());
                case ADD_SUBJECT -> addSubject();
                case DELETE_SUBJECT -> deleteSubject();
                case UPDATE_SUBJECT -> updateSubject();
                case COUNT_SUBJECTS_BY_WEEKDAY -> countSubjectsByWeekday();
                case GET_SUBJECT_BY_NAME -> getSubjectByName();
                case GET_SUBJECTS_BY_WEEKDAY -> getSubjectsByWeekday();
                case GET_WEEKDAYS -> getWeekdays();
                default -> throw new IllegalStateException("Unexpected value: " + query);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            out.writeInt(-1);
            return false;
        }
    }

    private void getWeekdays() throws IOException {
        List<Weekday> weekdays = subjectManager.getWeekdays();

        out.writeInt(0);
        out.writeInt(weekdays.size());

        for (Weekday weekday : weekdays) {
            for (String argument : weekday.toList()) {
                out.writeUTF(argument);
            }
        }
    }

    private void getSubjectsByWeekday() throws IOException {
        List<Subject> subjects = subjectManager.getSubjectsByWeekday(Weekday.parseWeekday(in).getName());

        out.writeInt(0);
        out.writeInt(subjects.size());

        for (Subject subject : subjects) {
            for (String argument : subject.toList()) {
                out.writeUTF(argument);
            }
        }
    }

    private void getSubjectByName() throws IOException, SQLException {
        List<String> list = new ArrayList<>();

        out.writeInt(0);

        for (int i = 0, n = Subject.listSize(); i < n; i++) {
            list.add(in.readUTF());
        }

        List<String> subjectsList = subjectManager.getSubjectByName(new Subject(list).getName()).get(0).toList();

        for (int i = 0, n = Subject.listSize(); i < n; i++) {
            out.writeUTF(subjectsList.get(i));
        }
    }

    private void countSubjectsByWeekday() throws IOException {
        out.writeInt(0);
        out.writeInt(subjectManager.countSubjectsByWeekday(Weekday.parseWeekday(in).getName()));
    }

    private void updateSubject() throws Exception {
        out.writeInt(0);
        subjectManager.updateSubject(Subject.parseSubject(in), Subject.parseSubject(in));
    }

    private void deleteSubject() throws IOException {
        out.writeInt(0);
        subjectManager.deleteSubject(Subject.parseSubject(in).getName());
    }

    private void addSubject() throws IOException {
        out.writeInt(0);
        subjectManager.addSubject(Subject.parseSubject(in));
    }

    private void acceptClient() {
        clientSocket = null;
        int attempts = 5;

        while (attempts > 0) {
            try {
                System.out.println("Waiting for a client...");
                clientSocket = server.accept();
                System.out.println("Client connected");
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
            --attempts;
        }
    }

    public static void main(String[] args) throws Exception {
        SubjectManagerDAO videoStore = new SubjectManagerDAO("SubjectManager", "localhost", 5432);
        Server server = new Server(2710, videoStore);

        server.start();
        videoStore.endSession();
    }

}
