package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.ResponseRepository;
import de.nordakademie.iaa.noodle.dao.ResponseTimeslotRepository;
import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.implementation.ResponseServiceImpl;
import de.nordakademie.iaa.noodle.services.interfaces.ParticipationService;
import de.nordakademie.iaa.noodle.services.interfaces.ResponseService;
import de.nordakademie.iaa.noodle.services.interfaces.SurveyService;
import de.nordakademie.iaa.noodle.services.interfaces.TimeslotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link ResponseService}
 *
 * @author Noah Peeters
 */
public class ResponseServiceTest {
    private ResponseService responseService;
    private ResponseRepository responseRepository;

    @BeforeEach
    public void setUp() {
        responseRepository = mock(ResponseRepository.class);
        TimeslotService timeslotService = mock(TimeslotService.class);
        ResponseTimeslotRepository responseTimeslotRepository = mock(ResponseTimeslotRepository.class);
        ParticipationService participationService = mock(ParticipationService.class);
        SurveyService surveyService = mock(SurveyService.class);
        EntityManager entityManager = mock(EntityManager.class);
        responseService = new ResponseServiceImpl(responseRepository, responseTimeslotRepository, timeslotService,
            participationService, surveyService, entityManager);
    }

    @Test
    void testQueryResponseNotFound() {
        when(responseRepository.findByIdAndSurveyId(42L, 43L)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> responseService.queryResponse(42L, 43L));

        assertEquals("responseNotFound", exception.getMessage());
    }

    @Test
    void testQueryResponse() throws EntityNotFoundException {
        Response response = mock(Response.class);
        when(responseRepository.findByIdAndSurveyId(42L, 43L)).thenReturn(response);

        Response queriedResponse = responseService.queryResponse(42L, 43L);
        assertEquals(response, queriedResponse);
    }
}
