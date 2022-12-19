package persistance;

import model.Subject;
import model.Weekday;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubjectManagerDAO {

    private final Connection connection;

    public SubjectManagerDAO(String DBName, String ip, int port) throws Exception {
        //port 5432 for postgresql
        //port 3306 for mysql
        Class.forName("org.postgresql.Driver");

        final String user = "guest";
        final String password = "123123";
        final String url = String.format("jdbc:postgresql://%s:%d/%s", ip, port, DBName);

        connection = DriverManager.getConnection(url, user, password);
    }

    public SubjectManagerDAO(String DBName, String ip, int port, String user, String password) throws Exception {
        Class.forName("org.postgresql.Driver");

        final String url = String.format("jdbc:postgresql://%s:%d/%s", ip, port, DBName);

        connection = DriverManager.getConnection(url, user, password);
    }

    public List<Weekday> getWeekdays() {
        PreparedStatement statement = null;
        List<Weekday> weekdays = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT id, name FROM Weekdays");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                weekdays.add(new Weekday(result.getInt("id"), result.getString("name")));
            }
            result.close();
        } catch (SQLException e) {
            System.out.println("Error during getting weekdays");
            System.out.println(" >> " + e.getMessage());
        } finally {
            return weekdays;
        }
    }

    public Weekday getWeekday(int id) {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement("SELECT name FROM Weekdays WHERE Weekdays.id = ?");
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            String name = result.getString("name");
            result.close();
            return new Weekday(id, name);
        } catch (SQLException e) {
            System.out.printf("Error getting weekday with id %d%n", id);
            System.out.println(" >> " + e.getMessage());
            return null;
        }
    }

    public List<Weekday> getWeekday(String name) {
        PreparedStatement statement;
        List<Weekday> weekdays = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT id, name FROM Weekdays WHERE Weekdays.name LIKE ?");
            statement.setString(1, "%" + name + "%");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                weekdays.add(new Weekday(result.getInt("id"), result.getString("name")));
            }
            result.close();
            return weekdays;
        } catch (SQLException e) {
            System.out.printf("Error getting weekday with name %s%n", name);
            System.out.println(" >> " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void showWeekdays() {
        System.out.println("Weekdays:");

        for (Weekday weekday : getWeekdays()) {
            System.out.println(weekday);
        }
    }

    public int addWeekday(Weekday weekday) {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("INSERT INTO  Weekdays(name) VALUES (?)");
            statement.setString(1, weekday.getName());

            statement.executeUpdate();
            System.out.printf("Weekday %s successfully added!%n", weekday);
            return getWeekdayId(weekday.getName());
        } catch (SQLException e) {
            System.out.printf("Weekday %s was not added!%n", weekday.toString());
            System.out.println(" >> " + e.getMessage());
            return -1;
        }
    }

    public boolean updateWeekday(String oldWeekday, Weekday newWeekday) throws Exception {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement("UPDATE Weekdays SET name=? WHERE name=?");

            statement.setString(1, newWeekday.getName());
            statement.setString(2, oldWeekday);

            statement.executeUpdate();
            System.out.printf("Weekday %s was successfully updated!%n", oldWeekday);
            return true;
        } catch (SQLException e) {
            System.out.printf("Weekday %s was not updated!%n", oldWeekday);
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    public boolean deleteWeekday(String name) {
        PreparedStatement deleteWeekdaysStatement = null, deleteSubjectsStatement = null;
        try {
            deleteWeekdaysStatement = connection.prepareStatement("DELETE FROM Weekdays WHERE Weekdays.name = ?");
            deleteWeekdaysStatement.setString(1, name);

            deleteSubjectsStatement = connection.prepareStatement("DELETE FROM Subjects WHERE Subjects.weekdayId = " +
                    "(SELECT Weekdays.id FROM Weekdays WHERE Weekdays.name = ?)");
            deleteSubjectsStatement.setString(1, name);

            deleteSubjectsStatement.executeUpdate();
            int result = deleteWeekdaysStatement.executeUpdate();

            if (result > 0) {
                System.out.printf("Weekday %s was successfully deleted%n", name);
                return true;
            } else {
                System.out.printf("There is no weekday with name %s%n", name);
                return false;
            }
        } catch (SQLException e) {
            System.out.printf("Error during deleting weekday with name %s%n", name);
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    public List<Subject> getSubjects() {
        PreparedStatement statement = null;
        List<Subject> subjects = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT Weekdays.id AS weekdayId, Weekdays.name AS weekdayName,  Subjects.id" +
                    " AS subjectId,  Subjects.name AS subjectName\n" +
                    "FROM Weekdays INNER JOIN Subjects on Weekdays.id = Subjects.weekdayId");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                subjects.add(new Subject(result.getInt("subjectId"),
                                result.getString("subjectName"),
                                new Weekday(result.getInt("weekdayId"),
                                        result.getString("weekdayName")
                                )
                        )
                );
            }
            result.close();

        } catch (SQLException e) {
            System.out.println("Error during getting subjects");
            System.out.println(" >> " + e.getMessage());
        } finally {
            return subjects;
        }
    }

    public void showSubjects() {
        System.out.println("Subjects:");

        for (Subject subject : getSubjects()) {
            System.out.println(subject);
        }
    }

    public Subject getSubject(int id) {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement("SELECT Weekdays.id AS weekdayId, Weekdays.name AS weekdayName,  Subjects.id" +
                    " AS subjectId,  Subjects.name AS subjectName FROM Weekdays INNER JOIN Subjects" +
                    " on Weekdays.id = Subjects.weekdayId WHERE Subjects.id = ?");
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            Subject subject = new Subject(result.getInt("subjectId"),
                    result.getString("subjectName"),
                    new Weekday(result.getInt("weekdayId"),
                            result.getString("weekdayName")
                    )
            );
            result.close();
            return subject;
        } catch (SQLException e) {
            System.out.printf("Error during getting subject with id %d%n", id);
            System.out.println(" >> " + e.getMessage());
            return null;
        }
    }

    public boolean addSubject(Subject subject) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO Subjects (name, weekdayId) VALUES (?, ?, ?)");
            statement.setString(1, subject.getName());
            statement.setInt(3, getWeekdayId(subject.getWeekday().getName()));

            statement.executeUpdate();
            System.out.printf("Subject %s successfully added!%n", subject);
            return true;
        } catch (SQLException e) {
            System.out.printf("Subject %s was not added!%n", subject.toString());
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    public boolean updateSubject(Subject oldSubject, Subject newSubject) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("UPDATE Subjects SET name=?, weekdayId=? WHERE name=?");

            statement.setString(1, newSubject.getName());
            statement.setInt(2, getWeekdayId(newSubject.getWeekday().getName()));
            statement.setString(3, oldSubject.getName());

            statement.executeUpdate();
            System.out.printf("Subject %s was successfully updated!%n", oldSubject);
            return true;
        } catch (SQLException e) {
            System.out.printf("Subject %s was not updated!%n", oldSubject.toString());
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSubject(String name) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("DELETE FROM Subjects WHERE Subjects.name = ?");

            statement.setString(1, name);

            int result = statement.executeUpdate();

            if (result > 0) {
                System.out.printf("Subjects %s was successfully deleted%n", name);
                return true;
            } else {
                System.out.printf("There is no subject with name %s%n", name);
                return false;
            }
        } catch (SQLException e) {
            System.out.printf("Error during deleting subject with name %s%n", name);
            System.out.println(" >> " + e.getMessage());
            return false;
        }
    }

    public int countSubjectsByWeekday(String weekdayName) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("SELECT COUNT(*) AS result \n" +
                    "FROM Subjects INNER JOIN Weekdays ON Subjects.weekdayId = Weekdays.id\n" +
                    "WHERE Weekdays.name = ?");

            statement.setString(1, weekdayName);

            ResultSet queryResult = statement.executeQuery();
            queryResult.next();
            int result = queryResult.getInt("result");

            queryResult.close();

            return result;
        } catch (SQLException e) {
            System.out.printf("Error during getting subjects with weekday %s%n", weekdayName);
            System.out.println(" >> " + e.getMessage());
            return -1;
        }
    }

    public List<Subject> getSubjectByName(String subjectName) throws SQLException {
        PreparedStatement statement = null;
        List<Subject> subjects = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT Weekdays.id AS weekdayId, Weekdays.name AS weekdayName,  Subjects.id" +
                    " AS subjectId,  Subjects.name AS subjectName\n" +
                    "FROM Weekdays INNER JOIN Subjects on Weekdays.id = Subjects.weekdayId WHERE Subjects.name=?");
            statement.setString(1, "%" + subjectName + "%");

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                subjects.add(new Subject(result.getInt("subjectId"),
                                result.getString("subjectName"),
                                new Weekday(result.getInt("weekdayId"),
                                        result.getString("weekdayName")
                                )
                        )
                );
            }
            result.close();

            return subjects;

        } catch (SQLException e) {
            System.out.printf("Error during getting subject by name %s%n", subjectName);
            System.out.println(" >> " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Subject> getSubjectsByWeekday(String weekdayName) {
        PreparedStatement statement = null;
        List<Subject> subjects = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT Weekdays.id AS weekdayId, Weekdays.name AS weekdayName,  Subjects.id" +
                    " AS subjectId,  Subjects.name AS subjectName\n" +
                    "FROM Weekdays INNER JOIN Subjects on Weekdays.id = Subjects.weekdayId " +
                    "WHERE Weekdays.name = ?");

            statement.setString(1, weekdayName);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                subjects.add(new Subject(result.getInt("subjectId"),
                                result.getString("subjectName"),
                                new Weekday(result.getInt("weekdayId"),
                                        result.getString("weekdayName")
                                )
                        )
                );
            }
            result.close();
            return subjects;
        } catch (SQLException e) {
            System.out.printf("Error during getting subjects by weekday %s%n", weekdayName);
            System.out.println(" >> " + e.getMessage());
            return null;
        }
    }

    private int getWeekdayId(String name) {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement("SELECT id FROM Weekdays WHERE Weekdays.name = ?");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            result.next();
            int id = result.getInt("id");
            result.close();
            return id;
        } catch (SQLException e) {
            System.out.printf("Error during getting weekday id for %s%n", name);
            System.out.println(" >> " + e.getMessage());
            return -1;
        }
    }

    public void endSession() throws SQLException {
        connection.close();
    }

}
