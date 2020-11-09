package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * One of the options {@link User}s can vote on in a {@link Survey}.
 */
@Entity
@Table(name = "timeslot")
public class Timeslot {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Transient
    transient private UUID transientID;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start",nullable = false)
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end",nullable = true)
    private Date end;

    /**
     * Creates a new timeslot without initial values.
     */
    public Timeslot() {
    }

    /**
     * Creates a new timeslot with initial values.
     * @param survey The survey this timeslot is for.
     * @param start The start date/time of the timeslot.
     * @param end The end date/time of the timeslot.
     */
    public Timeslot(Survey survey, Date start, Date end) {
        this.survey = survey;
        this.start = start;
        this.end = end;
        this.transientID = UUID.randomUUID();
    }

    public Long getId() {
        return id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timeslot)) return false;
        Timeslot timeslot = (Timeslot) o;

        if (getId() == null) {
            return Objects.equals(transientID, timeslot.transientID);
        } else {
            return Objects.equals(getId(), timeslot.getId());
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
