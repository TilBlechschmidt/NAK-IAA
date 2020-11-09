package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * A response for a {@link Survey} which contains the answers to the {@link Timeslot}s.
 * A response is always given in the context of a {@link Participation}.
 */
@Entity
@Table(name = "response")
public class Response {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id")
    private Participation participation;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "response", fetch = FetchType.LAZY)
    private Set<ResponseTimeslot> responseTimeslots;

    /**
     * Creates a new response without initial values.
     */
    public Response() {
    }

    /**
     * Creates a new response with initial values.
     * @param participation The participation the response is for.
     * @param responseTimeslots The answers to the timeslots.
     */
    public Response(Participation participation, Set<ResponseTimeslot> responseTimeslots) {
        this.participation = participation;
        this.responseTimeslots = responseTimeslots;
    }

    public Long getId() {
        return id;
    }

    public Participation getParticipation() {
        return participation;
    }

    public Set<ResponseTimeslot> getResponseTimeslots() {
        return responseTimeslots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response)) return false;
        Response response = (Response) o;
        return Objects.equals(getId(), response.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
