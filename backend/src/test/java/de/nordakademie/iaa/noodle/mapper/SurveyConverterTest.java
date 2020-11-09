//package de.nordakademie.iaa.noodle.converter;
//
//import de.nordakademie.iaa.noodle.api.model.*;
//import de.nordakademie.iaa.noodle.model.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static java.time.ZoneOffset.UTC;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class SurveyConverterTest {
//    private SurveyConverter surveyConverter;
//    private ResponseConverter responseConverter;
//    private UserConverter userConverter;
//    private final ResponseDTO[] emptyResponseDTOs = {};
//    private final TimeslotDTO[] emptyTimeslotDTOs = {};
//
//    @BeforeEach
//    public void setUp() {
//        userConverter = mock(UserConverter.class);
//        responseConverter = mock(ResponseConverter.class);
//        surveyConverter = new SurveyConverter(userConverter, responseConverter);
//    }
//
//    @Test
//    public void testConvertTimeslotToAPI() {
//        Timeslot timeslot = mock(Timeslot.class);
//
//        Date startDate = new Date(0);
//        Date endDate = new Date(1);
//
//        when(timeslot.getId()).thenReturn(42L);
//        when(timeslot.getStart()).thenReturn(startDate);
//        when(timeslot.getEnd()).thenReturn(endDate);
//
//        TimeslotDTO timeslotDTO = surveyConverter.convertTimeslotToDTO(timeslot);
//
//        assertEquals(42L, timeslotDTO.getId());
//        assertEquals(startDate.toInstant().atOffset(UTC), timeslotDTO.getStart());
//        assertEquals(endDate.toInstant().atOffset(UTC), timeslotDTO.getEnd());
//    }
//
//    @Test
//    public void testConvertSurveyToDTOClosed() {
//        Survey survey = mock(Survey.class);
//        User currentUser = mock(User.class);
//        User creator = mock(User.class);
//        Timeslot selectedTimeslot = mock(Timeslot.class);
//        IdentifiableUserDTO creatorDTO = mock(IdentifiableUserDTO.class);
//
//        Date selectedTimeslotStartDate = new Date(0);
//        Date selectedTimeslotEndDate = new Date(1);
//
//        when(selectedTimeslot.getId()).thenReturn(42L);
//        when(selectedTimeslot.getStart()).thenReturn(selectedTimeslotStartDate);
//        when(selectedTimeslot.getEnd()).thenReturn(selectedTimeslotEndDate);
//
//        TimeslotDTO selectedTimeslotDTO = new TimeslotDTO();
//        selectedTimeslotDTO.setId(42L);
//        selectedTimeslotDTO.setStart(selectedTimeslotStartDate.toInstant().atOffset(UTC));
//        selectedTimeslotDTO.setEnd(selectedTimeslotEndDate.toInstant().atOffset(UTC));
//        TimeslotDTO[] timeslotDTOs = { selectedTimeslotDTO };
//
//        when(survey.getTitle()).thenReturn("TITLE");
//        when(survey.getDescription()).thenReturn("DESCRIPTION");
//        when(survey.getIsClosed()).thenReturn(true);
//        when(survey.getSelectedTimeslot()).thenReturn(selectedTimeslot);
//        when(survey.getCreator()).thenReturn(creator);
//        when(survey.getTimeslots()).thenReturn(Collections.singleton(selectedTimeslot));
//        when(survey.getParticipations()).thenReturn(Collections.emptySet());
//        when(userConverter.convertUserToDTO(creator)).thenReturn(creatorDTO);
//
//        SurveyDTO surveyDTO = surveyConverter.convertSurveyToDTO(survey, currentUser);
//
//        assertEquals("TITLE", surveyDTO.getTitle());
//        assertEquals("DESCRIPTION", surveyDTO.getDescription());
//        assertEquals(true, surveyDTO.getIsClosed());
//        assertEquals(selectedTimeslotDTO, surveyDTO.getSelectedTimeslot());
//        assertEquals(creatorDTO, surveyDTO.getCreator());
//        assertArrayEquals(emptyResponseDTOs, surveyDTO.getResponses().toArray());
//        assertArrayEquals(timeslotDTOs, surveyDTO.getTimeslots().toArray());
//    }
//
//    @Test
//    public void testConvertSurveyToDTONotClosed() {
//        Survey survey = mock(Survey.class);
//        User currentUser = mock(User.class);
//        User creator = mock(User.class);
//        Timeslot timeslot = mock(Timeslot.class);
//        IdentifiableUserDTO creatorDTO = mock(IdentifiableUserDTO.class);
//
//        Date timeslotStartDate = new Date(0);
//        Date timeslotEndDate = new Date(1);
//
//        when(timeslot.getId()).thenReturn(42L);
//        when(timeslot.getStart()).thenReturn(timeslotStartDate);
//        when(timeslot.getEnd()).thenReturn(timeslotEndDate);
//
//        TimeslotDTO timeslotDTO = new TimeslotDTO();
//        timeslotDTO.setId(42L);
//        timeslotDTO.setStart(timeslotStartDate.toInstant().atOffset(UTC));
//        timeslotDTO.setEnd(timeslotEndDate.toInstant().atOffset(UTC));
//        TimeslotDTO[] timeslotDTOs = { timeslotDTO };
//
//        when(survey.getTitle()).thenReturn("TITLE");
//        when(survey.getDescription()).thenReturn("DESCRIPTION");
//        when(survey.getIsClosed()).thenReturn(false);
//        when(survey.getSelectedTimeslot()).thenReturn(null);
//        when(survey.getCreator()).thenReturn(creator);
//        when(survey.getTimeslots()).thenReturn(Collections.singleton(timeslot));
//        when(survey.getParticipations()).thenReturn(Collections.emptySet());
//        when(userConverter.convertUserToDTO(creator)).thenReturn(creatorDTO);
//
//        SurveyDTO surveyDTO = surveyConverter.convertSurveyToDTO(survey, currentUser);
//
//        assertEquals("TITLE", surveyDTO.getTitle());
//        assertEquals("DESCRIPTION", surveyDTO.getDescription());
//        assertEquals(false, surveyDTO.getIsClosed());
//        assertNull(surveyDTO.getSelectedTimeslot());
//        assertEquals(creatorDTO, surveyDTO.getCreator());
//        assertArrayEquals(emptyResponseDTOs, surveyDTO.getResponses().toArray());
//        assertArrayEquals(timeslotDTOs, surveyDTO.getTimeslots().toArray());
//    }
//
//    @Test
//    public void testConvertSurveyToDTOOneStaleParticipation() {
//        Survey survey = mock(Survey.class);
//        User currentUser = mock(User.class);
//        User creator = mock(User.class);
//        IdentifiableUserDTO creatorDTO = mock(IdentifiableUserDTO.class);
//        Participation staleParticipation = mock(Participation.class);
//
//        when(survey.getTitle()).thenReturn("TITLE");
//        when(survey.getDescription()).thenReturn("DESCRIPTION");
//        when(survey.getIsClosed()).thenReturn(false);
//        when(survey.getSelectedTimeslot()).thenReturn(null);
//        when(survey.getCreator()).thenReturn(creator);
//        when(survey.getTimeslots()).thenReturn(Collections.emptySet());
//        when(survey.getParticipations()).thenReturn(Collections.singleton(staleParticipation));
//        when(staleParticipation.getResponse()).thenReturn(null);
//        when(userConverter.convertUserToDTO(creator)).thenReturn(creatorDTO);
//
//        SurveyDTO surveyDTO = surveyConverter.convertSurveyToDTO(survey, currentUser);
//
//        assertEquals("TITLE", surveyDTO.getTitle());
//        assertEquals("DESCRIPTION", surveyDTO.getDescription());
//        assertEquals(false, surveyDTO.getIsClosed());
//        assertNull(surveyDTO.getSelectedTimeslot());
//        assertEquals(creatorDTO, surveyDTO.getCreator());
//        assertArrayEquals(emptyResponseDTOs, surveyDTO.getResponses().toArray());
//        assertArrayEquals(emptyTimeslotDTOs, surveyDTO.getTimeslots().toArray());
//    }
//
//    @Test
//    public void testConvertSurveyToDTOOneStaleParticipationAndOneResponse() {
//        Survey survey = mock(Survey.class);
//        User currentUser = mock(User.class);
//        User creator = mock(User.class);
//        IdentifiableUserDTO creatorDTO = mock(IdentifiableUserDTO.class);
//        Participation staleParticipation = mock(Participation.class);
//        Participation participation = mock(Participation.class);
//        Response response = mock(Response.class);
//        ResponseDTO responseDTO = mock(ResponseDTO.class);
//        ResponseDTO[] responseDTOs = { responseDTO };
//
//        Set<Participation> participations = Stream.of(staleParticipation, participation).collect(Collectors.toSet());
//
//        when(survey.getTitle()).thenReturn("TITLE");
//        when(survey.getDescription()).thenReturn("DESCRIPTION");
//        when(survey.getIsClosed()).thenReturn(false);
//        when(survey.getSelectedTimeslot()).thenReturn(null);
//        when(survey.getCreator()).thenReturn(creator);
//        when(survey.getTimeslots()).thenReturn(Collections.emptySet());
//        when(survey.getParticipations()).thenReturn(participations);
//        when(staleParticipation.getResponse()).thenReturn(null);
//        when(participation.getResponse()).thenReturn(response);
//        when(userConverter.convertUserToDTO(creator)).thenReturn(creatorDTO);
//        when(responseConverter.convertResponseToDTO(response, currentUser)).thenReturn(responseDTO);
//
//        SurveyDTO surveyDTO = surveyConverter.convertSurveyToDTO(survey, currentUser);
//
//        assertEquals("TITLE", surveyDTO.getTitle());
//        assertEquals("DESCRIPTION", surveyDTO.getDescription());
//        assertEquals(false, surveyDTO.getIsClosed());
//        assertNull(surveyDTO.getSelectedTimeslot());
//        assertEquals(creatorDTO, surveyDTO.getCreator());
//        assertArrayEquals(responseDTOs, surveyDTO.getResponses().toArray());
//        assertArrayEquals(emptyTimeslotDTOs, surveyDTO.getTimeslots().toArray());
//    }
//}
