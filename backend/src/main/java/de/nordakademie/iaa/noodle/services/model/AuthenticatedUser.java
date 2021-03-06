package de.nordakademie.iaa.noodle.services.model;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.interfaces.JWTService;

/**
 * {@link User} who is currently authenticated using a JWT token.
 *
 * @author Noah Peeters
 * @see JWTService
 */
public class AuthenticatedUser {
    private final User user;
    private final String jwtToken;

    /**
     * Creates a new AuthenticatedUser.
     *
     * @param user     The authenticated user.
     * @param jwtToken The token used for authentication.
     */
    public AuthenticatedUser(User user, String jwtToken) {
        this.user = user;
        this.jwtToken = jwtToken;
    }

    public User getUser() {
        return user;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
