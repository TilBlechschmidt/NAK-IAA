package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.ConflictException;
import de.nordakademie.iaa.noodle.services.exceptions.JWTException;
import de.nordakademie.iaa.noodle.services.exceptions.MailClientException;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;
import de.nordakademie.iaa.noodle.services.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to manage signing up.
 *
 * @author Noah Peeters
 * @author Hans Rißer
 */
@Service
public class SignUpService {
    private final PasswordService passwordService;
    private final UserService userService;
    private final JWTService jwtService;
    private final MailService mailService;

    /**
     * Creates a new SignUpService.
     *
     * @param passwordService Service used to manage passwords.
     * @param userService     Service used to manage users.
     * @param jwtService      Service used to manage JWT tokens.
     * @param mailService     Service used to send mails.
     */
    @Autowired
    public SignUpService(PasswordService passwordService, UserService userService, JWTService jwtService,
                         MailService mailService) {
        this.passwordService = passwordService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }

    /**
     * Creates a new account with a registration token and a password.
     *
     * @param token    The registration token.
     * @param password The password.
     * @return The new user.
     * @throws JWTException      Thrown, when the token is invalid.
     * @throws ConflictException Thrown, when a user with the email address in the token already exists.
     * @throws PasswordException Throw, when the password is invalid.
     */
    public User createAccount(String token, String password) throws JWTException, ConflictException, PasswordException {
        // Token does not have the TOKEN_PREFIX, because it is not used for authentication
        UserDetails userDetails = jwtService.userDetailsForToken(token);
        return createUser(password, userDetails);
    }

    /**
     * Sends a token to the user for registration.
     *
     * @param email    The email of the user.
     * @param fullName The full name of the user.
     * @throws MailClientException Thrown, when the mail cannot be send.
     */
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
