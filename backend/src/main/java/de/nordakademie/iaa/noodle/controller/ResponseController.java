package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.ResponsesApi;
import de.nordakademie.iaa.noodle.api.model.CreateResponseRequest;
import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
import de.nordakademie.iaa.noodle.mapper.ResponseMapper;
import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.model.ResponseType;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.ConflictException;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ForbiddenOperationException;
import de.nordakademie.iaa.noodle.services.exceptions.SemanticallyInvalidInputException;
import de.nordakademie.iaa.noodle.services.interfaces.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * Rest controller for all routes regarding {@link Response}s.
 *
 * @author Noah Peeters
 * @see ResponseService
 * @see ResponseMapper
 */
@RestController
public class ResponseController extends AuthenticatedController implements ResponsesApi {
    private final ResponseService responseService;
    private final ResponseMapper responseMapper;

    /**
     * Creates a new ResponseController.
     *
     * @param responseService Services used for operations on responses.
     * @param responseMapper  Service used to map responses to ResponseDTOs.
     */
    @Autowired
    public ResponseController(ResponseService responseService, ResponseMapper responseMapper) {
        this.responseService = responseService;
        this.responseMapper = responseMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<ResponseDTO> createResponse(Long surveyID, CreateResponseRequest createResponseRequest) {
        try {
            User currentUser = getCurrentUser();
            Map<Long, ResponseType> responseTimeslotDataMap = responseMapper
                .responseValueDTOsToMap(createResponseRequest.getValues());
            Response response = responseService.createResponse(surveyID, responseTimeslotDataMap, currentUser);
            ResponseDTO responseDTO = responseMapper.responseToDTO(response, currentUser);
            return ResponseEntity.status(CREATED).body(responseDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (SemanticallyInvalidInputException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<ResponseDTO> queryResponse(Long responseID, Long surveyID) {
        try {
            Response response = responseService.queryResponse(responseID, surveyID);
            ResponseDTO responseDTO = responseMapper.responseToDTO(response, getCurrentUser());
            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<ResponseDTO> updateResponse(Long responseID, Long surveyID,
                                                      CreateResponseRequest createResponseRequest) {
        try {
            User currentUser = getCurrentUser();
            Map<Long, ResponseType> responseTimeslotDataMap = responseMapper
                .responseValueDTOsToMap(createResponseRequest.getValues());
            Response response = responseService.updateResponse(responseID,
                surveyID,
                responseTimeslotDataMap,
                currentUser);
            ResponseDTO responseDTO = responseMapper.responseToDTO(response, currentUser);
            return ResponseEntity.status(CREATED).body(responseDTO);
        } catch (SemanticallyInvalidInputException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ForbiddenOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
