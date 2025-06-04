package tubes.Backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PengelolaTugas {
    private User currentUser;

    public PengelolaTugas() {
        this.currentUser = null;
    }

    // --- User Management ---

    /**
     * Mendaftarkan akun pengguna baru ke database.
     * PENTING: Password harus di-hash sebelum disimpan di aplikasi produksi.
     * @param username Username.
     * @param email Email.
     * @param sandi Password (plain text, akan disimpan apa adanya untuk contoh ini).
     * @return Objek User jika berhasil, null jika gagal (misal username/email sudah ada).
     */
    public User daftarAkun(String username, String email, String sandi) {
        // Di aplikasi nyata, HASH passwordnya DI SINI sebelum disimpan
        // Contoh: String hashedPassword = PasswordHasher.hash(sandi);
        // Kemudian simpan hashedPassword, bukan sandi.

        String sqlCheck = "SELECT id FROM users WHERE username = ? OR email = ?";
        String sqlInsert = "INSERT INTO users (username, email, sandi) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmtCheck = null;
        ResultSet rsCheck = null;
        PreparedStatement pstmtInsert = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseManager.getConnection();
            if (conn == null) return null;

            // 1. Cek apakah username atau email sudah ada
            pstmtCheck = conn.prepareStatement(sqlCheck);
            pstmtCheck.setString(1, username);
            pstmtCheck.setString(2, email);
            rsCheck = pstmtCheck.executeQuery();
            if (rsCheck.next()) {
                System.out.println("Username atau email sudah terdaftar.");
                return null; // Username atau email sudah ada
            }
            DatabaseManager.closeResultSet(rsCheck); // Tutup ResultSet dari pengecekan
            DatabaseManager.closeStatement(pstmtCheck); // Tutup PreparedStatement dari pengecekan


            // 2. Jika belum ada, lakukan insert
            pstmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            pstmtInsert.setString(1, username);
            pstmtInsert.setString(2, email);
            pstmtInsert.setString(3, sandi); // Simpan HASHED password, bukan plain text

            int affectedRows = pstmtInsert.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = pstmtInsert.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newUserId = generatedKeys.getInt(1);
                    User newUser = new User(newUserId, username, email, sandi);
                    System.out.println("User " + username + " berhasil terdaftar dengan ID: " + newUserId);
                    return newUser;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mendaftarkan akun: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResultSet(rsCheck); // Pastikan ditutup lagi di finally
            DatabaseManager.closeStatement(pstmtCheck); // Pastikan ditutup lagi di finally
            DatabaseManager.closeResultSet(generatedKeys);
            DatabaseManager.closeStatement(pstmtInsert);
            DatabaseManager.closeConnection(conn);
        }
        return null;
    }

    /**
     * Melakukan login pengguna.
     * @param usernameOrEmail Username atau Email pengguna.
     * @param sandiInput Password yang dimasukkan pengguna.
     * @return true jika login berhasil, false jika tidak.
     */
    public boolean masukSistem(String usernameOrEmail, String sandiInput) {
        String sql = "SELECT id, username, email, sandi FROM users WHERE username = ? OR email = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usernameOrEmail); // Bisa username
            pstmt.setString(2, usernameOrEmail); // Atau email

            rs = pstmt.executeQuery();

            if (rs.next()) {
                String sandiDariDB = rs.getString("sandi");
                // Di aplikasi nyata: if (PasswordHasher.verify(sandiInput, sandiDariDB))
                if (sandiDariDB.equals(sandiInput)) { // Ini TIDAK AMAN untuk produksi
                    this.currentUser = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            sandiDariDB // Simpan sandi (hash) dari DB ke objek User
                    );
                    // Muat tugas untuk user yang login setelah berhasil login
                    this.currentUser.setDaftarTugas(getTugasByUserId(this.currentUser.getId(), conn));
                    System.out.println("User " + this.currentUser.getUsername() + " berhasil login.");
                    return true;
                } else {
                    System.out.println("Password salah untuk user: " + usernameOrEmail);
                }
            } else {
                System.out.println("Username/Email tidak ditemukan: " + usernameOrEmail);
            }
        } catch (SQLException e) {
            System.err.println("Error saat login: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResultSet(rs);
            DatabaseManager.closeStatement(pstmt);
            DatabaseManager.closeConnection(conn); // Koneksi ditutup setelah selesai operasi login
        }
        this.currentUser = null; // Pastikan currentUser null jika login gagal
        return false;
    }

    public void logout() {
        if (this.currentUser != null) {
            System.out.println("User " + this.currentUser.getUsername() + " logout.");
        }
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // --- Task Management ---

    /**
     * Membuat tugas baru dan menyimpannya ke database.
     * @return Objek Tugas jika berhasil, null jika gagal.
     */
    public Tugas buatTugas(String judul, String deskripsi, LocalDateTime tanggalBatas, String kategori, String lokasi, String mataKuliah) {
        if (currentUser == null) {
            System.err.println("Tidak ada user yang login untuk membuat tugas.");
            return null;
        }

        String sql = "INSERT INTO tasks (user_id, judul, deskripsi, tanggal_batas, kategori, lokasi, mata_kuliah, selesai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseManager.getConnection();
            if (conn == null) return null;

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, currentUser.getId());
            pstmt.setString(2, judul);
            pstmt.setString(3, deskripsi);
            pstmt.setTimestamp(4, (tanggalBatas != null) ? Timestamp.valueOf(tanggalBatas) : null);
            pstmt.setString(5, kategori);
            pstmt.setString(6, lokasi);
            pstmt.setString(7, mataKuliah);
            pstmt.setBoolean(8, false); // Default 'selesai' adalah false untuk tugas baru

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newTaskId = generatedKeys.getInt(1);
                    Tugas tugasBaru = new Tugas(newTaskId, currentUser.getId(), judul, deskripsi, tanggalBatas, kategori, lokasi, mataKuliah, false);
                    if (this.currentUser.getDaftarTugas() != null) { // Pastikan list tidak null
                        this.currentUser.tambahTugas(tugasBaru);
                    }
                    System.out.println("Tugas '" + judul + "' berhasil dibuat untuk user " + currentUser.getUsername() + " dengan ID: " + newTaskId);
                    return tugasBaru;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat membuat tugas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResultSet(generatedKeys);
            DatabaseManager.closeStatement(pstmt);
            DatabaseManager.closeConnection(conn);
        }
        return null;
    }

    /**
     * Mengubah data tugas yang ada di database.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean ubahTugas(int idTugas, String judul, String deskripsi, LocalDateTime tanggalBatas, String kategori, String lokasi, String mataKuliah, boolean selesai) {
        if (currentUser == null) {
            System.err.println("Tidak ada user yang login untuk mengubah tugas.");
            return false;
        }

        String sql = "UPDATE tasks SET judul = ?, deskripsi = ?, tanggal_batas = ?, kategori = ?, " +
                     "lokasi = ?, mata_kuliah = ?, selesai = ? WHERE id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, judul);
            pstmt.setString(2, deskripsi);
            pstmt.setTimestamp(3, (tanggalBatas != null) ? Timestamp.valueOf(tanggalBatas) : null);
            pstmt.setString(4, kategori);
            pstmt.setString(5, lokasi);
            pstmt.setString(6, mataKuliah);
            pstmt.setBoolean(7, selesai);
            pstmt.setInt(8, idTugas);
            pstmt.setInt(9, currentUser.getId()); // Pastikan hanya tugas milik user yang diubah

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Update juga di list currentUser jika perlu
                if (this.currentUser.getDaftarTugas() != null) {
                    this.currentUser.getDaftarTugas().stream()
                        .filter(t -> t.getId() == idTugas)
                        .findFirst()
                        .ifPresent(t -> {
                            t.setJudul(judul);
                            t.setDeskripsi(deskripsi);
                            t.setTanggalBatas(tanggalBatas);
                            t.setKategori(kategori);
                            t.setLokasi(lokasi);
                            t.setMataKuliah(mataKuliah);
                            t.setSelesai(selesai);
                        });
                }
                System.out.println("Tugas ID " + idTugas + " berhasil diubah.");
                return true;
            } else {
                System.err.println("Gagal mengubah tugas: Tugas ID " + idTugas + " tidak ditemukan atau bukan milik user.");
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengubah tugas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeStatement(pstmt);
            DatabaseManager.closeConnection(conn);
        }
        return false;
    }
    
    /**
     * Menghapus tugas dari database.
     * @param idTugas ID tugas yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean hapusTugas(int idTugas) {
        if (currentUser == null) {
            System.err.println("Tidak ada user yang login untuk menghapus tugas.");
            return false;
        }

        String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.getConnection();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idTugas);
            pstmt.setInt(2, currentUser.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                if (this.currentUser.getDaftarTugas() != null) {
                    this.currentUser.hapusTugas(idTugas);
                }
                System.out.println("Tugas ID " + idTugas + " berhasil dihapus.");
                return true;
            } else {
                 System.err.println("Gagal menghapus tugas: Tugas ID " + idTugas + " tidak ditemukan atau bukan milik user.");
            }
        } catch (SQLException e) {
            System.err.println("Error saat menghapus tugas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeStatement(pstmt);
            DatabaseManager.closeConnection(conn);
        }
        return false;
    }

    /**
     * Mengambil semua tugas milik user yang sedang login dari database.
     * @return List Tugas.
     */
    public List<Tugas> getTugasCurrentUser() {
        if (currentUser == null) {
            return new ArrayList<>();
        }
        // Selalu ambil dari DB untuk data terbaru
        List<Tugas> tugasDariDB = getTugasByUserId(currentUser.getId(), null);
        // Update list di objek currentUser agar sinkron
        currentUser.setDaftarTugas(tugasDariDB);
        return tugasDariDB;
    }

    /**
     * Metode helper untuk mengambil tugas berdasarkan userId.
     * Jika existingConn null, akan membuat koneksi baru dan menutupnya.
     * Jika existingConn ada, tidak akan menutup koneksi tersebut (berguna untuk transaksi atau operasi berantai).
     */
    private List<Tugas> getTugasByUserId(int userId, Connection existingConn) {
        List<Tugas> daftarTugasUser = new ArrayList<>();
        String sql = "SELECT id, judul, deskripsi, tanggal_batas, kategori, lokasi, mata_kuliah, selesai " +
                     "FROM tasks WHERE user_id = ? ORDER BY tanggal_batas ASC, id DESC"; // Urutkan
        
        Connection conn = existingConn;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean closeConnHere = false; // Flag apakah koneksi dibuat di metode ini

        try {
            if (conn == null || conn.isClosed()) { // Jika tidak ada koneksi aktif, buat baru
                conn = DatabaseManager.getConnection();
                if (conn == null) return daftarTugasUser; // Kembalikan list kosong jika koneksi gagal
                closeConnHere = true;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("tanggal_batas");
                LocalDateTime tanggalBatas = (ts != null) ? ts.toLocalDateTime() : null;
                Tugas tugas = new Tugas(
                        rs.getInt("id"),
                        userId, // user_id sudah pasti sama dengan parameter
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        tanggalBatas,
                        rs.getString("kategori"),
                        rs.getString("lokasi"),
                        rs.getString("mata_kuliah"),
                        rs.getBoolean("selesai")
                );
                daftarTugasUser.add(tugas);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil tugas user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResultSet(rs);
            DatabaseManager.closeStatement(pstmt);
            if (closeConnHere) { // Hanya tutup koneksi jika dibuat di metode ini
                DatabaseManager.closeConnection(conn);
            }
        }
        return daftarTugasUser;
    }

    /**
     * Mengambil tugas user yang sedang login, difilter berdasarkan kategori.
     * @param kategoriFilter Nama kategori yang dicari. "Semua" akan mengambil semua kategori.
     * @return List Tugas yang sudah difilter.
     */
    public List<Tugas> getTugasCurrentUserByKategori(String kategoriFilter) {
        if (currentUser == null) {
            return new ArrayList<>();
        }
        if (kategoriFilter == null || kategoriFilter.equalsIgnoreCase("Semua") || kategoriFilter.trim().isEmpty()) {
            return getTugasCurrentUser(); // Ambil semua tugas jika filter "Semua" atau kosong
        }

        List<Tugas> daftarTugasUser = new ArrayList<>();
        String sql = "SELECT id, judul, deskripsi, tanggal_batas, kategori, lokasi, mata_kuliah, selesai " +
                     "FROM tasks WHERE user_id = ? AND kategori = ? ORDER BY tanggal_batas ASC, id DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.getConnection();
            if (conn == null) return daftarTugasUser;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentUser.getId());
            pstmt.setString(2, kategoriFilter);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("tanggal_batas");
                LocalDateTime tanggalBatas = (ts != null) ? ts.toLocalDateTime() : null;
                Tugas tugas = new Tugas(
                        rs.getInt("id"),
                        currentUser.getId(),
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        tanggalBatas,
                        rs.getString("kategori"),
                        rs.getString("lokasi"),
                        rs.getString("mata_kuliah"),
                        rs.getBoolean("selesai")
                );
                daftarTugasUser.add(tugas);
            }
            // Tidak perlu update currentUser.setDaftarTugas() di sini karena ini hanya view terfilter
        } catch (SQLException e) {
            System.err.println("Error saat mengambil tugas berdasarkan kategori '" + kategoriFilter + "': " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResultSet(rs);
            DatabaseManager.closeStatement(pstmt);
            DatabaseManager.closeConnection(conn);
        }
        return daftarTugasUser;
    }

    /**
     * Menghitung jumlah tugas per kategori untuk user yang sedang login.
     * @return Map dengan Kategori sebagai key dan jumlah sebagai value.
     */
    public Map<String, Long> rekapKategoriCurrentUser() {
        if (currentUser == null) {
            return new HashMap<>();
        }
        // Ambil data terbaru dari DB untuk rekap
        List<Tugas> tugasUserSaatIni = getTugasByUserId(currentUser.getId(), null);
        if (tugasUserSaatIni.isEmpty()) {
            return new HashMap<>();
        }
        return tugasUserSaatIni.stream()
                .filter(t -> t.getKategori() != null) // Hindari NullPointerException jika kategori bisa null
                .collect(Collectors.groupingBy(Tugas::getKategori, Collectors.counting()));
    }
}