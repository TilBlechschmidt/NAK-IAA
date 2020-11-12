package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.ResponseRepository;
import de.nordakademie.iaa.noodle.dao.ResponseTimeslotRepository;
import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private TimeslotService timeslotService;
    private ResponseTimeslotRepository responseTimeslotRepository;
    private ParticipationService participationService;
    private SurveyService surveyService;

    @BeforeEach
    public void setUp() {
        responseRepository = mock(ResponseRepository.class);
        timeslotService = mock(TimeslotService.class);
        responseTimeslotRepository = mock(ResponseTimeslotRepository.class);
        participationService = mock(ParticipationService.class);
        surveyService = mock(SurveyService.class);
        responseService = new ResponseService(responseRepository, responseTimeslotRepository, timeslotService,
            participationService, surveyService);
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
