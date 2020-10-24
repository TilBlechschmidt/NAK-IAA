package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Survey {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Timeslot> timeslots;

    @ManyToOne(optional = false)
    private User creator;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "survey")
    private Set<Participation> participations;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,length = 2048)
    private String description;

    public Survey() {}

    public Survey(Set<Timeslot> timeslots, User creator, Set<Participation> participations, String title, String description) {
        this.timeslots = timeslots;
        this.creator = creator;
        this.participations = participations;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Set<Timeslot> getTimeslots() {
        return timeslots;
    }

    public User getCreator() {
        return creator;
    }

    public Set<Participation> getParticipations() {
        return participations;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Survey)) return false;
        Survey survey = (Survey) o;
        return Objects.equals(getId(), survey.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
