package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Survey {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Timeslot> timeslots;

    @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
    private Timeslot chosenTimeslot;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User creator;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "survey", fetch = FetchType.LAZY)
    private Set<Participation> participations;
    @Column(nullable = false, length = 2048)
    private String title;
    @Column(nullable = false, length = 2048)
    private String description;

    public Survey() {
    }

    public Survey(Set<Timeslot> timeslots, Timeslot chosenTimeslot, User creator, Set<Participation> participations, String title, String description) {
        this.timeslots = timeslots;
        this.chosenTimeslot = chosenTimeslot;
        this.creator = creator;
        this.participations = participations;
        this.title = title;
        this.description = description;
    }

    public Timeslot getChosenTimeslot() {
        return chosenTimeslot;
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

    public boolean getIsClosed() {
        return getChosenTimeslot() != null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChosenTimeslot(Timeslot chosenTimeslot) {
        this.chosenTimeslot = chosenTimeslot;
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
