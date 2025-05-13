import java.util.*;

public class StudentManager {
    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(Objects.requireNonNull(student));
    }

    public boolean removeStudent(String id) {
        return students.removeIf(s -> s.getId().equals(id));
    }

    public Student findById(String id) {
        for (Student s : students) if (s.getId().equals(id)) return s;
        return null;
    }

    public List<Student> findByName(String name) {
        List<Student> list = new ArrayList<>();
        for (Student s : students) if (s.getName().equalsIgnoreCase(name)) list.add(s);
        return list;
    }

    public void printAllReports() {
        if (students.isEmpty()) {
            System.out.println("No students to display.");
        } else {
            for (Student s : students) printStudentReport(s);
        }
    }

    public void printStudentReport(Student s) {
        s.calculateAverage();
        System.out.println("\n+------------------------------+");
        System.out.println("|        Student Report       |");
        System.out.println("+------------------------------+");
        System.out.println("Name     : " + s.getName());
        System.out.println("ID       : " + s.getId());
        System.out.printf("Average  : %.1f\n", s.getAverage());
        System.out.println("Status   : " + (s.isPassed() ? "Passed" : "Failed"));
        System.out.println("Rating   : " + s.getRating());
        System.out.println("+------------------------------+");
    }

    public List<Student> getStudents() {
        return students;
    }
}
