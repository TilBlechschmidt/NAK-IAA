package de.nordakademie.iaa.noodle.model;

import de.nordakademie.iaa.noodle.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Date;

import static org.assertj.core.api.Assumptions.assumeThatCode;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TimeslotTest {

    private static Timeslot timeslot;
    private static Long id;
    private static Long surveyId;

    @Autowired
    private EntityManager entityManager;

    public static Timeslot testTimeslot(EntityManager entityManager) {
        Survey survey = TestUtil.requireEntity(SurveyTest::testSurvey, entityManager);
        surveyId = survey.getId();
        return new Timeslot(survey, new Date(0), new Date(10000));
    }

    @AfterEach
    void tearDown() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testCreate() {
        timeslot = testTimeslot(entityManager);
        entityManager.persist(timeslot);
        assertNotNull(id = timeslot.getId());
    }

    @Test
    public void testRead() {
        assumeThatCode(this::testCreate).doesNotThrowAnyException();

        timeslot = null;
        timeslot = entityManager.find(Timeslot.class, id);

        assertEquals(id, timeslot.getId());
        assertEquals(surveyId, timeslot.getSurvey().getId());
        assertEquals(new Date(0), timeslot.getStart());
        assertEquals(new Date(10000), timeslot.getEnd());
    }

    @Test
    public void testUpdate() {
//        We have decided not to make entities updatable before it is necessary
//        This test will be used but skipped until that decision is made final.
        TestUtil.skip();

        assumeThatCode(this::testCreate).doesNotThrowAnyException();
    }

    @Test
    public void testDelete() {
        assumeThatCode(this::testCreate).doesNotThrowAnyException();

        assertNotNull(entityManager.find(Timeslot.class,id));
        entityManager.remove(timeslot);
        assertNull(entityManager.find(Timeslot.class, id));
    }
}
