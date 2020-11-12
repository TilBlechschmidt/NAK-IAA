package de.nordakademie.iaa.noodle.services.implementation;

import de.nordakademie.iaa.noodle.dao.ParticipationRepository;
import de.nordakademie.iaa.noodle.model.Participation;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ServiceException;
import de.nordakademie.iaa.noodle.services.interfaces.ParticipationService;
import de.nordakademie.iaa.noodle.services.interfaces.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to manage {@link Participation}s.
 *
 * @author Noah Peeters
 * @see ParticipationRepository
 */
@Service("ParticipationService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
public class ParticipationServiceImpl implements ParticipationService {
    private final ParticipationRepository participationRepository;
    private final SurveyService surveyService;

    /**
     * Creates a new ParticipationService.
     *
     * @param participationRepository The repository for participations.
     * @param surveyService           The service to manage surveys.
     */
    @Autowired
    public ParticipationServiceImpl(ParticipationRepository participationRepository, SurveyService surveyService) {
        this.participationRepository = participationRepository;
        this.surveyService = surveyService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Participation getOrCreateParticipation(User user, Long surveyID) throws EntityNotFoundException {
        Participation queriedParticipation = participationRepository.findParticipation(surveyID, user);

        if (queriedParticipation != null) {
            return queriedParticipation;
        } else {
            Survey survey = surveyService.querySurvey(surveyID);
            Participation newParticipation = new Participation(user, survey);
            survey.getParticipations().add(newParticipation);
            participationRepository.save(newParticipation);
            return newParticipation;
        }
    }
}
