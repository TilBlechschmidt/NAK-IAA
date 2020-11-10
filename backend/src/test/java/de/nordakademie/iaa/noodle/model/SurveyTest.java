package de.nordakademie.iaa.noodle.model;

import de.nordakademie.iaa.noodle.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.HashSet;

import static org.assertj.core.api.Assumptions.assumeThatCode;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link Survey}
 *
 * @author Hans Rißer
 */
@DataJpaTest
public
class SurveyTest {

    private static Long id;
    private static Long userId;
    private static Survey survey;

    @Autowired
    private EntityManager entityManager;

    public static Survey testSurvey(EntityManager entityManager) {
        User testUser = TestUtil.requireEntity(UserTest::testUser, entityManager);
        userId = testUser.getId();

        return new Survey(new HashSet<>(), null, testUser, new HashSet<>(), "title", "description");
    }

    @Test
    void testCreate() {
        survey = testSurvey(entityManager);

        entityManager.persist(survey);
        assertNotNull(id = survey.getId());
    }

    @Test
    void testRead() {
        assumeThatCode(this::testCreate).doesNotThrowAnyException();

        survey = null;
        survey = entityManager.find(Survey.class, id);

        assertEquals(new HashSet<>(), survey.getParticipations());
        assertEquals(new HashSet<>(), survey.getTimeslots());
        assertEquals(userId, survey.getCreator().getId());
        assertEquals("title", survey.getTitle());
        assertEquals("description", survey.getDescription());
        assertNull(survey.getSelectedTimeslot());
    }

    @Test
    void testUpdate() {
//        We have decided not to make entities updatable before it is necessary
//        This test will be used but skipped until that decision is made final.
        TestUtil.skip();

        assumeThatCode(this::testCreate).doesNotThrowAnyException();
    }

    @Test
    void testDelete() {
        assumeThatCode(this::testCreate).doesNotThrowAnyException();

        assertNotNull(entityManager.find(Survey.class, id));
        entityManager.remove(survey);
        assertNull(entityManager.find(Survey.class, id));
    }

    @AfterEach
    void tearDown() {
        entityManager.flush();
        entityManager.clear();
    }
}

