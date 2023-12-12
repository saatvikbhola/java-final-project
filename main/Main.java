package main;

import model.Course;
import model.Student;
import database.DatabaseManager;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Course[] courses = new Course[10];
    private static final Student[] students = new Student[10];
    private static final String ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {
        initializeCourses();
        DatabaseManager.initializeDatabase();
        int userOption;

        do {
            System.out.println("Main Menu\n");
            displayMainMenu();
            userOption = getUserOption();

            switch (userOption) {
                case 1:
                    handleAdministration();
                    break;
                case 2:
                    handleStudent();
                    break;
                case 3:
                    enrollNewStudent();
                    break;
                case 4:
                    System.out.println("Exiting the system. Goodbye!\n");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
            System.out.println("------------------------------------------------------");
        } while (userOption != 4);

        DatabaseManager.closeDatabase();
    }

    private static void initializeCourses() {
        courses[0] = new Course("Mathematics");
        courses[1] = new Course("Computer Science");
        courses[2] = new Course("Physics");
    }

    private static void displayMainMenu() {
        System.out.println("1. Administration");
        System.out.println("2. Student");
        System.out.println("3. Enroll New Student");
        System.out.println("4. Exit");
        System.out.print("Enter your option: ");
    }

    private static int getUserOption() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();
            return -1;
        }
    }

    private static void handleAdministration() {
        System.out.println(" ");
        System.out.print("Enter the administration password: ");
        scanner.nextLine(); // Consume the newline character
        String enteredPassword = scanner.nextLine();

        if (enteredPassword.equals(ADMIN_PASSWORD)) {
            System.out.println("Authentication successful. Welcome, Administrator!\n");
            int adminChoice;

            do {
                displayAdminMenu();
                adminChoice = getUserOption();

                switch (adminChoice) {
                    case 1:
                        displayAllStudentDetails();
                        break;
                    case 2:
                        displayEnrolledCoursesForAllStudents();
                        break;
                    case 3:
                        System.out.println("Exiting admin menu. Returning to main menu.\n");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.\n");
                }
            } while (adminChoice != 3);
        } else {
            System.out.println("Authentication failed. Incorrect password.");
        }
    }

    private static void handleStudent() {
        System.out.print("Enter your student ID (or 0 to enroll as a new student): ");
        int studentId = scanner.nextInt();

        if (studentId == 0) {
            enrollNewStudent();
        } else {
            if (students != null) {
                Student student = findStudentById(studentId);

                if (student != null) {
                    System.out.println("Authentication successful. Welcome, " + student.getStudentName() + "!\n");
                    int studentChoice;

                    do {
                        displayStudentMenu();
                        studentChoice = getUserOption();

                        switch (studentChoice) {
                            case 1:
                                registerForCourse(student);
                                break;
                            case 2:
                                dropCourse(student);
                                break;
                            case 3:
                                displayEnrolledCourses(student);
                                break;
                            case 4:
                                System.out.println("Exiting student menu. Returning to main menu.\n");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.\n");
                        }
                    } while (studentChoice != 4);
                } else {
                    System.out.println("Student not found with the given ID.\n");
                }
            } else {
                System.out.println("No students enrolled yet. Please contact the administrator to enroll.\n");
            }
        }
    }

    private static void enrollNewStudent() {
        System.out.print("Enter new student name (or type 'exit' to cancel): ");
        scanner.nextLine(); // Consume the newline character
        String studentName = scanner.nextLine();

        if (!studentName.equalsIgnoreCase("exit")) {
            if (students != null) {
                for (int i = 0; i < students.length; i++) {
                    if (students[i] == null) {
                        students[i] = new Student(studentName);
                        System.out.println("New student enrolled successfully with ID: " + students[i].getStudentId() +"\n");
                        students[i].displayStudentDetails(); // Display details along with ID
                        return;
                    }
                }
                System.out.println("Error: Maximum students reached.");
            } else {
                System.out.println("Error: Students array is not initialized.");
            }
        } else {
            System.out.println("Enrollment canceled.\n");
        }
    }

    private static void registerForCourse(Student student) {
        displayAvailableCourses();

        System.out.print("Enter the course number to register (or 0 to exit): ");
        int courseNumber = scanner.nextInt();

        if (courseNumber >= 1 && courseNumber <= courses.length && courses[courseNumber - 1] != null) {
            String courseName = courses[courseNumber - 1].getCourseName();

            if (!isCourseAlreadyEnrolled(student, courseName)) {
                student.registerCourse(courseName);
                System.out.println("Course registration successful.\n");
            } else {
                System.out.println(student.getStudentName() + " is already enrolled in the course: " + courseName + "\n");
            }
        } else if (courseNumber == 0) {
            System.out.println("Exiting course registration.\n");
        } else {
            System.out.println("Invalid course number. Please try again.\n");
        }
    }

    private static boolean isCourseAlreadyEnrolled(Student student, String courseName) {
        for (int i = 0; i < student.getCourseCount(); i++) {
            if (student.getEnrolledCourses()[i].equals(courseName)) {
                return true;
            }
        }
        return false;
    }

    private static void dropCourse(Student student) {
        displayEnrolledCourses(student);

        System.out.print("Enter the course number to drop (or 0 to exit): ");
        int courseNumber = scanner.nextInt();

        if (courseNumber >= 1 && courseNumber <= student.getCourseCount()) {
            String courseToDrop = student.getEnrolledCourses()[courseNumber - 1];
            student.dropCourse(courseToDrop);
            System.out.println("Course dropped successfully.\n");
            DatabaseManager.deleteData(student.getStudentId(), courseToDrop); // Delete data for the specific course
        } else if (courseNumber == 0) {
            System.out.println("Exiting course drop.\n");
        } else {
            System.out.println("Invalid course number. Please try again.\n");
        }
    }

    private static void displayEnrolledCourses(Student student) {
        System.out.println();
        student.displayEnrolledCourses();
        System.out.println();
    }

    private static void displayEnrolledCoursesForAllStudents() {
        System.out.println();
        for (Student student : students) {
            if (student != null) {
                displayEnrolledCourses(student);
            }
        }
    }

    private static void displayAdminMenu() {
        System.out.println("Admin Menu:");
        System.out.println("1. Display student details");
        System.out.println("2. Display enrolled courses");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void displayStudentMenu() {
        System.out.println("Student Menu:");
        System.out.println("1. Register for a course");
        System.out.println("2. Drop a course");
        System.out.println("3. Display enrolled courses");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void displayAvailableCourses() {
        System.out.println();
        System.out.println("Available Courses:");
        for (int i = 0; i < courses.length; i++) {
            if (courses[i] != null) {
                System.out.println((i + 1) + ". " + courses[i].getCourseName());
            }
        }
    }

    private static void displayAllStudentDetails() {
        System.out.println();
        System.out.println("Details of all enrolled students:");
        for (Student student : students) {
            if (student != null) {
                System.out.println(student.getStudentName() + " (ID: " + student.getStudentId() + ")");
            }
        }
        System.out.println();
    }

    private static Student findStudentById(int studentId) {
        for (Student student : students) {
            if (student != null && student.getStudentId() == studentId) {
                return student;
            }
        }
        return null;
    }
}
