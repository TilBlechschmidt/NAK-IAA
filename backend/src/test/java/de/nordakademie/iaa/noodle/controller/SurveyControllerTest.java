package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.TestUtil;
import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.mapper.SurveyMapper;
import de.nordakademie.iaa.noodle.mapper.TimeslotMapper;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.SurveyService;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ForbiddenOperationException;
import de.nordakademie.iaa.noodle.services.exceptions.SemanticallyInvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.nordakademie.iaa.noodle.TestUtil.assertEqualsResponseEntity;
import static de.nordakademie.iaa.noodle.TestUtil.assertThrowsResponseStatusException;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SurveyControllerTest {
    private SurveyController surveyController;
    private SurveyService surveyService;
    private SurveyMapper surveyMapper;
    private TimeslotMapper timeslotMapper;
    private User authenticatedUser;

    @BeforeEach
    public void setUp() {
        authenticatedUser = TestUtil.setupAuthentication();
        surveyService = mock(SurveyService.class);
        surveyMapper = mock(SurveyMapper.class);
        timeslotMapper = mock(TimeslotMapper.class);
        surveyController = new SurveyController(surveyService, surveyMapper, timeslotMapper);

        when(authenticatedUser.getFullName()).thenReturn("TESTUSER");
    }

    @Test
    public void testCloseSurveyBadRequest() {
        CloseSurveyRequest closeSurveyRequest = new CloseSurveyRequest();
        assertThrowsResponseStatusException(HttpStatus.BAD_REQUEST, "unsupportedOperation", () -> surveyController.closeSurvey(42L, closeSurveyRequest));
    }

    @Test
    public void testCloseSurveyNotFound() throws Exception {
        CloseSurveyRequest closeSurveyRequest = new CloseSurveyRequest();
        when(surveyService.closeSurvey(any(), any(), any())).thenThrow(new EntityNotFoundException("test"));
        closeSurveyRequest.setOperation(CloseSurveyRequest.OperationEnum.CLOSE);
        assertThrowsResponseStatusException(HttpStatus.NOT_FOUND, "test", () -> surveyController.closeSurvey(42L, closeSurveyRequest));
    }

    @Test
    public void testCloseSurveyForbidden() throws Exception {
        CloseSurveyRequest closeSurveyRequest = new CloseSurveyRequest();
        closeSurveyRequest.setOperation(CloseSurveyRequest.OperationEnum.CLOSE);
        when(surveyService.closeSurvey(any(), any(), any())).thenThrow(new ForbiddenOperationException("test"));
        assertThrowsResponseStatusException(HttpStatus.FORBIDDEN, "test", () -> surveyController.closeSurvey(42L, closeSurveyRequest));
    }

//    @Test
//    public void testCloseSurveyThrowable() throws ForbiddenOperationException, EntityNotFoundException {
//        CloseSurveyRequest closeSurveyRequest = new CloseSurveyRequest();
//        closeSurveyRequest.setOperation(CloseSurveyRequest.OperationEnum.CLOSE);
//        when(surveyService.closeSurvey(any(), any(), any())).thenThrow(new RuntimeException("test"));
//        assertThrowsResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "test", () -> surveyController.closeSurvey(42L, closeSurveyRequest));
//    }

    @Test
    public void testCloseSurvey() throws ForbiddenOperationException, EntityNotFoundException {
        CloseSurveyRequest closeSurveyRequest = new CloseSurveyRequest();
        closeSurveyRequest.setOperation(CloseSurveyRequest.OperationEnum.CLOSE);

        Survey survey = mock(Survey.class);
        SurveyMetadataDTO dto = new SurveyMetadataDTO();
        when(surveyMapper.surveyToMetadataDTO(same(survey), any())).thenReturn(dto);
        when(surveyService.closeSurvey(any(), any(), any())).thenReturn(survey);

        ResponseEntity<SurveyMetadataDTO> responseEntity = surveyController.closeSurvey(42L, closeSurveyRequest);
        assertEqualsResponseEntity(HttpStatus.OK, dto, responseEntity);
    }

    @Test
    public void testCreateSurveyUnprocessable() throws SemanticallyInvalidInputException {
        SurveyCreationMetadataDTO surveyCreationMetadataDTO = mock(SurveyCreationMetadataDTO.class);
        when(surveyService.createSurvey(any(), any(), any(), any())).thenThrow(new SemanticallyInvalidInputException("test1"));
        assertThrowsResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "test1", () -> surveyController.createSurvey(surveyCreationMetadataDTO));
    }

    @Test
    public void testCreateSurvey() throws SemanticallyInvalidInputException {
        SurveyCreationMetadataDTO inputDTO = mock(SurveyCreationMetadataDTO.class);
        Survey survey = mock(Survey.class);
        SurveyMetadataDTO dto = mock(SurveyMetadataDTO.class);
        when(surveyService.createSurvey(any(), any(), any(), any())).thenReturn(survey);
        when(surveyMapper.surveyToMetadataDTO(same(survey), any())).thenReturn(dto);

        ResponseEntity<SurveyMetadataDTO> responseEntity = surveyController.createSurvey(inputDTO);
        assertEqualsResponseEntity(HttpStatus.CREATED, dto, responseEntity);
    }

    @Test
    public void testDeleteSurvey() {
        ResponseEntity<SurveyMetadataDTO> response = surveyController.deleteSurvey(42L);
        fail();
    }

    @Test
    public void testQuerySurvey() throws EntityNotFoundException {
        Survey survey = mock(Survey.class);
        SurveyDTO dto = mock(SurveyDTO.class);
        when(surveyService.querySurvey(42L)).thenReturn(survey);
        when(surveyMapper.surveyToDTO(survey, authenticatedUser)).thenReturn(dto);

        ResponseEntity<SurveyDTO> response = surveyController.querySurvey(42L);
        assertEqualsResponseEntity(HttpStatus.OK, dto, response);
    }

    @Test
    public void testQuerySurveyNotFound() throws EntityNotFoundException {
        when(surveyService.querySurvey(42L)).thenThrow(new EntityNotFoundException("surveyNotFound"));

        assertThrowsResponseStatusException(HttpStatus.NOT_FOUND, "surveyNotFound", () ->
            surveyController.querySurvey(42L));
    }

    @Test
    public void testQuerySurveys() {
        ResponseEntity<QuerySurveysResponse> response = surveyController.querySurveys(empty(), empty(), empty(), empty(), empty());
        fail();
    }

    @Test
    public void testUpdateSurvey() {
        SurveyCreationMetadataDTO surveyCreationMetadataDTO = mock(SurveyCreationMetadataDTO.class);
        ResponseEntity<SurveyMetadataDTO> response = surveyController.updateSurvey(42L, surveyCreationMetadataDTO);
        fail();
    }


}
