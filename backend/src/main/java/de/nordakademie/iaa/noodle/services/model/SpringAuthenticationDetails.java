package de.nordakademie.iaa.noodle.services.model;

import java.util.List;

/**
 * POJO with Authentication details needed for Spring Authentication.
 */
public class SpringAuthenticationDetails {
    private final Long userID;
    private final List<String> authorities;

    /**
     * Creates new SpringAuthenticationDetails.
     * @param userID The user id of the authenticated user.
     * @param authorities The authorities the user has.
     */
    public SpringAuthenticationDetails(Long userID, List<String> authorities) {
        this.userID = userID;
        this.authorities = authorities;
    }

    public Long getUserID() {
        return userID;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}
