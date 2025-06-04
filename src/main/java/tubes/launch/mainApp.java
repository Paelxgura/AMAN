package tubes.launch;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tubes.pages.*;
import tubes.Backend.PengelolaTugas; // Import
import tubes.Backend.EmailService;   // Import
import tubes.Backend.Notifikasi;     // Import
import java.util.HashMap;            // Import

public class mainApp extends Application {

    Stage stage;    
    Scene scene;

    // Backend Services - make them static or manage through a proper service locator/DI
    public static PengelolaTugas pengelolaTugas;
    public static Notifikasi notifikasiService;
    public static EmailService emailService;


    @Override
    public void start(Stage stage) {
        this.stage = stage;

        // Initialize backend services
        pengelolaTugas = new PengelolaTugas();
        // Example SMTP config - replace with your actual config or load from a file
        HashMap<String, String> smtpConfig = new HashMap<>();
        smtpConfig.put("host", "smtp.example.com");
        smtpConfig.put("port", "587");
        smtpConfig.put("username", "user@example.com");
        smtpConfig.put("password", "your_password");
        smtpConfig.put("tls_enable", "true");

        emailService = new EmailService(smtpConfig);
        notifikasiService = new Notifikasi(emailService);

        // Load any saved data at startup (optional, if you implement persistence)
        // pengelolaTugas.muatSemuaData();

        WelcomePage welcomePage = new WelcomePage(this); // Pass 'this' (mainApp instance)
        this.scene = new Scene(welcomePage, 1440, 800);

        stage.setTitle("AMAN");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            // Save data on close (optional)
            // if (pengelolaTugas != null) {
            //     pengelolaTugas.simpanSemuaData();
            // }
            System.out.println("Aplikasi ditutup.");
        });
        stage.show();
    }

    // Your switchScene methods remain the same, but the pages
    // will now be able to access pengelolaTugas via the mainApp instance or statically.

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
        // Ensure user is logged in, or redirect to login
        if (pengelolaTugas.getCurrentUser() == null) {
            System.out.println("Tidak ada user yang login, mengarahkan ke halaman Login.");
            switchSceneLogInPage();
            return;
        }
        this.scene.setRoot(new SchedulePage(this));
    }

    public void switchSceneEditSchedulePage(tubes.Backend.Tugas tugasToEdit){ // Pass Tugas if editing
        if (pengelolaTugas.getCurrentUser() == null) {
            switchSceneLogInPage();
            return;
        }
        this.scene.setRoot(new EditSchedule(this, tugasToEdit));
    }
     public void switchSceneEditSchedulePage(){ // For creating new task
        if (pengelolaTugas.getCurrentUser() == null) {
            switchSceneLogInPage();
            return;
        }
        this.scene.setRoot(new EditSchedule(this, null)); // Pass null for new task
    }

    // Add a getter for PengelolaTugas so pages can access it
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