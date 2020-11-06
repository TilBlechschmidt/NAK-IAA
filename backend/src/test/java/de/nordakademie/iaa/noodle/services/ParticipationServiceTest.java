package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.ParticipationRepository;
import de.nordakademie.iaa.noodle.model.Participation;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ParticipationServiceTest {
    private ParticipationService participationService;
    private ParticipationRepository participationRepository;
    private SurveyService surveyService;

    @BeforeEach
    void setUp() {
        participationRepository = mock(ParticipationRepository.class);
        surveyService = mock(SurveyService.class);
        participationService = new ParticipationService(participationRepository, surveyService);
    }

    @Test
    void testGetOrCreateParticipationExistingParticipation() throws EntityNotFoundException {
        User user = mock(User.class);
        Participation participation = mock(Participation.class);
        when(participationRepository.findParticipation(42L, user)).thenReturn(participation);

        Participation fetchedParticipation = participationService.getOrCreateParticipation(user, 42L);
        assertEquals(participation, fetchedParticipation);
    }

    @Test
    void testGetOrCreateParticipationNewParticipation() throws EntityNotFoundException {
        User user = mock(User.class);
        Survey survey = mock(Survey.class);
        when(participationRepository.findParticipation(42L, user)).thenReturn(null);
        when(surveyService.querySurvey(42L)).thenReturn(survey);

        Participation participation = participationService.getOrCreateParticipation(user, 42L);

        verify(participationRepository, times(1)).save(participation);
        assertEquals(survey, participation.getSurvey());
    }
}
