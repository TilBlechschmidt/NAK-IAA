package de.nordakademie.iaa.noodle.services;

import java.util.List;

public class SpringAuthenticationDetails {
    public final Long userID;
    public final List<String> authorities;

    public SpringAuthenticationDetails(Long userID, List<String> authorities) {
        this.userID = userID;
        this.authorities = authorities;
    }
}
