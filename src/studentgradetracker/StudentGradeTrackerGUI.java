package studentgradetracker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StudentGradeTrackerGUI {
    private ArrayList<Student> students = new ArrayList<>();
    private JFrame frame;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private DecimalFormat df = new DecimalFormat("0.00");
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new StudentGradeTrackerGUI().initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() {
        // Create main frame
        frame = new JFrame("Student Grade Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());

        createMenuBar();
        createToolBar();
        createTable();

        frame.setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Edit Menu (only keeping this one)
        JMenu editMenu = new JMenu("Edit");
        JMenuItem addItem = new JMenuItem("Add Student");
        addItem.addActionListener(e -> addStudent());
        JMenuItem editItem = new JMenuItem("Edit Student");
        editItem.addActionListener(e -> editStudent());
        JMenuItem deleteItem = new JMenuItem("Delete Student");
        deleteItem.addActionListener(e -> deleteStudent());
        editMenu.add(addItem);
        editMenu.add(editItem);
        editMenu.add(deleteItem);
        
        menuBar.add(editMenu);
        frame.setJMenuBar(menuBar);
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton addButton = new JButton("Add");
        addButton.setToolTipText("Add Student");
        addButton.addActionListener(e -> addStudent());
        
        JButton editButton = new JButton("Edit");
        editButton.setToolTipText("Edit Student");
        editButton.addActionListener(e -> editStudent());
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.setToolTipText("Delete Student");
        deleteButton.addActionListener(e -> deleteStudent());
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        
        frame.add(toolBar, BorderLayout.NORTH);
    }

    private void createTable() {
        String[] columnNames = {"ID", "Name", "Email", "Enrollment Date", "Grades", "Average", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void addStudent() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField dateField = new JTextField(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
        JTextField gradesField = new JTextField();
        
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Enrollment Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Grades (comma separated):"));
        panel.add(gradesField);
        
        int result = JOptionPane.showConfirmDialog(
            frame, panel, "Add New Student", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                String id = idField.getText();
                String email = emailField.getText();
                LocalDate enrollmentDate = LocalDate.parse(dateField.getText());
                
                String[] gradeStrings = gradesField.getText().split(",");
                ArrayList<Double> grades = new ArrayList<>();
                for (String gradeStr : gradeStrings) {
                    grades.add(Double.parseDouble(gradeStr.trim()));
                }
                
                Student student = new Student(name, id, email, enrollmentDate, grades);
                students.add(student);
                updateTable();
                
                JOptionPane.showMessageDialog(frame, 
                    "Student added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, 
                    "Invalid input: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame,
                "Please select a student to edit",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = students.get(selectedRow);

        // Create text fields with existing data
        JTextField nameField = new JTextField(student.getName());
        JTextField emailField = new JTextField(student.getEmail());
        JTextField dateField = new JTextField(student.getEnrollmentDate().toString());
        JTextField gradesField = new JTextField(student.getGradesAsString());

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Enrollment Date (YYYY-MM-DD):")); panel.add(dateField);
        panel.add(new JLabel("Grades (comma separated):")); panel.add(gradesField);

        int result = JOptionPane.showConfirmDialog(
            frame, panel, "Edit Student",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                student.name = nameField.getText();
                student.email = emailField.getText();
                student.enrollmentDate = LocalDate.parse(dateField.getText());

                // Parse grades
                String[] gradeStrings = gradesField.getText().split(",");
                ArrayList<Double> grades = new ArrayList<>();
                for (String gradeStr : gradeStrings) {
                    grades.add(Double.parseDouble(gradeStr.trim()));
                }
                student.grades = grades;

                updateTable();

                JOptionPane.showMessageDialog(frame,
                    "Student updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                    "Invalid input: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, 
                "Please select a student to delete", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            frame, 
            "Are you sure you want to delete this student?", 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            students.remove(selectedRow);
            updateTable();
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Student student : students) {
            Object[] row = {
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getEnrollmentDate(),
                student.getGradesAsString(),
                df.format(student.calculateAverage()),
                student.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private static class Student implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String name;
        private String id;
        private String email;
        private LocalDate enrollmentDate;
        private ArrayList<Double> grades;

        public Student(String name, String id, String email, LocalDate enrollmentDate, ArrayList<Double> grades) {
            this.name = name;
            this.id = id;
            this.email = email;
            this.enrollmentDate = enrollmentDate;
            this.grades = new ArrayList<>(grades);
        }

        public String getName() { return name; }
        public String getId() { return id; }
        public String getEmail() { return email; }
        public LocalDate getEnrollmentDate() { return enrollmentDate; }
        public String getGradesAsString() {
            return String.join(", ", grades.stream().map(Object::toString).toArray(String[]::new));
        }

        public double calculateAverage() {
            if (grades.isEmpty()) return 0;
            return grades.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        }

        public String getStatus() {
            double avg = calculateAverage();
            if (avg >= 90) return "Honors";
            if (avg >= 60) return "Passing";
            return "Failing";
        }
    }
}