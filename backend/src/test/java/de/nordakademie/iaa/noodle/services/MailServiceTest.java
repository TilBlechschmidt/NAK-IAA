package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.services.exceptions.MailClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MailServiceTest {
    private JavaMailSender mailSender;
    private MailService mailService;

    @BeforeEach
    public void setUp() {
        mailSender = mock(JavaMailSender.class);
        mailService = new MailService(mailSender);
        ReflectionTestUtils.setField(mailService, "fromEmail", "FROM_EMAIL");
    }

    @Test
    public void sendRegistrationMail() throws MailClientException {
        mailService.sendRegistrationMail("TOKEN", "FULL_NAME", "EMAIL");

        verify(mailSender).send(argThat((SimpleMailMessage simpleMailMessage) -> {
            String[] toArray = { "EMAIL" };

            assertEquals("FROM_EMAIL", simpleMailMessage.getFrom());
            assertArrayEquals(toArray, simpleMailMessage.getTo());
            assertEquals("Your registration", simpleMailMessage.getSubject());
            assertEquals("""
                Hello FULL_NAME!
                Thank you for your registration. Below, you can find your registration token:

                TOKEN

                Best regards
                Your team @Noodle""",
                simpleMailMessage.getText());
            return true;
        }));
    }

    @Test
    public void sendRegistrationMailDuplicateEmail() throws MailClientException {
        mailService.sendRegistrationMailDuplicateEmail("FULL_NAME", "EMAIL");

        verify(mailSender).send(argThat((SimpleMailMessage simpleMailMessage) -> {
            String[] toArray = { "EMAIL" };

            assertEquals("FROM_EMAIL", simpleMailMessage.getFrom());
            assertArrayEquals(toArray, simpleMailMessage.getTo());
            assertEquals("Your registration", simpleMailMessage.getSubject());
            assertEquals("""
                Hello FULL_NAME!
                Thank you for your registration. We have to inform you, that you already have an account with this email. Please try to sign in using this email.

                Best regards
                Your team @Noodle""",
                simpleMailMessage.getText());
            return true;
        }));
    }
}
