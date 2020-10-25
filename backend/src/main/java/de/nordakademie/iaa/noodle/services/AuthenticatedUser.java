package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;

public class AuthenticatedUser {
    private final User user;
    private final String jwtToken;

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
