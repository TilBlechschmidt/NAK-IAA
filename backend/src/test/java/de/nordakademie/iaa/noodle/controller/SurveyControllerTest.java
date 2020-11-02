package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.TestUtil;
import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SurveyControllerTest {
    private SurveyController surveyController;
    private User authenticatedUser;

    @BeforeEach
    public void setUp() {
        authenticatedUser = TestUtil.setupAuthentication();
        surveyController = new SurveyController();

        when(authenticatedUser.getFullName()).thenReturn("TESTUSER");
    }

    @Test
    public void testCloseSurvey() {
        CloseSurveyRequest closeSurveyRequest = new CloseSurveyRequest();
        ResponseEntity<SurveyMetadataDTO> response = surveyController.closeSurvey(42L, closeSurveyRequest);
        assertNull(response);
    }

    @Test
    public void testCreateSurvey() {
        CreateSurveyRequest createSurveyRequest = mock(CreateSurveyRequest.class);
        ResponseEntity<CreateSurveyResponse> response = surveyController.createSurvey(createSurveyRequest);
        assertNull(response);
    }

    @Test
    public void testDeleteSurvey() {
        ResponseEntity<SurveyMetadataDTO> response = surveyController.deleteSurvey(42L);
        assertNull(response);
    }


    @Test
    public void testQuerySurvey() {
        ResponseEntity<SurveyDTO> response = surveyController.querySurvey(42L);
        SurveyDTO surveyDTO = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(surveyDTO);
        assertEquals("This is the title for 42.", surveyDTO.getTitle());
        assertEquals("You are authenticated as TESTUSER", surveyDTO.getDescription());
        assertArrayEquals(Collections.EMPTY_LIST.toArray(), surveyDTO.getResponses().toArray());
        assertArrayEquals(Collections.EMPTY_LIST.toArray(), surveyDTO.getTimeslots().toArray());
    }

    @Test
    public void testQuerySurveys() {
        ResponseEntity<QuerySurveysResponse> response = surveyController.querySurveys(empty(), empty(), empty(), empty(), empty());
        assertNull(response);
    }

    @Test
    public void testUpdateSurvey() {
        SurveyMetadataDTO surveyMetadata = mock(SurveyMetadataDTO.class);
        ResponseEntity<SurveyMetadataDTO> response = surveyController.updateSurvey(42L, surveyMetadata);
        assertNull(response);
    }
}
