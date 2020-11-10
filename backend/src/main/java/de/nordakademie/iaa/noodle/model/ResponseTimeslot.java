package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

/**
 * The answer to a specific {@link Timeslot}.
 *
 * @author Hans Rißer
 */
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

    /**
     * Creates a new response timeslot without initial values.
     */
    public ResponseTimeslot() {
    }

    /**
     * Creates a new response timeslot with initial values.
     * @param response The response the answer is for.
     * @param timeslot The timeslot for which the answer is.
     * @param responseType The type of the answer.
     */
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
