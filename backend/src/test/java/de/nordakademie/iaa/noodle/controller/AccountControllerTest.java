package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.*;
import de.nordakademie.iaa.noodle.services.implementation.SignInServiceImpl;
import de.nordakademie.iaa.noodle.services.implementation.SignUpServiceImpl;
import de.nordakademie.iaa.noodle.services.interfaces.SignInService;
import de.nordakademie.iaa.noodle.services.interfaces.SignUpService;
import de.nordakademie.iaa.noodle.services.model.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.nordakademie.iaa.noodle.TestUtil.assertThrowsResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Test for {@link AccountController}
 *
 * @author Hans Rißer
 * @author Noah Peeters
 */
public class AccountControllerTest {
    private AccountController accountController;
    private SignInService signInService;
    private SignUpService signUpService;

    @BeforeEach
    public void setUp() {
        signInService = mock(SignInServiceImpl.class);
        signUpService = mock(SignUpServiceImpl.class);
        accountController = new AccountController(signUpService, signInService);
    }

    @Test
    void testAuthenticateWrongPassword() throws PasswordException, EntityNotFoundException {
        when(signInService.attemptAuthentication(any(), any()))
            .thenThrow(new PasswordException("testAuthenticatePassword"));
        AuthenticationRequest inputDTO = mock(AuthenticationRequest.class);
        assertThrowsResponseStatusException(HttpStatus.UNAUTHORIZED, "invalidAuthenticationData",
            () -> accountController.authenticate(inputDTO));

    }

    @Test
    void testAuthenticateNotFound() throws PasswordException, EntityNotFoundException {
        when(signInService.attemptAuthentication(any(), any()))
            .thenThrow(new EntityNotFoundException("testAuthenticateNotFound"));
        AuthenticationRequest inputDTO = mock(AuthenticationRequest.class);
        assertThrowsResponseStatusException(HttpStatus.UNAUTHORIZED, "invalidAuthenticationData",
            () -> accountController.authenticate(inputDTO));
    }

    @Test
    void testAuthenticate() throws PasswordException, EntityNotFoundException {
        AuthenticationRequest authenticationRequest = mock(AuthenticationRequest.class);
        when(authenticationRequest.getEmail()).thenReturn("EMAIL");
        when(authenticationRequest.getPassword()).thenReturn("PASSWORD");

        User user = mock(User.class);
        when(user.getEmail()).thenReturn("EMAIL");
        when(user.getFullName()).thenReturn("FULL_NAME");

        AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);
        when(authenticatedUser.getJwtToken()).thenReturn("TOKEN");
        when(authenticatedUser.getUser()).thenReturn(user);

        when(signInService.attemptAuthentication("EMAIL", "PASSWORD")).thenReturn(authenticatedUser);

        ResponseEntity<AuthenticatedResponse> response = accountController.authenticate(authenticationRequest);
        AuthenticatedResponse authenticatedResponse = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(authenticatedResponse);
        assertEquals("TOKEN", authenticatedResponse.getToken());
        assertEquals("FULL_NAME", authenticatedResponse.getName());
        assertEquals("EMAIL", authenticatedResponse.getEmail());
    }

    @Test
    void testCreateUserJWTError() throws ConflictException, JWTException, PasswordException {
        when(signUpService.createAccount(any(), any())).thenThrow(new JWTException("testCreateUserJWT"));
        ActivateUserRequest activateUserRequest = mock(ActivateUserRequest.class);
        assertThrowsResponseStatusException(HttpStatus.UNAUTHORIZED, "testCreateUserJWT",
            () -> accountController.activateUser(activateUserRequest));
    }

    @Test
    void testCreateUserConflict() throws ConflictException, JWTException, PasswordException {
        when(signUpService.createAccount(any(), any())).thenThrow(new ConflictException("testCreateUserConflict"));
        ActivateUserRequest activateUserRequest = mock(ActivateUserRequest.class);
        assertThrowsResponseStatusException(HttpStatus.CONFLICT, "testCreateUserConflict",
            () -> accountController.activateUser(activateUserRequest));
    }

    @Test
    void testCreateUserUnprocessableEntity() throws ConflictException, JWTException, PasswordException {
        when(signUpService.createAccount(any(), any()))
            .thenThrow(new PasswordException("testCreateUserUnprocessableEntity"));
        ActivateUserRequest activateUserRequest = mock(ActivateUserRequest.class);
        assertThrowsResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "testCreateUserUnprocessableEntity",
            () -> accountController.activateUser(activateUserRequest));
    }

    @Test
    void testCreateUser() throws ConflictException, JWTException, PasswordException {
        User user = mock(User.class);
        when(signUpService.createAccount("TOKEN", "PASSWORD")).thenReturn(user);
        when(user.getId()).thenReturn(42L);
        when(user.getFullName()).thenReturn("FULL_NAME");
        when(user.getEmail()).thenReturn("EMAIL");

        ActivateUserRequest activateUserRequest = mock(ActivateUserRequest.class);
        when(activateUserRequest.getToken()).thenReturn("TOKEN");
        when(activateUserRequest.getPassword()).thenReturn("PASSWORD");

        ResponseEntity<ActivateUserResponse> response = accountController.activateUser(activateUserRequest);
        ActivateUserResponse activateUserResponse = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(activateUserResponse);
        assertEquals(42, activateUserResponse.getId());
        assertEquals("FULL_NAME", activateUserResponse.getName());
        assertEquals("EMAIL", activateUserResponse.getEmail());
    }

    @Test
    void testRequestRegistrationMailError() throws MailClientException {
        doThrow(new MailClientException("testMailError")).when(signUpService).mailSignupToken(any(), any());
        RequestRegistrationEmailRequest requestRegistrationEmailRequest = mock(RequestRegistrationEmailRequest.class);

        assertThrowsResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "testMailError",
            () -> accountController.requestRegistrationEmail(requestRegistrationEmailRequest));
    }

    @Test
    void testRequestRegistrationEmail() throws MailClientException {
        RequestRegistrationEmailRequest requestRegistrationEmailRequest = mock(RequestRegistrationEmailRequest.class);
        when(requestRegistrationEmailRequest.getName()).thenReturn("FULL_NAME");
        when(requestRegistrationEmailRequest.getEmail()).thenReturn("EMAIL");

        ResponseEntity<RequestRegistrationEmailResponse> response =
            accountController.requestRegistrationEmail(requestRegistrationEmailRequest);
        RequestRegistrationEmailResponse requestRegistrationEmailResponse = response.getBody();

        verify(signUpService).mailSignupToken("EMAIL", "FULL_NAME");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(requestRegistrationEmailResponse);
        assertEquals("EMAIL", requestRegistrationEmailResponse.getEmail());
        assertEquals("FULL_NAME", requestRegistrationEmailResponse.getName());
    }
}
