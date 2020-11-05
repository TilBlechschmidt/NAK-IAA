package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.TestUtil;
import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.converter.SurveyConverter;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.SurveyService;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static de.nordakademie.iaa.noodle.TestUtil.assertExceptionEquals;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SurveyControllerTest {
    private SurveyController surveyController;
    private SurveyService surveyService;
    private SurveyConverter surveyConverter;
    private User authenticatedUser;

    @BeforeEach
    public void setUp() {
        authenticatedUser = TestUtil.setupAuthentication();
        surveyService = mock(SurveyService.class);
        surveyConverter = mock(SurveyConverter.class);
        surveyController = new SurveyController(surveyService, surveyConverter);

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
        SurveyCreationMetadataDTO surveyCreationMetadataDTO = mock(SurveyCreationMetadataDTO.class);
        ResponseEntity<SurveyMetadataDTO> response = surveyController.createSurvey(surveyCreationMetadataDTO);
        assertNull(response);
    }

    @Test
    public void testDeleteSurvey() {
        ResponseEntity<SurveyMetadataDTO> response = surveyController.deleteSurvey(42L);
        assertNull(response);
    }

    @Test
    public void testQuerySurvey() throws EntityNotFoundException {
        Survey inputSurvey = mock(Survey.class);
        SurveyDTO inputSurveyDTO = mock(SurveyDTO.class);
        when(surveyService.querySurvey(42L)).thenReturn(inputSurvey);
        when(surveyConverter.convertSurveyToDTO(inputSurvey, authenticatedUser)).thenReturn(inputSurveyDTO);

        ResponseEntity<SurveyDTO> response = surveyController.querySurvey(42L);
        SurveyDTO surveyDTO = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inputSurveyDTO, surveyDTO);
    }

    @Test
    public void testQuerySurveyNotFound() throws EntityNotFoundException {
        when(surveyService.querySurvey(42L)).thenThrow(new EntityNotFoundException("surveyNotFound"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            surveyController.querySurvey(42L));

        assertExceptionEquals(HttpStatus.NOT_FOUND, "surveyNotFound", exception);
    }

    @Test
    public void testQuerySurveys() {
        ResponseEntity<QuerySurveysResponse> response = surveyController.querySurveys(empty(), empty(), empty(), empty(), empty());
        assertNull(response);
    }

    @Test
    public void testUpdateSurvey() {
        SurveyCreationMetadataDTO surveyCreationMetadataDTO = mock(SurveyCreationMetadataDTO.class);
        ResponseEntity<SurveyMetadataDTO> response = surveyController.updateSurvey(42L, surveyCreationMetadataDTO);
        assertNull(response);
    }
}
