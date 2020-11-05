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

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { ServiceException.class })
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final TimeslotService timeslotService;
    private final ResponseTimeslotRepository responseTimeslotRepository;
    private final ParticipationService participationService;
    private final SurveyService surveyService;

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
            // TODO: Fix null ids
            response.getResponseTimeslots().add(responseTimeslot);
        }
    }

    public Response updateResponse(Long responseID, Long surveyID,
                                   Map<Long, ResponseType> responseTimeslotDataMap, User currentUser)
        throws EntityNotFoundException, SemanticallyInvalidInputException, ForbiddenOperationException {

        if (responseTimeslotDataMap.size() == 0) { throw new SemanticallyInvalidInputException("noTimeslotsSelected"); }

        Response response = queryResponse(responseID, surveyID);

        boolean isEditable = isResponseEditableByUser(response, currentUser);
        if (!isEditable) { throw new ForbiddenOperationException("notEditable"); }

        responseTimeslotRepository.deleteAllByResponse(response);
        response.getResponseTimeslots().clear();
        addTimeslotsToResponse(response, responseTimeslotDataMap);
        responseRepository.save(response);
        return response;
    }

    public Response createResponse(Long surveyID, Map<Long, ResponseType> responseTimeslotDataMap, User currentUser)
        throws EntityNotFoundException, ConflictException, SemanticallyInvalidInputException {

        if (responseTimeslotDataMap.size() == 0) {  throw new SemanticallyInvalidInputException("noTimeslotsSelected"); }

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

    public boolean isResponseEditableByUser(Response response, User user) {
        Survey survey = response.getParticipation().getSurvey();
        User responseUser = response.getParticipation().getParticipant();

        boolean responderIsCurrentUser = responseUser.equals(user);
        boolean canRespondToSurvey = surveyService.canUserRespondToSurvey(survey, user);

        return responderIsCurrentUser && canRespondToSurvey;
    }
}
