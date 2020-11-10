package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.TimeslotRepository;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.Timeslot;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test for {@link TimeslotService}
 *
 * @author Noah Peeters
 */
class TimeslotServiceTest {
    TimeslotService timeslotService;
    TimeslotRepository timeslotRepository;

    @BeforeEach
    void setUp() {
        timeslotRepository = mock(TimeslotRepository.class);
        timeslotService = new TimeslotService(timeslotRepository);
    }

    @Test
    void testFindTimeslotNotFound() {
        Survey survey = mock(Survey.class);
        when(timeslotRepository.findById(42L)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> timeslotService.findTimeslot(survey, 42L));

        assertEquals("timeslotNotFound", exception.getMessage());
    }

    @Test
    void testFindTimeslotSurveyMismatch() {
        Survey survey = mock(Survey.class);
        Survey timeslotSurvey = mock(Survey.class);
        Timeslot timeslot = mock(Timeslot.class);

        when(timeslot.getSurvey()).thenReturn(timeslotSurvey);
        when(timeslotRepository.findById(42L)).thenReturn(timeslot);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> timeslotService.findTimeslot(survey, 42L));

        assertEquals("timeslotNotFound", exception.getMessage());
    }

    @Test
    void testFindTimeslotSurvey() throws EntityNotFoundException {
        Survey survey = mock(Survey.class);
        Timeslot timeslot = mock(Timeslot.class);

        when(timeslot.getSurvey()).thenReturn(survey);
        when(timeslotRepository.findById(42L)).thenReturn(timeslot);

        Timeslot fetchedTimeslot = timeslotService.findTimeslot(survey, 42L);
        assertEquals(timeslot, fetchedTimeslot);
    }

    @Test
    void testDeleteTimeslotsOfSurvey() {
        Survey survey = mock(Survey.class);
        timeslotService.deleteTimeslotsOfSurvey(survey);
        verify(timeslotRepository, times(1)).deleteAllBySurvey(survey);
    }
}
