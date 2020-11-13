package de.nordakademie.iaa.noodle.services.interfaces;

import de.nordakademie.iaa.noodle.dao.SurveyRepository;
import de.nordakademie.iaa.noodle.dao.model.QuerySurveysItem;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ForbiddenOperationException;
import de.nordakademie.iaa.noodle.services.exceptions.SemanticallyInvalidInputException;
import de.nordakademie.iaa.noodle.services.model.TimeslotCreationData;

import java.util.List;
import java.util.Optional;

/**
 * Service to manage {@link Survey}s.
 *
 * @author Noah Peeters
 * @author Hans Rißer
 * @see SurveyRepository
 */
public interface SurveyService {
    /**
     * Creates a new survey.
     *
     * @param title                     The title of the survey.
     * @param description               The description of the survey.
     * @param timeslotCreationDataInput The timeslots for the new survey.
     * @param creator                   The creator of the survey.
     * @return The new survey.
     * @throws SemanticallyInvalidInputException Thrown, when the timeslots are invalid.
     */
    Survey createSurvey(String title, String description, List<TimeslotCreationData> timeslotCreationDataInput,
                        User creator) throws SemanticallyInvalidInputException;

    /**
     * Updates a survey.
     *
     * @param surveyID                  The id of the survey.
     * @param title                     The new title of the survey.
     * @param description               The new description of the survey.
     * @param timeslotCreationDataInput The new timeslots for the new survey.
     * @param currentUser               The current user.
     * @return The updated survey.
     * @throws EntityNotFoundException           Thrown, when the survey does not exit.
     * @throws SemanticallyInvalidInputException Thrown, when the timeslots are invalid.
     * @throws ForbiddenOperationException       Thrown, when the current used is not allowed to update the survey.
     */
    Survey updateSurvey(Long surveyID, String title, String description,
                        List<TimeslotCreationData> timeslotCreationDataInput, User currentUser)
        throws EntityNotFoundException, SemanticallyInvalidInputException, ForbiddenOperationException;

    /**
     * Closes a survey.
     *
     * @param surveyID    The survey id.
     * @param timeslotID  The timeslot if of the selected timeslot.
     * @param currentUser The current user.
     * @return The updated survey.
     * @throws EntityNotFoundException     Thrown, when the survey or the timeslot does not exist.
     * @throws ForbiddenOperationException Thrown, when the current used is not allowed to close the survey.
     */
    Survey closeSurvey(Long surveyID, Long timeslotID, User currentUser)
        throws EntityNotFoundException, ForbiddenOperationException;

    /**
     * Deletes a survey.
     *
     * @param surveyID    The id of the survey to delete.
     * @param currentUser The current user.
     * @return The deleted survey.
     * @throws EntityNotFoundException     Thrown, when the survey does not exist.
     * @throws ForbiddenOperationException Thrown, when the current used is not allowed to delete the survey.
     */
    Survey deleteSurvey(Long surveyID, User currentUser)
        throws EntityNotFoundException, ForbiddenOperationException;

    /**
     * Queries a single survey.
     *
     * @param id The id of the survey.
     * @return The requested survey.
     * @throws EntityNotFoundException Thrown, when the survey does not exist.
     */
    Survey querySurvey(Long id) throws EntityNotFoundException;

    /**
     * Checks if a user can respond to a survey.
     *
     * @param survey The survey.
     * @param user   The user.
     * @return <code>True</code>, if the user can respond to the survey. <code>False</code> otherwise.
     */
    boolean canUserRespondToSurvey(Survey survey, User user);

    /**
     * Checks if a user can edit a survey.
     *
     * @param survey The survey.
     * @param user   The user.
     * @return <code>True</code>, if the user can edit the survey. <code>False</code> otherwise.
     */
    boolean isSurveyEditableByUser(Survey survey, User user);

    /**
     * Checks if a user can close a survey.
     *
     * @param survey The survey.
     * @param user   The user.
     * @return <code>True</code>, if the user can close the survey. <code>False</code> otherwise.
     */
    boolean isSurveyClosableByUser(Survey survey, User user);

    /**
     * Checks if a user can delete a survey.
     *
     * @param survey The survey.
     * @param user   The user.
     * @return <code>True</code>, if the user can delete the survey. <code>False</code> otherwise.
     */
    boolean isSurveyDeletableByUser(Survey survey, User user);

    /**
     * Query the QuerySurveysItem of multiple surveys which fulfill the given criteria.
     *
     * @param currentUser             The user the other criteria refer to.
     * @param acceptsSelectedTimeslot The user accepted the selected timeslot.
     * @param didParticipateIn        The user has participated in the survey.
     * @param isClosed                The survey is completed.
     * @param isOwnSurvey             The survey was created by the user.
     * @param isUpcoming              The selected timeslot is in the future.
     * @param requiresAttention       THe user's response was discarded due to an update of the survey.
     * @return List of QuerySurveysItems which fulfill the given criteria.
     */
    List<QuerySurveysItem> querySurveys(User currentUser,
                                        Optional<Boolean> acceptsSelectedTimeslot,
                                        Optional<Boolean> didParticipateIn,
                                        Optional<Boolean> isClosed,
                                        Optional<Boolean> isOwnSurvey,
                                        Optional<Boolean> isUpcoming,
                                        Optional<Boolean> requiresAttention);
}
