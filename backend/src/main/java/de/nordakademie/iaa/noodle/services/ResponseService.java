package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.ResponseRepository;
import de.nordakademie.iaa.noodle.dao.ResponseTimeslotRepository;
import de.nordakademie.iaa.noodle.model.*;
import de.nordakademie.iaa.noodle.services.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;

/**
 * Service to manage {@link Response}s.
 *
 * @see ResponseRepository
 *
 * @author Noah Peeters
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { ServiceException.class })
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final TimeslotService timeslotService;
    private final ResponseTimeslotRepository responseTimeslotRepository;
    private final ParticipationService participationService;
    private final SurveyService surveyService;

    /**
     * Creates a new ResponseService.
     * @param responseRepository The repository for responses.
     * @param responseTimeslotRepository The repositories for response timeslots.
     * @param timeslotService The service to manage timeslots.
     * @param participationService The service to manage participations.
     * @param surveyService The service to manage surveys.
     */
    @Autowired
    public ResponseService(ResponseRepository responseRepository,
                           ResponseTimeslotRepository responseTimeslotRepository,
                           TimeslotService timeslotService,
                           ParticipationService participationService,
                           SurveyService surveyService) {
        this.responseRepository = responseRepository;
        this.responseTimeslotRepository = responseTimeslotRepository;
        this.timeslotService = timeslotService;
        this.participationService = participationService;
        this.surveyService = surveyService;
    }

    /**
     * Queries a single response of a specific survey.
     * @param id The id of the response.
     * @param surveyID The id of the survey.
     * @return The requested response.
     * @throws EntityNotFoundException, Thrown, when the requested response does not exist.
     */
    public Response queryResponse(Long id, Long surveyID) throws EntityNotFoundException {
        Response response = responseRepository.findByIdAndSurveyId(id, surveyID);
        if (response == null) { throw new EntityNotFoundException("responseNotFound"); }
        return response;
    }

    private void addTimeslotsToResponse(Response response, Map<Long, ResponseType> responseTimeslotDataMap)
        throws EntityNotFoundException {
        for (Map.Entry<Long, ResponseType> responseTimeslotData: responseTimeslotDataMap.entrySet()) {
            Timeslot timeslot = timeslotService.findTimeslot(response.getParticipation().getSurvey(),
                                                             responseTimeslotData.getKey());
            ResponseTimeslot responseTimeslot = new ResponseTimeslot(response, timeslot, responseTimeslotData.getValue());
            response.getResponseTimeslots().add(responseTimeslot);
        }
    }

    /**
     * Updates the response of a user
     * @param responseID The id of the response.
     * @param surveyID The id of the survey of the response.
     * @param responseTimeslotDataMap The new answers to the timeslots.
     *                                The key is the timeslot id.
     *                                The value is the new answer.
     * @param currentUser The current authenticated user.
     * @return The updated response.
     * @throws EntityNotFoundException Thrown, whe the response or one of the timeslots does not exist.
     * @throws SemanticallyInvalidInputException Thrown, when one of the timeslots is invalid.
     * @throws ForbiddenOperationException Thrown, when the operation is not permitted.
     */
    public Response updateResponse(Long responseID, Long surveyID,
                                   Map<Long, ResponseType> responseTimeslotDataMap, User currentUser)
        throws EntityNotFoundException, SemanticallyInvalidInputException, ForbiddenOperationException {

        if (responseTimeslotDataMap.isEmpty()) { throw new SemanticallyInvalidInputException("noTimeslotsSelected"); }

        Response response = queryResponse(responseID, surveyID);

        boolean isEditable = isResponseEditableByUser(response, currentUser);
        if (!isEditable) { throw new ForbiddenOperationException("notEditable"); }

        responseTimeslotRepository.deleteAllByResponse(response);
        response.getResponseTimeslots().clear();
        addTimeslotsToResponse(response, responseTimeslotDataMap);
        responseRepository.save(response);
        return response;
    }

    /**
     * Creates a new response for a survey.
     * @param surveyID The id of the survey.
     * @param responseTimeslotDataMap The new answers to the timeslots.
     *                                The key is the timeslot id.
     *                                The value is the new answer.
     * @param currentUser The user to create the response for.
     * @return The new response.
     * @throws EntityNotFoundException Thrown, whe the survey or one of the timeslots does not exist.
     * @throws ConflictException Thrown, when the user already created a response for the survey.
     * @throws SemanticallyInvalidInputException Thrown, when one of the timeslots is invalid.
     */
    public Response createResponse(Long surveyID, Map<Long, ResponseType> responseTimeslotDataMap, User currentUser)
        throws EntityNotFoundException, ConflictException, SemanticallyInvalidInputException {

        if (responseTimeslotDataMap.isEmpty()) { throw new SemanticallyInvalidInputException("noTimeslotsSelected"); }

        Participation participation = participationService.getOrCreateParticipation(currentUser, surveyID);

        if (participation.getResponse() != null) {
            throw new ConflictException("responseExists");
        }

        Response response = new Response(participation, new HashSet<>());
        participation.setResponse(response);

        addTimeslotsToResponse(response, responseTimeslotDataMap);
        responseRepository.save(response);
        return response;
    }

    /**
     * Checks if a response is editable by a user.
     * @param response The response to check.
     * @param user The user to check the permission for.
     * @return True, if the given user can update the response. False otherwise.
     */
    public boolean isResponseEditableByUser(Response response, User user) {
        Survey survey = response.getParticipation().getSurvey();
        User responseUser = response.getParticipation().getParticipant();

        boolean responderIsCurrentUser = responseUser.equals(user);
        boolean canRespondToSurvey = surveyService.canUserRespondToSurvey(survey, user);

        return responderIsCurrentUser && canRespondToSurvey;
    }
}
