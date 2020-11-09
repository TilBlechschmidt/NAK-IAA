package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.Timeslot;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for {@link Timeslot}s.
 */
@Repository
@RepositoryDefinition(idClass = Long.class, domainClass = Timeslot.class)
@Transactional(propagation = Propagation.REQUIRED)
public interface TimeslotRepository {
    /**
     * Queries a single timeslot by its id.
     * @param timeslotID The id of the timeslot.
     * @return The requested timeslot or null if it does not exist.
     */
    Timeslot findById(Long timeslotID);
    void deleteAllBySurvey(Survey survey);
}
