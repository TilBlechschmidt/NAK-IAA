package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.*;
import de.nordakademie.iaa.noodle.services.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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

    public User createAccount(String token, String password) throws JWTException, ConflictException, PasswordException {
        // Token does not have the TOKEN_PREFIX, because it is not used for authentication
        UserDetails userDetails = jwtService.userDetailsForToken(token);
        return createUser(password, userDetails);
    }

    public void mailSignupToken(String email, String fullName) throws MailClientException {
        if (userService.existsUserWithEMail(email)) {
            mailService.sendRegistrationMailDuplicateEmail(fullName, email);
        } else {
            mailService.sendRegistrationMail(jwtService.buildEmailToken(email, fullName), fullName, email);
        }
    }

    private User createUser(String password, UserDetails userDetails) throws ConflictException, PasswordException {
        String passwordHash = passwordService.hashPassword(password);
        return userService.createNewUser(userDetails.getEmail(), userDetails.getFullName(), passwordHash);
    }
}
