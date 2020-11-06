package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.dao.model.QuerySurveyItem;
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

    private static final int NUMBER_OF_SURVEYS = 4;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private EntityManager entityManager;
    private QuerySurveyItem survey;
    private List<QuerySurveyItem> surveys;
    // CHECK data.sql for the TestData referenced here.

    @Test
    void saveTest() {
        Survey survey = SurveyTest.testSurvey(entityManager);
        assertNull(survey.getId());
        surveyRepository.save(survey);
        Long id = survey.getId();
        assertNotNull(id);
        assertEquals(entityManager.find(Survey.class, id), survey);
    }

    @Test
    void deleteByIdTest() {
        Survey survey = entityManager.find(Survey.class, 10L);
        assertNotNull(survey);
        surveyRepository.delete(survey);
        assertNull(entityManager.find(Survey.class, 10L));
        entityManager.persist(survey);
    }

    @Test
    void queryAll() {
        surveys = surveyRepository.querySurvey(null, null, null, null, null, null);
        assertEquals(NUMBER_OF_SURVEYS, surveys.size());
    }

    @Test
    void querySurveysThatNeedAttentionByTest() {
        surveys = surveyRepository.querySurvey(2L, null, null, null, null, true);
        assertEquals(1L, surveys.size());
        assertEquals(12L, surveys.get(0).getID());
        surveys = surveyRepository.querySurvey(2L, null, null, null, null, false);
        assertSurveysContainsAllWithoutID(12L);
    }

    @Test
    void queryUpcomingSurveysTest() {
        surveys = surveyRepository.querySurvey(null, null, null, null, true, null);
        assertEquals(1, surveys.size());
        assertEquals(13L, surveys.get(0).getID());
        surveys = surveyRepository.querySurvey(null, null, null, null, false, null);
        assertSurveysContainsAllWithoutID(13L);
    }

    @Test
    void querySurveysByParticipantTest() {
        surveys = surveyRepository.querySurvey(3L, true, null, null, null, null);
        assertEquals(2,surveys.size());
        assertContainsSurveyWithId(11L);
        assertContainsSurveyWithId(12L);
        surveys = surveyRepository.querySurvey(3L, false, null, null, null, null);
        assertEquals(2,surveys.size());
        assertContainsSurveyWithId(10L);
        assertContainsSurveyWithId(13L);
    }

    @Test
    void querySurveysByCompletionTest() {
        surveys = surveyRepository.querySurvey(null, null, true, null, null, null);
        assertEquals(1,surveys.size());
        assertContainsSurveyWithId(13L);
        surveys = surveyRepository.querySurvey(null, null, false, null, null, null);
        assertSurveysContainsAllWithoutID(13L);
    }

    @Test
    void querySurveysByCreator() {
        surveys = surveyRepository.querySurvey(0L, null, null, true, null, null);
        assertEquals(1,surveys.size());
        assertContainsSurveyWithId(10L);
        surveys = surveyRepository.querySurvey(0L, null, null, false, null, null);
        assertSurveysContainsAllWithoutID(10L);
    }

    @Test
    void queryCombinationsTest() {
        surveys = surveyRepository.querySurvey(2L, true, null, null, null, true);
        assertEquals(1L, surveys.size());
        assertEquals(12L, surveys.get(0).getID());
        surveys = surveyRepository.querySurvey(2L, true, null, null, null, false);
        assertEquals(2L, surveys.size());
        assertEquals(11L, surveys.get(0).getID());
        assertEquals(13L, surveys.get(1).getID());

        surveys = surveyRepository.querySurvey(2L, null, true, true, true, null);
        assertEquals(1L,surveys.size());
        assertEquals(13L, surveys.get(0).getID());

        surveys = surveyRepository.querySurvey(2L, null, false, true, null, null);
        assertEquals(0L,surveys.size());
    }

    @Test
    void findByIdTest() {
        Survey survey = surveyRepository.findById(10L);
        assertNotNull(survey);
        assertEquals(entityManager.find(Survey.class, 10L), survey);
    }

    private void printSurveys() {
        surveys.stream().map(QuerySurveyItem::getID).forEach(System.out::println);
    }

    private void assertSurveysContainsAllWithoutID(Long id) {
        surveys.stream().map(QuerySurveyItem::getID).forEach(System.out::println);
        assertEquals(NUMBER_OF_SURVEYS - 1, surveys.size());
        assertFalse(surveys.stream().anyMatch(s -> s.getID().equals(id)));
    }

    private void assertContainsSurveyWithId(Long id) {
        assertTrue(surveys.stream().anyMatch(survey -> survey.getID().equals(id)));

    }
}
