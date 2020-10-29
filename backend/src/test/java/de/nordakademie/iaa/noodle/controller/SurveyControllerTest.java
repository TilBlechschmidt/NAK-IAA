package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class SurveyControllerTest {
    private SurveyController surveyController;

    @BeforeEach
    public void setUp() {
        surveyController = new SurveyController();
    }

    @Test
    public void testCloseSurvey() {
        CloseSurveyRequest closeSurveyRequest = new CloseSurveyRequest();
        ResponseEntity<SurveyMetadata> response = surveyController.closeSurvey(42, "Fake", closeSurveyRequest);
        assertNull(response);
    }

    @Test
    public void testCreateSurvey() {
        CreateSurveyRequest createSurveyRequest = mock(CreateSurveyRequest.class);
        ResponseEntity<CreateSurveyResponse> response = surveyController.createSurvey("Fake", createSurveyRequest);
        assertNull(response);
    }

    @Test
    public void testDeleteSurvey() {
        ResponseEntity<SurveyMetadata> response = surveyController.deleteSurvey(42, "Fake");
        assertNull(response);
    }


    @Test
    public void testQuerySurvey() {
        ResponseEntity<Survey> response = surveyController.querySurvey(42, "Fake");
        Survey survey = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(survey);
        assertEquals("This is the title for 42.", survey.getTitle());
        assertEquals("This is the description", survey.getDescription());
        assertArrayEquals(Collections.EMPTY_LIST.toArray(), survey.getResponses().toArray());
        assertArrayEquals(Collections.EMPTY_LIST.toArray(), survey.getTimeslots().toArray());
    }

    @Test
    public void testQuerySurveys() {
        ResponseEntity<QuerySurveysResponse> response = surveyController.querySurveys("Fake", empty(), empty(), empty(), empty(), empty());
        assertNull(response);
    }

    @Test
    public void testUpdateSurvey() {
        SurveyMetadata surveyMetadata = mock(SurveyMetadata.class);
        ResponseEntity<SurveyMetadata> response = surveyController.updateSurvey(42, "Fake", surveyMetadata);
        assertNull(response);
    }
}
