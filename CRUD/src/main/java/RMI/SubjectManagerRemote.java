package RMI;

import model.Subject;
import model.Weekday;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface SubjectManagerRemote extends Remote {

    void addWeekday(Weekday weekday) throws RemoteException;

    void deleteWeekday(String name) throws RemoteException;

    void addSubject(Subject subject) throws RemoteException;

    void deleteSubject(String name) throws RemoteException;

    void updateSubject(Subject old, Subject other) throws RemoteException;

    int countSubjectsByWeekday(String name) throws RemoteException;

    Subject getSubjectByName(String name) throws RemoteException, SQLException;

    List<Subject> getSubjectsByWeekday(String name) throws RemoteException;

    List<Weekday> getWeekdays() throws RemoteException;

}
