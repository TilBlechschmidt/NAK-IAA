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
 * Test for {@link Response}
 *
 * @author Hans Rißer
 */
@DataJpaTest
public class ResponseTest {

    private static Response response;
    private static Long participationId;
    private static Long id;
    @Autowired
    private EntityManager entityManager;

    public static Response testResponse(EntityManager entityManager) {
        Participation participation = TestUtil.requireEntity(ParticipationTest::testParticipation, entityManager);
        participationId = participation.getId();
        return new Response(participation, new HashSet<>());
    }

    @AfterEach
    void tearDown() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCreate() {
        response = testResponse(entityManager);
        entityManager.persist(response);
        assertNotNull(id = response.getId());
    }

    @Test
    void testRead() {
        assumeThatCode(this::testCreate).doesNotThrowAnyException();

        response = null;
        response = entityManager.find(Response.class, id);

        assertEquals(new HashSet<>(), response.getResponseTimeslots());
        assertEquals(id, response.getId());
        assertEquals(participationId, response.getParticipation().getId());
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

        assertNotNull(entityManager.find(Response.class, id));
        entityManager.remove(response);
        assertNull(entityManager.find(Response.class, id));
    }
}
