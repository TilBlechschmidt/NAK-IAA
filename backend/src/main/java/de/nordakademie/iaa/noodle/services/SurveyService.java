package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.ParticipationRepository;
import de.nordakademie.iaa.noodle.dao.model.QuerySurveysItem;
import de.nordakademie.iaa.noodle.dao.ResponseRepository;
import de.nordakademie.iaa.noodle.dao.SurveyRepository;
import de.nordakademie.iaa.noodle.model.*;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ForbiddenOperationException;
import de.nordakademie.iaa.noodle.services.exceptions.SemanticallyInvalidInputException;
import de.nordakademie.iaa.noodle.services.exceptions.ServiceException;
import de.nordakademie.iaa.noodle.services.model.TimeslotCreationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final TimeslotService timeslotService;
    private final ResponseRepository responseRepository;
    private final ParticipationRepository participationRepository;
    private final MailService mailService;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository, TimeslotService timeslotService,
                         ResponseRepository responseRepository, ParticipationRepository participationRepository,
                         MailService mailService) {
        this.surveyRepository = surveyRepository;
        this.timeslotService = timeslotService;
        this.responseRepository = responseRepository;
        this.participationRepository = participationRepository;
        this.mailService = mailService;
    }

    private void addParticipationForCreator(Survey survey, List<TimeslotCreationData> timeslotCreationDataList) throws SemanticallyInvalidInputException {
        Participation participation = new Participation(survey.getCreator(), survey, null);
        // Don't add the participation to the creator because the user is detached
        survey.getParticipations().add(participation);

        Response response = new Response(participation, new HashSet<>());
        participation.setResponse(response);

        for (TimeslotCreationData timeslotCreationData : timeslotCreationDataList) {
            addResponseTimeslotForCreator(survey, response, timeslotCreationData);
        }
    }

    private void addResponseTimeslotForCreator(Survey survey, Response response, TimeslotCreationData timeslotCreationData) throws SemanticallyInvalidInputException {
        if (!isTimeslotCreationDataValid(timeslotCreationData)) {
            throw new SemanticallyInvalidInputException("invalidTimeslot");
        }

        Timeslot timeslot = new Timeslot(survey, timeslotCreationData.getStart(), timeslotCreationData.getEnd());
        survey.getTimeslots().add(timeslot);

        ResponseTimeslot responseTimeslot = new ResponseTimeslot(response, timeslot, ResponseType.YES);
        response.getResponseTimeslots().add(responseTimeslot);
    }

    public Survey createSurvey(String title, String description, List<TimeslotCreationData> timeslotCreationDataList, User creator) throws SemanticallyInvalidInputException {
        checkSurveyCreationData(title, description, timeslotCreationDataList);

        Survey survey = new Survey(new HashSet<>(), null, creator, new HashSet<>(), title, description);
        // Don't add the survey to the creator because the user is detached

        addParticipationForCreator(survey, timeslotCreationDataList);

        surveyRepository.save(survey);
        return survey;
    }

    public Survey updateSurvey(Long surveyID, String title, String description, List<TimeslotCreationData> timeslotCreationDataList, User currentUser) throws EntityNotFoundException, SemanticallyInvalidInputException, ForbiddenOperationException {
        checkSurveyCreationData(title, description, timeslotCreationDataList);

        Survey survey = querySurvey(surveyID);
        if (!isSurveyEditableByUser(survey, currentUser)) {
            throw new ForbiddenOperationException("forbidden");
        }

        List<User> usersWithOutdatedResponses = usersWithOutdatedParticipationsAfterUpdate(survey);
        deleteSurveyResponses(survey);

        survey.setTitle(title);
        survey.setDescription(description);
        addParticipationForCreator(survey, timeslotCreationDataList);
        surveyRepository.save(survey);

        mailService.sendNeedsAttentionMailsAsync(survey, usersWithOutdatedResponses);

        return survey;
    }

    private void deleteSurveyResponses(Survey survey) {
        for (Participation participation : survey.getParticipations()) {
            Response response = participation.getResponse();
            if (response != null) {
                responseRepository.delete(response);
                participation.setResponse(null);
            }

            if (participation.getParticipant().equals(survey.getCreator())) {
                participationRepository.delete(participation);
            }
        }

        timeslotService.deleteTimeslotsOfSurvey(survey);
        survey.getTimeslots().clear();
    }

    private List<User> usersWithOutdatedParticipationsAfterUpdate(Survey survey) {
        User creator = survey.getCreator();
        return survey.getParticipations()
            .stream()
            .filter(participation -> participation.getResponse() != null)
            .map(Participation::getParticipant)
            .filter(user -> !creator.equals(user))
            .collect(Collectors.toList());
    }

    public Survey closeSurvey(Long surveyID, Long timeslotID, User currentUser)
        throws EntityNotFoundException, ForbiddenOperationException {

        Survey survey = querySurvey(surveyID);
        if (!isSurveyClosableByUser(survey, currentUser)) {
            throw new ForbiddenOperationException("forbidden");
        }

        Timeslot timeslot = timeslotService.findTimeslot(survey, timeslotID);
        survey.setChosenTimeslot(timeslot);
        surveyRepository.save(survey);

        return survey;
    }

    public Survey deleteSurvey(Long surveyID, User currentUser) throws EntityNotFoundException, ForbiddenOperationException {
        Survey survey = querySurvey(surveyID);
        if (!isSurveyDeletableByUser(survey, currentUser)) {
            throw new ForbiddenOperationException("forbidden");
        }
        surveyRepository.delete(survey);
        return survey;
    }

    public void checkSurveyCreationData(String title, String description, List<TimeslotCreationData> timeslotCreationDataList) throws SemanticallyInvalidInputException {
        if (timeslotCreationDataList.size() == 0) {
            throw new SemanticallyInvalidInputException("noTimeslots");
        }
        if (title.isBlank()) {
            throw new SemanticallyInvalidInputException("emptyTitle");
        }
        if (title.length() > 2048) {
            throw new SemanticallyInvalidInputException("titleTooLong");
        }
        if (description.length() > 2048) {
            throw new SemanticallyInvalidInputException("descriptionTooLong");
        }
    }

    public boolean isTimeslotCreationDataValid(TimeslotCreationData timeslotCreationData) {
        return timeslotCreationData.getEnd() == null ||
            timeslotCreationData.getStart().before(timeslotCreationData.getEnd());
    }

    public Survey querySurvey(Long id) throws EntityNotFoundException {
        Survey survey = surveyRepository.findById(id);
        if (survey == null) {
            throw new EntityNotFoundException("surveyNotFound");
        }
        return survey;
    }


    public boolean canUserRespondToSurvey(Survey survey, User user) {
        boolean surveyAcceptsResponses = !survey.getIsClosed();
        boolean isSurveyCreator = survey.getCreator().equals(user);
        return surveyAcceptsResponses && !isSurveyCreator;
    }

    public boolean isSurveyEditableByUser(Survey survey, User user) {
        return survey.getCreator().equals(user) && !survey.getIsClosed();
    }

    public boolean isSurveyClosableByUser(Survey survey, User user) {
        return isSurveyEditableByUser(survey, user);
    }

    public boolean isSurveyDeletableByUser(Survey survey, User user) {
        return survey.getCreator().equals(user);
    }

    public List<QuerySurveysItem> querySurveys(User currentUser, Optional<Boolean> didParticipateIn,
                                               Optional<Boolean> isCompleted, Optional<Boolean> isOwnSurvey,
                                               Optional<Boolean> isUpcoming, Optional<Boolean> requiresAttention) {

        return surveyRepository.querySurvey(currentUser.getId(),
            didParticipateIn.orElse(null), isCompleted.orElse(null), isOwnSurvey.orElse(null),
            isUpcoming.orElse(null), requiresAttention.orElse(null));
    }
}
