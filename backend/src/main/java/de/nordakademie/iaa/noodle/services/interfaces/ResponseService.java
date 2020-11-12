package de.nordakademie.iaa.noodle.services.interfaces;

import de.nordakademie.iaa.noodle.dao.ResponseRepository;
import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.ConflictException;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ForbiddenOperationException;
import de.nordakademie.iaa.noodle.services.exceptions.SemanticallyInvalidInputException;
import de.nordakademie.iaa.noodle.services.model.ResponseValue;

import java.util.List;

/**
 * Service to manage {@link Response}s.
 *
 * @author Noah Peeters
 * @see ResponseRepository
 */
public interface ResponseService {
    /**
     * Queries a single response of a specific survey.
     *
     * @param id       The id of the response.
     * @param surveyID The id of the survey.
     * @return The requested response.
     * @throws EntityNotFoundException, Thrown, when the requested response does not exist.
     */
    Response queryResponse(Long id, Long surveyID) throws EntityNotFoundException;

    /**
     * Updates the response of a user
     *
     * @param responseID     The id of the response.
     * @param surveyID       The id of the survey of the response.
     * @param responseValues The answers to the timeslots.
     * @param currentUser    The current authenticated user.
     * @return The updated response.
     * @throws EntityNotFoundException           Thrown, whe the response or one of the timeslots does not exist.
     * @throws SemanticallyInvalidInputException Thrown, when one of the timeslots is invalid.
     * @throws ForbiddenOperationException       Thrown, when the operation is not permitted.
     */
    Response updateResponse(Long responseID, Long surveyID,
                            List<ResponseValue> responseValues, User currentUser)
        throws EntityNotFoundException, SemanticallyInvalidInputException, ForbiddenOperationException;

    /**
     * Creates a new response for a survey.
     *
     * @param surveyID       The id of the survey.
     * @param responseValues The new answers to the timeslots.
     * @param currentUser    The user to create the response for.
     * @return The new response.
     * @throws EntityNotFoundException           Thrown, whe the survey or one of the timeslots does not exist.
     * @throws ConflictException                 Thrown, when the user already created a response for the survey.
     * @throws SemanticallyInvalidInputException Thrown, when one of the timeslots is invalid.
     */
    Response createResponse(Long surveyID, List<ResponseValue> responseValues, User currentUser)
        throws EntityNotFoundException, ConflictException, SemanticallyInvalidInputException;

    /**
     * Checks if a response is editable by a user.
     *
     * @param response The response to check.
     * @param user     The user to check the permission for.
     * @return True, if the given user can update the response. False otherwise.
     */
    boolean isResponseEditableByUser(Response response, User user);
}
