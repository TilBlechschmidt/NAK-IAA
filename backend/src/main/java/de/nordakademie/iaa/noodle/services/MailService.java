package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.services.exceptions.MailClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

import static java.util.Map.entry;
import static org.springframework.web.util.HtmlUtils.htmlEscape;

@Service
public class MailService {
    private static final String FOOTER = """
        <br/><br/>
        Best regards<br/>
        Your team @Noodle
        """;

    private static final String REGISTRATION_TEMPLATE = """
        Hello ${name}!<br/>
        Thank you for your registration. Please click
        <a href="${baseurl}/activate?token=${token}">here</a>
        to complete the registration.
        """;

    private static final String REGISTRATION_DUPLICATE_TEMPLATE = """
        Hello ${name}!<br/>
        We have to inform you, that you already have an account with this email.
        Please try to sign in using this email.
        """;

    private final JavaMailSender emailSender;
    private final String fromEmail;
    private final String baseURL;

    @Autowired
    public MailService(JavaMailSender emailSender,
                       @Value("${spring.noodle.mail.from}") String fromEmail,
                       @Value("${spring.noodle.baseurl}") String baseURL) {
        this.emailSender = emailSender;
        this.fromEmail = fromEmail;
        this.baseURL = baseURL;
    }

    public void sendRegistrationMail(String token, String fullName, String email) throws MailClientException {
        String body = fillTemplate(REGISTRATION_TEMPLATE, Map.ofEntries(
            entry("name", fullName),
            entry("baseurl", baseURL),
            entry("token", token)
        ));
        sendMail("Your registration", body, email);
    }

    public void sendRegistrationMailDuplicateEmail(String fullName, String email) throws MailClientException {
        String body = fillTemplate(REGISTRATION_DUPLICATE_TEMPLATE, Map.ofEntries(
            entry("name", fullName)
        ));
        sendMail("Registration failed", body, email);
    }

    public void sendMail(String subject, String body, String email) throws MailClientException {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            message.setFrom(fromEmail);
            message.setRecipients(Message.RecipientType.TO, email);
            message.setSubject(subject);
            message.setText(body, null, "html");
            emailSender.send(message);
        } catch (MailException | MessagingException e) {
            throw new MailClientException("mailError");
        }
    }

    private String fillTemplate(String template, Map<String, String> parameter) {
        String result = template + FOOTER;

        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", htmlEscape(entry.getValue()));
        }

        return result;
    }
}
