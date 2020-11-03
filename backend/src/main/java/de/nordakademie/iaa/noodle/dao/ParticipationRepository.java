package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Participation;
import de.nordakademie.iaa.noodle.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RepositoryDefinition(idClass = Long.class, domainClass = Participation.class)
@Transactional(propagation = Propagation.REQUIRED)
public interface ParticipationRepository {
    /**
     * @deprecated For extensibility Reasons, please use {@link #findParticipation(Long, User)}
     * instead of this method.
     */
    @Deprecated
    @EntityGraph(attributePaths = {
        "response"
    })
    Participation findParticipationBySurvey_IdAndParticipant(Long surveyID, User participant);

    default Participation findParticipation(Long surveyID, User participant) {
        return findParticipationBySurvey_IdAndParticipant(surveyID, participant);
    }

    void save(Participation participation);
}
