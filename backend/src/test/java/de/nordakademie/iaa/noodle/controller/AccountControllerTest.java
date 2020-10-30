package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.model.AuthenticatedUser;
import de.nordakademie.iaa.noodle.services.SignInService;
import de.nordakademie.iaa.noodle.services.SignUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AccountControllerTest {
    private AccountController accountController;
    private SignInService signInService;
    private SignUpService signUpService;

    @BeforeEach
    public void setUp() {
        signInService = mock(SignInService.class);
        signUpService = mock(SignUpService.class);
        accountController = new AccountController(signUpService, signInService);
    }

    @Test
    public void testAuthenticate() {
        AuthenticationRequest authenticationRequest = mock(AuthenticationRequest.class);
        when(authenticationRequest.getEmail()).thenReturn("EMAIL");
        when(authenticationRequest.getPassword()).thenReturn("PASSWORD");

        User user = mock(User.class);
        when(user.getEmail()).thenReturn("EMAIL");
        when(user.getFullName()).thenReturn("FULL_NAME");

        AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);
        when(authenticatedUser.getJwtToken()).thenReturn("TOKEN");
        when(authenticatedUser.getUser()).thenReturn(user);

        when(signInService.attemptAuthentication("EMAIL", "PASSWORD")).thenReturn(Optional.of(authenticatedUser));

        ResponseEntity<AuthenticatedResponse> response = accountController.authenticate(authenticationRequest);
        AuthenticatedResponse authenticatedResponse = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(authenticatedResponse);
        assertEquals("TOKEN", authenticatedResponse.getToken());
        assertEquals("FULL_NAME", authenticatedResponse.getName());
        assertEquals("EMAIL", authenticatedResponse.getEmail());
    }

    @Test
    public void testCreateUser() {
        User user = mock(User.class);
        when(signUpService.createAccount("TOKEN", "PASSWORD")).thenReturn(Optional.of(user));
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
    public void testRequestRegistrationEmail() {
        RequestRegistrationEmailRequest requestRegistrationEmailRequest = mock(RequestRegistrationEmailRequest.class);
        when(requestRegistrationEmailRequest.getName()).thenReturn("FULL_NAME");
        when(requestRegistrationEmailRequest.getEmail()).thenReturn("EMAIL");

        ResponseEntity<RequestRegistrationEmailResponse> response = accountController.requestRegistrationEmail(requestRegistrationEmailRequest);
        RequestRegistrationEmailResponse requestRegistrationEmailResponse = response.getBody();

        verify(signUpService).mailSignupToken("EMAIL", "FULL_NAME");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(requestRegistrationEmailResponse);
        assertEquals("EMAIL", requestRegistrationEmailResponse.getEmail());
        assertEquals("FULL_NAME", requestRegistrationEmailResponse.getName());
    }
}
