package de.nordakademie.iaa.noodle.converter;

import de.nordakademie.iaa.noodle.api.model.IdentifiableUserDTO;
import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
import de.nordakademie.iaa.noodle.api.model.ResponseValueDTO;
import de.nordakademie.iaa.noodle.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        boolean surveyAcceptsResponses = !survey.getIsClosed();
        boolean responderIsCurrentUser = responseUser.equals(currentUser);
        boolean isCreatorsResponse = survey.getCreator().equals(responseUser);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setIsEditable(surveyAcceptsResponses && responderIsCurrentUser && !isCreatorsResponse);
        responseDTO.setSurveyID(survey.getId());

        responseDTO.setUser(userConverter.convertUserToDTO(responseUser));

        List<ResponseValueDTO> responseValues = response.getResponseTimeslots()
            .stream()
            .map(this::convertResponseTimeslotToDTO)
            .collect(Collectors.toList());

        responseDTO.setResponses(responseValues);
        return responseDTO;
    }
}
