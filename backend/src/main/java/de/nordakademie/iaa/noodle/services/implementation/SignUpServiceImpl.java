package de.nordakademie.iaa.noodle.services.implementation;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.ConflictException;
import de.nordakademie.iaa.noodle.services.exceptions.JWTException;
import de.nordakademie.iaa.noodle.services.exceptions.MailClientException;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;
import de.nordakademie.iaa.noodle.services.interfaces.*;
import de.nordakademie.iaa.noodle.services.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Service to manage signing up.
 *
 * @author Noah Peeters
 * @author Hans Rißer
 */
@Service("SignUpService")
public class SignUpServiceImpl implements SignUpService {
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
    public SignUpServiceImpl(PasswordService passwordService, UserService userService, JWTService jwtService,
                             MailService mailService) {
        this.passwordService = passwordService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createAccount(String token, String password) throws JWTException, ConflictException, PasswordException {
        // Token does not have the TOKEN_PREFIX, because it is not used for authentication
        UserDetails userDetails = jwtService.userDetailsForToken(token);
        return createUser(password, userDetails);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mailSignupToken(String email, String fullName) throws MailClientException {
        if (userService.existsUserWithEMail(email)) {
            mailService.sendRegistrationMailDuplicateEmail(fullName, email);
        } else {
            mailService.sendRegistrationMail(jwtService.buildEmailToken(email, fullName), fullName, email);
        }
    }

    private User createUser(String password, UserDetails userDetails) throws ConflictException, PasswordException {
        String passwordHash = passwordService.hashPassword(password);
        try {
            return userService.createNewUser(userDetails.getEmail(), userDetails.getFullName(), passwordHash);
        } catch (DataIntegrityViolationException e) {
            // EMail address already exists in the database.
            throw new ConflictException("emailDuplicate");
        }
    }
}
