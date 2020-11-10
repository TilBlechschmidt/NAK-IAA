package de.nordakademie.iaa.noodle.model;

import de.nordakademie.iaa.noodle.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assumptions.assumeThatCode;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link ResponseTimeslot}
 *
 * @author Hans Rißer
 */
@DataJpaTest
class ResponseTimeslotTest {

    private static Long id;
    private static ResponseTimeslot responseTimeslot;
    private static Long responseId;
    private static Long timeslotId;

    @Autowired
    private EntityManager entityManager;

    public static ResponseTimeslot testResponseTimeslot(EntityManager entityManager) {
        Response response = TestUtil.requireEntity(ResponseTest::testResponse, entityManager);
        responseId = response.getId();
        Timeslot timeslot = TestUtil.requireEntity(TimeslotTest::testTimeslot, entityManager);
        timeslotId = timeslot.getId();
        return new ResponseTimeslot(response, timeslot, ResponseType.NO);
    }

    @AfterEach
    void tearDown() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCreate() {
        responseTimeslot = testResponseTimeslot(entityManager);
        entityManager.persist(responseTimeslot);
        assertNotNull(id = responseTimeslot.getId());
    }

    @Test
    public void read() {
        assumeThatCode(this::testCreate).doesNotThrowAnyException();

        responseTimeslot = null;
        responseTimeslot = entityManager.find(ResponseTimeslot.class, id);

        assertEquals(id, responseTimeslot.getId());
        assertEquals(ResponseType.NO, responseTimeslot.getResponseType());
        assertEquals(responseId, responseTimeslot.getResponse().getId());
        assertEquals(timeslotId, responseTimeslot.getTimeslot().getId());
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

        assertNotNull(entityManager.find(ResponseTimeslot.class, id));
        entityManager.remove(responseTimeslot);
        assertNull(entityManager.find(ResponseTimeslot.class, id));
    }
}
