package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.services.exceptions.MailClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender emailSender;

    @Value("${spring.noodle.mail.from:mail@example.com}")
    private String fromEmail;

    @Autowired
    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    private static String greeting(String fullName) {
        return "Hello " + fullName + "!\n" + "Thank you for your registration. ";
    }

    private static String regards() {
        return "\n\nBest regards\nYour team @Noodle";
    }

    private static String assembleContent(String fullName, String body) {
        return greeting(fullName) + body + regards();
    }

    public void sendRegistrationMail(String token, String fullName, String email) throws MailClientException {
        String body = "Below, you can find your registration token:\n\n" + token;
        sendMail(body, fullName, email);
    }

    public void sendRegistrationMailDuplicateEmail(String fullName, String email) throws MailClientException {
        String body = "We have to inform you, that you already have an account with this email." +
            " Please try to sign in using this email.";
        sendMail(body, fullName, email);
    }

    public void sendMail(String body, String fullName, String email) throws MailClientException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Your registration");
            message.setText(assembleContent(fullName, body));
            emailSender.send(message);
        } catch (MailException e) {
            throw new MailClientException("mailError");
        }
    }
}
