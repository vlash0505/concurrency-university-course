package RMI;

import model.Subject;
import model.Weekday;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    public static void main(String[] args) throws RemoteException {
        if (args.length != 1) {
            System.out.println("Usage: <SERVER IP>");
            System.exit(1);
        }
        SubjectManagerRemote subjectManager = null;
        try {
            subjectManager = (SubjectManagerRemote) Naming.lookup("//" + args[0] + "/SubjectManagerServer");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        assert subjectManager != null;
        for(var weekday : subjectManager.getWeekdays()) {
            System.out.println(weekday);

            for(var subject : subjectManager.getSubjectsByWeekday(weekday.getName())) {
                System.out.println(subject);
            }
        }

        subjectManager.addWeekday(new Weekday("Monday"));
        subjectManager.addSubject(new Subject("Quantum mechanics", "Monday"));

        subjectManager.updateSubject(new Subject("Quantum mechanics"), new Subject("Physics", "Monday"));

        subjectManager.deleteWeekday("Monday");
        System.out.println("\n");

        for(var weekday : subjectManager.getWeekdays()) {
            System.out.println(weekday);

            for(var subject : subjectManager.getSubjectsByWeekday(weekday.getName())) {
                System.out.println(subject);
            }
        }
    }

}
