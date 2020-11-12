package de.nordakademie.iaa.noodle.services.interfaces;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.JWTException;
import de.nordakademie.iaa.noodle.services.model.SpringAuthenticationDetails;
import de.nordakademie.iaa.noodle.services.model.UserDetails;

/**
 * Service to generate and parse JWT tokens.
 *
 * @author Noah Peeters
 * @author Hans Rißer
 * @see de.nordakademie.iaa.noodle.filter.JWTAuthorizationFilter
 */
public interface JWTService {
    /**
     * Generates a token for the account creating which can be mailed to the user.
     *
     * @param email    The email of the user.
     * @param fullName The full name of the user.
     * @return The generated token.
     */
    String buildEmailToken(String email, String fullName);

    /**
     * Generates a token which can be used to authenticate in future requests.
     *
     * @param user The user for which the token should be generated.
     * @return The generated token.
     */
    String buildSpringAuthenticationToken(User user);

    /**
     * Parses te user details from a user creation token.
     *
     * @param token The user creation token.
     * @return The parsed user details.
     * @throws JWTException Thrown, when the token is invalid.
     */
    UserDetails userDetailsForToken(String token) throws JWTException;

    /**
     * Parses the authentication data from an authentication token.
     *
     * @param token The authentication token.
     * @return The parsed authentication details.
     * @throws JWTException Thrown, when the token is invalid.
     */
    SpringAuthenticationDetails authenticationDetailsForToken(String token) throws JWTException;
}
