package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "creator")
    private Set<Survey> createdSurveys;

    @OneToMany(mappedBy = "participant", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Participation> participations;

    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false, length = 2048)
    private String passwordHash;
    @Column(nullable = false)
    private String passwordSalt;

    public User() {}

    public User(Set<Survey> createdSurveys, Set<Participation> participations, String fullName, String passwordHash, String passwordSalt) {
        this.createdSurveys = createdSurveys;
        this.participations = participations;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
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

    public String getPasswordSalt() {
        return passwordSalt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
