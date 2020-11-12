package de.nordakademie.iaa.noodle.services.interfaces;

import de.nordakademie.iaa.noodle.services.exceptions.AuthenticationException;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.JWTException;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;
import de.nordakademie.iaa.noodle.services.model.AuthenticatedUser;
import org.springframework.security.core.Authentication;

/**
 * Service to manage signing in.
 *
 * @author Noah Peeters
 * @author Hans Rißer
 */
public interface SignInService {
    /**
     * Tries to authenticate a user.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return An authenticated user.
     * @throws EntityNotFoundException Thrown, when the user does not exist.
     * @throws PasswordException       Thrown, when the password is invalid.
     */
    AuthenticatedUser attemptAuthentication(String email, String password)
        throws EntityNotFoundException, PasswordException;

    /**
     * Parses the Authentication data from a HTTP Header.
     *
     * @param header THe Authorization header value of a request.
     * @return Authentication data.
     * @throws JWTException            Thrown, when the JWT token is invalid.
     * @throws EntityNotFoundException Thrown, when the authenticated user does not exist.
     * @throws AuthenticationException Thrown, when the header does not contain valid data.
     */
    Authentication springAuthenticationForHeader(String header)
        throws JWTException, EntityNotFoundException, AuthenticationException;
}
