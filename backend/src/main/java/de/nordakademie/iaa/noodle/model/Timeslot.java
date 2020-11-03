package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Timeslot {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Survey survey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date end;


    public Timeslot() {
    }

    public Timeslot(Survey survey, Date start, Date end) {
        this.survey = survey;
        this.start = start;
        this.end = end;
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
        return Objects.equals(getId(), timeslot.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
