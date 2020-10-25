package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.SurveysApi;
import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class SurveyController extends AuthenticatedController implements SurveysApi {
    @Override
    public ResponseEntity<SurveyMetadata> closeSurvey(Integer id, CloseSurveyRequest closeSurveyRequest) {
        return null;
    }

    @Override
    public ResponseEntity<CreateSurveyResponse> createSurvey(CreateSurveyRequest createSurveyRequest) {
        return null;
    }

    @Override
    public ResponseEntity<SurveyMetadata> deleteSurvey(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<Survey> querySurvey(Integer id) {
        User user = getCurrentUser();

        Survey survey = new Survey();
        survey.setTitle("This is the title for " + id + ".");
        survey.setDescription("You are authenticated as " + user.getFullName());

        survey.setResponses(new ArrayList<>());
        survey.setResponses(new ArrayList<>());

        return ResponseEntity.ok(survey);
    }

    @Override
    public ResponseEntity<QuerySurveysResponse> querySurveys(Optional<Boolean> didParticipateIn, Optional<Boolean> isCompleted, Optional<Boolean> isOwnSurvey, Optional<Boolean> isUpcoming, Optional<Boolean> requiresAttention) {
        return null;
    }

    @Override
    public ResponseEntity<SurveyMetadata> updateSurvey(Integer id, SurveyMetadata surveyMetadata) {
        return null;
    }
}
