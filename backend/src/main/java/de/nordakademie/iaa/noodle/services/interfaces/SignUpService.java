package de.nordakademie.iaa.noodle.services.interfaces;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.ConflictException;
import de.nordakademie.iaa.noodle.services.exceptions.JWTException;
import de.nordakademie.iaa.noodle.services.exceptions.MailClientException;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;

/**
 * Service to manage signing up.
 *
 * @author Noah Peeters
 * @author Hans Rißer
 */
public interface SignUpService {
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
    User createAccount(String token, String password) throws JWTException, ConflictException, PasswordException;

    /**
     * Sends a token to the user for registration.
     *
     * @param email    The email of the user.
     * @param fullName The full name of the user.
     * @throws MailClientException Thrown, when the mail cannot be send.
     */
    void mailSignupToken(String email, String fullName) throws MailClientException;
}
