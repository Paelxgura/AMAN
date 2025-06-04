// src/main/java/tubes/backend/DatabaseManager.java
package tubes.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    // Path ke file database SQLite.
    // File ini akan dibuat otomatis jika belum ada, di direktori tempat aplikasi dijalankan.
    // Anda bisa juga menggunakan path absolut jika diinginkan, misal:
    // private static final String DB_URL = "jdbc:sqlite:D:/databases/aman_app.db";
    private static final String DB_NAME = "aman_app.db"; // Nama file database SQLite
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    /**
     * Membuat dan mengembalikan koneksi ke database SQLite.
     *
     * @return Objek Connection jika berhasil, null jika gagal.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Driver SQLite biasanya otomatis ter-load jika JAR ada di classpath.
            // Class.forName("org.sqlite.JDBC"); // Baris ini umumnya tidak lagi diperlukan.
            
            connection = DriverManager.getConnection(DB_URL);
            // System.out.println("Koneksi ke SQLite '" + DB_NAME + "' berhasil!");

            // (Opsional) Aktifkan foreign key constraints jika Anda menggunakannya
            // SQLite secara default tidak meng-enforce foreign keys untuk kompatibilitas mundur.
            if (connection != null) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }
            }

        } catch (SQLException e) {
            System.err.println("Koneksi ke SQLite gagal: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    // Metode closeConnection, closeStatement, closeResultSet tetap sama seperti sebelumnya
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Gagal menutup koneksi SQLite: " + e.getMessage());
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Gagal menutup Statement SQLite: " + e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Gagal menutup ResultSet SQLite: " + e.getMessage());
            }
        }
    }
}