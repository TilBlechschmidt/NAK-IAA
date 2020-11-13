package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.dao.model.QuerySurveysItem;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.SurveyTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link SurveyRepository}
 *
 * @author Hans Rißer
 */
@DataJpaTest
class SurveyRepositoryTest {

    private static final int NUMBER_OF_SURVEYS = 4;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private EntityManager entityManager;
    private List<QuerySurveysItem> surveys;
    // CHECK data.sql for the TestData referenced here.

    @Test
    void testSave() {
        Survey survey = SurveyTest.testSurvey(entityManager);
        assertNull(survey.getId());
        surveyRepository.save(survey);
        Long id = survey.getId();
        assertNotNull(id);
        assertEquals(entityManager.find(Survey.class, id), survey);
    }

    @Test
    void testDeleteById() {
        Survey survey = entityManager.find(Survey.class, 10L);
        assertNotNull(survey);
        surveyRepository.delete(survey);
        assertNull(entityManager.find(Survey.class, 10L));
        entityManager.persist(survey);
    }

    @Test
    void testQueryAll() {
        surveys = surveyRepository.querySurveys(null, null, null, null, null, null, null);
        assertEquals(NUMBER_OF_SURVEYS, surveys.size());
    }

    @Test
    void testQuerySurveysThatNeedAttentionBy() {
        surveys = surveyRepository.querySurveys(2L, null, null, null, null, null, true);
        assertEquals(1L, surveys.size());
        assertEquals(12L, surveys.get(0).getId());
        surveys = surveyRepository.querySurveys(2L, null, null, null, null, null, false);
        assertSurveysContainsAllWithoutID(12L);
    }

    @Test
    void testQueryUpcomingSurveys() {
        surveys = surveyRepository.querySurveys(null, null, null, null, null, true, null);
        assertEquals(1, surveys.size());
        assertEquals(13L, surveys.get(0).getId());
        surveys = surveyRepository.querySurveys(null, null, null, null, null, false, null);
        assertSurveysContainsAllWithoutID(13L);
    }

    @Test
    void testQuerySurveysByParticipant() {
        surveys = surveyRepository.querySurveys(3L, null, true, null, null, null, null);
        assertEquals(2, surveys.size());
        assertContainsSurveyWithId(11L);
        assertContainsSurveyWithId(12L);
        surveys = surveyRepository.querySurveys(3L, null, false, null, null, null, null);
        assertEquals(2, surveys.size());
        assertContainsSurveyWithId(10L);
        assertContainsSurveyWithId(13L);
    }

    @Test
    void testQuerySurveysByCompletion() {
        surveys = surveyRepository.querySurveys(null, null, null, true, null, null, null);
        assertEquals(1, surveys.size());
        assertContainsSurveyWithId(13L);
        surveys = surveyRepository.querySurveys(null, null, null, false, null, null, null);
        assertSurveysContainsAllWithoutID(13L);
    }

    @Test
    void testQuerySurveysByAcceptsSelectedTimeslot() {
        surveys = surveyRepository.querySurveys(1L, true, null, null, null, null, null);
        assertEquals(1, surveys.size());
        assertContainsSurveyWithId(13L);
        surveys = surveyRepository.querySurveys(1L, false, null, null, null, null, null);
        assertSurveysContainsAllWithoutID(13L);
    }

    @Test
    void testQuerySurveysByCreator() {
        surveys = surveyRepository.querySurveys(0L, null, null, null, true, null, null);
        assertEquals(1, surveys.size());
        assertContainsSurveyWithId(10L);
        surveys = surveyRepository.querySurveys(0L, null, null, null, false, null, null);
        assertSurveysContainsAllWithoutID(10L);
    }

    @Test
    void testQueryCombinations() {
        surveys = surveyRepository.querySurveys(2L, null, true, null, null, null, true);
        assertEquals(1L, surveys.size());
        assertEquals(12L, surveys.get(0).getId());
        surveys = surveyRepository.querySurveys(2L, null, true, null, null, null, false);
        assertEquals(2L, surveys.size());
        assertEquals(11L, surveys.get(0).getId());
        assertEquals(13L, surveys.get(1).getId());

        surveys = surveyRepository.querySurveys(2L, null, null, true, true, true, null);
        assertEquals(1L, surveys.size());
        assertEquals(13L, surveys.get(0).getId());

        surveys = surveyRepository.querySurveys(2L, null, null, false, true, null, null);
        assertEquals(0L, surveys.size());
    }

    @Test
    void testFindById() {
        Survey survey = surveyRepository.findById(10L);
        assertNotNull(survey);
        assertEquals(entityManager.find(Survey.class, 10L), survey);
    }

    private void assertSurveysContainsAllWithoutID(Long id) {
        surveys.stream().map(QuerySurveysItem::getId).forEach(System.out::println);
        assertEquals(NUMBER_OF_SURVEYS - 1, surveys.size());
        assertFalse(surveys.stream().anyMatch(s -> s.getId().equals(id)));
    }

    private void assertContainsSurveyWithId(Long id) {
        assertTrue(surveys.stream().anyMatch(survey -> survey.getId().equals(id)));

    }
}
