package de.nordakademie.iaa.noodle.converter;

import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
import de.nordakademie.iaa.noodle.api.model.SurveyDTO;
import de.nordakademie.iaa.noodle.api.model.TimeslotDTO;
import de.nordakademie.iaa.noodle.model.Participation;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.Timeslot;
import de.nordakademie.iaa.noodle.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SurveyConverter {
    private final UserConverter userConverter;
    private final ResponseConverter responseConverter;

    @Autowired
    public SurveyConverter(UserConverter userConverter, ResponseConverter responseConverter) {
        this.userConverter = userConverter;
        this.responseConverter = responseConverter;
    }

    public TimeslotDTO convertTimeslotToDTO(Timeslot timeslot) {
        TimeslotDTO timeslotDTO = new TimeslotDTO();
        timeslotDTO.setId(timeslot.getId());
        timeslotDTO.setStart(timeslot.getStart().toInstant().atOffset(ZoneOffset.UTC));
        timeslotDTO.setEnd(timeslot.getEnd().toInstant().atOffset(ZoneOffset.UTC));
        return timeslotDTO;
    }

    public SurveyDTO convertSurveyToDTO(Survey survey, User currentUser) {
        SurveyDTO surveyDTO = new SurveyDTO();

        surveyDTO.setTitle(survey.getTitle());
        surveyDTO.setDescription(survey.getDescription());
        surveyDTO.setIsClosed(survey.getIsClosed());
        surveyDTO.setCreator(userConverter.convertUserToDTO(survey.getCreator()));

        if (survey.getChosenTimeslot() != null) {
            surveyDTO.setSelectedTimeslot(convertTimeslotToDTO(survey.getChosenTimeslot()));
        }

        surveyDTO.setTimeslots(getTimeslotsAsDTOs(survey));
        surveyDTO.setResponses(getResponsesAsDTOs(survey, currentUser));

        return surveyDTO;
    }

    private List<ResponseDTO> getResponsesAsDTOs(Survey survey, User currentUser) {
        return survey.getParticipations()
            .stream()
            .map(Participation::getResponse)
            .filter(Objects::nonNull)
            .map(response -> responseConverter.convertResponseToDTO(response, currentUser))
            .collect(Collectors.toList());
    }

    private List<TimeslotDTO> getTimeslotsAsDTOs(Survey survey) {
        return survey.getTimeslots()
            .stream()
            .map(this::convertTimeslotToDTO)
            .sorted(Comparator.comparing(TimeslotDTO::getStart))
            .collect(Collectors.toList());
    }
}
