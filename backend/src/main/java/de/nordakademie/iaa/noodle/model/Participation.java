package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Participation {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(optional = false)
    private User participant;
    @ManyToOne(optional = false)
    private Survey survey;
    @OneToOne(optional = true, cascade = CascadeType.ALL, mappedBy = "participation")
    private Response response;

    public Participation() {
    }

    public Participation(User participant, Survey survey, Response response) {
        this.participant = participant;
        this.survey = survey;
        this.response = response;
    }

    public Long getId() {
        return id;
    }

    public User getParticipant() {
        return participant;
    }

    public Survey getSurvey() {
        return survey;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participation)) return false;
        Participation that = (Participation) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
