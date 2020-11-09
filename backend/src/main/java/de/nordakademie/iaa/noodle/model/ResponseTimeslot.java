package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "response_timeslot",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"response_id", "timeslot_id"})})
public class ResponseTimeslot {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Transient
    transient private UUID transientID;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id")
    private Response response;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot_id")
    private Timeslot timeslot;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_type")
    private ResponseType responseType;

    public ResponseTimeslot() {
    }

    public ResponseTimeslot(Response response, Timeslot timeslot, ResponseType responseType) {
        this.response = response;
        this.timeslot = timeslot;
        this.responseType = responseType;
        this.transientID = UUID.randomUUID();
    }

    public Long getId() {
        return id;
    }

    public Response getResponse() {
        return response;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseTimeslot)) return false;
        ResponseTimeslot that = (ResponseTimeslot) o;

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
