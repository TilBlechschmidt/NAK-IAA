package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.AccountApi;
import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.*;
import de.nordakademie.iaa.noodle.services.interfaces.SignInService;
import de.nordakademie.iaa.noodle.services.interfaces.SignUpService;
import de.nordakademie.iaa.noodle.services.model.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * Rest controller for all routes regarding authentication.
 *
 * @author Noah Peeters
 */
@RestController
public class AccountController implements AccountApi {
    private final SignUpService signUpService;
    private final SignInService signInService;

    /**
     * Creates a new AccountController
     *
     * @param signUpService Service used for creating new accounts.
     * @param signInService Service used to authenticate users.
     */
    @Autowired
    public AccountController(SignUpService signUpService, SignInService signInService) {
        this.signUpService = signUpService;
        this.signInService = signInService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<ActivateUserResponse> activateUser(ActivateUserRequest activateUserRequest) {
        try {
            String token = activateUserRequest.getToken();
            String password = activateUserRequest.getPassword();

            User user = signUpService.createAccount(token, password);
            ActivateUserResponse activateUserResponse = responseForCreatedUser(user);
            return ResponseEntity.status(CREATED).body(activateUserResponse);
        } catch (JWTException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (PasswordException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<AuthenticatedResponse> authenticate(AuthenticationRequest authenticationRequest) {
        try {
            AuthenticatedUser user = signInService.attemptAuthentication(authenticationRequest.getEmail(),
                authenticationRequest.getPassword());
            AuthenticatedResponse authenticatedResponse = responseForAuthenticatedUser(user);
            return ResponseEntity.ok(authenticatedResponse);
        } catch (PasswordException | EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalidAuthenticationData", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<RequestRegistrationEmailResponse> requestRegistrationEmail(
        RequestRegistrationEmailRequest requestRegistrationEmailRequest) {
        try {
            String email = requestRegistrationEmailRequest.getEmail();
            String fullName = requestRegistrationEmailRequest.getName();
            signUpService.mailSignupToken(email, fullName);

            RequestRegistrationEmailResponse requestRegistrationEmailResponse = new RequestRegistrationEmailResponse();
            requestRegistrationEmailResponse.setEmail(email);
            requestRegistrationEmailResponse.setName(fullName);
            return ResponseEntity.ok(requestRegistrationEmailResponse);
        } catch (MailClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), e);
        }
    }

    private AuthenticatedResponse responseForAuthenticatedUser(AuthenticatedUser user) {
        AuthenticatedResponse authenticatedResponse = new AuthenticatedResponse();
        authenticatedResponse.setEmail(user.getUser().getEmail());
        authenticatedResponse.setName(user.getUser().getFullName());
        authenticatedResponse.setToken(user.getJwtToken());
        return authenticatedResponse;
    }

    private ActivateUserResponse responseForCreatedUser(User user) {
        ActivateUserResponse activateUserResponse = new ActivateUserResponse();
        activateUserResponse.setId(user.getId());
        activateUserResponse.setEmail(user.getEmail());
        activateUserResponse.setName(user.getFullName());
        return activateUserResponse;
    }
}
