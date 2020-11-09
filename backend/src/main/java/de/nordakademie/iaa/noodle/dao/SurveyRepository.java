package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.dao.model.QuerySurveysItem;
import de.nordakademie.iaa.noodle.model.Survey;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository for surveys.
 */
@Repository
@RepositoryDefinition(idClass = Long.class, domainClass = Survey.class)
@Transactional(propagation = Propagation.REQUIRED)
public interface SurveyRepository {

    /**
     * Query the QuerySurveysItem of multiple surveys which fulfill the given criteria.
     * @param userID The id of the user the other criteria refer to.
     * @param didParticipateIn The user must habe participated in the survey.
     * @param isCompleted The survey is completed.
     * @param isOwnSurvey The survey was created by the user.
     * @param isUpcoming The selected timeslot is in the future.
     * @param requiresAttention THe user's response was discarded due to an update of the survey.
     * @return List of QuerySurveysItems which fulfill the given criteria.
     */
// Sadly, JPQL does not allow us to use "<boolean_expr> = flag", so we need to emulate it using boolean logic
// This leads to doubled logic
// However, the alternatives would be either writing a lot of queries, introducing a second query approach for one
// method, or filtering in the backend instead of the database, which are all not ideal either.
    @Query("""
SELECT DISTINCT
    survey.id AS id,
    survey.title AS title,
        (SELECT COUNT(response) FROM Response response WHERE response.participation.survey = survey)
        AS responseCount
FROM Survey survey
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
        survey.selectedTimeslot IS NULL) OR
    (:completed=false AND
        survey.selectedTimeslot IS NULL))
AND
(:owned IS NULL OR
    (:owned = true  AND
        survey.creator.id = :userID) OR
    (:owned = false AND NOT
        survey.creator.id = :userID))
AND
(:upcoming IS NULL OR
    (:upcoming = true AND
        (survey.selectedTimeslot IS NOT NULL AND CURRENT_TIMESTAMP <= ALL (SELECT start FROM Timeslot WHERE id=survey.selectedTimeslot.id)) ) OR
    (:upcoming = false AND NOT
        (survey.selectedTimeslot IS NOT NULL AND CURRENT_TIMESTAMP <= ALL (SELECT start FROM Timeslot WHERE id=survey.selectedTimeslot.id)) ))
AND
 (:attentionRequired IS NULL OR
    (:attentionRequired = true  AND
        (participation IS NOT NULL AND :userID = participation.participant.id AND NOT EXISTS (SELECT r FROM Response r where r.participation=participation))) OR
    (:attentionRequired = false AND
        (NOT EXISTS (SELECT p FROM Participation p WHERE p.survey=survey AND p.participant.id = :userID) OR
        EXISTS (SELECT r FROM Response r WHERE r.participation=participation AND participation.participant.id=:userID))))
    """)
    List<QuerySurveysItem> querySurvey(@Param("userID")            Long userID,
                                       @Param("participated")      Boolean didParticipateIn,
                                       @Param("completed")         Boolean isCompleted,
                                       @Param("owned")             Boolean isOwnSurvey,
                                       @Param("upcoming")          Boolean isUpcoming,
                                       @Param("attentionRequired") Boolean requiresAttention);

    /**
     * Queries a single survey by the id.
     * @param id The id of the survey.
     * @return The requested survey or null if it does not exists.
     */
    @EntityGraph(attributePaths = {
        "timeslots",
        "participations",
        "participations.participant",
        "participations.response",
        "participations.response.responseTimeslots"
    })
    Survey findById(Long id);

    /**
     * Saves a survey.
     * @param survey The survey to save.
     */
    Survey save(Survey survey);

    /**
     * Deletes a survey.
     * @param survey The survey to delete.
     */
    void delete(Survey survey);
}
