package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.model.ResponseTimeslot;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for responses timeslots.
 */
@Repository
@RepositoryDefinition(idClass = Long.class, domainClass = ResponseTimeslot.class)
@Transactional(propagation = Propagation.REQUIRED)
public interface ResponseTimeslotRepository {
    /**
     * Deletes all response timeslots of a response.
     * @param response The response of which the response timeslots will be deleted.
     */
    void deleteAllByResponse(Response response);
}
