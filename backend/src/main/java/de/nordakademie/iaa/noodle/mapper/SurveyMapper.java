package de.nordakademie.iaa.noodle.mapper;

import de.nordakademie.iaa.noodle.api.model.QuerySurveysResponse;
import de.nordakademie.iaa.noodle.api.model.QuerySurveysResult;
import de.nordakademie.iaa.noodle.api.model.SurveyDTO;
import de.nordakademie.iaa.noodle.api.model.SurveyMetadataDTO;
import de.nordakademie.iaa.noodle.dao.model.QuerySurveysItem;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.interfaces.SurveyService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for surveys.
 *
 * @author Noah Peeters
 * @author Hans Rißer
 * @see Survey
 * @see QuerySurveysItem
 * @see SurveyDTO
 * @see SurveyMetadataDTO
 * @see QuerySurveysResult
 * @see QuerySurveysResponse
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN,
        componentModel = "spring",
        uses = {UserMapper.class, ResponseMapper.class, TimeslotMapper.class})
public abstract class SurveyMapper {

    @Autowired
    private SurveyService surveyService;

    /**
     * Maps a survey to a SurveyDTO.
     *
     * @param survey      The survey to map.
     * @param currentUser The current user.
     * @return The mapped SurveyDTO.
     */
    @Mapping(target = "creator", qualifiedByName = "userToIdentifiableUserDTO")
    @Mapping(target = "isEditable", source = ".", qualifiedByName = "surveyIsEditable")
    @Mapping(target = "isClosable", source = ".", qualifiedByName = "surveyIsClosable")
    @Mapping(target = "isDeletable", source = ".", qualifiedByName = "surveyIsDeletable")
    @Mapping(target = "responses", source = "participations", qualifiedByName = "participationsToDTOs")
    @Mapping(target = "myResponse", source = "participations", qualifiedByName = "participationsToMyResponseDTO")
    public abstract SurveyDTO surveyToDTO(Survey survey, @Context User currentUser);

    /**
     * Maps a survey to a SurveyMetadataDTO.
     *
     * @param survey      The survey to map.
     * @param currentUser The current user.
     * @return The mapped SurveyMetadataDTO.
     */
    @Named("surveyToMetadataDTO")
    @Mapping(target = "creator", qualifiedByName = "userToIdentifiableUserDTO")
    @Mapping(target = "isEditable", source = ".", qualifiedByName = "surveyIsEditable")
    @Mapping(target = "isClosable", source = ".", qualifiedByName = "surveyIsClosable")
    @Mapping(target = "isDeletable", source = ".", qualifiedByName = "surveyIsDeletable")
    public abstract SurveyMetadataDTO surveyToMetadataDTO(Survey survey, @Context User currentUser);


    /**
     * Maps surveys to a QuerySurveysResponse.
     *
     * @param surveys The surveys to map.
     * @return The mapped QuerySurveysResponse.
     */
    public QuerySurveysResponse surveysToSurveysDTO(List<QuerySurveysItem> surveys) {
        QuerySurveysResponse response = new QuerySurveysResponse();
        response.setSurveys(surveys.stream().map(this::surveyToSurveyDTO).collect(Collectors.toList()));
        return response;
    }

    /**
     * Maps a QuerySurveysItem to a QuerySurveysResult.
     *
     * @param survey The survey data to map.
     * @return The mapped QuerySurveysResult.
     */
    @Named("surveyToSurveyDTO")
    @Mapping(target = "participantCount", source = "responseCount")
    public abstract QuerySurveysResult surveyToSurveyDTO(QuerySurveysItem survey);

    /**
     * Maps if a user can edit a survey.
     *
     * @param survey      The survey.
     * @param currentUser The user.
     * @return True, if the user can edit the survey. False otherwise.
     */
    @Named("surveyIsEditable")
    public boolean surveyIsEditable(Survey survey, @Context User currentUser) {
        return surveyService.isSurveyEditableByUser(survey, currentUser);
    }

    /**
     * Maps if a user can close a survey.
     *
     * @param survey      The survey.
     * @param currentUser The user.
     * @return True, if the user can close the survey. False otherwise.
     */
    @Named("surveyIsClosable")
    public boolean surveyIsClosable(Survey survey, @Context User currentUser) {
        return surveyService.isSurveyClosableByUser(survey, currentUser);
    }

    /**
     * Maps if a user can delete a survey.
     *
     * @param survey      The survey.
     * @param currentUser The user.
     * @return True, if the user can delete the survey. False otherwise.
     */
    @Named("surveyIsDeletable")
    public boolean surveyIsDeletable(Survey survey, @Context User currentUser) {
        return surveyService.isSurveyDeletableByUser(survey, currentUser);
    }
}
