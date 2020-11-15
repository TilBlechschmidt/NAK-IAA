package de.nordakademie.iaa.noodle.services.implementation;

import de.nordakademie.iaa.noodle.dao.SurveyRepository;
import de.nordakademie.iaa.noodle.dao.model.QuerySurveysItem;
import de.nordakademie.iaa.noodle.model.*;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ForbiddenOperationException;
import de.nordakademie.iaa.noodle.services.exceptions.SemanticallyInvalidInputException;
import de.nordakademie.iaa.noodle.services.exceptions.ServiceException;
import de.nordakademie.iaa.noodle.services.interfaces.MailService;
import de.nordakademie.iaa.noodle.services.interfaces.SurveyService;
import de.nordakademie.iaa.noodle.services.interfaces.TimeslotService;
import de.nordakademie.iaa.noodle.services.model.TimeslotCreationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service to manage {@link Survey}s.
 *
 * @author Noah Peeters
 * @author Hans Rißer
 * @see SurveyRepository
 */
@Service("SurveyService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepository surveyRepository;
    private final TimeslotService timeslotService;
    private final MailService mailService;

    @Autowired
    public SurveyServiceImpl(SurveyRepository surveyRepository, TimeslotService timeslotService,
                             MailService mailService) {
        this.surveyRepository = surveyRepository;
        this.timeslotService = timeslotService;
        this.mailService = mailService;
    }

    private Participation getOrCreateCreatorsParticipation(Survey survey) {
        User creator = survey.getCreator();
        for (Participation participation : survey.getParticipations()) {
            if (participation.getParticipant().equals(creator)) {
                return participation;
            }
        }

        Participation participation = new Participation(creator, survey);
        // Don't add the participation to the creator because the user is detached
        survey.getParticipations().add(participation);
        return participation;
    }

    private void addResponseForCreator(Survey survey, List<TimeslotCreationData> timeslotCreationDataList) {
        Participation participation = getOrCreateCreatorsParticipation(survey);

        Response response = new Response(participation);
        participation.setResponse(response);

        for (TimeslotCreationData timeslotCreationData : timeslotCreationDataList) {
            addResponseTimeslotForCreator(survey, response, timeslotCreationData);
        }
    }

    private void addResponseTimeslotForCreator(Survey survey, Response response,
                                               TimeslotCreationData timeslotCreationData) {

        Timeslot timeslot = new Timeslot(survey, timeslotCreationData.getStart(), timeslotCreationData.getEnd());
        survey.getTimeslots().add(timeslot);

        ResponseTimeslot responseTimeslot = new ResponseTimeslot(response, timeslot, ResponseType.YES);
        response.getResponseTimeslots().add(responseTimeslot);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Survey createSurvey(String title, String description, List<TimeslotCreationData> timeslotCreationDataInput,
                               User creator) throws SemanticallyInvalidInputException {
        List<TimeslotCreationData> timeslotCreationData =
            checkSurveyCreationData(title, description, timeslotCreationDataInput);

        Survey survey = new Survey(creator, title, description);
        // Don't add the survey to the creator because the user is detached

        addResponseForCreator(survey, timeslotCreationData);

        surveyRepository.save(survey);
        return survey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Survey updateSurvey(Long surveyID, String title, String description,
                               List<TimeslotCreationData> timeslotCreationDataInput, User currentUser)
        throws EntityNotFoundException, SemanticallyInvalidInputException, ForbiddenOperationException {

        List<TimeslotCreationData> timeslotCreationData =
            checkSurveyCreationData(title, description, timeslotCreationDataInput);

        Survey survey = querySurvey(surveyID);
        if (!isSurveyEditableByUser(survey, currentUser)) {
            throw new ForbiddenOperationException("forbidden");
        }

        if (timeslotsChanged(survey, timeslotCreationData)) {
            List<User> usersWithOutdatedResponses = usersWithOutdatedParticipationsAfterUpdate(survey);
            deleteSurveyResponses(survey);

            survey.setTitle(title);
            survey.setDescription(description);
            addResponseForCreator(survey, timeslotCreationData);
            surveyRepository.save(survey);

            mailService.sendNeedsAttentionMailsAsync(survey, usersWithOutdatedResponses);
        } else {
            survey.setTitle(title);
            survey.setDescription(description);
            surveyRepository.save(survey);
        }

        return survey;
    }

    private boolean timeslotsChanged(Survey survey, List<TimeslotCreationData> timeslotCreationDataList) {
        if (survey.getTimeslots().size() != timeslotCreationDataList.size()) {
            return true;
        }

        return timeslotCreationDataList
            .stream()
            .anyMatch(data ->
                survey.getTimeslots()
                    .stream()
                    .noneMatch(timeslot -> timeslotService.timeslotCreationDataMatchesTimeslot(data, timeslot)));
    }

    private void deleteSurveyResponses(Survey survey) {
        survey.getParticipations().forEach(participation -> participation.setResponse(null));
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Survey closeSurvey(Long surveyID, Long timeslotID, User currentUser)
        throws EntityNotFoundException, ForbiddenOperationException {

        Survey survey = querySurvey(surveyID);
        if (!isSurveyClosableByUser(survey, currentUser)) {
            throw new ForbiddenOperationException("forbidden");
        }

        Timeslot timeslot = timeslotService.findTimeslot(survey, timeslotID);
        survey.setSelectedTimeslot(timeslot);
        surveyRepository.save(survey);
        mailService.sendSurveyClosedMailsAsync(survey);

        return survey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Survey deleteSurvey(Long surveyID, User currentUser)
        throws EntityNotFoundException, ForbiddenOperationException {
        Survey survey = querySurvey(surveyID);
        if (!isSurveyDeletableByUser(survey, currentUser)) {
            throw new ForbiddenOperationException("forbidden");
        }
        surveyRepository.delete(survey);
        return survey;
    }

    private List<TimeslotCreationData> checkSurveyCreationData(String title, String description,
                                                               List<TimeslotCreationData> timeslotCreationDataList)
        throws SemanticallyInvalidInputException {
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

        for (TimeslotCreationData timeslotCreationData : timeslotCreationDataList) {
            if (!isTimeslotCreationDataValid(timeslotCreationData)) {
                throw new SemanticallyInvalidInputException("invalidTimeslot");
            }
        }

        return timeslotCreationDataList.stream().distinct().collect(Collectors.toList());
    }

    private boolean isTimeslotCreationDataValid(TimeslotCreationData timeslotCreationData) {
        return timeslotCreationData.getEnd() == null ||
               timeslotCreationData.getStart().before(timeslotCreationData.getEnd());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Survey querySurvey(Long id) throws EntityNotFoundException {
        Survey survey = surveyRepository.findById(id);
        if (survey == null) {
            throw new EntityNotFoundException("surveyNotFound");
        }
        return survey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canUserRespondToSurvey(Survey survey, User user) {
        boolean surveyAcceptsResponses = !survey.getIsClosed();
        boolean isSurveyCreator = survey.getCreator().equals(user);
        return surveyAcceptsResponses && !isSurveyCreator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSurveyEditableByUser(Survey survey, User user) {
        return survey.getCreator().equals(user) && !survey.getIsClosed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSurveyClosableByUser(Survey survey, User user) {
        return isSurveyEditableByUser(survey, user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSurveyDeletableByUser(Survey survey, User user) {
        return survey.getCreator().equals(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QuerySurveysItem> querySurveys(User currentUser,
                                               Optional<Boolean> acceptsSelectedTimeslot,
                                               Optional<Boolean> didParticipateIn,
                                               Optional<Boolean> isClosed,
                                               Optional<Boolean> isOwnSurvey,
                                               Optional<Boolean> isUpcoming,
                                               Optional<Boolean> requiresAttention) {

        return surveyRepository.querySurveys(
            currentUser.getId(),
            acceptsSelectedTimeslot.orElse(null),
            didParticipateIn.orElse(null),
            isClosed.orElse(null),
            isOwnSurvey.orElse(null),
            isUpcoming.orElse(null),
            requiresAttention.orElse(null));
    }
}
