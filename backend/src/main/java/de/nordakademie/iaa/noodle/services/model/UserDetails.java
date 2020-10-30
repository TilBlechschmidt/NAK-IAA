package de.nordakademie.iaa.noodle.services.model;

public class UserDetails {
    private final String email;
    private final String fullName;

    public UserDetails(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }
}
