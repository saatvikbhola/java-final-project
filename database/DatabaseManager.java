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

    private static void initializeDatabase() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        stmt = con.createStatement();
        createTable();
        System.out.println("Database initialized successfully.");
    } catch (Exception e) {
        System.err.println("Error initializing database: " + e.getMessage());
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
        System.out.println("Table created successfully.");
    } catch (SQLException e) {
        System.err.println("Error creating table: " + e.getMessage());
    }
}

public static void insertData(int studentId, String studentName, String course) {
    try {
        String insertDataQuery = "INSERT INTO student_courses VALUES (?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(insertDataQuery);
        pstmt.setInt(1, studentId);
        pstmt.setString(2, studentName);
        pstmt.setString(3, course);
        pstmt.executeUpdate();
        System.out.println("Data inserted successfully.");
    } catch (SQLException e) {
        System.err.println("Error inserting data: " + e.getMessage());
    }
}

public static void deleteData(int studentId, String course) {
    try {
        String deleteDataQuery = "DELETE FROM student_courses WHERE student_id=? AND course_name=?";
        PreparedStatement pstmt = con.prepareStatement(deleteDataQuery);
        pstmt.setInt(1, studentId);
        pstmt.setString(2, course);
        pstmt.executeUpdate();
        System.out.println("Data deleted successfully.");
    } catch (SQLException e) {
        System.err.println("Error deleting data: " + e.getMessage());
    }
}

private static void closeDatabase() {
    try {
        if (stmt != null) {
            stmt.close();
        }
        if (con != null) {
            con.close();
        }
        System.out.println("Database closed successfully.");
    } catch (SQLException e) {
        System.err.println("Error closing database: " + e.getMessage());
    }
}
