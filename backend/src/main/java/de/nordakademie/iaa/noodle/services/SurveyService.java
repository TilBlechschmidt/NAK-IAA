package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.SurveyRepository;
import de.nordakademie.iaa.noodle.model.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SurveyService {
    private final SurveyRepository surveyRepository;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public Survey querySurvey(Long id) {
        return surveyRepository.findById(id);
    }
}
