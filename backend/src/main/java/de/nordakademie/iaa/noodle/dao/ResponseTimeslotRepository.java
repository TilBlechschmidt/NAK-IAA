package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.model.ResponseTimeslot;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RepositoryDefinition(idClass = Long.class, domainClass = ResponseTimeslot.class)
@Transactional(propagation = Propagation.REQUIRED)
public interface ResponseTimeslotRepository {
    void deleteAllByResponse(Response response);
}
