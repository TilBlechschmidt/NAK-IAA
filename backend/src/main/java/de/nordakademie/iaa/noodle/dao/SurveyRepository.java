package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Survey;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RepositoryDefinition(idClass = Long.class, domainClass = Survey.class)
@Transactional(propagation = Propagation.REQUIRED)
public interface SurveyRepository {

    List<Survey> findAllByCreator_id(Long creatorId);

    @Query("""
        SELECT s1 FROM Survey s1 WHERE ?1 NOT IN
            (SELECT p.participant
            FROM Survey s LEFT OUTER JOIN Participation p ON p.survey=s
            WHERE s.id = s1.id )""")
    List<Survey> findSurveysWhereUserHasNotParticipated(Long userID);


    /**
     * @deprecated For extensibility Reasons, please use {@link #findSurveysThatNeedAttentionBy(Long)}}
     * instead of this method.
     */
    @Deprecated
    List<Survey> findAllByParticipations_participant_idAndParticipations_responseNull(Long id);

    default List<Survey> findSurveysThatNeedAttentionBy(Long userID) {
        return findAllByParticipations_participant_idAndParticipations_responseNull(userID);
    }

    @EntityGraph(attributePaths = {
        "timeslots",
        "participations",
        "participations.participant",
        "participations.response",
        "participations.response.responseTimeslots"
        })
    Survey findById(Long id);

    Survey save(Survey toSave);

    void deleteById(Long id);
}
