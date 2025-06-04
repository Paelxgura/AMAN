package tubes.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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


    public User daftarAkun(String username, String email, String sandi) {


        String sqlCheck = "SELECT id FROM users WHERE username = ? OR email = ?";
        String sqlInsert = "INSERT INTO users (username, email, sandi) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmtCheck = null;
        ResultSet rsCheck = null;
        PreparedStatement pstmtInsert = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseManager.getConnection();
            if (conn == null) {
                System.err.println("Daftar akun gagal: Tidak bisa mendapatkan koneksi database.");
                return null;
            }

            // 1. Cek apakah username atau email sudah ada
            pstmtCheck = conn.prepareStatement(sqlCheck);
            pstmtCheck.setString(1, username);
            pstmtCheck.setString(2, email);
            rsCheck = pstmtCheck.executeQuery();
            if (rsCheck.next()) {
                System.out.println("Username atau email sudah terdaftar.");
                return null; 
            }
            // Penting untuk menutup resource setelah digunakan, bahkan sebelum try-finally utama
            DatabaseManager.closeResultSet(rsCheck); 
            rsCheck = null; // Set ke null setelah ditutup
            DatabaseManager.closeStatement(pstmtCheck);
            pstmtCheck = null; // Set ke null setelah ditutup


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
            System.err.println("Error SQL saat mendaftarkan akun: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Pastikan semua resource yang mungkin dibuka ditutup
            DatabaseManager.closeResultSet(rsCheck); 
            DatabaseManager.closeStatement(pstmtCheck); 
            DatabaseManager.closeResultSet(generatedKeys);
            DatabaseManager.closeStatement(pstmtInsert);
            DatabaseManager.closeConnection(conn);
        }
        return null;
    }

    public boolean masukSistem(String usernameOrEmail, String sandiInput) {
        String sql = "SELECT id, username, email, sandi FROM users WHERE username = ? OR email = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.getConnection();
            if (conn == null) {
                System.err.println("Login gagal: Tidak bisa mendapatkan koneksi database.");
                return false;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usernameOrEmail); 
            pstmt.setString(2, usernameOrEmail); 

            rs = pstmt.executeQuery();

            if (rs.next()) {
                String sandiDariDB = rs.getString("sandi");
                // Di aplikasi nyata: if (PasswordHasher.verify(sandiInput, sandiDariDB))
                if (sandiDariDB.equals(sandiInput)) { // Ini TIDAK AMAN untuk produksi
                    this.currentUser = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            sandiDariDB 
                    );
                    // Muat tugas untuk user yang login setelah berhasil login
                    // Melewatkan 'conn' ke getTugasByUserId agar tidak membuka koneksi baru jika tidak perlu
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
            System.err.println("Error SQL saat login: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResultSet(rs);
            DatabaseManager.closeStatement(pstmt);
            // Koneksi ditutup setelah selesai operasi login (atau setelah getTugasByUserId jika dipass)
            // Jika getTugasByUserId menggunakan koneksi yang sama, koneksi ditutup oleh pemanggil terluar.
            // Untuk kasus ini, karena getTugasByUserId dipanggil di sini, kita tutup koneksi di sini.
            DatabaseManager.closeConnection(conn); 
        }
        this.currentUser = null; 
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
            if (conn == null) {
                 System.err.println("Buat tugas gagal: Tidak bisa mendapatkan koneksi database.");
                return null;
            }

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, currentUser.getId());
            pstmt.setString(2, judul);
            pstmt.setString(3, deskripsi);

            // PENYESUAIAN UNTUK SQLITE: LocalDateTime -> TEXT (String ISO-8601)
            if (tanggalBatas != null) {
                pstmt.setString(4, tanggalBatas.toString()); 
            } else {
                pstmt.setNull(4, java.sql.Types.VARCHAR); 
            }

            pstmt.setString(5, kategori);
            pstmt.setString(6, lokasi);
            pstmt.setString(7, mataKuliah);

            // PENYESUAIAN UNTUK SQLITE: boolean -> INTEGER (0 untuk false, 1 untuk true)
            pstmt.setInt(8, 0); // Default 'selesai' adalah false (0) untuk tugas baru

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newTaskId = generatedKeys.getInt(1);
                    // Pastikan konstruktor Tugas sesuai
                    Tugas tugasBaru = new Tugas(newTaskId, currentUser.getId(), judul, deskripsi, tanggalBatas, kategori, lokasi, mataKuliah, false);
                    if (this.currentUser.getDaftarTugas() != null) { 
                        this.currentUser.tambahTugas(tugasBaru);
                    }
                    System.out.println("Tugas '" + judul + "' berhasil dibuat untuk user " + currentUser.getUsername() + " dengan ID: " + newTaskId);
                    return tugasBaru;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL saat membuat tugas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResultSet(generatedKeys);
            DatabaseManager.closeStatement(pstmt);
            DatabaseManager.closeConnection(conn);
        }
        return null;
    }

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
            if (conn == null) {
                System.err.println("Ubah tugas gagal: Tidak bisa mendapatkan koneksi database.");
                return false;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, judul);
            pstmt.setString(2, deskripsi);

            // PENYESUAIAN UNTUK SQLITE: LocalDateTime -> TEXT
            if (tanggalBatas != null) {
                pstmt.setString(3, tanggalBatas.toString());
            } else {
                pstmt.setNull(3, java.sql.Types.VARCHAR);
            }

            pstmt.setString(4, kategori);
            pstmt.setString(5, lokasi);
            pstmt.setString(6, mataKuliah);

            // PENYESUAIAN UNTUK SQLITE: boolean -> INTEGER
            pstmt.setInt(7, selesai ? 1 : 0); 

            pstmt.setInt(8, idTugas);
            pstmt.setInt(9, currentUser.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
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
            System.err.println("Error SQL saat mengubah tugas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeStatement(pstmt);
            DatabaseManager.closeConnection(conn);
        }
        return false;
    }
    
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
            if (conn == null) {
                System.err.println("Hapus tugas gagal: Tidak bisa mendapatkan koneksi database.");
                return false;
            }

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
            System.err.println("Error SQL saat menghapus tugas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeStatement(pstmt);
            DatabaseManager.closeConnection(conn);
        }
        return false;
    }

    public List<Tugas> getTugasCurrentUser() {
        if (currentUser == null) {
            return new ArrayList<>();
        }
        List<Tugas> tugasDariDB = getTugasByUserId(currentUser.getId(), null);
        currentUser.setDaftarTugas(tugasDariDB);
        return tugasDariDB;
    }

    private List<Tugas> getTugasByUserId(int userId, Connection existingConn) {
        List<Tugas> daftarTugasUser = new ArrayList<>();
        // Pastikan kolom user_id ada di SELECT jika akan digunakan dari ResultSet
        String sql = "SELECT id, user_id, judul, deskripsi, tanggal_batas, kategori, lokasi, mata_kuliah, selesai " +
                     "FROM tasks WHERE user_id = ? ORDER BY tanggal_batas ASC, id DESC";
        
        Connection conn = existingConn;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean closeConnHere = false; 

        try {
            if (conn == null || conn.isClosed()) { 
                conn = DatabaseManager.getConnection();
                if (conn == null) {
                    System.err.println("Ambil tugas gagal: Tidak bisa mendapatkan koneksi database.");
                    return daftarTugasUser;
                }
                closeConnHere = true;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // PENYESUAIAN UNTUK SQLITE: TEXT (String ISO-8601) -> LocalDateTime
                String tanggalBatasStr = rs.getString("tanggal_batas");
                LocalDateTime tglBatasObj = null;
                if (tanggalBatasStr != null && !tanggalBatasStr.isEmpty()) {
                    try {
                        tglBatasObj = LocalDateTime.parse(tanggalBatasStr); 
                    } catch (DateTimeParseException e) {
                        System.err.println("Format tanggal_batas tidak valid di DB: " + tanggalBatasStr + " untuk tugas id: " + rs.getInt("id"));
                    }
                }
                
                // PENYESUAIAN UNTUK SQLITE: INTEGER (0/1) -> boolean
                boolean statusSelesai = (rs.getInt("selesai") == 1); 

                Tugas tugas = new Tugas(
                        rs.getInt("id"),
                        rs.getInt("user_id"), // Ambil user_id dari ResultSet
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        tglBatasObj, 
                        rs.getString("kategori"),
                        rs.getString("lokasi"),
                        rs.getString("mata_kuliah"),
                        statusSelesai 
                );
                daftarTugasUser.add(tugas);
            }
        } catch (SQLException e) {
            System.err.println("Error SQL saat mengambil tugas user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResultSet(rs);
            DatabaseManager.closeStatement(pstmt);
            if (closeConnHere) { 
                DatabaseManager.closeConnection(conn);
            }
        }
        return daftarTugasUser;
    }

    public List<Tugas> getTugasCurrentUserByKategori(String kategoriFilter) {
        if (currentUser == null) {
            return new ArrayList<>();
        }
        if (kategoriFilter == null || kategoriFilter.equalsIgnoreCase("Semua") || kategoriFilter.trim().isEmpty()) {
            return getTugasCurrentUser(); 
        }

        List<Tugas> daftarTugasUser = new ArrayList<>();
        String sql = "SELECT id, user_id, judul, deskripsi, tanggal_batas, kategori, lokasi, mata_kuliah, selesai " +
                     "FROM tasks WHERE user_id = ? AND kategori = ? ORDER BY tanggal_batas ASC, id DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.getConnection();
            if (conn == null) {
                System.err.println("Ambil tugas by kategori gagal: Tidak bisa mendapatkan koneksi database.");
                return daftarTugasUser;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentUser.getId());
            pstmt.setString(2, kategoriFilter);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String tanggalBatasStr = rs.getString("tanggal_batas");
                LocalDateTime tglBatasObj = null;
                if (tanggalBatasStr != null && !tanggalBatasStr.isEmpty()) {
                     try {
                        tglBatasObj = LocalDateTime.parse(tanggalBatasStr);
                    } catch (DateTimeParseException e) {
                        System.err.println("Format tanggal_batas tidak valid di DB: " + tanggalBatasStr + " untuk tugas id: " + rs.getInt("id"));
                    }
                }
                boolean statusSelesai = (rs.getInt("selesai") == 1);

                Tugas tugas = new Tugas(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("judul"),
                        rs.getString("deskripsi"),
                        tglBatasObj,
                        rs.getString("kategori"),
                        rs.getString("lokasi"),
                        rs.getString("mata_kuliah"),
                        statusSelesai
                );
                daftarTugasUser.add(tugas);
            }
        } catch (SQLException e) {
            System.err.println("Error SQL saat mengambil tugas berdasarkan kategori '" + kategoriFilter + "': " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResultSet(rs);
            DatabaseManager.closeStatement(pstmt);
            DatabaseManager.closeConnection(conn);
        }
        return daftarTugasUser;
    }

    public Map<String, Long> rekapKategoriCurrentUser() {
        if (currentUser == null) {
            return new HashMap<>();
        }
        List<Tugas> tugasUserSaatIni = getTugasByUserId(currentUser.getId(), null); // Ambil data terbaru
        if (tugasUserSaatIni.isEmpty()) {
            return new HashMap<>();
        }
        return tugasUserSaatIni.stream()
                .filter(t -> t.getKategori() != null) 
                .collect(Collectors.groupingBy(Tugas::getKategori, Collectors.counting()));
    }
}