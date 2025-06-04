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
import javafx.scene.text.TextAlignment; // Ditambahkan jika belum ada
import tubes.backend.Tugas;
import tubes.launch.mainApp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

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
    // Tambahkan field UI lain jika ada, misal untuk lokasi atau mata kuliah

    // KONSTRUKTOR YANG DIPERBARUI
    public EditSchedule(mainApp app, Tugas tugas) { // Terima mainApp dan Tugas
        this.app = app;
        this.tugasToEdit = tugas; // Ini bisa null jika membuat tugas baru

        // Background
        String backgroundString = getClass().getResource("/Background.png").toString();
        ImageView backgroundImage = new ImageView(new Image(backgroundString));

        // TOP
        this.topLbl = new Label(); // Inisialisasi field kelas
        this.topLbl.setFont(Font.font("Franklin Gothic Heavy", FontWeight.BOLD, FontPosture.ITALIC, 90));
        this.topLbl.setTextFill(Color.rgb(193, 214, 200, 1));
        this.topLbl.setTextAlignment(TextAlignment.CENTER);

        HBox topLblWrapper = new HBox(this.topLbl);
        topLblWrapper.setAlignment(Pos.CENTER);

        Rectangle persegi = new Rectangle(1400, 600);
        persegi.setStyle(
                "-fx-fill: #FFFFFF;" +
                "-fx-opacity: 0.35;" +
                "-fx-arc-width: 25;" + // Koreksi typo (Width -> width)
                "-fx-arc-height: 25;" // Koreksi typo (Height -> height)
        );

        // LABELS (bisa tetap variabel lokal jika tidak diakses di luar konstruktor)
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

        // FIELDS (inisialisasi field kelas)
        this.namaKegiatanField = new TextField();
        this.namaKegiatanField.setPrefSize(900, 40);
        this.namaKegiatanField.setStyle(
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
        // Anda bisa menambahkan kategori lain di sini jika perlu

        this.kategoriCB = new ComboBox<>();
        this.kategoriCB.getItems().addAll(kategoriItems);
        this.kategoriCB.setPrefSize(900, 40);
        this.kategoriCB.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                "-fx-background-opacity: 0.35;" +
                "-fx-border-color: transparent;" +
                "-fx-border-radius: 5;" +
                // Untuk ComboBox, warna teks item dan teks terpilih mungkin memerlukan styling lebih lanjut
                // via CSS atau setCellFactory dan setButtonCell jika ingin kustomisasi penuh.
                // "-fx-text-fill: rgb(193, 214, 200, 1);" // Ini mungkin tidak berpengaruh banyak di ComboBox
                "-fx-font-size: 15px"
        );
        // Pastikan ComboBox menampilkan teks dengan warna yang terlihat pada background gelapnya


        this.deskripsiField = new TextField();
        this.deskripsiField.setPrefSize(900, 40);
        this.deskripsiField.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                "-fx-background-opacity: 0.35;" +
                "-fx-border-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-text-fill: rgb(193, 214, 200, 1);" +
                "-fx-font-size: 15px"
        );

        this.tanggalDP = new DatePicker();
        this.tanggalDP.setPrefSize(900, 40);
        this.tanggalDP.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                "-fx-background-opacity: 0.35;" +
                "-fx-border-color: transparent;" +
                "-fx-border-radius: 5;" +
                // "-fx-text-fill: rgb(193, 214, 200, 1);" // Warna teks di DatePicker juga diatur oleh tema/CSS internal
                "-fx-font-size: 15px"
        );
        // Untuk DatePicker, warna teks juga bisa diatur lebih detail melalui CSS


        this.waktuField = new TextField();
        this.waktuField.setPromptText("HH:mm (contoh: 14:30)"); // Memberi petunjuk format
        this.waktuField.setPrefSize(900, 40);
        this.waktuField.setStyle(
                "-fx-background-color: rgb(0, 6, 18, 0.35);" +
                "-fx-background-opacity: 0.35;" +
                "-fx-border-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-text-fill: rgb(193, 214, 200, 1);" +
                "-fx-font-size: 15px"
        );

        // Mengisi field jika dalam mode edit
        if (this.tugasToEdit != null) {
            this.topLbl.setText("EDIT JADWAL"); // Ubah judul halaman
            this.namaKegiatanField.setText(this.tugasToEdit.getJudul());
            this.kategoriCB.setValue(this.tugasToEdit.getKategori()); // Pastikan kategori ada di ComboBox
            if (this.tugasToEdit.getTanggalBatas() != null) {
                this.tanggalDP.setValue(this.tugasToEdit.getTanggalBatas().toLocalDate());
                this.waktuField.setText(this.tugasToEdit.getTanggalBatas().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            this.deskripsiField.setText(this.tugasToEdit.getDeskripsi());
            // TODO: Jika ada field untuk lokasi, mata kuliah, isi juga di sini
            // Contoh: if (this.lokasiField != null) this.lokasiField.setText(this.tugasToEdit.getLokasi());
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
        // TODO: Tambahkan baris untuk Lokasi dan Mata Kuliah jika ada field inputnya

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
        // Penempatan tombol dengan translate mungkin perlu penyesuaian atau dilakukan dengan layout manager yang lebih baik
        // Untuk sekarang kita biarkan, tapi idealnya gunakan HBox atau VBox di dalam BorderPane.
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
        simpanBtn.setPrefSize(200, 40);
        simpanBtn.setTranslateY(300);
        simpanBtn.setTranslateX(120);

        // LAYOUT UTAMA
        StackPane inputCard = new StackPane(); // Ini membungkus persegi dan grid input
        inputCard.getChildren().addAll(persegi, inputGridPane);

        // Kontainer untuk tombol agar posisinya lebih teratur relatif terhadap inputCard
        // Jika ingin tombol di bawah inputCard, bisa gunakan VBox atau BorderPane.setBottom
        // Untuk sementara, kita letakkan tombol di atas inputCard dalam StackPane utama halaman ini
        // karena `EditSchedule extends StackPane`. Perlu hati-hati agar tombol tidak menutupi input.
        // Jika `batalBtn` dan `simpanBtn` diletakkan di `stackPane` asli, mereka akan di tengah.
        // Mungkin lebih baik membuat HBox untuk tombol dan meletakkannya di bawah inputCard
        // dalam sebuah VBox yang menjadi child dari BorderPane.

        // Menggunakan BorderPane untuk struktur halaman yang lebih baik
        BorderPane contentPane = new BorderPane();
        contentPane.setCenter(inputCard);

        // HBox untuk tombol-tombol agar berdampingan
        HBox tombolBox = new HBox(20, batalBtn, simpanBtn); // 20 adalah spasi antar tombol
        tombolBox.setAlignment(Pos.CENTER);
        tombolBox.setPadding(new Insets(0, 0, 50, 0)); // Padding bawah untuk tombol

        contentPane.setBottom(tombolBox); // Letakkan tombol di bawah input card

        BorderPane borderPane1 = new BorderPane();
        borderPane1.setTop(topLblWrapper);
        borderPane1.setCenter(contentPane); // Ganti stackPane dengan contentPane
        borderPane1.setPadding(new Insets(20));

        // BACKGROUND + LAYOUT
        this.getChildren().addAll(backgroundImage, borderPane1); // 'this' adalah StackPane root

        // ADJUST BACKGROUND
        backgroundImage.fitHeightProperty().bind(this.heightProperty());
        backgroundImage.fitWidthProperty().bind(this.widthProperty());

        // BUTTON ACTION
        batalBtn.setOnAction(e -> {
            this.app.switchSceneSchedulePage();
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
        // TODO: Ambil nilai dari field lokasi dan mata kuliah jika sudah Anda tambahkan

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
            // Jika waktu tidak diisi, default ke awal hari (00:00)
            tanggalBatasGabungan = LocalDateTime.of(tanggal, LocalTime.MIDNIGHT);
        }

        boolean sukses;
        // Ganti string literal dengan input dari field jika sudah ada
        String lokasiInput = "Belum Diisi"; // Contoh, ambil dari TextField lokasiField jika ada
        String mataKuliahInput = ""; // Contoh, ambil dari TextField mataKuliahField jika ada

        if (tugasToEdit == null) { // Mode: Membuat tugas baru
            Tugas tugasBaruHasil = this.app.getPengelolaTugas().buatTugas(
                namaKegiatan,
                deskripsi,
                tanggalBatasGabungan,
                kategori,
                lokasiInput,
                mataKuliahInput
            );
            sukses = (tugasBaruHasil != null);
        } else { // Mode: Mengedit tugas yang ada
            sukses = this.app.getPengelolaTugas().ubahTugas(
                tugasToEdit.getId(),
                namaKegiatan,
                deskripsi,
                tanggalBatasGabungan,
                kategori,
                lokasiInput,
                mataKuliahInput,
                tugasToEdit.isSelesai() // Pertahankan status selesai, atau ambil dari UI jika ada CheckBox
            );
        }

        if (sukses) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data jadwal berhasil disimpan.");
            this.app.switchSceneSchedulePage();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan data jadwal. Periksa konsol untuk detail error.");
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