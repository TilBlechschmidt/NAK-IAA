package de.nordakademie.iaa.noodle.mapper;

import de.nordakademie.iaa.noodle.api.model.SurveyDTO;
import de.nordakademie.iaa.noodle.api.model.SurveyMetadataDTO;
import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.SurveyService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN,
        componentModel = "spring",
        uses = { UserMapper.class, ResponseMapper.class, TimeslotMapper.class })
public abstract class SurveyMapper {

    @Autowired
    private SurveyService surveyService;

    @Mapping(target = "selectedTimeslot", source = "chosenTimeslot")
    @Mapping(target = "creator", qualifiedByName = "userToIdentifiableUserDTO")
    @Mapping(target = "isEditable", source = ".", qualifiedByName = "surveyIsEditable")
    @Mapping(target = "isClosable", source = ".", qualifiedByName = "surveyIsClosable")
    @Mapping(target = "isDeletable", source = ".", qualifiedByName = "surveyIsDeletable")
    @Mapping(target = "responses", source = "participations", qualifiedByName = "participationsToDTOs")
    public abstract SurveyDTO surveyToDTO(Survey survey, @Context User currentUser);

    @Named("surveyToMetadataDTO")
    @Mapping(target = "selectedTimeslot", source = "chosenTimeslot")
    @Mapping(target = "creator", qualifiedByName = "userToIdentifiableUserDTO")
    @Mapping(target = "isEditable", source = ".", qualifiedByName = "surveyIsEditable")
    @Mapping(target = "isClosable", source = ".", qualifiedByName = "surveyIsClosable")
    @Mapping(target = "isDeletable", source = ".", qualifiedByName = "surveyIsDeletable")
    public abstract SurveyMetadataDTO surveyToMetadataDTO(Survey survey, @Context User currentUser);

    @Named("surveyIsEditable")
    public boolean surveyIsEditable(Survey survey, @Context User currentUser) {
        return surveyService.isSurveyEditableByUser(survey, currentUser);
    }

    @Named("surveyIsClosable")
    public boolean surveyIsClosable(Survey survey, @Context User currentUser) {
        return surveyService.isSurveyClosableByUser(survey, currentUser);
    }

    @Named("surveyIsDeletable")
    public boolean surveyIsDeletable(Survey survey, @Context User currentUser) {
        return surveyService.isSurveyDeletableByUser(survey, currentUser);
    }
}
