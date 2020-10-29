package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.SurveyTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SurveyRepositoryTest {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private EntityManager entityManager;
    private Survey survey;
    private List<Survey> surveys;

    // CHECK data.sql for the TestData referenced here.

    @Test
    public void findAllByCreator_idTest() {
        surveys = surveyRepository.findAllByCreator_id(2L);
        assertEquals(2, surveys.size());
        assertTrue(surveys.stream().anyMatch(survey -> survey.getId() == 5));
        assertTrue(surveys.stream().anyMatch(survey -> survey.getId() == 6));
    }

    @Test
    public void findSurveysWhereUserHasNotParticipatedTest() {
        surveys = surveyRepository.findSurveysWhereUserHasNotParticipated(3L);
        assertEquals(2, surveys.size());
        assertTrue(surveys.stream().anyMatch(s -> s.getId() == 5));
        assertTrue(surveys.stream().anyMatch(s -> s.getId() == 7));
    }

    @Test
    public void saveTest() {
        survey = SurveyTest.testSurvey(entityManager);
        assertNull(survey.getId());
        surveyRepository.save(survey);
        Long id = survey.getId();
        assertNotNull(id);
        assertEquals(entityManager.find(Survey.class, id), survey);
    }

    @Test
    public void deleteByIdTest() {
        survey = entityManager.find(Survey.class, 4L);
        assertNotNull(survey);
        surveyRepository.deleteById(4L);
        assertNull(entityManager.find(Survey.class, 4L));
        entityManager.persist(survey);
    }

    @Test
    public void findSurveysThatNeedAttentionByTest() {
        surveys = surveyRepository.findSurveysThatNeedAttentionBy(2L);
        assertEquals(1, surveys.size());
        assertEquals(4, surveys.get(0).getId());
    }

    @Test
    public void findByIdTest() {
        Survey survey = surveyRepository.findById(4L);
        assertNotNull(survey);
        assertEquals(entityManager.find(Survey.class, 4L), survey);
    }
}
