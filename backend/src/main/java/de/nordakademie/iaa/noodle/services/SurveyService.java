package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.SurveyRepository;
import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { ServiceException.class })
public class SurveyService {
    private final SurveyRepository surveyRepository;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public Survey querySurvey(Long id) throws EntityNotFoundException {
        Survey survey = surveyRepository.findById(id);
        if (survey == null) { throw new EntityNotFoundException("surveyNotFound"); }
        return survey;
    }

    public boolean canUserRespondToSurvey(Survey survey, User user) {
        boolean surveyAcceptsResponses = !survey.getIsClosed();
        boolean isSurveyCreator = survey.getCreator().equals(user);
        return surveyAcceptsResponses && !isSurveyCreator;
    }
}
