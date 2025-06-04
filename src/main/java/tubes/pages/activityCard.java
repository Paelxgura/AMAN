package tubes.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import tubes.launch.mainApp;

public class activityCard {

    private String judul;
    private String waktu;
    private String lokasi;
    private String isiLokasi;
    private String deskripsi;
    private String isiDeskripsi;

    private Rectangle kotakAktivitas;
    private Label judulAktvLbl;
    private Label waktuAktvLbl;
    private Label lokasiAktvLbl;
    private Label namaLokasiAktvLbl;
    private Label deskripsiAktvLbl;
    private Label isiDeskripsiAktvLbl;
    private Button editBtn;
    private Button hapusBtn;

    private VBox judulWaktuVBox;
    private VBox lokasiVBox;
    private VBox deskripsiVBox;
    private VBox isiAKtivitasVBox;
    private VBox editHapusVBox;
    private BorderPane aktivitasCard;
    private StackPane aktivitasPane;

    private mainApp app;

    public activityCard(mainApp app, String judul, String waktu, String lokasi, String isiLokasi, String deskripsi, String isiDeskripsi){

        this.judul = judul;
        this.waktu = waktu;
        this.lokasi = lokasi;
        this.isiLokasi = isiLokasi;
        this.deskripsi = deskripsi;
        this.isiDeskripsi = isiDeskripsi;
        this.app = app;

        this.kotakAktivitas = new Rectangle(1300, 400);
        this.kotakAktivitas.setStyle(
                "-fx-fill: #000612;" +
                        "-fx-opacity: 0.45;" +
                        "-fx-arc-Width: 100;" +
                        "-fx-arc-Height: 100;"
        );

        this.judulAktvLbl = new Label();
        this.judulAktvLbl.setText(this.judul);
        this.judulAktvLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        this.judulAktvLbl.setTextFill(Color.rgb(193, 214, 200, 1));

        this.waktuAktvLbl = new Label();
        this.waktuAktvLbl.setText(this.waktu);
        this.waktuAktvLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        this.waktuAktvLbl.setTextFill(Color.rgb(116, 0, 0, 1));

        this.judulWaktuVBox = new VBox(5);
        this.judulWaktuVBox.getChildren().addAll(this.judulAktvLbl, this.waktuAktvLbl);

        this.lokasiAktvLbl = new Label();
        this.lokasiAktvLbl.setText(this.lokasi);
        this.lokasiAktvLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        this.lokasiAktvLbl.setTextFill(Color.rgb(193, 214, 200, 1));
        this.lokasiAktvLbl.setUnderline(true);

        this.namaLokasiAktvLbl = new Label();
        this.namaLokasiAktvLbl.setText(this.isiLokasi);
        this.namaLokasiAktvLbl.setFont(Font.font("Segoe UI", 30));
        this.namaLokasiAktvLbl.setTextFill(Color.rgb(193, 214, 200, 1));

        this.lokasiVBox = new VBox(5);
        this.lokasiVBox.getChildren().addAll(this.lokasiAktvLbl, this.namaLokasiAktvLbl);

        this.deskripsiAktvLbl = new Label();
        this.deskripsiAktvLbl.setText(this.deskripsi);
        this.deskripsiAktvLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        this.deskripsiAktvLbl.setTextFill(Color.rgb(193, 214, 200, 1));
        this.deskripsiAktvLbl.setUnderline(true);

        this.isiDeskripsiAktvLbl = new Label();
        this.isiDeskripsiAktvLbl.setText(this.isiDeskripsi);
        this.isiDeskripsiAktvLbl.setFont(Font.font("Segoe UI", 30));
        this.isiDeskripsiAktvLbl.setTextFill(Color.rgb(193, 214, 200, 1));
        this.isiDeskripsiAktvLbl.setWrapText(true);

        this.deskripsiVBox = new VBox(5);
        this.deskripsiVBox.getChildren().addAll(this.deskripsiAktvLbl, this.isiDeskripsiAktvLbl);

        this.isiAKtivitasVBox = new VBox(15);
        this.isiAKtivitasVBox.getChildren().addAll(this.judulWaktuVBox, this.lokasiVBox, this.deskripsiVBox);
        this.isiAKtivitasVBox.setAlignment(Pos.CENTER);

        //CRUD BUTTON
        this.editBtn = new Button("EDIT");
        this.editBtn.setPrefSize(250,60);
        this.editBtn.setStyle(
                "-fx-background-color: #012F10;" +
                        "-fx-text-fill: #C1D6C8;" +
                        "-fx-font-size: 25px;" +
                        "-fx-font-weight: BOLD;" +
                        "-fx-background-radius: 50;" +
                        "-fx-cursor: hand;"
        );

        this.editBtn.setOnAction(e -> {
            this.app.switchSceneEditSchedulePage();
        });

        this.hapusBtn = new Button("HAPUS");
        this.hapusBtn.setPrefSize(250,60);
        this.hapusBtn.setStyle(
                "-fx-background-color: #012F10;" +
                        "-fx-text-fill: #C1D6C8;" +
                        "-fx-font-size: 25px;" +
                        "-fx-font-weight: BOLD;" +
                        "-fx-background-radius: 50;" +
                        "-fx-cursor: hand;"
        );

        this.editHapusVBox = new VBox(50);
        this.editHapusVBox.getChildren().addAll(this.editBtn, this.hapusBtn);
        this.editHapusVBox.setAlignment(Pos.CENTER);

        // AKTIVITAS
        this.aktivitasCard = new BorderPane();
        this.aktivitasCard.setLeft(this.isiAKtivitasVBox);
        this.aktivitasCard.setRight(this.editHapusVBox);
        this.aktivitasCard.setPadding(new Insets(60,80,60,80));

        this.aktivitasPane = new StackPane();
        this.aktivitasPane.setPrefSize(1400,400);
        this.aktivitasPane.getChildren().addAll(this.kotakAktivitas, this.aktivitasCard);

    }

    public StackPane getView(){
        return this.aktivitasPane;
    }

}
