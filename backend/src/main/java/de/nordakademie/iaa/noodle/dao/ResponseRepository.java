package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Response;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RepositoryDefinition(idClass = Long.class, domainClass = Response.class)
@Transactional(propagation = Propagation.REQUIRED)
public interface ResponseRepository {
    Response findById(Long id);

    /**
     * @deprecated For extensibility Reasons, please use {@link #findBySurveyId(Long)}
     * instead of this method.
     */
    @Deprecated
    List<Response> findByParticipation_Survey_Id(Long surveyID);

    default List<Response> findBySurveyId(Long surveyID) {
        return findByParticipation_Survey_Id(surveyID);
    }

    void save(Response toSave);
}
