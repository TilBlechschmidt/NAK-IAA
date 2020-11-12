package de.nordakademie.iaa.noodle.services.implementation;

import de.nordakademie.iaa.noodle.dao.ResponseRepository;
import de.nordakademie.iaa.noodle.dao.ResponseTimeslotRepository;
import de.nordakademie.iaa.noodle.model.*;
import de.nordakademie.iaa.noodle.services.exceptions.*;
import de.nordakademie.iaa.noodle.services.interfaces.ParticipationService;
import de.nordakademie.iaa.noodle.services.interfaces.ResponseService;
import de.nordakademie.iaa.noodle.services.interfaces.SurveyService;
import de.nordakademie.iaa.noodle.services.interfaces.TimeslotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Map;

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

    private void addTimeslotsToResponse(Response response, Map<Long, ResponseType> responseTimeslotDataMap)
        throws EntityNotFoundException {
        for (Map.Entry<Long, ResponseType> responseTimeslotData : responseTimeslotDataMap.entrySet()) {
            Timeslot timeslot = timeslotService.findTimeslot(response.getParticipation().getSurvey(),
                responseTimeslotData.getKey());
            ResponseTimeslot responseTimeslot =
                new ResponseTimeslot(response, timeslot, responseTimeslotData.getValue());
            response.getResponseTimeslots().add(responseTimeslot);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateResponse(Long responseID, Long surveyID,
                                   Map<Long, ResponseType> responseTimeslotDataMap, User currentUser)
        throws EntityNotFoundException, SemanticallyInvalidInputException, ForbiddenOperationException {

        if (responseTimeslotDataMap.isEmpty()) {
            throw new SemanticallyInvalidInputException("noTimeslotsSelected");
        }

        Response response = queryResponse(responseID, surveyID);

        boolean isEditable = isResponseEditableByUser(response, currentUser);
        if (!isEditable) {
            throw new ForbiddenOperationException("notEditable");
        }

        responseTimeslotRepository.deleteAllByResponse(response);
        response.getResponseTimeslots().clear();
        addTimeslotsToResponse(response, responseTimeslotDataMap);
        entityManager.flush();
        responseRepository.save(response);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createResponse(Long surveyID, Map<Long, ResponseType> responseTimeslotDataMap, User currentUser)
        throws EntityNotFoundException, ConflictException, SemanticallyInvalidInputException {

        if (responseTimeslotDataMap.isEmpty()) {
            throw new SemanticallyInvalidInputException("noTimeslotsSelected");
        }

        Participation participation = participationService.getOrCreateParticipation(currentUser, surveyID);

        if (participation.getResponse() != null) {
            throw new ConflictException("responseExists");
        }

        Response response = new Response(participation);
        participation.setResponse(response);

        addTimeslotsToResponse(response, responseTimeslotDataMap);
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
}
