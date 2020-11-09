package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordServiceTest {
    private User user;
    private PasswordService passwordService;

    @BeforeEach
    public void setUp() {
        passwordService = new PasswordService("pepper");
        user = mock(User.class);

        when(user.getPasswordHash()).thenReturn("$2a$10$Ls0kE/AydHiuaHFdZXJXBuPpp4O51XC2vt.qfk4RlfVLn8OCry4aG");
    }

    @Test
    public void hashPassword() throws PasswordException {
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode("password1pepper")).thenReturn("hash_password1");

        ReflectionTestUtils.setField(passwordService, "passwordEncoder", passwordEncoder);
        String hash = passwordService.hashPassword("password1");
        assertEquals("hash_password1", hash);
    }

    @Test
    public void testHashPasswordTooShort() {
        PasswordException exception = assertThrows(PasswordException.class,
            () -> passwordService.hashPassword("a"));
        assertEquals("passwordDoesNotMatchRules", exception.getMessage());
    }

    @Test
    public void testHashPasswordTooLong() {
        PasswordException exception = assertThrows(PasswordException.class,
            () -> passwordService.hashPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        assertEquals("passwordDoesNotMatchRules", exception.getMessage());
    }

    @Test
    public void isPasswordCorrect() {
        boolean isCorrect = passwordService.isPasswordCorrect(user, "password1");
        assertTrue(isCorrect);
    }

    @Test
    public void isPasswordCorrectWrongPassword() {
        boolean isCorrect = passwordService.isPasswordCorrect(user, "password2");
        assertFalse(isCorrect);
    }
}
