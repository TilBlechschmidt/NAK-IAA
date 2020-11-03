package de.nordakademie.iaa.noodle.converter;

import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
import de.nordakademie.iaa.noodle.api.model.ResponseValueDTO;
import de.nordakademie.iaa.noodle.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponseConverter {
    private final UserConverter userConverter;

    @Autowired
    public ResponseConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public ResponseValueDTO convertResponseTimeslotToDTO(ResponseTimeslot responseTimeslot) {
        ResponseValueDTO responseValueDTO = new ResponseValueDTO();
        responseValueDTO.setTimeslotID(responseTimeslot.getTimeslot().getId());
        responseValueDTO.setValue(responseTimeslot.getResponseType() == ResponseType.YES);
        return responseValueDTO;
    }

    public ResponseDTO convertResponseToDTO(Response response, User currentUser) {
        User responseUser = response.getParticipation().getParticipant();
        Survey survey = response.getParticipation().getSurvey();

        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setIsEditable(responseIsEditableByCurrentUser(survey, responseUser, currentUser));
        responseDTO.setSurveyID(survey.getId());
        responseDTO.setUser(userConverter.convertUserToDTO(responseUser));
        responseDTO.setResponses(convertResponseTimeslotsToDTO(response.getResponseTimeslots()));

        return responseDTO;
    }

    private List<ResponseValueDTO> convertResponseTimeslotsToDTO(Collection<ResponseTimeslot> responseTimeslots) {
        return responseTimeslots
            .stream()
            .map(this::convertResponseTimeslotToDTO)
            .collect(Collectors.toList());
    }

    private boolean responseIsEditableByCurrentUser(Survey survey, User responseUser, User currentUser) {
        boolean surveyAcceptsResponses = !survey.getIsClosed();
        boolean responderIsCurrentUser = responseUser.equals(currentUser);
        boolean isCreatorsResponse = survey.getCreator().equals(responseUser);

        return surveyAcceptsResponses && responderIsCurrentUser && !isCreatorsResponse;
    }
}
