package tubes.Backend;

import java.util.Map;

/**
 * Service for sending emails.
 * Placeholder implementation.
 */
public class EmailService {
    private Map<String, String> smtpConfig; // e.g., host, port, username, password

    /**
     * Constructs an EmailService with SMTP configuration.
     *
     * @param smtpConfig A map containing SMTP server settings.
     */
    public EmailService(Map<String, String> smtpConfig) {
        this.smtpConfig = smtpConfig;
        System.out.println("EmailService diinisialisasi dengan config: " + smtpConfig);
    }

    /**
     * Sends an email.
     *
     * @param to The recipient's email address.
     * @param subjek The subject of the email.
     * @param isi The body content of the email.
     * @return true if email was "sent" successfully (placeholder), false otherwise.
     */
    public boolean kirimEmail(String to, String subjek, String isi) {
        System.out.println("--- MENGIRIM EMAIL (Placeholder) ---");
        System.out.println("Kepada: " + to);
        System.out.println("Subjek: " + subjek);
        System.out.println("Isi: " + isi);
        System.out.println("SMTP Config Digunakan: " + smtpConfig);
        System.out.println("------------------------------------");
        // Actual email sending logic using JavaMail API or similar would go here.
        // This would involve connecting to an SMTP server.
        return true; // Placeholder
    }
}