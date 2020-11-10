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

/**
 * Service to manage {@link Participation}s.
 *
 * @author Noah Peeters
 * @see ParticipationRepository
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final SurveyService surveyService;

    /**
     * Creates a new ParticipationService.
     *
     * @param participationRepository The repository for participations.
     * @param surveyService           The service to manage surveys.
     */
    @Autowired
    public ParticipationService(ParticipationRepository participationRepository, SurveyService surveyService) {
        this.participationRepository = participationRepository;
        this.surveyService = surveyService;
    }

    /**
     * If the user already has a participation, the participation is returned.
     * Otherwise, a new participation is created.
     *
     * @param user     The user for which the participation is returned.
     * @param surveyID The id of the survey for which the participation is returned.
     * @return The participation of the give user for the given survey.
     * @throws EntityNotFoundException Thrown, when he survey with the given id does not exist.
     */
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
