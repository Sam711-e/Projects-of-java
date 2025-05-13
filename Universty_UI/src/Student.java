import java.util.*;

public class Student {
    private static final Set<String> generatedIds = new HashSet<>();
    private final String id;
    private String name;
    private final Map<String, Double> subjects = new LinkedHashMap<>();
    private double average;

    public Student(String name) {
        setName(name);
        this.id = generateUniqueId();
    }

    private String generateUniqueId() {
        Random rand = new Random();
        String newId;
        do {
            Set<Integer> digits = new LinkedHashSet<>();
            while (digits.size() < 5) {
                digits.add(rand.nextInt(10));
            }
            StringBuilder sb = new StringBuilder("2025");
            for (int d : digits) sb.append(d);
            newId = sb.toString();
        } while (generatedIds.contains(newId));
        generatedIds.add(newId);
        return newId;
    }

    public void setName(String name) {
        if (name == null || !name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Invalid student name. Letters and spaces only.");
        }
        this.name = name;
    }

    public void addSubject(String subject, double grade) {
        if (subject == null || subject.trim().isEmpty())
            throw new IllegalArgumentException("Subject name cannot be empty.");
        if (grade < 0 || grade > 100)
            throw new IllegalArgumentException("Grade must be between 0 and 100.");
        subjects.put(subject, grade);
    }

    public void calculateAverage() {
        if (subjects.isEmpty()) {
            average = 0.0;
        } else {
            average = subjects.values().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
        }
    }

    public String getRating() {
        if (average >= 90) return "Excellent";
        if (average >= 80) return "Very Good";
        if (average >= 70) return "Good";
        if (average >= 60) return "Fair";
        if (average >= 50) return "Pass";
        return "Fail";
    }

    public boolean isPassed() {
        return average >= 50;
    }

    public void clearSubjects() {
        subjects.clear();
        average = 0;
    }

    // Getter methods
    public String getId() { return id; }
    public String getName() { return name; }
    public double getAverage() { return average; }
    public Map<String, Double> getSubjects() { return Collections.unmodifiableMap(subjects); }
}
