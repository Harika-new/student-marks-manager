package main.java;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
// Custom Exception
class InvalidMarksException extends Exception {
    public InvalidMarksException(String message) {
        super(message);
    }
}
public class StudentMarksManagerFile {
    static Map<String, Integer> studentMap = new HashMap<>();
    static final String FILE_NAME = "students.txt";
    public static void main(String[] args) {
        loadFromFile();   // Load all data at start
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int choice;
            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Enter number only (1,2,3)");
                sc.nextLine();
                continue;
            }
            sc.nextLine();

            switch (choice) {
                case 1:
                    addStudent(sc);
                    break;
                case 2:
                    showStudents();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    static void addStudent(Scanner sc) {
        try {
            System.out.print("Enter student name: ");
            String name = sc.nextLine();
            System.out.print("Enter marks: ");
            int marks = sc.nextInt();
            sc.nextLine();
            validateMarks(marks);
            studentMap.put(name, marks);
            saveToFile();
            System.out.println("Student added and saved!");

        } catch (InvalidMarksException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input.");
            sc.nextLine();
        }
    }
    static void validateMarks(int marks) throws InvalidMarksException {
        if (marks < 0 || marks > 100) {
            throw new InvalidMarksException("Marks must be between 0 and 100");
        }
    }
    static void showStudents() {
        if (studentMap.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\nStudent List:");
        for (Map.Entry<String, Integer> entry : studentMap.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
    // Save Map data to file
    static void saveToFile() {
        try (FileWriter fw = new FileWriter(FILE_NAME)) {
            for (Map.Entry<String, Integer> entry : studentMap.entrySet()) {
                fw.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    // Load data from file at program start
    static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 3) {   // ID,Name,Marks
                    String name = parts[1];
                    int marks = Integer.parseInt(parts[2]);
                    studentMap.put(name, marks);
                }
                else if (parts.length == 2) { // Name,Marks
                    studentMap.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file.");
        }
    }
}
