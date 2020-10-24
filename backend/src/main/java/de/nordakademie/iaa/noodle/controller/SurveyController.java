package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.SurveysApi;
import de.nordakademie.iaa.noodle.api.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class SurveyController implements SurveysApi {
    @Override
    public ResponseEntity<SurveyMetadata> closeSurvey(Integer id, String authorization) {
        return null;
    }

    @Override
    public ResponseEntity<CreateSurveyResponse> createSurvey(String authorization, CreateSurveyRequest createSurveyRequest) {
        return null;
    }

    @Override
    public ResponseEntity<SurveyMetadata> deleteSurvey(Integer id, String authorization) {
        return null;
    }

    @Override
    public ResponseEntity<Survey> querySurvey(Integer id, String authorization) {
        Survey survey = new Survey();
        survey.setTitle("This is the title for " + id + ".");
        survey.setDescription("This is the description");

        survey.setResponses(new ArrayList<>());
        survey.setResponses(new ArrayList<>());

        return ResponseEntity.ok(survey);
    }

    @Override
    public ResponseEntity<QuerySurveysResponse> querySurveys(String authorization, Optional<Integer> createdBy, Optional<Integer> needsAttentionBy, Optional<Integer> notParticipatedBy, Optional<Integer> participatedBy) {
        return null;
    }

    @Override
    public ResponseEntity<SurveyMetadata> updateSurvey(Integer id, String authorization, SurveyMetadata surveyMetadata) {
        return null;
    }
}
