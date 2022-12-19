package RMI;

import model.Subject;
import model.Weekday;
import persistance.SubjectManagerDAO;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class Server extends UnicastRemoteObject implements SubjectManagerRemote {

    private final SubjectManagerDAO subjectManager;

    protected Server(SubjectManagerDAO subjectManager) throws Exception {
        this.subjectManager = subjectManager;
    }

    @Override
    public void addWeekday(Weekday weekday) throws RemoteException {
        synchronized (subjectManager){
            subjectManager.addWeekday(weekday);
        }
    }

    @Override
    public void deleteWeekday(String weekday) throws RemoteException {
        synchronized (subjectManager){
            subjectManager.deleteWeekday(weekday);
        }
    }

    @Override
    public void addSubject(Subject subject) throws RemoteException {
        synchronized (subjectManager){
            subjectManager.addSubject(subject);
        }
    }

    @Override
    public void deleteSubject(String name) throws RemoteException {
        synchronized (subjectManager){
            subjectManager.deleteSubject(name);
        }
    }

    @Override
    public void updateSubject(Subject old, Subject other) throws RemoteException {
        synchronized (subjectManager){
            subjectManager.updateSubject(old, other);
        }
    }

    @Override
    public int countSubjectsByWeekday(String name) throws RemoteException {
        synchronized (subjectManager){
            return subjectManager.countSubjectsByWeekday(name);
        }
    }

    @Override
    public Subject getSubjectByName(String name) throws RemoteException, SQLException {
        synchronized (subjectManager){
            return subjectManager.getSubjectByName(name).get(0);
        }
    }

    @Override
    public List<Subject> getSubjectsByWeekday(String name) throws RemoteException {
        synchronized (subjectManager){
            return subjectManager.getSubjectsByWeekday(name);
        }
    }

    @Override
    public List<Weekday> getWeekdays() throws RemoteException {
        synchronized (subjectManager){
            return subjectManager.getWeekdays();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            Registry r = LocateRegistry.createRegistry(1099);

            r.rebind("SubjectManagerServer", new Server(new SubjectManagerDAO("SubjectManager", "localhost", 5432)));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
