    package tubes.launch;

    import javafx.application.Application;
    import javafx.scene.Scene;
    import javafx.stage.Stage;
    import tubes.pages.*;

    public class mainApp extends Application {

        Stage stage;
        Scene scene;
    
        @Override
        public void start(Stage stage) {

            this.stage = stage;

            WelcomePage welcomePage = new WelcomePage(this);
            this.scene = new Scene(welcomePage, 1440, 800);

            stage.setTitle("AMAN");
            stage.setScene(scene);
            stage.show();

        }

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
            this.scene.setRoot(new SchedulePage(this));
        }

        public void switchSceneEditSchedulePage(){
            this.scene.setRoot(new EditSchedule(this));
        }

    }