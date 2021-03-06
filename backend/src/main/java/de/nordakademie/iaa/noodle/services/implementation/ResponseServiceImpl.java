package de.nordakademie.iaa.noodle.services.implementation;

import de.nordakademie.iaa.noodle.dao.ResponseRepository;
import de.nordakademie.iaa.noodle.dao.ResponseTimeslotRepository;
import de.nordakademie.iaa.noodle.model.*;
import de.nordakademie.iaa.noodle.services.exceptions.*;
import de.nordakademie.iaa.noodle.services.interfaces.ParticipationService;
import de.nordakademie.iaa.noodle.services.interfaces.ResponseService;
import de.nordakademie.iaa.noodle.services.interfaces.SurveyService;
import de.nordakademie.iaa.noodle.services.interfaces.TimeslotService;
import de.nordakademie.iaa.noodle.services.model.ResponseValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Service to manage {@link Response}s.
 *
 * @author Noah Peeters
 * @see ResponseRepository
 */
@Service("ResponseService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
public class ResponseServiceImpl implements ResponseService {
    private final ResponseRepository responseRepository;
    private final TimeslotService timeslotService;
    private final ResponseTimeslotRepository responseTimeslotRepository;
    private final ParticipationService participationService;
    private final SurveyService surveyService;
    private final EntityManager entityManager;

    /**
     * Creates a new ResponseService.
     *
     * @param responseRepository         The repository for responses.
     * @param responseTimeslotRepository The repositories for response timeslots.
     * @param timeslotService            The service to manage timeslots.
     * @param participationService       The service to manage participations.
     * @param surveyService              The service to manage surveys.
     * @param entityManager              Manager for the entities.
     */
    @Autowired
    public ResponseServiceImpl(ResponseRepository responseRepository,
                               ResponseTimeslotRepository responseTimeslotRepository,
                               TimeslotService timeslotService,
                               ParticipationService participationService,
                               SurveyService surveyService,
                               EntityManager entityManager) {
        this.responseRepository = responseRepository;
        this.responseTimeslotRepository = responseTimeslotRepository;
        this.timeslotService = timeslotService;
        this.participationService = participationService;
        this.surveyService = surveyService;
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response queryResponse(Long id, Long surveyID) throws EntityNotFoundException {
        Response response = responseRepository.findByIdAndSurveyId(id, surveyID);
        if (response == null) {
            throw new EntityNotFoundException("responseNotFound");
        }
        return response;
    }

    private void addTimeslotsToResponse(Response response, List<ResponseValue> responseValues)
        throws EntityNotFoundException {
        for (ResponseValue responseValue : responseValues) {
            Timeslot timeslot = timeslotService.findTimeslot(response.getParticipation().getSurvey(),
                responseValue.getTimeslotId());
            ResponseTimeslot responseTimeslot =
                new ResponseTimeslot(response, timeslot, responseValue.getResponseType());
            response.getResponseTimeslots().add(responseTimeslot);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateResponse(Long responseID, Long surveyID,
                                   List<ResponseValue> responseValues, User currentUser)
        throws EntityNotFoundException, SemanticallyInvalidInputException, ForbiddenOperationException {

        checkResponseCreationData(responseValues);

        Response response = queryResponse(responseID, surveyID);

        boolean isEditable = isResponseEditableByUser(response, currentUser);
        if (!isEditable) {
            throw new ForbiddenOperationException("notEditable");
        }

        responseTimeslotRepository.deleteAllByResponse(response);
        response.getResponseTimeslots().clear();

        // Here, we have to flush the delete operations because we will insert new response timeslots
        // which (in most cases) will insert new response timeslots for the same response and timeslot.
        // Normally, hibernate first executes the inserts and then the deletions. Due to the the DB constraints,
        // this order is not possible and we manually have to flush the delete operations first.
        entityManager.flush();
        addTimeslotsToResponse(response, responseValues);
        responseRepository.save(response);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createResponse(Long surveyID, List<ResponseValue> responseValues, User currentUser)
        throws EntityNotFoundException, ConflictException, SemanticallyInvalidInputException {

        checkResponseCreationData(responseValues);

        Participation participation = participationService.getOrCreateParticipation(currentUser, surveyID);

        if (participation.getResponse() != null) {
            throw new ConflictException("responseExists");
        }

        Response response = new Response(participation);
        participation.setResponse(response);

        addTimeslotsToResponse(response, responseValues);
        responseRepository.save(response);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isResponseEditableByUser(Response response, User user) {
        Survey survey = response.getParticipation().getSurvey();
        User responseUser = response.getParticipation().getParticipant();

        boolean responderIsCurrentUser = responseUser.equals(user);
        boolean canRespondToSurvey = surveyService.canUserRespondToSurvey(survey, user);

        return responderIsCurrentUser && canRespondToSurvey;
    }

    private void checkResponseCreationData(List<ResponseValue> responseValues)
        throws SemanticallyInvalidInputException {

        if (responseValues.isEmpty()) {
            throw new SemanticallyInvalidInputException("noTimeslotsSelected");
        }

        long distinctCount = responseValues
            .stream()
            .map(ResponseValue::getTimeslotId)
            .distinct()
            .count();

        if (distinctCount != responseValues.size()) {
            throw new SemanticallyInvalidInputException("duplicateTimeslotResponse");
        }
    }
}
