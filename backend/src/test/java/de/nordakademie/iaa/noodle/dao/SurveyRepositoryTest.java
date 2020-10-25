package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.HashSet;

@DataJpaTest
class SurveyRepositoryTest {

    private final String email = "email@email.de";
    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Survey survey;

    @BeforeEach
    void setUp() {
        user = new User(new HashSet<>(), new HashSet<>(), email, "Mr. Email", "hash", "salt");
        userRepository.save(user);

        survey = new Survey(new HashSet<>(), user, new HashSet<>(), "My Survey", "");

        Timeslot timeslot = new Timeslot(survey, new Date(0), new Date(10000));
        survey.getTimeslots().add(timeslot);

        Participation participation = new Participation(user, survey, null);
        survey.getParticipations().add(participation);

        Response response = new Response(participation, new HashSet<>());
        participation.setResponse(response);

        ResponseTimeslot responseTimeslot = new ResponseTimeslot(response, timeslot, ResponseType.NO);
        response.getResponseTimeslots().add(responseTimeslot);

        survey.getParticipations().add(participation);
        surveyRepository.save(survey);
    }

    @Test
    void test() {
        user = userRepository.findByEmail(email);
        survey = surveyRepository.findByCreator(user).get(0);
        System.out.println("survey.getTitle() = " + survey.getTitle());
        System.out.println("survey.getParticipations() = " + survey.getParticipations());
        System.out.println("survey.getTimeslots() = " + survey.getTimeslots().iterator().next().getStart());
        System.out.println("survey.getParticipations().iterator().next().getResponse().getResponseTimeslots().iterator().next().getResponseType() = " + survey.getParticipations().iterator().next().getResponse().getResponseTimeslots().iterator().next().getResponseType());
        System.out.println("survey.getParticipations().iterator().next().getResponse().getResponseTimeslots().iterator().next().getTimeslot().getStart() = " + survey.getParticipations().iterator().next().getResponse().getResponseTimeslots().iterator().next().getTimeslot().getStart());
    }
}
