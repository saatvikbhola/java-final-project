package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static Connection con;
    private static Statement stmt;

    private static final String JDBC_URL = "jdbc:mysql://localhost:/project";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            stmt = con.createStatement();
            createTable();
        } catch (Exception e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    public static void createTable() {
        try {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS student_courses (" +
                    "student_id INT, " +
                    "student_name VARCHAR(255), " +
                    "course_name VARCHAR(255)" +
                    ")";
            stmt.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public static void insertData(int studentId, String studentName, String course) {
        try {
            String insertDataQuery = "INSERT INTO student_courses VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertDataQuery);
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, studentName);
            preparedStatement.setString(3, course);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }

    public static void deleteData(int studentId, String course) {
        try {
            String deleteDataQuery = "DELETE FROM student_courses WHERE student_id=? AND course_name=?";
            PreparedStatement preparedStatement = con.prepareStatement(deleteDataQuery);
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, course);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting data: " + e.getMessage());
        }
    }

    public static void closeDatabase() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing database: " + e.getMessage());
        }
    }
}
