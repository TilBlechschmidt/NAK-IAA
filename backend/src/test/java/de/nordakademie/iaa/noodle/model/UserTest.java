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

@DataJpaTest
class UserTest {
    private static Long id;
    private static User user;
    @Autowired
    private EntityManager entityManager;

    public static User testUser() {
        return new User(new HashSet<>(), new HashSet<>(), "testName", "testHash", "testSalt");
    }

    @AfterEach
    void tearDown() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void create() {
        user = testUser();
        entityManager.persist(user);
        assertNotNull(id = user.getId());
    }

    @Test
    public void read() {
        assumeThatCode(this::create).doesNotThrowAnyException();

        user = null;
        user = entityManager.find(User.class, id);

        assertEquals(new HashSet<>(), user.getCreatedSurveys());
        assertEquals(new HashSet<>(), user.getParticipations());
        assertEquals("testName", user.getFullName());
        assertEquals("testHash", user.getPasswordHash());
        assertEquals("testSalt", user.getPasswordSalt());
    }

    @Test
    public void update() {
//        We have decided not to make entities updatable before it is necessary
//        This test will be used but skipped until that decision is made final.
        TestUtil.skip();

        assumeThatCode(this::create).doesNotThrowAnyException();
    }

    @Test
    public void delete() {
        assumeThatCode(this::create).doesNotThrowAnyException();

        assertNotNull(entityManager.find(User.class, id));
        entityManager.remove(user);
        assertNull(entityManager.find(User.class, id));
    }
}
