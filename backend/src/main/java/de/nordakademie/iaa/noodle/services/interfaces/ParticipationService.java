package de.nordakademie.iaa.noodle.services.interfaces;

import de.nordakademie.iaa.noodle.dao.ParticipationRepository;
import de.nordakademie.iaa.noodle.model.Participation;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;

/**
 * Service to manage {@link Participation}s.
 *
 * @author Noah Peeters
 * @see ParticipationRepository
 */
public interface ParticipationService {
    /**
     * If the user already has a participation, the participation is returned.
     * Otherwise, a new participation is created.
     *
     * @param user     The user for which the participation is returned.
     * @param surveyID The id of the survey for which the participation is returned.
     * @return The participation of the give user for the given survey.
     * @throws EntityNotFoundException Thrown, when he survey with the given id does not exist.
     */
    Participation getOrCreateParticipation(User user, Long surveyID) throws EntityNotFoundException;
}
