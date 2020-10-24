package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

public class AuthenticationControllerTest {
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        authenticationController = new AuthenticationController();
    }


    @Test
    public void testAuthenticate() {
        AuthenticationRequest authenticationRequest = mock(AuthenticationRequest.class);
        ResponseEntity<AuthenticatedResponse> response = authenticationController.authenticate(authenticationRequest);
        assertNull(response);
    }

    @Test
    public void testCreateUser() {
        CreateUserRequest createUserRequest = mock(CreateUserRequest.class);
        ResponseEntity<CreateUserResponse> response = authenticationController.createUser(createUserRequest);
        assertNull(response);
    }
}
