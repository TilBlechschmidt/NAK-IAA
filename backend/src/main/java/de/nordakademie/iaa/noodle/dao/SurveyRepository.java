package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Survey;
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

    /**
     * @deprecated For extensibility Reasons, please use {@link #findSurveysWhereUserHasNotParticipated(Long)}
     * instead of this method.
     */
    @Deprecated
    List<Survey> findAllByParticipations_participant_idNot(Long id);

    default List<Survey> findSurveysWhereUserHasNotParticipated(Long userID) {
        return findAllByParticipations_participant_idNot(userID);
    }

    /**
     * @deprecated For extensibility Reasons, please use {@link #findSurveysThatNeedAttentionBy(Long)}}
     * instead of this method.
     */
    @Deprecated
    List<Survey> findAllByParticipations_participant_idAndParticipations_responseNull(Long id);

    default List<Survey> findSurveysThatNeedAttentionBy(Long userID) {
        return findAllByParticipations_participant_idAndParticipations_responseNull(userID);
    }

    Survey findById(Long id);

    Survey save(Survey toSave);

    void deleteById(Long id);
}
