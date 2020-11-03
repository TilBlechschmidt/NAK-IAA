package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.ParticipationRepository;
import de.nordakademie.iaa.noodle.model.Participation;
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
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final SurveyService surveyService;

    @Autowired
    public ParticipationService(ParticipationRepository participationRepository, SurveyService surveyService) {
        this.participationRepository = participationRepository;
        this.surveyService = surveyService;
    }

    public Participation getOrCreateParticipation(User user, Long surveyID) throws EntityNotFoundException {
        Participation queriedParticipation = participationRepository.findParticipation(surveyID, user);

        if (queriedParticipation != null) {
            return queriedParticipation;
        } else {
            Survey survey = surveyService.querySurvey(surveyID);
            Participation newParticipation = new Participation(user, survey, null);
            participationRepository.save(newParticipation);
            return newParticipation;
        }
    }
}
