package de.nordakademie.iaa.noodle.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

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

    public Response() {
    }

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
