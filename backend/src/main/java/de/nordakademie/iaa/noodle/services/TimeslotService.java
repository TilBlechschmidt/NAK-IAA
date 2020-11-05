package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.TimeslotRepository;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.Timeslot;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { ServiceException.class })
public class TimeslotService {
    private final TimeslotRepository timeslotRepository;

    public TimeslotService(TimeslotRepository timeslotRepository) {
        this.timeslotRepository = timeslotRepository;
    }

    public Timeslot findTimeslot(Survey survey, Long timeslotID) throws EntityNotFoundException {
        Timeslot timeslot = timeslotRepository.findById(timeslotID);

        if (timeslot == null || !timeslot.getSurvey().equals(survey)) {
            throw new EntityNotFoundException("timeslotNotFound");
        }

        return timeslot;
    }

    public void deleteTimeslotsOfSurvey(Survey survey) {
        timeslotRepository.deleteAllBySurvey(survey);
    }
}
