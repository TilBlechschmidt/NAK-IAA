package de.nordakademie.iaa.noodle.model;

import de.nordakademie.iaa.noodle.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assumptions.assumeThatCode;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ParticipationTest {

    private static Participation participation;
    private static Long userId;
    private static Long surveyId;
    private static Long id;
    @Autowired
    private EntityManager entityManager;

    public static Participation testParticipation(EntityManager entityManager) {
        User user = TestUtil.requireEntity(UserTest::testUser, entityManager);
        userId = user.getId();
        Survey survey = TestUtil.requireEntity(SurveyTest::testSurvey, entityManager);
        surveyId = survey.getId();
        return new Participation(user, survey, null);
    }

    @AfterEach
    void tearDown() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testCreate() {
        participation = testParticipation(entityManager);
        entityManager.persist(participation);
        assertNotNull(id = participation.getId());
    }

    @Test
    public void testRead() {
        assumeThatCode(this::testCreate).doesNotThrowAnyException();

        participation = null;
        participation = entityManager.find(Participation.class, id);

        assertEquals(id, participation.getId());
        assertEquals(userId, participation.getParticipant().getId());
        assertEquals(surveyId, participation.getSurvey().getId());
        assertNull(participation.getResponse());
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

        assertNotNull(entityManager.find(Participation.class, id));
        entityManager.remove(participation);
        assertNull(entityManager.find(Participation.class, id));
    }
}
