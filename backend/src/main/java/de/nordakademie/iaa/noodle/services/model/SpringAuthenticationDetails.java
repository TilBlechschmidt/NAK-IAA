package de.nordakademie.iaa.noodle.services.model;

import java.util.List;

public class SpringAuthenticationDetails {
    private final Long userID;
    private final List<String> authorities;

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
