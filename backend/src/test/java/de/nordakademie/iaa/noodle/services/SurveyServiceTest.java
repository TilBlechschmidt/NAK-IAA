package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.ParticipationRepository;
import de.nordakademie.iaa.noodle.dao.ResponseRepository;
import de.nordakademie.iaa.noodle.dao.SurveyRepository;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link SurveyService}
 *
 * @author Noah Peeters
 */
public class SurveyServiceTest {
    private SurveyService surveyService;
    private SurveyRepository surveyRepository;
    private TimeslotService timeslotRepository;
    private ResponseRepository responseService;
    private ParticipationRepository participationRepository;
    private MailService mailService;

    @BeforeEach
    public void setUp() {
        surveyRepository = mock(SurveyRepository.class);
        timeslotRepository = mock(TimeslotService.class);
        responseService = mock(ResponseRepository.class);
        participationRepository = mock(ParticipationRepository.class);
        mailService = mock(MailService.class);
        surveyService = new SurveyService(
            surveyRepository, timeslotRepository,
            responseService, participationRepository, mailService);
    }

    @Test
    void testQuerySurvey() throws EntityNotFoundException {
        Survey survey = mock(Survey.class);
        when(surveyRepository.findById(42L)).thenReturn(survey);

        Survey queriedSurvey = surveyService.querySurvey(42L);
        assertEquals(survey, queriedSurvey);
    }
}
