package de.nordakademie.iaa.noodle.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {
    private final JavaMailSender emailSender;

    @Value("${spring.noodle.mail.from:mail@example.com}")
    private String fromEmail;

    @Autowired
    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendRegistrationMail(String token, String fullName, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Your registration");
        message.setText("Hello " + fullName + "!\n" + "Thank you for your registration. Below, you can find your registration token:\n\n" + token + "\n\nBest regards\nYour team @Noodle");
        emailSender.send(message);
    }

    public void sendRegistrationMailDuplicateEmail(String fullName, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Your registration");
        message.setText("Hello " + fullName + "!\n" + "Thank you for your registration. We have to inform you, that you already have an account with this email. Please try to sign in using this email.\n\nBest regards\nYour team @Noodle");
        emailSender.send(message);
    }
}
