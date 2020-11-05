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

//    @Test
//    void findAllByCreator_idTest() {
//        surveys = surveyRepository.findAllByCreator_id(2L);
//        assertEquals(2, surveys.size());
//        assertContainsSurveyWithId(5L);
//        assertContainsSurveyWithId(6L);
//    }

//    @Test
//    void findSurveysWhereUserHasNotParticipatedTest() {
//        surveys = surveyRepository.findSurveysWhereUserHasNotParticipated(3L);
//        assertEquals(2, surveys.size());
//        assertContainsSurveyWithId(5L);
//        assertContainsSurveyWithId(7L);
//    }

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
        Survey survey = entityManager.find(Survey.class, 4L);
        assertNotNull(survey);
        surveyRepository.delete(survey);
        assertNull(entityManager.find(Survey.class, 4L));
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
        assertEquals(4L, surveys.get(0).getID());
        surveys = surveyRepository.querySurvey(2L, null, null, null, null, false);
        assertSurveysContainsAllWithoutID(4L);
    }

    @Test
    void queryUpcomingSurveysTest() {
        surveys = surveyRepository.querySurvey(null, null, null, null, true, null);
        assertEquals(1, surveys.size());
        assertEquals(6L, surveys.get(0).getID());
        surveys = surveyRepository.querySurvey(null, null, null, null, false, null);
        assertSurveysContainsAllWithoutID(6L);
    }

    @Test
    void querySurveysByParticipantTest() {
        surveys = surveyRepository.querySurvey(1L, true, null, null, null, null);
        assertEquals(2,surveys.size());
        assertContainsSurveyWithId(4L);
        assertContainsSurveyWithId(5L);
        surveys = surveyRepository.querySurvey(1L, false, null, null, null, null);
        assertEquals(2,surveys.size());
        assertContainsSurveyWithId(6L);
        assertContainsSurveyWithId(7L);
    }

    @Test
    void querySurveysByCompletionTest() {
        surveys = surveyRepository.querySurvey(null, null, true, null, null, null);
        assertEquals(1,surveys.size());
        assertContainsSurveyWithId(6L);
        surveys = surveyRepository.querySurvey(null, null, false, null, null, null);
        assertSurveysContainsAllWithoutID(1L);
    }

    @Test
    void querySurveysByCreator() {
        surveys = surveyRepository.querySurvey(1L, null, null, true, null, null);
        assertEquals(1,surveys.size());
        assertContainsSurveyWithId(4L);
        surveys = surveyRepository.querySurvey(1L, null, null, false, null, null);
        assertSurveysContainsAllWithoutID(4L);
    }

    @Test
    void queryCombinationsTest() {
        surveys = surveyRepository.querySurvey(2L, true, null, null, null, true);
        assertEquals(1L, surveys.size());
        assertEquals(4L, surveys.get(0).getID());
        surveys = surveyRepository.querySurvey(2L, true, null, null, null, false);
        assertEquals(1L, surveys.size());
        assertEquals(5L, surveys.get(0).getID());

        surveys = surveyRepository.querySurvey(2L, null, true, true, true, null);
        assertEquals(1L,surveys.size());
        assertEquals(6L, surveys.get(0).getID());

        surveys = surveyRepository.querySurvey(2L, null, false, true, null, null);
        assertEquals(1L,surveys.size());
        assertEquals(5L, surveys.get(0).getID());
    }

    @Test
    void findByIdTest() {
        Survey survey = surveyRepository.findById(4L);
        assertNotNull(survey);
        assertEquals(entityManager.find(Survey.class, 4L), survey);
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
