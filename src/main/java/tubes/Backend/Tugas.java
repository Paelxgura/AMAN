package tubes.Backend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tugas {
    private int id; // ID dari database
    private String judul;
    private String deskripsi;
    private LocalDateTime tanggalBatas;
    private String kategori;
    private String lokasi;
    private String mataKuliah;
    private boolean selesai;
    private int userId; // Foreign key ke tabel users

    // Konstruktor untuk tugas baru (ID belum ada)
    public Tugas(String judul, String deskripsi, LocalDateTime tanggalBatas, String kategori, String lokasi, String mataKuliah, int userId) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggalBatas = tanggalBatas;
        this.kategori = kategori;
        this.lokasi = lokasi;
        this.mataKuliah = mataKuliah;
        this.selesai = false;
        this.userId = userId;
    }

    // Konstruktor untuk tugas yang diambil dari DB (ID sudah ada)
    public Tugas(int id, String judul, String deskripsi, LocalDateTime tanggalBatas, String kategori, String lokasi, String mataKuliah, boolean selesai, int userId) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggalBatas = tanggalBatas;
        this.kategori = kategori;
        this.lokasi = lokasi;
        this.mataKuliah = mataKuliah;
        this.selesai = selesai;
        this.userId = userId;
    }


    // Getters
    public int getId() { return id; }
    public String getJudul() { return judul; }
    public String getDeskripsi() { return deskripsi; }
    public LocalDateTime getTanggalBatas() { return tanggalBatas; }
    public String getKategori() { return kategori; }
    public String getLokasi() { return lokasi; }
    public String getMataKuliah() { return mataKuliah; }
    public boolean isSelesai() { return selesai; }
    public int getUserId() { return userId; }

    // Setters
    public void setId(int id) { this.id = id; } // Untuk set ID setelah insert ke DB
    public void setJudul(String judul) { this.judul = judul; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public void setTanggalBatas(LocalDateTime tanggalBatas) { this.tanggalBatas = tanggalBatas; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }
    public void setMataKuliah(String mataKuliah) { this.mataKuliah = mataKuliah; }
    public void setSelesai(boolean selesai) { this.selesai = selesai; }
    // userId biasanya tidak diubah setelah tugas dibuat

    public String getTanggalBatasFormatted() {
        if (tanggalBatas == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        return tanggalBatas.format(formatter);
    }


    public String buatLaporan() {
        return String.format("Tugas: %s\nDeskripsi: %s\nBatas Waktu: %s\nKategori: %s\nStatus: %s",
                judul, deskripsi, (tanggalBatas != null ? tanggalBatas.toString() : "N/A"), kategori, selesai ? "Selesai" : "Belum Selesai");
    }

    @Override
    public String toString() {
        return "Tugas{" +
                "id=" + id +
                ", judul='" + judul + '\'' +
                ", userId=" + userId +
                '}';
    }
}