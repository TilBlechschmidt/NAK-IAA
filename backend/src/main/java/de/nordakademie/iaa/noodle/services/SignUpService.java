package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.filter.NoodleException;
import de.nordakademie.iaa.noodle.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SignUpService {
    private final PasswordService passwordService;
    private final UserService userService;
    private final JWTService jwtService;
    private final MailService mailService;

    @Autowired
    public SignUpService(PasswordService passwordService, UserService userService, JWTService jwtService, MailService mailService) {
        this.passwordService = passwordService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }

    public Optional<User> createAccount(String token, String password) {
        // Token does not have the TOKEN_PREFIX, because it is not used for authentication
        return Optional.of(token)
            .flatMap(jwtService::userDetailsForToken)
            .flatMap(userDetails -> this.createUser(password, userDetails));
    }

    public void mailSignupToken(String email, String fullName) {
        try {
            userService.getUserByEMail(email).ifPresentOrElse(
                user -> mailService.sendRegistrationMailDuplicateEmail(fullName, email),
                () -> mailService.sendRegistrationMail(jwtService.buildEmailToken(email, fullName), fullName, email)
            );
        } catch (MailException e) {
            throw NoodleException.serviceUnavailable("Failed to send email");
        }
    }

    private Optional<User> createUser(String password, UserDetails userDetails) {
        String passwordHash = passwordService.hashPassword(password);
        User user = userService.createNewUser(userDetails.email, userDetails.fullName, passwordHash);
        return Optional.of(user);
    }
}
