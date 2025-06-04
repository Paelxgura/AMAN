package tubes.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import tubes.launch.mainApp;

import java.nio.file.StandardWatchEventKinds;
import java.util.ArrayList;
import java.util.Stack;

public class SchedulePage extends StackPane {

    public SchedulePage(mainApp app) {

        // Background
        String backgroundString = getClass().getResource("/Background.png").toString();
        ImageView backgroundImage = new ImageView(new Image(backgroundString));

        // Top
        Label jadwalLbl = new Label("JADWAL");
        jadwalLbl.setFont(Font.font("Franklin Gothic Heavy", FontWeight.BOLD, FontPosture.ITALIC, 90));
        jadwalLbl.setTextFill(Color.WHITE);
        BorderPane.setAlignment(jadwalLbl, Pos.CENTER);

        Button keluarBtn= new Button("KELUAR");
        keluarBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-size: 20px;" +
                "-fx-underline: true;" +
                "-fx-cursor: hand;"
        );

        Label filterLbl = new Label("FILTER");
        filterLbl.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-size: 20px;"
        );

        ArrayList<String> filterItems = new ArrayList<>();
        filterItems.add("Semua");
        filterItems.add("Akademik");
        filterItems.add("Non-Akademik");

        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll(filterItems);
        filterComboBox.getSelectionModel().selectFirst();

        HBox filterHBox = new HBox(5);
        filterHBox.getChildren().addAll(filterLbl, filterComboBox);
        filterHBox.setAlignment(Pos.CENTER);

        HBox topHBox = new HBox(350);
        topHBox.getChildren().addAll(filterHBox, jadwalLbl, keluarBtn);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setPadding(new Insets(20));

        // DAFTAR AKTIVITAS

        Region persegi = new Region();
        persegi.setPrefSize(1440,600);
        persegi.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-opacity: 0.35;" +
                        "-fx-background-radius: 50 50 0 0;"
        );

        // BIKIN NEW OBJECT DARI ACTIVITY CARD
        activityCard aktivitas1 = new activityCard(app, "INI JUDUL1", "12", "LOKASI","isiLokasi","DESKRIPSI", "ini isi deskripsi awiakaowkaiwkaokekaweoiawoek");
        activityCard aktivitas2 = new activityCard(app, "INI JUDUL2", "12", "LOKASI","isiLokasi","DESKRIPSI", "ini isi deskripsi awiakaowkaiwkaokekaweoiawoek");
        activityCard aktivitas3 = new activityCard(app, "INI JUDUL3", "12", "LOKASI","isiLokasi","DESKRIPSI", "ini isi deskripsi awiakaowkaiwkaokekaweoiawoek");

        VBox ab = new VBox(10);
        ab.setPadding(new Insets(50, 0, 0, 0));
        ab.getChildren().addAll(aktivitas1.getView(), aktivitas2.getView(), aktivitas3.getView());

        ScrollPane scrollPane = new ScrollPane(ab);
        scrollPane.setStyle("""
        -fx-background: transparent;
        -fx-background-color: transparent;
        """);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(600);

        VBox scrollVBox = new VBox();
        scrollVBox.setPadding(new Insets(10));
        scrollVBox.getChildren().add(scrollPane);

        Button addScheduleBtn = new Button("+");
        addScheduleBtn.setStyle("""
                -fx-background-color: #68AE5A;
                -fx-text-fill: #FFFFFF;
                -fx-font-size: 40px;
                -fx-background-radius: 100px;
                -fx-cursor: hand;
                -fx-font-weight: bold;
        """);
        addScheduleBtn.setPrefSize(100,100);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(persegi, scrollVBox);

        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setTop(topHBox);
        mainBorderPane.setBottom(stackPane);

        StackPane main = new StackPane();
        main.getChildren().addAll(mainBorderPane, addScheduleBtn);

        StackPane.setAlignment(addScheduleBtn, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(addScheduleBtn, new Insets(20));

        // Background + Layout
        this.getChildren().addAll(backgroundImage, main);

        // ADJUST BACKGROUND
        backgroundImage.fitWidthProperty().bind(this.widthProperty());
        backgroundImage.fitHeightProperty().bind(this.heightProperty());

        keluarBtn.setOnAction(e -> {
            app.switchSceneWelcomePage();
        });

        addScheduleBtn.setOnAction(e -> {
            app.switchSceneEditSchedulePage();
        });

    }
}