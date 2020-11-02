package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.SurveyRepository;
import de.nordakademie.iaa.noodle.model.Survey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SurveyServiceTest {
    private SurveyService surveyService;
    private SurveyRepository surveyRepository;

    @BeforeEach
    public void setUp() {
        surveyRepository = mock(SurveyRepository.class);
        surveyService = new SurveyService(surveyRepository);
    }

    @Test
    public void testQuerySurvey() {
        Survey survey = mock(Survey.class);
        when(surveyRepository.findById(42L)).thenReturn(survey);

        Survey queriedSurvey = surveyService.querySurvey(42L);
        assertEquals(survey, queriedSurvey);
    }
}
