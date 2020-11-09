package de.nordakademie.iaa.noodle.services.model;

import de.nordakademie.iaa.noodle.model.User;

/**
 * User who is currently authenticated using a JWT token.
 */
public class AuthenticatedUser {
    private final User user;
    private final String jwtToken;

    /**
     * Creates a new AuthenticatedUser.
     * @param user The authenticated user.
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
