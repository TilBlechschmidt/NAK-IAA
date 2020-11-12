package de.nordakademie.iaa.noodle.services.interfaces;

import de.nordakademie.iaa.noodle.dao.TimeslotRepository;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.Timeslot;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.model.TimeslotCreationData;

/**
 * Service to manage {@link Timeslot}s.
 *
 * @author Noah Peeters
 * @see TimeslotRepository
 */
public interface TimeslotService {
    /**
     * Find a timeslot of a survey by its id.
     *
     * @param survey     The survey of the timeslot.
     * @param timeslotID The id timeslot.
     * @return The requested timeslot.
     * @throws EntityNotFoundException Thrown, when the timeslot does not exist.
     */
    Timeslot findTimeslot(Survey survey, Long timeslotID) throws EntityNotFoundException;

    /**
     * Deletes all timeslots of a survey.
     *
     * @param survey The survey of which all timeslots will be deleted.
     */
    void deleteTimeslotsOfSurvey(Survey survey);

    /**
     * Checks if the given timeslot creation data produce a timeslot with the same dates as the given timeslot.
     *
     * @param timeslotCreationData The creation data for the timeslot.
     * @param timeslot             The timeslot to check against.
     * @return True, if the timeslot produces by the creation data has the same start and end date as the given
     * timeslot. False otherwise.
     */
    boolean timeslotCreationDataMatchesTimeslot(TimeslotCreationData timeslotCreationData, Timeslot timeslot);
}
