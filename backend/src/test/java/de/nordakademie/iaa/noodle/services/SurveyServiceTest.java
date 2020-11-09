package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.ParticipationRepository;
import de.nordakademie.iaa.noodle.dao.ResponseRepository;
import de.nordakademie.iaa.noodle.dao.SurveyRepository;
import de.nordakademie.iaa.noodle.model.*;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ForbiddenOperationException;
import de.nordakademie.iaa.noodle.services.exceptions.SemanticallyInvalidInputException;
import de.nordakademie.iaa.noodle.services.model.TimeslotCreationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for {@link SurveyService}
 *
 * @author Noah Peeters
 */
public class SurveyServiceTest {
    private SurveyService surveyService;
    private SurveyRepository surveyRepository;
    private TimeslotService timeslotRepository;
    private ResponseRepository responseRepository;
    private ParticipationRepository participationRepository;
    private MailService mailService;

    @BeforeEach
    public void setUp() {
        surveyRepository = mock(SurveyRepository.class);
        timeslotRepository = mock(TimeslotService.class);
        responseRepository = mock(ResponseRepository.class);
        participationRepository = mock(ParticipationRepository.class);
        mailService = mock(MailService.class);
        surveyService = new SurveyService(
            surveyRepository, timeslotRepository,
            responseRepository, participationRepository, mailService);
    }

    @Test
    void testCreateSurveyInvalidTimeslot() {
        User creator = mock(User.class);
        List<TimeslotCreationData> timeslots = Arrays.asList(
            new TimeslotCreationData(Date.from(Instant.ofEpochMilli(0L)), null),
            new TimeslotCreationData(Date.from(Instant.ofEpochMilli(1L)), Date.from(Instant.ofEpochMilli(0L)))
        );

        SemanticallyInvalidInputException exception = assertThrows(SemanticallyInvalidInputException.class,
            () -> surveyService.createSurvey("TITLE", "DESCRIPTION", timeslots, creator));

        assertEquals("invalidTimeslot", exception.getMessage());
    }

    @Test
    void testCreateSurveyValidTimeslot() throws SemanticallyInvalidInputException {
        User creator = mock(User.class);
        List<TimeslotCreationData> timeslotsCreationData = Arrays.asList(
            new TimeslotCreationData(Date.from(Instant.ofEpochMilli(0L)), null),
            new TimeslotCreationData(Date.from(Instant.ofEpochMilli(1L)), Date.from(Instant.ofEpochMilli(2L)))
        );

        Survey survey = surveyService.createSurvey("TITLE", "DESCRIPTION", timeslotsCreationData, creator);

        verify(surveyRepository, times(1)).save(survey);
        assertEquals("TITLE", survey.getTitle());
        assertEquals("DESCRIPTION", survey.getDescription());
        assertEquals(2, survey.getTimeslots().size());
        assertEquals(creator, survey.getCreator());

        List<Timeslot> timeslots = survey.getTimeslots()
            .stream()
            .sorted(Comparator.comparing(Timeslot::getStart))
            .collect(Collectors.toList());

        assertEquals(0L, timeslots.get(0).getStart().getTime());
        assertNull(timeslots.get(0).getEnd());
        assertEquals(1L, timeslots.get(1).getStart().getTime());
        assertEquals(2L, timeslots.get(1).getEnd().getTime());
        assertEquals(survey, timeslots.get(0).getSurvey());
        assertEquals(survey, timeslots.get(1).getSurvey());

        assertEquals(1, survey.getParticipations().size());
        Participation participation = (Participation) survey.getParticipations().toArray()[0];

        assertEquals(creator, participation.getParticipant());
        assertNotNull(participation.getResponse());

        Response surveyResponse = participation.getResponse();
        assertEquals(participation, surveyResponse.getParticipation());

        assertEquals(2, surveyResponse.getResponseTimeslots().size());

        List<ResponseTimeslot> responseTimeslots = surveyResponse.getResponseTimeslots().stream()
            .sorted(Comparator.comparing(r -> r.getTimeslot().getStart()))
            .collect(Collectors.toList());

        assertEquals(surveyResponse, responseTimeslots.get(0).getResponse());
        assertEquals(ResponseType.YES, responseTimeslots.get(0).getResponseType());
        assertEquals(timeslots.get(0), responseTimeslots.get(0).getTimeslot());
        assertEquals(surveyResponse, responseTimeslots.get(1).getResponse());
        assertEquals(ResponseType.YES, responseTimeslots.get(1).getResponseType());
        assertEquals(timeslots.get(1), responseTimeslots.get(1).getTimeslot());
    }

    @Test
    void testUpdateSurveyNotFound() {
        User creator = mock(User.class);
        List<TimeslotCreationData> timeslots = Arrays.asList(
            new TimeslotCreationData(Date.from(Instant.ofEpochMilli(0L)), null),
            new TimeslotCreationData(Date.from(Instant.ofEpochMilli(1L)), Date.from(Instant.ofEpochMilli(2L)))
        );

        when(surveyRepository.findById(42L)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> surveyService.updateSurvey(42L, "TITLE", "DESCRIPTION", timeslots, creator));

        assertEquals("surveyNotFound", exception.getMessage());
    }

    @Test
    void testUpdateSurveyForbidden() {
        User creator = mock(User.class);
        Survey survey = mock(Survey.class);
        List<TimeslotCreationData> timeslots = Arrays.asList(
            new TimeslotCreationData(Date.from(Instant.ofEpochMilli(0L)), null),
            new TimeslotCreationData(Date.from(Instant.ofEpochMilli(1L)), Date.from(Instant.ofEpochMilli(2L)))
        );

        when(surveyRepository.findById(42L)).thenReturn(survey);
        when(survey.getCreator()).thenReturn(mock(User.class));
        when(survey.getIsClosed()).thenReturn(true);

        ForbiddenOperationException exception = assertThrows(ForbiddenOperationException.class,
            () -> surveyService.updateSurvey(42L, "TITLE", "DESCRIPTION", timeslots, creator));

        assertEquals("forbidden", exception.getMessage());
    }

    @Test
    void testUpdateSurvey()
        throws EntityNotFoundException, ForbiddenOperationException, SemanticallyInvalidInputException {
        User creator = mock(User.class);
        User otherUser1 = mock(User.class);
        User otherUser2 = mock(User.class);

        Participation creatorsParticipation = mock(Participation.class);
        Participation otherParticipation1 = mock(Participation.class);
        Participation otherParticipation2 = mock(Participation.class);

        Response creatorsResponse = mock(Response.class);
        Response otherResponse = mock(Response.class);

        when(creatorsParticipation.getResponse()).thenReturn(creatorsResponse);
        when(otherParticipation1.getResponse()).thenReturn(otherResponse);
        when(otherParticipation2.getResponse()).thenReturn(null);
        when(creatorsParticipation.getParticipant()).thenReturn(creator);
        when(otherParticipation1.getParticipant()).thenReturn(otherUser1);
        when(otherParticipation2.getParticipant()).thenReturn(otherUser2);

        Survey survey = new Survey(
            new HashSet<>(),
            null,
            creator,
            new HashSet<>(Arrays.asList(creatorsParticipation, otherParticipation1, otherParticipation2)),
            "OLD_TTLE",
            "OLD_DESCRIPTION");
        List<TimeslotCreationData> timeslotCreationData = Arrays.asList(
            new TimeslotCreationData(Date.from(Instant.ofEpochMilli(0L)), null),
            new TimeslotCreationData(Date.from(Instant.ofEpochMilli(1L)), Date.from(Instant.ofEpochMilli(2L)))
        );

        when(surveyRepository.findById(42L)).thenReturn(survey);

        Survey updatedSurvey = surveyService.updateSurvey(42L, "TITLE", "DESCRIPTION",
            timeslotCreationData, creator);

        assertEquals(survey, updatedSurvey);
        verify(surveyRepository, times(1)).save(survey);
        verify(responseRepository, times(1)).delete(creatorsResponse);
        verify(responseRepository, times(1)).delete(otherResponse);
        verify(participationRepository, times(1)).delete(creatorsParticipation);
        verify(timeslotRepository, times(1)).deleteTimeslotsOfSurvey(survey);
        verify(creatorsParticipation, times(1)).setResponse(null);
        verify(otherParticipation1, times(1)).setResponse(null);
        verify(mailService, times(1)).sendNeedsAttentionMailsAsync(eq(survey), argThat(users -> {
            assertEquals(1, users.size());
            assertEquals(otherUser1, users.get(0));
            return true;
        }));

        verify(surveyRepository, times(1)).save(survey);
        assertEquals("TITLE", survey.getTitle());
        assertEquals("DESCRIPTION", survey.getDescription());
        assertEquals(2, survey.getTimeslots().size());
        assertEquals(creator, survey.getCreator());

        List<Timeslot> timeslots = survey.getTimeslots()
            .stream()
            .sorted(Comparator.comparing(Timeslot::getStart))
            .collect(Collectors.toList());

        assertEquals(0L, timeslots.get(0).getStart().getTime());
        assertNull(timeslots.get(0).getEnd());
        assertEquals(1L, timeslots.get(1).getStart().getTime());
        assertEquals(2L, timeslots.get(1).getEnd().getTime());
        assertEquals(survey, timeslots.get(0).getSurvey());
        assertEquals(survey, timeslots.get(1).getSurvey());

        assertEquals(3, survey.getParticipations().size());
        assertTrue(survey.getParticipations().contains(otherParticipation1));
        assertTrue(survey.getParticipations().contains(otherParticipation2));
        assertFalse(survey.getParticipations().contains(creatorsParticipation));
        Participation participation = survey.getParticipations()
            .stream()
            .filter(p -> !p.equals(otherParticipation1) && !p.equals(otherParticipation2))
            .findFirst().orElse(null);

        assertNotNull(participation);
        assertEquals(creator, participation.getParticipant());
        assertNotNull(participation.getResponse());

        Response surveyResponse = participation.getResponse();
        assertEquals(participation, surveyResponse.getParticipation());

        assertEquals(2, surveyResponse.getResponseTimeslots().size());

        List<ResponseTimeslot> responseTimeslots = surveyResponse.getResponseTimeslots().stream()
            .sorted(Comparator.comparing(r -> r.getTimeslot().getStart()))
            .collect(Collectors.toList());

        assertEquals(surveyResponse, responseTimeslots.get(0).getResponse());
        assertEquals(ResponseType.YES, responseTimeslots.get(0).getResponseType());
        assertEquals(timeslots.get(0), responseTimeslots.get(0).getTimeslot());
        assertEquals(surveyResponse, responseTimeslots.get(1).getResponse());
        assertEquals(ResponseType.YES, responseTimeslots.get(1).getResponseType());
        assertEquals(timeslots.get(1), responseTimeslots.get(1).getTimeslot());
    }

    @Test
    void testQuerySurvey() throws EntityNotFoundException {
        Survey survey = mock(Survey.class);
        when(surveyRepository.findById(42L)).thenReturn(survey);

        Survey queriedSurvey = surveyService.querySurvey(42L);
        assertEquals(survey, queriedSurvey);
    }

    @Test
    void testQuerySurveyNotFound() {
        when(surveyRepository.findById(42L)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> surveyService.querySurvey(42L));

        assertEquals("surveyNotFound", exception.getMessage());
    }

    @Test
    void testCloseSurveySurveyNotFound() {
        User user = mock(User.class);
        when(surveyRepository.findById(42L)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> surveyService.closeSurvey(42L, 43L, user));

        assertEquals("surveyNotFound", exception.getMessage());
    }

    @Test
    void testCloseSurveyTimeslotNotFound() throws EntityNotFoundException {
        User user = mock(User.class);
        Survey survey = mock(Survey.class);

        when(surveyRepository.findById(42L)).thenReturn(survey);
        when(survey.getCreator()).thenReturn(user);
        when(survey.getIsClosed()).thenReturn(false);
        when(timeslotRepository.findTimeslot(survey, 43L))
            .thenThrow(new EntityNotFoundException("testEntityNotFound"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> surveyService.closeSurvey(42L, 43L, user));

        assertEquals("testEntityNotFound", exception.getMessage());
    }

    @Test
    void testCloseSurveyForbidden() {
        User user = mock(User.class);
        User creator = mock(User.class);
        Survey survey = mock(Survey.class);

        when(surveyRepository.findById(42L)).thenReturn(survey);
        when(survey.getCreator()).thenReturn(creator);
        when(survey.getIsClosed()).thenReturn(true);

        ForbiddenOperationException exception = assertThrows(ForbiddenOperationException.class,
            () -> surveyService.closeSurvey(42L, 43L, user));

        assertEquals("forbidden", exception.getMessage());
    }

    @Test
    void testCloseSurvey() throws ForbiddenOperationException, EntityNotFoundException {
        User user = mock(User.class);
        Survey survey = mock(Survey.class);
        Timeslot selectedTimeslot = mock(Timeslot.class);

        when(surveyRepository.findById(42L)).thenReturn(survey);
        when(timeslotRepository.findTimeslot(survey, 43L)).thenReturn(selectedTimeslot);
        when(survey.getCreator()).thenReturn(user);
        when(survey.getIsClosed()).thenReturn(false);

        Survey deletedSurvey = surveyService.closeSurvey(42L, 43L, user);

        assertEquals(survey, deletedSurvey);
        verify(survey, times(1)).setSelectedTimeslot(selectedTimeslot);
        verify(surveyRepository, times(1)).save(survey);
    }

    @Test
    void testDeleteSurveyNotFound() {
        User user = mock(User.class);
        when(surveyRepository.findById(42L)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> surveyService.deleteSurvey(42L, user));

        assertEquals("surveyNotFound", exception.getMessage());
    }

    @Test
    void testDeleteSurveyForbidden() {
        User user = mock(User.class);
        User creator = mock(User.class);
        Survey survey = mock(Survey.class);

        when(surveyRepository.findById(42L)).thenReturn(survey);
        when(survey.getCreator()).thenReturn(creator);

        ForbiddenOperationException exception = assertThrows(ForbiddenOperationException.class,
            () -> surveyService.deleteSurvey(42L, user));

        assertEquals("forbidden", exception.getMessage());
    }

    @Test
    void testDeleteSurvey() throws ForbiddenOperationException, EntityNotFoundException {
        User user = mock(User.class);
        Survey survey = mock(Survey.class);

        when(surveyRepository.findById(42L)).thenReturn(survey);
        when(survey.getCreator()).thenReturn(user);

        Survey deletedSurvey = surveyService.deleteSurvey(42L, user);

        assertEquals(survey, deletedSurvey);
        verify(surveyRepository, times(1)).delete(survey);
    }

    @Test
    void testCreateUpdateSurveyNoTimeslots() {
        String title = "TITLE";
        String description = "DESCIPTION";
        List<TimeslotCreationData> timeslots = Collections.emptyList();
        User creator = mock(User.class);

        SemanticallyInvalidInputException createException = assertThrows(SemanticallyInvalidInputException.class,
            () -> surveyService.createSurvey(title, description, timeslots, creator));

        SemanticallyInvalidInputException updateException = assertThrows(SemanticallyInvalidInputException.class,
            () -> surveyService.updateSurvey(42L, title, description, timeslots, creator));

        assertEquals("noTimeslots", createException.getMessage());
        assertEquals("noTimeslots", updateException.getMessage());
    }

    @Test
    void testCreateUpdateSurveyMissingTitle() {
        String title = "";
        String description = "DESCIPTION";
        List<TimeslotCreationData> timeslots = Collections.singletonList(mock(TimeslotCreationData.class));
        User creator = mock(User.class);

        SemanticallyInvalidInputException createException = assertThrows(SemanticallyInvalidInputException.class,
            () -> surveyService.createSurvey(title, description, timeslots, creator));

        SemanticallyInvalidInputException updateException = assertThrows(SemanticallyInvalidInputException.class,
            () -> surveyService.updateSurvey(42L, title, description, timeslots, creator));

        assertEquals("emptyTitle", createException.getMessage());
        assertEquals("emptyTitle", updateException.getMessage());
    }

    @Test
    void testCreateUpdateSurveyTitleTooLong() {
        String title = new String(new char[2049]).replace("\0", "a");
        String description = "DESCIPTION";
        List<TimeslotCreationData> timeslots = Collections.singletonList(mock(TimeslotCreationData.class));
        User creator = mock(User.class);

        SemanticallyInvalidInputException createException = assertThrows(SemanticallyInvalidInputException.class,
            () -> surveyService.createSurvey(title, description, timeslots, creator));

        SemanticallyInvalidInputException updateException = assertThrows(SemanticallyInvalidInputException.class,
            () -> surveyService.updateSurvey(42L, title, description, timeslots, creator));

        assertEquals("titleTooLong", createException.getMessage());
        assertEquals("titleTooLong", updateException.getMessage());
    }

    @Test
    void testCreateUpdateSurveyDescriptionTooLong() {
        String title = "TITLE";
        String description = new String(new char[2049]).replace("\0", "a");
        List<TimeslotCreationData> timeslots = Collections.singletonList(mock(TimeslotCreationData.class));
        User creator = mock(User.class);

        SemanticallyInvalidInputException createException = assertThrows(SemanticallyInvalidInputException.class,
            () -> surveyService.createSurvey(title, description, timeslots, creator));

        SemanticallyInvalidInputException updateException = assertThrows(SemanticallyInvalidInputException.class,
            () -> surveyService.updateSurvey(42L, title, description, timeslots, creator));

        assertEquals("descriptionTooLong", createException.getMessage());
        assertEquals("descriptionTooLong", updateException.getMessage());
    }

    @Test
    void testIsSurveyEditableByUserOtherCreatorClosed() {
        Survey survey = mock(Survey.class);
        User user = mock(User.class);
        User creator = mock(User.class);
        when(survey.getCreator()).thenReturn(creator);
        when(survey.getIsClosed()).thenReturn(true);

        assertFalse(surveyService.isSurveyEditableByUser(survey, user));
        assertFalse(surveyService.isSurveyClosableByUser(survey, user));
        assertFalse(surveyService.canUserRespondToSurvey(survey, user));
    }

    @Test
    void testIsSurveyEditableByUserSameCreatorClosed() {
        Survey survey = mock(Survey.class);
        User user = mock(User.class);
        when(survey.getCreator()).thenReturn(user);
        when(survey.getIsClosed()).thenReturn(true);

        assertFalse(surveyService.isSurveyEditableByUser(survey, user));
        assertFalse(surveyService.isSurveyClosableByUser(survey, user));
        assertFalse(surveyService.canUserRespondToSurvey(survey, user));
    }

    @Test
    void testIsSurveyEditableByUserOtherCreatorNotClosed() {
        Survey survey = mock(Survey.class);
        User user = mock(User.class);
        User creator = mock(User.class);
        when(survey.getCreator()).thenReturn(creator);
        when(survey.getIsClosed()).thenReturn(false);

        assertFalse(surveyService.isSurveyEditableByUser(survey, user));
        assertFalse(surveyService.isSurveyClosableByUser(survey, user));
        assertTrue(surveyService.canUserRespondToSurvey(survey, user));
    }

    @Test
    void testIsSurveyEditableByUserSameCreatorNotClosed() {
        Survey survey = mock(Survey.class);
        User user = mock(User.class);
        when(survey.getCreator()).thenReturn(user);
        when(survey.getIsClosed()).thenReturn(false);

        assertTrue(surveyService.isSurveyEditableByUser(survey, user));
        assertTrue(surveyService.isSurveyClosableByUser(survey, user));
        assertFalse(surveyService.canUserRespondToSurvey(survey, user));
    }

    @Test
    void testIsSurveyDeletableByUserOtherCreator() {
        Survey survey = mock(Survey.class);
        User user = mock(User.class);
        User creator = mock(User.class);
        when(survey.getCreator()).thenReturn(creator);

        assertFalse(surveyService.isSurveyDeletableByUser(survey, user));
    }

    @Test
    void testIsSurveyDeletableByUserSameCreator() {
        Survey survey = mock(Survey.class);
        User user = mock(User.class);
        when(survey.getCreator()).thenReturn(user);

        assertTrue(surveyService.isSurveyDeletableByUser(survey, user));
    }

    @Test
    void testQuerySurveysNull() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(42L);

        surveyService.querySurveys(user, empty(), empty(), empty(), empty(), empty());
        verify(surveyRepository, times(1)).querySurvey(42L, null, null, null, null, null);
    }

    @Test
    void testQuerySurveysTrue() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(42L);
        Optional<Boolean> opTrue = Optional.of(true);

        surveyService.querySurveys(user, opTrue, opTrue, opTrue, opTrue, opTrue);
        verify(surveyRepository, times(1)).querySurvey(42L, true, true, true, true, true);
    }

    @Test
    void testQuerySurveysFalse() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(42L);
        Optional<Boolean> opFalse = Optional.of(false);

        surveyService.querySurveys(user, opFalse, opFalse, opFalse, opFalse, opFalse);
        verify(surveyRepository, times(1)).querySurvey(42L, false, false, false, false, false);
    }
}
