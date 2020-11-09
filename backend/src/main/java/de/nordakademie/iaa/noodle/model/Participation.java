package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "participation")
public class Participation {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Transient
    transient private UUID transientID;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private User participant;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToOne(optional = true, cascade = CascadeType.ALL, mappedBy = "participation", fetch = FetchType.LAZY)
    private Response response;

    public Participation() {
    }

    public Participation(User participant, Survey survey, Response response) {
        this.participant = participant;
        this.survey = survey;
        this.response = response;
        this.transientID = UUID.randomUUID();
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

        if (getId() == null) {
            return Objects.equals(transientID, that.transientID);
        } else {
            return Objects.equals(getId(), that.getId());
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
