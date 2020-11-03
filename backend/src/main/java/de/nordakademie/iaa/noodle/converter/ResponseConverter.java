package de.nordakademie.iaa.noodle.converter;

import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
import de.nordakademie.iaa.noodle.api.model.ResponseValueDTO;
import de.nordakademie.iaa.noodle.model.*;
import de.nordakademie.iaa.noodle.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ResponseConverter {
    private final UserConverter userConverter;
    private final ResponseService responseService;

    @Autowired
    public ResponseConverter(UserConverter userConverter, ResponseService responseService) {
        this.userConverter = userConverter;
        this.responseService = responseService;
    }

    public ResponseValueDTO convertResponseTimeslotToDTO(ResponseTimeslot responseTimeslot) {
        ResponseValueDTO responseValueDTO = new ResponseValueDTO();
        responseValueDTO.setTimeslotID(responseTimeslot.getTimeslot().getId());
        responseValueDTO.setValue(responseTimeslot.getResponseType() == ResponseType.YES);
        return responseValueDTO;
    }

    public ResponseType convertResponseValueDTOToResponseType(ResponseValueDTO responseValueDTO) {
        return responseValueDTO.getValue() ? ResponseType.YES : ResponseType.NO;
    }

    public Map<Long, ResponseType> convertResponseValueDTOsToMap(List<ResponseValueDTO> responseValueDTOs) {
        return responseValueDTOs
            .stream()
            .collect(Collectors.toMap(
                ResponseValueDTO::getTimeslotID,
                this::convertResponseValueDTOToResponseType));
    }

    public ResponseDTO convertResponseToDTO(Response response, User currentUser) {
        User responseUser = response.getParticipation().getParticipant();
        Survey survey = response.getParticipation().getSurvey();

        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setIsEditable(responseService.responseIsEditableByUser(response, currentUser));
        responseDTO.setSurveyID(survey.getId());
        responseDTO.setResponseID(response.getId());
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
}
