package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class ResponseTimeslot {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Response response;

    @ManyToOne(optional = false)
    private Timeslot timeslot;

    @Enumerated(EnumType.STRING)
    private ResponseType responseType;

    public ResponseTimeslot() {}

    public ResponseTimeslot(Response response, Timeslot timeslot, ResponseType responseType) {
        this.response = response;
        this.timeslot = timeslot;
        this.responseType = responseType;
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
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
