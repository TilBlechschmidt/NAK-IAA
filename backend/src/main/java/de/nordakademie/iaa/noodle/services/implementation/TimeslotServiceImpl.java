package de.nordakademie.iaa.noodle.services.implementation;

import de.nordakademie.iaa.noodle.dao.TimeslotRepository;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.Timeslot;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ServiceException;
import de.nordakademie.iaa.noodle.services.interfaces.TimeslotService;
import de.nordakademie.iaa.noodle.services.model.TimeslotCreationData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Service to manage {@link Timeslot}s.
 *
 * @author Noah Peeters
 * @see TimeslotRepository
 */
@Service("TimeslotService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
public class TimeslotServiceImpl implements TimeslotService {
    private final TimeslotRepository timeslotRepository;

    /**
     * Creates a new TimeslotService.
     *
     * @param timeslotRepository Te repository for timeslots.
     */
    public TimeslotServiceImpl(TimeslotRepository timeslotRepository) {
        this.timeslotRepository = timeslotRepository;
    }

    static private boolean dateEquals(Date a, Date b) {
        if (a == b) {
            return true;
        } else if ((a == null) || (b == null)) {
            return false;
        } else {
            return a.compareTo(b) == 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timeslot findTimeslot(Survey survey, Long timeslotID) throws EntityNotFoundException {
        Timeslot timeslot = timeslotRepository.findById(timeslotID);

        if (timeslot == null || !timeslot.getSurvey().equals(survey)) {
            throw new EntityNotFoundException("timeslotNotFound");
        }

        return timeslot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTimeslotsOfSurvey(Survey survey) {
        timeslotRepository.deleteAllBySurvey(survey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean timeslotCreationDataMatchesTimeslot(TimeslotCreationData timeslotCreationData, Timeslot timeslot) {
        boolean startIsEqual = dateEquals(timeslot.getStart(), timeslotCreationData.getStart());
        boolean endIsEqual = dateEquals(timeslot.getEnd(), timeslotCreationData.getEnd());

        return startIsEqual && endIsEqual;
    }
}
