package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * The central domain object of the application.
 * A {@link Survey} has multiple {@link Timeslot}s which {@link User}s can
 * {@link Response respond} to with a {@link Participation}.
 *
 * @author Hans Rißer
 */
@Entity
@Table(name = "survey")
public class Survey {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Transient
    transient private UUID transientID;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Timeslot> timeslots;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_timeslot_id")
    private Timeslot selectedTimeslot;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "survey", fetch = FetchType.LAZY)
    private Set<Participation> participations;

    @Column(name = "title", nullable = false, length = 2048)
    private String title;
    @Column(name = "description", nullable = false, length = 2048)
    private String description;

    /**
     * Creates a new survey without initial values.
     */
    public Survey() {
    }

    /**
     * Creates a new survey with initial values.
     *
     * @param creator     The user who created the survey.
     * @param title       The title of the survey.
     * @param description The description of the survey.
     */
    public Survey(User creator, String title, String description) {
        this.timeslots = new HashSet<>();
        this.selectedTimeslot = null;
        this.creator = creator;
        this.participations = new HashSet<>();
        this.title = title;
        this.description = description;
        this.transientID = UUID.randomUUID();
    }

    public Timeslot getSelectedTimeslot() {
        return selectedTimeslot;
    }

    public void setSelectedTimeslot(Timeslot selectedTimeslot) {
        this.selectedTimeslot = selectedTimeslot;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsClosed() {
        return getSelectedTimeslot() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Survey))
            return false;
        Survey survey = (Survey) o;

        if (getId() == null) {
            return Objects.equals(transientID, survey.transientID);
        } else {
            return Objects.equals(getId(), survey.getId());
        }
    }

    @Override
    public int hashCode() {
        if (transientID != null) {
            return Objects.hash(transientID);
        } else {
            return Objects.hash(getId());
        }
    }
}
