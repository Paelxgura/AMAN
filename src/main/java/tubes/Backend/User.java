package tubes.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
// Tidak ada import java.sql di sini, karena User adalah Entitas/POJO

public class User {
    private int id; // ID dari database
    private String username;
    private String email;
    private String sandi; // Ini adalah HASH password di aplikasi nyata
    private List<Tugas> daftarTugas; // Daftar tugas yang dimuat dari DB

    // Konstruktor untuk user baru yang belum ada di DB (ID belum ada)
    public User(String username, String email, String sandi) {
        this.username = username;
        this.email = email;
        this.sandi = sandi;
        this.daftarTugas = new ArrayList<>();
    }

    // Konstruktor untuk user yang diambil dari DB (ID sudah ada)
    public User(int id, String username, String email, String sandi) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.sandi = sandi; // Ini adalah HASH password dari DB
        this.daftarTugas = new ArrayList<>();
    }


    // Getters
    public int getId() {
        return id;
    }

    public void setId(int id) { // Setter untuk ID setelah disimpan ke DB dan mendapatkan generated key
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<Tugas> getDaftarTugas() {
        return new ArrayList<>(daftarTugas);
    }

    public void setDaftarTugas(List<Tugas> daftarTugas) {
        this.daftarTugas = new ArrayList<>(daftarTugas);
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSandi(String sandi) { // Seharusnya menerima hash password
        this.sandi = sandi;
    }

    public void tambahTugas(Tugas tugas) {
        if (tugas != null && tugas.getUserId() == this.id) {
            this.daftarTugas.add(tugas);
        }
    }

    public boolean hapusTugas(int tugasId) {
        return this.daftarTugas.removeIf(t -> t.getId() == tugasId);
    }

    public boolean verifikasiSandi(String inputSandi) {
        // Jika menggunakan hashing:
        // return PasswordHasher.verify(inputSandi, this.sandi);
        return Objects.equals(this.sandi, inputSandi); // Perbandingan plain text (TIDAK AMAN)
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}