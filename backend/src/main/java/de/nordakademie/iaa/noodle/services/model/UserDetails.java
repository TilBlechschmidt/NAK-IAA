package de.nordakademie.iaa.noodle.services.model;

/**
 * POJO with the user details of a new user.
 */
public class UserDetails {
    private final String email;
    private final String fullName;

    /**
     * Creates new UserDetails.
     * @param email The email of the user.
     * @param fullName The full name of the user.
     */
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
