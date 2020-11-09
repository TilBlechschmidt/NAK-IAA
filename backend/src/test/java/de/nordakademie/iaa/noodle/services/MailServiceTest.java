package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.MailClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MailServiceTest {
    private JavaMailSender mailSender;
    private MailService mailService;
    private MimeMessage mimeMessage;

    @BeforeEach
    public void setUp() {
        mailSender = mock(JavaMailSender.class);
        mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        mailService = new MailService(mailSender, "FROM_EMAIL", "BASEURL");
    }

    @Test
    void testSendRegistrationMail() throws MailClientException, MessagingException {
        mailService.sendRegistrationMail("TOKEN", "FULL_NAME", "EMAIL");

        String body = """
            Hello FULL_NAME!<br/>
            Thank you for your registration. Please click
            <a href="BASEURL/activate?token=TOKEN">here</a>
            to complete the registration.
            <br/><br/>
            Best regards<br/>
            Your team @Noodle
            """;

        verify(mimeMessage, times(1)).setFrom("FROM_EMAIL");
        verify(mimeMessage, times(1)).setRecipients(Message.RecipientType.TO, "EMAIL");
        verify(mimeMessage, times(1)).setSubject("Your registration");
        verify(mimeMessage, times(1)).setText(body, null, "html");
    }

    @Test
    void testSendRegistrationMailDuplicateEmail() throws MailClientException, MessagingException {
        mailService.sendRegistrationMailDuplicateEmail("FULL_NAME", "EMAIL");

        String body = """
            Hello FULL_NAME!<br/>
            We have to inform you, that you already have an account with this email.
            Please try to sign in using this email.
            <br/><br/>
            Best regards<br/>
            Your team @Noodle
            """;

        verify(mimeMessage, times(1)).setFrom("FROM_EMAIL");
        verify(mimeMessage, times(1)).setRecipients(Message.RecipientType.TO, "EMAIL");
        verify(mimeMessage, times(1)).setSubject("Registration failed");
        verify(mimeMessage, times(1)).setText(body, null, "html");
    }

    @Test
    void testSendNeedsAttentionMail() throws MailClientException, MessagingException {
        Survey survey = mock(Survey.class);
        User participant = mock(User.class);
        User creator = mock(User.class);

        when(survey.getCreator()).thenReturn(creator);
        when(survey.getTitle()).thenReturn("TITLE");
        when(survey.getId()).thenReturn(42L);
        when(creator.getFullName()).thenReturn("CREATOR_FULL_NAME");
        when(participant.getFullName()).thenReturn("PARTICIPANT_FULL_NAME");
        when(participant.getEmail()).thenReturn("EMAIL");

        mailService.sendNeedsAttentionMail(survey, participant);

        String body = """
            Hello PARTICIPANT_FULL_NAME!<br/>
            CREATOR_FULL_NAME modified the survey
            <a href=BASEURL/surveys/42>TITLE</a>.
            Please create a new response.
            <br/><br/>
            Best regards<br/>
            Your team @Noodle
            """;

        verify(mimeMessage, times(1)).setFrom("FROM_EMAIL");
        verify(mimeMessage, times(1)).setRecipients(Message.RecipientType.TO, "EMAIL");
        verify(mimeMessage, times(1)).setSubject("Survey update");
        verify(mimeMessage, times(1)).setText(body, null, "html");
    }

    @Test
    void testSendMail() throws MailClientException, MessagingException {
        mailService.sendMail("SUBJECT", "BODY", "EMAIL");

        verify(mimeMessage, times(1)).setFrom("FROM_EMAIL");
        verify(mimeMessage, times(1)).setRecipients(Message.RecipientType.TO, "EMAIL");
        verify(mimeMessage, times(1)).setSubject("SUBJECT");
        verify(mimeMessage, times(1)).setText("BODY", null, "html");
    }

    @Test
    void testSendMailFailed() {
        doThrow(mock(MailException.class))
            .when(mailSender)
            .send(any(MimeMessage.class));

        MailClientException exception = assertThrows(MailClientException.class,
            () -> mailService.sendMail("SUBJECT", "BODY", "EMAIL"));

        assertEquals("mailError", exception.getMessage());
    }
}
