package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.SurveysApi;
import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

/**
 * FIXME: Implement this
 * This is mostly a stub to show the structure of future controllers.
 */
@RestController
public class SurveyController extends AuthenticatedController implements SurveysApi {
    @Override
    public ResponseEntity<SurveyMetadataDTO> closeSurvey(Long id, CloseSurveyRequest closeSurveyRequest) {
        return null;
    }

    @Override
    public ResponseEntity<CreateSurveyResponse> createSurvey(CreateSurveyRequest createSurveyRequest) {
        return null;
    }

    @Override
    public ResponseEntity<SurveyMetadataDTO> deleteSurvey(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<SurveyDTO> querySurvey(Long id) {
        User user = getCurrentUser();

        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setTitle("This is the title for " + id + ".");
        surveyDTO.setDescription("You are authenticated as " + user.getFullName());

        surveyDTO.setResponses(new ArrayList<>());
        surveyDTO.setResponses(new ArrayList<>());

        return ResponseEntity.ok(surveyDTO);
    }

    @Override
    public ResponseEntity<QuerySurveysResponse> querySurveys(
        Optional<Boolean> didParticipateIn, Optional<Boolean> isCompleted,
        Optional<Boolean> isOwnSurvey, Optional<Boolean> isUpcoming, Optional<Boolean> requiresAttention) {
        return null;
    }

    @Override
    public ResponseEntity<SurveyMetadataDTO> updateSurvey(Long id, SurveyMetadataDTO surveyMetadata) {
        return null;
    }
}
