package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.AuthenticationException;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.JWTException;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;
import de.nordakademie.iaa.noodle.services.model.AuthenticatedUser;
import de.nordakademie.iaa.noodle.services.model.SpringAuthenticationDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link SignInService}
 *
 * @author Noah Peeters
 */
class SignInServiceTest {
    private SignInService signInService;
    private PasswordService passwordService;
    private UserService userService;
    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        passwordService = mock(PasswordService.class);
        userService = mock(UserService.class);
        jwtService = mock(JWTService.class);

        signInService = new SignInService(passwordService, userService, jwtService);
    }

    @Test
    void testAttemptAuthenticationUserNotFound() throws EntityNotFoundException {
        when(userService.getUserByEMail("EMAIL")).thenThrow(new EntityNotFoundException("userNotFound"));

        EntityNotFoundException exception =
            assertThrows(EntityNotFoundException.class, () -> signInService.attemptAuthentication("EMAIL", "PASSWORD"));

        assertEquals("userNotFound", exception.getMessage());
    }

    @Test
    void testAttemptAuthenticationInvalidPassword() throws EntityNotFoundException {
        User user = mock(User.class);

        when(userService.getUserByEMail("EMAIL")).thenReturn(user);
        when(passwordService.isPasswordCorrect(user, "PASSWORD")).thenReturn(false);

        PasswordException exception = assertThrows(PasswordException.class,
            () -> signInService.attemptAuthentication("EMAIL", "PASSWORD"));

        assertEquals("invalidPassword", exception.getMessage());
    }

    @Test
    void testAttemptAuthenticationSuccess() throws EntityNotFoundException, PasswordException {
        User user = mock(User.class);

        when(userService.getUserByEMail("EMAIL")).thenReturn(user);
        when(passwordService.isPasswordCorrect(user, "PASSWORD")).thenReturn(true);
        when(jwtService.buildSpringAuthenticationToken(user)).thenReturn("TOKEN");

        AuthenticatedUser authenticatedUser = signInService.attemptAuthentication("EMAIL", "PASSWORD");
        assertEquals(user, authenticatedUser.getUser());
        assertEquals("Bearer TOKEN", authenticatedUser.getJwtToken());
    }

    @Test
    void testSpringAuthenticationForHeaderInvalidHeader() {
        AuthenticationException exception = assertThrows(AuthenticationException.class,
            () -> signInService.springAuthenticationForHeader("BAD_TOKEN"));

        assertEquals("invalidHeaderFormat", exception.getMessage());
    }

    @Test
    void testSpringAuthenticationForHeader() throws JWTException, AuthenticationException, EntityNotFoundException {
        SpringAuthenticationDetails authenticationDetails = mock(SpringAuthenticationDetails.class);
        User user = mock(User.class);

        when(jwtService.authenticationDetailsForToken("TOKEN")).thenReturn(authenticationDetails);
        when(authenticationDetails.getUserID()).thenReturn(42L);
        when(userService.getUserByUserID(42L)).thenReturn(user);
        when(user.getFullName()).thenReturn("FULL_NAME");
        when(authenticationDetails.getAuthorities()).thenReturn(Collections.emptyList());

        Authentication authentication = signInService.springAuthenticationForHeader("Bearer TOKEN");

        assertEquals(user, authentication.getDetails());
        assertNull(authentication.getCredentials());
        assertEquals("FULL_NAME", authentication.getPrincipal());
    }
}
