package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a user of the application.
 *
 * @author Hans Rißer
 */
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private Set<Survey> createdSurveys;

    @OneToMany(mappedBy = "participant", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Participation> participations;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "password_hash", nullable = false, length = 2048)
    private String passwordHash;

    /**
     * Creates a new user without initial values.
     */
    public User() {
    }

    /**
     * Creates a new user with initial values.
     *
     * @param email        The email of the user.
     * @param fullName     The full name of the user.
     * @param passwordHash The password hash of the user (includes salt).
     */
    public User(String email, String fullName, String passwordHash) {
        this.createdSurveys = new HashSet<>();
        this.participations = new HashSet<>();
        this.email = email;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public Set<Survey> getCreatedSurveys() {
        return createdSurveys;
    }

    public Set<Participation> getParticipations() {
        return participations;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
