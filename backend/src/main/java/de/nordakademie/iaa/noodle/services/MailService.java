package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.MailClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.springframework.web.util.HtmlUtils.htmlEscape;

/**
 * Service to send mails to the user.
 */
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

    private static final String NEEDS_ATTENTION_TEMPLATE = """
        Hello ${participant_name}!<br/>
        ${creator_name} modified the survey
        <a href=${baseurl}/surveys/${survey_id}>${survey_title}</a>.
        Please create a new response.
        """;

    private final JavaMailSender emailSender;
    private final String fromEmail;
    private final String baseURL;

    /**
     * Creates a new MailService.
     * @param emailSender The JavaMailSender used to send the mails.
     * @param fromEmail The email used to send the mails from.
     * @param baseURL The base url of the noodle service. This will be used for links in the mails.
     */
    @Autowired
    public MailService(JavaMailSender emailSender,
                       @Value("${spring.noodle.mail.from}") String fromEmail,
                       @Value("${spring.noodle.baseurl}") String baseURL) {
        this.emailSender = emailSender;
        this.fromEmail = fromEmail;
        this.baseURL = baseURL;
    }

    /**
     * Sends a mail with the registration token.
     * @param token The registration token.
     * @param fullName The name of the user.
     * @param email The email of the user.
     * @throws MailClientException Thrown, when the mail cannot be send.
     */
    public void sendRegistrationMail(String token, String fullName, String email) throws MailClientException {
        String body = fillTemplate(REGISTRATION_TEMPLATE, Map.ofEntries(
            entry("name", fullName),
            entry("baseurl", baseURL),
            entry("token", token)
        ));
        sendMail("Your registration", body, email);
    }

    /**
     * Send a mail to a user, when there is already an account with the given email.
     * @param fullName The name of the user.
     * @param email The email of the user.
     * @throws MailClientException Thrown, when the mail cannot be send.
     */
    public void sendRegistrationMailDuplicateEmail(String fullName, String email) throws MailClientException {
        String body = fillTemplate(REGISTRATION_DUPLICATE_TEMPLATE, Map.ofEntries(
            entry("name", fullName)
        ));
        sendMail("Registration failed", body, email);
    }

    /**
     * Sends a mail.
     * @param subject The subject of the mail.
     * @param body The body of the mail.
     * @param email The email the mail will be send to.
     * @throws MailClientException Thrown, when the mail cannot be send.
     */
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

    /**
     * Sends a mail to every user to inform them, that their participation is stale.
     * @param survey The survey the participations belong to.
     * @param participants The participants who must creat a new response.
     */
    public void sendNeedsAttentionMailsAsync(Survey survey, List<User> participants) {
        new Thread(() -> {
            participants.forEach(user -> {
                try {
                    sendNeedsAttentionMail(survey, user);
                } catch (MailClientException e) {
                    System.err.println("Failed to send needs attention mail for " + user.getEmail());
                }
            });
        }).start();
    }

    /**
     * Sends a mail to a user to inform them, that their participation is stale.
     * @param survey The survey the participations belong to.
     * @param participant The user with the stale response.
     * @throws MailClientException Thrown, when the mail cannot be send.
     */
    public void sendNeedsAttentionMail(Survey survey, User participant) throws MailClientException {
        String body = fillTemplate(NEEDS_ATTENTION_TEMPLATE, Map.ofEntries(
            entry("participant_name", participant.getFullName()),
            entry("creator_name", survey.getCreator().getFullName()),
            entry("survey_id", survey.getId().toString()),
            entry("survey_title", survey.getTitle()),
            entry("baseurl", baseURL)
        ));
        sendMail("Survey update", body, participant.getEmail());
    }

    private String fillTemplate(String template, Map<String, String> parameter) {
        String result = template + FOOTER;

        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", htmlEscape(entry.getValue()));
        }

        return result;
    }
}
