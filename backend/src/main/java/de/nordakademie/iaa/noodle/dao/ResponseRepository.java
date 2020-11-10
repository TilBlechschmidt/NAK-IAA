package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Response;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for {@link Response}s.
 *
 * @author Hans Rißer
 */
@Repository
@RepositoryDefinition(idClass = Long.class, domainClass = Response.class)
@Transactional(propagation = Propagation.REQUIRED)
public interface ResponseRepository {
    /**
     * @deprecated For extensibility Reasons, please use {@link #findByIdAndSurveyId(Long, Long)}
     * instead of this method, as this method name is dependent on the database structure.
     */
    @EntityGraph(attributePaths = {
        "participation",
        "participation.survey",
        "participation.survey.creator",
        "participation.participant",
        "responseTimeslots"
    })
    @Deprecated
    Response findByIdAndParticipation_Survey_Id(Long id, Long surveyId);

    /**
     * Queries a response based on its id and survey id.
     *
     * @param responseId The id of the response
     * @param surveyId   The id of the survey
     * @return The requested Response or null if it does not exists.
     */
    default Response findByIdAndSurveyId(Long responseId, Long surveyId) {
        return findByIdAndParticipation_Survey_Id(responseId, surveyId);
    }

    /**
     * Saves a response.
     *
     * @param response The response to save.
     */
    void save(Response response);

    /**
     * Deletes a response.
     *
     * @param response The response to delete.
     */
    void delete(Response response);
}
