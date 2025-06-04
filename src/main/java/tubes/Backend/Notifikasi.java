package tubes.Backend;

import java.time.LocalDateTime;

/**
 * Handles sending notifications, primarily email reminders for tasks.
 */
public class Notifikasi {
    private EmailService pengirim; // The EmailService to use for sending emails

    /**
     * Constructs a Notifikasi service.
     *
     * @param emailService The EmailService instance to be used.
     */
    public Notifikasi(EmailService emailService) {
        this.pengirim = emailService;
    }

    /**
     * Schedules a reminder (placeholder).
     * In a real app, this would integrate with a scheduling library (e.g., Quartz Scheduler)
     * or OS-level cron jobs to trigger `kirimPengingat` at the appropriate time.
     *
     * @param tugas The task for which to schedule a reminder.
     * @param user The user who owns the task.
     * @param waktuPengingat The time at which the reminder should be sent.
     */
    public void jadwalPengingat(Tugas tugas, User user, LocalDateTime waktuPengingat) {
        System.out.println(String.format("MENJADWALKAN PENGINGAT (Placeholder) untuk tugas '%s' milik %s pada %s",
                tugas.getJudul(), user.getUsername(), waktuPengingat.toString()));
        // Logic to schedule a job that calls kirimPengingat at waktuPengingat
    }

    /**
     * Sends a reminder email for a specific task to a user.
     *
     * @param tugas The task to remind about.
     * @param user The user to remind.
     * @return true if the reminder was sent successfully, false otherwise.
     */
    public boolean kirimPengingat(Tugas tugas, User user) {
        if (user == null || tugas == null) {
            System.err.println("User atau Tugas tidak boleh null untuk mengirim pengingat.");
            return false;
        }

        String emailTo = user.getEmail();
        String subjek = "Pengingat Tugas: " + tugas.getJudul();
        String isiEmail = String.format(
                "Halo %s,\n\nIni adalah pengingat untuk tugas Anda:\n" +
                "Judul: %s\n" +
                "Deskripsi: %s\n" +
                "Batas Waktu: %s\n" +
                "Kategori: %s\n" +
                (tugas.getLokasi() != null && !tugas.getLokasi().isEmpty() ? "Lokasi: " + tugas.getLokasi() + "\n" : "") +
                "\nSegera selesaikan tugas Anda!\n\nSalam,\nAplikasi AMAN",
                user.getUsername(),
                tugas.getJudul(),
                tugas.getDeskripsi(),
                tugas.getTanggalBatas().toString(),
                tugas.getKategori()
        );

        System.out.println("Mempersiapkan pengingat untuk tugas: " + tugas.getJudul() + " kepada user: " + user.getUsername());
        return pengirim.kirimEmail(emailTo, subjek, isiEmail);
    }
}