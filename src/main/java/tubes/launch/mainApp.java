package tubes.launch;

import java.util.HashMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


import tubes.backend.DatabaseManager; // Penting untuk inisialisasiDatabase
import tubes.backend.EmailService;
import tubes.backend.Notifikasi;
import tubes.backend.PengelolaTugas;
import tubes.backend.Tugas; // Import Tugas jika digunakan di parameter switchScene


import tubes.pages.EditSchedule;
import tubes.pages.LogInPage;
import tubes.pages.SchedulePage;
import tubes.pages.SignUpPage;
import tubes.pages.WelcomePage;

// Import untuk SQL (diperlukan untuk inisialisasiDatabase)
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class mainApp extends Application {

    Stage stage;
    Scene scene;

    // Backend Services - tetap menggunakan static sesuai kode Anda sebelumnya
    public static PengelolaTugas pengelolaTugas;
    public static Notifikasi notifikasiService;
    public static EmailService emailService;

    @Override
    public void start(Stage stage) {
        this.stage = stage;


        pengelolaTugas = new PengelolaTugas();

  
        inisialisasiDatabase();

        // Example SMTP config - replace with your actual config or load from a file
        HashMap<String, String> smtpConfig = new HashMap<>();
        smtpConfig.put("host", "smtp.example.com");
        smtpConfig.put("port", "587");
        smtpConfig.put("username", "user@example.com");
        smtpConfig.put("password", "your_password");
        smtpConfig.put("tls_enable", "true");

        emailService = new EmailService(smtpConfig);
        notifikasiService = new Notifikasi(emailService);

        WelcomePage welcomePage = new WelcomePage(this); // Pass 'this' (mainApp instance)
        this.scene = new Scene(welcomePage, 1440, 800);

        stage.setTitle("AMAN");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            System.out.println("Aplikasi ditutup.");
        });
        stage.show();
    }

    private void inisialisasiDatabase() {
        System.out.println("Memulai proses inisialisasi database SQLite...");

        String createUserTableSql = "CREATE TABLE IF NOT EXISTS users ("
                                  + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                  + "username TEXT NOT NULL UNIQUE,"
                                  + "email TEXT NOT NULL UNIQUE,"
                                  + "sandi TEXT NOT NULL," // INGAT: Ini harusnya HASH, bukan plain text
                                  + "created_at TEXT DEFAULT (STRFTIME('%Y-%m-%d %H:%M:%S', 'now', 'localtime'))"
                                  + ");";

        String createTasksTableSql = "CREATE TABLE IF NOT EXISTS tasks ("
                                   + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                   + "user_id INTEGER NOT NULL,"
                                   + "judul TEXT NOT NULL,"
                                   + "deskripsi TEXT,"
                                   + "tanggal_batas TEXT," // Disimpan sebagai TEXT (ISO8601 String)
                                   + "kategori TEXT,"
                                   + "lokasi TEXT,"
                                   + "mata_kuliah TEXT,"
                                   + "selesai INTEGER DEFAULT 0," // 0 untuk false, 1 untuk true
                                   + "created_at TEXT DEFAULT (STRFTIME('%Y-%m-%d %H:%M:%S', 'now', 'localtime')),"
                                   + "updated_at TEXT," // Diupdate oleh trigger
                                   + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                                   + ");";

        String createTasksUpdateTriggerSql = "CREATE TRIGGER IF NOT EXISTS update_tasks_updated_at "
                                           + "AFTER UPDATE ON tasks "
                                           + "FOR EACH ROW "
                                           + "BEGIN "
                                           + "    UPDATE tasks SET updated_at = STRFTIME('%Y-%m-%d %H:%M:%S', 'now', 'localtime') WHERE id = OLD.id; "
                                           + "END;";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            if (conn == null) {
                System.err.println("INISIALISASI DB GAGAL: Koneksi database null.");
                return;
            }
            System.out.println("Koneksi DB untuk inisialisasi berhasil.");

            System.out.println("Mencoba membuat tabel users...");
            stmt.execute(createUserTableSql);
            System.out.println("Perintah CREATE TABLE users dieksekusi.");

            System.out.println("Mencoba membuat tabel tasks...");
            stmt.execute(createTasksTableSql);
            System.out.println("Perintah CREATE TABLE tasks dieksekusi.");
            
            System.out.println("Mencoba membuat trigger tasks_updated_at...");
            stmt.execute(createTasksUpdateTriggerSql);
            System.out.println("Perintah CREATE TRIGGER tasks_updated_at dieksekusi.");

            System.out.println("Skema database SQLite (users, tasks, trigger) berhasil disiapkan/diverifikasi.");

        } catch (SQLException e) {
            System.err.println("Error SQL saat inisialisasi skema database SQLite:");
            e.printStackTrace(); // Sangat penting untuk melihat detail error
        } catch (Exception e) {
            System.err.println("Error umum saat inisialisasi skema database SQLite:");
            e.printStackTrace();
        }
    }

    // Metode switchScene Anda tetap sama
    public void switchSceneWelcomePage(){
        this.scene.setRoot(new WelcomePage(this));
    }

    public void switchSceneLogInPage(){
        this.scene.setRoot(new LogInPage(this));
    }

    public void switchSceneSignUpPage(){
        this.scene.setRoot(new SignUpPage(this));
    }

    public void switchSceneSchedulePage(){
        if (pengelolaTugas == null || pengelolaTugas.getCurrentUser() == null) {
            System.out.println("Tidak ada user yang login atau PengelolaTugas belum siap, mengarahkan ke halaman Login.");
            switchSceneLogInPage();
            return;
        }
        this.scene.setRoot(new SchedulePage(this));
    }

    public void switchSceneEditSchedulePage(Tugas tugasToEdit){ // Terima objek Tugas
        if (pengelolaTugas == null || pengelolaTugas.getCurrentUser() == null) {
            switchSceneLogInPage();
            return;
        }
        this.scene.setRoot(new EditSchedule(this, tugasToEdit));
    }

    public void switchSceneEditSchedulePage(){ // Untuk tugas baru
        if (pengelolaTugas == null || pengelolaTugas.getCurrentUser() == null) {
            switchSceneLogInPage();
            return;
        }
        this.scene.setRoot(new EditSchedule(this, null)); // Pass null untuk tugas baru
    }

    public PengelolaTugas getPengelolaTugas() {
        return pengelolaTugas;
    }
    
    public Notifikasi getNotifikasiService() {
        return notifikasiService;
    }

    public static void main(String[] args) {
        launch(args);
    }
}