package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.AuthenticationApi;
import de.nordakademie.iaa.noodle.api.model.AuthenticatedResponse;
import de.nordakademie.iaa.noodle.api.model.AuthenticationRequest;
import de.nordakademie.iaa.noodle.api.model.CreateUserRequest;
import de.nordakademie.iaa.noodle.api.model.CreateUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController implements AuthenticationApi {
    @Override
    public ResponseEntity<AuthenticatedResponse> authenticate(AuthenticationRequest authenticationRequest) {
        return null;
    }

    @Override
    public ResponseEntity<CreateUserResponse> createUser(CreateUserRequest createUserRequest) {
        return null;
    }
}
