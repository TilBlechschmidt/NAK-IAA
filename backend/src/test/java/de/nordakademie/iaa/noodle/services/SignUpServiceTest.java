package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.ConflictException;
import de.nordakademie.iaa.noodle.services.exceptions.JWTException;
import de.nordakademie.iaa.noodle.services.exceptions.MailClientException;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;
import de.nordakademie.iaa.noodle.services.model.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignUpServiceTest {
    private SignUpService signUpService;
    private PasswordService passwordService;
    private UserService userService;
    private JWTService jwtService;
    private MailService mailService;

    @BeforeEach
    void setUp() {
        passwordService = mock(PasswordService.class);
        userService = mock(UserService.class);
        jwtService = mock(JWTService.class);
        mailService = mock(MailService.class);
        signUpService = new SignUpService(passwordService, userService, jwtService, mailService);
    }

    @Test
    void createAccountTest() throws JWTException, ConflictException, PasswordException {
        UserDetails userDetails = mock(UserDetails.class);
        User user = mock(User.class);
        when(jwtService.userDetailsForToken("TOKEN")).thenReturn(userDetails);
        when(passwordService.hashPassword("PASSWORD")).thenReturn("PASSWORD_HASH");
        when(userService.createNewUser("EMAIL", "FULL_NAME", "PASSWORD_HASH")).thenReturn(user);
        when(userDetails.getEmail()).thenReturn("EMAIL");
        when(userDetails.getFullName()).thenReturn("FULL_NAME");

        User createdUser = signUpService.createAccount("TOKEN", "PASSWORD");
        assertEquals(user, createdUser);
    }

    @Test
    void mailSignupTokenDuplicateTest() throws MailClientException {
        when(userService.existsUserWithEMail("EMAIL")).thenReturn(true);
        signUpService.mailSignupToken("EMAIL", "FULL_NAME");

        verify(mailService, times(1))
            .sendRegistrationMailDuplicateEmail("FULL_NAME", "EMAIL");
    }

    @Test
    void mailSignupTokenTest() throws MailClientException {
        when(userService.existsUserWithEMail("EMAIL")).thenReturn(false);
        when(jwtService.buildEmailToken("EMAIL", "FULL_NAME")).thenReturn("TOKEN");
        signUpService.mailSignupToken("EMAIL", "FULL_NAME");

        verify(mailService, times(1))
            .sendRegistrationMail("TOKEN", "FULL_NAME", "EMAIL");
    }
}
