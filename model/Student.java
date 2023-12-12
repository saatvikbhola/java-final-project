package model;

import database.DatabaseManager;

public class Student {
    private static int studentIdCounter = 1;
    private int studentId;
    private String studentName;
    private String[] enrolledCourses;
    private int courseCount;

    public Student(String name) {
        this.studentId = studentIdCounter++;
        this.studentName = name;
        this.enrolledCourses = new String[5];
        this.courseCount = 0;
    }

    public void registerCourse(String course) {
        if (courseCount < enrolledCourses.length) {
            enrolledCourses[courseCount++] = course;
            System.out.println(studentName + " is registered for the course: " + course);
            insertData(studentId, studentName, course); // Insert data when registering for a course
        } else {
            System.out.println("Error: Maximum courses reached for " + studentName);
        }
    }

    public void dropCourse(String course) {
        for (int i = 0; i < courseCount; i++) {
            if (enrolledCourses[i] != null && enrolledCourses[i].equals(course)) {
                System.out.println(studentName + " has dropped the course: " + course);
                deleteData(studentId, course); // Delete data for the specific course
                System.arraycopy(enrolledCourses, i + 1, enrolledCourses, i, courseCount - i - 1);
                enrolledCourses[courseCount - 1] = null;
                courseCount--;
                return;
            }
        }
        System.out.println(studentName + " is not enrolled in the course: " + course);
    }

    public void displayEnrolledCourses() {
        System.out.println("Enrolled courses for " + studentName + " (ID: " + studentId + "):");
        for (int i = 0; i < courseCount; i++) {
            System.out.println((i + 1) + ". " + enrolledCourses[i]);
        }
    }

    public void displayStudentDetails() {
        System.out.println(studentName + " (ID: " + studentId + "):");
        System.out.println("Enrolled courses:");
        for (int i = 0; i < courseCount; i++) {
            System.out.println((i + 1) + ". " + enrolledCourses[i]);
        }
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String[] getEnrolledCourses() {
        return enrolledCourses;
    }

    public int getCourseCount() {
        return courseCount;
    }

    public void insertData(int studentId, String studentName, String course) {
        DatabaseManager.insertData(studentId, studentName, course);
    }

    public void deleteData(int studentId, String course) {
        DatabaseManager.deleteData(studentId, course);
    }
}
