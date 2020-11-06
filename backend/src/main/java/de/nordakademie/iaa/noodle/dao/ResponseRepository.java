package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Response;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RepositoryDefinition(idClass = Long.class, domainClass = Response.class)
@Transactional(propagation = Propagation.REQUIRED)
public interface ResponseRepository {



    default Response findByIdAndSurveyId(Long id, Long surveyId) {
        return findByIdAndParticipation_Survey_Id(id, surveyId);
    }

    /**
     * @deprecated For extensibility Reasons, please use {@link #findByIdAndSurveyId(Long, Long)}
     * instead of this method.
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

    void save(Response toSave);

    void delete(Response response);
}
