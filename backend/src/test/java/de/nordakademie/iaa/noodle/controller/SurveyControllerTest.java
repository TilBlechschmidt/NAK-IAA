package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.TestUtil;
import de.nordakademie.iaa.noodle.api.model.*;
import de.nordakademie.iaa.noodle.dao.model.QuerySurveysItem;
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

import java.util.ArrayList;
import java.util.List;

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
    private User authenticatedUser;

    @BeforeEach
    public void setUp() {
        authenticatedUser = TestUtil.setupAuthentication();
        surveyService = mock(SurveyService.class);
        surveyMapper = mock(SurveyMapper.class);
        TimeslotMapper timeslotMapper = mock(TimeslotMapper.class);
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
        when(surveyService.closeSurvey(any(), any(), any())).thenThrow(new EntityNotFoundException("testCloseSurvey"));
        closeSurveyRequest.setOperation(CloseSurveyRequest.OperationEnum.CLOSE);
        assertThrowsResponseStatusException(HttpStatus.NOT_FOUND, "testCloseSurvey", () -> surveyController.closeSurvey(42L, closeSurveyRequest));
    }

    @Test
    public void testCloseSurveyForbidden() throws Exception {
        CloseSurveyRequest closeSurveyRequest = new CloseSurveyRequest();
        closeSurveyRequest.setOperation(CloseSurveyRequest.OperationEnum.CLOSE);
        when(surveyService.closeSurvey(any(), any(), any())).thenThrow(new ForbiddenOperationException("testCloseSurvey"));
        assertThrowsResponseStatusException(HttpStatus.FORBIDDEN, "testCloseSurvey", () -> surveyController.closeSurvey(42L, closeSurveyRequest));
    }

    @Test
    public void testCloseSurvey() throws ForbiddenOperationException, EntityNotFoundException {
        CloseSurveyRequest closeSurveyRequest = new CloseSurveyRequest();
        closeSurveyRequest.setOperation(CloseSurveyRequest.OperationEnum.CLOSE);

        Survey survey = mock(Survey.class);
        SurveyMetadataDTO expectedDTO = new SurveyMetadataDTO();
        when(surveyMapper.surveyToMetadataDTO(same(survey), any())).thenReturn(expectedDTO);
        when(surveyService.closeSurvey(any(), any(), any())).thenReturn(survey);

        ResponseEntity<SurveyMetadataDTO> response = surveyController.closeSurvey(42L, closeSurveyRequest);
        assertEqualsResponseEntity(HttpStatus.OK, expectedDTO, response);
    }

    @Test
    public void testCreateSurveyUnprocessable() throws SemanticallyInvalidInputException {
        SurveyCreationMetadataDTO surveyCreationMetadataDTO = mock(SurveyCreationMetadataDTO.class);
        when(surveyService.createSurvey(any(), any(), any(), any())).thenThrow(new SemanticallyInvalidInputException("testCreateSurvey"));
        assertThrowsResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "testCreateSurvey", () -> surveyController.createSurvey(surveyCreationMetadataDTO));
    }

    @Test
    public void testCreateSurvey() throws SemanticallyInvalidInputException {
        SurveyCreationMetadataDTO inputDTO = mock(SurveyCreationMetadataDTO.class);
        Survey survey = mock(Survey.class);
        SurveyMetadataDTO expectedDTO = mock(SurveyMetadataDTO.class);
        when(surveyService.createSurvey(any(), any(), any(), any())).thenReturn(survey);
        when(surveyMapper.surveyToMetadataDTO(same(survey), any())).thenReturn(expectedDTO);

        ResponseEntity<SurveyMetadataDTO> response = surveyController.createSurvey(inputDTO);
        assertEqualsResponseEntity(HttpStatus.CREATED, expectedDTO, response);
    }

    @Test
    public void testDeleteSurveyNotFound() throws ForbiddenOperationException, EntityNotFoundException {
        when(surveyService.deleteSurvey(any(), any())).thenThrow(new EntityNotFoundException("deleteTest"));
        assertThrowsResponseStatusException(HttpStatus.NOT_FOUND, "deleteTest", () -> surveyController.deleteSurvey(42L));
    }

    @Test
    public void testDeleteSurveyForbidden() throws ForbiddenOperationException, EntityNotFoundException {
        when(surveyService.deleteSurvey(any(), any())).thenThrow(new ForbiddenOperationException("deleteTest"));
        assertThrowsResponseStatusException(HttpStatus.FORBIDDEN, "deleteTest", () -> surveyController.deleteSurvey(42L));
    }

    @Test
    public void testDeleteSurvey() throws ForbiddenOperationException, EntityNotFoundException {
        Survey survey = mock(Survey.class);
        when(surveyService.deleteSurvey(any(), any())).thenReturn(survey);

        SurveyMetadataDTO expectedDTO = mock(SurveyMetadataDTO.class);
        when(surveyMapper.surveyToMetadataDTO(same(survey), any())).thenReturn(expectedDTO);

        ResponseEntity<SurveyMetadataDTO> response = surveyController.deleteSurvey(42L);
        assertEqualsResponseEntity(HttpStatus.OK, expectedDTO, response);
    }

    @Test
    public void testQuerySurvey() throws EntityNotFoundException {
        Survey survey = mock(Survey.class);
        SurveyDTO expectedDTO = mock(SurveyDTO.class);
        when(surveyService.querySurvey(42L)).thenReturn(survey);
        when(surveyMapper.surveyToDTO(survey, authenticatedUser)).thenReturn(expectedDTO);

        ResponseEntity<SurveyDTO> response = surveyController.querySurvey(42L);
        assertEqualsResponseEntity(HttpStatus.OK, expectedDTO, response);
    }

    @Test
    public void testQuerySurveyNotFound() throws EntityNotFoundException {
        when(surveyService.querySurvey(42L)).thenThrow(new EntityNotFoundException("surveyNotFound"));

        assertThrowsResponseStatusException(HttpStatus.NOT_FOUND, "surveyNotFound", () ->
            surveyController.querySurvey(42L));
    }

    @Test
    public void testQuerySurveys() {
        List<QuerySurveysItem> expectedSurveys = new ArrayList<>();
        when(surveyService.querySurveys(any(),any(),any(),any(),any(),any())).thenReturn(expectedSurveys);
        QuerySurveysResponse expectedDTO = mock(QuerySurveysResponse.class);
        when(surveyMapper.surveysToSurveysDTO(expectedSurveys)).thenReturn(expectedDTO);
        ResponseEntity<QuerySurveysResponse> response = surveyController.querySurveys(empty(), empty(), empty(), empty(), empty());
        assertEqualsResponseEntity(HttpStatus.OK, expectedDTO, response);
    }


    @Test
    public void testUpdateSurveyForbidden() throws EntityNotFoundException, ForbiddenOperationException, SemanticallyInvalidInputException {
        when(surveyService.updateSurvey(any(), any(), any(), any(), any())).thenThrow(new ForbiddenOperationException("testUpdateSurvey"));
        SurveyCreationMetadataDTO inputDTO = mock(SurveyCreationMetadataDTO.class);

        assertThrowsResponseStatusException(HttpStatus.FORBIDDEN,"testUpdateSurvey",()->surveyController.updateSurvey(42L,inputDTO));
    }

    @Test
    public void testUpdateSurveyNotFound() throws EntityNotFoundException, ForbiddenOperationException, SemanticallyInvalidInputException {
        when(surveyService.updateSurvey(any(), any(), any(), any(), any())).thenThrow(new EntityNotFoundException("testUpdateSurvey"));
        SurveyCreationMetadataDTO inputDTO = mock(SurveyCreationMetadataDTO.class);

        assertThrowsResponseStatusException(HttpStatus.NOT_FOUND,"testUpdateSurvey",()->surveyController.updateSurvey(42L,inputDTO));
    }

    @Test
    public void testUpdateSurveyInvalid() throws EntityNotFoundException, ForbiddenOperationException, SemanticallyInvalidInputException {
        when(surveyService.updateSurvey(any(), any(), any(), any(), any())).thenThrow(new SemanticallyInvalidInputException("testUpdateSurvey"));
        SurveyCreationMetadataDTO inputDTO = mock(SurveyCreationMetadataDTO.class);

        assertThrowsResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"testUpdateSurvey",()->surveyController.updateSurvey(42L,inputDTO));
    }

    @Test
    public void testUpdateSurvey() throws EntityNotFoundException, ForbiddenOperationException, SemanticallyInvalidInputException {
        Survey survey = mock(Survey.class);
        when(surveyService.updateSurvey(any(), any(), any(), any(), any())).thenReturn(survey);
        SurveyMetadataDTO expectedDTO = mock(SurveyMetadataDTO.class);
        when(surveyMapper.surveyToMetadataDTO(same(survey), any())).thenReturn(expectedDTO);

        SurveyCreationMetadataDTO surveyCreationMetadataDTO = mock(SurveyCreationMetadataDTO.class);
        ResponseEntity<SurveyMetadataDTO> response = surveyController.updateSurvey(42L, surveyCreationMetadataDTO);
        assertEqualsResponseEntity(HttpStatus.OK, expectedDTO, response);
    }
}
