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

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring", uses = { UserMapper.class })
public abstract class ResponseMapper {

    @Autowired
    private ResponseService responseService;

    @Named("participationsToDTOs")
    public List<ResponseDTO> participationsToDTOs(Set<Participation> participations, @Context User currentUser) {
        return participations.stream()
            .map(Participation::getResponse)
            .filter(Objects::nonNull)
            .map(response -> this.responseToDTO(response, currentUser))
            .collect(Collectors.toList());
    }

    @Mapping(target = "surveyID", source = "participation.survey.id")
    @Mapping(target = "responseID", source = "id")
    @Mapping(target = "user", source = "participation.participant", qualifiedByName = "userToIdentifiableUserDTO")
    @Mapping(target = "isEditable", source = ".", qualifiedByName = "responseIsEditable")
    @Mapping(target = "responses", source = "responseTimeslots", qualifiedByName = "responseTimeslotsToDTOs")
    public abstract ResponseDTO responseToDTO(Response response, @Context User currentUser);

    @Named("responseIsEditable")
    public boolean responseIsEditable(Response response, @Context User currentUser) {
        return responseService.isResponseEditableByUser(response, currentUser);
    }

    @Named("responseTimeslotsToDTOs")
    public List<ResponseValueDTO> responseTimeslotsToDTOs(Set<ResponseTimeslot> responseTimeslots) {
        return responseTimeslots
            .stream()
            .map(this::responseTimeslotToDTO)
            .collect(Collectors.toList());
    }

    @Mapping(target = "timeslotID", source = "timeslot.id")
    @Mapping(target = "value", source = "responseType")
    public abstract ResponseValueDTO responseTimeslotToDTO(ResponseTimeslot responseTimeslot);

    public boolean responseTypeToDTO(ResponseType responseType) {
        return responseType == ResponseType.YES;
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
}
