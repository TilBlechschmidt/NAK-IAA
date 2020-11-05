package de.nordakademie.iaa.noodle.mapper;

import de.nordakademie.iaa.noodle.api.model.SurveyDTO;
import de.nordakademie.iaa.noodle.api.model.SurveyMetadataDTO;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN,
        componentModel = "spring",
        uses = { UserMapper.class, ResponseMapper.class, TimeslotMapper.class })
public interface SurveyMapper {

    @Mapping(target = "selectedTimeslot", source = "survey.chosenTimeslot")
    @Mapping(target = "creator", qualifiedByName = "userToIdentifiableUserDTO")
    @Mapping(target = "responses", source = "survey.participations", qualifiedByName = "participationsToDTOs")
    SurveyDTO surveyToDTO(Survey survey, @Context User currentUser);

    @Mapping(target = "selectedTimeslot", source = "chosenTimeslot")
    @Mapping(target = "creator", qualifiedByName = "userToIdentifiableUserDTO")
    SurveyMetadataDTO surveyToMetadataDTO(Survey survey);
}
