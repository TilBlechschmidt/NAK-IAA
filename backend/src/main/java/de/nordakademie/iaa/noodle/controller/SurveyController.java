package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.SurveysApi;
import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.mapper.SurveyMapper;
import de.nordakademie.iaa.noodle.mapper.TimeslotMapper;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.SurveyService;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ForbiddenOperationException;
import de.nordakademie.iaa.noodle.services.exceptions.SemanticallyInvalidInputException;
import de.nordakademie.iaa.noodle.services.model.TimeslotCreationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * FIXME: Implement this
 * This is mostly a stub to show the structure of future controllers.
 */
@RestController
public class SurveyController extends AuthenticatedController implements SurveysApi {
    private final SurveyService surveyService;
    private final SurveyMapper surveyMapper;
    private final TimeslotMapper timeslotMapper;

    @Autowired
    public SurveyController(SurveyService surveyService, SurveyMapper surveyMapper, TimeslotMapper timeslotMapper) {
        this.surveyService = surveyService;
        this.surveyMapper = surveyMapper;
        this.timeslotMapper = timeslotMapper;
    }

    @Override
    public ResponseEntity<SurveyMetadataDTO> closeSurvey(Long id, CloseSurveyRequest closeSurveyRequest) {
        try {
            if (closeSurveyRequest.getOperation() == CloseSurveyRequest.OperationEnum.CLOSE) {
                Survey survey = surveyService.closeSurvey(id,
                    closeSurveyRequest.getSelectedTimeslot(),
                    getCurrentUser());
                SurveyMetadataDTO surveyMetadataDTO = surveyMapper.surveyToMetadataDTO(survey, getCurrentUser());
                return ResponseEntity.ok(surveyMetadataDTO);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsupportedOperation");
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ForbiddenOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<SurveyMetadataDTO> createSurvey(SurveyCreationMetadataDTO surveyCreationMetadataDTO) {
        try {
            User currentUser = getCurrentUser();
            List<TimeslotCreationData> timeslotCreationDataList = timeslotMapper.timeslotCreationDTOsToData(surveyCreationMetadataDTO.getTimeslots());

            Survey survey = surveyService.createSurvey(surveyCreationMetadataDTO.getTitle(),
                surveyCreationMetadataDTO.getDescription(),
                timeslotCreationDataList,
                currentUser);

            SurveyMetadataDTO surveyMetadataDTO = surveyMapper.surveyToMetadataDTO(survey, currentUser);
            return ResponseEntity.status(CREATED).body(surveyMetadataDTO);
        } catch (SemanticallyInvalidInputException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<SurveyMetadataDTO> deleteSurvey(Long id) {
        try {
            Survey survey = surveyService.deleteSurvey(id, getCurrentUser());
            SurveyMetadataDTO surveyMetadataDTO = surveyMapper.surveyToMetadataDTO(survey, getCurrentUser());
            return ResponseEntity.ok(surveyMetadataDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ForbiddenOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<SurveyDTO> querySurvey(Long id) {
        try {
            Survey survey = surveyService.querySurvey(id);
            SurveyDTO surveyDTO = surveyMapper.surveyToDTO(survey, getCurrentUser());
            return ResponseEntity.ok(surveyDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<QuerySurveysResponse> querySurveys(
        Optional<Boolean> didParticipateIn, Optional<Boolean> isCompleted,
        Optional<Boolean> isOwnSurvey, Optional<Boolean> isUpcoming, Optional<Boolean> requiresAttention) {

        return ResponseEntity.ok(surveyMapper.surveysToSurveysDTO(
            surveyService.querySurvey(getCurrentUser(),
                didParticipateIn,
                isCompleted,
                isOwnSurvey,
                isUpcoming,
                requiresAttention)));
    }

    @Override
    public ResponseEntity<SurveyMetadataDTO> updateSurvey(Long id, SurveyCreationMetadataDTO surveyCreationMetadataDTO) {
        try {
            User currentUser = getCurrentUser();
            List<TimeslotCreationData> timeslotCreationDataList = timeslotMapper.timeslotCreationDTOsToData(surveyCreationMetadataDTO.getTimeslots());

            Survey survey = surveyService.updateSurvey(id,
                surveyCreationMetadataDTO.getTitle(),
                surveyCreationMetadataDTO.getDescription(),
                timeslotCreationDataList,
                currentUser);

            SurveyMetadataDTO surveyMetadataDTO = surveyMapper.surveyToMetadataDTO(survey, currentUser);
            return ResponseEntity.ok(surveyMetadataDTO);
        } catch (SemanticallyInvalidInputException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ForbiddenOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
