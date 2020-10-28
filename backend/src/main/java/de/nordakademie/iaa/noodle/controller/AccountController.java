package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.AccountApi;
import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.filter.NoodleException;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.AuthenticatedUser;
import de.nordakademie.iaa.noodle.services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class AccountController implements AccountApi {
    private final JWTService jwtService;

    @Autowired
    public AccountController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<ActivateUserResponse> activateUser(ActivateUserRequest activateUserRequest) {
        String token = activateUserRequest.getToken();
        String password = activateUserRequest.getPassword();

        return jwtService.createAccount(token, password)
            .map(this::responseForCreatedUser)
            .orElseThrow(() -> NoodleException.badRequest("Invalid Token for account creation"));
    }

    @Override
    public ResponseEntity<AuthenticatedResponse> authenticate(AuthenticationRequest authenticationRequest) {
        return jwtService.attemptAuthentication(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            .map(this::responseForAuthenticatedUser)
            .orElseThrow(() -> NoodleException.unauthorized("Username or password incorrect"));
    }

    @Override
    public ResponseEntity<RequestRegistrationEmailResponse> requestRegistrationEmail(RequestRegistrationEmailRequest requestRegistrationEmailRequest) {
        String email = requestRegistrationEmailRequest.getEmail();
        String fullName = requestRegistrationEmailRequest.getName();
        jwtService.mailSignupToken(email, fullName);

        RequestRegistrationEmailResponse requestRegistrationEmailResponse = new RequestRegistrationEmailResponse();
        requestRegistrationEmailResponse.setEmail(email);
        requestRegistrationEmailResponse.setName(fullName);
        return ResponseEntity.ok(requestRegistrationEmailResponse);
    }

    private ResponseEntity<AuthenticatedResponse> responseForAuthenticatedUser(AuthenticatedUser user) {
        AuthenticatedResponse authenticatedResponse = new AuthenticatedResponse();
        authenticatedResponse.setEmail(user.getUser().getEmail());
        authenticatedResponse.setName(user.getUser().getFullName());
        authenticatedResponse.setToken(user.getJwtToken());
        return ResponseEntity.ok(authenticatedResponse);
    }

    private ResponseEntity<ActivateUserResponse> responseForCreatedUser(User user) {
        ActivateUserResponse activateUserResponse = new ActivateUserResponse();
        activateUserResponse.setId(user.getId().intValue());
        activateUserResponse.setEmail(user.getEmail());
        activateUserResponse.setName(user.getFullName());
        return ResponseEntity.status(CREATED).body(activateUserResponse);
    }
}
