package de.nordakademie.iaa.noodle.mapper;

import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
import de.nordakademie.iaa.noodle.api.model.ResponseValueDTO;
import de.nordakademie.iaa.noodle.model.*;
import de.nordakademie.iaa.noodle.services.ResponseService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for responses.
 *
 * @author Noah Peeters
 * @see Response
 * @see ResponseDTO
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring", uses = {UserMapper.class})
public abstract class ResponseMapper {

    @Autowired
    private ResponseService responseService;

    /**
     * Maps a list of participations to a list of response DTOs.
     *
     * @param participations The participations.
     * @param currentUser    The current user.
     * @return The mapped response DTOs.
     */
    @Named("participationsToDTOs")
    public List<ResponseDTO> participationsToDTOs(Set<Participation> participations, @Context User currentUser) {
        return participations.stream()
            .map(Participation::getResponse)
            .filter(Objects::nonNull)
            .map(response -> this.responseToDTO(response, currentUser))
            .collect(Collectors.toList());
    }

    /**
     * Maps a list of participations to the response of the current user.
     *
     * @param participations The participations.
     * @param currentUser    The current user.
     * @return The mapped response dto.
     */
    @Named("participationsToMyResponseDTO")
    public ResponseDTO participationsToMyResponseDTO(Set<Participation> participations, @Context User currentUser) {
        for (Participation participation : participations) {
            if (participation.getParticipant().equals(currentUser)) {
                return responseToDTO(participation.getResponse(), currentUser);
            }
        }

        return null;
    }

    /**
     * Maps a response to a response dto.
     *
     * @param response    The response to map.
     * @param currentUser The current user.
     * @return The mapped response dto.
     */
    @Mapping(target = "surveyID", source = "participation.survey.id")
    @Mapping(target = "responseID", source = "id")
    @Mapping(target = "user", source = "participation.participant", qualifiedByName = "userToIdentifiableUserDTO")
    @Mapping(target = "isEditable", source = ".", qualifiedByName = "responseIsEditable")
    @Mapping(target = "responses", source = "responseTimeslots", qualifiedByName = "responseTimeslotsToDTOs")
    public abstract ResponseDTO responseToDTO(Response response, @Context User currentUser);

    /**
     * Maps if a response is editable by the current user.
     *
     * @param response    The response to map.
     * @param currentUser The current user.
     * @return True, if the response is editable. False otherwise.
     */
    @Named("responseIsEditable")
    public boolean responseIsEditable(Response response, @Context User currentUser) {
        return responseService.isResponseEditableByUser(response, currentUser);
    }

    /**
     * Maps ResponseTimeslots to ResponseValueDTOs.
     *
     * @param responseTimeslots The ResponseTimeslots to map.
     * @return The mapped ResponseValueDTOs.
     */
    @Named("responseTimeslotsToDTOs")
    public List<ResponseValueDTO> responseTimeslotsToDTOs(Set<ResponseTimeslot> responseTimeslots) {
        return responseTimeslots
            .stream()
            .map(this::responseTimeslotToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Maps ResponseTimeslot to ResponseValueDTO.
     *
     * @param responseTimeslot The ResponseTimeslot to map.
     * @return The mapped ResponseValueDTO.
     */
    @Mapping(target = "timeslotID", source = "timeslot.id")
    @Mapping(target = "value", source = "responseType")
    public abstract ResponseValueDTO responseTimeslotToDTO(ResponseTimeslot responseTimeslot);

    /**
     * Maps a ResponseType to true or false.
     *
     * @param responseType The ResponseType to map.
     * @return True, of the ResponseType was YES. False otherwise.
     */
    public boolean responseTypeToDTO(ResponseType responseType) {
        return responseType == ResponseType.YES;
    }

    /**
     * Maps a ResponseValueDTO to a ResponseType.
     *
     * @param responseValueDTO The ResponseValueDTO to map.
     * @return The mapped ResponseType.
     */
    public ResponseType responseValueDTOToResponseType(ResponseValueDTO responseValueDTO) {
        return responseValueDTO.getValue() ? ResponseType.YES : ResponseType.NO;
    }

    /**
     * Maps ResponseValueDTO to a JAVA Map.
     *
     * @param responseValueDTOs The ResponseValueDTO to map.
     * @return A Java Map with the timeslot ID and the ResponseType.
     */
    public Map<Long, ResponseType> responseValueDTOsToMap(List<ResponseValueDTO> responseValueDTOs) {
        return responseValueDTOs
            .stream()
            .collect(Collectors.toMap(
                ResponseValueDTO::getTimeslotID,
                this::responseValueDTOToResponseType));
    }
}
