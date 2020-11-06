package de.nordakademie.iaa.noodle.services;

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
    public void sendRegistrationMailTest() throws MailClientException, MessagingException {
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
    public void sendRegistrationMailDuplicateEmailTest() throws MailClientException, MessagingException {
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
    public void sendMailTest() throws MailClientException, MessagingException {
        mailService.sendMail("SUBJECT", "BODY", "EMAIL");

        verify(mimeMessage, times(1)).setFrom("FROM_EMAIL");
        verify(mimeMessage, times(1)).setRecipients(Message.RecipientType.TO, "EMAIL");
        verify(mimeMessage, times(1)).setSubject("SUBJECT");
        verify(mimeMessage, times(1)).setText("BODY", null, "html");
    }

    @Test
    public void sendMailFailedTest() {
        doThrow(mock(MailException.class))
            .when(mailSender)
            .send(any(MimeMessage.class));

        MailClientException exception = assertThrows(MailClientException.class,
            () -> mailService.sendMail("SUBJECT", "BODY", "EMAIL"));

        assertEquals("mailError", exception.getMessage());
    }
}
