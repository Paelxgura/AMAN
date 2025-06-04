package tubes.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import tubes.backend.Tugas; // Pastikan import Tugas ada
import tubes.launch.mainApp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Optional;

public class EditSchedule extends StackPane {

    private mainApp app;
    private Tugas tugasToEdit; // Untuk menyimpan tugas yang sedang diedit (bisa null jika baru)

    // Deklarasikan komponen UI yang perlu diakses di luar konstruktor sebagai field kelas
    private Label topLbl;
    private TextField namaKegiatanField;
    private ComboBox<String> kategoriCB;
    private DatePicker tanggalDP;
    private TextField waktuField;
    private TextField deskripsiField;
    // Tambahkan field untuk lokasi dan mata kuliah jika diperlukan

    // Konstruktor yang mendukung mode BUAT BARU dan EDIT
    public EditSchedule(mainApp app, Tugas tugas) {
        this.app = app;
        this.tugasToEdit = tugas; // Simpan tugas yang akan diedit (bisa null)

        // Background
        String backgroundString = getClass().getResource("/Background.png").toString();
        ImageView backgroundImage = new ImageView(new Image(backgroundString));

        // TOP
        this.topLbl = new Label(); // Label judul halaman
        this.topLbl.setFont(Font.font("Franklin Gothic Heavy", FontWeight.BOLD, FontPosture.ITALIC, 90));
        this.topLbl.setTextFill(Color.rgb(193, 214, 200, 1));
        this.topLbl.setTextAlignment(TextAlignment.CENTER);

        HBox topLblWrapper = new HBox(this.topLbl);
        topLblWrapper.setAlignment(Pos.CENTER);

        Rectangle persegi = new Rectangle(1400, 600);
        persegi.setStyle(
                "-fx-fill: #FFFFFF;" +
                        "-fx-opacity: 0.35;" +
                        "-fx-arc-width: 25;" + // Koreksi Width -> width
                        "-fx-arc-height: 25;" // Koreksi Height -> height
        );

        // LABELS
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

        // FIELDS
        this.namaKegiatanField = new TextField();
        this.namaKegiatanField.setPrefSize(900, 40);
        this.namaKegiatanField.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                // ... (style lainnya) ...
                "-fx-text-fill: rgb(193, 214, 200, 1);" +
                "-fx-font-size: 15px"
        );

        ArrayList<String> kategoriItems = new ArrayList<>();
        kategoriItems.add("AKADEMIK");
        kategoriItems.add("NON-AKADEMIK");

        this.kategoriCB = new ComboBox<>();
        this.kategoriCB.getItems().addAll(kategoriItems);
        this.kategoriCB.setPrefSize(900, 40);
        this.kategoriCB.setStyle(
                 "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                // ... (style lainnya) ...
                // Warna teks di ComboBox mungkin perlu penyesuaian lebih lanjut via CSS
                "-fx-font-size: 15px"
        );


        this.deskripsiField = new TextField();
        this.deskripsiField.setPrefSize(900, 40);
         this.deskripsiField.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                // ... (style lainnya) ...
                "-fx-text-fill: rgb(193, 214, 200, 1);" +
                "-fx-font-size: 15px"
        );


        this.tanggalDP = new DatePicker();
        this.tanggalDP.setPrefSize(900, 40);
        this.tanggalDP.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                // ... (style lainnya) ...
                "-fx-font-size: 15px"
        );

        this.waktuField = new TextField();
        this.waktuField.setPromptText("HH:mm (contoh: 14:30)");
        this.waktuField.setPrefSize(900, 40);
        this.waktuField.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                // ... (style lainnya) ...
                "-fx-text-fill: rgb(193, 214, 200, 1);" +
                "-fx-font-size: 15px"
        );

        // Mengisi field jika dalam mode edit
        if (this.tugasToEdit != null) {
            this.topLbl.setText("EDIT JADWAL"); // Ubah judul halaman
            this.namaKegiatanField.setText(this.tugasToEdit.getJudul());
            this.kategoriCB.setValue(this.tugasToEdit.getKategori());
            if (this.tugasToEdit.getTanggalBatas() != null) {
                this.tanggalDP.setValue(this.tugasToEdit.getTanggalBatas().toLocalDate());
                this.waktuField.setText(this.tugasToEdit.getTanggalBatas().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            this.deskripsiField.setText(this.tugasToEdit.getDeskripsi());
            // TODO: Isi field lokasi dan mata kuliah jika ada
        } else {
            this.topLbl.setText("MASUKKAN JADWAL BARU"); // Judul untuk jadwal baru
        }

        // GRIDPANE UNTUK INPUT
        GridPane inputGridPane = new GridPane();
        inputGridPane.setHgap(200);
        inputGridPane.setVgap(50);
        inputGridPane.setAlignment(Pos.CENTER);

        inputGridPane.add(namaKegiatanLbl, 0, 0);
        inputGridPane.add(this.namaKegiatanField, 1, 0);
        inputGridPane.add(kategoriLbl, 0, 1);
        inputGridPane.add(this.kategoriCB, 1, 1);
        inputGridPane.add(tanggalLbl, 0, 2);
        inputGridPane.add(this.tanggalDP, 1, 2);
        inputGridPane.add(waktuLbl, 0, 3);
        inputGridPane.add(this.waktuField, 1, 3);
        inputGridPane.add(deskripsiLbl, 0, 4);
        inputGridPane.add(this.deskripsiField, 1, 4);
        // TODO: Tambahkan baris untuk Lokasi dan Mata Kuliah jika ada

        // TOMBOL
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

        batalBtn.setPrefSize(200, 40);

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
        simpanBtn.setPrefSize(200, 40);
        // simpanBtn.setTranslateY(300); // Sebaiknya gunakan layout manager
        // simpanBtn.setTranslateX(120);

        // LAYOUT UTAMA
        StackPane inputCard = new StackPane();
        inputCard.getChildren().addAll(persegi, inputGridPane);

        // Kontainer untuk tombol agar posisinya lebih teratur
        HBox tombolBox = new HBox(40, batalBtn, simpanBtn); // Spasi antar tombol
        tombolBox.setAlignment(Pos.CENTER);
        tombolBox.setPadding(new Insets(30, 0, 0, 0)); // Padding atas untuk tombol

        VBox centerContentVBox = new VBox(20, inputCard, tombolBox); // Gabungkan input card dan tombol
        centerContentVBox.setAlignment(Pos.CENTER);


        BorderPane borderPane1 = new BorderPane();
        borderPane1.setTop(topLblWrapper);
        borderPane1.setCenter(centerContentVBox); // Gunakan VBox yang sudah berisi tombol
        borderPane1.setPadding(new Insets(20));

        // BACKGROUND + LAYOUT
        this.getChildren().addAll(backgroundImage, borderPane1);

        // ADJUST BACKGROUND
        backgroundImage.fitHeightProperty().bind(this.heightProperty());
        backgroundImage.fitWidthProperty().bind(this.widthProperty());

        // BUTTON ACTION
        batalBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Batal");
            alert.setHeaderText("Anda yakin ingin membatalkan?");
            alert.setContentText(this.tugasToEdit != null ? "Perubahan tidak akan disimpan." : "Jadwal baru tidak akan dibuat.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                this.app.switchSceneSchedulePage();
            }
        });

        simpanBtn.setOnAction(e -> {
            handleSimpan(); // Panggil metode handleSimpan
        });
    }

    // Metode untuk menangani aksi simpan
    private void handleSimpan() {
        String namaKegiatan = this.namaKegiatanField.getText();
        String kategori = this.kategoriCB.getValue();
        LocalDate tanggal = this.tanggalDP.getValue();
        String waktuText = this.waktuField.getText();
        String deskripsi = this.deskripsiField.getText();
        // TODO: Ambil nilai dari field lokasi dan mata kuliah jika ada

        // Validasi Input Dasar
        if (namaKegiatan == null || namaKegiatan.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Nama Kegiatan tidak boleh kosong.");
            return;
        }
        if (kategori == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Kategori harus dipilih.");
            return;
        }
        if (tanggal == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Tanggal harus dipilih.");
            return;
        }

        LocalDateTime tanggalBatasGabungan = null;
        if (waktuText != null && !waktuText.trim().isEmpty()) {
            try {
                LocalTime waktu = LocalTime.parse(waktuText, DateTimeFormatter.ofPattern("HH:mm"));
                tanggalBatasGabungan = LocalDateTime.of(tanggal, waktu);
            } catch (DateTimeParseException ex) {
                showAlert(Alert.AlertType.ERROR, "Format Waktu Salah", "Gunakan format HH:mm untuk waktu (contoh: 09:30 atau 14:00).");
                return;
            }
        } else {
            // Jika waktu tidak diisi, default ke awal hari (00:00) atau bisa juga error
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Waktu harus diisi.");
            return;
            // atau: tanggalBatasGabungan = LocalDateTime.of(tanggal, LocalTime.MIDNIGHT);
        }

        boolean sukses;
        // Ganti string literal dengan input dari field jika sudah ada
        String lokasiInput = "Belum Diisi"; // Contoh, ambil dari TextField lokasiField jika ada
        String mataKuliahInput = ""; // Contoh, ambil dari TextField mataKuliahField jika ada

        // Konfirmasi sebelum menyimpan
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Simpan");
        confirmAlert.setHeaderText("Anda yakin ingin menyimpan jadwal ini?");
        confirmAlert.setContentText("Pastikan semua data sudah benar.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (tugasToEdit == null) { // Mode: Membuat tugas baru
                Tugas tugasBaruHasil = this.app.getPengelolaTugas().buatTugas(
                    namaKegiatan,
                    deskripsi,
                    tanggalBatasGabungan,
                    kategori,
                    lokasiInput, // Ganti dengan input field lokasi jika ada
                    mataKuliahInput // Ganti dengan input field matkul jika ada
                );
                sukses = (tugasBaruHasil != null);
            } else { // Mode: Mengedit tugas yang ada
                sukses = this.app.getPengelolaTugas().ubahTugas(
                    tugasToEdit.getId(),
                    namaKegiatan,
                    deskripsi,
                    tanggalBatasGabungan,
                    kategori,
                    lokasiInput, // Ganti dengan input field lokasi jika ada
                    mataKuliahInput, // Ganti dengan input field matkul jika ada
                    tugasToEdit.isSelesai() // Pertahankan status selesai, atau ambil dari UI jika ada CheckBox
                );
            }

            if (sukses) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data jadwal berhasil disimpan.");
                this.app.switchSceneSchedulePage(); // Kembali ke halaman jadwal
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan data jadwal. Periksa konsol untuk detail error.");
            }
        }
    }

    // Metode bantuan untuk menampilkan Alert
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}