package de.nordakademie.iaa.noodle.converter;

import de.nordakademie.iaa.noodle.api.model.IdentifiableUserDTO;
import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
import de.nordakademie.iaa.noodle.api.model.ResponseValueDTO;
import de.nordakademie.iaa.noodle.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseConverterTest {
    private ResponseConverter responseConverter;
    private UserConverter userConverter;
    private final ResponseValueDTO[] emptyResponseValueDTOs = {};

    @BeforeEach
    public void setUp() {
        userConverter = mock(UserConverter.class);
        responseConverter = new ResponseConverter(userConverter);
    }

    @Test
    public void testConvertResponseTimeslotToDTO() {
        ResponseTimeslot responseTimeslot = mock(ResponseTimeslot.class);
        Timeslot timeslot = mock(Timeslot.class);

        when(responseTimeslot.getTimeslot()).thenReturn(timeslot);
        when(timeslot.getId()).thenReturn(42L);
        when(responseTimeslot.getResponseType()).thenReturn(ResponseType.YES);

        ResponseValueDTO responseValueDTO = responseConverter.convertResponseTimeslotToDTO(responseTimeslot);

        assertEquals(42L, responseValueDTO.getTimeslotID());
        assertEquals(true, responseValueDTO.getValue());
    }

    @Test
    public void testConvertResponseToDTOClosedSameAsParticipantAndCreator() {
        Response response = mock(Response.class);
        Survey survey = mock(Survey.class);
        Participation participation = mock(Participation.class);
        User currentUser = mock(User.class);

        IdentifiableUserDTO identifiableUserDTO = mock(IdentifiableUserDTO.class);

        when(response.getParticipation()).thenReturn(participation);
        when(participation.getParticipant()).thenReturn(currentUser);
        when(participation.getSurvey()).thenReturn(survey);
        when(survey.getId()).thenReturn(42L);
        when(response.getId()).thenReturn(43L);
        when(survey.getIsClosed()).thenReturn(true);
        when(survey.getCreator()).thenReturn(currentUser);
        when(userConverter.convertUserToDTO(currentUser)).thenReturn(identifiableUserDTO);

        ResponseDTO responseDTO = responseConverter.convertResponseToDTO(response, currentUser);

        assertEquals(42L, responseDTO.getSurveyID());
        assertEquals(43L, responseDTO.getResponseID());
        assertEquals(false, responseDTO.getIsEditable());
        assertEquals(identifiableUserDTO, responseDTO.getUser());
        assertArrayEquals(emptyResponseValueDTOs, responseDTO.getResponses().toArray());
    }

    @Test
    public void testConvertResponseToDTONotClosedSameAsParticipantAndCreator() {
        Response response = mock(Response.class);
        Survey survey = mock(Survey.class);
        Participation participation = mock(Participation.class);
        User currentUser = mock(User.class);

        IdentifiableUserDTO identifiableUserDTO = mock(IdentifiableUserDTO.class);

        when(response.getParticipation()).thenReturn(participation);
        when(participation.getParticipant()).thenReturn(currentUser);
        when(participation.getSurvey()).thenReturn(survey);
        when(survey.getId()).thenReturn(42L);
        when(response.getId()).thenReturn(43L);
        when(survey.getIsClosed()).thenReturn(false);
        when(survey.getCreator()).thenReturn(currentUser);
        when(userConverter.convertUserToDTO(currentUser)).thenReturn(identifiableUserDTO);

        ResponseDTO responseDTO = responseConverter.convertResponseToDTO(response, currentUser);

        assertEquals(42L, responseDTO.getSurveyID());
        assertEquals(43L, responseDTO.getResponseID());
        assertEquals(false, responseDTO.getIsEditable());
        assertEquals(identifiableUserDTO, responseDTO.getUser());
        assertArrayEquals(emptyResponseValueDTOs, responseDTO.getResponses().toArray());
    }

    @Test
    public void testConvertResponseToDTOClosedSameAsParticipant() {
        Response response = mock(Response.class);
        Survey survey = mock(Survey.class);
        Participation participation = mock(Participation.class);
        User currentUser = mock(User.class);
        User creator = mock(User.class);

        IdentifiableUserDTO identifiableUserDTO = mock(IdentifiableUserDTO.class);

        when(response.getParticipation()).thenReturn(participation);
        when(participation.getParticipant()).thenReturn(currentUser);
        when(participation.getSurvey()).thenReturn(survey);
        when(survey.getId()).thenReturn(42L);
        when(response.getId()).thenReturn(43L);
        when(survey.getIsClosed()).thenReturn(true);
        when(survey.getCreator()).thenReturn(creator);
        when(userConverter.convertUserToDTO(currentUser)).thenReturn(identifiableUserDTO);

        ResponseDTO responseDTO = responseConverter.convertResponseToDTO(response, currentUser);

        assertEquals(42L, responseDTO.getSurveyID());
        assertEquals(43L, responseDTO.getResponseID());
        assertEquals(false, responseDTO.getIsEditable());
        assertEquals(identifiableUserDTO, responseDTO.getUser());
        assertArrayEquals(emptyResponseValueDTOs, responseDTO.getResponses().toArray());
    }

    @Test
    public void testConvertResponseToDTONotClosedSameAsParticipant() {
        Response response = mock(Response.class);
        Survey survey = mock(Survey.class);
        Participation participation = mock(Participation.class);
        User currentUser = mock(User.class);
        User creator = mock(User.class);

        IdentifiableUserDTO identifiableUserDTO = mock(IdentifiableUserDTO.class);

        when(response.getParticipation()).thenReturn(participation);
        when(participation.getParticipant()).thenReturn(currentUser);
        when(participation.getSurvey()).thenReturn(survey);
        when(survey.getId()).thenReturn(42L);
        when(response.getId()).thenReturn(43L);
        when(survey.getIsClosed()).thenReturn(false);
        when(survey.getCreator()).thenReturn(creator);
        when(userConverter.convertUserToDTO(currentUser)).thenReturn(identifiableUserDTO);

        ResponseDTO responseDTO = responseConverter.convertResponseToDTO(response, currentUser);

        assertEquals(42L, responseDTO.getSurveyID());
        assertEquals(43L, responseDTO.getResponseID());
        assertEquals(true, responseDTO.getIsEditable());
        assertEquals(identifiableUserDTO, responseDTO.getUser());
        assertArrayEquals(emptyResponseValueDTOs, responseDTO.getResponses().toArray());
    }

    @Test
    public void testConvertResponseToDTOClosedSameAsCreator() {
        Response response = mock(Response.class);
        Survey survey = mock(Survey.class);
        Participation participation = mock(Participation.class);
        User currentUser = mock(User.class);
        User participant = mock(User.class);

        IdentifiableUserDTO identifiableUserDTO = mock(IdentifiableUserDTO.class);

        when(response.getParticipation()).thenReturn(participation);
        when(participation.getParticipant()).thenReturn(participant);
        when(participation.getSurvey()).thenReturn(survey);
        when(survey.getId()).thenReturn(42L);
        when(response.getId()).thenReturn(43L);
        when(survey.getIsClosed()).thenReturn(true);
        when(survey.getCreator()).thenReturn(currentUser);
        when(userConverter.convertUserToDTO(participant)).thenReturn(identifiableUserDTO);

        ResponseDTO responseDTO = responseConverter.convertResponseToDTO(response, currentUser);

        assertEquals(42L, responseDTO.getSurveyID());
        assertEquals(43L, responseDTO.getResponseID());
        assertEquals(false, responseDTO.getIsEditable());
        assertEquals(identifiableUserDTO, responseDTO.getUser());
        assertArrayEquals(emptyResponseValueDTOs, responseDTO.getResponses().toArray());
    }

    @Test
    public void testConvertResponseToDTONotClosedSameAsCreator() {
        Response response = mock(Response.class);
        Survey survey = mock(Survey.class);
        Participation participation = mock(Participation.class);
        User currentUser = mock(User.class);
        User participant = mock(User.class);

        IdentifiableUserDTO identifiableUserDTO = mock(IdentifiableUserDTO.class);

        when(response.getParticipation()).thenReturn(participation);
        when(participation.getParticipant()).thenReturn(participant);
        when(participation.getSurvey()).thenReturn(survey);
        when(survey.getId()).thenReturn(42L);
        when(response.getId()).thenReturn(43L);
        when(survey.getIsClosed()).thenReturn(false);
        when(survey.getCreator()).thenReturn(currentUser);
        when(userConverter.convertUserToDTO(participant)).thenReturn(identifiableUserDTO);

        ResponseDTO responseDTO = responseConverter.convertResponseToDTO(response, currentUser);

        assertEquals(42L, responseDTO.getSurveyID());
        assertEquals(43L, responseDTO.getResponseID());
        assertEquals(false, responseDTO.getIsEditable());
        assertEquals(identifiableUserDTO, responseDTO.getUser());
        assertArrayEquals(emptyResponseValueDTOs, responseDTO.getResponses().toArray());
    }

    @Test
    public void testConvertResponseToDTOClosed() {
        Response response = mock(Response.class);
        Survey survey = mock(Survey.class);
        Participation participation = mock(Participation.class);
        User currentUser = mock(User.class);
        User creator = mock(User.class);
        User participant = mock(User.class);

        IdentifiableUserDTO identifiableUserDTO = mock(IdentifiableUserDTO.class);

        when(response.getParticipation()).thenReturn(participation);
        when(participation.getParticipant()).thenReturn(participant);
        when(participation.getSurvey()).thenReturn(survey);
        when(survey.getId()).thenReturn(42L);
        when(response.getId()).thenReturn(43L);
        when(survey.getIsClosed()).thenReturn(true);
        when(survey.getCreator()).thenReturn(creator);
        when(userConverter.convertUserToDTO(participant)).thenReturn(identifiableUserDTO);

        ResponseDTO responseDTO = responseConverter.convertResponseToDTO(response, currentUser);

        assertEquals(42L, responseDTO.getSurveyID());
        assertEquals(43L, responseDTO.getResponseID());
        assertEquals(false, responseDTO.getIsEditable());
        assertEquals(identifiableUserDTO, responseDTO.getUser());
        assertArrayEquals(emptyResponseValueDTOs, responseDTO.getResponses().toArray());
    }

    @Test
    public void testConvertResponseToDTONotClosed() {
        Response response = mock(Response.class);
        Survey survey = mock(Survey.class);
        Participation participation = mock(Participation.class);
        User currentUser = mock(User.class);
        User creator = mock(User.class);
        User participant = mock(User.class);

        IdentifiableUserDTO identifiableUserDTO = mock(IdentifiableUserDTO.class);

        when(response.getParticipation()).thenReturn(participation);
        when(participation.getParticipant()).thenReturn(participant);
        when(participation.getSurvey()).thenReturn(survey);
        when(survey.getId()).thenReturn(42L);
        when(response.getId()).thenReturn(43L);
        when(survey.getIsClosed()).thenReturn(false);
        when(survey.getCreator()).thenReturn(creator);
        when(userConverter.convertUserToDTO(participant)).thenReturn(identifiableUserDTO);

        ResponseDTO responseDTO = responseConverter.convertResponseToDTO(response, currentUser);

        assertEquals(42L, responseDTO.getSurveyID());
        assertEquals(43L, responseDTO.getResponseID());
        assertEquals(false, responseDTO.getIsEditable());
        assertEquals(identifiableUserDTO, responseDTO.getUser());
        assertArrayEquals(emptyResponseValueDTOs, responseDTO.getResponses().toArray());
    }

    @Test
    public void testConvertResponseToDTOOneResponse() {
        Response response = mock(Response.class);
        Survey survey = mock(Survey.class);
        Participation participation = mock(Participation.class);
        User currentUser = mock(User.class);
        ResponseTimeslot responseTimeslot = mock(ResponseTimeslot.class);
        Timeslot timeslot = mock(Timeslot.class);

        IdentifiableUserDTO identifiableUserDTO = mock(IdentifiableUserDTO.class);

        when(response.getParticipation()).thenReturn(participation);
        when(participation.getParticipant()).thenReturn(currentUser);
        when(participation.getSurvey()).thenReturn(survey);
        when(survey.getId()).thenReturn(42L);
        when(response.getId()).thenReturn(43L);
        when(survey.getIsClosed()).thenReturn(true);
        when(survey.getCreator()).thenReturn(currentUser);
        when(userConverter.convertUserToDTO(currentUser)).thenReturn(identifiableUserDTO);
        when(response.getResponseTimeslots()).thenReturn(Collections.singleton(responseTimeslot));
        when(responseTimeslot.getResponseType()).thenReturn(ResponseType.YES);
        when(responseTimeslot.getTimeslot()).thenReturn(timeslot);
        when(timeslot.getId()).thenReturn(44L);

        ResponseDTO responseDTO = responseConverter.convertResponseToDTO(response, currentUser);

        assertEquals(42L, responseDTO.getSurveyID());
        assertEquals(43L, responseDTO.getResponseID());
        assertEquals(false, responseDTO.getIsEditable());
        assertEquals(identifiableUserDTO, responseDTO.getUser());

        ResponseValueDTO responseValueDTO = new ResponseValueDTO();
        responseValueDTO.setTimeslotID(44L);
        responseValueDTO.setValue(true);

        ResponseValueDTO[] responseValueDTOs = { responseValueDTO };
        assertArrayEquals(responseValueDTOs, responseDTO.getResponses().toArray());
    }
}
