import com.formdev.flatlaf.FlatLightLaf;  // FlatLaf Import
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementGUI extends JFrame {
    private final StudentManager manager;
    private final JTable studentTable;
    private final DefaultTableModel tableModel;

    public StudentManagementGUI() {
        manager = new StudentManager();
        setTitle("Student Management System");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table Setup
        String[] columns = {"ID", "Name", "Average", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(30);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            studentTable.getColumnModel().getColumn(i).setPreferredWidth(200);
        }

        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // Buttons Panel (Placed at the top)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10)); // Horizontal layout for buttons
        JButton addButton = new JButton("Add Student");
        JButton viewButton = new JButton("View Student Details");
        JButton deleteButton = new JButton("Delete Student");
        JButton editButton = new JButton("Edit Student Name");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        add(buttonPanel, BorderLayout.NORTH);  // Place buttons at the top

        // Add Button Action
        addButton.addActionListener(e -> addStudent());

        // View Button Action (Show Details in New Window)
        viewButton.addActionListener(e -> viewStudentDetails());

        // Delete Button Action
        deleteButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "Enter Student ID to delete:");
            if (id != null && !id.trim().isEmpty()) {
                boolean removed = manager.removeStudent(id.trim());
                if (removed) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Student deleted.");
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID not found.");
                }
            }
        });

        // Edit Button Action
        editButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "Enter Student ID to edit name:");
            if (id != null && !id.trim().isEmpty()) {
                Student s = manager.findById(id.trim());
                if (s != null) {
                    String newName = JOptionPane.showInputDialog(this, "Enter new name:");
                    if (newName != null && newName.matches("[a-zA-Z ]+")) {
                        s.setName(newName.trim());
                        refreshTable();
                        JOptionPane.showMessageDialog(this, "Name updated.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid name.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID not found.");
                }
            }
        });

        setVisible(true);
    }

    private void addStudent() {
        String name = JOptionPane.showInputDialog(this, "Enter student name:");
        if (name == null || !name.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this, "Invalid name.");
            return;
        }

        Student student = new Student(name);
        try {
            int count = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter number of subjects:"));
            for (int i = 1; i <= count; i++) {
                String subject = JOptionPane.showInputDialog(this, "Enter subject " + i + " name:");
                double grade = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter grade for " + subject + ":"));
                student.addSubject(subject, grade);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
            return;
        }

        student.calculateAverage();
        manager.addStudent(student);
        refreshTable();
    }

    private void viewStudentDetails() {
        String id = JOptionPane.showInputDialog(this, "Enter Student ID:");
        Student s = manager.findById(id);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Student not found.");
            return;
        }

        // Create a new JDialog for displaying student details
        JDialog studentDetailsDialog = new JDialog(this, "Student Details", true);
        studentDetailsDialog.setLayout(new BorderLayout());
        studentDetailsDialog.setSize(400, 400);

        JTextArea detailsTextArea = new JTextArea();
        detailsTextArea.setEditable(false);
        detailsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        detailsTextArea.setText(formatStudentDetails(s));
        
        studentDetailsDialog.add(new JScrollPane(detailsTextArea), BorderLayout.CENTER);
        studentDetailsDialog.setVisible(true);
    }

    private String formatStudentDetails(Student s) {
        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(s.getName()).append("\n");
        details.append("ID: ").append(s.getId()).append("\n");
        details.append("Average: ").append(String.format("%.2f", s.getAverage())).append("\n");
        details.append("Status: ").append(s.isPassed() ? "Passed" : "Failed").append("\n");
        details.append("Rating: ").append(s.getRating()).append("\n\n");
        details.append("Subjects:\n");
        s.getSubjects().forEach((sub, grd) ->
                details.append("- ").append(sub).append(": ").append(grd).append("\n")
        );

        List<String> failedSubjects = new ArrayList<>();
        s.getSubjects().forEach((sub, grd) -> {
            if (grd < 50) failedSubjects.add(sub);
        });

        if (!failedSubjects.isEmpty()) {
            details.append("\nFailed Subjects:\n");
            failedSubjects.forEach(f -> details.append("- ").append(f).append("\n"));
        } else {
            details.append("\nFailed Subjects: None");
        }

        return details.toString();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student s : manager.getStudents()) {
            s.calculateAverage();
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getAverage(), s.isPassed() ? "Passed" : "Failed"});
        }
    }

    public static void main(String[] args) {
        try {
            // Apply FlatLaf theme
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(StudentManagementGUI::new);
    }
}
