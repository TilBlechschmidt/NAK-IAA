package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.model.ResponseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link ResponseRepository}
 *
 * @author Hans Rißer
 */
@DataJpaTest
class ResponseRepositoryTest {

    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private EntityManager entityManager;
    private Response response;

    // CHECK data.sql for the TestData referenced here.

    @Test
    void testFindByIdAndSurveyId() {
        response = responseRepository.findByIdAndSurveyId(50L, 10L);
        assertEquals(20L, response.getParticipation().getId());
    }

    @Test
    void testFindByIdAndSurveyIdMismatchedSurveyID() {
        response = responseRepository.findByIdAndSurveyId(50L, 50L);
        assertNull(response);
    }

    @Test
    void testSave() {
        response = ResponseTest.testResponse(entityManager);
        assertNull(response.getId());
        responseRepository.save(response);
        Long id = response.getId();
        assertNotNull(id);
        assertNotNull(entityManager.find(Response.class, id));
        assertEquals(response, entityManager.find(Response.class, id));
    }
}
