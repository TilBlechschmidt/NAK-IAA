package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.SurveysApi;
import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.converter.SurveyConverter;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.services.SurveyService;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * FIXME: Implement this
 * This is mostly a stub to show the structure of future controllers.
 */
@RestController
public class SurveyController extends AuthenticatedController implements SurveysApi {
    private final SurveyService surveyService;
    private final SurveyConverter surveyConverter;

    @Autowired
    public SurveyController(SurveyService surveyService, SurveyConverter surveyConverter) {
        this.surveyService = surveyService;
        this.surveyConverter = surveyConverter;
    }

    @Override
    public ResponseEntity<SurveyMetadataDTO> closeSurvey(Long id, CloseSurveyRequest closeSurveyRequest) {
        return null;
    }

    @Override
    public ResponseEntity<SurveyMetadataDTO> createSurvey(SurveyCreationMetadataDTO surveyCreationMetadataDTO) {
        return null;
    }

    @Override
    public ResponseEntity<SurveyMetadataDTO> deleteSurvey(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<SurveyDTO> querySurvey(Long id) {
        try {
            Survey survey = surveyService.querySurvey(id);
            SurveyDTO surveyDTO = surveyConverter.convertSurveyToDTO(survey, getCurrentUser());
            return ResponseEntity.ok(surveyDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<QuerySurveysResponse> querySurveys(
        Optional<Boolean> didParticipateIn, Optional<Boolean> isCompleted,
        Optional<Boolean> isOwnSurvey, Optional<Boolean> isUpcoming, Optional<Boolean> requiresAttention) {
        return null;
    }

    @Override
    public ResponseEntity<SurveyMetadataDTO> updateSurvey(Long id, SurveyCreationMetadataDTO surveyCreationMetadataDTO) {
        return null;
    }
}
