package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.TimeslotRepository;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.Timeslot;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to manage {@link Timeslot}s.
 *
 * @see TimeslotRepository
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { ServiceException.class })
public class TimeslotService {
    private final TimeslotRepository timeslotRepository;

    /**
     * Creates a new TimeslotService.
     * @param timeslotRepository Te repository for timeslots.
     */
    public TimeslotService(TimeslotRepository timeslotRepository) {
        this.timeslotRepository = timeslotRepository;
    }

    /**
     * Find a timeslot of a survey by its id.
     * @param survey The survey of the timeslot.
     * @param timeslotID The id timeslot.
     * @return The requested timeslot.
     * @throws EntityNotFoundException Thrown, when the timeslot does not exist.
     */
    public Timeslot findTimeslot(Survey survey, Long timeslotID) throws EntityNotFoundException {
        Timeslot timeslot = timeslotRepository.findById(timeslotID);

        if (timeslot == null || !timeslot.getSurvey().equals(survey)) {
            throw new EntityNotFoundException("timeslotNotFound");
        }

        return timeslot;
    }

    /**
     * Deletes all timeslots of a survey.
     * @param survey The survey of which all timeslots will be deleted.
     */
    public void deleteTimeslotsOfSurvey(Survey survey) {
        timeslotRepository.deleteAllBySurvey(survey);
    }
}
