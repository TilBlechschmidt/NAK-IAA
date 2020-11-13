package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.TimeslotRepository;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.Timeslot;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.implementation.TimeslotServiceImpl;
import de.nordakademie.iaa.noodle.services.interfaces.TimeslotService;
import de.nordakademie.iaa.noodle.services.model.TimeslotCreationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for {@link TimeslotServiceImpl}
 *
 * @author Noah Peeters
 */
class TimeslotServiceTest {
    TimeslotService timeslotService;
    TimeslotRepository timeslotRepository;

    @BeforeEach
    void setUp() {
        timeslotRepository = mock(TimeslotRepository.class);
        timeslotService = new TimeslotServiceImpl(timeslotRepository);
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

    @Test
    void testTimeslotCreationDataMatchesTimeslot() {
        Survey survey = mock(Survey.class);

        Timeslot timeslot = new Timeslot(survey, new Date(0), null);
        TimeslotCreationData creationData = new TimeslotCreationData(new Date(0), null);

        assertTrue(timeslotService.timeslotCreationDataMatchesTimeslot(creationData, timeslot));
    }

    @Test
    void testTimeslotCreationDataMatchesTimeslotStartDiffers() {
        Survey survey = mock(Survey.class);

        Timeslot timeslot = new Timeslot(survey, new Date(0), null);
        TimeslotCreationData creationData = new TimeslotCreationData(new Date(1), null);

        assertFalse(timeslotService.timeslotCreationDataMatchesTimeslot(creationData, timeslot));
    }

    @Test
    void testTimeslotCreationDataMatchesTimeslotWithEndDate() {
        Survey survey = mock(Survey.class);

        Timeslot timeslot = new Timeslot(survey, new Date(0), new Date(10));
        TimeslotCreationData creationData = new TimeslotCreationData(new Date(0), new Date(10));

        assertTrue(timeslotService.timeslotCreationDataMatchesTimeslot(creationData, timeslot));
    }

    @Test
    void testTimeslotCreationDataMatchesTimeslotWithEndStartDiffers() {
        Survey survey = mock(Survey.class);

        Timeslot timeslot = new Timeslot(survey, new Date(0), new Date(10));
        TimeslotCreationData creationData = new TimeslotCreationData(new Date(1), new Date(10));

        assertFalse(timeslotService.timeslotCreationDataMatchesTimeslot(creationData, timeslot));
    }

    @Test
    void testTimeslotCreationDataMatchesTimeslotWithEndEndDiffers() {
        Survey survey = mock(Survey.class);

        Timeslot timeslot = new Timeslot(survey, new Date(0), new Date(10));
        TimeslotCreationData creationData = new TimeslotCreationData(new Date(0), new Date(11));

        assertFalse(timeslotService.timeslotCreationDataMatchesTimeslot(creationData, timeslot));
    }

    @Test
    void testTimeslotCreationDataMatchesTimeslotWithEndBothDiffer() {
        Survey survey = mock(Survey.class);

        Timeslot timeslot = new Timeslot(survey, new Date(0), new Date(10));
        TimeslotCreationData creationData = new TimeslotCreationData(new Date(1), new Date(11));

        assertFalse(timeslotService.timeslotCreationDataMatchesTimeslot(creationData, timeslot));
    }

    @Test
    void testTimeslotCreationDataMatchesTimeslotNewEnd() {
        Survey survey = mock(Survey.class);

        Timeslot timeslot = new Timeslot(survey, new Date(0), null);
        TimeslotCreationData creationData = new TimeslotCreationData(new Date(0), new Date(11));

        assertFalse(timeslotService.timeslotCreationDataMatchesTimeslot(creationData, timeslot));
    }

    @Test
    void testTimeslotCreationDataMatchesTimeslotEndRemoved() {
        Survey survey = mock(Survey.class);

        Timeslot timeslot = new Timeslot(survey, new Date(0), new Date(10));
        TimeslotCreationData creationData = new TimeslotCreationData(new Date(0), null);

        assertFalse(timeslotService.timeslotCreationDataMatchesTimeslot(creationData, timeslot));
    }
}
