package tubes.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import tubes.launch.mainApp;

public class EditSchedule extends StackPane {

    public EditSchedule(mainApp app) {

        // Background
        String backgroundString = getClass().getResource("/Background.png").toString();
        ImageView backgroundImage = new ImageView(new Image(backgroundString));

        //TOP
        Label topLbl = new Label("MASUKKAN JADWAL BARU");
        topLbl.setFont(Font.font("Franklin Gothic Heavy", FontWeight.BOLD, FontPosture.ITALIC, 90));
        topLbl.setTextFill(Color.rgb(193, 214, 200, 1));
        topLbl.setTextAlignment(TextAlignment.CENTER);

        HBox topLblWrapper = new HBox(topLbl);
        topLblWrapper.setAlignment(Pos.CENTER);

        Rectangle persegi = new Rectangle(1400, 600);
        persegi.setStyle(
                "-fx-fill: #FFFFFF;" +
                "-fx-opacity: 0.35;" +
                "-fx-arc-Width: 25;"+
                "-fx-arc-Height: 25;"
        );

        Label namaKegiatanLbl = new Label("NAMA KEGIATAN");
        namaKegiatanLbl.setFont(Font.font("Segoe UI", 20));
        namaKegiatanLbl.setTextFill(Color.rgb(1, 47, 16, 1));

        Label kategoriLbl = new Label("KATEGORI");
        kategoriLbl.setFont(Font.font("Segoe UI", 20));
        kategoriLbl.setTextFill(Color.rgb(1, 47, 16, 1));

        Label tanggalLbl = new Label("TANGGAL");
        tanggalLbl.setFont(Font.font("Segoe UI", 20));
        tanggalLbl.setTextFill(Color.rgb(1, 47, 16, 1));

        Label waktuLbl = new Label("WAKTU");
        waktuLbl.setFont(Font.font("Segoe UI", 20));
        waktuLbl.setTextFill(Color.rgb(1, 47, 16, 1));

        Label deskripsiLbl = new Label("DESKRIPSI");
        deskripsiLbl.setFont(Font.font("Segoe UI", 20));
        deskripsiLbl.setTextFill(Color.rgb(1, 47, 16, 1));

        TextField namaKegiatanField = new TextField();
        namaKegiatanField.setPrefSize(900, 40);
        namaKegiatanField.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                "-fx-background-opacity: 0.35;" +
                "-fx-border-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-text-fill: rgb(193, 214, 200, 1);" +
                "-fx-font-size: 15px"
        );

        ArrayList<String> kategoriItems = new ArrayList<>();
        kategoriItems.add("AKADEMIK");
        kategoriItems.add("NON-AKADEMIK");

        ComboBox<String> kategoriCB = new ComboBox<>();
        kategoriCB.getItems().addAll(kategoriItems);
        kategoriCB.setPrefSize(900,40);
        kategoriCB.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                        "-fx-background-opacity: 0.35;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-radius: 5;" +
                        "-fx-text-fill: rgb(193, 214, 200, 1);" +
                        "-fx-font-size: 15px"
        );

        TextField deskripsiField = new TextField();
        deskripsiField.setPrefSize(900, 40);
        deskripsiField.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                        "-fx-background-opacity: 0.35;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-radius: 5;" +
                        "-fx-text-fill: rgb(193, 214, 200, 1);" +
                        "-fx-font-size: 15px"
        );

        DatePicker tanggalDP = new DatePicker();
        tanggalDP.setPrefSize(900,40);
        tanggalDP.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                        "-fx-background-opacity: 0.35;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-radius: 5;" +
                        "-fx-text-fill: rgb(193, 214, 200, 1);" +
                        "-fx-font-size: 15px"
        );

        TextField waktuField = new TextField();
        waktuField.setPrefSize(900,40);
        waktuField.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                        "-fx-background-opacity: 0.35;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-radius: 5;" +
                        "-fx-text-fill: rgb(193, 214, 200, 1);" +
                        "-fx-font-size: 15px"
        );

        GridPane inputGridPane = new GridPane();
        inputGridPane.setHgap(200);
        inputGridPane.setVgap(50);
        inputGridPane.setAlignment(Pos.CENTER);

        inputGridPane.add(namaKegiatanLbl, 0,0);
        inputGridPane.add(namaKegiatanField, 1, 0);
        inputGridPane.add(kategoriLbl, 0, 1);
        inputGridPane.add(kategoriCB, 1, 1);
        inputGridPane.add(tanggalLbl, 0, 2);
        inputGridPane.add(tanggalDP, 1,2);
        inputGridPane.add(waktuLbl, 0, 3);
        inputGridPane.add(waktuField,1,3);
        inputGridPane.add(deskripsiLbl, 0,4);
        inputGridPane.add(deskripsiField,1 ,4);

        Button batalBtn = new Button("BATAL");
        batalBtn.setStyle(
                "-fx-background-color: rgb(1, 47, 16, 1);" +
                "-fx-border-color: transparent;" +
                "-fx-text-fill: rgb(193, 214, 200, 1);" +
                "-fx-font-size: 20px;" +
                "-fx-cursor: hand;" +
                "-fx-font-weight: BOLD;" +
                "-fx-background-radius: 50;"
        );
        batalBtn.setPrefSize(200,40);
        batalBtn.setTranslateY(300);
        batalBtn.setTranslateX(-120);

        Button simpanBtn = new Button("SIMPAN");
        simpanBtn.setStyle(
                "-fx-background-color: rgb(1, 47, 16, 1);" +
                "-fx-border-color: transparent;" +
                "-fx-text-fill: rgb(193, 214, 200, 1);" +
                "-fx-font-size: 20px;" +
                "-fx-cursor: hand;" +
                "-fx-font-weight: BOLD;" +
                "-fx-background-radius: 50;"
        );
        simpanBtn.setPrefSize(200,40);
        simpanBtn.setTranslateY(300);
        simpanBtn.setTranslateX(120);

        StackPane inputCard = new StackPane();
        inputCard.getChildren().addAll(persegi, inputGridPane);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(inputCard, batalBtn, simpanBtn);

        BorderPane borderPane1 = new BorderPane();
        borderPane1.setTop(topLblWrapper);
        borderPane1.setCenter(stackPane);
        borderPane1.setPadding(new Insets(20));

        // BACKGROUND + LAYOUT
        this.getChildren().addAll(backgroundImage, borderPane1);

        // ADJUST BACKGROUND
        backgroundImage.fitHeightProperty().bind(this.heightProperty());
        backgroundImage.fitWidthProperty().bind(this.widthProperty());


        // BUTTON ACTION

        batalBtn.setOnAction(e -> {
            app.switchSceneSchedulePage();
        });

        simpanBtn.setOnAction(e -> {
            app.switchSceneSchedulePage();
        });

    }

}