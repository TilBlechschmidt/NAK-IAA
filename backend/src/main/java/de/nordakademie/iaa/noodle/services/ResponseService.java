package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.ResponseRepository;
import de.nordakademie.iaa.noodle.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ResponseService {
    private final ResponseRepository responseRepository;

    @Autowired
    public ResponseService(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    public Response queryResponse(Long id, Long serviceID) {
        return responseRepository.findByIdAndSurveyId(id, serviceID);
    }
}
