package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Survey;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
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
        SELECT s1 FROM Survey s1 WHERE :userID NOT IN
            (SELECT p.participant
            FROM Survey s LEFT OUTER JOIN Participation p ON p.survey=s
            WHERE s.id = s1.id )""")
    List<Survey> findSurveysWhereUserHasNotParticipated(@Param("userID") Long userID);

    // Sadly, JPQL does not allow us to use "<boolean_expr> = flag", so we need to emulate it using boolean logic
// This leads to doubled logic
// However, the alternatives would be either writing a lot of queries, introducing a second query approach for one
// method, or filtering in the backend instead of the database, which are all not ideal either.
    @Query("""
SELECT DISTINCT survey FROM Survey survey
    LEFT OUTER JOIN Participation participation ON participation.survey = survey
    WHERE
    (:participated IS NULL OR
        (:participated = true  AND
            participation.participant.id = :userID) OR
        (:participated = false AND
            (NOT EXISTS (SELECT p FROM Participation p WHERE p.survey=survey AND p.participant.id = :userID))))
    AND
    (:completed IS NULL OR
        (:completed=true  AND NOT
            survey.chosenTimeslot IS NULL) OR
        (:completed=false AND
            survey.chosenTimeslot IS NULL))
    AND
    (:owned IS NULL OR
        (:owned = true  AND
            survey.creator.id = :userID) OR
        (:owned = false AND NOT
            survey.creator.id = :userID))
    AND
    (:upcoming IS NULL OR
        (:upcoming = true AND
            (survey.chosenTimeslot IS NOT NULL AND CURRENT_TIMESTAMP <= ALL (SELECT start FROM Timeslot WHERE id=survey.chosenTimeslot.id)) ) OR
        (:upcoming = false AND NOT
            (survey.chosenTimeslot IS NOT NULL AND CURRENT_TIMESTAMP <= ALL (SELECT start FROM Timeslot WHERE id=survey.chosenTimeslot.id)) ))
    AND
     (:attentionRequired IS NULL OR
        (:attentionRequired = true  AND
            (participation IS NOT NULL AND :userID = participation.participant.id AND NOT EXISTS (SELECT r FROM Response r where r.participation=participation))) OR
        (:attentionRequired = false AND
            (NOT EXISTS (SELECT p FROM Participation p WHERE p.survey=survey AND p.participant.id = :userID) OR
            EXISTS (SELECT r FROM Response r where r.participation=participation))))
        """)
    List<Survey> querySurvey(@Param("userID") Long userID,
                             @Param("participated") Boolean didParticipateIn,
                             @Param("completed") Boolean isCompleted,
                             @Param("owned") Boolean isOwnSurvey,
                             @Param("upcoming") Boolean isUpcoming,
                             @Param("attentionRequired") Boolean requiresAttention);

    @EntityGraph(attributePaths = {
        "timeslots",
        "participations",
        "participations.participant",
        "participations.response",
        "participations.response.responseTimeslots"
    })
    Survey findById(Long id);

    Survey save(Survey toSave);

    void delete(Survey survey);
}
